package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.projectile.ThrownTrident;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ThrownTrident.class)
public class ThrownTridentMixin {
    // modify thrown trident damage
    @ModifyVariable(method = "onHitEntity", at = @At("STORE"), ordinal = 0)
    private float preserved_inferno$modifyDamage(float originalValue) {
        return 10.0F;
    }
}
