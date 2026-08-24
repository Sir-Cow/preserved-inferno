package sircow.preservedinferno.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.recipe.LoomRecipe;

import java.util.List;

public record SyncLoomRecipesPayload(List<LoomRecipe> recipes) implements CustomPacketPayload {
    public static final Type<SyncLoomRecipesPayload> TYPE = new Type<>(Constants.id("sync_loom_recipes"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncLoomRecipesPayload> CODEC = StreamCodec.composite(
            LoomRecipe.STREAM_CODEC.apply(ByteBufCodecs.list()),
            SyncLoomRecipesPayload::recipes,
            SyncLoomRecipesPayload::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
