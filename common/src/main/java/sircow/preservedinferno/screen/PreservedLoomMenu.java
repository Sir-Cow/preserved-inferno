package sircow.preservedinferno.screen;

import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.item.ModItems;

import java.util.HashMap;
import java.util.Map;

public class PreservedLoomMenu extends AbstractContainerMenu {
    private static final Map<Item, Item> conversionMap = new HashMap<>();
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(4, ItemStack.EMPTY);
    private final Slot inputSlotOne;
    private final Slot inputSlotTwo;
    private final Slot shearsSlot;
    private final Slot resultSlot;
    private final ContainerLevelAccess access;
    private Level world;

    static {
        conversionMap.put(Items.BLACK_WOOL, ModItems.BLACK_CLOTH);
        conversionMap.put(Items.BLUE_WOOL, ModItems.BLUE_CLOTH);
        conversionMap.put(Items.BROWN_WOOL, ModItems.BROWN_CLOTH);
        conversionMap.put(Items.CYAN_WOOL, ModItems.CYAN_CLOTH);
        conversionMap.put(Items.GRAY_WOOL, ModItems.GRAY_CLOTH);
        conversionMap.put(Items.GREEN_WOOL, ModItems.GREEN_CLOTH);
        conversionMap.put(Items.LIGHT_BLUE_WOOL, ModItems.LIGHT_BLUE_CLOTH);
        conversionMap.put(Items.LIGHT_GRAY_WOOL, ModItems.LIGHT_GRAY_CLOTH);
        conversionMap.put(Items.LIME_WOOL, ModItems.LIME_CLOTH);
        conversionMap.put(Items.MAGENTA_WOOL, ModItems.MAGENTA_CLOTH);
        conversionMap.put(Items.ORANGE_WOOL, ModItems.ORANGE_CLOTH);
        conversionMap.put(Items.PINK_WOOL, ModItems.PINK_CLOTH);
        conversionMap.put(Items.PURPLE_WOOL, ModItems.PURPLE_CLOTH);
        conversionMap.put(Items.RED_WOOL, ModItems.RED_CLOTH);
        conversionMap.put(Items.WHITE_WOOL, ModItems.WHITE_CLOTH);
        conversionMap.put(Items.YELLOW_WOOL, ModItems.YELLOW_CLOTH);

        conversionMap.put(ModItems.PHANTOM_SINEW, ModItems.HOLLOW_TWINE);
        conversionMap.put(Items.LEATHER, ModItems.LEATHER_FABRIC);
        conversionMap.put(Items.STRING, Items.WHITE_WOOL);
    }

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
                return conversionMap.containsKey(stack.getItem());
            }
        });
        this.inputSlotTwo = this.addSlot(new Slot(this.inputContainer, 1, 68, 26) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return conversionMap.containsKey(stack.getItem());
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
                PreservedLoomMenu.this.inputSlotOne.remove(1);
                PreservedLoomMenu.this.inputSlotTwo.remove(1);
                ItemStack shearsItemStack = PreservedLoomMenu.this.shearsSlot.getItem();
                if (!shearsItemStack.isEmpty()) {
                    // unbreaking check
                    int unbreakingLevel = EnchantmentHelper.getItemEnchantmentLevel(PreservedLoomMenu.this.world.registryAccess().lookupOrThrow(Enchantments.UNBREAKING.registryKey())
                            .getOrThrow(Enchantments.UNBREAKING), shearsItemStack);
                    if (unbreakingLevel > 0) {
                        double chance = 1.0 / (unbreakingLevel + 1);
                        if (Math.random() >= chance) {
                            shearsItemStack.setDamageValue(shearsItemStack.getDamageValue() + 1);
                            if (shearsItemStack.getDamageValue() >= shearsItemStack.getMaxDamage()) {
                                shearsItemStack.shrink(1);
                            }
                        }
                    } else {
                        shearsItemStack.setDamageValue(shearsItemStack.getDamageValue() + 1);
                        if (shearsItemStack.getDamageValue() >= shearsItemStack.getMaxDamage()) {
                            shearsItemStack.setDamageValue(shearsItemStack.getDamageValue() + 1);
                        }
                    }
                }

                access.execute((p_39952_, p_39953_) -> {
                    long i = p_39952_.getGameTime();
                    if (PreservedLoomMenu.this.lastSoundTime != i) {
                        p_39952_.playSound(null, p_39953_, SoundEvents.UI_LOOM_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
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
        ItemStack itemstack = this.inputSlotOne.getItem();
        ItemStack itemstack1 = this.inputSlotTwo.getItem();
        ItemStack itemstack2 = this.shearsSlot.getItem();
        if (!itemstack.isEmpty() && !itemstack1.isEmpty() && !itemstack2.isEmpty()) {
            this.setupResultSlot();
            this.broadcastChanges();
        } else {
            this.resultSlot.set(ItemStack.EMPTY);
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index == this.resultSlot.index) {
                if (!this.moveItemStackTo(itemstack1, 4, 40, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            }
            else if (index == this.shearsSlot.index) {
                if (!this.moveItemStackTo(itemstack1, 4, 40, false)) {
                    if (!this.moveItemStackTo(itemstack1, 0, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
            else if (itemstack1.getItem() == Items.SHEARS) {
                if (!this.moveItemStackTo(itemstack1, this.shearsSlot.index, this.shearsSlot.index + 1, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (index != this.inputSlotOne.index && index != this.inputSlotTwo.index) {
                if (conversionMap.containsKey(itemstack1.getItem())) {
                    if (!this.moveItemStackTo(itemstack1, this.inputSlotOne.index, this.inputSlotOne.index + 1, false)) {
                        if (!this.moveItemStackTo(itemstack1, this.inputSlotTwo.index, this.inputSlotTwo.index + 1, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
                else if (index >= 4 && index < 31) {
                    if (!this.moveItemStackTo(itemstack1, 31, 40, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 31 && index < 40 && !this.moveItemStackTo(itemstack1, 4, 31, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(itemstack1, 4, 40, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            }
            else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
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
        ItemStack itemstack = this.inputSlotOne.getItem();
        ItemStack itemstack1 = this.inputSlotTwo.getItem();
        ItemStack itemstack2 = ItemStack.EMPTY;
        if (!itemstack.isEmpty() && !itemstack1.isEmpty()) {
            if (conversionMap.get(itemstack1.getItem()) == ModItems.LEATHER_FABRIC) {
                itemstack2 = new ItemStack(conversionMap.get(itemstack1.getItem()), 8);
            }
            else {
                itemstack2 = new ItemStack(conversionMap.get(itemstack1.getItem()));
            }
        }

        if (!ItemStack.matches(itemstack2, this.resultSlot.getItem())) {
            this.resultSlot.set(itemstack2);
        }
    }

    public @NotNull NonNullList<ItemStack> getItems() {
        return inventory;
    }
}
