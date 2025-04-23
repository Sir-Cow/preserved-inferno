package sircow.preservedinferno.item;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import sircow.preservedinferno.Constants;

import java.util.function.Function;

public class ModItems {
    // items
    public static final Item DREAMCATCHER = registerItem("dreamcatcher", new Item.Properties().stacksTo(1));
    public static final Item ELDER_GUARDIAN_SPINE = registerItem("elder_guardian_spine", new Item.Properties().rarity(Rarity.EPIC));
    public static final Item HOLLOW_TWINE = registerItem("hollow_twine");
    public static final Item PHANTOM_SINEW = registerItem("phantom_sinew");
    public static final Item RAW_HIDE = registerItem("raw_hide");

    public static final Item RAW_IRON_CHUNK = registerItem("raw_iron_chunk");
    public static final Item RAW_GOLD_CHUNK = registerItem("raw_gold_chunk");
    public static final Item RAW_COPPER_CHUNK = registerItem("raw_copper_chunk");
    public static final Item COPPER_NUGGET = registerItem("copper_nugget");

    public static final Item BLACK_CLOTH = registerItem("black_cloth");
    public static final Item BLUE_CLOTH = registerItem("blue_cloth");
    public static final Item BROWN_CLOTH = registerItem("brown_cloth");
    public static final Item CYAN_CLOTH = registerItem("cyan_cloth");
    public static final Item GRAY_CLOTH = registerItem("gray_cloth");
    public static final Item GREEN_CLOTH = registerItem("green_cloth");
    public static final Item LIGHT_BLUE_CLOTH = registerItem("light_blue_cloth");
    public static final Item LIGHT_GRAY_CLOTH = registerItem("light_gray_cloth");
    public static final Item LIME_CLOTH = registerItem("lime_cloth");
    public static final Item MAGENTA_CLOTH = registerItem("magenta_cloth");
    public static final Item ORANGE_CLOTH = registerItem("orange_cloth");
    public static final Item PINK_CLOTH = registerItem("pink_cloth");
    public static final Item PURPLE_CLOTH = registerItem("purple_cloth");
    public static final Item RED_CLOTH = registerItem("red_cloth");
    public static final Item WHITE_CLOTH = registerItem("white_cloth");
    public static final Item YELLOW_CLOTH = registerItem("yellow_cloth");

    public static final Item DIAMOND_SHIELD = registerItem("diamond_shield", new Item.Properties()
            .durability(1561)
            .stacksTo(1)
            .repairable(ItemTags.DIAMOND_TOOL_MATERIALS)
    );
    public static final Item GOLDEN_SHIELD = registerItem("golden_shield", new Item.Properties()
            .durability(32)
            .stacksTo(1)
            .repairable(ItemTags.GOLD_TOOL_MATERIALS)
    );
    public static final Item IRON_SHIELD = registerItem("iron_shield", new Item.Properties()
            .durability(250)
            .stacksTo(1)
            .repairable(ItemTags.IRON_TOOL_MATERIALS)
    );
    public static final Item NETHERITE_SHIELD = registerItem("netherite_shield", new Item.Properties()
            .durability(2031)
            .stacksTo(1)
            .repairable(ItemTags.NETHERITE_TOOL_MATERIALS)
            .fireResistant()
    );
    public static final Item WOODEN_SHIELD = registerItem("wooden_shield", new Item.Properties()
            .durability(59)
            .stacksTo(1)
            .repairable(ItemTags.WOODEN_TOOL_MATERIALS)
    );

    public static final Item AQUATIC_FIBER = registerItem("aquatic_fiber");

    public static final Item IRON_FISHING_HOOK = registerItem("iron_fishing_hook", new Item.Properties()
            .durability(250)
            .stacksTo(1)
            .repairable(ItemTags.IRON_TOOL_MATERIALS)
    );
    public static final Item DIAMOND_FISHING_HOOK = registerItem("diamond_fishing_hook", new Item.Properties()
            .durability(1561)
            .stacksTo(1)
            .repairable(ItemTags.DIAMOND_TOOL_MATERIALS)
    );
    public static final Item NETHERITE_FISHING_HOOK = registerItem("netherite_fishing_hook", new Item.Properties()
            .durability(2031)
            .stacksTo(1)
            .repairable(ItemTags.NETHERITE_TOOL_MATERIALS)
            .fireResistant()
    );
    public static final Item IRON_LACED_FISHING_LINE = registerItem("iron_laced_fishing_line", new Item.Properties()
            .durability(250)
            .stacksTo(1)
            .repairable(ItemTags.IRON_TOOL_MATERIALS)
    );
    public static final Item DIAMOND_LACED_FISHING_LINE = registerItem("diamond_laced_fishing_line", new Item.Properties()
            .durability(1561)
            .stacksTo(1)
            .repairable(ItemTags.DIAMOND_TOOL_MATERIALS)
    );
    public static final Item NETHERITE_LACED_FISHING_LINE = registerItem("netherite_laced_fishing_line", new Item.Properties()
            .durability(2031)
            .stacksTo(1)
            .repairable(ItemTags.NETHERITE_TOOL_MATERIALS)
            .fireResistant()
    );
    public static final Item IRON_SINKER = registerItem("iron_sinker", new Item.Properties()
            .durability(250)
            .stacksTo(1)
            .repairable(ItemTags.IRON_TOOL_MATERIALS)
    );
    public static final Item DIAMOND_SINKER = registerItem("diamond_sinker", new Item.Properties()
            .durability(1561)
            .stacksTo(1)
            .repairable(ItemTags.DIAMOND_TOOL_MATERIALS)
    );
    public static final Item NETHERITE_SINKER = registerItem("netherite_sinker", new Item.Properties()
            .durability(2031)
            .stacksTo(1)
            .repairable(ItemTags.NETHERITE_TOOL_MATERIALS)
            .fireResistant()
    );
    public static final Item MUSIC_DISC_AQUA = registerItem("music_disc_aqua", new Item.Properties().stacksTo(1));

    private static ResourceKey<Item> moddedItemId(String name) {
        return ResourceKey.create(Registries.ITEM, Constants.id(name));
    }

    public static Item registerItem(String name, Function<Item.Properties, Item> factory, Item.Properties properties) {
        return registerItem(moddedItemId(name), factory, properties);
    }

    public static Item registerItem(String name, Item.Properties properties) {
        return registerItem(moddedItemId(name), Item::new, properties);
    }

    public static Item registerItem(String name) {
        return registerItem(moddedItemId(name), Item::new, new Item.Properties());
    }

    public static Item registerItem(ResourceKey<Item> key, Function<Item.Properties, Item> factory, Item.Properties properties) {
        Item item = factory.apply(properties.setId(key));
        if (item instanceof BlockItem blockitem) {
            blockitem.registerBlocks(Item.BY_BLOCK, item);
        }
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    public static void registerModItems() {
        Constants.LOG.info("Registering Mod Items for " + Constants.MOD_ID);
    }
}
