package sircow.preservedinferno.other;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import sircow.preservedinferno.PreservedInferno;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class DelayedBlockTransformationTask {
    protected final ServerLevel serverLevel;
    protected final BlockPos pos;
    protected final BlockState newState;
    protected int delayTicks;
    protected final PreservedInferno modInstance;
    protected final int breakerId;
    public final Block expectedInitialBlock;
    private static final Set<BlockPos> noDropBlocks = Collections.synchronizedSet(new HashSet<>());

    public DelayedBlockTransformationTask(PreservedInferno modInstance, ServerLevel serverLevel, BlockPos pos, BlockState newState, Block expectedInitialBlock, int delayTicks) {
        this.modInstance = modInstance;
        this.serverLevel = serverLevel;
        this.pos = pos;
        this.newState = newState;
        this.delayTicks = delayTicks;
        this.breakerId = generateUniqueBreakerId();
        this.expectedInitialBlock = expectedInitialBlock;
        addNoDropBlock(pos);
    }

    public void addNoDropBlock(BlockPos pos) {
        noDropBlocks.add(pos.immutable());
    }

    public void removeNoDropBlock(BlockPos pos) {
        noDropBlocks.remove(pos);
    }

    public static boolean isNoDropBlock(BlockPos pos) {
        return noDropBlocks.contains(pos);
    }

    private int generateUniqueBreakerId() {
        return pos.hashCode() + serverLevel.dimension().hashCode();
    }

    public void tick() {
        if (!isBlockValid()) {
            cancelTransformation();
            return;
        }

        delayTicks--;
        if (delayTicks >= 0) {
            updateBreakingProgress();
        }
    }

    protected void updateBreakingProgress() {
        int maxDelay = getMaxDelayTicks();
        if (maxDelay == 0) {
            return;
        }

        int progress = (int) (((float)(maxDelay - delayTicks) / maxDelay) * 10);
        progress = Math.max(0, Math.min(9, progress));

        sendBreakingProgressToClients(progress);
    }

    protected void sendBreakingProgressToClients(int progress) {
        serverLevel.players().forEach(player -> {
            if (player.blockPosition().closerThan(pos, 64)) {
                player.connection.send(new ClientboundBlockDestructionPacket(breakerId, pos, progress));
            }
        });
    }

    protected abstract int getMaxDelayTicks();

    public abstract DelayedBlockTransformationTask transformBlock();

    public boolean isFinished() {
        return delayTicks <= 0;
    }

    protected boolean isBlockValid() {
        return serverLevel.getBlockState(pos).is(expectedInitialBlock);
    }

    public void cancelTransformation() {
        removeBreakingAnimation();
    }

    public void removeBreakingAnimation() {
        sendBreakingProgressToClients(-1);
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
    private final int initialPackedToIceDelay;

    public PackedIceToIceTask(PreservedInferno modInstance, ServerLevel serverLevel, BlockPos pos, int packedToIceDelay, int iceToAirDelay) {
        super(modInstance, serverLevel, pos, Blocks.ICE.defaultBlockState(), Blocks.PACKED_ICE, packedToIceDelay);
        this.iceToAirDelay = iceToAirDelay;
        this.initialPackedToIceDelay = packedToIceDelay;
    }

    @Override
    protected int getMaxDelayTicks() {
        return initialPackedToIceDelay;
    }

    @Override
    public DelayedBlockTransformationTask transformBlock() {
        if (!isBlockValid()) {
            return null;
        }

        if (serverLevel.getBlockState(pos).is(Blocks.PACKED_ICE)) {
            serverLevel.setBlockAndUpdate(pos, newState);
            removeBreakingAnimation();
            removeNoDropBlock(pos);
            return new SimpleBlockTransformationTask(modInstance, serverLevel, pos, Blocks.AIR.defaultBlockState(), Blocks.ICE, iceToAirDelay);
        }
        else {
            removeBreakingAnimation();
            removeNoDropBlock(pos);
            return null;
        }
    }
}

class BlueIceToPackedIceTask extends DelayedBlockTransformationTask {
    private final int packedToIceDelay;
    private final int iceToAirDelay;
    private final int initialBlueToPackedDelay;

    public BlueIceToPackedIceTask(PreservedInferno modInstance, ServerLevel serverLevel, BlockPos pos, int blueToPackedDelay, int packedToIceDelay, int iceToAirDelay) {
        super(modInstance, serverLevel, pos, Blocks.PACKED_ICE.defaultBlockState(), Blocks.BLUE_ICE, blueToPackedDelay);
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
        if (!isBlockValid()) {
            return null;
        }

        if (serverLevel.getBlockState(pos).is(Blocks.BLUE_ICE)) {
            serverLevel.setBlockAndUpdate(pos, newState);
            removeBreakingAnimation();
            removeNoDropBlock(pos);
            return new PackedIceToIceTask(modInstance, serverLevel, pos, packedToIceDelay, iceToAirDelay);
        }
        else {
            removeBreakingAnimation();
            removeNoDropBlock(pos);
            return null;
        }
    }
}
