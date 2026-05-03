package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathfindingContext;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WalkNodeEvaluator.class)
public class WalkNodeEvaluatorMixin {
    @Inject(method = "getPathTypeOfMob", at = @At("RETURN"), cancellable = true)
    private void preserved_inferno$zombieFenceGateAsDoor(PathfindingContext context, int x, int y, int z, Mob mob, CallbackInfoReturnable<PathType> cir) {
        if (!(mob instanceof Zombie)) return;

        PathType original = cir.getReturnValue();
        if (original != PathType.FENCE) return;

        BlockState state = context.level().getBlockState(new BlockPos(x, y, z));
        if (state.getBlock() instanceof FenceGateBlock && !state.getValue(FenceGateBlock.OPEN)) {
            cir.setReturnValue(PathType.DOOR_WOOD_CLOSED);
        }
    }
}

