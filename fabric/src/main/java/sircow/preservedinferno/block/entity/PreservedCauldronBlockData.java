package sircow.preservedinferno.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record PreservedCauldronBlockData(BlockPos pos) {
    public static final StreamCodec<RegistryFriendlyByteBuf, PreservedCauldronBlockData> STREAM_CODEC =
            StreamCodec.composite(BlockPos.STREAM_CODEC, PreservedCauldronBlockData::pos, PreservedCauldronBlockData::new);
}
