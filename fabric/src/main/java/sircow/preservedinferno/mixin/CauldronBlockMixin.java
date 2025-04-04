package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.block.entity.PreservedCauldronBlockEntity;

@Mixin(CauldronBlock.class)
public class CauldronBlockMixin implements EntityBlock {
    @Unique
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PreservedCauldronBlockEntity(pos, state);
    }

    @Unique
    private static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
            BlockEntityType<A> serverType, BlockEntityType<E> clientType, BlockEntityTicker<? super E> ticker
    ) {
        return clientType.equals(serverType) ? (BlockEntityTicker<A>)ticker : null;
    }

    @Unique
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> blockEntityType) {
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
    public void cancel(Fluid fluid, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(method = "receiveStalactiteDrip", at = @At("HEAD"), cancellable = true)
    public void cancel2(BlockState state, Level level, BlockPos pos, Fluid fluid, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "shouldHandlePrecipitation", at = @At("HEAD"), cancellable = true)
    private static void cancel3(Level level, Biome.Precipitation precipitation, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(method = "handlePrecipitation", at = @At("HEAD"), cancellable = true)
    public void cancel4(BlockState state, Level level, BlockPos pos, Biome.Precipitation precipitation, CallbackInfo ci) {
        ci.cancel();
    }
}
