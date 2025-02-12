package sircow.preservedinferno.block.entity;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.BlockPos;

public record NewFletchingTableBlockData(BlockPos pos) {
    public static final PacketCodec<RegistryByteBuf, NewFletchingTableBlockData> PACKET_CODEC =
            PacketCodec.tuple(BlockPos.PACKET_CODEC, NewFletchingTableBlockData::pos, NewFletchingTableBlockData::new);
}
