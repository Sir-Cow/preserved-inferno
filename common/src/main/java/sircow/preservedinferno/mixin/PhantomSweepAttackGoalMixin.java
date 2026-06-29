package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@Mixin(targets = "net.minecraft.world.entity.monster.Phantom$PhantomSweepAttackGoal")
public class PhantomSweepAttackGoalMixin {

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void pinferno$cancelSwoopNearEyeblossom(CallbackInfo ci) {
        Goal goal = (Goal)(Object)this;
        try {
            Field outerField = goal.getClass().getDeclaredField("this$0");
            outerField.setAccessible(true);
            Phantom phantom = (Phantom) outerField.get(goal);

            if (phantom != null && !phantom.level().isClientSide()) {
                BlockPos pos = phantom.blockPosition();
                BlockPos nearest = BlockPos.findClosestMatch(pos, 7, 7, p -> phantom.level().getBlockState(p).is(Blocks.OPEN_EYEBLOSSOM)).orElse(null);

                if (nearest != null) {
                    phantom.setTarget(null);

                    Vec3 phantomPos = phantom.position();
                    Vec3 flowerPos = Vec3.atCenterOf(nearest);
                    Vec3 away = phantomPos.subtract(flowerPos);

                    if (away.x == 0 && away.z == 0) {
                        away = away.add(phantom.getRandom().nextDouble() * 0.1, 0, phantom.getRandom().nextDouble() * 0.1);
                    }

                    double horizontalDistance = Math.sqrt(away.x * away.x + away.z * away.z);
                    double dx = (away.x / horizontalDistance) * 5.0;
                    double dz = (away.z / horizontalDistance) * 5.0;
                    double dy = 6.0;

                    Field moveTargetField = Phantom.class.getDeclaredField("moveTargetPoint");
                    moveTargetField.setAccessible(true);
                    moveTargetField.set(phantom, phantomPos.add(dx, dy, dz));
                    ci.cancel();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
