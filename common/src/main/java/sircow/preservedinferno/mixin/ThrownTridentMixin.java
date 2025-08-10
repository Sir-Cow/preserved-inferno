package sircow.preservedinferno.mixin;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTrident.class)
public abstract class ThrownTridentMixin extends AbstractArrow {
    @Shadow @Final private static EntityDataAccessor<Byte> ID_LOYALTY;
    @Shadow private boolean dealtDamage = false;
    @Unique private Vec3 initialPos = null;

    protected ThrownTridentMixin(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    // modify thrown trident damage
    @ModifyVariable(method = "onHitEntity", at = @At("STORE"), ordinal = 0)
    private float preserved_inferno$modifyDamage(float originalValue) {
        return 10.0F;
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
}
