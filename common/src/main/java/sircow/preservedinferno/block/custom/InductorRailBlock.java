package sircow.preservedinferno.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import org.jetbrains.annotations.NotNull;

public class InductorRailBlock extends BaseRailBlock {
        public static final MapCodec<InductorRailBlock> CODEC = simpleCodec(InductorRailBlock::new);
    public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;

    public InductorRailBlock(BlockBehaviour.Properties properties) {
        super(true, properties);
        this.registerDefaultState(this.stateDefinition
                .any().setValue(SHAPE, RailShape.NORTH_SOUTH)
                .setValue(WATERLOGGED, Boolean.FALSE)
        );
    }

    @Override
    public @NotNull MapCodec<? extends InductorRailBlock> codec() {
        return CODEC;
    }

    @Override
    public @NotNull Property<RailShape> getShapeProperty() {
        return SHAPE;
    }

    @Override
    protected @NotNull BlockState rotate(@NotNull BlockState state, Rotation rot) {
        switch (rot) {
            case CLOCKWISE_180:
                switch (state.getValue(SHAPE)) {
                    case ASCENDING_EAST:
                        return state.setValue(SHAPE, RailShape.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return state.setValue(SHAPE, RailShape.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                        return state.setValue(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return state.setValue(SHAPE, RailShape.ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return state.setValue(SHAPE, RailShape.NORTH_WEST);
                    case SOUTH_WEST:
                        return state.setValue(SHAPE, RailShape.NORTH_EAST);
                    case NORTH_WEST:
                        return state.setValue(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_EAST:
                        return state.setValue(SHAPE, RailShape.SOUTH_WEST);
                }
            case COUNTERCLOCKWISE_90:
                return switch (state.getValue(SHAPE)) {
                    case NORTH_SOUTH -> state.setValue(SHAPE, RailShape.EAST_WEST);
                    case EAST_WEST -> state.setValue(SHAPE, RailShape.NORTH_SOUTH);
                    case ASCENDING_EAST -> state.setValue(SHAPE, RailShape.ASCENDING_NORTH);
                    case ASCENDING_WEST -> state.setValue(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_NORTH -> state.setValue(SHAPE, RailShape.ASCENDING_WEST);
                    case ASCENDING_SOUTH -> state.setValue(SHAPE, RailShape.ASCENDING_EAST);
                    case SOUTH_EAST -> state.setValue(SHAPE, RailShape.NORTH_EAST);
                    case SOUTH_WEST -> state.setValue(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_WEST -> state.setValue(SHAPE, RailShape.SOUTH_WEST);
                    case NORTH_EAST -> state.setValue(SHAPE, RailShape.NORTH_WEST);
                };
            case CLOCKWISE_90:
                return switch (state.getValue(SHAPE)) {
                    case NORTH_SOUTH -> state.setValue(SHAPE, RailShape.EAST_WEST);
                    case EAST_WEST -> state.setValue(SHAPE, RailShape.NORTH_SOUTH);
                    case ASCENDING_EAST -> state.setValue(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_WEST -> state.setValue(SHAPE, RailShape.ASCENDING_NORTH);
                    case ASCENDING_NORTH -> state.setValue(SHAPE, RailShape.ASCENDING_EAST);
                    case ASCENDING_SOUTH -> state.setValue(SHAPE, RailShape.ASCENDING_WEST);
                    case SOUTH_EAST -> state.setValue(SHAPE, RailShape.SOUTH_WEST);
                    case SOUTH_WEST -> state.setValue(SHAPE, RailShape.NORTH_WEST);
                    case NORTH_WEST -> state.setValue(SHAPE, RailShape.NORTH_EAST);
                    case NORTH_EAST -> state.setValue(SHAPE, RailShape.SOUTH_EAST);
                };
            default:
                return state;
        }
    }

    @Override
    protected @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        RailShape railshape = state.getValue(SHAPE);
        switch (mirror) {
            case LEFT_RIGHT:
                switch (railshape) {
                    case ASCENDING_NORTH:
                        return state.setValue(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return state.setValue(SHAPE, RailShape.ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return state.setValue(SHAPE, RailShape.NORTH_EAST);
                    case SOUTH_WEST:
                        return state.setValue(SHAPE, RailShape.NORTH_WEST);
                    case NORTH_WEST:
                        return state.setValue(SHAPE, RailShape.SOUTH_WEST);
                    case NORTH_EAST:
                        return state.setValue(SHAPE, RailShape.SOUTH_EAST);
                    default:
                        return super.mirror(state, mirror);
                }
            case FRONT_BACK:
                switch (railshape) {
                    case ASCENDING_EAST:
                        return state.setValue(SHAPE, RailShape.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return state.setValue(SHAPE, RailShape.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                    case ASCENDING_SOUTH:
                    default:
                        break;
                    case SOUTH_EAST:
                        return state.setValue(SHAPE, RailShape.SOUTH_WEST);
                    case SOUTH_WEST:
                        return state.setValue(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_WEST:
                        return state.setValue(SHAPE, RailShape.NORTH_EAST);
                    case NORTH_EAST:
                        return state.setValue(SHAPE, RailShape.NORTH_WEST);
                }
        }

        return super.mirror(state, mirror);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, WATERLOGGED);
    }
}
