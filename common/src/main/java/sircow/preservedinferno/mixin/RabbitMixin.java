package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.animal.rabbit.Rabbit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Rabbit.class)
public class RabbitMixin {
    // modify health value
    @ModifyArg(method = "createAttributes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;add(Lnet/minecraft/core/Holder;D)Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;", ordinal = 0), index = 1)
    private static double preserved_inferno$modifyHealth(double baseValue) {
        baseValue = 8.0F;
        return baseValue;
    }

    @ModifyArg(method = "setVariant", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;setBaseValue(D)V"), index = 0)
    private double preserved_inferno$modifyArmorValue(double originalValue) {
        originalValue = 50.0;
        return originalValue;
    }
}
