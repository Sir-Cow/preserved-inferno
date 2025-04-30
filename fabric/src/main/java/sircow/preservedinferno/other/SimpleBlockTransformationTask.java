package sircow.preservedinferno.other;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.PreservedInferno;

public class SimpleBlockTransformationTask extends DelayedBlockTransformationTask {
    private final Block expectedOriginalBlock;

    public SimpleBlockTransformationTask(PreservedInferno modInstance, ServerLevel serverLevel, BlockPos pos, BlockState newState, Block expectedOriginalBlock, int delayTicks) {
        super(modInstance, serverLevel, pos, newState, delayTicks);
        this.expectedOriginalBlock = expectedOriginalBlock;
    }

    @Override
    public DelayedBlockTransformationTask transformBlock() {
        if (serverLevel.getBlockState(pos).is(expectedOriginalBlock)) {
            serverLevel.setBlockAndUpdate(pos, newState);
        }
        else {
            Constants.LOG.warn("Block at {} was not the expected original type ({}) when transformation task executed. Current block: {}", pos, expectedOriginalBlock, serverLevel.getBlockState(pos).getBlock());
        }
        return null;
    }
}
