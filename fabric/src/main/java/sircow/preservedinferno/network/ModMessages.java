package sircow.preservedinferno.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.advancement.ModAdvancements;

import java.util.UUID;

public class ModMessages {
    public record PlayerPointsPayload(UUID playerUUID, int points) implements CustomPacketPayload {
        public static final Type<PlayerPointsPayload> TYPE = new Type<>(Constants.id("player_points_update"));

        public static final StreamCodec<RegistryFriendlyByteBuf, PlayerPointsPayload> CODEC = CustomPacketPayload.codec(
                PlayerPointsPayload::write, PlayerPointsPayload::read
        );

        private static PlayerPointsPayload read(RegistryFriendlyByteBuf buf) {
            return new PlayerPointsPayload(buf.readUUID(), buf.readInt());
        }

        private void write(RegistryFriendlyByteBuf buf) {
            buf.writeUUID(playerUUID);
            buf.writeInt(points);
        }

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(PlayerPointsPayload.TYPE, (payload, context) -> context.client().execute(() -> ModAdvancements.setPlayerPoints(payload.playerUUID(), payload.points())));
    }

    public static void registerMessages() {
        // Constants.LOG.info("Registering Mod Messages for " + Constants.MOD_ID);
        PayloadTypeRegistry.playS2C().register(PlayerPointsPayload.TYPE, PlayerPointsPayload.CODEC);
    }
}
