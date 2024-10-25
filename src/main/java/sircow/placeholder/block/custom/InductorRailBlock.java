package sircow.placeholder.block.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.*;
import net.minecraft.block.enums.RailShape;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class InductorRailBlock extends AbstractRailBlock implements Oxidizable {
    public static final MapCodec<InductorRailBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(OxidationLevel.CODEC.fieldOf("weathering_state").forGetter(Degradable::getDegradationLevel), createSettingsCodec())
                    .apply(instance, InductorRailBlock::new)
    );
    public static final EnumProperty<RailShape> SHAPE = Properties.STRAIGHT_RAIL_SHAPE;
    public static final BooleanProperty POWERED = Properties.POWERED;

    public InductorRailBlock(OxidationLevel oxidationLevel, AbstractBlock.Settings settings) {
        super(true, settings);
        this.oxidationLevel = oxidationLevel;
        this.setDefaultState(this.stateManager.getDefaultState().with(POWERED, Boolean.TRUE).with(SHAPE, RailShape.NORTH_SOUTH).with(WATERLOGGED, Boolean.FALSE));
    }

    @Override
    public MapCodec<InductorRailBlock> getCodec() {
        return CODEC;
    }

    @Override
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return switch (rotation) {
            case CLOCKWISE_180 -> switch (state.get(SHAPE)) {
                case NORTH_SOUTH, EAST_WEST -> null;
                case ASCENDING_EAST -> state.with(SHAPE, RailShape.ASCENDING_WEST);
                case ASCENDING_WEST -> state.with(SHAPE, RailShape.ASCENDING_EAST);
                case ASCENDING_NORTH -> state.with(SHAPE, RailShape.ASCENDING_SOUTH);
                case ASCENDING_SOUTH -> state.with(SHAPE, RailShape.ASCENDING_NORTH);
                case SOUTH_EAST -> state.with(SHAPE, RailShape.NORTH_WEST);
                case SOUTH_WEST -> state.with(SHAPE, RailShape.NORTH_EAST);
                case NORTH_WEST -> state.with(SHAPE, RailShape.SOUTH_EAST);
                case NORTH_EAST -> state.with(SHAPE, RailShape.SOUTH_WEST);
            };
            case COUNTERCLOCKWISE_90 -> switch (state.get(SHAPE)) {
                case NORTH_SOUTH -> state.with(SHAPE, RailShape.EAST_WEST);
                case EAST_WEST -> state.with(SHAPE, RailShape.NORTH_SOUTH);
                case ASCENDING_EAST -> state.with(SHAPE, RailShape.ASCENDING_NORTH);
                case ASCENDING_WEST -> state.with(SHAPE, RailShape.ASCENDING_SOUTH);
                case ASCENDING_NORTH -> state.with(SHAPE, RailShape.ASCENDING_WEST);
                case ASCENDING_SOUTH -> state.with(SHAPE, RailShape.ASCENDING_EAST);
                case SOUTH_EAST -> state.with(SHAPE, RailShape.NORTH_EAST);
                case SOUTH_WEST -> state.with(SHAPE, RailShape.SOUTH_EAST);
                case NORTH_WEST -> state.with(SHAPE, RailShape.SOUTH_WEST);
                case NORTH_EAST -> state.with(SHAPE, RailShape.NORTH_WEST);
            };
            case CLOCKWISE_90 -> switch (state.get(SHAPE)) {
                case NORTH_SOUTH -> state.with(SHAPE, RailShape.EAST_WEST);
                case EAST_WEST -> state.with(SHAPE, RailShape.NORTH_SOUTH);
                case ASCENDING_EAST -> state.with(SHAPE, RailShape.ASCENDING_SOUTH);
                case ASCENDING_WEST -> state.with(SHAPE, RailShape.ASCENDING_NORTH);
                case ASCENDING_NORTH -> state.with(SHAPE, RailShape.ASCENDING_EAST);
                case ASCENDING_SOUTH -> state.with(SHAPE, RailShape.ASCENDING_WEST);
                case SOUTH_EAST -> state.with(SHAPE, RailShape.SOUTH_WEST);
                case SOUTH_WEST -> state.with(SHAPE, RailShape.NORTH_WEST);
                case NORTH_WEST -> state.with(SHAPE, RailShape.NORTH_EAST);
                case NORTH_EAST -> state.with(SHAPE, RailShape.SOUTH_EAST);
            };
            default -> state;
        };
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        RailShape railShape = state.get(SHAPE);
        switch (mirror) {
            case LEFT_RIGHT:
                return switch (railShape) {
                    case ASCENDING_NORTH -> state.with(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH -> state.with(SHAPE, RailShape.ASCENDING_NORTH);
                    case SOUTH_EAST -> state.with(SHAPE, RailShape.NORTH_EAST);
                    case SOUTH_WEST -> state.with(SHAPE, RailShape.NORTH_WEST);
                    case NORTH_WEST -> state.with(SHAPE, RailShape.SOUTH_WEST);
                    case NORTH_EAST -> state.with(SHAPE, RailShape.SOUTH_EAST);
                    default -> super.mirror(state, mirror);
                };
            case FRONT_BACK:
                switch (railShape) {
                    case ASCENDING_EAST:
                        return state.with(SHAPE, RailShape.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return state.with(SHAPE, RailShape.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                    case ASCENDING_SOUTH:
                    default:
                        break;
                    case SOUTH_EAST:
                        return state.with(SHAPE, RailShape.SOUTH_WEST);
                    case SOUTH_WEST:
                        return state.with(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_WEST:
                        return state.with(SHAPE, RailShape.NORTH_EAST);
                    case NORTH_EAST:
                        return state.with(SHAPE, RailShape.NORTH_WEST);
                }
        }
        return super.mirror(state, mirror);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, POWERED, WATERLOGGED);
    }

    // oxidization stuff
    private final OxidationLevel oxidationLevel;

    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        this.tickDegradation(state, world, pos, random);
    }

    @Override
    protected boolean hasRandomTicks(BlockState state) {
        return Oxidizable.getIncreasedOxidationBlock(state.getBlock()).isPresent();
    }

    @Override
    public OxidationLevel getDegradationLevel() {
        return this.oxidationLevel;
    }
}
