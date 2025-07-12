package sircow.preservedinferno.mixin;

import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.network.protocol.game.ClientboundUpdateAdvancementsPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.client.FabricPreservedInfernoClient;

@Mixin(ClientAdvancements.class)
public class ClientAdvancementsMixin {
    @Inject(method = "update", at = @At("TAIL"))
    private void onUpdateAdvancements(ClientboundUpdateAdvancementsPacket packet, CallbackInfo ci) {
        FabricPreservedInfernoClient.advancementsSynced = true;
    }
}
