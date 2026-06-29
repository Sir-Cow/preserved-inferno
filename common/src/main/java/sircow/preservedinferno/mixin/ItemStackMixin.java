package sircow.preservedinferno.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.components.ModComponents;
import sircow.preservedinferno.item.ModItems;
import sircow.preservedinferno.other.HeatAccessor;
import sircow.preservedinferno.other.ModTags;
import sircow.preservedinferno.trigger.ModTriggers;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Unique private static final int HEAT_MODIFIER = 3;

    @Inject(method = "applyAfterUseComponentSideEffects", at = @At("HEAD"))
    private void pinferno$drinkReduceHeat(LivingEntity entity, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (!entity.level().isClientSide() && entity instanceof Player player) {
            if (stack.getItem() instanceof PotionItem && !(stack.getItem() == Items.SPLASH_POTION)) {
                if (((HeatAccessor) player).pinferno$getHeat() >= HEAT_MODIFIER) {
                    ((HeatAccessor) player).pinferno$decreaseHeat(HEAT_MODIFIER);
                    if (player instanceof ServerPlayer serverPlayer) {
                        ModTriggers.DRINK_WATER.get().trigger(serverPlayer);
                    }
                }
                else if (((HeatAccessor) player).pinferno$getHeat() < HEAT_MODIFIER && ((HeatAccessor) player).pinferno$getHeat() > 0) {
                    ((HeatAccessor) player).pinferno$setHeat(0);
                    if (player instanceof ServerPlayer serverPlayer) {
                        ModTriggers.DRINK_WATER.get().trigger(serverPlayer);
                    }
                }
            }
        }
    }

    @Inject(method = "getHoverName", at = @At("HEAD"), cancellable = true)
    private void pinferno$dynamicTemplateName(CallbackInfoReturnable<Component> cir) {
        ItemStack self = (ItemStack)(Object)this;

        if (!self.is(ModTags.ARMOR_TRIM_TEMPLATES)) return;

        if (self.has(ModComponents.EXHAUSTED_TEMPLATE)) {
            MutableComponent prefix = Component.translatable("item.pinferno.exhausted_template");
            MutableComponent base = Component.translatable(self.getItem().getDescriptionId());
            MutableComponent result = prefix.append(Component.literal(" ")).append(base);

            cir.setReturnValue(result);
        }
    }

    @Inject(method = "addAttributeTooltips", at = @At("HEAD"), cancellable = true)
    private void pinferno$replaceAttributeTooltips(Consumer<Component> tooltipAdder, TooltipDisplay tooltipDisplay, @Nullable Player player, CallbackInfo ci) {
        if (!tooltipDisplay.shows(DataComponents.ATTRIBUTE_MODIFIERS)) return;

        ItemStack stack = (ItemStack)(Object)this;
        Item item = stack.getItem();
        ItemEnchantments enchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        Holder<Enchantment> efficiency = null;

        if (player != null) efficiency = player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.EFFICIENCY);

        boolean hasEfficiency = efficiency != null && enchantments.getLevel(efficiency) > 0;

        for (EquipmentSlotGroup slot : EquipmentSlotGroup.values()) {
            MutableBoolean first = new MutableBoolean(true);

            stack.forEachModifier(slot, (attribute, modifier, display) -> {
                if (display == ItemAttributeModifiers.Display.hidden()) return;

                if (hasEfficiency && attribute.is(Attributes.MINING_EFFICIENCY)) return;

                if (first.isTrue()) {
                    tooltipAdder.accept(CommonComponents.EMPTY);
                    tooltipAdder.accept(Component.translatable("item.modifiers." + slot.getSerializedName()).withStyle(ChatFormatting.GRAY));
                    first.setFalse();
                }
                display.apply(tooltipAdder, player, attribute, modifier);
            });
        }

        float baseSpeed = getBaseSpeed(item);

        if (baseSpeed > 1.0F) {
            double finalSpeed = baseSpeed;

            if (hasEfficiency) finalSpeed += 6;

            tooltipAdder.accept(Component.literal(" ").append(Component.translatable("attribute.modifier.equals.0", ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(finalSpeed), Component.translatable(Attributes.MINING_EFFICIENCY.value().getDescriptionId())).withStyle(ChatFormatting.BLUE)));
        }
        ci.cancel();
    }

    @Unique
    private static float getBaseSpeed(Item item) {
        float baseSpeed = 1.0F;
        if (item == Items.WOODEN_PICKAXE || item == Items.WOODEN_SHOVEL || item == Items.WOODEN_AXE || item == Items.WOODEN_HOE || item == ModItems.WOODEN_MULTITOOL)
            baseSpeed = 1.5F;
        else if (item == Items.STONE_PICKAXE || item == Items.STONE_SHOVEL || item == Items.STONE_AXE || item == Items.STONE_HOE || item == ModItems.STONE_MULTITOOL)
            baseSpeed = 3.0F;
        else if (item == Items.COPPER_PICKAXE || item == Items.COPPER_SHOVEL || item == Items.COPPER_AXE || item == Items.COPPER_HOE || item == ModItems.COPPER_MULTITOOL)
            baseSpeed = 4.5F;
        else if (item == Items.IRON_PICKAXE || item == Items.IRON_SHOVEL || item == Items.IRON_AXE || item == Items.IRON_HOE || item == ModItems.IRON_MULTITOOL)
            baseSpeed = 6.0F;
        else if (item == Items.GOLDEN_PICKAXE || item == Items.GOLDEN_SHOVEL || item == Items.GOLDEN_AXE || item == Items.GOLDEN_HOE || item == ModItems.GOLDEN_MULTITOOL)
            baseSpeed = 12.0F;
        else if (item == Items.DIAMOND_PICKAXE || item == Items.DIAMOND_SHOVEL || item == Items.DIAMOND_AXE || item == Items.DIAMOND_HOE || item == ModItems.DIAMOND_MULTITOOL)
            baseSpeed = 9.0F;
        else if (item == Items.NETHERITE_PICKAXE || item == Items.NETHERITE_SHOVEL || item == Items.NETHERITE_AXE || item == Items.NETHERITE_HOE || item == ModItems.NETHERITE_MULTITOOL)
            baseSpeed = 16.0F;
        else if (item == ModItems.QUARTZITE_PICKAXE || item == ModItems.QUARTZITE_SHOVEL || item == ModItems.QUARTZITE_AXE || item == ModItems.QUARTZITE_SCYTHE || item == ModItems.QUARTZITE_MULTITOOL)
            baseSpeed = 8.0F;
        else if (item == ModItems.NETHER_ALLOY_PICKAXE || item == ModItems.NETHER_ALLOY_SHOVEL || item == ModItems.NETHER_ALLOY_AXE || item == ModItems.NETHER_ALLOY_SCYTHE || item == ModItems.NETHER_ALLOY_MULTITOOL)
            baseSpeed = 16.0F;
        return baseSpeed;
    }

    @Inject(method = "postHurtEnemy", at = @At("HEAD"), cancellable = true)
    private void pinferno$preventDoubleDurabilityLossOnAttack(LivingEntity mob, LivingEntity attacker, CallbackInfo ci) {
        ItemStack self = (ItemStack) (Object) this;

        if (self.is(ItemTags.PICKAXES) || self.is(ItemTags.SHOVELS) || self.is(ItemTags.HOES) || self.is(ItemTags.AXES)) {
            self.getItem().postHurtEnemy(self, mob, attacker);
            self.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
            ci.cancel();
        }
    }
}
