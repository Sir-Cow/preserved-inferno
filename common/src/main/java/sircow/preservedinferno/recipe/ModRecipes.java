package sircow.preservedinferno.recipe;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.item.FlareParticleDyeRecipe;
import sircow.preservedinferno.other.FlareParticleDyeRecipeSerializer;

public class ModRecipes {
    public static final RecipeSerializer<FlareParticleDyeRecipe> FLARE_GUN_SERIALIZER = Registry.register(
            BuiltInRegistries.RECIPE_SERIALIZER, Constants.id("flare_gun"),
            new FlareParticleDyeRecipeSerializer());
    public static final RecipeType<FlareParticleDyeRecipe> FLARE_GUN_TYPE = Registry.register(
            BuiltInRegistries.RECIPE_TYPE, Constants.id("flare_gun"), new RecipeType<FlareParticleDyeRecipe>() {
                @Override
                public String toString() {
                    return "flare_gun";
                }
            });

    public static void registerModRecipes() {
        Constants.LOG.info("Registering Custom Recipes for " + Constants.MOD_ID);
    }
}
