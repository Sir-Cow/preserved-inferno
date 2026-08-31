package sircow.preservedinferno.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.recipe.vanilla.IJeiGrindstoneRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRuntimeRegistration;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.component.ModComponents;
import sircow.preservedinferno.item.ModItems;
import sircow.preservedinferno.recipe.CauldronRecipe;
import sircow.preservedinferno.recipe.LoomRecipe;
import sircow.preservedinferno.tag.ModTags;
import sircow.preservedinferno.recipe.PreservedRecipeCache;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class PreservedJeiPlugin implements IModPlugin {
    public static final IRecipeType<LoomRecipe> LOOM = IRecipeType.create(Constants.id("loom"), LoomRecipe.class);
    public static final IRecipeType<CauldronRecipe> CAULDRON = IRecipeType.create(Constants.id("cauldron"), CauldronRecipe.class);

    @Override
    public @NonNull Identifier getPluginUid() {
        return Constants.id("jei_plugin");
    }

    @Override
    public void registerRuntime(@NonNull IRuntimeRegistration registration) {
        IRecipeManager recipeManager = registration.getRecipeManager();

        List<IJeiAnvilRecipe> anvilToRemove = recipeManager.createRecipeLookup(RecipeTypes.ANVIL)
                .includeHidden()
                .get()
                .filter(recipe -> containsBlacklistedItem(recipe.getLeftInputs()) || containsBlacklistedItem(recipe.getRightInputs()) || isVanillaRepair(recipe.getLeftInputs(), recipe.getRightInputs()))
                .collect(Collectors.toList());
        recipeManager.hideRecipes(RecipeTypes.ANVIL, anvilToRemove);

        List<IJeiGrindstoneRecipe> grindstoneToRemove = recipeManager.createRecipeLookup(RecipeTypes.GRINDSTONE)
                .includeHidden()
                .get()
                .filter(recipe -> containsBlacklistedItem(recipe.getTopInputs()) || containsBlacklistedItem(recipe.getBottomInputs()))
                .collect(Collectors.toList());
        recipeManager.hideRecipes(RecipeTypes.GRINDSTONE, grindstoneToRemove);
    }

    private boolean containsBlacklistedItem(List<ItemStack> inputs) {
        return inputs.stream().anyMatch(stack -> stack.is(ModItems.SCULK_INFUSION.get()) || stack.is(ModItems.DREAMCATCHER.get()));
    }

    private boolean isVanillaRepair(List<ItemStack> leftInputs, List<ItemStack> rightInputs) {
        for (ItemStack left : leftInputs) {
            for (ItemStack right : rightInputs) {
                if (right.is(ModItems.REPAIR_KIT.get()) || right.is(ModItems.FORGE_DUST.get()) || right.is(ModItems.AQUATIC_FIBER.get())) continue;
                if (left.isDamageableItem() && left.isValidRepairItem(right)) return true;
            }
        }
        return false;
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

        IVanillaRecipeFactory factory = registration.getJeiHelpers().getVanillaRecipeFactory();
        List<IJeiAnvilRecipe> customAnvilRecipes = new ArrayList<>();
        customAnvilRecipes.addAll(createRepairKitRecipes(factory));
        customAnvilRecipes.addAll(createForgeDustRecipes(factory));
        customAnvilRecipes.addAll(createAquaticFiberRecipes(factory));
        registration.addRecipes(RecipeTypes.ANVIL, customAnvilRecipes);
    }

    private List<IJeiAnvilRecipe> createRepairKitRecipes(IVanillaRecipeFactory factory) {
        List<IJeiAnvilRecipe> recipes = new ArrayList<>();

        List<Item> planksTier = List.of(
                Items.WOODEN_SWORD, Items.WOODEN_PICKAXE, Items.WOODEN_AXE, Items.WOODEN_SHOVEL, Items.WOODEN_HOE, Items.WOODEN_SPEAR,
                ModItems.WOODEN_MULTITOOL.get(),
                Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS,
                Items.BOW, Items.CROSSBOW, Items.CARROT_ON_A_STICK, Items.FLINT_AND_STEEL, Items.BRUSH,
                Items.WARPED_FUNGUS_ON_A_STICK
        );
        for (Item item : planksTier) {
            addRepairKitRecipe(recipes, factory, item, 0.5, "planks");
        }

        List<Item> cobblestoneTier = List.of(
                Items.STONE_SWORD, Items.STONE_PICKAXE, Items.STONE_AXE, Items.STONE_SHOVEL, Items.STONE_HOE, Items.STONE_SPEAR,
                ModItems.STONE_MULTITOOL.get()
        );
        for (Item item : cobblestoneTier) {
            addRepairKitRecipe(recipes, factory, item, 0.3334, "cobblestone");
        }

        List<Item> quartzTier = List.of(
                ModItems.QUARTZITE_SWORD.get(), ModItems.QUARTZITE_PICKAXE.get(), ModItems.QUARTZITE_AXE.get(), ModItems.QUARTZITE_SCYTHE.get(), ModItems.QUARTZITE_SHOVEL.get(),
                ModItems.QUARTZITE_MULTITOOL.get()
        );
        for (Item item : quartzTier) {
            addRepairKitRecipe(recipes, factory, item, 0.25, "quartz");
        }

        List<Item> copperTier = List.of(
                Items.COPPER_SWORD, Items.COPPER_PICKAXE, Items.COPPER_AXE, Items.COPPER_SHOVEL, Items.COPPER_HOE, Items.COPPER_SPEAR,
                ModItems.COPPER_MULTITOOL.get(), ModItems.COPPER_TRIDENT.get(), ModItems.COPPER_SHIELD.get(), ModItems.FLARE_GUN.get(),
                ModItems.COPPER_FISHING_HOOK.get(), ModItems.COPPER_LACED_FISHING_LINE.get(), ModItems.COPPER_SINKER.get(),
                ModItems.PRISMARINE_FISHING_HOOK.get(), ModItems.PRISMARINE_LACED_FISHING_LINE.get(), ModItems.PRISMARINE_SINKER.get(),
                Items.SHEARS, Items.WOLF_ARMOR
                );
        for (Item item : copperTier) {
            addRepairKitRecipe(recipes, factory, item, 0.1667, "copper");
        }

        List<Item> goldTier = List.of(
                Items.GOLDEN_SWORD, Items.GOLDEN_PICKAXE, Items.GOLDEN_AXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_HOE, Items.GOLDEN_SPEAR,
                Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS,
                ModItems.GOLDEN_MULTITOOL.get(), ModItems.GOLDEN_SHIELD.get(),
                ModItems.GOLDEN_FISHING_HOOK.get(), ModItems.GOLDEN_LACED_FISHING_LINE.get(), ModItems.GOLDEN_SINKER.get()
        );
        for (Item item : goldTier) {
            addRepairKitRecipe(recipes, factory, item, 0.125, "gold");
        }

        List<Item> ironTier = List.of(
                Items.IRON_SWORD, Items.IRON_PICKAXE, Items.IRON_AXE, Items.IRON_SHOVEL, Items.IRON_HOE, Items.IRON_SPEAR,
                Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS,
                Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS,
                Items.TRIDENT, Items.MACE, Items.ELYTRA,
                ModItems.IRON_MULTITOOL.get(), ModItems.IRON_SHIELD.get(),
                ModItems.IRON_FISHING_HOOK.get(), ModItems.IRON_LACED_FISHING_LINE.get(), ModItems.IRON_SINKER.get()
        );
        for (Item item : ironTier) {
            addRepairKitRecipe(recipes, factory, item, 0.0834, "iron");
        }

        List<Item> netherAlloyTier = List.of(
                ModItems.NETHER_ALLOY_SWORD.get(), ModItems.NETHER_ALLOY_PICKAXE.get(), ModItems.NETHER_ALLOY_AXE.get(), ModItems.NETHER_ALLOY_SCYTHE.get(), ModItems.NETHER_ALLOY_SHOVEL.get(),
                ModItems.NETHER_ALLOY_MULTITOOL.get()
        );
        for (Item item : netherAlloyTier) {
            addRepairKitRecipe(recipes, factory, item, 0.0625, "nether_alloy");
        }

        List<Item> diamondTier = List.of(
                Items.DIAMOND_SWORD, Items.DIAMOND_PICKAXE, Items.DIAMOND_AXE, Items.DIAMOND_SHOVEL, Items.DIAMOND_HOE, Items.DIAMOND_SPEAR,
                Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS,
                ModItems.DIAMOND_MULTITOOL.get(), ModItems.DIAMOND_SHIELD.get(),
                ModItems.DIAMOND_FISHING_HOOK.get(), ModItems.DIAMOND_LACED_FISHING_LINE.get(), ModItems.DIAMOND_SINKER.get()
        );
        for (Item item : diamondTier) {
            addRepairKitRecipe(recipes, factory, item, 0.015625, "diamond");
        }

        List<Item> netheriteTier = List.of(
                Items.NETHERITE_SWORD, Items.NETHERITE_PICKAXE, Items.NETHERITE_AXE, Items.NETHERITE_SHOVEL, Items.NETHERITE_HOE, Items.NETHERITE_SPEAR,
                Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS,
                ModItems.NETHERITE_MULTITOOL.get(), ModItems.NETHERITE_SHIELD.get(),
                ModItems.NETHERITE_FISHING_HOOK.get(), ModItems.NETHERITE_LACED_FISHING_LINE.get(), ModItems.NETHERITE_SINKER.get()
        );
        for (Item item : netheriteTier) {
            addRepairKitRecipe(recipes, factory, item, 0.0078125, "netherite");
        }

        return recipes;
    }

    private void addRepairKitRecipe(List<IJeiAnvilRecipe> recipes, IVanillaRecipeFactory factory, Item item, double fraction, String group) {
        ItemStack damaged = new ItemStack(item);
        int maxDamage = damaged.getMaxDamage();
        if (maxDamage <= 0) return;
        damaged.setDamageValue(maxDamage);

        int repairAmount = (int) Math.ceil(maxDamage * fraction);
        ItemStack repaired = new ItemStack(item);
        repaired.setDamageValue(Math.max(0, maxDamage - repairAmount));

        ItemStack repairKit = new ItemStack(ModItems.REPAIR_KIT.get());

        recipes.add(factory.createAnvilRecipe(
                damaged,
                List.of(repairKit),
                List.of(repaired),
                Constants.id("jei_repair_kit_" + group + "/" + BuiltInRegistries.ITEM.getKey(item).getPath())
        ));
    }

    private List<IJeiAnvilRecipe> createForgeDustRecipes(IVanillaRecipeFactory factory) {
        List<IJeiAnvilRecipe> recipes = new ArrayList<>();

        List<Item> copperItems = List.of(
                Items.COPPER_SWORD, Items.COPPER_PICKAXE, Items.COPPER_AXE, Items.COPPER_SHOVEL, Items.COPPER_HOE, Items.COPPER_SPEAR,
                ModItems.COPPER_MULTITOOL.get(), ModItems.COPPER_TRIDENT.get(), ModItems.COPPER_SHIELD.get(), ModItems.FLARE_GUN.get(),
                ModItems.COPPER_FISHING_HOOK.get(), ModItems.COPPER_LACED_FISHING_LINE.get(), ModItems.COPPER_SINKER.get(), Items.SHEARS
        );
        for (Item item : copperItems) {
            addForgeDustRecipe(recipes, factory, item, "copper", "Copper");
        }

        List<Item> goldItems = List.of(
                Items.GOLDEN_SWORD, Items.GOLDEN_PICKAXE, Items.GOLDEN_AXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_HOE, Items.GOLDEN_SPEAR,
                Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS,
                ModItems.GOLDEN_MULTITOOL.get(), ModItems.GOLDEN_SHIELD.get(),
                ModItems.GOLDEN_FISHING_HOOK.get(), ModItems.GOLDEN_LACED_FISHING_LINE.get(), ModItems.GOLDEN_SINKER.get()
        );
        for (Item item : goldItems) {
            addForgeDustRecipe(recipes, factory, item, "gold", "Gold");
        }

        List<Item> ironItems = List.of(
                Items.IRON_SWORD, Items.IRON_PICKAXE, Items.IRON_AXE, Items.IRON_SHOVEL, Items.IRON_HOE, Items.IRON_SPEAR,
                Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS,
                Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS,
                ModItems.IRON_MULTITOOL.get(), ModItems.IRON_SHIELD.get(),
                ModItems.IRON_FISHING_HOOK.get(), ModItems.IRON_LACED_FISHING_LINE.get(), ModItems.IRON_SINKER.get()
        );
        for (Item item : ironItems) {
            addForgeDustRecipe(recipes, factory, item, "iron", "Iron");
        }

        List<Item> diamondItems = List.of(
                Items.DIAMOND_SWORD, Items.DIAMOND_PICKAXE, Items.DIAMOND_AXE, Items.DIAMOND_SHOVEL, Items.DIAMOND_HOE, Items.DIAMOND_SPEAR,
                Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS,
                ModItems.DIAMOND_MULTITOOL.get(), ModItems.DIAMOND_SHIELD.get(),
                ModItems.DIAMOND_FISHING_HOOK.get(), ModItems.DIAMOND_LACED_FISHING_LINE.get(), ModItems.DIAMOND_SINKER.get()
        );
        for (Item item : diamondItems) {
            addForgeDustRecipe(recipes, factory, item, "diamond", "Diamond");
        }

        List<Item> netheriteItems = List.of(
                Items.NETHERITE_SWORD, Items.NETHERITE_PICKAXE, Items.NETHERITE_AXE, Items.NETHERITE_SHOVEL, Items.NETHERITE_HOE, Items.NETHERITE_SPEAR,
                Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS,
                ModItems.NETHERITE_MULTITOOL.get(), ModItems.NETHERITE_SHIELD.get(),
                ModItems.NETHERITE_FISHING_HOOK.get(), ModItems.NETHERITE_LACED_FISHING_LINE.get(), ModItems.NETHERITE_SINKER.get()
        );
        for (Item item : netheriteItems) {
            addForgeDustRecipe(recipes, factory, item, "netherite", "Netherite");
        }

        List<Item> quartziteItems = List.of(
                ModItems.QUARTZITE_SWORD.get(), ModItems.QUARTZITE_PICKAXE.get(), ModItems.QUARTZITE_AXE.get(), ModItems.QUARTZITE_SCYTHE.get(), ModItems.QUARTZITE_SHOVEL.get(),
                ModItems.QUARTZITE_MULTITOOL.get()
        );
        for (Item item : quartziteItems) {
            addForgeDustRecipe(recipes, factory, item, "quartzite", "Quartzite");
        }

        List<Item> netherAlloyItems = List.of(
                ModItems.NETHER_ALLOY_SWORD.get(), ModItems.NETHER_ALLOY_PICKAXE.get(), ModItems.NETHER_ALLOY_AXE.get(), ModItems.NETHER_ALLOY_SCYTHE.get(), ModItems.NETHER_ALLOY_SHOVEL.get(),
                ModItems.NETHER_ALLOY_MULTITOOL.get()
        );
        for (Item item : netherAlloyItems) {
            addForgeDustRecipe(recipes, factory, item, "nether_alloy", "Nether Alloy");
        }

        return recipes;
    }

    private void addForgeDustRecipe(List<IJeiAnvilRecipe> recipes, IVanillaRecipeFactory factory, Item item, String dustName, String materialName) {
        ItemStack damaged = new ItemStack(item);
        int maxDamage = damaged.getMaxDamage();
        if (maxDamage <= 0) return;
        damaged.setDamageValue(maxDamage);

        int repairAmount = (int) Math.ceil(maxDamage * 0.3334);
        ItemStack repaired = new ItemStack(item);
        repaired.setDamageValue(Math.max(0, maxDamage - repairAmount));

        ItemStack forgeDust = new ItemStack(ModItems.FORGE_DUST.get());
        forgeDust.set(ModComponents.FORGE_MATERIAL_COMPONENT, materialName);

        recipes.add(factory.createAnvilRecipe(
                damaged,
                List.of(forgeDust),
                List.of(repaired),
                Constants.id("jei_forge_dust_" + dustName + "/" + BuiltInRegistries.ITEM.getKey(item).getPath())
        ));
    }

    private List<IJeiAnvilRecipe> createAquaticFiberRecipes(IVanillaRecipeFactory factory) {
        List<IJeiAnvilRecipe> recipes = new ArrayList<>();

        for (var holder : BuiltInRegistries.ITEM.getTagOrEmpty(ModTags.ROD_UPGRADES)) {
            Item item = holder.value();
            ItemStack damaged = new ItemStack(item);
            int maxDamage = damaged.getMaxDamage();
            if (maxDamage <= 0) continue;
            damaged.setDamageValue(maxDamage);

            ItemStack repaired = new ItemStack(item);
            repaired.setDamageValue(Math.max(0, maxDamage - 200));

            ItemStack aquaticFiber = new ItemStack(ModItems.AQUATIC_FIBER.get());

            recipes.add(factory.createAnvilRecipe(
                    damaged,
                    List.of(aquaticFiber),
                    List.of(repaired),
                    Constants.id("jei_aquatic_fiber/" + BuiltInRegistries.ITEM.getKey(item).getPath())
            ));
        }

        return recipes;
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(LOOM, Blocks.LOOM);
        registration.addCraftingStation(CAULDRON, Blocks.CAULDRON);
    }
}
