package sircow.preservedinferno.mixin;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.LevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.network.RespawnSyncPayload;

@Mixin(ServerPlayer.class)
public class ServerPlayerRespawnMixin {
    @Shadow @Final private MinecraftServer server;
    @Inject(method = "setRespawnPosition", at = @At("TAIL"))
    private void preserved_inferno$syncRespawn(ServerPlayer.RespawnConfig respawnConfig, boolean showMessage, CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer)(Object)this;
        GlobalPos pos = null;

        if (respawnConfig != null) {
            LevelData.RespawnData data = respawnConfig.respawnData();
            GlobalPos candidate = data.globalPos();

            if (candidate != null) {
                var level = this.server.getLevel(candidate.dimension());

                if (level != null) {
                    var state = level.getBlockState(candidate.pos());
                    if (state.is(BlockTags.BEDS) || state.is(Blocks.RESPAWN_ANCHOR)) pos = candidate;
                }
            }
        }
        ServerPlayNetworking.send(player, new RespawnSyncPayload(pos));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void preserved_inferno$validateRespawn(CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer)(Object)this;
        ServerPlayer.RespawnConfig config = player.getRespawnConfig();
        GlobalPos pos = null;

        if (config != null) {
            LevelData.RespawnData data = config.respawnData();
            if (data != null) pos = data.globalPos();
        }
        ServerPlayNetworking.send(player, new RespawnSyncPayload(pos));
    }
}
