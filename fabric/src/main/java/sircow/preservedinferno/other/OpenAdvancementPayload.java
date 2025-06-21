package sircow.preservedinferno.other;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;
import sircow.preservedinferno.Constants;

public record OpenAdvancementPayload() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<OpenAdvancementPayload> ID =
            new CustomPacketPayload.Type<>(Constants.id("trigger_open_advancement"));

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenAdvancementPayload> CODEC =
            StreamCodec.unit(new OpenAdvancementPayload());

    @Override
    public CustomPacketPayload.@NotNull Type<OpenAdvancementPayload> type() {
        return ID;
    }
}
