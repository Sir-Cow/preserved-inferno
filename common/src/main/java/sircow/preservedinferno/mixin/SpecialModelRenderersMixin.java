package sircow.preservedinferno.mixin;

import net.minecraft.client.renderer.special.SpecialModelRenderers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.client.CopperTridentSpecialRenderer;

@Mixin(SpecialModelRenderers.class)
public abstract class SpecialModelRenderersMixin {
    @Inject(method = "bootstrap", at = @At("TAIL"))
    private static void pinferno$injectCopperTridentRenderer(CallbackInfo ci) {
        SpecialModelRenderersAccessor.getIdMapper().put(
                Constants.id("copper_trident"),
                CopperTridentSpecialRenderer.Unbaked.MAP_CODEC
        );
    }
}
