package sircow.preservedinferno.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$preventEffectsOnShieldBlock(MobEffectInstance effectInstance, Entity source, CallbackInfoReturnable<Boolean> cir) {
        if ((LivingEntity)(Object)this instanceof Player player) {
            if (player.isBlocking()) {
                if (source instanceof Monster) {
                    cir.setReturnValue(false);
                }
            }
        }
    }
}
