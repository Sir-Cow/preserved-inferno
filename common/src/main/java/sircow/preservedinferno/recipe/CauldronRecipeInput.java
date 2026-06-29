package sircow.preservedinferno.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;
import sircow.preservedinferno.fluid.CauldronFluid;

public record CauldronRecipeInput(ItemStack input, CauldronFluid fluid, int fluidCost) implements RecipeInput {
    @Override
    public @NotNull ItemStack getItem(int slot) {
        return input;
    }

    @Override
    public int size() {
        return 1;
    }
}
