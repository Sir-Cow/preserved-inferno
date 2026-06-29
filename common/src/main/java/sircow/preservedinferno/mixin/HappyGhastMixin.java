package sircow.preservedinferno.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.happyghast.HappyGhast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.trigger.ModTriggers;

@Mixin(HappyGhast.class)
public class HappyGhastMixin {
    // modify health value
    @ModifyArg(method = "createAttributes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;add(Lnet/minecraft/core/Holder;D)Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;", ordinal = 0), index = 1)
    private static double pinferno$modifyHealth(double baseValue) {
        baseValue = 40.0F;
        return baseValue;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void pinferno$checkBuildLimit(CallbackInfo ci) {
        HappyGhast ghast = (HappyGhast)(Object)this;

        if (ghast.level().isClientSide()) return;

        int maxY = ghast.level().getMaxY() - 1;

        for (Entity passenger : ghast.getPassengers()) {
            if (passenger instanceof ServerPlayer player) {
                if (player.getY() >= maxY) ModTriggers.HAPPY_GHAST_BUILD_HEIGHT.get().trigger(player);
            }
        }
    }
}
