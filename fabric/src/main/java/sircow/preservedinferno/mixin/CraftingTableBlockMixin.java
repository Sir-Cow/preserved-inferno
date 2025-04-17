package sircow.preservedinferno.mixin;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.FletchingTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.PreservedInferno;
import sircow.preservedinferno.screen.PreservedFletchingTableMenu;

@Mixin(CraftingTableBlock.class)
public class CraftingTableBlockMixin {
    @Unique
    private static final Component CONTAINER_TITLE = Component.literal("Fletching Table");

    @Inject(method = "useWithoutItem", at = @At("HEAD"), cancellable = true)
    public void preserved_inferno$checkForFletchingTable(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (!level.isClientSide) {
            if (level.getBlockState(pos).getBlock() instanceof FletchingTableBlock) {
                player.openMenu(state.getMenuProvider(level, pos));
                player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
                cir.setReturnValue(InteractionResult.SUCCESS);
            }
        }
        else {
            // do nothing!
        }
    }

    @SuppressWarnings("rawtypes")
    @Inject(method = "getMenuProvider", at = @At("HEAD"), cancellable = true)
    public void preserved_inferno$checkForFletchingTable2(BlockState state, Level level, BlockPos pos, CallbackInfoReturnable<MenuProvider> cir) {
        if (level.getBlockState(pos).getBlock() instanceof FletchingTableBlock) {
            cir.setReturnValue(
                    new ExtendedScreenHandlerFactory() {
                        @Override
                        public @NotNull AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
                            return new PreservedFletchingTableMenu(syncId, playerInventory, ContainerLevelAccess.create(level, pos));
                        }

                        @Override
                        public @NotNull Component getDisplayName() {
                            return CONTAINER_TITLE;
                        }

                        @Override
                        public Object getScreenOpeningData(ServerPlayer serverPlayer) {
                            boolean isEmpty = level.getBlockEntity(pos) == null;
                            return new PreservedInferno.BlockData(isEmpty);
                        }
                    }
            );
        } else {
            // do nothing!
        }
    }
}
