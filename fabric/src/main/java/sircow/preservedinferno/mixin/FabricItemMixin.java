package sircow.preservedinferno.mixin;

import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.codec.ItemData;
import sircow.preservedinferno.container.CacheContainer;
import sircow.preservedinferno.item.custom.CacheItem;
import sircow.preservedinferno.menu.CacheMenu;
import sircow.preservedinferno.sound.ModSounds;

import java.util.Collections;

@Mixin(Item.class)
public class FabricItemMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void pinferno$openCacheMenu(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if ((Object) this instanceof CacheItem cacheItem) {
            if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
                ItemStack usedStack = player.getItemInHand(hand);
                ItemContainerContents container = usedStack.get(DataComponents.CONTAINER);

                int size = cacheItem.getSize();

                if (container == null) {
                    container = ItemContainerContents.fromItems(Collections.nCopies(size, ItemStack.EMPTY));
                    usedStack.set(DataComponents.CONTAINER, container);
                }

                CacheContainer cacheContainer = new CacheContainer(size, usedStack, container);
                serverPlayer.openMenu(getMenuProvider(cacheContainer, usedStack, size));
                serverPlayer.level().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), ModSounds.CACHE_OPEN, SoundSource.PLAYERS, 1.0F, 1.0F);

                cir.setReturnValue(InteractionResult.SUCCESS);
            }
        }
    }

    @Unique
    @SuppressWarnings("rawtypes")
    private MenuProvider getMenuProvider(Container container, ItemStack stackContext, int size) {
        return new ExtendedMenuProvider() {
            @Override
            public Object getScreenOpeningData(@NonNull ServerPlayer serverPlayer) {
                return new ItemData(size);
            }

            @Override
            public @NotNull AbstractContainerMenu createMenu(int syncId, @NonNull Inventory playerInventory, @NonNull Player player) {
                return new CacheMenu(syncId, playerInventory, container, stackContext);
            }

            @Override
            public @NotNull Component getDisplayName() {
                return Component.translatable("container.pinferno.cache");
            }
        };
    }
}
