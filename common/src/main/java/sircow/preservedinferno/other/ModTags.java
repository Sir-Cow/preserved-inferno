package sircow.preservedinferno.other;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import sircow.preservedinferno.Constants;

public class ModTags {
    public static final TagKey<Item> BANNERS = TagKey.create(Registries.ITEM, Constants.id("banners"));
    public static final TagKey<Item> CARPETS = TagKey.create(Registries.ITEM, Constants.id("carpets"));
    public static final TagKey<Item> CLOTH = TagKey.create(Registries.ITEM, Constants.id("cloth"));
    public static final TagKey<Item> OVERWORLD_WOODEN_SLABS = TagKey.create(Registries.ITEM, Constants.id("overworld_wooden_slabs"));
    public static final TagKey<Item> OVERWORLD_PLANKS = TagKey.create(Registries.ITEM, Constants.id("overworld_planks"));
    public static final TagKey<Item> OVERWORLD_WOODEN_PRESSURE_PLATES = TagKey.create(Registries.ITEM, Constants.id("overworld_wooden_pressure_plates"));
    public static final TagKey<Item> SHIELDS = TagKey.create(Registries.ITEM, Constants.id("shields"));

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

    public static final TagKey<Item> DYES = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "dyes"));

    public static void registerModTags() {
        // Constants.LOG.info("Registering Mod Tags for " + Constants.MOD_ID);
    }
}
