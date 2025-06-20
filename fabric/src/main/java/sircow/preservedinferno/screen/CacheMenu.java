package sircow.preservedinferno.screen;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sircow.preservedinferno.MenuTypes;
import sircow.preservedinferno.PreservedInferno;
import sircow.preservedinferno.item.custom.CacheItem;
import sircow.preservedinferno.sound.ModSounds;

public class CacheMenu extends AbstractContainerMenu {
    private final Container container;
    private final ItemStack stackContext;

    public CacheMenu(int syncId, Inventory playerInventory, Container container, ItemStack stackContext) {
        super(MenuTypes.CACHE_MENU_TYPE.get(), syncId);
        this.container = container;
        this.stackContext = stackContext;
        container.startOpen(playerInventory.player);
        checkContainerSize(container, 9);
        setupSlots(playerInventory);
    }

    public CacheMenu(int containerId, Inventory playerInventory, PreservedInferno.ItemData data) {
        super(MenuTypes.CACHE_MENU_TYPE.get(), containerId);
        this.container = new SimpleContainer(data.containerSize());
        this.stackContext = ItemStack.EMPTY;
        checkContainerSize(this.container, data.containerSize());
        setupSlots(playerInventory);
    }

    private void setupSlots(Inventory playerInventory) {
        int startX = 62;
        int startY = 17;
        int slotSize = 18;

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                this.addSlot(new CacheSlot(this.container, col + row * 3, startX + col * slotSize, startY + row * slotSize));
            }
        }

        this.addStandardInventorySlots(playerInventory, 8, 84);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (itemstack1.getItem() instanceof CacheItem) {
                return ItemStack.EMPTY;
            }

            if (index < 9) {
                if (!this.moveItemStackTo(itemstack1, 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (index < 36) {
                if (!this.moveItemStackTo(itemstack1, 0, 9, false) && !this.moveItemStackTo(itemstack1, 36, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (index < this.slots.size()) {
                if (!this.moveItemStackTo(itemstack1, 0, 9, false) && !this.moveItemStackTo(itemstack1, 9, 36, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            }
            else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return this.stackContext != null && !this.stackContext.isEmpty() && player.getInventory().contains(this.stackContext) && this.container.stillValid(player);
    }

    private static class CacheSlot extends Slot {
        public CacheSlot(Container container, int index, int x, int y) {
            super(container, index, x, y);
        }

        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            return !(stack.getItem() instanceof CacheItem || stack.is(ItemTags.SHULKER_BOXES));
        }
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.level().playSound(null,
                    serverPlayer.getX(),
                    serverPlayer.getY(),
                    serverPlayer.getZ(),
                    ModSounds.CACHE_CLOSE,
                    SoundSource.PLAYERS,
                    1.0F,
                    1.0F
            );
        }
    }
}
