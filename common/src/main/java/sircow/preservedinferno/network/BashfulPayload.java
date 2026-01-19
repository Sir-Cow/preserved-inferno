package sircow.preservedinferno.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.Constants;

public record BashfulPayload() implements CustomPacketPayload {
    public static final Type<BashfulPayload> TYPE = new Type<>(Constants.id("bashful_dash"));
    public static final StreamCodec<RegistryFriendlyByteBuf, BashfulPayload> CODEC = StreamCodec.unit(new BashfulPayload());

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
