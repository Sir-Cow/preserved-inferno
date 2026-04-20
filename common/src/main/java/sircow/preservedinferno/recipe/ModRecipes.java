package sircow.preservedinferno.recipe;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import sircow.preservedinferno.Constants;

public class ModRecipes {
    public static final RecipeSerializer<CauldronRecipe> CAULDRON_SERIALIZER = new RecipeSerializer<>(CauldronRecipe.CODEC, CauldronRecipe.STREAM_CODEC);
    public static final RecipeSerializer<FlareParticleDyeRecipe> FLARE_GUN_SERIALIZER = new RecipeSerializer<>(FlareParticleDyeRecipe.CODEC, FlareParticleDyeRecipe.STREAM_CODEC);
    public static final RecipeSerializer<LoomRecipe> LOOM_SERIALIZER = new RecipeSerializer<>(LoomRecipe.CODEC, LoomRecipe.STREAM_CODEC);

    public static final RecipeType<CauldronRecipe> CAULDRON_TYPE = Registry.register(
            BuiltInRegistries.RECIPE_TYPE, Constants.id("cauldron"), new RecipeType<CauldronRecipe>() {
                @Override
                public String toString() {
                    return "cauldron";
                }
            });
    public static final RecipeType<LoomRecipe> LOOM_TYPE = Registry.register(
            BuiltInRegistries.RECIPE_TYPE, Constants.id("loom"), new RecipeType<LoomRecipe>() {
                @Override
                public String toString() {
                    return "loom";
                }
            });

    public static void registerModRecipes() {
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Constants.id("cauldron"), CAULDRON_SERIALIZER);
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Constants.id("flare_gun"), FLARE_GUN_SERIALIZER);
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Constants.id("loom"), LOOM_SERIALIZER);
    }
}
