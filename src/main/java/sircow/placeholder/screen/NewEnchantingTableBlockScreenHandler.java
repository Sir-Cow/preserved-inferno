package sircow.placeholder.screen;

import com.mojang.datafixers.util.Pair;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;

import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import sircow.placeholder.block.ModBlocks;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class NewEnchantingTableBlockScreenHandler extends ScreenHandler {
    static final Identifier EMPTY_LAPIS_SLOT_TEXTURE = Identifier.ofVanilla("item/empty_slot_lapis_lazuli");
    public final Inventory inventory = new SimpleInventory(2) {
        @Override
        public void markDirty() {
            super.markDirty();
            NewEnchantingTableBlockScreenHandler.this.onContentChanged(this);
        }
    };

    public final ScreenHandlerContext context;
    public final int[] enchantmentPower = new int[3];
    public final int[] enchantmentId = new int[]{-1, -1, -1};
    public final int[] enchantmentLevel = new int[]{-1, -1, -1};
    final Slot inputSlot;
    final Slot lapisSlot;
    public World world;
    public boolean enchantSelected;
    public int selectedEnchantID;

    public String[] enchantmentLevelCosts = {
            "30", "10", "10", "10", "30", "10", "10", "10", "10", "10",
            "10", "20", "20", "10", "30", "20", "20", "10", "10", "10",
            "30", "10", "10", "10", "10", "20", "10", "10", "10", "10",
            "30", "10", "10", "10", "10",
    };

    public NewEnchantingTableBlockScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, ScreenHandlerContext.EMPTY);
        this.world = inventory.player.getWorld();
    }

    public NewEnchantingTableBlockScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ModScreenHandlers.NEW_ENCHANTING_TABLE_BLOCK_SCREEN_HANDLER, syncId);
        this.context = context;
        this.inputSlot = this.addSlot(new Slot(this.inventory, 0, 25, 53) {
            @Override
            public int getMaxItemCount() {
                return 1;
            }
        });
        this.lapisSlot = this.addSlot(new Slot(this.inventory, 1, 45, 53) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(Items.LAPIS_LAZULI);
            }

            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, NewEnchantingTableBlockScreenHandler.EMPTY_LAPIS_SLOT_TEXTURE);
            }
        });

        this.addPlayerSlots(playerInventory, 8, 84);
        this.addProperty(Property.create(this.enchantmentPower, 0));
        this.addProperty(Property.create(this.enchantmentPower, 1));
        this.addProperty(Property.create(this.enchantmentPower, 2));
    }

    public ItemEnchantmentsComponent getPresentEnchantments(ItemStack itemStack) {
        return EnchantmentHelper.getEnchantments(itemStack);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.context.run((world, pos) -> this.dropInventory(player, this.inventory));
        this.enchantSelected = false;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, ModBlocks.NEW_ENCHANTING_TABLE_BLOCK);
    }

    public Slot getInputSlot() { return this.inputSlot; }

    public Slot getLapisSlot() { return this.lapisSlot; }

    public void setSelectedEnchantID (int num) {
        this.selectedEnchantID = num;
    }

    public int getSelectedEnchantID() {
        return this.selectedEnchantID;
    }

    public static final Map<Integer, RegistryKey<Enchantment>> enchants = new HashMap<>();

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

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {

        AtomicBoolean shouldReduceXP = new AtomicBoolean(false);
        if (id == 101) {
            if (player.experienceLevel >= 10 || player.isInCreativeMode()) {
                this.context.run((world, pos) -> {
                    ItemStack itemStack = this.inventory.getStack(0);
                    ItemEnchantmentsComponent presentEnchantments = itemStack.getEnchantments();
                    RegistryKey<Enchantment> enchantment = enchants.get(this.selectedEnchantID);

                    if (Objects.equals(enchantmentLevelCosts[this.selectedEnchantID], "10")) {
                        if (enchantment != null && !presentEnchantments.getEnchantments().contains(world.getRegistryManager()
                                .getOrThrow(enchantment.getRegistryRef())
                                .getOrThrow(enchantment))) {
                            itemStack.addEnchantment(world.getRegistryManager()
                                    .getOrThrow(enchantment.getRegistryRef())
                                    .getOrThrow(enchantment), 1);
                            shouldReduceXP.set(true);
                            this.getLapisSlot().getStack().decrement(1);
                            this.inventory.markDirty();
                        }
                    }
                });
                if (shouldReduceXP.get()) {
                    if (Objects.equals(enchantmentLevelCosts[this.selectedEnchantID], "10")) {
                        player.addExperienceLevels(-10);
                    }
                    shouldReduceXP.set(false);
                }
            }
        }
        else if (id == 102) {
            if (player.experienceLevel >= 20 || player.isInCreativeMode()) {
                this.context.run((world, pos) -> {
                    ItemStack itemStack = this.inventory.getStack(0);
                    ItemEnchantmentsComponent presentEnchantments = itemStack.getEnchantments();
                    RegistryKey<Enchantment> enchantment = enchants.get(this.selectedEnchantID);

                    if (Objects.equals(enchantmentLevelCosts[this.selectedEnchantID], "20")) {
                        if (enchantment != null && !presentEnchantments.getEnchantments().contains(world.getRegistryManager()
                                .getOrThrow(enchantment.getRegistryRef())
                                .getOrThrow(enchantment))) {
                            itemStack.addEnchantment(world.getRegistryManager()
                                    .getOrThrow(enchantment.getRegistryRef())
                                    .getOrThrow(enchantment), 1);
                            shouldReduceXP.set(true);
                            this.getLapisSlot().getStack().decrement(1);
                            this.inventory.markDirty();
                        }
                    }
                });
                if (shouldReduceXP.get()) {
                    if (Objects.equals(enchantmentLevelCosts[this.selectedEnchantID], "20")) {
                        player.addExperienceLevels(-20);
                    }
                    shouldReduceXP.set(false);
                }
            }
        }
        else if (id == 103) {
            if (player.experienceLevel >= 30 || player.isInCreativeMode()) {
                this.context.run((world, pos) -> {
                    ItemStack itemStack = this.inventory.getStack(0);
                    ItemEnchantmentsComponent presentEnchantments = itemStack.getEnchantments();
                    RegistryKey<Enchantment> enchantment = enchants.get(this.selectedEnchantID);

                    if (Objects.equals(enchantmentLevelCosts[this.selectedEnchantID], "30")) {
                        if (enchantment != null && !presentEnchantments.getEnchantments().contains(world.getRegistryManager()
                                .getOrThrow(enchantment.getRegistryRef())
                                .getOrThrow(enchantment))) {
                            itemStack.addEnchantment(world.getRegistryManager()
                                    .getOrThrow(enchantment.getRegistryRef())
                                    .getOrThrow(enchantment), 1);
                            shouldReduceXP.set(true);

                            this.getLapisSlot().getStack().decrement(1);
                            this.inventory.markDirty();
                        }
                    }
                });
                if (shouldReduceXP.get()) {
                    if (Objects.equals(enchantmentLevelCosts[this.selectedEnchantID], "30")) {
                        player.addExperienceLevels(-30);
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
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slot);
        if (slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if (slot == 0) {
                if (!this.insertItem(itemStack2, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (slot == 1) {
                if (!this.insertItem(itemStack2, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (itemStack2.isOf(Items.LAPIS_LAZULI)) {
                if (!this.insertItem(itemStack2, 1, 2, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (this.slots.getFirst().hasStack() || !this.slots.getFirst().canInsert(itemStack2)) {
                    return ItemStack.EMPTY;
                }

                ItemStack itemStack3 = itemStack2.copyWithCount(1);
                itemStack2.decrement(1);
                this.slots.getFirst().setStack(itemStack3);
            }

            if (itemStack2.isEmpty()) {
                slot2.setStack(ItemStack.EMPTY);
            } else {
                slot2.markDirty();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot2.onTakeItem(player, itemStack2);
        }

        return itemStack;
    }
}
