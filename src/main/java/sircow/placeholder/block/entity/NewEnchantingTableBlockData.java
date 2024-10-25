package sircow.placeholder.block.entity;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.BlockPos;

public record NewEnchantingTableBlockData(BlockPos pos) {
    public static final PacketCodec<RegistryByteBuf, NewEnchantingTableBlockData> PACKET_CODEC =
            PacketCodec.tuple(BlockPos.PACKET_CODEC, NewEnchantingTableBlockData::pos, NewEnchantingTableBlockData::new);
}