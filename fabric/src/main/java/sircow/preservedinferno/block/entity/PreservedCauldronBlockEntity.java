package sircow.preservedinferno.block.entity;

import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
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
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.PreservedInferno;
import sircow.preservedinferno.block.ModBlockProperties;
import sircow.preservedinferno.fluid.CauldronFluid;
import sircow.preservedinferno.item.ModItems;
import sircow.preservedinferno.recipe.CauldronRecipe;
import sircow.preservedinferno.recipe.CauldronRecipeInput;
import sircow.preservedinferno.recipe.ModRecipes;
import sircow.preservedinferno.screen.PreservedCauldronMenu;
import sircow.preservedinferno.sound.ModSounds;

import java.util.*;

@SuppressWarnings("rawtypes")
public class PreservedCauldronBlockEntity extends BaseContainerBlockEntity implements ExtendedMenuProvider, WorldlyContainer {
    private NonNullList<ItemStack> inventory = NonNullList.withSize(4, ItemStack.EMPTY);
    private static final int INPUT_SLOT = 0;
    private static final int INPUT_SLOT_TWO = 1;
    private static final int OUTPUT_SLOT = 2;
    private static final int OUTPUT_SLOT_TWO = 3;
    protected final ContainerData propertyDelegate, propertyDelegateTwo;
    public int progress, fluidAmount;
    public int maxProgress = 100;
    public int maxFluidAmount = 64;
    public int fluidValueBottle = 1;
    public int fluidValueBucket = 8;
    public int fluidValueHoneyBlock = 4;
    private boolean needsInitialUpdate = true;
    public CauldronFluid fluid = CauldronFluid.EMPTY;
    private final Map<UUID, Integer> snowCauldronTimers = new HashMap<>();
    private final Set<Integer> entitiesInHoney = new HashSet<>();

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
                    case 0 -> PreservedCauldronBlockEntity.this.fluidAmount;
                    case 1 -> PreservedCauldronBlockEntity.this.maxFluidAmount;
                    case 2 -> PreservedCauldronBlockEntity.this.fluid.ordinal();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> PreservedCauldronBlockEntity.this.fluidAmount = value;
                    case 1 -> PreservedCauldronBlockEntity.this.maxFluidAmount = value;
                    case 2 -> {
                        CauldronFluid[] values = CauldronFluid.values();
                        PreservedCauldronBlockEntity.this.fluid = value >= 0 && value < values.length ? values[value] : CauldronFluid.EMPTY;
                    }
                }
            }

            @Override
            public int getCount() {
                return 3;
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
        output.putInt("CauldronFluidAmount", this.fluidAmount);
        output.putString("CauldronFluid", this.fluid.name());
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);
        ContainerHelper.loadAllItems(input, this.inventory);
        this.progress = input.getIntOr("CauldronProgress", 0);
        this.fluidAmount = input.getIntOr("CauldronFluidAmount", 0);
        this.fluid = CauldronFluid.valueOf(input.getStringOr("CauldronFluid", "EMPTY"));
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NonNull CompoundTag getUpdateTag(HolderLookup.@NonNull Provider provider) {
        return this.saveWithoutMetadata(provider);
    }

    public void sendInitialUpdate() {
        if (level != null && !level.isClientSide()) {
            this.setChanged();
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
        boolean shouldBeLit = (cauldron.fluid == CauldronFluid.LAVA);
        if (state.hasProperty(ModBlockProperties.IS_LIT) && state.getValue(ModBlockProperties.IS_LIT) != shouldBeLit) {
            BlockState newState = state.setValue(ModBlockProperties.IS_LIT, shouldBeLit);
            level.setBlock(pos, newState, 3);
            level.sendBlockUpdated(pos, state, newState, 3);
        }

        if (level.isClientSide()) return;
        if (cauldron.needsInitialUpdate) cauldron.sendInitialUpdate();

        cauldron.snowCauldronTimers.entrySet().removeIf(entry -> {
            Entity entity = level.getEntity(entry.getKey());
            return entity == null || !entity.getBoundingBox().intersects(Shapes.block().bounds().move(pos));
        });

        cauldron.entitiesInHoney.removeIf(id -> {
            Entity entity = level.getEntity(id);
            if (entity == null) return true;

            BlockPos entityPos = entity.blockPosition();
            return !entityPos.equals(cauldron.worldPosition);
        });

        ItemStack fluidStack = cauldron.getItem(INPUT_SLOT_TWO);

        if (!fluidStack.isEmpty()) {
            cauldron.insertBottle();
            cauldron.insertBucket();
            cauldron.insertMisc();
        }

        if (!cauldron.hasRecipe()) {
            cauldron.resetProgress();
            return;
        }

        cauldron.increaseCraftProgress();
        setChanged(level, pos, state);

        if (cauldron.hasCraftingFinished()) {
            cauldron.craftItem();
            cauldron.resetProgress();
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private boolean canInsertFluid(CauldronFluid newFluid) {
        return fluid == CauldronFluid.EMPTY || fluid == newFluid;
    }

    private void craftItem() {
        var recipe = getCurrentRecipe().orElse(null);
        ItemStack inputStack = this.getItem(INPUT_SLOT);
        int amountToDeduct = 1;

        if (recipe != null) {
            var outputTemplate = recipe.value().output();
            amountToDeduct = recipe.value().fluidCost();

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
        }
        else if (!(this.fluid == CauldronFluid.LAVA && !inputStack.has(DataComponents.DAMAGE_RESISTANT))) {
            return;
        }

        this.removeItem(INPUT_SLOT, 1);
        this.fluidAmount -= amountToDeduct;

        setChanged();
        normaliseFluidState();

        if (level != null) {
            if (!level.isClientSide()) level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);

            level.playSound(null, this.getBlockPos(), ModSounds.CAULDRON_BUBBLE, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    private boolean hasCraftingFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftProgress() {
        progress++;
    }

    public void insertBottle() {
        ItemStack stack = getItem(INPUT_SLOT_TWO);
        CauldronFluid insertedFluid;

        if (stack.is(Items.POTION)) {
            PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
            if (contents == null || !contents.is(Potions.WATER)) return;
            insertedFluid = CauldronFluid.WATER;
        }
        else if (stack.is(ModItems.MILK_BOTTLE)) insertedFluid = CauldronFluid.MILK;
        else if (stack.is(Items.HONEY_BOTTLE)) insertedFluid = CauldronFluid.HONEY;
        else if (stack.is(ModItems.LAVA_BOTTLE)) insertedFluid = CauldronFluid.LAVA;
        else return;

        if (!canInsertFluid(insertedFluid)) return;
        if (fluidAmount >= maxFluidAmount) return;

        ItemStack emptyBottle = new ItemStack(Items.GLASS_BOTTLE);

        if (!canInsertItemIntoOutputSlotTwo(emptyBottle)) return;
        if (!canInsertAmountIntoOutputSlotTwo(emptyBottle)) return;

        fluid = insertedFluid;
        fluidAmount += fluidValueBottle;

        stack.shrink(1);

        ItemStack output = getItem(OUTPUT_SLOT_TWO);
        if (output.isEmpty()) setItem(OUTPUT_SLOT_TWO, emptyBottle);
        else output.grow(1);

        setChanged();
        normaliseFluidState();

        if (level != null) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            level.playSound(null, getBlockPos(), SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1F, 1F);
        }
    }

    public void insertBucket() {
        ItemStack stack = getItem(INPUT_SLOT_TWO);
        CauldronFluid insertedFluid;

        if (stack.is(Items.WATER_BUCKET)) insertedFluid = CauldronFluid.WATER;
        else if (stack.is(Items.LAVA_BUCKET)) insertedFluid = CauldronFluid.LAVA;
        else if (stack.is(Items.MILK_BUCKET)) insertedFluid = CauldronFluid.MILK;
        else if (stack.is(Items.POWDER_SNOW_BUCKET)) insertedFluid = CauldronFluid.SNOW;
        else return;

        if (!canInsertFluid(insertedFluid)) return;
        if (fluidAmount > maxFluidAmount - fluidValueBucket) return;

        ItemStack emptyBucket = new ItemStack(Items.BUCKET);

        if (!canInsertItemIntoOutputSlotTwo(emptyBucket)) return;
        if (!canInsertAmountIntoOutputSlotTwo(emptyBucket)) return;

        fluid = insertedFluid;
        fluidAmount += fluidValueBucket;

        stack.shrink(1);

        ItemStack output = getItem(OUTPUT_SLOT_TWO);
        if (output.isEmpty()) setItem(OUTPUT_SLOT_TWO, emptyBucket);
        else output.grow(1);

        setChanged();
        normaliseFluidState();

        if (level != null) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            level.playSound(null, getBlockPos(), SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1F, 1F);
        }
    }

    public void insertMisc() {
        ItemStack stack = getItem(INPUT_SLOT_TWO);
        CauldronFluid insertedFluid;

        if (stack.is(Items.HONEY_BLOCK)) insertedFluid = CauldronFluid.HONEY;
        else return;

        if (!canInsertFluid(insertedFluid)) return;
        if (fluidAmount > maxFluidAmount - fluidValueHoneyBlock) return;

        fluid = insertedFluid;
        fluidAmount += fluidValueHoneyBlock;
        fluidAmount = Math.min(fluidAmount, maxFluidAmount);

        stack.shrink(1);
        setChanged();
        normaliseFluidState();

        if (level != null) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            level.playSound(null, getBlockPos(), SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1F, 1F);
        }
    }

    private boolean hasRecipe() {
        ItemStack input = this.getItem(INPUT_SLOT);
        if (input.isEmpty()) return false;
        if (isLeatherArmor(input) && !input.has(DataComponents.DYED_COLOR)) return false;

        Optional<RecipeHolder<CauldronRecipe>> recipe = getCurrentRecipe();

        if (recipe.isPresent()) {
            ItemStack outputStack = recipe.get().value().output().create();
            int requiredFluid = recipe.get().value().fluidCost();

            return this.fluidAmount >= requiredFluid && canInsertAmountIntoOutputSlot(outputStack) && canInsertItemIntoOutputSlot(outputStack);
        }

        if (this.fluid == CauldronFluid.LAVA && !input.has(DataComponents.DAMAGE_RESISTANT)) return this.fluidAmount >= 1;

        return false;
    }

    private Optional<RecipeHolder<CauldronRecipe>> getCurrentRecipe() {
        if (!(this.getLevel() instanceof ServerLevel serverLevel)) return Optional.empty();

        ItemStack original = inventory.getFirst();

        if (original.has(DataComponents.DYED_COLOR) && original.getItem().getDescriptionId().contains("leather_")) {
            ItemStack normalized = original.copy();
            normalized.remove(DataComponents.DYED_COLOR);
            return serverLevel.recipeAccess().getRecipeFor(ModRecipes.CAULDRON_TYPE, new CauldronRecipeInput(normalized, this.fluid, this.fluidAmount), serverLevel);
        }
        return serverLevel.recipeAccess().getRecipeFor(ModRecipes.CAULDRON_TYPE, new CauldronRecipeInput(original, this.fluid, this.fluidAmount), serverLevel);
    }

    private boolean isLeatherArmor(ItemStack stack) {
        return stack.is(Items.LEATHER_HELMET) || stack.is(Items.LEATHER_CHESTPLATE) || stack.is(Items.LEATHER_LEGGINGS) || stack.is(Items.LEATHER_BOOTS);
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack item) {
        ItemStack output = this.getItem(OUTPUT_SLOT);
        return output.isEmpty() || ItemStack.isSameItemSameComponents(output, item);
    }

    private boolean canInsertAmountIntoOutputSlot(ItemStack result) {
        ItemStack output = getItem(OUTPUT_SLOT);
        return output.getCount() + result.getCount() <= output.getMaxStackSize();
    }

    private boolean canInsertItemIntoOutputSlotTwo(ItemStack item) {
        ItemStack output = this.getItem(OUTPUT_SLOT_TWO);
        return output.isEmpty() || ItemStack.isSameItemSameComponents(output, item);
    }

    private boolean canInsertAmountIntoOutputSlotTwo(ItemStack stack) {
        ItemStack output = this.getItem(OUTPUT_SLOT_TWO);
        return output.getCount() + stack.getCount() <= output.getMaxStackSize();
    }

    @Override
    public int getContainerSize() {
        return 4;
    }

    @Override
    public @NonNull Object getScreenOpeningData(@NonNull ServerPlayer serverPlayer) {
        return new PreservedCauldronBlockData(this.getBlockPos());
    }

    @Override
    public int @NonNull [] getSlotsForFace(Direction side) {
        return switch (side) {
            case UP -> new int[]{INPUT_SLOT};
            case DOWN -> new int[]{OUTPUT_SLOT, OUTPUT_SLOT_TWO};
            default -> new int[]{INPUT_SLOT_TWO};
        };
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, @NonNull ItemStack stack, Direction direction) {
        if (slot == INPUT_SLOT) return direction == Direction.UP;
        if (slot == INPUT_SLOT_TWO) return canAcceptFluidItem(stack);

        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, @NotNull ItemStack stack, @NotNull Direction direction) {
        return (slot == OUTPUT_SLOT || slot == OUTPUT_SLOT_TWO) && direction == Direction.DOWN;
    }

    public static boolean isFluidInputItem(ItemStack stack) {
        if (stack.is(Items.WATER_BUCKET)) return true;
        if (stack.is(Items.LAVA_BUCKET)) return true;
        if (stack.is(Items.MILK_BUCKET)) return true;
        if (stack.is(Items.POWDER_SNOW_BUCKET)) return true;
        if (stack.is(ModItems.MILK_BOTTLE)) return true;
        if (stack.is(Items.HONEY_BOTTLE)) return true;
        if (stack.is(ModItems.LAVA_BOTTLE)) return true;
        if (stack.is(Items.HONEY_BLOCK)) return true;

        if (stack.is(Items.POTION)) {
            PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
            return contents != null && contents.is(Potions.WATER);
        }

        return false;
    }

    private boolean canAcceptFluidItem(ItemStack stack) {
        if (!isFluidInputItem(stack)) return false;

        CauldronFluid insertedFluid;

        if (stack.is(Items.WATER_BUCKET)) insertedFluid = CauldronFluid.WATER;
        else if (stack.is(Items.LAVA_BUCKET)) insertedFluid = CauldronFluid.LAVA;
        else if (stack.is(Items.MILK_BUCKET)) insertedFluid = CauldronFluid.MILK;
        else if (stack.is(Items.POWDER_SNOW_BUCKET)) insertedFluid = CauldronFluid.SNOW;
        else if (stack.is(ModItems.MILK_BOTTLE)) insertedFluid = CauldronFluid.MILK;
        else if (stack.is(Items.HONEY_BOTTLE)) insertedFluid = CauldronFluid.HONEY;
        else if (stack.is(ModItems.LAVA_BOTTLE)) insertedFluid = CauldronFluid.LAVA;
        else if (stack.is(Items.HONEY_BLOCK)) insertedFluid = CauldronFluid.HONEY;
        else insertedFluid = CauldronFluid.EMPTY;

        return fluidAmount < maxFluidAmount && canInsertFluid(insertedFluid);
    }

    public boolean markEntityInHoney(Entity entity) {
        return this.entitiesInHoney.add(entity.getId());
    }

    public int incrementSnowCauldronTimer(Entity entity) {
        UUID uuid = entity.getUUID();
        int ticks = snowCauldronTimers.getOrDefault(uuid, 0) + 1;
        snowCauldronTimers.put(uuid, ticks);
        return ticks;
    }

    public void resetSnowCauldronTimer(Entity entity) {
        snowCauldronTimers.remove(entity.getUUID());
    }

    private void normaliseFluidState() {
        if (this.fluidAmount <= 0) {
            this.fluidAmount = 0;
            this.fluid = CauldronFluid.EMPTY;
        }
    }
}
