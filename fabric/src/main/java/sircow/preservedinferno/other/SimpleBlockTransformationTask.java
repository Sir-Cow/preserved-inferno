package sircow.preservedinferno.other;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.PreservedInferno;

public class SimpleBlockTransformationTask extends DelayedBlockTransformationTask {
    private final Block expectedOriginalBlock;
    private final int initialDelay;

    public SimpleBlockTransformationTask(PreservedInferno modInstance, ServerLevel serverLevel, BlockPos pos, BlockState newState, Block expectedOriginalBlock, int delayTicks) {
        super(modInstance, serverLevel, pos, newState, expectedOriginalBlock, delayTicks);
        this.expectedOriginalBlock = expectedOriginalBlock;
        this.initialDelay = delayTicks;
    }

    @Override
    protected int getMaxDelayTicks() {
        return initialDelay;
    }

    @Override
    public DelayedBlockTransformationTask transformBlock() {
        if (!isBlockValid()) {
            removeNoDropBlock(pos);
            return null;
        }

        if (serverLevel.getBlockState(pos).is(expectedOriginalBlock)) {
            serverLevel.setBlockAndUpdate(pos, newState);
            if (newState.is(Blocks.AIR)) {
                meltEffects();
            }
        }
        else {
            Constants.LOG.warn("Block at {} was not the expected original type ({}) when transformation task executed. Current block: {}. Please report this!", pos, expectedOriginalBlock, serverLevel.getBlockState(pos).getBlock());
        }
        removeBreakingAnimation();
        removeNoDropBlock(pos);
        return null;
    }

    private void meltEffects() {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();

        // extinguish sound
        serverLevel.playSound(
                null,
                pos,
                SoundEvents.FIRE_EXTINGUISH,
                SoundSource.BLOCKS,
                0.5F,
                2.6F + (serverLevel.random.nextFloat() - serverLevel.random.nextFloat()) * 0.8F
        );

        // smoke particles
        serverLevel.sendParticles(
                ParticleTypes.LARGE_SMOKE,
                i + Math.random(), j + Math.random(), k + Math.random(),
                5,
                0.5, 0.0, 0.5,
                0.0
        );
    }
}
