package sircow.preservedinferno.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.recipe.LoomRecipe;

import java.util.List;

@JeiPlugin
public class PreservedJeiPlugin implements IModPlugin {
    public static final IRecipeType<LoomRecipe> LOOM = IRecipeType.create(Constants.id("loom"), LoomRecipe.class);

    @Override
    public @NonNull Identifier getPluginUid() {
        return Constants.id("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new LoomRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(@NonNull IRecipeRegistration registration) {
        List<LoomRecipe> recipes = PreservedRecipeCache.getLoomRecipes();
        registration.addRecipes(LOOM, recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(LOOM, Blocks.LOOM);
    }
}
