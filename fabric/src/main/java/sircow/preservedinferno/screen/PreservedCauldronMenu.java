package sircow.preservedinferno.screen;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import sircow.preservedinferno.PreservedInferno;
import sircow.preservedinferno.block.entity.PreservedCauldronBlockData;

import static sircow.preservedinferno.block.entity.PreservedCauldronBlockEntity.conversionMap;

public class PreservedCauldronMenu extends AbstractContainerMenu {
    private final Container inventory;
    private final ContainerData propertyDelegate;
    private final ContainerData propertyDelegateTwo;

    public PreservedCauldronMenu(int syncId, Inventory inventory, PreservedCauldronBlockData blockData) {
        this(syncId, inventory,
                new SimpleContainerData(2), new SimpleContainerData(2), new SimpleContainer(3));
    }

    public PreservedCauldronMenu(int syncId, Inventory playerInventory,
                                 ContainerData arrayPropertyDelegate, ContainerData arrayPropertyDelegateTwo, Container inventory) {
        super(PreservedInferno.PRESERVED_CAULDRON_MENU_TYPE, syncId);
        checkContainerSize(inventory, 3);
        checkContainerDataCount(arrayPropertyDelegate, 2);
        checkContainerDataCount(arrayPropertyDelegateTwo, 2);
        this.inventory = inventory;
        inventory.startOpen(playerInventory.player);
        this.propertyDelegate = arrayPropertyDelegate;
        this.propertyDelegateTwo = arrayPropertyDelegateTwo;

        this.addSlot(new Slot(inventory, 0, 80, 15)); // item input
        this.addSlot(new Slot(inventory, 1, 152, 52)); // water input
        this.addSlot(new Slot(inventory, 2, 80, 52)); // item output

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addDataSlots(arrayPropertyDelegate);
        addDataSlots(arrayPropertyDelegateTwo);
    }

    public boolean isCrafting() {
        return propertyDelegate.get(0) > 0;
    }

    public int getScaledProgressArrow() {
        int progress = this.propertyDelegate.get(0);
        int maxProgress = this.propertyDelegate.get(1);  // Max Progress
        int progressArrowSize = 16;

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    public int getScaledProgressWater() {
        int waterProgress = this.propertyDelegateTwo.get(0);
        int maxWaterProgress = this.propertyDelegateTwo.get(1);  // Max Progress
        int progressWaterSize = 32;

        return maxWaterProgress != 0 && waterProgress != 0 ? waterProgress * progressWaterSize / maxWaterProgress : 0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        PotionContents potionContentsComponent = slot.getItem().get(DataComponents.POTION_CONTENTS);
        if (slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            Item inputCheck = conversionMap.get(originalStack.getItem());

            if (invSlot == 2) {
                if (!this.moveItemStackTo(originalStack, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(originalStack, newStack);
            }
            else if (invSlot != 1 && invSlot != 0) {
                // shift click into water slot
                if (originalStack.getItem() == Items.WATER_BUCKET ||
                        originalStack.getItem() == Items.POTION && (potionContentsComponent != null && potionContentsComponent.is(Potions.WATER))) {
                    if (!this.moveItemStackTo(originalStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                // shift click into input slot
                else if (inputCheck != null || originalStack.has(DataComponents.DYED_COLOR)) {
                    if (!this.moveItemStackTo(originalStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (invSlot >= 3 && invSlot < 30) {
                    if (!this.moveItemStackTo(originalStack, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (invSlot >= 30 && invSlot < 39 && !this.moveItemStackTo(originalStack, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(originalStack, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (originalStack.getCount() == newStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, originalStack);
        }
        return newStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.inventory.stillValid(player);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
