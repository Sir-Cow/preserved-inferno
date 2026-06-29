package sircow.preservedinferno.mixin;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.network.BashfulPayload;
import sircow.preservedinferno.other.ModTags;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "handleKeybinds", at = @At("HEAD"))
    private void pinferno$handleBashfulInput(CallbackInfo ci) {
        Minecraft client = (Minecraft) (Object) this;
        if (client.player != null && client.options.keyAttack.isDown()) {
            if (client.player.isUsingItem() && client.player.getUseItem().is(ModTags.SHIELDS)) ClientPlayNetworking.send(new BashfulPayload());
        }
    }
}
