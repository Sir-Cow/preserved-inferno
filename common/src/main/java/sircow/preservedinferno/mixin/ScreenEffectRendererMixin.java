package sircow.preservedinferno.mixin;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import sircow.preservedinferno.other.HeatAccessor;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {
    // first person fire render
    @Redirect(method = "submit", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isOnFire()Z"))
    private boolean pinferno$shouldRenderHeatFire(LocalPlayer player) {
        return (player.isOnFire() || player instanceof HeatAccessor accessor && accessor.pinferno$getHeat() >= 100) && (!player.isCreative() && !player.isSpectator());
    }
}
