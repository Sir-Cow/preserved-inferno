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
import sircow.preservedinferno.recipe.CauldronRecipe;
import sircow.preservedinferno.recipe.LoomRecipe;

import java.util.List;

@JeiPlugin
public class PreservedJeiPlugin implements IModPlugin {
    public static final IRecipeType<LoomRecipe> LOOM = IRecipeType.create(Constants.id("loom"), LoomRecipe.class);
    public static final IRecipeType<CauldronRecipe> CAULDRON = IRecipeType.create(Constants.id("cauldron"), CauldronRecipe.class);

    @Override
    public @NonNull Identifier getPluginUid() {
        return Constants.id("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new LoomRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new CauldronRecipeCategory(registration.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(@NonNull IRecipeRegistration registration) {
        List<LoomRecipe> loomRecipes = PreservedRecipeCache.getLoomRecipes();
        registration.addRecipes(LOOM, loomRecipes);

        List<CauldronRecipe> cauldronRecipes = PreservedRecipeCache.getCauldronRecipes();
        registration.addRecipes(CAULDRON, cauldronRecipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(LOOM, Blocks.LOOM);
        registration.addCraftingStation(CAULDRON, Blocks.CAULDRON);
    }
}
