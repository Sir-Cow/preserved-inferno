package sircow.preservedinferno.other;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import sircow.preservedinferno.Constants;

public class ModTags {
    public static final TagKey<Item> ARMOR_TRIM_TEMPLATES = TagKey.create(Registries.ITEM, Constants.id("armor_trim_templates"));
    public static final TagKey<Item> BANNERS = TagKey.create(Registries.ITEM, Constants.id("banners"));
    public static final TagKey<Item> CARPETS = TagKey.create(Registries.ITEM, Constants.id("carpets"));
    public static final TagKey<Item> CLOTH = TagKey.create(Registries.ITEM, Constants.id("cloth"));
    public static final TagKey<Item> OVERWORLD_WOODEN_SLABS = TagKey.create(Registries.ITEM, Constants.id("overworld_wooden_slabs"));
    public static final TagKey<Item> OVERWORLD_PLANKS = TagKey.create(Registries.ITEM, Constants.id("overworld_planks"));
    public static final TagKey<Item> OVERWORLD_WOODEN_PRESSURE_PLATES = TagKey.create(Registries.ITEM, Constants.id("overworld_wooden_pressure_plates"));
    public static final TagKey<Item> RAILS_ITEM = TagKey.create(Registries.ITEM, Constants.id("rails"));
    public static final TagKey<Item> REINFORCED_DOORS_ITEM = TagKey.create(Registries.ITEM, Constants.id("reinforced_doors"));
    public static final TagKey<Item> SHIELDS = TagKey.create(Registries.ITEM, Constants.id("shields"));
    public static final TagKey<Item> MULTITOOLS = TagKey.create(Registries.ITEM, Constants.id("multitools"));
    public static final TagKey<Item> NETHER_ALLOY_TOOL_MATERIALS = TagKey.create(Registries.ITEM, Constants.id("nether_alloy_materials"));
    public static final TagKey<Item> QUARTZITE_TOOL_MATERIALS = TagKey.create(Registries.ITEM, Constants.id("quartzite_tool_materials"));
    public static final TagKey<Item> HOOKS = TagKey.create(Registries.ITEM, Constants.id("hooks"));
    public static final TagKey<Item> LINES = TagKey.create(Registries.ITEM, Constants.id("lines"));
    public static final TagKey<Item> SINKERS = TagKey.create(Registries.ITEM, Constants.id("sinkers"));
    public static final TagKey<Item> ROD_UPGRADES = TagKey.create(Registries.ITEM, Constants.id("rod_upgrades"));
    public static final TagKey<Item> FISHING_LOOT_FISH = TagKey.create(Registries.ITEM, Constants.id("fishing_loot/fish"));
    public static final TagKey<Item> FISHING_LOOT_JUNK = TagKey.create(Registries.ITEM, Constants.id("fishing_loot/junk"));
    public static final TagKey<Item> FISHING_LOOT_TREASURE = TagKey.create(Registries.ITEM, Constants.id("fishing_loot/treasure"));
    public static final TagKey<Item> FISHING_LOOT_VARIETY = TagKey.create(Registries.ITEM, Constants.id("fishing_loot/variety"));
    public static final TagKey<Item> REPAIRS_NETHERITE_TOOL = TagKey.create(Registries.ITEM, Constants.id("repairs_netherite_tool"));
    public static final TagKey<Item> LOOTING = TagKey.create(Registries.ITEM, Constants.id("enchantable/looting"));
    public static final TagKey<Item> DYES = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "dyes"));
    public static final TagKey<Item> SHIELDS_COMMON = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "tools/shield"));

    public static final TagKey<Block> RAILS_BLOCK = TagKey.create(Registries.BLOCK, Constants.id("rails"));
    public static final TagKey<Block> REINFORCED_DOORS_BLOCK = TagKey.create(Registries.BLOCK, Constants.id("reinforced_doors"));
    public static final TagKey<Block> BREAKABLE_DOORS = TagKey.create(Registries.BLOCK, Constants.id("breakable_doors"));
    public static final TagKey<Block> COPPER_DOORS = TagKey.create(Registries.BLOCK, Constants.id("copper_doors"));
    public static final TagKey<Block> INCORRECT_FOR_QUARTZITE_TOOL = TagKey.create(Registries.BLOCK, Constants.id("incorrect_for_quartzite_tool"));
    public static final TagKey<Block> INCORRECT_FOR_NETHER_ALLOY_TOOL = TagKey.create(Registries.BLOCK, Constants.id("incorrect_for_nether_alloy_tool"));

    public static void registerModTags() {
        // Constants.LOG.info("Registering Mod Tags for " + Constants.MOD_ID);
    }
}
