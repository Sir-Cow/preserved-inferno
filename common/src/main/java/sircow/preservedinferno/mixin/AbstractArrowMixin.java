package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.Captures;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin {
    @Shadow private double baseDamage;

    @Unique
    private static void preserved_inferno$catchArrowBaseDamage(double damage) {
        Captures.arrowBaseDamage = damage;
    }

    @Inject(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V", at = @At("TAIL"))
    private void catchAndApply(CallbackInfo ci) {
        preserved_inferno$catchArrowBaseDamage(baseDamage);
    }
}
