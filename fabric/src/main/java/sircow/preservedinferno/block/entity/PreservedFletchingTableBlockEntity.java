package sircow.preservedinferno.block.entity;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import sircow.preservedinferno.PreservedInferno;
import sircow.preservedinferno.screen.PreservedFletchingTableMenu;

public class PreservedFletchingTableBlockEntity extends BaseContainerBlockEntity implements ExtendedScreenHandlerFactory {
    private NonNullList<ItemStack> inventory = NonNullList.withSize(5, ItemStack.EMPTY);

    private static final int INPUT_SLOT = 0;
    private static final int INPUT_SLOT_TWO = 1;
    private static final int INPUT_SLOT_THREE = 2;
    private static final int INPUT_SLOT_FOUR = 3;
    private static final int OUTPUT_SLOT = 4;

    protected final ContainerData propertyDelegate;

    public PreservedFletchingTableBlockEntity(BlockPos pos, BlockState state) {
        super(PreservedInferno.PRESERVED_FLETCHING_TABLE_BLOCK_ENTITY, pos, state);
        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) { return 0; }

            @Override
            public void set(int index, int value) { }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Object getScreenOpeningData(ServerPlayer player) {
        return new PreservedFletchingTableBlockData(this.getBlockPos());
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.inventory = items;
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        ContainerHelper.saveAllItems(nbt, this.inventory, false, registryLookup);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
        ContainerHelper.loadAllItems(nbt, this.inventory, registryLookup);
    }

    @Override
    public Component getDefaultName() {
        return Component.literal("Fletching Table");
    }

    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
        return new PreservedFletchingTableMenu(syncId, playerInventory, this.propertyDelegate, this);
    }

    public static void tick(ServerLevel world, BlockPos pos, BlockState state, PreservedFletchingTableBlockEntity fletchingTableBlock) {
        if (world.isClientSide) {
            return;
        }

        if (fletchingTableBlock.isOutputSlotEmptyOrReceivable()) {
            setChanged(world, pos, state);
            fletchingTableBlock.craftItem();
        }
    }

    private void craftItem() {
        Item inputItem = this.getItem(INPUT_SLOT).getItem();
        Item inputItem2 = this.getItem(INPUT_SLOT_TWO).getItem();
        Item inputItem3 = this.getItem(INPUT_SLOT_THREE).getItem();
        Item inputItem4 = this.getItem(INPUT_SLOT_FOUR).getItem();
        Item outputItem = Items.ARROW;

        // start crafting process if recipe is successful
        if (inputItem == Items.FLINT && inputItem2 == Items.STICK && inputItem3 == Items.FEATHER) {
            ItemStack result;
            if (inputItem4 instanceof PotionItem) {
                if (this.getItem(OUTPUT_SLOT).getItem() == Items.ARROW) {
                    this.setItem(OUTPUT_SLOT, ItemStack.EMPTY);
                    setChanged();
                }
                result = new ItemStack(Items.TIPPED_ARROW, 16);
                result.set(DataComponents.POTION_CONTENTS, getItem(INPUT_SLOT_FOUR).get(DataComponents.POTION_CONTENTS));
                setChanged();
            }
            else {
               result = new ItemStack(outputItem, 16);
            }

            if (this.getItem(OUTPUT_SLOT).isEmpty()) {
                this.setItem(OUTPUT_SLOT, new ItemStack(result.getItem(), getItem(OUTPUT_SLOT).getCount() + result.getCount()));
                if (!this.getItem(INPUT_SLOT_FOUR).isEmpty()) {
                    this.getItem(OUTPUT_SLOT).set(DataComponents.POTION_CONTENTS, getItem(INPUT_SLOT_FOUR).get(DataComponents.POTION_CONTENTS));
                }
                setChanged();
            }
            else if (this.getItem(OUTPUT_SLOT).getItem() == Items.TIPPED_ARROW) {
                if (this.getItem(INPUT_SLOT_FOUR).isEmpty()) {
                    if (!this.getItem(INPUT_SLOT).isEmpty() && !this.getItem(INPUT_SLOT_TWO).isEmpty() && !this.getItem(INPUT_SLOT_THREE).isEmpty())
                    {
                        this.setItem(OUTPUT_SLOT, new ItemStack(Items.ARROW, 16));
                    }
                    else {
                        this.setItem(OUTPUT_SLOT, ItemStack.EMPTY);
                    }
                }
                setChanged();
            }
        }
        else {
            this.setItem(OUTPUT_SLOT, ItemStack.EMPTY);
        }
        setChanged();
    }

    private boolean isOutputSlotEmptyOrReceivable() {
        return this.getItem(OUTPUT_SLOT).isEmpty() || this.getItem(OUTPUT_SLOT).getCount() < this.getItem(OUTPUT_SLOT).getMaxStackSize();
    }

    @Override
    public int getContainerSize() {
        return 5;
    }

    @Override
    public void setChanged() {
        super.setChanged();
    }
}
