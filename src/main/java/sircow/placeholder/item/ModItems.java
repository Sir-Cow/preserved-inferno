package sircow.placeholder.item;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import sircow.placeholder.Placeholder;
import sircow.placeholder.block.ModBlocks;

import java.util.function.Function;

public class ModItems {
    // items
    public static final Item RAW_HIDE = registerItem("raw_hide");
    public static final Item PHANTOM_SINEW = registerItem("phantom_sinew");
    public static final Item HOLLOW_TWINE = registerItem("hollow_twine");
    public static final Item DREAMCATCHER = register("dreamcatcher", new Item.Settings().maxCount(1));

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

    public static final Item NEW_CAULDRON_BLOCK = Items.register(ModBlocks.NEW_CAULDRON_BLOCK);
    public static final Item NEW_LOOM_BLOCK = Items.register(ModBlocks.NEW_LOOM_BLOCK);
    public static final Item NEW_FLETCHING_TABLE_BLOCK = Items.register(ModBlocks.NEW_FLETCHING_TABLE_BLOCK);
    public static final Item NEW_ENCHANTING_TABLE_BLOCK = Items.register(ModBlocks.NEW_ENCHANTING_TABLE_BLOCK);
    public static final Item INDUCTOR_RAIL = Items.register(ModBlocks.INDUCTOR_RAIL);
    public static final Item EXPOSED_INDUCTOR_RAIL = Items.register(ModBlocks.EXPOSED_INDUCTOR_RAIL);
    public static final Item WEATHERED_INDUCTOR_RAIL = Items.register(ModBlocks.WEATHERED_INDUCTOR_RAIL);
    public static final Item OXIDIZED_INDUCTOR_RAIL = Items.register(ModBlocks.OXIDIZED_INDUCTOR_RAIL);
    public static final Item WAXED_INDUCTOR_RAIL = Items.register(ModBlocks.WAXED_INDUCTOR_RAIL);
    public static final Item WAXED_EXPOSED_INDUCTOR_RAIL = Items.register(ModBlocks.WAXED_EXPOSED_INDUCTOR_RAIL);
    public static final Item WAXED_WEATHERED_INDUCTOR_RAIL = Items.register(ModBlocks.WAXED_WEATHERED_INDUCTOR_RAIL);
    public static final Item WAXED_OXIDIZED_INDUCTOR_RAIL = Items.register(ModBlocks.WAXED_OXIDIZED_INDUCTOR_RAIL);

    private static Item registerItem(String name) {
        Identifier id = Identifier.of(Placeholder.MOD_ID, name);
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);
        Item.Settings settings = new Item.Settings().registryKey(key);
        return Registry.register(Registries.ITEM, key, new Item(settings));
    }

    private static RegistryKey<Item> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Placeholder.MOD_ID, id));
    }

    public static Item register(String id, Item.Settings settings) {
        return register(keyOf(id), Item::new, settings);
    }

    public static Item register(RegistryKey<Item> key, Function<Item.Settings, Item> factory, Item.Settings settings) {
        Item item = factory.apply(settings.registryKey(key));
        if (item instanceof BlockItem blockItem) {
            blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
        }
        return Registry.register(Registries.ITEM, key, item);
    }

    public static void registerModItems() {
        Placeholder.LOGGER.info("Registering Mod Items for " + Placeholder.MOD_ID);
    }
}
