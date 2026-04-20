package sircow.preservedinferno.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record LoomRecipe(Ingredient inputItem, Ingredient shearsItem, ItemStackTemplate output) implements Recipe<LoomRecipeInput> {
    public static final MapCodec<LoomRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.fieldOf("input").forGetter(LoomRecipe::inputItem),
            Ingredient.CODEC.fieldOf("shears").forGetter(LoomRecipe::shearsItem),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(LoomRecipe::output)
    ).apply(inst, LoomRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, LoomRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC, LoomRecipe::inputItem,
                    Ingredient.CONTENTS_STREAM_CODEC, LoomRecipe::shearsItem,
                    ItemStackTemplate.STREAM_CODEC, LoomRecipe::output,
                    LoomRecipe::new
            );

    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(this.inputItem);
        list.add(this.inputItem);
        list.add(this.shearsItem);
        return list;
    }

    @Override
    public boolean matches(@NotNull LoomRecipeInput input, @NotNull Level level) {
        boolean inputMatches = inputItem.test(input.getItem(0)) && inputItem.test(input.getItem(1));
        boolean shearsMatches = shearsItem.test(input.getItem(2));

        return inputMatches && shearsMatches;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull LoomRecipeInput input) {
        return output.create();
    }

    @Override
    public @NotNull PlacementInfo placementInfo() {
        return PlacementInfo.create(List.of(inputItem, inputItem, shearsItem));
    }

    @Override
    public @NotNull RecipeSerializer<LoomRecipe> getSerializer() {
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

    @Override
    public boolean showNotification() {
        return true;
    }

    @Override
    public @NotNull String group() {
        return "";
    }
}
