package sircow.preservedinferno.screen;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.FabricPreservedInferno;
import sircow.preservedinferno.block.entity.PreservedCauldronBlockData;
import sircow.preservedinferno.block.entity.PreservedCauldronBlockEntity;
import sircow.preservedinferno.fluid.CauldronFluid;
import sircow.preservedinferno.recipe.CauldronRecipe;
import sircow.preservedinferno.recipe.CauldronRecipeInput;
import sircow.preservedinferno.recipe.ModRecipes;

import java.util.Optional;

public class PreservedCauldronMenu extends AbstractContainerMenu {
    private final Container inventory;
    private final ContainerData propertyDelegate, propertyDelegateTwo;

    public PreservedCauldronMenu(int syncId, Inventory inventory, PreservedCauldronBlockData blockData) {
        this(syncId, inventory, new SimpleContainerData(2), new SimpleContainerData(3), new SimpleContainer(4));
    }

    public PreservedCauldronMenu(int syncId, Inventory playerInventory, ContainerData arrayPropertyDelegate, ContainerData arrayPropertyDelegateTwo, Container inventory) {
        super(FabricPreservedInferno.PRESERVED_CAULDRON_MENU_TYPE, syncId);
        checkContainerSize(inventory, 4);
        checkContainerDataCount(arrayPropertyDelegate, 2);
        checkContainerDataCount(arrayPropertyDelegateTwo, 3);
        this.inventory = inventory;
        inventory.startOpen(playerInventory.player);
        this.propertyDelegate = arrayPropertyDelegate;
        this.propertyDelegateTwo = arrayPropertyDelegateTwo;

        this.addSlot(new Slot(inventory, 0, 80, 15)); // item input
        this.addSlot(new Slot(inventory, 1, 152, 52) { // fluid input
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return PreservedCauldronBlockEntity.isFluidInputItem(stack);
            }
        });
        this.addSlot(new Slot(inventory, 2, 80, 52) { // item output
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }
        });
        this.addSlot(new Slot(inventory, 3, 132, 52) { // bottle/bucket output
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }
        });

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addDataSlots(arrayPropertyDelegate);
        addDataSlots(arrayPropertyDelegateTwo);
    }

    public boolean isCrafting() {
        return propertyDelegate.get(0) > 0;
    }

    public int getScaledProgressArrow() {
        int progress = this.propertyDelegate.get(0);
        int maxProgress = this.propertyDelegate.get(1);
        int progressArrowSize = 16;

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    public int getFluidAmount() {
        return this.propertyDelegateTwo.get(0);
    }

    public int getMaxFluidAmount() {
        return this.propertyDelegateTwo.get(1);
    }

    public int getScaledProgressFluid() {
        int fluidProgress = this.propertyDelegateTwo.get(0);
        int maxFluidProgress = this.propertyDelegateTwo.get(1);
        int progressFluidSize = 32;

        return maxFluidProgress != 0 && fluidProgress != 0 ? fluidProgress * progressFluidSize / maxFluidProgress : 0;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NonNull Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);

        if (slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();

            if (invSlot == 2 || invSlot == 3) {
                if (!this.moveItemStackTo(originalStack, 4, 40, true)) return ItemStack.EMPTY;
                slot.onQuickCraft(originalStack, newStack);
            }
            else if (invSlot != 0 && invSlot != 1) {
                boolean isFluidInput = PreservedCauldronBlockEntity.isFluidInputItem(originalStack);

                if (isFluidInput) {
                    if (!this.moveItemStackTo(originalStack, 1, 2, false)) return ItemStack.EMPTY;
                }
                else {
                    ItemStack checkStack = originalStack.copy();
                    checkStack.remove(DataComponents.DYED_COLOR);

                    boolean canInsertIntoInput = false;

                    if (player.level() instanceof ServerLevel serverLevel) {
                        Optional<RecipeHolder<CauldronRecipe>> recipeMatch =
                                serverLevel.recipeAccess().getRecipeFor(
                                        ModRecipes.CAULDRON_TYPE,
                                        new CauldronRecipeInput(checkStack, this.getFluid(), this.getMaxFluidAmount()),
                                        serverLevel
                                );

                        if (recipeMatch.isPresent()) canInsertIntoInput = true;
                        else if (this.getFluid() == CauldronFluid.EMPTY) {
                            for (RecipeHolder<?> holder : serverLevel.recipeAccess().getRecipes()) {
                                if (holder.value() instanceof CauldronRecipe cauldronRecipe) {
                                    if (cauldronRecipe.inputItem().test(checkStack)) {
                                        canInsertIntoInput = true;
                                        break;
                                    }
                                }
                            }
                        }
                        else if (this.getFluid() == CauldronFluid.LAVA && !originalStack.has(DataComponents.DAMAGE_RESISTANT)) {
                            canInsertIntoInput = true;
                        }
                    }

                    if (canInsertIntoInput) {
                        if (!this.moveItemStackTo(originalStack, 0, 1, false)) return ItemStack.EMPTY;
                    }
                    else if (invSlot >= 4 && invSlot < 31) {
                        if (!this.moveItemStackTo(originalStack, 31, 40, false)) return ItemStack.EMPTY;
                    }
                    else if (invSlot >= 31 && invSlot < 40) {
                        if (!this.moveItemStackTo(originalStack, 4, 31, false)) return ItemStack.EMPTY;
                    }
                }
            }
            else {
                if (!this.moveItemStackTo(originalStack, 4, 40, false)) return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
            else slot.setChanged();

            if (originalStack.getCount() == newStack.getCount()) return ItemStack.EMPTY;

            slot.onTake(player, originalStack);
        }
        return newStack;
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        return this.inventory.stillValid(player);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    public CauldronFluid getFluid() {
        int ordinal = propertyDelegateTwo.get(2);
        CauldronFluid[] values = CauldronFluid.values();

        if (ordinal < 0 || ordinal >= values.length) return CauldronFluid.EMPTY;
        return values[ordinal];
    }
}
