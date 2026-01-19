package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.block.entity.PreservedCauldronBlockEntity;

@Mixin(CauldronBlock.class)
public abstract class CauldronBlockMixin extends Block implements EntityBlock {
    public CauldronBlockMixin(Properties properties) {
        super(properties);
    }

    @Unique
    @Override
    public @Nullable BlockEntity newBlockEntity(@NonNull BlockPos pos, @NonNull BlockState state) {
        return new PreservedCauldronBlockEntity(pos, state);
    }

    @SuppressWarnings("unchecked")
    @Unique
    private static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
            BlockEntityType<A> serverType, BlockEntityType<E> clientType, BlockEntityTicker<? super E> ticker
    ) {
        return clientType.equals(serverType) ? (BlockEntityTicker<A>) ticker : null;
    }

    @Unique
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NonNull Level world, @NonNull BlockState state, @NonNull BlockEntityType<T> blockEntityType) {
        return createCauldronTicker(world, blockEntityType);
    }

    @Unique
    private static <T extends BlockEntity> BlockEntityTicker<T> createCauldronTicker(
            Level level, BlockEntityType<T> blockEntityType
    ) {
        return level instanceof ServerLevel serverlevel
                ? createTickerHelper(
                blockEntityType,
                blockEntityType,
                (world1, pos, state1, blockEntity) -> PreservedCauldronBlockEntity.tick(serverlevel, pos, state1, (PreservedCauldronBlockEntity) blockEntity)
        )
                : null;
    }

    // cancel other vanilla cauldron stuff
    @Inject(method = "canReceiveStalactiteDrip", at = @At("HEAD"), cancellable = true)
    public void preserved_inferno$cancel(Fluid fluid, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(method = "receiveStalactiteDrip", at = @At("HEAD"), cancellable = true)
    public void preserved_inferno$cancel2(BlockState state, Level level, BlockPos pos, Fluid fluid, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "shouldHandlePrecipitation", at = @At("HEAD"), cancellable = true)
    private static void preserved_inferno$cancel3(Level level, Biome.Precipitation precipitation, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(method = "handlePrecipitation", at = @At("HEAD"), cancellable = true)
    public void preserved_inferno$cancel4(BlockState state, Level level, BlockPos pos, Biome.Precipitation precipitation, CallbackInfo ci) {
        ci.cancel();
    }

    @Unique
    @Override
    public boolean hasAnalogOutputSignal(@NonNull BlockState state) {
        return true;
    }

    @Unique
    @Override
    public int getAnalogOutputSignal(@NonNull BlockState state, Level level, @NonNull BlockPos pos, @NonNull Direction direction)  {
        if (level.getBlockEntity(pos) instanceof PreservedCauldronBlockEntity cauldron) {
            return (int) Math.floor((double) cauldron.progressWater / cauldron.maxWaterProgress * 15.0);
        }
        return 0;
    }
}
