package sircow.preservedinferno.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jspecify.annotations.NonNull;

public class SparklingBlackstoneBlock extends Block {
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 4);

    public SparklingBlackstoneBlock(BlockBehaviour.Properties properties) {
        super(properties.randomTicks());
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }

    @Override
    protected void randomTick(BlockState state, @NonNull ServerLevel level, @NonNull BlockPos pos, @NonNull RandomSource random) {
        int stage = state.getValue(STAGE);

        if (stage < 4 && random.nextInt(5) == 0) {
            level.setBlock(pos, state.setValue(STAGE, stage + 1), Block.UPDATE_ALL);
        }
    }

    @Override
    public void playerDestroy(@NonNull Level level, @NonNull Player player, @NonNull BlockPos pos, BlockState state, BlockEntity blockEntity, @NonNull ItemStack tool) {
        int stage = state.getValue(STAGE);

        if (stage == 0) {
            super.playerDestroy(level, player, pos, state, blockEntity, tool);
            return;
        }

        if (!level.isClientSide() && !player.isCreative() && !player.isSpectator()) {
            Block.dropResources(state, level, pos, blockEntity, player, tool);
            level.setBlock(pos, state.setValue(STAGE, stage - 1), Block.UPDATE_ALL);
        }
    }
}
