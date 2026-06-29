package sircow.preservedinferno.mixin;

import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
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
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.PreservedInferno;
import sircow.preservedinferno.screen.PreservedEnchantmentMenu;

import java.util.List;
import java.util.stream.Stream;

@Mixin(EnchantingTableBlock.class)
public class EnchantingTableBlockMixin {
    @SuppressWarnings("rawtypes")
    @Inject(method = "getMenuProvider", at = @At("HEAD"), cancellable = true)
    public void pinferno$getMenuProvider(BlockState state, Level level, BlockPos pos, CallbackInfoReturnable<MenuProvider> cir) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        if (blockentity instanceof EnchantingTableBlockEntity) {
            Component component = ((Nameable) blockentity).getDisplayName();
            cir.setReturnValue(new ExtendedMenuProvider() {
                @Override
                public PreservedInferno.BlockData getScreenOpeningData(@NonNull ServerPlayer serverPlayer) {
                    return new PreservedInferno.BlockData(level.getBlockEntity(pos) == null);
                }

                @Override
                public @NotNull AbstractContainerMenu createMenu(int syncId, @NonNull Inventory playerInventory, @NonNull Player player) {
                    return new PreservedEnchantmentMenu(syncId, playerInventory, ContainerLevelAccess.create(level, pos));
                }

                @Override
                public @NotNull Component getDisplayName() {
                    return component;
                }
            });
        }
    }

    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;toList()Ljava/util/List;", remap = false))
    private static List<BlockPos> pinferno$redirectBookshelfOffsets(Stream<BlockPos> stream) {
        return BlockPos.betweenClosedStream(-3, -3, -3, 3, 3, 3)
                .filter(pos -> !pos.equals(BlockPos.ZERO))
                .map(BlockPos::immutable)
                .toList();
    }

    @Overwrite
    public static boolean isValidBookShelf(Level level, BlockPos enchantingTablePos, BlockPos bookshelfOffset) {
        return level.getBlockState(enchantingTablePos.offset(bookshelfOffset)).is(BlockTags.ENCHANTMENT_POWER_PROVIDER);
    }
}
