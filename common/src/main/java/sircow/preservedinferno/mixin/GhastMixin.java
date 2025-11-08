package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Ghast.class)
public class GhastMixin {
    @Shadow private int explosionPower;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void preserved_inferno$onInit(EntityType<? extends Ghast> entityType, Level level, CallbackInfo ci) {
        explosionPower = 2;
    }

    // modify health value
    @ModifyArg(method = "createAttributes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;add(Lnet/minecraft/core/Holder;D)Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;", ordinal = 0), index = 1)
    private static double preserved_inferno$modifyHealth(double baseValue) {
        baseValue = 40.0F;
        return baseValue;
    }
}
