package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.DoorInteractGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DoorInteractGoal.class)
public abstract class DoorInteractGoalMixin {
    @Shadow protected Mob mob;

    @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$allowFenceGates(CallbackInfoReturnable<Boolean> cir) {
        if (!(mob instanceof Zombie)) return;

        BlockState state = mob.level().getBlockState(mob.blockPosition());

        boolean isDoorOrGate = state.getBlock() instanceof DoorBlock || state.getBlock() instanceof FenceGateBlock || mob.level().getBlockState(mob.blockPosition().above()).getBlock() instanceof DoorBlock;

        if (isDoorOrGate) {
            cir.setReturnValue(true);
        }
    }
}
