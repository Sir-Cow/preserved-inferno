package sircow.preservedinferno.codec;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ItemData(int containerSize) {
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemData> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            ItemData::containerSize,
            ItemData::new
    );

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(containerSize);
    }

    public static ItemData read(RegistryFriendlyByteBuf buf) {
        return new ItemData(buf.readVarInt());
    }
}
