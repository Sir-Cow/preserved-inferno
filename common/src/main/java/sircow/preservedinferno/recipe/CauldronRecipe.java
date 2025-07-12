package sircow.preservedinferno.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public record CauldronRecipe(Ingredient inputItem, ItemStack output) implements Recipe<CauldronRecipeInput> {
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(this.inputItem);
        return list;
    }

    @Override
    public boolean matches(@NotNull CauldronRecipeInput input, @NotNull Level level) {
        if (level.isClientSide()) {
            return false;
        }

        return inputItem.test(input.getItem(0));
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CauldronRecipeInput input, HolderLookup.@NotNull Provider provider) {
        return output.copy();
    }

    @Override
    public @NotNull RecipeSerializer<? extends Recipe<CauldronRecipeInput>> getSerializer() {
        return ModRecipes.CAULDRON_SERIALIZER;
    }

    @Override
    public @NotNull RecipeType<? extends Recipe<CauldronRecipeInput>> getType() {
        return ModRecipes.CAULDRON_TYPE;
    }

    @Override
    public @NotNull PlacementInfo placementInfo() {
        return PlacementInfo.create(inputItem);
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    public static class Serializer implements RecipeSerializer<CauldronRecipe> {
        public static final MapCodec<CauldronRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(CauldronRecipe::inputItem),
                ItemStack.CODEC.fieldOf("result").forGetter(CauldronRecipe::output)
        ).apply(inst, CauldronRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, CauldronRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, CauldronRecipe::inputItem,
                        ItemStack.STREAM_CODEC, CauldronRecipe::output,
                        CauldronRecipe::new);

        @Override
        public @NotNull MapCodec<CauldronRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, CauldronRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
