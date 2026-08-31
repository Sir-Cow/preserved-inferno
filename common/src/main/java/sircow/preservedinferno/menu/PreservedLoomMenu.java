package sircow.preservedinferno.menu;

import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.recipe.*;
import sircow.preservedinferno.trigger.ModTriggers;

import java.util.Optional;

public class PreservedLoomMenu extends AbstractContainerMenu {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(4, ItemStack.EMPTY);
    private final Slot inputSlotOne, inputSlotTwo, shearsSlot, resultSlot;
    private final ContainerLevelAccess access;
    private Level world;

    Runnable slotUpdateListener = () -> {
    };

    long lastSoundTime;
    private final Container inputContainer = new SimpleContainer(3) {
        @Override
        public void setChanged() {
            super.setChanged();
            PreservedLoomMenu.this.slotsChanged(this);
            PreservedLoomMenu.this.slotUpdateListener.run();
        }
    };
    private final Container outputContainer = new SimpleContainer(1) {
        @Override
        public void setChanged() {
            super.setChanged();
            PreservedLoomMenu.this.slotUpdateListener.run();
        }
    };

    public PreservedLoomMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public PreservedLoomMenu(int containerId, Inventory playerInventory, final ContainerLevelAccess access) {
        super(Constants.PRESERVED_LOOM_MENU_TYPE.get(), containerId);
        this.access = access;
        this.inputSlotOne = this.addSlot(new Slot(this.inputContainer, 0, 48, 26) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return true;
            }
        });
        this.inputSlotTwo = this.addSlot(new Slot(this.inputContainer, 1, 68, 26) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return true;
            }
        });
        this.shearsSlot = this.addSlot(new Slot(this.inputContainer, 2, 58, 45) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.getItem() == Items.SHEARS;
            }
        });

        this.resultSlot = this.addSlot(new Slot(this.outputContainer, 0, 134, 36) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
                PreservedLoomMenu.this.world = player.level();
                NonNullList<ItemStack> currentInputs = NonNullList.withSize(3, ItemStack.EMPTY);
                currentInputs.set(0, PreservedLoomMenu.this.inputSlotOne.getItem());
                currentInputs.set(1, PreservedLoomMenu.this.inputSlotTwo.getItem());
                currentInputs.set(2, PreservedLoomMenu.this.shearsSlot.getItem());

                if (player.level() instanceof ServerLevel serverLevel) {
                    Optional<RecipeHolder<LoomRecipe>> recipeMatch =
                            serverLevel.recipeAccess().getRecipeFor(
                                    ModRecipes.LOOM_TYPE,
                                    new LoomRecipeInput(currentInputs),
                                    serverLevel
                            );
                    if (recipeMatch.isPresent()) {
                        PreservedLoomMenu.this.inputSlotOne.remove(1);
                        PreservedLoomMenu.this.inputSlotTwo.remove(1);

                        ItemStack shearsItemStack = PreservedLoomMenu.this.shearsSlot.getItem();
                        if (!shearsItemStack.isEmpty() && !player.isCreative() && !player.isSpectator()) {
                            // unbreaking check
                            int unbreakingLevel = EnchantmentHelper.getItemEnchantmentLevel(PreservedLoomMenu.this.world.registryAccess().lookupOrThrow(Enchantments.UNBREAKING.registryKey()).getOrThrow(Enchantments.UNBREAKING), shearsItemStack);
                            if (unbreakingLevel > 0) {
                                double chance = 1.0 / (unbreakingLevel + 1);
                                if (Math.random() >= chance) {
                                    shearsItemStack.setDamageValue(shearsItemStack.getDamageValue() + 1);
                                    if (shearsItemStack.getDamageValue() >= shearsItemStack.getMaxDamage()) {
                                        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                                        shearsItemStack.shrink(1);
                                    }
                                }
                            }
                            else {
                                shearsItemStack.setDamageValue(shearsItemStack.getDamageValue() + 1);
                                if (shearsItemStack.getDamageValue() >= shearsItemStack.getMaxDamage()) {
                                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                                    shearsItemStack.shrink(1);
                                }
                            }
                        }
                    }
                }

                // award advancement
                if (stack.is(ItemTags.WOOL)) {
                    if (player instanceof ServerPlayer serverPlayer) ModTriggers.WOOL_FROM_LOOM.trigger(serverPlayer);
                }

                access.execute((level, blockPos) -> {
                    long i = level.getGameTime();
                    if (PreservedLoomMenu.this.lastSoundTime != i) {
                        level.playSound(null, blockPos, SoundEvents.UI_LOOM_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
                        PreservedLoomMenu.this.lastSoundTime = i;
                    }
                });
                super.onTake(player, stack);
            }
        });
        this.addStandardInventorySlots(playerInventory, 8, 84);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(this.access, player, Blocks.LOOM);
    }

    @Override
    public void slotsChanged(@NotNull Container inventory) {
        this.access.execute((level, blockPos) -> {
            if (level.isClientSide()) return;
            this.world = level;
            this.setupResultSlot();
            this.broadcastChanges();
        });
    }

    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index == this.resultSlot.index) {
                if (!this.moveItemStackTo(itemstack1, 4, 40, true)) return ItemStack.EMPTY;
                slot.onQuickCraft(itemstack1, itemstack);
            }
            else if (index == this.shearsSlot.index) {
                if (!this.moveItemStackTo(itemstack1, 4, 40, false)) {
                    if (!this.moveItemStackTo(itemstack1, 0, 4, false)) return ItemStack.EMPTY;
                }
            }
            else if (itemstack1.getItem() == Items.SHEARS) {
                if (!this.moveItemStackTo(itemstack1, this.shearsSlot.index, this.shearsSlot.index + 1, false)) return ItemStack.EMPTY;
            }
            else if (index != this.inputSlotOne.index && index != this.inputSlotTwo.index) {
                if (!this.moveItemStackTo(itemstack1, this.inputSlotOne.index, this.inputSlotOne.index + 1, false)) {
                    if (!this.moveItemStackTo(itemstack1, this.inputSlotTwo.index, this.inputSlotTwo.index + 1, false)) return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(itemstack1, 4, 40, false)) return ItemStack.EMPTY;

            if (itemstack1.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
            else slot.setChanged();

            if (itemstack1.getCount() == itemstack.getCount()) return ItemStack.EMPTY;
            slot.onTake(player, itemstack1);
        }
        return itemstack;
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        this.access.execute((level, blockPos) -> this.clearContainer(player, this.inputContainer));
    }

    private void setupResultSlot() {
        if (this.world == null) return;

        NonNullList<ItemStack> currentInputs = NonNullList.withSize(3, ItemStack.EMPTY);
        currentInputs.set(0, this.inputSlotOne.getItem());
        currentInputs.set(1, this.inputSlotTwo.getItem());
        currentInputs.set(2, this.shearsSlot.getItem());

        LoomRecipeInput recipeInput = new LoomRecipeInput(currentInputs);

        if (world instanceof ServerLevel serverLevel) {
            Optional<RecipeHolder<LoomRecipe>> recipeMatch =
                    serverLevel.recipeAccess().getRecipeFor(
                            ModRecipes.LOOM_TYPE,
                            new LoomRecipeInput(currentInputs),
                            serverLevel
                    );
            if (recipeMatch.isPresent()) {
                LoomRecipe recipe = recipeMatch.get().value();
                this.resultSlot.set(recipe.assemble(recipeInput));
            }
            else this.resultSlot.set(ItemStack.EMPTY);
        }
    }

    public @NotNull NonNullList<ItemStack> getItems() {
        return inventory;
    }
}
