package sircow.preservedinferno.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.block.ModBlocks;

public class MilkFluid extends FlowingFluid {
    @Override
    public @NonNull Fluid getFlowing() {
        return ModFluids.FLOWING_MILK;
    }

    @Override
    public @NonNull Fluid getSource() {
        return ModFluids.MILK;
    }

    @Override
    protected boolean canConvertToSource(@NonNull ServerLevel serverLevel) {
        return false;
    }

    @Override
    protected void beforeDestroyingBlock(@NonNull LevelAccessor levelAccessor, @NonNull BlockPos blockPos, @NonNull BlockState blockState) {}

    @Override
    protected int getSlopeFindDistance(@NonNull LevelReader levelReader) {
        return 0;
    }

    @Override
    protected int getDropOff(@NonNull LevelReader levelReader) {
        return 0;
    }

    @Override
    public @NonNull Item getBucket() {
        return null;
    }

    @Override
    protected boolean canBeReplacedWith(@NonNull FluidState fluidState, @NonNull BlockGetter blockGetter, @NonNull BlockPos blockPos, @NonNull Fluid fluid, @NonNull Direction direction) {
        return false;
    }

    @Override
    public int getTickDelay(@NonNull LevelReader levelReader) {
        return 0;
    }

    @Override
    protected float getExplosionResistance() {
        return 100.0F;
    }

    @Override
    protected @NonNull BlockState createLegacyBlock(@NonNull FluidState fluidState) {
        return ModBlocks.MILK.defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(fluidState));
    }

    @Override
    public boolean isSource(@NonNull FluidState fluidState) {
        return false;
    }

    @Override
    public int getAmount(@NonNull FluidState fluidState) {
        return 0;
    }

    public static class Flowing extends MilkFluid {
        @Override
        protected void createFluidStateDefinition(StateDefinition.@NonNull Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }
    }

    public static class Source extends MilkFluid {
        @Override
        public int getAmount(@NonNull FluidState state) {
            return 8;
        }

        @Override
        public boolean isSource(@NonNull FluidState state) {
            return true;
        }
    }
}
