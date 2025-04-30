package sircow.preservedinferno.other;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.PreservedInferno;

public abstract class DelayedBlockTransformationTask {
    protected final ServerLevel serverLevel;
    protected final BlockPos pos;
    protected final BlockState newState;
    protected int delayTicks;
    protected final PreservedInferno modInstance;

    public DelayedBlockTransformationTask(PreservedInferno modInstance, ServerLevel serverLevel, BlockPos pos, BlockState newState, int delayTicks) {
        this.modInstance = modInstance;
        this.serverLevel = serverLevel;
        this.pos = pos;
        this.newState = newState;
        this.delayTicks = delayTicks;
    }

    public void tick() {
        delayTicks--;
    }

    public abstract DelayedBlockTransformationTask transformBlock();

    public boolean isFinished() {
        return delayTicks <= 0;
    }

    public ServerLevel getServerLevel() {
        return serverLevel;
    }

    public BlockPos getPos() {
        return pos;
    }

    public BlockState getNewState() {
        return newState;
    }

    public int getDelayTicks() {
        return delayTicks;
    }
}

class PackedIceToIceTask extends DelayedBlockTransformationTask {
    private final int iceToAirDelay;

    public PackedIceToIceTask(PreservedInferno modInstance, ServerLevel serverLevel, BlockPos pos, int packedToIceDelay, int iceToAirDelay) {
        super(modInstance, serverLevel, pos, Blocks.ICE.defaultBlockState(), packedToIceDelay);
        this.iceToAirDelay = iceToAirDelay;
    }

    @Override
    public DelayedBlockTransformationTask transformBlock() {
        if (serverLevel.getBlockState(pos).is(Blocks.PACKED_ICE)) {
            serverLevel.setBlockAndUpdate(pos, newState);
            return new SimpleBlockTransformationTask(modInstance, serverLevel, pos, Blocks.AIR.defaultBlockState(), Blocks.ICE, iceToAirDelay);
        } else {
            Constants.LOG.warn("Block at {} was not Packed Ice when Ice transformation task executed.", pos);
            return null;
        }
    }
}

class BlueIceToPackedIceTask extends DelayedBlockTransformationTask {
    private final int packedToIceDelay;
    private final int iceToAirDelay;

    public BlueIceToPackedIceTask(PreservedInferno modInstance, ServerLevel serverLevel, BlockPos pos, int blueToPackedDelay, int packedToIceDelay, int iceToAirDelay) {
        super(modInstance, serverLevel, pos, Blocks.PACKED_ICE.defaultBlockState(), blueToPackedDelay);
        this.packedToIceDelay = packedToIceDelay;
        this.iceToAirDelay = iceToAirDelay;
    }

    @Override
    public DelayedBlockTransformationTask transformBlock() {
        if (serverLevel.getBlockState(pos).is(Blocks.BLUE_ICE)) {
            serverLevel.setBlockAndUpdate(pos, newState);
            return new PackedIceToIceTask(modInstance, serverLevel, pos, packedToIceDelay, iceToAirDelay);
        } else {
            Constants.LOG.warn("Block at {} was not Blue Ice when Packed Ice transformation task executed.", pos);
            return null;
        }
    }
}
