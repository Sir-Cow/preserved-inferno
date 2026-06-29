package sircow.preservedinferno.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.level.storage.LevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.client.ClientRespawnData;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {
    @Inject(method = "getRespawnData", at = @At("HEAD"), cancellable = true)
    private void pinferno$overrideRespawnData(CallbackInfoReturnable<LevelData.RespawnData> cir) {
        GlobalPos respawn = ClientRespawnData.respawnPos;

        if (respawn != null) cir.setReturnValue(new LevelData.RespawnData(respawn, 0.0F, 0.0F));
    }
}
