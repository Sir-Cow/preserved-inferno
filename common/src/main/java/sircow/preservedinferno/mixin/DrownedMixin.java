package sircow.preservedinferno.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.zombie.Drowned;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.entity.custom.ThrownCopperTrident;
import sircow.preservedinferno.item.ModItems;

@Mixin(Drowned.class)
public abstract class DrownedMixin extends Zombie implements RangedAttackMob {
    public DrownedMixin(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "createAttributes", at = @At("RETURN"), cancellable = true)
    private static void pinferno$overwriteAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.setReturnValue(Zombie.createAttributes()
                .add(Attributes.STEP_HEIGHT, 1.0)
                .add(Attributes.ARMOR, 10.0));
    }

    @Inject(method = "populateDefaultEquipmentSlots", at = @At("HEAD"), cancellable = true)
    private void pinferno$modifyEquipment(RandomSource random, DifficultyInstance difficulty, CallbackInfo ci) {
        float roll = random.nextFloat();

        if (roll < 0.15F) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.COPPER_TRIDENT.get()));
            ci.cancel();
        }
        else if (roll < 0.15F + 0.0625F) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.FISHING_ROD));
            ci.cancel();
        }
        else {
            ci.cancel();
        }
    }

    @Inject(method = "performRangedAttack", at = @At("HEAD"), cancellable = true)
    private void pinferno$changeTrident(LivingEntity target, float distanceFactor, CallbackInfo ci) {
        ItemStack itemStack = this.getMainHandItem();
        ItemStack itemStack2 = itemStack.is(ModItems.COPPER_TRIDENT.get()) ? itemStack : new ItemStack(ModItems.COPPER_TRIDENT.get());
        ThrownCopperTrident thrownTrident = new ThrownCopperTrident(this.level(), this, itemStack2);
        double d = target.getX() - this.getX();
        double e = target.getY(0.3333333333333333) - thrownTrident.getY();
        double f = target.getZ() - this.getZ();
        double g = Math.sqrt(d * d + f * f);
        if (this.level() instanceof ServerLevel serverLevel) {
            Projectile.spawnProjectileUsingShoot(thrownTrident, serverLevel, itemStack2, d, e + g * 0.2F, f, 1.6F, 14 - this.level().getDifficulty().getId() * 4);
        }

        this.playSound(SoundEvents.DROWNED_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        ci.cancel();
    }
}
