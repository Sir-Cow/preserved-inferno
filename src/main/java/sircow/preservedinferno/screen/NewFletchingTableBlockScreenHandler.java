package sircow.preservedinferno.screen;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import sircow.preservedinferno.block.entity.NewFletchingTableBlockData;
import sircow.preservedinferno.block.entity.NewFletchingTableBlockEntity;

public class NewFletchingTableBlockScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    public final NewFletchingTableBlockEntity blockEntity;

    public NewFletchingTableBlockScreenHandler(int syncId, PlayerInventory inventory, NewFletchingTableBlockData data) {
        this(syncId, inventory, inventory.player.getWorld().getBlockEntity(data.pos()), new ArrayPropertyDelegate(2));
    }

    public NewFletchingTableBlockScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity, PropertyDelegate arrayPropertyDelegate) {
        super(ModScreenHandlers.NEW_FLETCHING_TABLE_BLOCK_SCREEN_HANDLER, syncId);
        checkSize(((Inventory) blockEntity), 2);
        this.inventory = ((Inventory) blockEntity);
        inventory.onOpen(playerInventory.player);
        this.blockEntity = ((NewFletchingTableBlockEntity) blockEntity);

        this.addSlot(new Slot(inventory, 0, 62, 15) { // flint input
            @Override
            public boolean canInsert(ItemStack stack) { return stack.getItem() == Items.FLINT; }
        });
        this.addSlot(new Slot(inventory, 1, 62, 35) { // stick input
            @Override
            public boolean canInsert(ItemStack stack) { return stack.getItem() == Items.STICK; }
        });
        this.addSlot(new Slot(inventory, 2, 62, 55) { // feather input
            @Override
            public boolean canInsert(ItemStack stack) { return stack.getItem() == Items.FEATHER; }
        });
        this.addSlot(new Slot(inventory, 3, 116, 55) { // potion input
            @Override
            public boolean canInsert(ItemStack stack) { return stack.getItem() == Items.POTION; }
        });

        this.addSlot(new Slot(inventory, 4, 116, 35) { // output
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }
        });

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
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
