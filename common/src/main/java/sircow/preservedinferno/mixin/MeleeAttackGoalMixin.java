package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MeleeAttackGoal.class)
public abstract class MeleeAttackGoalMixin {
    @Shadow @Final protected PathfinderMob mob;

    @Inject(method = "canPerformAttack", at = @At("HEAD"), cancellable = true)
    private void pinferno$zombieAttackLineOfSight(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        if (!(this.mob instanceof Zombie)) return;

        Vec3 start = this.mob.getEyePosition();
        Vec3 end = target.getBoundingBox().getCenter();

        HitResult hitResult = this.mob.level().clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.mob));

        if (hitResult.getType() != HitResult.Type.MISS) cir.setReturnValue(false);
    }
}
