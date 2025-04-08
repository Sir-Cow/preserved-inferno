package sircow.preservedinferno.mixin;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FletchingTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.PreservedInferno;
import sircow.preservedinferno.screen.PreservedFletchingTableMenu;

@Mixin(FletchingTableBlock.class)
public class FletchingTableBlockMixin {
    @Unique
    private static final Component CONTAINER_TITLE = Component.literal("Fletching Table");

    @Inject(method = "useWithoutItem", at = @At("HEAD"), cancellable = true)
    public void useWithoutItemOverride(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (!level.isClientSide) {
            MenuProvider menuProvider = state.getMenuProvider(level, pos);
            if (menuProvider != null) {
                player.openMenu(menuProvider);
            }
        }
        cir.setReturnValue(InteractionResult.SUCCESS);
    }

    @Unique
    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new ExtendedScreenHandlerFactory() {

            @Override
            public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
                return new PreservedFletchingTableMenu(syncId, playerInventory, ContainerLevelAccess.create(level, pos));
            }

            @Override
            public Component getDisplayName() {
                return CONTAINER_TITLE;
            }

            @Override
            public Object getScreenOpeningData(ServerPlayer serverPlayer) {
                boolean isEmpty = level.getBlockEntity(pos) == null;
                return new PreservedInferno.BlockData(isEmpty);
            }
        };
    }
}
