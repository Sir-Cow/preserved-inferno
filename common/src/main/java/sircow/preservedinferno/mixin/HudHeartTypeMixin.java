package sircow.preservedinferno.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import sircow.preservedinferno.effect.ModEffects;

@Mixin(targets = "net.minecraft.client.gui.Hud$HeartType")
public class HudHeartTypeMixin {
    @ModifyExpressionValue(method = "forPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;hasEffect(Lnet/minecraft/core/Holder;)Z", ordinal = 0))
    private static boolean pinferno$fumigatedPoisonHearts(boolean original, Player player) {
        return original || player.hasEffect(ModEffects.fumigatedHolder());
    }
}
