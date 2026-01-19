package sircow.preservedinferno.mixin;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import sircow.preservedinferno.other.HeatAccessor;

@Mixin(ScreenEffectRenderer.class)
public abstract class ScreenEffectRendererMixin {
    // first person fire render
    @Redirect(method = "renderScreenEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isOnFire()Z"))
    private boolean preserved_inferno$shouldRenderHeatFire(LocalPlayer player) {
        return (player.isOnFire() || player instanceof HeatAccessor accessor && accessor.preserved_inferno$getHeat() >= 100) && (!player.isCreative() && !player.isSpectator());
    }
}
