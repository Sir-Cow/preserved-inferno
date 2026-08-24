package sircow.preservedinferno.compat.jei;

import mezz.jei.api.fabric.constants.FabricTypes;
import mezz.jei.api.fabric.ingredients.fluids.JeiFluidIngredient;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.fluid.CauldronFluid;
import sircow.preservedinferno.fluid.ModFluids;
import sircow.preservedinferno.recipe.CauldronRecipe;

import java.util.List;
import java.util.Random;

public class CauldronRecipeCategory extends AbstractRecipeCategory<CauldronRecipe> {
    private static final int FLUID_UNITS_PER_BUCKET = 8;
    private static final long MILLIBUCKETS_PER_FLUID_UNIT = 1000L / FLUID_UNITS_PER_BUCKET;
    private static final long DROPLETS_PER_MILLIBUCKET = FluidConstants.BUCKET / 1000L;

    public CauldronRecipeCategory(IGuiHelper guiHelper) {
        super(PreservedJeiPlugin.CAULDRON, Component.translatable("jei.pinferno.cauldron.title"), guiHelper.createDrawableItemStack(new ItemStack(Items.CAULDRON)), 150, 60);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CauldronRecipe recipe, @NonNull IFocusGroup focuses) {
        IRecipeSlotBuilder inputSlot = builder.addInputSlot(20, 22).setStandardSlotBackground();
        inputSlot.addItemStacks(getInputStacks(recipe.inputItem(), inputSlot.getContextMap()));

        long amount = recipe.fluidCost() * MILLIBUCKETS_PER_FLUID_UNIT * DROPLETS_PER_MILLIBUCKET;

        IRecipeSlotBuilder fuelSlot = builder.addInputSlot(45, 22).setStandardSlotBackground();
        fuelSlot.setFluidRenderer(1, false, 16, 16);
        fuelSlot.add(FabricTypes.FLUID_STACK, new JeiFluidIngredient(FluidVariant.of(getFluid(recipe.fluid())), amount));
        fuelSlot.addRichTooltipCallback((slotView, tooltip) -> tooltip.add(Component.translatable("jei.pinferno.cauldron.fuel_required", recipe.fluidCost())));

        builder.addOutputSlot(120, 22).setOutputSlotBackground().add(recipe.output().create());
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, @NonNull CauldronRecipe recipe, @NonNull IFocusGroup focuses) {
        builder.addRecipeArrow().setPosition(72, 22);
    }

    private static List<ItemStack> getInputStacks(Ingredient ingredient, ContextMap contextMap) {
        return ingredient.display()
                .resolveForStacks(contextMap)
                .stream()
                .map(stack -> {
                    if (isLeatherArmor(stack.getItem())) {
                        int rgb = new Random().nextInt(0xFFFFFF);
                        stack = stack.copy();
                        stack.set(DataComponents.DYED_COLOR, new DyedItemColor(rgb));
                    }
                    return stack;
                })
                .toList();
    }

    private static boolean isLeatherArmor(Item item) {
        return item == Items.LEATHER_HELMET || item == Items.LEATHER_CHESTPLATE || item == Items.LEATHER_LEGGINGS || item == Items.LEATHER_BOOTS;
    }

    private static Fluid getFluid(CauldronFluid fluid) {
        return switch (fluid) {
            case WATER -> Fluids.WATER;
            case LAVA -> Fluids.LAVA;
            case MILK -> ModFluids.MILK;
            case HONEY -> ModFluids.HONEY;
            case SNOW -> ModFluids.SNOW;
            default -> Fluids.EMPTY;
        };
    }
}
