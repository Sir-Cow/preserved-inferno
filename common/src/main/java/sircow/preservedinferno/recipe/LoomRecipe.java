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

import java.util.List;

public record LoomRecipe(Ingredient inputItem, Ingredient shearsItem, ItemStack output) implements Recipe<LoomRecipeInput> {
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(this.inputItem);
        list.add(this.inputItem);
        list.add(this.shearsItem);
        return list;
    }

    @Override
    public boolean matches(@NotNull LoomRecipeInput input, @NotNull Level level) {
        if (level.isClientSide()) {
            return false;
        }

        boolean inputMatches = inputItem.test(input.getItem(0)) && inputItem.test(input.getItem(1));
        boolean shearsMatches = shearsItem.test(input.getItem(2));

        return inputMatches && shearsMatches;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull LoomRecipeInput input, HolderLookup.@NotNull Provider provider) {
        return output.copy();
    }

    @Override
    public @NotNull PlacementInfo placementInfo() {
        return PlacementInfo.create(List.of(inputItem, shearsItem));
    }

    @Override
    public @NotNull RecipeSerializer<? extends Recipe<LoomRecipeInput>> getSerializer() {
        return ModRecipes.LOOM_SERIALIZER;
    }

    @Override
    public @NotNull RecipeType<? extends Recipe<LoomRecipeInput>> getType() {
        return ModRecipes.LOOM_TYPE;
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    public static class Serializer implements RecipeSerializer<LoomRecipe> {
        public static final MapCodec<LoomRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC.fieldOf("input").forGetter(LoomRecipe::inputItem),
                Ingredient.CODEC.fieldOf("shears").forGetter(LoomRecipe::shearsItem),
                ItemStack.CODEC.fieldOf("result").forGetter(LoomRecipe::output)
        ).apply(inst, LoomRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, LoomRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, LoomRecipe::inputItem,
                        Ingredient.CONTENTS_STREAM_CODEC, LoomRecipe::shearsItem,
                        ItemStack.STREAM_CODEC, LoomRecipe::output,
                        LoomRecipe::new);

        @Override
        public @NotNull MapCodec<LoomRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, LoomRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
