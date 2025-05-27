package sircow.preservedinferno.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sircow.preservedinferno.components.ModComponents;

import java.util.ArrayList;
import java.util.List;

public class FlareParticleDyeRecipe extends CustomRecipe {
    public FlareParticleDyeRecipe(CraftingBookCategory craftingBookCategory) {
        super(craftingBookCategory);
    }

    public boolean matches(CraftingInput craftingInput, @NotNull Level level) {
        if (craftingInput.ingredientCount() < 2) {
            return false;
        }
        else {
            boolean bl = false;
            boolean bl2 = false;

            for (int i = 0; i < craftingInput.size(); i++) {
                ItemStack itemStack = craftingInput.getItem(i);
                if (!itemStack.isEmpty()) {
                    if (itemStack.is(ModItems.FLARE_GUN)) {
                        if (bl) {
                            return false;
                        }

                        bl = true;
                    }
                    else {
                        if (!(itemStack.getItem() instanceof DyeItem)) {
                            return false;
                        }

                        bl2 = true;
                    }
                }
            }

            return bl2 && bl;
        }
    }

    public @NotNull ItemStack assemble(CraftingInput craftingInput, HolderLookup.@NotNull Provider provider) {
        List<DyeItem> dyes = new ArrayList<>();
        ItemStack flareGunStack = ItemStack.EMPTY;

        for (int i = 0; i < craftingInput.size(); i++) {
            ItemStack itemStack = craftingInput.getItem(i);
            if (!itemStack.isEmpty()) {
                if (itemStack.is(ModItems.FLARE_GUN)) {
                    if (!flareGunStack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    flareGunStack = itemStack.copy();
                } else {
                    if (!(itemStack.getItem() instanceof DyeItem dyeItem)) {
                        return ItemStack.EMPTY;
                    }
                    dyes.add(dyeItem);
                }
            }
        }

        if (flareGunStack.isEmpty() || dyes.isEmpty()) {
            return ItemStack.EMPTY;
        }

        int r = 0;
        int g = 0;
        int b = 0;
        int maxColorComponent = 0;
        int dyeCount = 0;

        for (DyeItem dyeItem : dyes) {
            int dyeColorInt = dyeItem.getDyeColor().getTextureDiffuseColor();
            int currentR = ARGB.red(dyeColorInt);
            int currentG = ARGB.green(dyeColorInt);
            int currentB = ARGB.blue(dyeColorInt);

            maxColorComponent += Math.max(currentR, Math.max(currentG, currentB));
            r += currentR;
            g += currentG;
            b += currentB;
            dyeCount++;
        }

        int avgR = r / dyeCount;
        int avgG = g / dyeCount;
        int avgB = b / dyeCount;

        float f = (float) maxColorComponent / (float) dyeCount;
        float f1 = (float) Math.max(avgR, Math.max(avgG, avgB));

        if (f1 > 0) {
            avgR = (int) ((float) avgR * f / f1);
            avgG = (int) ((float) avgG * f / f1);
            avgB = (int) ((float) avgB * f / f1);
        }

        int finalColorInt = ARGB.color(0, avgR, avgG, avgB); // ARGB, with A=0 (opaque for RGB)

        String dyeValueString = String.format("#%06X", finalColorInt & 0xFFFFFF);

        flareGunStack.set(ModComponents.FLARE_PARTICLE_COMPONENT, dyeValueString);

        return flareGunStack;
    }

    @Override
    public @NotNull RecipeSerializer<ArmorDyeRecipe> getSerializer() {
        return RecipeSerializer.ARMOR_DYE;
    }
}
