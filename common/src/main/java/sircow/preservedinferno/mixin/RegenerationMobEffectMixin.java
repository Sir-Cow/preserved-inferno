package sircow.preservedinferno.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.effect.RegenerationMobEffect")
public class RegenerationMobEffectMixin {
    @Inject(method = "applyEffectTick", at = @At("HEAD"), cancellable = true)
    private void pinferno$invertForUndead(ServerLevel level, LivingEntity entity, int amplifier, CallbackInfoReturnable<Boolean> cir) {
        if (entity.isInvertedHealAndHarm()) {
            if (entity.getHealth() > 1.0F) entity.hurtServer(level, entity.damageSources().magic(), 1.0F);
            cir.setReturnValue(true);
        }
    }
}
