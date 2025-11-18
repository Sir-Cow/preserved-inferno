package sircow.preservedinferno.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.components.ModComponents;
import sircow.preservedinferno.other.HeatAccessor;
import sircow.preservedinferno.other.ModTags;
import sircow.preservedinferno.trigger.ModTriggers;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Unique private static final int HEAT_MODIFIER = 3;

    @Inject(method = "applyAfterUseComponentSideEffects", at = @At("HEAD"))
    private void preserved_inferno$drinkReduceHeat(LivingEntity entity, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (!entity.level().isClientSide() && entity instanceof Player player) {
            if (stack.getItem() instanceof PotionItem && !(stack.getItem() == Items.SPLASH_POTION)) {
                if (((HeatAccessor) player).preserved_inferno$getHeat() >= HEAT_MODIFIER) {
                    ((HeatAccessor) player).preserved_inferno$decreaseHeat(HEAT_MODIFIER);
                    if (player instanceof ServerPlayer serverPlayer) {
                        ModTriggers.DRINK_WATER.trigger(serverPlayer);
                    }
                }
                else if (((HeatAccessor) player).preserved_inferno$getHeat() < HEAT_MODIFIER && ((HeatAccessor) player).preserved_inferno$getHeat() > 0) {
                    ((HeatAccessor) player).preserved_inferno$setHeat(0);
                    if (player instanceof ServerPlayer serverPlayer) {
                        ModTriggers.DRINK_WATER.trigger(serverPlayer);
                    }
                }
            }
        }
    }

    @Inject(method = "getHoverName", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$dynamicTemplateName(CallbackInfoReturnable<Component> cir) {
        ItemStack self = (ItemStack)(Object)this;

        if (!self.is(ModTags.ARMOR_TRIM_TEMPLATES)) {
            return;
        }

        if (self.has(ModComponents.EXHAUSTED_TEMPLATE)) {
            MutableComponent prefix = Component.translatable("item.pinferno.exhausted_template");
            MutableComponent base = Component.translatable(self.getItem().getDescriptionId());
            MutableComponent result = prefix.append(Component.literal(" ")).append(base);

            cir.setReturnValue(result);
        }
    }
}
