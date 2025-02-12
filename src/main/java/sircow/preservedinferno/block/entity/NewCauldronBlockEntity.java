package sircow.preservedinferno.block.entity;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.Potions;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import sircow.preservedinferno.item.ModItems;
import sircow.preservedinferno.screen.NewCauldronBlockScreenHandler;

import java.util.HashMap;
import java.util.Map;

public class NewCauldronBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);

    private static final int INPUT_SLOT = 0;
    private static final int INPUT_SLOT_TWO = 1;
    private static final int OUTPUT_SLOT = 2;

    protected final PropertyDelegate propertyDelegate;
    protected final PropertyDelegate propertyDelegateTwo;
    private int progress = 0;
    private int maxProgress = 100;
    private int progressWater = 0;
    private int maxWaterProgress = 64;

    // define recipes - TURN THIS INTO DATA DRIVEN
    public static final Map<Item, Item> conversionMap = new HashMap<>();
    static {
        conversionMap.put(ModItems.RAW_HIDE, Items.LEATHER);
        conversionMap.put(Items.DIRT, Items.MUD);
        conversionMap.put(Items.COARSE_DIRT, Items.MUD);
        // concrete
        conversionMap.put(Items.BLACK_CONCRETE_POWDER, Items.BLACK_CONCRETE);
        conversionMap.put(Items.BLUE_CONCRETE_POWDER, Items.BLUE_CONCRETE);
        conversionMap.put(Items.BROWN_CONCRETE_POWDER, Items.BROWN_CONCRETE);
        conversionMap.put(Items.CYAN_CONCRETE_POWDER, Items.CYAN_CONCRETE);
        conversionMap.put(Items.GRAY_CONCRETE_POWDER, Items.GRAY_CONCRETE);
        conversionMap.put(Items.GREEN_CONCRETE_POWDER, Items.GREEN_CONCRETE);
        conversionMap.put(Items.LIGHT_BLUE_CONCRETE_POWDER, Items.LIGHT_BLUE_CONCRETE);
        conversionMap.put(Items.LIGHT_GRAY_CONCRETE_POWDER, Items.LIGHT_GRAY_CONCRETE);
        conversionMap.put(Items.LIME_CONCRETE_POWDER, Items.LIME_CONCRETE);
        conversionMap.put(Items.MAGENTA_CONCRETE_POWDER, Items.MAGENTA_CONCRETE);
        conversionMap.put(Items.ORANGE_CONCRETE_POWDER, Items.ORANGE_CONCRETE);
        conversionMap.put(Items.PINK_CONCRETE_POWDER, Items.PINK_CONCRETE);
        conversionMap.put(Items.PURPLE_CONCRETE_POWDER, Items.PURPLE_CONCRETE);
        conversionMap.put(Items.RED_CONCRETE_POWDER, Items.RED_CONCRETE);
        conversionMap.put(Items.WHITE_CONCRETE_POWDER, Items.WHITE_CONCRETE);
        conversionMap.put(Items.YELLOW_CONCRETE_POWDER, Items.YELLOW_CONCRETE);
        // terracotta
        conversionMap.put(Items.TERRACOTTA, Items.CLAY);
        conversionMap.put(Items.BLACK_TERRACOTTA, Items.CLAY);
        conversionMap.put(Items.BLUE_TERRACOTTA, Items.CLAY);
        conversionMap.put(Items.BROWN_TERRACOTTA, Items.CLAY);
        conversionMap.put(Items.CYAN_TERRACOTTA, Items.CLAY);
        conversionMap.put(Items.GRAY_TERRACOTTA, Items.CLAY);
        conversionMap.put(Items.GREEN_TERRACOTTA, Items.CLAY);
        conversionMap.put(Items.LIGHT_BLUE_TERRACOTTA, Items.CLAY);
        conversionMap.put(Items.LIGHT_GRAY_TERRACOTTA, Items.CLAY);
        conversionMap.put(Items.LIME_TERRACOTTA, Items.CLAY);
        conversionMap.put(Items.MAGENTA_TERRACOTTA, Items.CLAY);
        conversionMap.put(Items.ORANGE_TERRACOTTA, Items.CLAY);
        conversionMap.put(Items.PINK_TERRACOTTA, Items.CLAY);
        conversionMap.put(Items.PURPLE_TERRACOTTA, Items.CLAY);
        conversionMap.put(Items.RED_TERRACOTTA, Items.CLAY);
        conversionMap.put(Items.WHITE_TERRACOTTA, Items.CLAY);
        conversionMap.put(Items.YELLOW_TERRACOTTA, Items.CLAY);
        // wool
        conversionMap.put(Items.BLACK_WOOL, Items.WHITE_WOOL);
        conversionMap.put(Items.BLUE_WOOL, Items.WHITE_WOOL);
        conversionMap.put(Items.BROWN_WOOL, Items.WHITE_WOOL);
        conversionMap.put(Items.CYAN_WOOL, Items.WHITE_WOOL);
        conversionMap.put(Items.GRAY_WOOL, Items.WHITE_WOOL);
        conversionMap.put(Items.GREEN_WOOL, Items.WHITE_WOOL);
        conversionMap.put(Items.LIGHT_BLUE_WOOL, Items.WHITE_WOOL);
        conversionMap.put(Items.LIGHT_GRAY_WOOL, Items.WHITE_WOOL);
        conversionMap.put(Items.LIME_WOOL, Items.WHITE_WOOL);
        conversionMap.put(Items.MAGENTA_WOOL, Items.WHITE_WOOL);
        conversionMap.put(Items.ORANGE_WOOL, Items.WHITE_WOOL);
        conversionMap.put(Items.PINK_WOOL, Items.WHITE_WOOL);
        conversionMap.put(Items.PURPLE_WOOL, Items.WHITE_WOOL);
        conversionMap.put(Items.RED_WOOL, Items.WHITE_WOOL);
        conversionMap.put(Items.YELLOW_WOOL, Items.WHITE_WOOL);
    }

    public NewCauldronBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.NEW_CAULDRON_BLOCK_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> NewCauldronBlockEntity.this.progress;
                    case 1 -> NewCauldronBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> NewCauldronBlockEntity.this.progress = value;
                    case 1 -> NewCauldronBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
        this.propertyDelegateTwo = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> NewCauldronBlockEntity.this.progressWater;
                    case 1 -> NewCauldronBlockEntity.this.maxWaterProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> NewCauldronBlockEntity.this.progressWater = value;
                    case 1 -> NewCauldronBlockEntity.this.maxWaterProgress = value;
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    @Override
    public Object getScreenOpeningData(ServerPlayerEntity player) {
        return new NewCauldronBlockData(this.pos);

    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, this.inventory, false, registryLookup);
        nbt.putInt("newCauldronProgress", this.progress);
        nbt.putInt("newCauldronWaterProgress", this.progressWater);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.progress = nbt.getInt("newCauldronProgress");
        this.progressWater = nbt.getInt("newCauldronWaterProgress");
        Inventories.readNbt(nbt, this.inventory, registryLookup);
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Cauldron");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new NewCauldronBlockScreenHandler(syncId, playerInventory, this, this.propertyDelegate, this.propertyDelegateTwo);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if(world.isClient()) {
            return;
        }

        if(isOutputSlotEmptyOrReceivable()) {
            if(this.hasRecipe() && this.progressWater > 0) {
                this.increaseCraftProgress();
                markDirty(world, pos, state);

                if(hasCraftingFinished()) {
                    this.craftItem();
                    this.resetProgress();
                }
            } else {
                this.resetProgress();
            }
        } else {
            this.resetProgress();
            markDirty(world, pos, state);
        }

        insertWater();
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private void craftItem() {
        Item inputItem = this.getStack(INPUT_SLOT).getItem();
        Item outputItem = conversionMap.get(inputItem);

        // start crafting process if recipe is successful
        // dyed items
        if (this.getStack(INPUT_SLOT).contains(DataComponentTypes.DYED_COLOR)) {
            ItemStack result = this.getStack(INPUT_SLOT).copy();
            result.remove(DataComponentTypes.DYED_COLOR);
            this.setStack(OUTPUT_SLOT, result);
        }
        // everything else
        else if (outputItem != null) {
            ItemStack result = new ItemStack(outputItem);
            this.setStack(OUTPUT_SLOT, new ItemStack(result.getItem(), getStack(OUTPUT_SLOT).getCount() + result.getCount()));
        }
        this.removeStack(INPUT_SLOT, 1);
        this.progressWater -= 1;
    }

    private boolean hasCraftingFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftProgress() {
        progress++;
    }

    private void insertWater() {
        PotionContentsComponent potionContentsComponent = getStack(INPUT_SLOT_TWO).get(DataComponentTypes.POTION_CONTENTS);
        // water bucket
        if ((getStack(INPUT_SLOT_TWO).getItem() == Items.WATER_BUCKET)
                && ((this.progressWater != this.maxWaterProgress)
                || (this.progressWater + 8 < this.maxWaterProgress))) {
            ItemStack emptyBucket = new ItemStack(Items.BUCKET);
            this.progressWater += 8;
            this.removeStack(INPUT_SLOT_TWO, 1);
            this.setStack(INPUT_SLOT_TWO, new ItemStack(emptyBucket.getItem()));
            assert world != null;
            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
        // water bottle
        else if ((getStack(INPUT_SLOT_TWO).getItem() == Items.POTION && (potionContentsComponent != null && potionContentsComponent.matches(Potions.WATER)))
                && ((this.progressWater != this.maxWaterProgress)
                || (this.progressWater + (getStack(INPUT_SLOT_TWO).getCount() * 2) < this.maxWaterProgress))) {
            int stackSize = getStack(INPUT_SLOT_TWO).getCount();
            this.progressWater += stackSize * 2;
            ItemStack emptyBottle = new ItemStack(Items.GLASS_BOTTLE);
            this.removeStack(INPUT_SLOT_TWO, 1);
            this.setStack(INPUT_SLOT_TWO, new ItemStack(emptyBottle.getItem(), stackSize));
            assert world != null;
            world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
        // cap water limit at 64
        if (this.progressWater > this.maxWaterProgress) {
            this.progressWater = 64;
        }
    }

    private boolean hasRecipe() {
        // check if inserted item has recipe
        Item inputItem = this.getStack(INPUT_SLOT).getItem();
        Item outputItem = conversionMap.get(inputItem);

        if (outputItem != null) {
            ItemStack result = new ItemStack(outputItem);
            boolean hasInput = getStack(INPUT_SLOT).getItem() == inputItem;
            return hasInput && this.progressWater >= 1 && canInsertAmountIntoOutputSlot(result) && canInsertItemIntoOutputSlot(result.getItem());
        }
        else return this.getStack(INPUT_SLOT).contains(DataComponentTypes.DYED_COLOR);
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.getStack(OUTPUT_SLOT).getItem() == item || this.getStack(OUTPUT_SLOT).isEmpty();
    }

    private boolean canInsertAmountIntoOutputSlot(ItemStack result) {
        return this.getStack(OUTPUT_SLOT).getCount() + result.getCount() <= getStack(OUTPUT_SLOT).getMaxCount();
    }

    private boolean isOutputSlotEmptyOrReceivable() {
        return this.getStack(OUTPUT_SLOT).isEmpty() || this.getStack(OUTPUT_SLOT).getCount() < this.getStack(OUTPUT_SLOT).getMaxCount();
    }
}
