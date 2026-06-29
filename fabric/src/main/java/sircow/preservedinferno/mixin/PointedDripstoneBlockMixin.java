package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.block.entity.PreservedCauldronBlockEntity;
import sircow.preservedinferno.fluid.CauldronFluid;
import sircow.preservedinferno.other.DripstoneHoneyHelper;

@Mixin(PointedDripstoneBlock.class)
public class PointedDripstoneBlockMixin {
    @Inject(method = "maybeTransferFluid", at = @At("HEAD"), cancellable = true)
    private static void pinferno$tryHoneyTransfer(BlockState state, ServerLevel level, BlockPos pos, float randomValue, CallbackInfo ci) {
        if (!DripstoneHoneyHelper.hasHoneySource(level, pos, state)) return;

        BlockPos tipPos = DripstoneHoneyHelper.findTip(state, level, pos, 11, false);
        if (tipPos == null) return;

        BlockPos cauldronPos = DripstoneHoneyHelper.findHoneyCauldronBelowTip(level, tipPos);
        if (cauldronPos == null) return;

        if (level.getBlockEntity(cauldronPos) instanceof PreservedCauldronBlockEntity cauldron) {
            if (cauldron.fluid == CauldronFluid.EMPTY || cauldron.fluid == CauldronFluid.HONEY) {
                BlockState cauldronState = level.getBlockState(cauldronPos);
                int oldAmount = cauldron.fluidAmount;

                cauldron.fluid = CauldronFluid.HONEY;
                cauldron.fluidAmount = Math.min(cauldron.maxFluidAmount, cauldron.fluidAmount + 1);

                if (cauldron.fluidAmount != oldAmount) {
                    cauldron.setChanged();
                    level.sendBlockUpdated(cauldronPos, cauldronState, cauldronState, 3);
                }

                level.levelEvent(1504, tipPos, 0);
                ci.cancel();
            }
        }
    }

    @Inject(method = "animateTick", at = @At("HEAD"), cancellable = true)
    private void pinferno$honeyParticleDrip(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (DripstoneHoneyHelper.hasHoneySource(level, pos, state)) {
            float randomValue = random.nextFloat();
            if (randomValue < 0.12F) {
                BlockPos tipPos = DripstoneHoneyHelper.findTip(state, level, pos, 11, false);
                if (tipPos != null && tipPos.equals(pos)) {
                    Vec3 offset = state.getOffset(pos);
                    double x = pos.getX() + 0.5 + offset.x;
                    double y = pos.getY() + 0.1875;
                    double z = pos.getZ() + 0.5 + offset.z;

                    level.addParticle(ParticleTypes.DRIPPING_HONEY, x, y, z, 0.0, 0.0, 0.0);
                }
            }
            ci.cancel();
        }
    }
}
