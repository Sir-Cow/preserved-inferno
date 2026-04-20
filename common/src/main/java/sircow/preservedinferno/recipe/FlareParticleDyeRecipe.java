package sircow.preservedinferno.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.components.ModComponents;
import sircow.preservedinferno.item.ModItems;

import java.util.ArrayList;
import java.util.List;

public class FlareParticleDyeRecipe extends NormalCraftingRecipe {
    public static final MapCodec<FlareParticleDyeRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Recipe.CommonInfo.MAP_CODEC.forGetter(r -> r.commonInfo), CraftingRecipe.CraftingBookInfo.MAP_CODEC.forGetter(r -> r.bookInfo)).apply(instance, FlareParticleDyeRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FlareParticleDyeRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Recipe.CommonInfo.STREAM_CODEC,
                    r -> r.commonInfo,
                    CraftingRecipe.CraftingBookInfo.STREAM_CODEC,
                    r -> r.bookInfo,
                    FlareParticleDyeRecipe::new
            );

    public static final RecipeSerializer<FlareParticleDyeRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    public FlareParticleDyeRecipe(Recipe.CommonInfo commonInfo, CraftingRecipe.CraftingBookInfo bookInfo) {
        super(commonInfo, bookInfo);
    }

    @Override
    public boolean matches(CraftingInput input, @NonNull Level level) {
        if (input.ingredientCount() < 2) return false;

        boolean hasTarget = false;
        boolean hasDye = false;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.is(ModItems.FLARE_GUN)) {
                if (hasTarget) return false;
                hasTarget = true;
            }
            else {
                if (!(stack.getItem() instanceof DyeItem)) return false;
                hasDye = true;
            }
        }
        return hasTarget && hasDye;
    }

    @Override
    public @NotNull ItemStack assemble(CraftingInput input) {
        ItemStack targetStack = ItemStack.EMPTY;
        List<DyeColor> dyes = new ArrayList<>();

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.is(ModItems.FLARE_GUN)) {
                if (!targetStack.isEmpty()) return ItemStack.EMPTY;
                targetStack = stack.copy();
            }
            else {
                if (!(stack.getItem() instanceof DyeItem)) return ItemStack.EMPTY;
                DyeColor color = stack.get(DataComponents.DYE);
                if (color == null) return ItemStack.EMPTY;
                dyes.add(color);
            }
        }

        if (targetStack.isEmpty() || dyes.isEmpty()) return ItemStack.EMPTY;

        DyedItemColor current = targetStack.get(DataComponents.DYED_COLOR);
        DyedItemColor combined = DyedItemColor.applyDyes(current, dyes);
        ItemStack result = targetStack.copy();
        result.set(DataComponents.DYED_COLOR, combined);
        int rgb = combined.rgb();
        String dyeValueString = String.format("#%06X", rgb & 0xFFFFFF);
        result.set(ModComponents.FLARE_PARTICLE_COMPONENT, dyeValueString);

        return result;
    }

    @Override
    public @NotNull RecipeSerializer<FlareParticleDyeRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    protected @NonNull PlacementInfo createPlacementInfo() {
        return PlacementInfo.create(List.of(
                Ingredient.of(ModItems.FLARE_GUN),
                Ingredient.of(BuiltInRegistries.ITEM.getOrThrow(ItemTags.DYES))
        ));
    }
}
