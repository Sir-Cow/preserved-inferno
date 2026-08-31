package sircow.preservedinferno.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.trigger.ModTriggers;

@Mixin(AbstractChestedHorse.class)
public class ChestedHorseMixin {
    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void pinferno$goldenFoodHealth(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        AbstractHorse horse = (AbstractHorse) (Object) this;
        ItemStack stack = player.getItemInHand(hand);

        if (!horse.isTamed()) return;
        if (!stack.is(Items.GOLDEN_APPLE) && !stack.is(Items.ENCHANTED_GOLDEN_APPLE) && !stack.is(Items.GOLDEN_CARROT)) return;

        var attribute = horse.getAttribute(Attributes.MAX_HEALTH);

        if (attribute != null && attribute.getBaseValue() < 40.0D) {
            if (!player.level().isClientSide()) {
                double oldValue = attribute.getBaseValue();
                double newValue = Math.min(oldValue + 1.0D, 40.0D);

                if (newValue > oldValue) {
                    attribute.setBaseValue(newValue);
                    horse.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 5, 0, false, true));

                    if (player instanceof ServerPlayer serverPlayer) {
                        ModTriggers.UPGRADE_HORSE_HEALTH.trigger(serverPlayer);

                        var speed = horse.getAttribute(Attributes.MOVEMENT_SPEED);
                        var jump = horse.getAttribute(Attributes.JUMP_STRENGTH);

                        if (speed != null && jump != null && speed.getBaseValue() >= 0.4633D && jump.getBaseValue() >= 1.0D && attribute.getBaseValue() >= 40.0D) {
                            ModTriggers.MAX_HORSE_STATS.trigger(serverPlayer);
                        }
                    }
                }
                stack.consume(1, player);
                if (player instanceof ServerPlayer serverPlayer) {
                    ModTriggers.UPGRADE_HORSE_HEALTH.trigger(serverPlayer);
                }
            }
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}