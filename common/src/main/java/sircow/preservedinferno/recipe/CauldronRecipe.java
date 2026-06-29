package sircow.preservedinferno.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.fluid.CauldronFluid;

public record CauldronRecipe(Ingredient inputItem, ItemStackTemplate output, CauldronFluid fluid, int fluidCost) implements Recipe<CauldronRecipeInput> {
    public static final Codec<CauldronFluid> FLUID_CODEC = Codec.STRING.xmap(s -> CauldronFluid.valueOf(s.toUpperCase()), fluid -> fluid.name().toLowerCase());
    public static final StreamCodec<RegistryFriendlyByteBuf, CauldronFluid> FLUID_STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public @NonNull CauldronFluid decode(RegistryFriendlyByteBuf buf) {
                    int ordinal = buf.readVarInt();
                    CauldronFluid[] values = CauldronFluid.values();

                    if (ordinal < 0 || ordinal >= values.length) {
                        throw new IllegalArgumentException("Invalid CauldronFluid ordinal: " + ordinal);
                    }
                    return values[ordinal];
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buf, CauldronFluid fluid) {
                    buf.writeVarInt(fluid.ordinal());
                }
            };

    public static final MapCodec<CauldronRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(CauldronRecipe::inputItem),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(CauldronRecipe::output),
            FLUID_CODEC.fieldOf("fluid").forGetter(CauldronRecipe::fluid),
            Codec.INT.optionalFieldOf("fluid_cost", 1).forGetter(CauldronRecipe::fluidCost)
    ).apply(inst, CauldronRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CauldronRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC, CauldronRecipe::inputItem,
                    ItemStackTemplate.STREAM_CODEC, CauldronRecipe::output,
                    FLUID_STREAM_CODEC, CauldronRecipe::fluid,
                    ByteBufCodecs.VAR_INT, CauldronRecipe::fluidCost,
                    CauldronRecipe::new
            );

    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(this.inputItem);
        return list;
    }

    @Override
    public boolean matches(@NotNull CauldronRecipeInput input, @NotNull Level level) {
        return inputItem.test(input.getItem(0)) && fluid == input.fluid();
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CauldronRecipeInput input) {
        return output.create();
    }

    @Override
    public @NotNull RecipeSerializer<CauldronRecipe> getSerializer() {
        return ModRecipes.CAULDRON_SERIALIZER;
    }

    @Override
    public @NotNull RecipeType<? extends Recipe<CauldronRecipeInput>> getType() {
        return ModRecipes.CAULDRON_TYPE;
    }

    @Override
    public @NotNull PlacementInfo placementInfo() {
        return PlacementInfo.create(inputItem);
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    public boolean showNotification() {
        return true;
    }

    @Override
    public @NotNull String group() {
        return "";
    }
}
