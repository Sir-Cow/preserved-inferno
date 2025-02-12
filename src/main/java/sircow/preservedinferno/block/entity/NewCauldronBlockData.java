package sircow.preservedinferno.block.entity;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.BlockPos;

public record NewCauldronBlockData(BlockPos pos) {
    public static final PacketCodec<RegistryByteBuf, NewCauldronBlockData> PACKET_CODEC =
            PacketCodec.tuple(BlockPos.PACKET_CODEC, NewCauldronBlockData::pos, NewCauldronBlockData::new);
}
