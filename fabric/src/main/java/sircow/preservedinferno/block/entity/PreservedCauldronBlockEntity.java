package sircow.preservedinferno.block.entity;

import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
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
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.PreservedInferno;
import sircow.preservedinferno.recipe.CauldronRecipe;
import sircow.preservedinferno.recipe.CauldronRecipeInput;
import sircow.preservedinferno.recipe.ModRecipes;
import sircow.preservedinferno.screen.PreservedCauldronMenu;
import sircow.preservedinferno.sound.ModSounds;

import java.util.Optional;

@SuppressWarnings("rawtypes")
public class PreservedCauldronBlockEntity extends BaseContainerBlockEntity implements ExtendedMenuProvider {
    private NonNullList<ItemStack> inventory = NonNullList.withSize(3, ItemStack.EMPTY);
    private static final int INPUT_SLOT = 0;
    private static final int INPUT_SLOT_TWO = 1;
    private static final int OUTPUT_SLOT = 2;
    protected final ContainerData propertyDelegate, propertyDelegateTwo;
    public int progress, progressWater = 0;
    public int maxProgress = 100;
    public int maxWaterProgress = 64;
    private boolean needsInitialUpdate = true;

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
    protected void setItems(@NonNull NonNullList<ItemStack> items) {
        this.inventory = items;
    }

    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, this.inventory, false);
        output.putInt("CauldronProgress", this.progress);
        output.putInt("CauldronWaterProgress", this.progressWater);
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);
        ContainerHelper.loadAllItems(input, this.inventory);
        this.progress = input.getIntOr("CauldronProgress", 0);
        this.progressWater = input.getIntOr("CauldronWaterProgress", 0);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, (blockEntity, registryAccess) -> {
            CompoundTag tag = new CompoundTag();
            tag.putInt("CauldronWaterProgress", this.progressWater);
            tag.putInt("CauldronMaxWaterProgress", this.maxWaterProgress);
            return tag;
        });
    }

    public void onDataPacket(ClientboundBlockEntityDataPacket packet) {
        CompoundTag tag = packet.getTag();
        this.progressWater = tag.getIntOr("CauldronWaterProgress", 0);
        this.maxWaterProgress = tag.getIntOr("CauldronMaxWaterProgress", 64);
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public void sendInitialUpdate() {
        if (level != null && !level.isClientSide()) {
            setChanged(level, worldPosition, getBlockState());
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            needsInitialUpdate = false;
        }
    }

    @Override
    public @NotNull Component getDefaultName() {
        return Component.translatable("block.minecraft.cauldron");
    }

    @Override
    public @NotNull AbstractContainerMenu createMenu(int syncId, @NonNull Inventory playerInventory) {
        return new PreservedCauldronMenu(syncId, playerInventory, this.propertyDelegate, this.propertyDelegateTwo, this);
    }

    public static void tick(ServerLevel level, BlockPos pos, BlockState state, PreservedCauldronBlockEntity cauldron) {
        if (level.isClientSide()) return;
        if (cauldron.needsInitialUpdate) cauldron.sendInitialUpdate();

        if (cauldron.isOutputSlotEmptyOrReceivable()) {
            if (cauldron.hasRecipe() && cauldron.progressWater > 0) {
                cauldron.increaseCraftProgress();
                setChanged(level, pos, state);

                if (cauldron.hasCraftingFinished()) {
                    cauldron.craftItem();
                    cauldron.resetProgress();
                }
            }
            else cauldron.resetProgress();
        }
        else {
            cauldron.resetProgress();
            setChanged(level, pos, state);
        }
        cauldron.insertWater();
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private void craftItem() {
        Optional<RecipeHolder<CauldronRecipe>> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) return;

        var outputTemplate = recipe.get().value().output();
        ItemStack inputStack = this.getItem(INPUT_SLOT);

        if (inputStack.has(DataComponents.DYED_COLOR)) {
            ItemStack result = inputStack.copy();
            result.remove(DataComponents.DYED_COLOR);
            this.setItem(OUTPUT_SLOT, result);
        }
        else if (outputTemplate != null) {
            ItemStack templateStack = outputTemplate.create();
            templateStack.setCount(this.getItem(OUTPUT_SLOT).getCount() + templateStack.getCount());
            this.setItem(OUTPUT_SLOT, templateStack);
        }

        this.removeItem(INPUT_SLOT, 1);
        this.progressWater -= 1;
        setChanged();

        if (level != null) {
            if (!level.isClientSide()) level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
            level.playSound(null, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), ModSounds.CAULDRON_BUBBLE, SoundSource.BLOCKS);
        }
    }

    private boolean hasCraftingFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftProgress() {
        progress++;
    }

    private void insertWater() {
        boolean waterProgressChanged = false;

        PotionContents potionContentsComponent = getItem(INPUT_SLOT_TWO).get(DataComponents.POTION_CONTENTS);
        // water bucket
        if ((getItem(INPUT_SLOT_TWO).getItem() == Items.WATER_BUCKET)
                && ((this.progressWater != this.maxWaterProgress)
                || (this.progressWater + 8 < this.maxWaterProgress))) {
            ItemStack emptyBucket = new ItemStack(Items.BUCKET);
            this.progressWater += 8;
            this.removeItem(INPUT_SLOT_TWO, 1);
            this.setItem(INPUT_SLOT_TWO, new ItemStack(emptyBucket.getItem()));
            if (level != null) level.playSound(null, getBlockPos(), SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            waterProgressChanged = true;
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
            if (level != null) level.playSound(null, getBlockPos(), SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            waterProgressChanged = true;
        }
        // cap water limit at 64
        if (this.progressWater > this.maxWaterProgress) this.progressWater = 64;
        if (waterProgressChanged) {
            if (level != null && !level.isClientSide()) {
                setChanged(level, worldPosition, getBlockState());
                level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
            }
        }
    }

    private boolean hasRecipe() {
        ItemStack input = inventory.getFirst();
        if (isLeatherArmor(input) && !input.has(DataComponents.DYED_COLOR)) return false;

        Optional<RecipeHolder<CauldronRecipe>> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) return false;

        ItemStack outputStack = recipe.get().value().output().create();

        return this.progressWater >= 1 && canInsertAmountIntoOutputSlot(outputStack) && canInsertItemIntoOutputSlot(outputStack);
    }

    private Optional<RecipeHolder<CauldronRecipe>> getCurrentRecipe() {
        if (!(this.getLevel() instanceof ServerLevel serverLevel)) return Optional.empty();

        ItemStack original = inventory.getFirst();

        if (original.has(DataComponents.DYED_COLOR) && original.getItem().getDescriptionId().contains("leather_")) {
            ItemStack normalized = original.copy();
            normalized.remove(DataComponents.DYED_COLOR);
            return serverLevel.recipeAccess().getRecipeFor(ModRecipes.CAULDRON_TYPE, new CauldronRecipeInput(normalized), serverLevel);
        }
        return serverLevel.recipeAccess().getRecipeFor(ModRecipes.CAULDRON_TYPE, new CauldronRecipeInput(original), serverLevel);
    }

    private boolean isLeatherArmor(ItemStack stack) {
        Item item = stack.getItem();
        String id = item.getDescriptionId();
        return id.contains("leather_helmet") || id.contains("leather_chestplate") || id.contains("leather_leggings") || id.contains("leather_boots");
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack item) {
        return this.getItem(OUTPUT_SLOT).getItem() == item.getItem() || this.getItem(OUTPUT_SLOT).isEmpty();
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
    public Object getScreenOpeningData(@NonNull ServerPlayer serverPlayer) {
        return new PreservedCauldronBlockData(this.getBlockPos());
    }

    @Override
    public void setChanged() {
        super.setChanged();
    }
}
