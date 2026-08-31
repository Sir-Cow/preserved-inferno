package sircow.preservedinferno.codec;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record BlockData(boolean empty) {
    public static final StreamCodec<RegistryFriendlyByteBuf, BlockData> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            BlockData::empty,
            BlockData::new
    );
}
