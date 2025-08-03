package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.NewMinecartBehavior;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.block.ModBlocks;

@Mixin(NewMinecartBehavior.class)
public abstract class NewMinecartBehaviorMixin {
    @Inject(method = "calculateBoostTrackSpeed", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$railSpeed(Vec3 speed, BlockPos pos, BlockState state, CallbackInfoReturnable<Vec3> cir) {
        double speedLen = speed.length();

        if (speedLen <= 0.01) return;

        double boost; // default boost
        double maxSpeed; // default max (8 blocks/sec = 0.4 blocks/tick)

        if (state.is(ModBlocks.OXIDIZED_INDUCTOR_RAIL) || state.is(ModBlocks.WAXED_OXIDIZED_INDUCTOR_RAIL)) {
            boost = 0.06;
            maxSpeed = 0.4;
        }
        else if (state.is(ModBlocks.WEATHERED_INDUCTOR_RAIL) || state.is(ModBlocks.WAXED_WEATHERED_INDUCTOR_RAIL)) {
            boost = 0.12;
            maxSpeed = 0.8;
        }
        else if (state.is(ModBlocks.EXPOSED_INDUCTOR_RAIL) || state.is(ModBlocks.WAXED_EXPOSED_INDUCTOR_RAIL)) {
            boost = 0.18;
            maxSpeed = 1.2;
        }
        else if (state.is(ModBlocks.INDUCTOR_RAIL) || state.is(ModBlocks.WAXED_INDUCTOR_RAIL)) {
            boost = 0.24;
            maxSpeed = 1.6;
        }
        else if (state.is(Blocks.POWERED_RAIL) && state.getValue(PoweredRailBlock.POWERED)) {
            boost = 0.03;
            maxSpeed = 3.2;
        }
        else {
            return;
        }

        cir.setReturnValue(speed.normalize().scale(Math.min(speedLen + boost, maxSpeed)));
    }
}
