package sircow.placeholder.block.entity;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import sircow.placeholder.screen.NewFletchingTableBlockScreenHandler;

public class NewFletchingTableBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(5, ItemStack.EMPTY);

    private static final int INPUT_SLOT = 0;
    private static final int INPUT_SLOT_TWO = 1;
    private static final int INPUT_SLOT_THREE = 2;
    private static final int INPUT_SLOT_FOUR = 3;
    private static final int OUTPUT_SLOT = 4;

    protected final PropertyDelegate propertyDelegate;

    public NewFletchingTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.NEW_FLETCHING_TABLE_BLOCK_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) { return 0; }

            @Override
            public void set(int index, int value) { }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    @Override
    public Object getScreenOpeningData(ServerPlayerEntity player) {
        return new NewFletchingTableBlockData(this.pos);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, this.inventory, false, registryLookup);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        Inventories.readNbt(nbt, this.inventory, registryLookup);
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Fletching Table");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new NewFletchingTableBlockScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if(world.isClient()) {
            return;
        }

        if (isOutputSlotEmptyOrReceivable()) {
            markDirty(world, pos, state);
            this.craftItem();
        }
    }

    private void craftItem() {
        Item inputItem = this.getStack(INPUT_SLOT).getItem();
        Item inputItem2 = this.getStack(INPUT_SLOT_TWO).getItem();
        Item inputItem3 = this.getStack(INPUT_SLOT_THREE).getItem();
        Item inputItem4 = this.getStack(INPUT_SLOT_FOUR).getItem();
        Item outputItem = Items.ARROW;

        // start crafting process if recipe is successful
        if (inputItem == Items.FLINT && inputItem2 == Items.STICK && inputItem3 == Items.FEATHER) {
            ItemStack result;
            if (inputItem4 instanceof PotionItem) {
                result = new ItemStack(Items.TIPPED_ARROW, 16);
                result.set(DataComponentTypes.POTION_CONTENTS, getStack(INPUT_SLOT_FOUR).get(DataComponentTypes.POTION_CONTENTS));
            }
            else {
               result = new ItemStack(outputItem, 16);
            }

            if (this.getStack(OUTPUT_SLOT).isEmpty()) {
                this.setStack(OUTPUT_SLOT, new ItemStack(result.getItem(), getStack(OUTPUT_SLOT).getCount() + result.getCount()));
                if (this.getStack(INPUT_SLOT_FOUR) != null){
                    this.getStack(OUTPUT_SLOT).set(DataComponentTypes.POTION_CONTENTS, getStack(INPUT_SLOT_FOUR).get(DataComponentTypes.POTION_CONTENTS));
                }
                this.removeStack(INPUT_SLOT, 1);
                this.removeStack(INPUT_SLOT_TWO, 1);
                this.removeStack(INPUT_SLOT_THREE, 1);
                this.removeStack(INPUT_SLOT_FOUR, 1);
            }
        }
        else {
            this.setStack(OUTPUT_SLOT, ItemStack.EMPTY);
        }
    }

    private boolean isOutputSlotEmptyOrReceivable() {
        return this.getStack(OUTPUT_SLOT).isEmpty() || this.getStack(OUTPUT_SLOT).getCount() < this.getStack(OUTPUT_SLOT).getMaxCount();
    }
}
