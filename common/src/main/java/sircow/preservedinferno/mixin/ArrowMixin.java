package sircow.preservedinferno.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.trigger.ModTriggers;

@Mixin(Arrow.class)
public abstract class ArrowMixin {
    @Inject(method = "doPostHurtEffects", at = @At("HEAD"))
    private void pinferno$onTippedArrowEffect(LivingEntity target, CallbackInfo ci) {
        Arrow arrow = (Arrow) (Object) this;

        if (arrow.getColor() == -1) return;

        if (arrow.getOwner() instanceof Player player) {
            if (player instanceof ServerPlayer serverPlayer) ModTriggers.USE_TIPPED_ARROW.get().trigger(serverPlayer);
        }
    }
}
