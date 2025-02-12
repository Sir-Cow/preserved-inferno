package sircow.preservedinferno.screen;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import sircow.preservedinferno.block.entity.NewCauldronBlockData;
import sircow.preservedinferno.block.entity.NewCauldronBlockEntity;

import static sircow.preservedinferno.block.entity.NewCauldronBlockEntity.conversionMap;

public class NewCauldronBlockScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    private final PropertyDelegate propertyDelegateTwo;
    public final NewCauldronBlockEntity blockEntity;

    public NewCauldronBlockScreenHandler(int syncId, PlayerInventory inventory, NewCauldronBlockData data) {
        this(syncId, inventory, inventory.player.getWorld().getBlockEntity(data.pos()),
                new ArrayPropertyDelegate(2), new ArrayPropertyDelegate(2));
    }

    public NewCauldronBlockScreenHandler(int syncId, PlayerInventory playerInventory,
                                     BlockEntity blockEntity, PropertyDelegate arrayPropertyDelegate, PropertyDelegate arrayPropertyDelegateTwo) {
        super(ModScreenHandlers.NEW_CAULDRON_BLOCK_SCREEN_HANDLER, syncId);
        checkSize(((Inventory) blockEntity), 2);
        this.inventory = ((Inventory) blockEntity);
        inventory.onOpen(playerInventory.player);
        this.propertyDelegate = arrayPropertyDelegate;
        this.propertyDelegateTwo = arrayPropertyDelegateTwo;
        this.blockEntity = ((NewCauldronBlockEntity) blockEntity);

        this.addSlot(new Slot(inventory, 0, 80, 15)); // item input
        this.addSlot(new Slot(inventory, 1, 152, 52)); // water input
        this.addSlot(new Slot(inventory, 2, 80, 52)); // item output

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addProperties(arrayPropertyDelegate);
        addProperties(arrayPropertyDelegateTwo);
    }

    public boolean isCrafting() {
        return propertyDelegate.get(0) > 0;
    }

    public int getScaledProgressArrow() {
        int progress = this.propertyDelegate.get(0);
        int maxProgress = this.propertyDelegate.get(1);  // Max Progress
        int progressArrowSize = 15;

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    public int getScaledProgressWater() {
        int waterProgress = this.propertyDelegateTwo.get(0);
        int maxWaterProgress = this.propertyDelegateTwo.get(1);  // Max Progress
        int progressWaterSize = 32;

        return maxWaterProgress != 0 && waterProgress != 0 ? waterProgress * progressWaterSize / maxWaterProgress : 0;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        PotionContentsComponent potionContentsComponent = slot.getStack().get(DataComponentTypes.POTION_CONTENTS);
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            Item inputCheck = conversionMap.get(originalStack.getItem());

            if (invSlot == 2) {
                if (!this.insertItem(originalStack, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(originalStack, newStack);
            }
            else if (invSlot != 1 && invSlot != 0) {
                // shift click into water slot
                if (originalStack.getItem() == Items.WATER_BUCKET ||
                        originalStack.getItem() == Items.POTION && (potionContentsComponent != null && potionContentsComponent.matches(Potions.WATER))) {
                    if (!this.insertItem(originalStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                // shift click into input slot
                else if (inputCheck != null || originalStack.contains(DataComponentTypes.DYED_COLOR)) {
                    if (!this.insertItem(originalStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (invSlot >= 3 && invSlot < 30) {
                    if (!this.insertItem(originalStack, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (invSlot >= 30 && invSlot < 39 && !this.insertItem(originalStack, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.insertItem(originalStack, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (originalStack.getCount() == newStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTakeItem(player, originalStack);
        }
        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
