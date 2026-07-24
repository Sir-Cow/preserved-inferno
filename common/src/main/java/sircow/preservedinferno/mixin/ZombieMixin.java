package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.other.BabyHealthHelper;

import java.util.Objects;
import java.util.function.Predicate;

@Mixin(Zombie.class)
public class ZombieMixin extends Monster {
    protected ZombieMixin(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Mutable @Shadow @Final private static Predicate<Difficulty> DOOR_BREAKING_PREDICATE;

    @Shadow protected void randomizeReinforcementsChance() {
        Objects.requireNonNull(this.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE)).setBaseValue(this.random.nextDouble() * 0.1F);
    }
    @Inject(method = "createAttributes", at = @At("RETURN"), cancellable = true)
    private static void pinferno$overwriteAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.setReturnValue(Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 40.0)
                .add(Attributes.MOVEMENT_SPEED, 0.23F)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.ARMOR, 10.0)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE)
        );
    }

    @Inject(method = "setBaby", at = @At("TAIL"))
    private void pinferno$modifyBaby(boolean baby, CallbackInfo ci) {
        BabyHealthHelper.updateBabyHealth(this, baby);
    }

    @Inject(method = "killedEntity", at = @At("HEAD"), cancellable = true)
    private void pinferno$alwaysConvertVillagers(ServerLevel level, LivingEntity entity, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        Zombie self = (Zombie) (Object) this;

        if (entity instanceof Villager villager) {
            boolean converted = self.convertVillagerToZombieVillager(level, villager);
            cir.setReturnValue(!converted);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void pinferno$forceCanBreakDoors(ValueInput input, CallbackInfo ci) {
        Zombie self = (Zombie) (Object) this;
        self.setCanBreakDoors(true);
    }

    @Redirect(method = "finalizeSpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextFloat()F", ordinal = 1))
    private float pinferno$alwaysAllowDoorBreaking(RandomSource randomSource) {
        return 0.0F;
    }

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void pinferno$forceRemoveDifficultyCheck(CallbackInfo ci) {
        DOOR_BREAKING_PREDICATE = difficulty -> true;
    }

    @Inject(method = "handleAttributes", at = @At("HEAD"), cancellable = true)
    private void pinferno$boostLowYLeaderChance(float difficultyModifier, EntitySpawnReason spawnReason, CallbackInfo ci) {
        Zombie self = (Zombie) (Object) this;
        BlockPos pos = self.blockPosition();
        boolean isLowY = pos.getY() <= 0;

        randomizeReinforcementsChance();
        Objects.requireNonNull(self.getAttribute(Attributes.KNOCKBACK_RESISTANCE))
                .addOrReplacePermanentModifier(new AttributeModifier(
                        Identifier.withDefaultNamespace("zombie_random_spawn_bonus"),
                        self.getRandom().nextDouble() * 0.05F,
                        AttributeModifier.Operation.ADD_VALUE));

        double d = self.getRandom().nextDouble() * 1.5 * difficultyModifier;
        if (d > 1.0) {
            Objects.requireNonNull(self.getAttribute(Attributes.FOLLOW_RANGE))
                    .addOrReplacePermanentModifier(new AttributeModifier(
                            Identifier.withDefaultNamespace("zombie_random_spawn_bonus"),
                            d,
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }

        if (self.getRandom().nextFloat() < difficultyModifier * (isLowY ? 0.15F : 0.05F)) {
            Objects.requireNonNull(self.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE))
                    .addOrReplacePermanentModifier(new AttributeModifier(
                            Identifier.withDefaultNamespace("leader_zombie_bonus"),
                            self.getRandom().nextDouble() * 0.25 + 0.5,
                            AttributeModifier.Operation.ADD_VALUE));
            Objects.requireNonNull(self.getAttribute(Attributes.MAX_HEALTH))
                    .addOrReplacePermanentModifier(new AttributeModifier(
                            Identifier.withDefaultNamespace("leader_zombie_bonus"),
                            self.getRandom().nextDouble() * 3.0 + 1.0,
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            self.setCanBreakDoors(true);
        }

        if (isLowY) {
            ci.cancel();
        }
    }

    @Inject(method = "populateDefaultEquipmentSlots", at = @At("HEAD"), cancellable = true)
    private void pinferno$boostLowYWeapons(RandomSource random, DifficultyInstance difficulty, CallbackInfo ci) {
        Zombie self = (Zombie) (Object) this;

        float equipChance = switch (self.level().getDifficulty()) {
            case EASY -> 0.10F;
            case NORMAL -> 0.20F;
            case HARD -> self.blockPosition().getY() < 0 ? 0.60F : 0.30F;
            default -> 0.0F;
        };

        if (random.nextFloat() < equipChance) {
            float roll = random.nextFloat();

            ItemStack weapon;

            if (roll < 0.07F) weapon = new ItemStack(Items.STONE_SPEAR);
            else if (roll < 0.14F) weapon = new ItemStack(Items.STONE_SHOVEL);
            else if (roll < 0.28F) weapon = new ItemStack(Items.STONE_SWORD);
            else if (roll < 0.405F) weapon = new ItemStack(Items.COPPER_SPEAR);
            else if (roll < 0.53F) weapon = new ItemStack(Items.COPPER_SHOVEL);
            else if (roll < 0.78F) weapon = new ItemStack(Items.COPPER_SWORD);
            else if (roll < 0.83F) weapon = new ItemStack(Items.IRON_SPEAR);
            else if (roll < 0.88F) weapon = new ItemStack(Items.IRON_SHOVEL);
            else if (roll < 0.98F) weapon = new ItemStack(Items.IRON_SWORD);
            else if (roll < 0.985F) weapon = new ItemStack(Items.DIAMOND_SPEAR);
            else if (roll < 0.99F) weapon = new ItemStack(Items.DIAMOND_SHOVEL);
            else weapon = new ItemStack(Items.DIAMOND_SWORD);

            self.setItemSlot(EquipmentSlot.MAINHAND, weapon);
        }
        ci.cancel();
    }
}
