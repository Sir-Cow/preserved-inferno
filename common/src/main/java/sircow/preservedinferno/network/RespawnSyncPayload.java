package sircow.preservedinferno.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.Constants;

public record RespawnSyncPayload(GlobalPos pos) implements CustomPacketPayload {
    public static final Type<RespawnSyncPayload> TYPE = new Type<>(Constants.id("respawn_sync"));
    public static final StreamCodec<FriendlyByteBuf, RespawnSyncPayload> CODEC = StreamCodec.of(RespawnSyncPayload::write, RespawnSyncPayload::read);

    private static RespawnSyncPayload read(FriendlyByteBuf buf) {
        boolean present = buf.readBoolean();
        if (!present) return new RespawnSyncPayload(null);

        ResourceKey<Level> dimension = buf.readResourceKey(net.minecraft.core.registries.Registries.DIMENSION);
        BlockPos pos = buf.readBlockPos();
        return new RespawnSyncPayload(GlobalPos.of(dimension, pos));
    }

    private static void write(FriendlyByteBuf buf, RespawnSyncPayload payload) {
        if (payload.pos == null) {
            buf.writeBoolean(false);
            return;
        }

        buf.writeBoolean(true);
        buf.writeResourceKey(payload.pos.dimension());
        buf.writeBlockPos(payload.pos.pos());
    }

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
