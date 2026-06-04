package sircow.preservedinferno.mixin;

import net.minecraft.client.renderer.LightmapRenderStateExtractor;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import sircow.preservedinferno.effect.ModEffects;

@Mixin(LightmapRenderStateExtractor.class)
public class LightmapRenderStateExtractorMixin {
    @ModifyArg(method = "extract", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;hasEffect(Lnet/minecraft/core/Holder;)Z"))
    private static Holder<MobEffect> preserved_inferno$replaceConduitPower(Holder<MobEffect> effect) {
        return effect == MobEffects.CONDUIT_POWER ? ModEffects.PINFERNO_CONDUIT_POWER.holder : effect;
    }
}
