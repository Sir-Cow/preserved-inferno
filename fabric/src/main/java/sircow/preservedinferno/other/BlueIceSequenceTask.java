package sircow.preservedinferno.other;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import sircow.preservedinferno.PreservedInferno;

public class BlueIceSequenceTask extends DelayedBlockTransformationTask {
    private final int packedToIceDelay;
    private final int iceToAirDelay;
    private final int initialBlueToPackedDelay;

    public BlueIceSequenceTask(PreservedInferno modInstance, ServerLevel serverLevel, BlockPos pos, int blueToPackedDelay, int packedToIceDelay, int iceToAirDelay) {
        super(modInstance, serverLevel, pos, Blocks.BLUE_ICE.defaultBlockState(), Blocks.BLUE_ICE, blueToPackedDelay);
        this.packedToIceDelay = packedToIceDelay;
        this.iceToAirDelay = iceToAirDelay;
        this.initialBlueToPackedDelay = blueToPackedDelay;
    }

    @Override
    protected int getMaxDelayTicks() {
        return initialBlueToPackedDelay;
    }

    @Override
    public DelayedBlockTransformationTask transformBlock() {
        return new BlueIceToPackedIceTask(modInstance, serverLevel, pos, 0, packedToIceDelay, iceToAirDelay);
    }
}
