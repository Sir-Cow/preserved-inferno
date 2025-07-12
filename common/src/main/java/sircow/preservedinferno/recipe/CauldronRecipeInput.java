package sircow.preservedinferno.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

public record CauldronRecipeInput(ItemStack input) implements RecipeInput {
    @Override
    public @NotNull ItemStack getItem(int slot) {
        return input;
    }

    @Override
    public int size() {
        return 1;
    }
}
