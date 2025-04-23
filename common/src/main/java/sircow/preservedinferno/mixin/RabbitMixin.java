package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.animal.Rabbit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Rabbit.class)
public class RabbitMixin {
    @ModifyArg(
            method = "setVariant(Lnet/minecraft/world/entity/animal/Rabbit$Variant;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;setBaseValue(D)V"),
            index = 0
    )
    private double preserved_inferno$modifyArmorValue(double originalValue) {
        originalValue = 50.0;
        return originalValue;
    }
}
