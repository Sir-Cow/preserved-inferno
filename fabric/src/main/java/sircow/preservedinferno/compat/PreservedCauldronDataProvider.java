package sircow.preservedinferno.compat;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.block.entity.PreservedCauldronBlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.StreamServerDataProvider;

import java.util.List;

public class PreservedCauldronDataProvider implements StreamServerDataProvider<BlockAccessor, PreservedCauldronDataProvider.Data> {
    public static final PreservedCauldronDataProvider INSTANCE = new PreservedCauldronDataProvider();

    @Override
    public Data streamData(@NonNull BlockAccessor accessor) {
        PreservedCauldronBlockEntity cauldron = accessor.typedBlockEntity();
        return new Data(cauldron.progress, cauldron.maxProgress, List.of(cauldron.getItem(0), cauldron.getItem(1), cauldron.getItem(2)));
    }

    @Override
    public @NonNull StreamCodec<RegistryFriendlyByteBuf, Data> streamCodec() {
        return Data.STREAM_CODEC;
    }

    @Override
    public @NonNull Identifier getUid() {
        return JadePlugin.CAULDRON_PROGRESS;
    }

    public record Data(int progress, int total, List<ItemStack> inventory) {
        public static final StreamCodec<RegistryFriendlyByteBuf, Data> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, Data::progress, ByteBufCodecs.VAR_INT, Data::total, ItemStack.OPTIONAL_LIST_STREAM_CODEC, Data::inventory, Data::new);
    }
}
