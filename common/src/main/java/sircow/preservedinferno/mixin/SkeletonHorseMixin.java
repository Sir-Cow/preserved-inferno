package sircow.preservedinferno.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.equine.SkeletonHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.item.ModItems;
import sircow.preservedinferno.trigger.ModTriggers;

@Mixin(SkeletonHorse.class)
public class SkeletonHorseMixin {
    // modify health value
    @ModifyArg(method = "createAttributes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;add(Lnet/minecraft/core/Holder;D)Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;", ordinal = 0), index = 1)
    private static double pinferno$modifyHealth(double baseValue) {
        baseValue = 20.0F;
        return baseValue;
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void pinferno$equipHorseArmor(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        SkeletonHorse horse = (SkeletonHorse) (Object) this;
        ItemStack stack = player.getItemInHand(hand);

        if (horse.isEquippableInSlot(stack, EquipmentSlot.BODY) && !horse.isWearingBodyArmor()) {
            horse.equipBodyArmor(player, stack);
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void pinferno$phantomSinewHealth(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        SkeletonHorse horse = (SkeletonHorse) (Object) this;
        ItemStack stack = player.getItemInHand(hand);

        if (!stack.is(ModItems.PHANTOM_SINEW.get())) return;

        if (!horse.isTamed()) {
            if (!player.level().isClientSide()) {
                stack.consume(1, player);
                horse.heal(2.0F);
                horse.modifyTemper(3);
            }
            cir.setReturnValue(InteractionResult.SUCCESS);
            return;
        }

        if (!player.level().isClientSide()) stack.consume(1, player);

        var attribute = horse.getAttribute(Attributes.MAX_HEALTH);

        if (attribute != null) {
            if (attribute.getBaseValue() >= 40.0D) horse.heal(1.0F);
            else if (horse.getRandom().nextFloat() < 0.3F) {
                double oldValue = attribute.getBaseValue();
                double newValue = Math.min(oldValue + 3.0D, 40.0D);

                if (newValue > oldValue) {
                    attribute.setBaseValue(newValue);
                    horse.addEffect(new MobEffectInstance(MobEffects.POISON, 20 * 5, 0, false, true));

                    if (player instanceof ServerPlayer serverPlayer) {
                        ModTriggers.UPGRADE_HORSE_HEALTH.trigger(serverPlayer);

                        var speed = horse.getAttribute(Attributes.MOVEMENT_SPEED);
                        var jump = horse.getAttribute(Attributes.JUMP_STRENGTH);

                        if (speed != null && jump != null && speed.getBaseValue() >= 0.4633D && jump.getBaseValue() >= 1.0D && attribute.getBaseValue() >= 40.0D) {
                            ModTriggers.MAX_HORSE_STATS.trigger(serverPlayer);
                        }
                    }
                }
            }
        }
        cir.setReturnValue(InteractionResult.SUCCESS);
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void pinferno$tame(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        SkeletonHorse horse = (SkeletonHorse) (Object) this;
        if (horse.isVehicle() || horse.isBaby()) return;
        if (horse.isTamed() && player.isSecondaryUseActive()) return;

        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(ModItems.PHANTOM_SINEW.get())) return;

        if (!stack.isEmpty()) {
            if (horse.isFood(stack)) {
                cir.setReturnValue(horse.fedFood(player, stack));
                return;
            }

            if (!horse.isTamed()) {
                horse.makeMad();
                cir.setReturnValue(InteractionResult.SUCCESS);
            }
            return;
        }

        if (!horse.isTamed()) {
            ((AbstractHorseAccessor) horse).pinferno$callDoPlayerRide(player);
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}
