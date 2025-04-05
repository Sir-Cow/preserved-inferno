package sircow.preservedinferno.screen;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import sircow.preservedinferno.block.entity.PreservedFletchingTableBlockData;
import sircow.preservedinferno.PreservedInferno;

public class PreservedFletchingTableMenu extends AbstractContainerMenu {
    private final Container inventory;
    private final ContainerData propertyDelegate;

    public PreservedFletchingTableMenu(int syncId, Inventory inventory, PreservedFletchingTableBlockData blockData) {
        this(syncId, inventory, new SimpleContainerData(2), new SimpleContainer(5));
    }

    public PreservedFletchingTableMenu(int syncId, Inventory playerInventory, ContainerData arrayPropertyDelegate, Container inventory) {
        super(PreservedInferno.PRESERVED_FLETCHING_TABLE_MENU_TYPE, syncId);
        checkContainerSize(inventory, 5);
        checkContainerDataCount(arrayPropertyDelegate, 2);
        this.inventory = inventory;
        this.propertyDelegate = arrayPropertyDelegate;
        inventory.startOpen(playerInventory.player);

        this.addSlot(new Slot(inventory, 0, 62, 15) { // flint input
            @Override
            public boolean mayPlace(ItemStack stack) { return stack.getItem() == Items.FLINT; }
        });
        this.addSlot(new Slot(inventory, 1, 62, 35) { // stick input
            @Override
            public boolean mayPlace(ItemStack stack) { return stack.getItem() == Items.STICK; }
        });
        this.addSlot(new Slot(inventory, 2, 62, 55) { // feather input
            @Override
            public boolean mayPlace(ItemStack stack) { return stack.getItem() == Items.FEATHER; }
        });
        this.addSlot(new Slot(inventory, 3, 116, 55) { // potion input
            @Override
            public boolean mayPlace(ItemStack stack) { return stack.getItem() == Items.POTION; }
        });

        this.addSlot(new Slot(inventory, 4, 116, 35) { // output
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                PreservedFletchingTableMenu.this.inventory.removeItem(0, 1);
                PreservedFletchingTableMenu.this.inventory.removeItem(1, 1);
                PreservedFletchingTableMenu.this.inventory.removeItem(2, 1);
                if (!PreservedFletchingTableMenu.this.inventory.getItem(3).isEmpty()) {
                    ItemEntity itementity = player.drop(new ItemStack(Items.GLASS_BOTTLE), false);
                    if (itementity != null) {
                        itementity.setNoPickUpDelay();
                        itementity.setTarget(player.getUUID());
                    }
                }
                PreservedFletchingTableMenu.this.inventory.removeItem(3, 1);
            }
        });

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addDataSlots(arrayPropertyDelegate);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.getContainerSize()) {
                if (!this.moveItemStackTo(originalStack, this.inventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(originalStack, 0, this.inventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
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
