package sircow.preservedinferno.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.components.ModComponents;
import sircow.preservedinferno.item.ModItems;
import sircow.preservedinferno.other.ModTags;
import sircow.preservedinferno.trigger.ModTriggers;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {
    @Shadow private String itemName;
    @Shadow @Final private DataSlot cost;
    @Shadow private int repairItemCountCost;

    @Unique private int number;

    @ModifyArg(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/DataSlot;set(I)V", ordinal = 4), index = 0)
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

    @ModifyArg(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/DataSlot;set(I)V", ordinal = 5), index = 0)
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
            if (((ItemCombinerMenuAccessor) this).getInputSlots().getItem(1).is(ModItems.REPAIR_KIT)) ModTriggers.USED_ANVIL_REPAIR.get().trigger(serverPlayer);
            if (((ItemCombinerMenuAccessor) this).getInputSlots().getItem(1).is(ModItems.FORGE_DUST)) ModTriggers.USED_FORGE_DUST.get().trigger(serverPlayer);
        }
    }

    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$disableVanillaMaterialRepair(CallbackInfo ci) {
        AnvilMenu self = (AnvilMenu) (Object) this;
        ItemCombinerMenuAccessor accessor = (ItemCombinerMenuAccessor) self;
        ItemStack left = accessor.getInputSlots().getItem(0);
        ItemStack right = accessor.getInputSlots().getItem(1);

        if (left.isEmpty() || right.isEmpty()) return;

        if (right.is(ModItems.REPAIR_KIT) || right.is(ModItems.FORGE_DUST) || (left.is(ModTags.ROD_UPGRADES) && right.is(ModItems.AQUATIC_FIBER))) return;

        if (left.isDamageableItem() && left.isValidRepairItem(right)) {
            accessor.getResultSlots().setItem(0, ItemStack.EMPTY);
            this.cost.set(0);
            ci.cancel();
        }
    }

    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$customRepair(CallbackInfo ci) {
        AnvilMenu self = (AnvilMenu) (Object) this;
        ItemCombinerMenuAccessor accessor = (ItemCombinerMenuAccessor) self;
        ItemStack leftInput = accessor.getInputSlots().getItem(0);
        ItemStack rightInput = accessor.getInputSlots().getItem(1);

        if (leftInput.isEmpty() || rightInput.isEmpty()) return;

        // repair kit
        if (rightInput.is(ModItems.REPAIR_KIT) && leftInput.isDamageableItem()) {
            double repairFraction = getRepairFraction(leftInput);
            if (repairFraction <= 0) return;

            int maxDamage = leftInput.getMaxDamage();
            int damage = leftInput.getDamageValue();

            if (damage <= 0) {
                accessor.getResultSlots().setItem(0, ItemStack.EMPTY);
                this.cost.set(0);
                ci.cancel();
                return;
            }

            double repairPerKit = maxDamage * repairFraction;
            if (repairPerKit <= 0) repairPerKit = 1;

            int needed = (int)Math.ceil(damage / repairPerKit);
            int count = Math.min(rightInput.getCount(), needed);

            double totalRepair = repairPerKit * count;
            ItemStack result = leftInput.copy();
            result.setDamageValue((int)Math.max(0, Math.round(damage - totalRepair)));

            if (this.itemName != null && !this.itemName.isEmpty()) {
                if (!this.itemName.equals(leftInput.getHoverName().getString())) result.set(DataComponents.CUSTOM_NAME, Component.literal(this.itemName));
            }
            else if (leftInput.has(DataComponents.CUSTOM_NAME)) result.remove(DataComponents.CUSTOM_NAME);

            accessor.getResultSlots().setItem(0, result);
            this.cost.set(1);
            this.repairItemCountCost = count;
            this.number = 10;

            ci.cancel();
            return;
        }

        // forge dust
        if (rightInput.is(ModItems.FORGE_DUST) && leftInput.isDamageableItem()) {
            String material = rightInput.get(ModComponents.FORGE_MATERIAL_COMPONENT);
            if (material == null) return;
            boolean valid = switch (material) {
                case "Copper" -> leftInput.isValidRepairItem(new ItemStack(Items.COPPER_INGOT));
                case "Gold" -> leftInput.isValidRepairItem(new ItemStack(Items.GOLD_INGOT));
                case "Iron" -> leftInput.isValidRepairItem(new ItemStack(Items.IRON_INGOT));
                case "Diamond" -> leftInput.isValidRepairItem(new ItemStack(Items.DIAMOND));
                case "Netherite" -> leftInput.isValidRepairItem(new ItemStack(Items.NETHERITE_SCRAP));
                case "Quartzite" -> leftInput.isValidRepairItem(new ItemStack(Items.QUARTZ));
                case "Nether Alloy" -> leftInput.isValidRepairItem(new ItemStack(ModItems.NETHER_ALLOY_INGOT));
                default -> false;
            };

            if (!valid) return;

            int maxDamage = leftInput.getMaxDamage();
            int damage = leftInput.getDamageValue();

            if (damage <= 0) {
                accessor.getResultSlots().setItem(0, ItemStack.EMPTY);
                this.cost.set(0);
                ci.cancel();
                return;
            }

            double repairPerDust = maxDamage * 0.3334;
            if (repairPerDust <= 0) repairPerDust = 1;

            int needed = (int)Math.ceil(damage / repairPerDust);
            int count = Math.min(rightInput.getCount(), needed);
            double totalRepair = repairPerDust * count;
            ItemStack result = leftInput.copy();
            result.setDamageValue((int)Math.max(0, Math.round(damage - totalRepair)));

            if (this.itemName != null && !this.itemName.isEmpty()) {
                if (!this.itemName.equals(leftInput.getHoverName().getString())) result.set(DataComponents.CUSTOM_NAME, Component.literal(this.itemName));
            }
            else if (leftInput.has(DataComponents.CUSTOM_NAME)) result.remove(DataComponents.CUSTOM_NAME);

            accessor.getResultSlots().setItem(0, result);
            this.cost.set(1);
            this.repairItemCountCost = count;
            this.number = 10;

            ci.cancel();
            return;
        }

        // aquatic fiber
        if (leftInput.is(ModTags.ROD_UPGRADES) && rightInput.getItem() == ModItems.AQUATIC_FIBER) {
            ItemStack result = leftInput.copy();
            int repairedDamage = Math.max(result.getDamageValue() - 200, 0);

            result.setDamageValue(repairedDamage);
            accessor.getResultSlots().setItem(0, result);
            this.cost.set(1);
            this.repairItemCountCost = 10;
            this.number = 10;
            ci.cancel();
        }
    }

    @ModifyVariable(method = "createResult", at = @At(value = "STORE", ordinal = 0), ordinal = 0)
    private int preserved_inferno$reduceMultitoolRepair(int repairAmount) {
        // cap multitool repair to 10%
        AnvilMenu self = (AnvilMenu) (Object) this;
        ItemCombinerMenuAccessor accessor = (ItemCombinerMenuAccessor) self;
        ItemStack input = accessor.getInputSlots().getItem(0);

        if (input.is(ModTags.MULTITOOLS)) return Math.max(1, repairAmount / 10);

        return repairAmount;
    }

    @Inject(method = "createResult", at = @At("TAIL"))
    private void preserved_inferno$stripEnchantmentsOnItemRepair(CallbackInfo ci) {
        AnvilMenu self = (AnvilMenu) (Object) this;
        ItemCombinerMenuAccessor accessor = (ItemCombinerMenuAccessor) self;
        ItemStack input = accessor.getInputSlots().getItem(0);
        ItemStack addition = accessor.getInputSlots().getItem(1);

        if (input.isEmpty() || addition.isEmpty()) return;

        boolean usingBook = addition.has(net.minecraft.core.component.DataComponents.STORED_ENCHANTMENTS);

        if (!usingBook && input.is(addition.getItem()) && input.isDamageableItem()) {
            ItemStack result = accessor.getResultSlots().getItem(0);
            if (!result.isEmpty()) EnchantmentHelper.setEnchantments(result, ItemEnchantments.EMPTY);
        }
    }

    @Unique
    private double getRepairFraction(ItemStack stack) {
        if (!stack.isDamageableItem()) return 0;
        if (stack.isValidRepairItem(new ItemStack(Items.OAK_PLANKS))
                || stack.isValidRepairItem(new ItemStack(Items.LEATHER))
                || stack.is(Items.CARROT_ON_A_STICK)
                || stack.is(Items.FLINT_AND_STEEL)
                || stack.is(Items.BRUSH)
                || stack.is(Items.WARPED_FUNGUS_ON_A_STICK)
                || stack.is(Items.BOW)
                || stack.is(Items.CROSSBOW)
        ) return 0.5;
        if (stack.isValidRepairItem(new ItemStack(Items.COBBLESTONE))) return 0.3334;
        if (stack.isValidRepairItem(new ItemStack(Items.QUARTZ))) return 0.25;
        if (stack.isValidRepairItem(new ItemStack(Items.COPPER_INGOT))
                || stack.is(ModItems.PRISMARINE_FISHING_HOOK)
                || stack.is(ModItems.PRISMARINE_LACED_FISHING_LINE)
                || stack.is(ModItems.PRISMARINE_SINKER)
                || stack.is(Items.WOLF_ARMOR)
        ) return 0.1667;
        if (stack.isValidRepairItem(new ItemStack(Items.GOLD_INGOT))) return 0.125;
        if (stack.isValidRepairItem(new ItemStack(Items.IRON_INGOT))
                || stack.isValidRepairItem(new ItemStack(Items.TURTLE_SCUTE))
                || stack.is(Items.TRIDENT)
                || stack.is(Items.MACE)
                || stack.is(Items.ELYTRA)
        ) return 0.0834;
        if (stack.isValidRepairItem(new ItemStack(ModItems.NETHER_ALLOY_INGOT))) return 0.0625;
        if (stack.isValidRepairItem(new ItemStack(Items.DIAMOND))) return 0.015625;
        if (stack.isValidRepairItem(new ItemStack(Items.NETHERITE_SCRAP))) return 0.0078125;

        return 0;
    }
}
