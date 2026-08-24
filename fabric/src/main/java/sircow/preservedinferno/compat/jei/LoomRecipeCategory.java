package sircow.preservedinferno.compat.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.recipe.LoomRecipe;

public class LoomRecipeCategory extends AbstractRecipeCategory<LoomRecipe> {
    public LoomRecipeCategory(IGuiHelper guiHelper) {
        super(PreservedJeiPlugin.LOOM, Component.translatable("jei.pinferno.loom.title"), guiHelper.createDrawableItemStack(new ItemStack(Items.LOOM)), 150, 60);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, LoomRecipe recipe, @NonNull IFocusGroup focuses) {
        builder.addInputSlot(20, 22).setStandardSlotBackground().add(recipe.inputItem());
        builder.addInputSlot(45, 22).setStandardSlotBackground().add(recipe.inputItem());
        builder.addInputSlot(70, 22).setStandardSlotBackground().add(recipe.shearsItem());
        builder.addOutputSlot(120, 22).setOutputSlotBackground().add(recipe.output().create());
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, @NonNull LoomRecipe recipe, @NonNull IFocusGroup focuses) {
        builder.addRecipeArrow().setPosition(90, 22);
    }
}
