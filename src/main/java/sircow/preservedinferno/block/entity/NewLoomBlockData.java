package sircow.preservedinferno.block.entity;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.BlockPos;

public record NewLoomBlockData(BlockPos pos) {
    public static final PacketCodec<RegistryByteBuf, NewLoomBlockData> PACKET_CODEC =
            PacketCodec.tuple(BlockPos.PACKET_CODEC, NewLoomBlockData::pos, NewLoomBlockData::new);
}
