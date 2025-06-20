package sircow.preservedinferno.item.custom;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sircow.preservedinferno.PreservedInferno;
import sircow.preservedinferno.other.CacheContainer;
import sircow.preservedinferno.screen.CacheMenu;
import sircow.preservedinferno.sound.ModSounds;

import java.util.Collections;

public class CacheItem extends Item {
    private static final Component CONTAINER_TITLE = Component.translatable("container.pinferno.cache");
    private final int size;

    public CacheItem(Properties properties, int size) {
        super(properties);
        this.size = size;
    }

    @Override
    public @NotNull InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        if (!level.isClientSide) {
            ItemStack usedStack = player.getItemInHand(usedHand);
            ItemContainerContents container = usedStack.get(DataComponents.CONTAINER);
            if (container == null) {
                container = ItemContainerContents.fromItems(Collections.nCopies(size, ItemStack.EMPTY));
                usedStack.set(DataComponents.CONTAINER, container);
            }
            CacheContainer cacheContainer = new CacheContainer(size, usedStack, container);
            player.openMenu(getMenuProvider(cacheContainer, usedStack));
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.level().playSound(null,
                        serverPlayer.getX(),
                        serverPlayer.getY(),
                        serverPlayer.getZ(),
                        ModSounds.CACHE_OPEN,
                        SoundSource.PLAYERS,
                        1.0F,
                        1.0F
                );
            }
        }
        return InteractionResult.SUCCESS;
    }

    @SuppressWarnings("rawtypes")
    public MenuProvider getMenuProvider(Container container, ItemStack stackContext) {
        final Container finalContainer = container;
        final ItemStack finalStackContext = stackContext;
        return new ExtendedScreenHandlerFactory() {
            @Override
            public PreservedInferno.ItemData getScreenOpeningData(ServerPlayer serverPlayer) {
                return new PreservedInferno.ItemData(CacheItem.this.size);
            }

            @Override
            public @NotNull AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
                return new CacheMenu(syncId, playerInventory, finalContainer, finalStackContext);
            }

            @Override
            public @NotNull Component getDisplayName() {
                return CONTAINER_TITLE;
            }
        };
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return false;
    }
}
