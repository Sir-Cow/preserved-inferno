package sircow.preservedinferno.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.item.ModItems;
import sircow.preservedinferno.other.ModTags;
import sircow.preservedinferno.trigger.ModTriggers;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {
    @Shadow private String itemName;
    @Shadow @Final private DataSlot cost;
    @Shadow private int repairItemCountCost;

    @Unique private int number = 0;

    @ModifyArg(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/DataSlot;set(I)V",
            ordinal = 4), index = 0)
    private int preserved_inferno$anvilRepairCostModifier(int originalCost) {
        Container inputSlots = ((ItemCombinerMenuAccessor) this).getInputSlots();
        ItemStack itemStack = inputSlots.getItem(0);
        ItemStack itemStack3 = inputSlots.getItem(1);

        boolean isRenaming = this.itemName != null && !this.itemName.isEmpty() && !this.itemName.equals(itemStack.getHoverName().getString());
        boolean combinationSlotEmpty = itemStack3.isEmpty();

        if (isRenaming && combinationSlotEmpty) {
            this.number = 1;
            return 1;
        }

        if (originalCost > 0) {
            this.number = 10;
            return 10;
        }
        this.number = 0;
        return 0;
    }

    @ModifyArg(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/DataSlot;set(I)V",
            ordinal = 5), index = 0)
    private int preserved_inferno$anvilRepairCostModifier2(int originalCost) {
        Container inputSlots = ((ItemCombinerMenuAccessor) this).getInputSlots();
        ItemStack itemStack = inputSlots.getItem(0);
        ItemStack itemStack3 = inputSlots.getItem(1);

        boolean isRenaming = this.itemName != null && !this.itemName.isEmpty() && !this.itemName.equals(itemStack.getHoverName().getString());
        boolean combinationSlotEmpty = itemStack3.isEmpty();

        if (isRenaming && combinationSlotEmpty) {
            this.number = 1;
            return 1;
        }
        this.number = 10;
        return 10;
    }

    @Inject(method = "onTake", at = @At("HEAD"))
    private void preserved_inferno$triggerAnvilRepair(Player player, ItemStack stack, CallbackInfo ci) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        if (this.number == 10) {
            if (!((ItemCombinerMenuAccessor) this).getInputSlots().getItem(1).is(Items.ENCHANTED_BOOK)) {
                ModTriggers.USED_ANVIL_REPAIR.trigger(serverPlayer);
            }
        }
    }

    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$repairRodUpgrades(CallbackInfo ci) {
        AnvilMenu self = (AnvilMenu) (Object) this;
        ItemCombinerMenuAccessor accessor = (ItemCombinerMenuAccessor) self;
        ItemStack leftInput = accessor.getInputSlots().getItem(0);
        ItemStack rightInput = accessor.getInputSlots().getItem(1);

        // custom item repair
        if (!leftInput.isEmpty() && !rightInput.isEmpty()) {
            if (leftInput.is(ModTags.ROD_UPGRADES) && rightInput.getItem() == ModItems.AQUATIC_FIBER) {
                ItemStack result = leftInput.copy();

                int repairedDamage = Math.max(result.getDamageValue() - 200, 0);
                result.setDamageValue(repairedDamage);

                accessor.getResultSlots().setItem(0, result);
                this.cost.set(1);
                this.repairItemCountCost = 10;
                ci.cancel();
            }
        }
    }
}
