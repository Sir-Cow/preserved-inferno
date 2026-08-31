package sircow.preservedinferno.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.component.ModComponents;
import sircow.preservedinferno.item.ModItems;
import sircow.preservedinferno.tag.ModTags;

import java.util.List;

public class ForgeDustRecipe extends NormalCraftingRecipe {
    public static final MapCodec<ForgeDustRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(
            i -> i.group(
                            Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
                            CraftingRecipe.CraftingBookInfo.MAP_CODEC.forGetter(o -> o.bookInfo),
                            Ingredient.CODEC.fieldOf("material").forGetter(o -> o.material),
                            ItemStackTemplate.CODEC.fieldOf("result").forGetter(o -> o.result)
                    )
                    .apply(i, ForgeDustRecipe::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ForgeDustRecipe> STREAM_CODEC = StreamCodec.composite(
            Recipe.CommonInfo.STREAM_CODEC,
            o -> o.commonInfo,
            CraftingRecipe.CraftingBookInfo.STREAM_CODEC,
            o -> o.bookInfo,
            Ingredient.CONTENTS_STREAM_CODEC,
            o -> o.material,
            ItemStackTemplate.STREAM_CODEC,
            o -> o.result,
            ForgeDustRecipe::new
    );
    public static final RecipeSerializer<ForgeDustRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
    private final Ingredient material;
    private final ItemStackTemplate result;

    public ForgeDustRecipe(
            final Recipe.CommonInfo commonInfo,
            final CraftingRecipe.CraftingBookInfo bookInfo,
            final Ingredient material,
            final ItemStackTemplate result
    ) {
        super(commonInfo, bookInfo);
        this.material = material;
        this.result = result;
    }

    @Override
    public boolean matches(CraftingInput input, @NonNull Level level) {
        boolean hasLapis = false;
        boolean hasAmethyst = false;
        boolean hasGlowstone = false;
        boolean hasGunpowder = false;
        ItemStack materialStack = ItemStack.EMPTY;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.is(Items.LAPIS_LAZULI)) {
                if (hasLapis) return false;
                hasLapis = true;
            } else if (stack.is(Items.AMETHYST_SHARD)) {
                if (hasAmethyst) return false;
                hasAmethyst = true;
            } else if (stack.is(Items.GLOWSTONE_DUST)) {
                if (hasGlowstone) return false;
                hasGlowstone = true;
            } else if (stack.is(Items.GUNPOWDER)) {
                if (hasGunpowder) return false;
                hasGunpowder = true;
            } else if (material.test(stack)) {
                if (!materialStack.isEmpty()) return false;
                materialStack = stack;
            } else {
                return false;
            }
        }
        return hasLapis && hasAmethyst && hasGlowstone && hasGunpowder && !materialStack.isEmpty();
    }

    @Override
    public @NonNull ItemStack assemble(CraftingInput input) {
        ItemStack materialStack = ItemStack.EMPTY;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty() && material.test(stack)) {
                materialStack = stack;
                break;
            }
        }

        if (materialStack.isEmpty()) return ItemStack.EMPTY;

        ItemStack resultStack = this.result.create();
        resultStack.set(ModComponents.FORGE_MATERIAL_COMPONENT, resolveMaterial(materialStack));
        return resultStack;
    }

    @Override
    public @NonNull RecipeSerializer<? extends NormalCraftingRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    protected @NonNull PlacementInfo createPlacementInfo() {
        return PlacementInfo.create(List.of(
                Ingredient.of(Items.LAPIS_LAZULI),
                Ingredient.of(Items.AMETHYST_SHARD),
                Ingredient.of(Items.GLOWSTONE_DUST),
                Ingredient.of(Items.GUNPOWDER),
                this.material
        ));
    }

    @Override
    public @NonNull List<RecipeDisplay> display() {
        SlotDisplay lapis = new SlotDisplay.ItemSlotDisplay(Items.LAPIS_LAZULI);
        SlotDisplay amethyst = new SlotDisplay.ItemSlotDisplay(Items.AMETHYST_SHARD);
        SlotDisplay glowstone = new SlotDisplay.ItemSlotDisplay(Items.GLOWSTONE_DUST);
        SlotDisplay gunpowder = new SlotDisplay.ItemSlotDisplay(Items.GUNPOWDER);

        SlotDisplay materialDisplay = this.material.display();

        return List.of(
                new ShapelessCraftingRecipeDisplay(
                        List.of(lapis, amethyst, glowstone, gunpowder, materialDisplay),
                        new SlotDisplay.ItemStackSlotDisplay(this.result),
                        new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)
                )
        );
    }

    public static String resolveMaterial(ItemStack stack) {
        if (stack.is(ItemTags.COPPER_TOOL_MATERIALS)) return "Copper";
        if (stack.is(ItemTags.GOLD_TOOL_MATERIALS)) return "Gold";
        if (stack.is(ItemTags.IRON_TOOL_MATERIALS)) return "Iron";
        if (stack.is(ItemTags.DIAMOND_TOOL_MATERIALS)) return "Diamond";
        if (stack.is(ModTags.REPAIRS_NETHERITE_TOOL)) return "Netherite";
        if (stack.is(ModTags.QUARTZITE_TOOL_MATERIALS)) return "Quartzite";
        if (stack.is(ModItems.NETHER_ALLOY_PLATE.get())) return "Nether Alloy";
        return "None";
    }
}
