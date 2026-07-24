package sircow.preservedinferno.compat.mixin;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import snownee.jade.addon.vanilla.HorseStatsProvider;

@Mixin(HorseStatsProvider.class)
public abstract class HorseStatsProviderMixin {
    @Shadow @Final @Mutable private static double MAX_MOVEMENT_SPEED;

    @Overwrite
    private static double getSpeed(double speed) {
        return speed / 0.4633D * 20.0D;
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void pinferno$setMaxMovementSpeed(CallbackInfo ci) {
        MAX_MOVEMENT_SPEED = 20.0D;
    }
}
