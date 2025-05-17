package sircow.preservedinferno.screen;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import sircow.preservedinferno.Constants;

public class PreservedFletchingTableMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess access;
    private final Slot flintInputSlot;
    private final Slot stickInputSlot;
    private final Slot featherInputSlot;
    private final Slot potionInputSlot;
    private final Slot resultSlot;

    Runnable slotUpdateListener = () -> {
    };

    private final Container inputContainer = new SimpleContainer(4) {
        @Override
        public void setChanged() {
            super.setChanged();
            PreservedFletchingTableMenu.this.slotsChanged(this);
            PreservedFletchingTableMenu.this.slotUpdateListener.run();
        }
    };
    private final Container outputContainer = new SimpleContainer(1) {
        @Override
        public void setChanged() {
            super.setChanged();
            PreservedFletchingTableMenu.this.slotUpdateListener.run();
        }
    };

    public PreservedFletchingTableMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public PreservedFletchingTableMenu(int containerId, Inventory playerInventory, final ContainerLevelAccess access) {
        super(Constants.PRESERVED_FLETCHING_TABLE_MENU_TYPE.get(), containerId);
        this.access = access;

        this.flintInputSlot = this.addSlot(new Slot(this.inputContainer, 0, 62, 15) { // flint input
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) { return stack.getItem() == Items.FLINT; }
        });
        this.stickInputSlot = this.addSlot(new Slot(this.inputContainer, 1, 62, 35) { // stick input
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) { return stack.getItem() == Items.STICK; }
        });
        this.featherInputSlot = this.addSlot(new Slot(this.inputContainer, 2, 62, 55) { // feather input
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) { return stack.getItem() == Items.FEATHER; }
        });
        this.potionInputSlot = this.addSlot(new Slot(this.inputContainer, 3, 116, 55) { // potion input
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) { return stack.getItem() == Items.POTION; }
        });

        this.resultSlot = this.addSlot(new Slot(this.outputContainer, 0, 116, 35) { // output
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
                PreservedFletchingTableMenu.this.flintInputSlot.remove(1);
                PreservedFletchingTableMenu.this.stickInputSlot.remove(1);
                PreservedFletchingTableMenu.this.featherInputSlot.remove(1);
                if (!PreservedFletchingTableMenu.this.potionInputSlot.getItem().isEmpty()) {
                    ItemEntity itementity = player.drop(new ItemStack(Items.GLASS_BOTTLE), false);
                    if (itementity != null) {
                        itementity.setNoPickUpDelay();
                        itementity.setTarget(player.getUUID());
                    }
                }
                PreservedFletchingTableMenu.this.potionInputSlot.remove(1);
                super.onTake(player, stack);
            }
        });
        this.addStandardInventorySlots(playerInventory, 8, 84);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index == this.resultSlot.index) {
                if (!this.moveItemStackTo(slot.remove(slot.getMaxStackSize()), 4, 40, true)) {
                    slot.set(slot.remove(slot.getMaxStackSize()));
                    return ItemStack.EMPTY;
                }
            }

            else if (index >= 0 && index < 4) {
                if (!this.moveItemStackTo(itemstack1, 4, 40, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= 4 && index < 40) {
                if (itemstack1.getItem() == Items.FLINT) {
                    if (!this.moveItemStackTo(itemstack1, this.flintInputSlot.index, this.flintInputSlot.index + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (itemstack1.getItem() == Items.STICK) {
                    if (!this.moveItemStackTo(itemstack1, this.stickInputSlot.index, this.stickInputSlot.index + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (itemstack1.getItem() == Items.FEATHER) {
                    if (!this.moveItemStackTo(itemstack1, this.featherInputSlot.index, this.featherInputSlot.index + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (itemstack1.getItem() instanceof PotionItem) {
                    if (!this.moveItemStackTo(itemstack1, this.potionInputSlot.index, this.potionInputSlot.index + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (!this.moveItemStackTo(itemstack1, 0, 4, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            }
            else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }
        return itemstack;
    }

    @Override
    public void slotsChanged(@NotNull Container inventory) {
        ItemStack itemstack = this.flintInputSlot.getItem();
        ItemStack itemstack1 = this.stickInputSlot.getItem();
        ItemStack itemstack2 = this.featherInputSlot.getItem();
        ItemStack itemstack3 = this.potionInputSlot.getItem();
        if (!itemstack.isEmpty() && !itemstack1.isEmpty() && !itemstack2.isEmpty()) {
            this.setupResultSlot();
            this.broadcastChanges();
        }
        else {
            this.resultSlot.set(ItemStack.EMPTY);
        }
    }

    private void setupResultSlot() {
        ItemStack flintStack = this.flintInputSlot.getItem();
        ItemStack stickStack = this.stickInputSlot.getItem();
        ItemStack featherStack = this.featherInputSlot.getItem();
        ItemStack potionStack = this.potionInputSlot.getItem();

        if (!flintStack.isEmpty() && !stickStack.isEmpty() && !featherStack.isEmpty()) {
            if (potionStack.getItem() instanceof PotionItem) {
                ItemStack tippedArrowStack = new ItemStack(Items.TIPPED_ARROW, 16);
                if (potionStack.get(DataComponents.POTION_CONTENTS) != null) {
                    tippedArrowStack.set(DataComponents.POTION_CONTENTS, potionStack.get(DataComponents.POTION_CONTENTS));
                }
                this.outputContainer.setItem(0, tippedArrowStack);
            }
            else {
                this.outputContainer.setItem(0, new ItemStack(Items.ARROW, 16));
            }
        }
        else {
            this.outputContainer.setItem(0, ItemStack.EMPTY);
        }
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        this.access.execute((level, blockPos) -> this.clearContainer(player, this.inputContainer));
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(this.access, player, Blocks.FLETCHING_TABLE);
    }
}
