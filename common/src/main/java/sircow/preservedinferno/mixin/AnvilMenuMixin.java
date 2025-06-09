package sircow.preservedinferno.mixin;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {
    @Shadow private String itemName;

    @ModifyArg(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/DataSlot;set(I)V",
            ordinal = 4), index = 0)
    private int preserved_inferno$anvilRepairCostModifier(int originalCost) {
        Container inputSlots = ((ItemCombinerMenuAccessor) this).getInputSlots();
        ItemStack itemStack = inputSlots.getItem(0);
        ItemStack itemStack3 = inputSlots.getItem(1);

        boolean isRenaming = this.itemName != null && !this.itemName.isEmpty() && !this.itemName.equals(itemStack.getHoverName().getString());
        boolean combinationSlotEmpty = itemStack3.isEmpty();

        if (isRenaming && combinationSlotEmpty) {
            return 1;
        }

        if (originalCost > 0) {
            return 10;
        }
        return 0;
    }

    @ModifyArg(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/DataSlot;set(I)V",
            ordinal = 5), index = 0)
    private int preserved_inferno$anvilRepairCostModifer2(int originalCost) {
        Container inputSlots = ((ItemCombinerMenuAccessor) this).getInputSlots();
        ItemStack itemStack = inputSlots.getItem(0);
        ItemStack itemStack3 = inputSlots.getItem(1);

        boolean isRenaming = this.itemName != null && !this.itemName.isEmpty() && !this.itemName.equals(itemStack.getHoverName().getString());
        boolean combinationSlotEmpty = itemStack3.isEmpty();

        if (isRenaming && combinationSlotEmpty) {
            return 1;
        }
        return 10;
    }
}
