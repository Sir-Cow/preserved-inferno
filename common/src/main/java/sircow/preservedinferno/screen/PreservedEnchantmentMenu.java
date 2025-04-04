package sircow.preservedinferno.screen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EnchantingTableBlock;
import sircow.preservedinferno.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class PreservedEnchantmentMenu extends AbstractContainerMenu {
    static final ResourceLocation EMPTY_SLOT_LAPIS_LAZULI = ResourceLocation.withDefaultNamespace("container/slot/lapis_lazuli");
    private final Container enchantSlots = new SimpleContainer(2) {
        @Override
        public void setChanged() {
            super.setChanged();
            PreservedEnchantmentMenu.this.slotsChanged(this);
        }
    };

    private final ContainerLevelAccess access;

    public Level world;
    public boolean enchantSelected;
    public int selectedEnchantID;

    public final DataSlot enchantmentPower = DataSlot.standalone();

    public String[] enchantmentLevelCosts = {
            "30", "10", "10", "10", "30", "10", "10", "10", "10", "10",
            "10", "20", "20", "10", "30", "20", "20", "10", "10", "10",
            "30", "10", "10", "10", "10", "20", "10", "10", "10", "10",
            "30", "10", "10", "10", "10",
    };

    public static final Map<Integer, ResourceKey<Enchantment>> enchants = new HashMap<>();

    static {
        enchants.put(0, Enchantments.AQUA_AFFINITY);
        enchants.put(1, Enchantments.BANE_OF_ARTHROPODS);
        enchants.put(2, Enchantments.BLAST_PROTECTION);
        enchants.put(3, Enchantments.BREACH);
        enchants.put(4, Enchantments.CHANNELING);
        enchants.put(5, Enchantments.DENSITY);
        enchants.put(6, Enchantments.DEPTH_STRIDER);
        enchants.put(7, Enchantments.EFFICIENCY);
        enchants.put(8, Enchantments.FEATHER_FALLING);
        enchants.put(9, Enchantments.FIRE_ASPECT);
        enchants.put(10, Enchantments.FIRE_PROTECTION);
        enchants.put(11, Enchantments.FLAME);
        enchants.put(12, Enchantments.FORTUNE);
        enchants.put(13, Enchantments.IMPALING);
        enchants.put(14, Enchantments.INFINITY);
        enchants.put(15, Enchantments.KNOCKBACK);
        enchants.put(16, Enchantments.LOOTING);
        enchants.put(17, Enchantments.LOYALTY);
        enchants.put(18, Enchantments.LUCK_OF_THE_SEA);
        enchants.put(19, Enchantments.LURE);
        enchants.put(20, Enchantments.MULTISHOT);
        enchants.put(21, Enchantments.PIERCING);
        enchants.put(22, Enchantments.POWER);
        enchants.put(23, Enchantments.PROJECTILE_PROTECTION);
        enchants.put(24, Enchantments.PROTECTION);
        enchants.put(25, Enchantments.PUNCH);
        enchants.put(26, Enchantments.QUICK_CHARGE);
        enchants.put(27, Enchantments.RESPIRATION);
        enchants.put(28, Enchantments.RIPTIDE);
        enchants.put(29, Enchantments.SHARPNESS);
        enchants.put(30, Enchantments.SILK_TOUCH);
        enchants.put(31, Enchantments.SMITE);
        enchants.put(32, Enchantments.SWEEPING_EDGE);
        enchants.put(33, Enchantments.THORNS);
        enchants.put(34, Enchantments.UNBREAKING);
    }

    public PreservedEnchantmentMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
        this.world = playerInventory.player.level();
    }

    public PreservedEnchantmentMenu(int containerId, Inventory playerInventory, final ContainerLevelAccess access) {
        super(Constants.PRESERVED_ENCHANT_MENU_TYPE.get(), containerId);
        this.access = access;
        this.addSlot(new Slot(this.enchantSlots, 0, 25, 53) {
            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });
        this.addSlot(new Slot(this.enchantSlots, 1, 45, 53) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(Items.LAPIS_LAZULI);
            }

            @Override
            public ResourceLocation getNoItemIcon() {
                return PreservedEnchantmentMenu.EMPTY_SLOT_LAPIS_LAZULI;
            }
        });
        this.addDataSlot(this.enchantmentPower);
        this.addStandardInventorySlots(playerInventory, 8, 84);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.access.execute((world, pos) -> {
            this.clearContainer(player, this.enchantSlots);
            int randomNum = (int)(Math.random() * 1);
//            switch (randomNum) {
//                case 0 ->
//                        world.playSound(null, pos, ModSounds.ENCHANT_CLOSE_ONE, SoundSource.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
//                case 1 ->
//                        world.playSound(null, pos, ModSounds.ENCHANT_CLOSE_TWO, SoundSource.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
//            }
        });
        this.enchantSelected = false;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, Blocks.ENCHANTING_TABLE);
    }

    public int getSelectedEnchantID() {
        return this.selectedEnchantID;
    }

    @Override
    public void slotsChanged(Container inventory) {
        if (inventory == this.enchantSlots) {
            this.access.execute((world, pos) -> {
                int bookshelfCount = 0;

                for (BlockPos blockPos : EnchantingTableBlock.BOOKSHELF_OFFSETS) {
                    if (EnchantingTableBlock.isValidBookShelf(world, pos, blockPos)) {
                        bookshelfCount++;
                    }
                }

                if (bookshelfCount < 5) {
                    this.enchantmentPower.set(0);
                }
                else if (bookshelfCount >= 5 && bookshelfCount < 10) {
                    this.enchantmentPower.set(1);
                }
                else if (bookshelfCount >= 10 && bookshelfCount < 15) {
                    this.enchantmentPower.set(2);
                }
                else if (bookshelfCount >= 15) {
                    this.enchantmentPower.set(3);
                }

                this.broadcastChanges();
            });
        }
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        ItemStack itemStack = this.enchantSlots.getItem(0);
        ItemEnchantments presentEnchantments = itemStack.getEnchantments();
        AtomicBoolean shouldReduceXP = new AtomicBoolean(false);
        if (id == 101) {
            if (this.enchantmentPower.get() < 1) {
                return false;
            }
            else if (player.experienceLevel >= 10 || player.hasInfiniteMaterials()) {
                this.access.execute((world, pos) -> {
                    ResourceKey<Enchantment> enchantment = enchants.get(this.selectedEnchantID);

                    if (Objects.equals(enchantmentLevelCosts[this.selectedEnchantID], "10")) {
                        if (enchantment != null && !presentEnchantments.keySet().contains(world.registryAccess()
                                .lookupOrThrow(enchantment.registryKey())
                                .getOrThrow(enchantment)) && this.enchantSlots.getItem(0).getItem() != Items.ENCHANTED_BOOK) {
                            if (this.enchantSlots.getItem(0).getItem() == Items.BOOK) {
                                this.enchantSlots.getItem(0).shrink(1);
                                this.enchantSlots.setItem(0, new ItemStack(Items.ENCHANTED_BOOK, 1));
                            }
                            if (this.enchantSlots.getItem(0).getItem() != Items.ENCHANTED_BOOK ||
                                    !Objects.requireNonNull(this.enchantSlots.getItem(0)
                                                    .get(DataComponents.STORED_ENCHANTMENTS))
                                            .keySet().contains(world.registryAccess()
                                                    .lookupOrThrow(enchantment.registryKey())
                                                    .getOrThrow(enchantment))) {
                                this.enchantSlots.getItem(0)
                                        .enchant(world.registryAccess()
                                                .lookupOrThrow(enchantment.registryKey())
                                                .getOrThrow(enchantment), 1);
                                shouldReduceXP.set(true);
                                this.getSlot(1).getItem().shrink(1);
                                this.enchantSlots.setChanged();
                                this.slotsChanged(this.enchantSlots);
                                //world.playSound(null, pos, ModSounds.ENCHANT_ONE, SoundSource.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
                            }
                        }
                    }
                });
                if (shouldReduceXP.get()) {
                    if (Objects.equals(enchantmentLevelCosts[this.selectedEnchantID], "10")) {
                        player.giveExperienceLevels(-10);
                    }
                    shouldReduceXP.set(false);
                }
            }
        }
        else if (id == 102) {
            if (this.enchantmentPower.get() < 2) {
                return false;
            }
            else if (player.experienceLevel >= 20 || player.hasInfiniteMaterials()) {
                this.access.execute((world, pos) -> {
                    ResourceKey<Enchantment> enchantment = enchants.get(this.selectedEnchantID);

                    if (Objects.equals(enchantmentLevelCosts[this.selectedEnchantID], "20")) {
                        if (enchantment != null && !presentEnchantments.keySet().contains(world.registryAccess()
                                .lookupOrThrow(enchantment.registryKey())
                                .getOrThrow(enchantment)) && this.enchantSlots.getItem(0).getItem() != Items.ENCHANTED_BOOK) {
                            if (this.enchantSlots.getItem(0).getItem() == Items.BOOK) {
                                this.enchantSlots.getItem(0).shrink(1);
                                this.enchantSlots.setItem(0, new ItemStack(Items.ENCHANTED_BOOK, 1));
                            }
                            if (this.enchantSlots.getItem(0).getItem() != Items.ENCHANTED_BOOK ||
                                    !Objects.requireNonNull(this.enchantSlots.getItem(0).get(DataComponents.STORED_ENCHANTMENTS))
                                            .keySet().contains(world.registryAccess()
                                                    .lookupOrThrow(enchantment.registryKey())
                                                    .getOrThrow(enchantment))) {
                                this.enchantSlots.getItem(0).enchant(world.registryAccess()
                                        .lookupOrThrow(enchantment.registryKey())
                                        .getOrThrow(enchantment), 1);
                                shouldReduceXP.set(true);
                                this.getSlot(1).getItem().shrink(1);
                                this.enchantSlots.setChanged();
                                this.slotsChanged(this.enchantSlots);
                                //world.playSound(null, pos, ModSounds.ENCHANT_TWO, SoundSource.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
                            }
                        }
                    }
                });
                if (shouldReduceXP.get()) {
                    if (Objects.equals(enchantmentLevelCosts[this.selectedEnchantID], "20")) {
                        player.giveExperienceLevels(-20);
                    }
                    shouldReduceXP.set(false);
                }
            }
        }
        else if (id == 103) {
            if (this.enchantmentPower.get() < 3) {
                return false;
            }
            else if (player.experienceLevel >= 30 || player.hasInfiniteMaterials()) {
                this.access.execute((world, pos) -> {
                    ResourceKey<Enchantment> enchantment = enchants.get(this.selectedEnchantID);

                    if (Objects.equals(enchantmentLevelCosts[this.selectedEnchantID], "30")) {
                        if (enchantment != null && !presentEnchantments.keySet().contains(world.registryAccess()
                                .lookupOrThrow(enchantment.registryKey())
                                .getOrThrow(enchantment)) && this.enchantSlots.getItem(0).getItem() != Items.ENCHANTED_BOOK) {
                            if (this.enchantSlots.getItem(0).getItem() == Items.BOOK) {
                                this.enchantSlots.getItem(0).shrink(1);
                                this.enchantSlots.setItem(0, new ItemStack(Items.ENCHANTED_BOOK, 1));
                            }
                            if (this.enchantSlots.getItem(0).getItem() != Items.ENCHANTED_BOOK ||
                                    !Objects.requireNonNull(this.enchantSlots.getItem(0).get(DataComponents.STORED_ENCHANTMENTS))
                                            .keySet().contains(world.registryAccess()
                                                    .lookupOrThrow(enchantment.registryKey())
                                                    .getOrThrow(enchantment))) {
                                this.enchantSlots.getItem(0).enchant(world.registryAccess()
                                        .lookupOrThrow(enchantment.registryKey())
                                        .getOrThrow(enchantment), 1);
                                shouldReduceXP.set(true);
                                this.getSlot(1).getItem().shrink(1);
                                this.enchantSlots.setChanged();
                                this.slotsChanged(this.enchantSlots);
                                //world.playSound(null, pos, ModSounds.ENCHANT_THREE, SoundSource.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
                            }
                        }
                    }
                });
                if (shouldReduceXP.get()) {
                    if (Objects.equals(enchantmentLevelCosts[this.selectedEnchantID], "30")) {
                        player.giveExperienceLevels(-30);
                    }
                    shouldReduceXP.set(false);
                }
            }
        }
        else {
            this.selectedEnchantID = id;
        }
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slot);
        if (slot2.hasItem()) {
            ItemStack itemStack2 = slot2.getItem();
            itemStack = itemStack2.copy();
            if (slot == 0) {
                if (!this.moveItemStackTo(itemStack2, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (slot == 1) {
                if (!this.moveItemStackTo(itemStack2, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (itemStack2.is(Items.LAPIS_LAZULI)) {
                if (!this.moveItemStackTo(itemStack2, 1, 2, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (this.slots.getFirst().hasItem() || !this.slots.getFirst().mayPlace(itemStack2)) {
                    return ItemStack.EMPTY;
                }

                ItemStack itemStack3 = itemStack2.copyWithCount(1);
                itemStack2.shrink(1);
                this.slots.getFirst().setByPlayer(itemStack3);
            }

            if (itemStack2.isEmpty()) {
                slot2.setByPlayer(ItemStack.EMPTY);
            } else {
                slot2.setChanged();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot2.onTake(player, itemStack2);
        }

        return itemStack;
    }
}

