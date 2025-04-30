package sircow.preservedinferno.other;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import sircow.preservedinferno.PreservedInferno;

public class PackedIceSequenceTask extends DelayedBlockTransformationTask {
    private final int iceToAirDelay;

    public PackedIceSequenceTask(PreservedInferno modInstance, ServerLevel serverLevel, BlockPos pos, int packedToIceDelay, int iceToAirDelay) {
        super(modInstance, serverLevel, pos, Blocks.PACKED_ICE.defaultBlockState(), packedToIceDelay);
        this.iceToAirDelay = iceToAirDelay;
    }

    @Override
    public DelayedBlockTransformationTask transformBlock() {
        return new PackedIceToIceTask(modInstance, serverLevel, pos, 0, iceToAirDelay);
    }
}
