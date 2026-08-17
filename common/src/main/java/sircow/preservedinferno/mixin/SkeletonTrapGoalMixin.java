package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.animal.equine.SkeletonHorse;
import net.minecraft.world.entity.animal.equine.SkeletonTrapGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SkeletonTrapGoal.class)
public class SkeletonTrapGoalMixin {
    @Redirect(method = {"tick", "createHorse"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/equine/SkeletonHorse;setTamed(Z)V"))
    private void pinferno$keepAmbushHorsesUntamed(SkeletonHorse horse, boolean tamed) {}
}
