package sircow.placeholder.mixin;

import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.consume.RemoveEffectsConsumeEffect;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.component.type.ConsumableComponents.drink;

@Mixin(ConsumableComponents.class)
public class ConsumableComponentMixin {
    @Mutable @Final @Shadow public static ConsumableComponent HONEY_BOTTLE;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void modifyConsumableComponents(CallbackInfo ci) {
        HONEY_BOTTLE = drink()
                .consumeSeconds(0.8F)
                .sound(SoundEvents.ITEM_HONEY_BOTTLE_DRINK)
                .consumeEffect(new RemoveEffectsConsumeEffect(StatusEffects.POISON))
                .build();
    }

    @ModifyConstant(method = "drink", constant = @Constant(floatValue = 1.6F))
    private static float modifyFloatValue(float original) { return 0.8F; }
}
