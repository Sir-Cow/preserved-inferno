package sircow.preservedinferno.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.recipe.CauldronRecipe;

import java.util.List;

public record SyncCauldronRecipesPayload(List<CauldronRecipe> recipes) implements CustomPacketPayload {
    public static final Type<SyncCauldronRecipesPayload> TYPE = new Type<>(Constants.id("sync_cauldron_recipes"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncCauldronRecipesPayload> CODEC = StreamCodec.composite(
            CauldronRecipe.STREAM_CODEC.apply(ByteBufCodecs.list()),
            SyncCauldronRecipesPayload::recipes,
            SyncCauldronRecipesPayload::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
