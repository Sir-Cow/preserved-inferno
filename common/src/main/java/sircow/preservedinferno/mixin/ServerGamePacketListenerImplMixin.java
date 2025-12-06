package sircow.preservedinferno.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.scores.PlayerTeam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {
    @Redirect(method = "removePlayerFromWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;getDisplayName()Lnet/minecraft/network/chat/Component;"))
    private Component preserved_inferno$redirectPlayerDisplayName(ServerPlayer player) {
        PlayerTeam team = player.getTeam();
        if (team != null && !team.getPlayerPrefix().getString().isEmpty()) {
            MutableComponent prefixComponent = team.getPlayerPrefix().copy().withStyle(ChatFormatting.WHITE);
            MutableComponent playerNameComponent = player.getName().copy().withStyle(ChatFormatting.YELLOW);
            MutableComponent suffixComponent = team.getPlayerSuffix().copy().withStyle(ChatFormatting.YELLOW);

            return prefixComponent.append(playerNameComponent).append(suffixComponent);
        }
        return player.getDisplayName().copy().withStyle(ChatFormatting.YELLOW);
    }
}
