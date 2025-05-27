package sircow.preservedinferno.other;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import sircow.preservedinferno.item.FlareParticleDyeRecipe;

public class FlareParticleDyeRecipeSerializer implements RecipeSerializer<FlareParticleDyeRecipe> {

    public static final MapCodec<FlareParticleDyeRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(FlareParticleDyeRecipe::category)
    ).apply(instance, FlareParticleDyeRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FlareParticleDyeRecipe> STREAM_CODEC = StreamCodec.composite(
            CraftingBookCategory.STREAM_CODEC,
            FlareParticleDyeRecipe::category,
            FlareParticleDyeRecipe::new
    );

    @Override
    public @NotNull MapCodec<FlareParticleDyeRecipe> codec() {
        return CODEC;
    }

    @Override
    public @NotNull StreamCodec<RegistryFriendlyByteBuf, FlareParticleDyeRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
