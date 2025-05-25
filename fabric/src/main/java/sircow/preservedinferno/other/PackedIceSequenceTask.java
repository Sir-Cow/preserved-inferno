package sircow.preservedinferno.other;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import sircow.preservedinferno.PreservedInferno;

public class PackedIceSequenceTask extends DelayedBlockTransformationTask {
    private final int iceToAirDelay;
    private final int initialPackedToIceDelay;

    public PackedIceSequenceTask(PreservedInferno modInstance, ServerLevel serverLevel, BlockPos pos, int packedToIceDelay, int iceToAirDelay) {
        super(modInstance, serverLevel, pos, Blocks.PACKED_ICE.defaultBlockState(), Blocks.PACKED_ICE, packedToIceDelay);
        this.iceToAirDelay = iceToAirDelay;
        this.initialPackedToIceDelay = packedToIceDelay;
    }

    @Override
    protected int getMaxDelayTicks() {
        return initialPackedToIceDelay;
    }

    @Override
    public DelayedBlockTransformationTask transformBlock() {
        return new PackedIceToIceTask(modInstance, serverLevel, pos, 0, iceToAirDelay);
    }
}
