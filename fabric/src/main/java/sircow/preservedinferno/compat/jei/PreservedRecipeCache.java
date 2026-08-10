package sircow.preservedinferno.compat.jei;

import sircow.preservedinferno.recipe.LoomRecipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PreservedRecipeCache {
    private static final List<LoomRecipe> loomRecipes = new ArrayList<>();

    private PreservedRecipeCache() {}

    public static void setLoomRecipes(List<LoomRecipe> recipes) {
        loomRecipes.clear();
        loomRecipes.addAll(recipes);
    }

    public static List<LoomRecipe> getLoomRecipes() {
        return Collections.unmodifiableList(loomRecipes);
    }
}