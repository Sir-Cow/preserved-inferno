package sircow.preservedinferno.mixin;

import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.PreservedInferno;
import sircow.preservedinferno.block.custom.AnglingTableBlock;
import sircow.preservedinferno.screen.AnglingTableMenu;

@Mixin(AnglingTableBlock.class)
public class AnglingTableBlockMixin {
    @Shadow
    private static final Component CONTAINER_TITLE = Component.translatable("container.pinferno.angling_table");

    @SuppressWarnings("rawtypes")
    @Inject(method = "getMenuProvider", at = @At("HEAD"), cancellable = true)
    public void preserved_inferno$checkForAnglingTable(BlockState state, Level level, BlockPos pos, CallbackInfoReturnable<MenuProvider> cir) {
        cir.setReturnValue(
                new ExtendedMenuProvider() {
                    @Override
                    public @NotNull AbstractContainerMenu createMenu(int syncId, @NonNull Inventory playerInventory, @NonNull Player player) {
                        return new AnglingTableMenu(syncId, playerInventory, ContainerLevelAccess.create(level, pos));
                    }

                    @Override
                    public @NotNull Component getDisplayName() {
                        return CONTAINER_TITLE;
                    }

                    @Override
                    public Object getScreenOpeningData(@NonNull ServerPlayer serverPlayer) {
                        boolean isEmpty = level.getBlockEntity(pos) == null;
                        return new PreservedInferno.BlockData(isEmpty);
                    }
                }
        );
    }
}
