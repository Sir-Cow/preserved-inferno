package sircow.preservedinferno.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record PreservedFletchingTableBlockData(BlockPos pos) {
    public static final StreamCodec<RegistryFriendlyByteBuf, PreservedFletchingTableBlockData> STREAM_CODEC =
            StreamCodec.composite(BlockPos.STREAM_CODEC, PreservedFletchingTableBlockData::pos, PreservedFletchingTableBlockData::new);
}
