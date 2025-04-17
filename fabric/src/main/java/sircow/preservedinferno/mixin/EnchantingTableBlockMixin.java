package sircow.preservedinferno.mixin;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EnchantingTableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.EnchantingTableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.PreservedInferno;
import sircow.preservedinferno.screen.PreservedEnchantmentMenu;

@Mixin(EnchantingTableBlock.class)
public class EnchantingTableBlockMixin {
    @SuppressWarnings("rawtypes")
    @Inject(method = "getMenuProvider", at = @At("HEAD"), cancellable = true)
    public void preserved_inferno$getMenuProvider(BlockState state, Level level, BlockPos pos, CallbackInfoReturnable<MenuProvider> cir) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        if (blockentity instanceof EnchantingTableBlockEntity) {
            Component component = ((Nameable) blockentity).getDisplayName();
            cir.setReturnValue(new ExtendedScreenHandlerFactory() {
                @Override
                public PreservedInferno.BlockData getScreenOpeningData(ServerPlayer serverPlayer) {
                    boolean isEmpty = level.getBlockEntity(pos) == null;
                    return new PreservedInferno.BlockData(isEmpty);
                }

                @Override
                public @NotNull AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
                    return new PreservedEnchantmentMenu(syncId, playerInventory, ContainerLevelAccess.create(level, pos));
                }

                @Override
                public @NotNull Component getDisplayName() {
                    return component;
                }
            });
        }
    }
}
