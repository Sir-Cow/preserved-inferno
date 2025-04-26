package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.monster.Evoker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Evoker.class)
public class EvokerMixin {
    // modify health value
    @ModifyArg(
            method = "createAttributes",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;add(Lnet/minecraft/core/Holder;D)Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;",
                    ordinal = 2),
            index = 1
    )
    private static double preserved_inferno$modifyHealth(double baseValue) {
        baseValue = 40.0F;
        return baseValue;
    }
}
