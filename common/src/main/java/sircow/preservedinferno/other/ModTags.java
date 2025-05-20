package sircow.preservedinferno.other;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import sircow.preservedinferno.Constants;

public class ModTags {
    public static final TagKey<Item> CLOTH = TagKey.create(Registries.ITEM, Constants.id("cloth"));
    public static final TagKey<Item> SHIELDS = TagKey.create(Registries.ITEM, Constants.id("shields"));
    public static final TagKey<Item> HOOKS = TagKey.create(Registries.ITEM, Constants.id("hooks"));
    public static final TagKey<Item> LINES = TagKey.create(Registries.ITEM, Constants.id("lines"));
    public static final TagKey<Item> SINKERS = TagKey.create(Registries.ITEM, Constants.id("sinkers"));
    public static final TagKey<Item> ROD_UPGRADES = TagKey.create(Registries.ITEM, Constants.id("rod_upgrades"));
    public static final TagKey<Item> FISHING_LOOT_FISH = TagKey.create(Registries.ITEM, Constants.id("fishing_loot/fish"));
    public static final TagKey<Item> FISHING_LOOT_JUNK = TagKey.create(Registries.ITEM, Constants.id("fishing_loot/junk"));
    public static final TagKey<Item> FISHING_LOOT_TREASURE = TagKey.create(Registries.ITEM, Constants.id("fishing_loot/treasure"));
    public static final TagKey<Item> FISHING_LOOT_VARIETY = TagKey.create(Registries.ITEM, Constants.id("fishing_loot/variety"));
    public static final TagKey<Item> COPPER_TOOL_MATERIALS = TagKey.create(Registries.ITEM, Constants.id("copper_tool_materials"));

    public static final TagKey<Block> INCORRECT_FOR_COPPER_TOOL = TagKey.create(Registries.BLOCK, Constants.id("incorrect_for_copper_tool"));

    public static void registerModTags() {
        Constants.LOG.info("Registering Mod Tags for " + Constants.MOD_ID);
    }
}
