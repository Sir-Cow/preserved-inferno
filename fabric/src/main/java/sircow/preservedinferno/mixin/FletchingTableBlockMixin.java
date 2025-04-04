package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FletchingTableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.block.entity.PreservedFletchingTableBlockEntity;

@Mixin(FletchingTableBlock.class)
public class FletchingTableBlockMixin implements EntityBlock {
    @Inject(method = "useWithoutItem", at = @At("HEAD"), cancellable = true)
    public void useWithoutItemOverride(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (!level.isClientSide) {
            MenuProvider menuProvider = ((PreservedFletchingTableBlockEntity) level.getBlockEntity(pos));
            if (menuProvider != null) {
                player.openMenu(menuProvider);
            }
            player.openMenu(state.getMenuProvider(level, pos));
        }

        cir.setReturnValue(InteractionResult.SUCCESS);
    }

    @Unique
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PreservedFletchingTableBlockEntity(pos, state);
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
        return createFletchingTableTicker(world, blockEntityType);
    }

    @Unique
    private static <T extends BlockEntity> BlockEntityTicker<T> createFletchingTableTicker(
            Level level, BlockEntityType<T> blockEntityType
    ) {
        return level instanceof ServerLevel serverlevel
                ? createTickerHelper(
                blockEntityType,
                blockEntityType,
                (world1, pos, state1, blockEntity) -> PreservedFletchingTableBlockEntity.tick(serverlevel, pos, state1, (PreservedFletchingTableBlockEntity) blockEntity)
        )
                : null;
    }
}
