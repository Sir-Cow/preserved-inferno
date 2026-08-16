package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.RunAroundLikeCrazyGoal;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RunAroundLikeCrazyGoal.class)
public class RunAroundLikeCrazyGoalMixin {
    @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
    private void pinferno$noCrazyForNonPlayerRiders(CallbackInfoReturnable<Boolean> cir) {
        AbstractHorse horse = ((RunAroundLikeCrazyGoalAccessor) this).pinferno$getHorse();
        Entity passenger = horse.getFirstPassenger();
        if (passenger != null && !(passenger instanceof Player)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "canContinueToUse", at = @At("HEAD"), cancellable = true)
    private void pinferno$stopCrazyForNonPlayerRiders(CallbackInfoReturnable<Boolean> cir) {
        AbstractHorse horse = ((RunAroundLikeCrazyGoalAccessor) this).pinferno$getHorse();
        Entity passenger = horse.getFirstPassenger();
        if (passenger != null && !(passenger instanceof Player)) {
            cir.setReturnValue(false);
        }
    }
}
