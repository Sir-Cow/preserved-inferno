package sircow.preservedinferno.other;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.SpeleothemThickness;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public final class DripstoneHoneyHelper {
    private static final VoxelShape REQUIRED_SPACE_TO_DRIP_THROUGH_NON_SOLID_BLOCK = Block.column(4.0, 0.0, 16.0);
    public static final EnumProperty<Direction> TIP_DIRECTION = BlockStateProperties.VERTICAL_DIRECTION;

    private DripstoneHoneyHelper() {}

    public static boolean hasHoneySource(Level level, BlockPos pos, BlockState state) {
        if (!isStalactite(state)) return false;

        BlockPos.MutableBlockPos cursor = pos.mutable();

        for (int i = 0; i < 256; i++) {
            cursor.move(Direction.UP);
            BlockState current = level.getBlockState(cursor);

            if (current.is(state.getBlock()) && current.hasProperty(BlockStateProperties.VERTICAL_DIRECTION) && current.getValue(BlockStateProperties.VERTICAL_DIRECTION) == Direction.DOWN) continue;

            if (current.is(Blocks.BEEHIVE) || current.is(Blocks.BEE_NEST)) {
                if (current.hasProperty(BeehiveBlock.HONEY_LEVEL)) return current.getValue(BeehiveBlock.HONEY_LEVEL) >= 5;
            }
            break;
        }
        return false;
    }

    public static BlockPos findHoneyCauldronBelowTip(Level level, BlockPos stalactiteTipPos) {
        Predicate<BlockState> cauldronPredicate = state -> state.getBlock() instanceof AbstractCauldronBlock;
        BiPredicate<BlockPos, BlockState> pathPredicate = (pos, state) -> canDripThrough(level, pos, state);

        return findBlockVertical(level, stalactiteTipPos, Direction.DOWN.getAxisDirection(), pathPredicate, cauldronPredicate, 11).orElse(null);
    }

    public static BlockPos findTip(BlockState speleothemState, LevelAccessor level, BlockPos speleothemPos, int maxSearchLength, boolean includeMergedTip) {
        if (isTip(speleothemState, includeMergedTip)) return speleothemPos;

        Direction searchDirection = speleothemState.getValue(SpeleothemBlock.TIP_DIRECTION);
        BiPredicate<BlockPos, BlockState> pathPredicate = (pos, state) -> state.is(speleothemState.getBlock()) && state.getValue(SpeleothemBlock.TIP_DIRECTION) == searchDirection;

        return findBlockVertical(level, speleothemPos, searchDirection.getAxisDirection(), pathPredicate, state -> isTip(state, includeMergedTip), maxSearchLength).orElse(null);
    }

    public static Optional<BlockPos> findBlockVertical(LevelAccessor level, BlockPos pos, Direction.AxisDirection axisDirection, BiPredicate<BlockPos, BlockState> pathPredicate, Predicate<BlockState> targetPredicate, int maxSteps) {
        Direction direction = Direction.get(axisDirection, Direction.Axis.Y);
        BlockPos.MutableBlockPos mutablePos = pos.mutable();

        for (int i = 1; i < maxSteps; i++) {
            mutablePos.move(direction);
            BlockState state = level.getBlockState(mutablePos);

            if (targetPredicate.test(state)) return Optional.of(mutablePos.immutable());
            if (level.isOutsideBuildHeight(mutablePos.getY()) || !pathPredicate.test(mutablePos, state)) return Optional.empty();
        }
        return Optional.empty();
    }

    private static boolean canDripThrough(BlockGetter level, BlockPos pos, BlockState state) {
        if (state.isAir()) return true;
        if (state.isSolidRender()) return false;
        if (!state.getFluidState().isEmpty()) return false;

        VoxelShape collisionShape = state.getCollisionShape(level, pos);

        return !Shapes.joinIsNotEmpty(REQUIRED_SPACE_TO_DRIP_THROUGH_NON_SOLID_BLOCK, collisionShape, BooleanOp.AND);
    }

    private static boolean isTip(BlockState state, boolean includeMergedTip) {
        if (!state.is(BlockTags.SPELEOTHEMS)) return false;

        SpeleothemThickness thickness = state.getValue(SpeleothemBlock.THICKNESS);
        return thickness == SpeleothemThickness.TIP || (includeMergedTip && thickness == SpeleothemThickness.TIP_MERGE);
    }

    private static boolean isStalactite(final BlockState state) {
        return isSpeleothemWithDirection(state, Direction.DOWN);
    }

    private static boolean isSpeleothemWithDirection(final BlockState blockState, final Direction tipDirection) {
        return blockState.is(BlockTags.SPELEOTHEMS) && blockState.getValue(TIP_DIRECTION) == tipDirection;
    }
}
