package sircow.preservedinferno.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
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

import java.util.function.Predicate;

@Mixin(Zombie.class)
public class ZombieMixin extends Monster {
    protected ZombieMixin(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Mutable @Shadow @Final private static Predicate<Difficulty> DOOR_BREAKING_PREDICATE;

    @Inject(method = "createAttributes", at = @At("RETURN"), cancellable = true)
    private static void preserved_inferno$overwriteAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.setReturnValue(Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 40.0)
                .add(Attributes.MOVEMENT_SPEED, 0.23F)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.ARMOR, 10.0)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE)
        );
    }

    @Inject(method = "setBaby", at = @At("TAIL"))
    private void preserved_inferno$modifyBaby(boolean baby, CallbackInfo ci) {
        if (this.level().isClientSide()) return;

        AttributeInstance health = this.getAttribute(Attributes.MAX_HEALTH);
        if (health == null) return;

        if (baby) {
            health.setBaseValue(health.getBaseValue() * 0.75);
        }
        else {
            health.setBaseValue(health.getBaseValue());
        }
        this.setHealth(this.getMaxHealth());
    }

    @Inject(method = "killedEntity", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$alwaysConvertVillagers(ServerLevel level, LivingEntity entity, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        Zombie self = (Zombie) (Object) this;

        if (entity instanceof Villager villager) {
            boolean converted = self.convertVillagerToZombieVillager(level, villager);
            cir.setReturnValue(!converted);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void preserved_inferno$forceCanBreakDoors(ValueInput input, CallbackInfo ci) {
        Zombie self = (Zombie) (Object) this;
        self.setCanBreakDoors(true);
    }

    @Redirect(method = "finalizeSpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextFloat()F", ordinal = 1))
    private float preserved_inferno$alwaysAllowDoorBreaking(RandomSource randomSource) {
        return 0.0F;
    }

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void preserved_inferno$forceRemoveDifficultyCheck(CallbackInfo ci) {
        DOOR_BREAKING_PREDICATE = difficulty -> true;
    }
}
