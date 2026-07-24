package sircow.preservedinferno.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
        baseValue = 30.0F;
        return baseValue;
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void pinferno$phantomSinewHealth(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        SkeletonHorse horse = (SkeletonHorse) (Object) this;
        ItemStack stack = player.getItemInHand(hand);

        if (!stack.is(ModItems.PHANTOM_SINEW)) return;

        var attribute = horse.getAttribute(Attributes.MAX_HEALTH);

        if (attribute != null && horse.getRandom().nextFloat() < 0.3F && attribute.getBaseValue() < 30.0D) {
            if (!player.level().isClientSide()) {
                double oldValue = attribute.getBaseValue();
                double newValue = Math.min(oldValue + 1.0D, 30.0D);

                if (newValue > oldValue) {
                    attribute.setBaseValue(newValue);
                    horse.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 5, 0, false, true));

                    if (player instanceof ServerPlayer serverPlayer) {
                        ModTriggers.UPGRADE_HORSE_HEALTH.get().trigger(serverPlayer);

                        var speed = horse.getAttribute(Attributes.MOVEMENT_SPEED);
                        var jump = horse.getAttribute(Attributes.JUMP_STRENGTH);

                        if (speed != null && jump != null && speed.getBaseValue() >= 0.4633D && jump.getBaseValue() >= 1.0D && attribute.getBaseValue() >= 30.0D) {
                            ModTriggers.MAX_HORSE_STATS.get().trigger(serverPlayer);
                        }
                    }
                }
                stack.consume(1, player);
                if (player instanceof ServerPlayer serverPlayer) {
                    ModTriggers.UPGRADE_HORSE_HEALTH.get().trigger(serverPlayer);
                }
            }
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}
