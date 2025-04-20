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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import sircow.preservedinferno.PreservedInferno;
import sircow.preservedinferno.item.ModItems;
import sircow.preservedinferno.screen.PreservedCauldronMenu;

import java.util.HashMap;
import java.util.Map;

public class PreservedCauldronBlockEntity extends BaseContainerBlockEntity implements ExtendedScreenHandlerFactory {
    private NonNullList<ItemStack> inventory = NonNullList.withSize(3, ItemStack.EMPTY);

    private static final int INPUT_SLOT = 0;
    private static final int INPUT_SLOT_TWO = 1;
    private static final int OUTPUT_SLOT = 2;

    protected final ContainerData propertyDelegate;
    protected final ContainerData propertyDelegateTwo;
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
        conversionMap.put(Items.ROOTED_DIRT, Items.MUD);
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

    public PreservedCauldronBlockEntity(BlockPos pos, BlockState state) {
        super(PreservedInferno.PRESERVED_CAULDRON_BLOCK_ENTITY, pos, state);
        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> PreservedCauldronBlockEntity.this.progress;
                    case 1 -> PreservedCauldronBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> PreservedCauldronBlockEntity.this.progress = value;
                    case 1 -> PreservedCauldronBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
        this.propertyDelegateTwo = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> PreservedCauldronBlockEntity.this.progressWater;
                    case 1 -> PreservedCauldronBlockEntity.this.maxWaterProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> PreservedCauldronBlockEntity.this.progressWater = value;
                    case 1 -> PreservedCauldronBlockEntity.this.maxWaterProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public @NotNull NonNullList<ItemStack> getItems() {
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
        nbt.putInt("newCauldronProgress", this.progress);
        nbt.putInt("newCauldronWaterProgress", this.progressWater);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
        this.progress = nbt.getIntOr("newCauldronProgress", 0);
        this.progressWater = nbt.getIntOr("newCauldronWaterProgress", 0);
        ContainerHelper.loadAllItems(nbt, this.inventory, registryLookup);
    }

    @Override
    public Component getDefaultName() {
        return Component.literal("Cauldron");
    }

    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
        return new PreservedCauldronMenu(syncId, playerInventory, this.propertyDelegate, this.propertyDelegateTwo, this);
    }

    public static void tick(ServerLevel world, BlockPos pos, BlockState state, PreservedCauldronBlockEntity cauldron) {
        if (world.isClientSide()) {
            return;
        }

        if (cauldron.isOutputSlotEmptyOrReceivable()) {
            if (cauldron.hasRecipe() && cauldron.progressWater > 0) {
                cauldron.increaseCraftProgress();
                setChanged(world, pos, state);

                if (cauldron.hasCraftingFinished()) {
                    cauldron.craftItem();
                    cauldron.resetProgress();
                }
            } else {
                cauldron.resetProgress();
            }
        } else {
            cauldron.resetProgress();
            setChanged(world, pos, state);
        }

        cauldron.insertWater();
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private void craftItem() {
        Item inputItem = this.getItem(INPUT_SLOT).getItem();
        Item outputItem = conversionMap.get(inputItem);

        // start crafting process if recipe is successful
        // dyed items
        if (this.getItem(INPUT_SLOT).has(DataComponents.DYED_COLOR)) {
            ItemStack result = this.getItem(INPUT_SLOT).copy();
            result.remove(DataComponents.DYED_COLOR);
            this.setItem(OUTPUT_SLOT, result);
        }
        // everything else
        else if (outputItem != null) {
            ItemStack result = new ItemStack(outputItem);
            this.setItem(OUTPUT_SLOT, new ItemStack(result.getItem(), getItem(OUTPUT_SLOT).getCount() + result.getCount()));
        }
        this.removeItem(INPUT_SLOT, 1);
        this.progressWater -= 1;
        setChanged();
    }

    private boolean hasCraftingFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftProgress() {
        progress++;
    }

    private void insertWater() {
        PotionContents potionContentsComponent = getItem(INPUT_SLOT_TWO).get(DataComponents.POTION_CONTENTS);
        // water bucket
        if ((getItem(INPUT_SLOT_TWO).getItem() == Items.WATER_BUCKET)
                && ((this.progressWater != this.maxWaterProgress)
                || (this.progressWater + 8 < this.maxWaterProgress))) {
            ItemStack emptyBucket = new ItemStack(Items.BUCKET);
            this.progressWater += 8;
            this.removeItem(INPUT_SLOT_TWO, 1);
            this.setItem(INPUT_SLOT_TWO, new ItemStack(emptyBucket.getItem()));
            if (level != null) {
                level.playSound(null, getBlockPos(), SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }
        // water bottle
        else if ((getItem(INPUT_SLOT_TWO).getItem() == Items.POTION && (potionContentsComponent != null && potionContentsComponent.is(Potions.WATER)))
                && ((this.progressWater != this.maxWaterProgress)
                || (this.progressWater + (getItem(INPUT_SLOT_TWO).getCount() * 2) < this.maxWaterProgress))) {
            int stackSize = getItem(INPUT_SLOT_TWO).getCount();
            this.progressWater += stackSize * 2;
            ItemStack emptyBottle = new ItemStack(Items.GLASS_BOTTLE);
            this.removeItem(INPUT_SLOT_TWO, 1);
            this.setItem(INPUT_SLOT_TWO, new ItemStack(emptyBottle.getItem(), stackSize));
            if (level != null) {
                level.playSound(null, getBlockPos(), SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }
        // cap water limit at 64
        if (this.progressWater > this.maxWaterProgress) {
            this.progressWater = 64;
        }

        setChanged();
    }

    private boolean hasRecipe() {
        // check if inserted item has recipe
        Item inputItem = this.getItem(INPUT_SLOT).getItem();
        Item outputItem = conversionMap.get(inputItem);

        if (outputItem != null) {
            ItemStack result = new ItemStack(outputItem);
            boolean hasInput = getItem(INPUT_SLOT).getItem() == inputItem;
            return hasInput && this.progressWater >= 1 && canInsertAmountIntoOutputSlot(result) && canInsertItemIntoOutputSlot(result.getItem());
        }
        else return this.getItem(INPUT_SLOT).has(DataComponents.DYED_COLOR);
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.getItem(OUTPUT_SLOT).getItem() == item || this.getItem(OUTPUT_SLOT).isEmpty();
    }

    private boolean canInsertAmountIntoOutputSlot(ItemStack result) {
        return this.getItem(OUTPUT_SLOT).getCount() + result.getCount() <= getItem(OUTPUT_SLOT).getMaxStackSize();
    }

    private boolean isOutputSlotEmptyOrReceivable() {
        return this.getItem(OUTPUT_SLOT).isEmpty() || this.getItem(OUTPUT_SLOT).getCount() < this.getItem(OUTPUT_SLOT).getMaxStackSize();
    }

    @Override
    public int getContainerSize() {
        return 3;
    }

    @Override
    public Object getScreenOpeningData(ServerPlayer serverPlayer) {
        return new PreservedCauldronBlockData(this.getBlockPos());
    }

    @Override
    public void setChanged() {
        super.setChanged();
    }
}
