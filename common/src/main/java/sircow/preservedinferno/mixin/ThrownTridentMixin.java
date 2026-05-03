package sircow.preservedinferno.mixin;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ThrownTrident.class)
public abstract class ThrownTridentMixin extends AbstractArrow {
    @Shadow @Final private static EntityDataAccessor<Byte> ID_LOYALTY;
    @Shadow private boolean dealtDamage;
    @Unique private Vec3 initialPos;

    protected ThrownTridentMixin(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    // modify thrown trident damage
    @ModifyVariable(method = "onHitEntity", at = @At("STORE"), ordinal = 0)
    private float preserved_inferno$modifyDamage(float originalValue) {
        return 12.0F;
    }

    // cap distance loyalty trident can be thrown
    @Inject(method = "tick", at = @At("HEAD"))
    private void preserved_inferno$captureInitialPos(CallbackInfo ci) {
        if (initialPos == null) {
            initialPos = this.position();
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void preserved_inferno$checkDistanceAndForceReturn(CallbackInfo ci) {
        if (!dealtDamage && initialPos != null) {
            byte loyalty = this.entityData.get(ID_LOYALTY);
            if (loyalty > 0) {
                double distanceTravelled = this.position().distanceTo(initialPos);
                double maxDistance = 60.0;
                if (distanceTravelled > maxDistance) {
                    this.dealtDamage = true;
                }
            }
        }
    }

    // trigger channeled lightning here because it won't work when one-hitting
    @Inject(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurtOrSimulate(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private void preserved_inferno$triggerChanneledLightning(EntityHitResult result, CallbackInfo ci) {
        ThrownTrident trident = (ThrownTrident) (Object) this;
        Entity victim = result.getEntity();

        if (trident.level() instanceof ServerLevel serverLevel) {
            if (trident.getOwner() instanceof ServerPlayer player) {
                if (EnchantmentHelper.getItemEnchantmentLevel(serverLevel.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.CHANNELING), trident.getWeaponItem()) > 0 && serverLevel.isThundering() && serverLevel.canSeeSky(victim.blockPosition())) {
                    CriteriaTriggers.CHANNELED_LIGHTNING.trigger(player, List.of(victim));
                }
            }
        }
    }
}
