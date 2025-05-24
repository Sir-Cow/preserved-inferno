package sircow.preservedinferno.screen;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.block.ModBlocks;
import sircow.preservedinferno.components.ModComponents;
import sircow.preservedinferno.item.ModItems;
import sircow.preservedinferno.other.ModTags;

import java.util.Objects;

public class AnglingTableMenu extends AbstractContainerMenu {
    private final Slot rodInputSlot;
    private final Slot hookInputSlot;
    private final Slot lineInputSlot;
    private final Slot sinkerInputSlot;
    private boolean hookPresent;
    private boolean linePresent;
    private boolean sinkerPresent;

    Runnable slotUpdateListener = () -> {
    };

    private final Container inputContainer = new SimpleContainer(4) {
        @Override
        public void setChanged() {
            super.setChanged();
            AnglingTableMenu.this.slotsChanged(this);
            AnglingTableMenu.this.slotUpdateListener.run();
        }
    };
    private final ContainerLevelAccess access;

    public AnglingTableMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public AnglingTableMenu(int containerId, Inventory playerInventory, final ContainerLevelAccess access) {
        super(Constants.ANGLING_TABLE_MENU_TYPE.get(), containerId);
        this.access = access;
        rodInputSlot = this.addSlot(new Slot(this.inputContainer, 0, 79, 17) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(Items.FISHING_ROD);
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }

            @Override
            public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
                if (!Objects.equals(stack.get(ModComponents.HOOK_COMPONENT), "none")) {
                    AnglingTableMenu.this.hookInputSlot.set(ItemStack.EMPTY);
                    AnglingTableMenu.this.hookPresent = false;
                }
                if (!Objects.equals(stack.get(ModComponents.LINE_COMPONENT), "none")) {
                    AnglingTableMenu.this.lineInputSlot.set(ItemStack.EMPTY);
                    AnglingTableMenu.this.linePresent = false;
                }
                if (!Objects.equals(stack.get(ModComponents.SINKER_COMPONENT), "none")) {
                    AnglingTableMenu.this.sinkerInputSlot.set(ItemStack.EMPTY);
                    AnglingTableMenu.this.sinkerPresent = false;
                }
                super.onTake(player, stack);
            }
        });
        hookInputSlot = this.addSlot(new Slot(this.inputContainer, 1, 56, 51) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(ModTags.HOOKS) && !AnglingTableMenu.this.rodInputSlot.getItem().isEmpty();
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }

            @Override
            public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
                ItemStack rod = AnglingTableMenu.this.rodInputSlot.getItem();
                if (!rod.isEmpty()) {
                    rod.set(ModComponents.HOOK_COMPONENT, "none");
                    rod.set(ModComponents.HOOK_DURABILITY, 0);
                    AnglingTableMenu.this.rodInputSlot.setChanged();
                }
                AnglingTableMenu.this.hookInputSlot.set(ItemStack.EMPTY);
                hookPresent = false;
                super.onTake(player, stack);
            }
        });
        lineInputSlot = this.addSlot(new Slot(this.inputContainer, 2, 79, 58) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(ModTags.LINES) && !AnglingTableMenu.this.rodInputSlot.getItem().isEmpty();
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }

            @Override
            public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
                ItemStack rod = AnglingTableMenu.this.rodInputSlot.getItem();
                if (!rod.isEmpty()) {
                    rod.set(ModComponents.LINE_COMPONENT, "none");
                    rod.set(ModComponents.LINE_DURABILITY, 0);
                    AnglingTableMenu.this.rodInputSlot.setChanged();
                }
                AnglingTableMenu.this.lineInputSlot.set(ItemStack.EMPTY);
                linePresent = false;
                super.onTake(player, stack);
            }
        });
        sinkerInputSlot = this.addSlot(new Slot(this.inputContainer, 3, 102, 51) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(ModTags.SINKERS) && !AnglingTableMenu.this.rodInputSlot.getItem().isEmpty();
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }

            @Override
            public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
                ItemStack rod = AnglingTableMenu.this.rodInputSlot.getItem();
                if (!rod.isEmpty()) {
                    rod.set(ModComponents.SINKER_COMPONENT, "none");
                    rod.set(ModComponents.SINKER_DURABILITY, 0);
                    AnglingTableMenu.this.rodInputSlot.setChanged();
                }
                AnglingTableMenu.this.sinkerInputSlot.set(ItemStack.EMPTY);
                sinkerPresent = false;
                super.onTake(player, stack);
            }
        });
        this.addStandardInventorySlots(playerInventory, 8, 84);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemStack1 = slot.getItem();
            itemStack = itemStack1.copy();
            if (index >= 0 && index < 4) {
                if (!this.moveItemStackTo(itemStack1, 4, 40, false)) {
                    return ItemStack.EMPTY;
                }
            }

            else if (index >= 4 && index < 40) {
                if (itemStack1.is(Items.FISHING_ROD)) {
                    if (!this.moveItemStackTo(itemStack1, this.rodInputSlot.index, this.rodInputSlot.index + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (itemStack1.is(ModTags.HOOKS)) {
                    if (!this.moveItemStackTo(itemStack1, this.hookInputSlot.index, this.hookInputSlot.index + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (itemStack1.is(ModTags.LINES)) {
                    if (!this.moveItemStackTo(itemStack1, this.lineInputSlot.index, this.lineInputSlot.index + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (itemStack1.is(ModTags.SINKERS)) {
                    if (!this.moveItemStackTo(itemStack1, this.sinkerInputSlot.index, this.sinkerInputSlot.index + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (itemStack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            }
            else {
                slot.setChanged();
            }

            if (itemStack1.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemStack1);
        }

        return itemStack;
    }

    @Override
    public void slotsChanged(@NotNull Container inventory) {
        ItemStack itemstack = this.rodInputSlot.getItem();

        if (!itemstack.isEmpty()) {
            this.handleUpgrades();
        }
        this.broadcastChanges();
    }

    public void handleUpgrades() {
        ItemStack rod = this.rodInputSlot.getItem();
        ItemStack hook = this.hookInputSlot.getItem();
        ItemStack line = this.lineInputSlot.getItem();
        ItemStack sinker = this.sinkerInputSlot.getItem();

        // hooks
        if (!hook.isEmpty()) {
            if (hook.getItem() == ModItems.COPPER_FISHING_HOOK && !Objects.equals(rod.get(ModComponents.HOOK_COMPONENT), "copper")) {
                rod.set(ModComponents.HOOK_COMPONENT, "copper");
                rod.set(ModComponents.HOOK_DURABILITY, hook.getDamageValue());
            }
            if (hook.getItem() == ModItems.PRISMARINE_FISHING_HOOK && !Objects.equals(rod.get(ModComponents.HOOK_COMPONENT), "prismarine")) {
                rod.set(ModComponents.HOOK_COMPONENT, "prismarine");
                rod.set(ModComponents.HOOK_DURABILITY, hook.getDamageValue());
            }
            if (hook.getItem() == ModItems.IRON_FISHING_HOOK && !Objects.equals(rod.get(ModComponents.HOOK_COMPONENT), "iron")) {
                rod.set(ModComponents.HOOK_COMPONENT, "iron");
                rod.set(ModComponents.HOOK_DURABILITY, hook.getDamageValue());
            }
            if (hook.getItem() == ModItems.GOLDEN_FISHING_HOOK && !Objects.equals(rod.get(ModComponents.HOOK_COMPONENT), "golden")) {
                rod.set(ModComponents.HOOK_COMPONENT, "golden");
                rod.set(ModComponents.HOOK_DURABILITY, hook.getDamageValue());
            }
            if (hook.getItem() == ModItems.DIAMOND_FISHING_HOOK && !Objects.equals(rod.get(ModComponents.HOOK_COMPONENT), "diamond")) {
                rod.set(ModComponents.HOOK_COMPONENT, "diamond");
                rod.set(ModComponents.HOOK_DURABILITY, hook.getDamageValue());
            }
            if (hook.getItem() == ModItems.NETHERITE_FISHING_HOOK && !Objects.equals(rod.get(ModComponents.HOOK_COMPONENT), "netherite")) {
                rod.set(ModComponents.HOOK_COMPONENT, "netherite");
                rod.set(ModComponents.HOOK_DURABILITY, hook.getDamageValue());
            }
        }
        else {
            if (Objects.equals(rod.get(ModComponents.HOOK_COMPONENT), "copper")) {
                if (!hookPresent && this.hookInputSlot.getItem().isEmpty()) {
                    this.inputContainer.setItem(this.hookInputSlot.index, new ItemStack(ModItems.COPPER_FISHING_HOOK, 1));
                    this.inputContainer.getItem(this.hookInputSlot.index).setDamageValue(rod.get(ModComponents.HOOK_DURABILITY));
                    hookPresent = true;
                }
            }
            if (Objects.equals(rod.get(ModComponents.HOOK_COMPONENT), "prismarine")) {
                if (!hookPresent && this.hookInputSlot.getItem().isEmpty()) {
                    this.inputContainer.setItem(this.hookInputSlot.index, new ItemStack(ModItems.PRISMARINE_FISHING_HOOK, 1));
                    this.inputContainer.getItem(this.hookInputSlot.index).setDamageValue(rod.get(ModComponents.HOOK_DURABILITY));
                    hookPresent = true;
                }
            }
            if (Objects.equals(rod.get(ModComponents.HOOK_COMPONENT), "iron")) {
                if (!hookPresent && this.hookInputSlot.getItem().isEmpty()) {
                    this.inputContainer.setItem(this.hookInputSlot.index, new ItemStack(ModItems.IRON_FISHING_HOOK, 1));
                    this.inputContainer.getItem(this.hookInputSlot.index).setDamageValue(rod.get(ModComponents.HOOK_DURABILITY));
                    hookPresent = true;
                }
            }
            if (Objects.equals(rod.get(ModComponents.HOOK_COMPONENT), "golden")) {
                if (!hookPresent && this.hookInputSlot.getItem().isEmpty()) {
                    this.inputContainer.setItem(this.hookInputSlot.index, new ItemStack(ModItems.GOLDEN_FISHING_HOOK, 1));
                    this.inputContainer.getItem(this.hookInputSlot.index).setDamageValue(rod.get(ModComponents.HOOK_DURABILITY));
                    hookPresent = true;
                }
            }
            if (Objects.equals(rod.get(ModComponents.HOOK_COMPONENT), "diamond")) {
                if (!hookPresent && this.hookInputSlot.getItem().isEmpty()) {
                    this.inputContainer.setItem(this.hookInputSlot.index, new ItemStack(ModItems.DIAMOND_FISHING_HOOK, 1));
                    this.inputContainer.getItem(this.hookInputSlot.index).setDamageValue(rod.get(ModComponents.HOOK_DURABILITY));
                    hookPresent = true;
                }
            }
            if (Objects.equals(rod.get(ModComponents.HOOK_COMPONENT), "netherite")) {
                if (!hookPresent && this.hookInputSlot.getItem().isEmpty()) {
                    this.inputContainer.setItem(this.hookInputSlot.index, new ItemStack(ModItems.NETHERITE_FISHING_HOOK, 1));
                    this.inputContainer.getItem(this.hookInputSlot.index).setDamageValue(rod.get(ModComponents.HOOK_DURABILITY));
                    hookPresent = true;
                }
            }
            if (!Objects.equals(rod.get(ModComponents.HOOK_COMPONENT), "none")) {
                rod.set(ModComponents.HOOK_COMPONENT, "none");
            }
        }
        // lines
        if (!line.isEmpty()) {
            if (line.getItem() == ModItems.COPPER_LACED_FISHING_LINE && !Objects.equals(rod.get(ModComponents.LINE_COMPONENT), "copper")) {
                rod.set(ModComponents.LINE_COMPONENT, "copper");
                rod.set(ModComponents.LINE_DURABILITY, line.getDamageValue());
            }
            if (line.getItem() == ModItems.PRISMARINE_LACED_FISHING_LINE && !Objects.equals(rod.get(ModComponents.LINE_COMPONENT), "prismarine")) {
                rod.set(ModComponents.LINE_COMPONENT, "prismarine");
                rod.set(ModComponents.LINE_DURABILITY, line.getDamageValue());
            }
            if (line.getItem() == ModItems.IRON_LACED_FISHING_LINE && !Objects.equals(rod.get(ModComponents.LINE_COMPONENT), "iron")) {
                rod.set(ModComponents.LINE_COMPONENT, "iron");
                rod.set(ModComponents.LINE_DURABILITY, line.getDamageValue());
            }
            if (line.getItem() == ModItems.GOLDEN_LACED_FISHING_LINE && !Objects.equals(rod.get(ModComponents.LINE_COMPONENT), "golden")) {
                rod.set(ModComponents.LINE_COMPONENT, "golden");
                rod.set(ModComponents.LINE_DURABILITY, line.getDamageValue());
            }
            if (line.getItem() == ModItems.DIAMOND_LACED_FISHING_LINE && !Objects.equals(rod.get(ModComponents.LINE_COMPONENT), "diamond")) {
                rod.set(ModComponents.LINE_COMPONENT, "diamond");
                rod.set(ModComponents.LINE_DURABILITY, line.getDamageValue());
            }
            if (line.getItem() == ModItems.NETHERITE_LACED_FISHING_LINE && !Objects.equals(rod.get(ModComponents.LINE_COMPONENT), "netherite")) {
                rod.set(ModComponents.LINE_COMPONENT, "netherite");
                rod.set(ModComponents.LINE_DURABILITY, line.getDamageValue());
            }
        }
        else {
            if (Objects.equals(rod.get(ModComponents.LINE_COMPONENT), "copper")) {
                if (!linePresent && this.lineInputSlot.getItem().isEmpty()) {
                    this.inputContainer.setItem(this.lineInputSlot.index, new ItemStack(ModItems.COPPER_LACED_FISHING_LINE, 1));
                    this.inputContainer.getItem(this.lineInputSlot.index).setDamageValue(rod.get(ModComponents.LINE_DURABILITY));
                    linePresent = true;
                }
            }
            if (Objects.equals(rod.get(ModComponents.LINE_COMPONENT), "prismarine")) {
                if (!linePresent && this.lineInputSlot.getItem().isEmpty()) {
                    this.inputContainer.setItem(this.lineInputSlot.index, new ItemStack(ModItems.PRISMARINE_LACED_FISHING_LINE, 1));
                    this.inputContainer.getItem(this.lineInputSlot.index).setDamageValue(rod.get(ModComponents.LINE_DURABILITY));
                    linePresent = true;
                }
            }
            if (Objects.equals(rod.get(ModComponents.LINE_COMPONENT), "iron")) {
                if (!linePresent && this.lineInputSlot.getItem().isEmpty()) {
                    this.inputContainer.setItem(this.lineInputSlot.index, new ItemStack(ModItems.IRON_LACED_FISHING_LINE, 1));
                    this.inputContainer.getItem(this.lineInputSlot.index).setDamageValue(rod.get(ModComponents.LINE_DURABILITY));
                    linePresent = true;
                }
            }
            if (Objects.equals(rod.get(ModComponents.LINE_COMPONENT), "golden")) {
                if (!linePresent && this.lineInputSlot.getItem().isEmpty()) {
                    this.inputContainer.setItem(this.lineInputSlot.index, new ItemStack(ModItems.GOLDEN_LACED_FISHING_LINE, 1));
                    this.inputContainer.getItem(this.lineInputSlot.index).setDamageValue(rod.get(ModComponents.LINE_DURABILITY));
                    linePresent = true;
                }
            }
            if (Objects.equals(rod.get(ModComponents.LINE_COMPONENT), "diamond")) {
                if (!linePresent && this.lineInputSlot.getItem().isEmpty()) {
                    this.inputContainer.setItem(this.lineInputSlot.index, new ItemStack(ModItems.DIAMOND_LACED_FISHING_LINE, 1));
                    this.inputContainer.getItem(this.lineInputSlot.index).setDamageValue(rod.get(ModComponents.LINE_DURABILITY));
                    linePresent = true;
                }
            }
            if (Objects.equals(rod.get(ModComponents.LINE_COMPONENT), "netherite")) {
                if (!linePresent && this.lineInputSlot.getItem().isEmpty()) {
                    this.inputContainer.setItem(this.lineInputSlot.index, new ItemStack(ModItems.NETHERITE_LACED_FISHING_LINE, 1));
                    this.inputContainer.getItem(this.lineInputSlot.index).setDamageValue(rod.get(ModComponents.LINE_DURABILITY));
                    linePresent = true;
                }
            }
            if (!Objects.equals(rod.get(ModComponents.LINE_COMPONENT), "none")) {
                rod.set(ModComponents.LINE_COMPONENT, "none");
            }
        }
        // sinkers
        if (!sinker.isEmpty()) {
            if (sinker.getItem() == ModItems.COPPER_SINKER && !Objects.equals(rod.get(ModComponents.SINKER_COMPONENT), "copper")) {
                rod.set(ModComponents.SINKER_COMPONENT, "copper");
                rod.set(ModComponents.SINKER_DURABILITY, sinker.getDamageValue());
            }
            if (sinker.getItem() == ModItems.PRISMARINE_SINKER && !Objects.equals(rod.get(ModComponents.SINKER_COMPONENT), "prismarine")) {
                rod.set(ModComponents.SINKER_COMPONENT, "prismarine");
                rod.set(ModComponents.SINKER_DURABILITY, sinker.getDamageValue());
            }
            if (sinker.getItem() == ModItems.IRON_SINKER && !Objects.equals(rod.get(ModComponents.SINKER_COMPONENT), "iron")) {
                rod.set(ModComponents.SINKER_COMPONENT, "iron");
                rod.set(ModComponents.SINKER_DURABILITY, sinker.getDamageValue());
            }
            if (sinker.getItem() == ModItems.GOLDEN_SINKER && !Objects.equals(rod.get(ModComponents.SINKER_COMPONENT), "golden")) {
                rod.set(ModComponents.SINKER_COMPONENT, "golden");
                rod.set(ModComponents.SINKER_DURABILITY, sinker.getDamageValue());
            }
            if (sinker.getItem() == ModItems.DIAMOND_SINKER && !Objects.equals(rod.get(ModComponents.SINKER_COMPONENT), "diamond")) {
                rod.set(ModComponents.SINKER_COMPONENT, "diamond");
                rod.set(ModComponents.SINKER_DURABILITY, sinker.getDamageValue());
            }
            if (sinker.getItem() == ModItems.NETHERITE_SINKER && !Objects.equals(rod.get(ModComponents.SINKER_COMPONENT), "netherite")) {
                rod.set(ModComponents.SINKER_COMPONENT, "netherite");
                rod.set(ModComponents.SINKER_DURABILITY, sinker.getDamageValue());
            }
        }
        else {
            if (Objects.equals(rod.get(ModComponents.SINKER_COMPONENT), "copper")) {
                if (!sinkerPresent && this.sinkerInputSlot.getItem().isEmpty()) {
                    this.inputContainer.setItem(this.sinkerInputSlot.index, new ItemStack(ModItems.COPPER_SINKER, 1));
                    this.inputContainer.getItem(this.sinkerInputSlot.index).setDamageValue(rod.get(ModComponents.SINKER_DURABILITY));
                    sinkerPresent = true;
                }
            }
            if (Objects.equals(rod.get(ModComponents.SINKER_COMPONENT), "prismarine")) {
                if (!sinkerPresent && this.sinkerInputSlot.getItem().isEmpty()) {
                    this.inputContainer.setItem(this.sinkerInputSlot.index, new ItemStack(ModItems.PRISMARINE_SINKER, 1));
                    this.inputContainer.getItem(this.sinkerInputSlot.index).setDamageValue(rod.get(ModComponents.SINKER_DURABILITY));
                    sinkerPresent = true;
                }
            }
            if (Objects.equals(rod.get(ModComponents.SINKER_COMPONENT), "iron")) {
                if (!sinkerPresent && this.sinkerInputSlot.getItem().isEmpty()) {
                    this.inputContainer.setItem(this.sinkerInputSlot.index, new ItemStack(ModItems.IRON_SINKER, 1));
                    this.inputContainer.getItem(this.sinkerInputSlot.index).setDamageValue(rod.get(ModComponents.SINKER_DURABILITY));
                    sinkerPresent = true;
                }
            }
            if (Objects.equals(rod.get(ModComponents.SINKER_COMPONENT), "golden")) {
                if (!sinkerPresent && this.sinkerInputSlot.getItem().isEmpty()) {
                    this.inputContainer.setItem(this.sinkerInputSlot.index, new ItemStack(ModItems.GOLDEN_SINKER, 1));
                    this.inputContainer.getItem(this.sinkerInputSlot.index).setDamageValue(rod.get(ModComponents.SINKER_DURABILITY));
                    sinkerPresent = true;
                }
            }
            if (Objects.equals(rod.get(ModComponents.SINKER_COMPONENT), "diamond")) {
                if (!sinkerPresent && this.sinkerInputSlot.getItem().isEmpty()) {
                    this.inputContainer.setItem(this.sinkerInputSlot.index, new ItemStack(ModItems.DIAMOND_SINKER, 1));
                    this.inputContainer.getItem(this.sinkerInputSlot.index).setDamageValue(rod.get(ModComponents.SINKER_DURABILITY));
                    sinkerPresent = true;
                }
            }
            if (Objects.equals(rod.get(ModComponents.SINKER_COMPONENT), "netherite")) {
                if (!sinkerPresent && this.sinkerInputSlot.getItem().isEmpty()) {
                    this.inputContainer.setItem(this.sinkerInputSlot.index, new ItemStack(ModItems.NETHERITE_SINKER, 1));
                    this.inputContainer.getItem(this.sinkerInputSlot.index).setDamageValue(rod.get(ModComponents.SINKER_DURABILITY));
                    sinkerPresent = true;
                }
            }
            if (!Objects.equals(rod.get(ModComponents.SINKER_COMPONENT), "none")) {
                rod.set(ModComponents.SINKER_COMPONENT, "none");
            }
        }
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        this.access.execute((level, blockPos) -> {
            ItemStack hookStack = this.hookInputSlot.getItem();
            if (!hookStack.isEmpty()) {
                this.inputContainer.removeItemNoUpdate(this.hookInputSlot.index);
            }
            ItemStack lineStack = this.lineInputSlot.getItem();
            if (!lineStack.isEmpty()) {
                this.inputContainer.removeItemNoUpdate(this.lineInputSlot.index);
            }
            ItemStack sinkerStack = this.sinkerInputSlot.getItem();
            if (!sinkerStack.isEmpty()) {
                this.inputContainer.removeItemNoUpdate(this.sinkerInputSlot.index);
            }
            this.clearContainer(player, this.inputContainer);
        });
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(this.access, player, ModBlocks.ANGLING_TABLE);
    }
}
