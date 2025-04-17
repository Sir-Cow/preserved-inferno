package sircow.preservedinferno.mixin;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.RemoveStatusEffectsConsumeEffect;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.world.item.component.Consumables.defaultDrink;

@Mixin(Consumables.class)
public class ConsumablesMixin {
    @Mutable @Final @Shadow public static Consumable HONEY_BOTTLE;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void preserved_inferno$modifyConsumableComponents(CallbackInfo ci) {
        HONEY_BOTTLE = defaultDrink()
                .consumeSeconds(0.8F)
                .sound(SoundEvents.HONEY_DRINK)
                .onConsume(new RemoveStatusEffectsConsumeEffect(MobEffects.POISON))
                .build();
    }

    @ModifyConstant(method = "defaultDrink", constant = @Constant(floatValue = 1.6F))
    private static float preserved_inferno$modifyFloatValue(float original) { return 0.8F; }
}
