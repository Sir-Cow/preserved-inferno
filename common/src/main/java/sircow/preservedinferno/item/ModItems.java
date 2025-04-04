package sircow.preservedinferno.item;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.block.ModBlocks;

import java.util.function.BiFunction;
import java.util.function.Function;

public class ModItems {
    // items
    public static final Item RAW_HIDE = registerItem("raw_hide");
    public static final Item PHANTOM_SINEW = registerItem("phantom_sinew");
    public static final Item HOLLOW_TWINE = registerItem("hollow_twine");
    public static final Item DREAMCATCHER = registerItem("dreamcatcher", new Item.Properties().stacksTo(1));

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

    public static final Item DIAMOND_SHIELD = registerItem("diamond_shield");
    public static final Item GOLDEN_SHIELD = registerItem("golden_shield");
    public static final Item IRON_SHIELD = registerItem("iron_shield");
    public static final Item NETHERITE_SHIELD = registerItem("netherite_shield");
    public static final Item WOODEN_SHIELD = registerItem("wooden_shield");

    public static final Item INDUCTOR_RAIL = registerBlock(ModBlocks.INDUCTOR_RAIL);
    public static final Item EXPOSED_INDUCTOR_RAIL = registerBlock(ModBlocks.EXPOSED_INDUCTOR_RAIL);
    public static final Item WEATHERED_INDUCTOR_RAIL = registerBlock(ModBlocks.WEATHERED_INDUCTOR_RAIL);
    public static final Item OXIDIZED_INDUCTOR_RAIL = registerBlock(ModBlocks.OXIDIZED_INDUCTOR_RAIL);
    public static final Item WAXED_INDUCTOR_RAIL = registerBlock(ModBlocks.WAXED_INDUCTOR_RAIL);
    public static final Item WAXED_EXPOSED_INDUCTOR_RAIL = registerBlock(ModBlocks.WAXED_EXPOSED_INDUCTOR_RAIL);
    public static final Item WAXED_WEATHERED_INDUCTOR_RAIL = registerBlock(ModBlocks.WAXED_WEATHERED_INDUCTOR_RAIL);
    public static final Item WAXED_OXIDIZED_INDUCTOR_RAIL = registerBlock(ModBlocks.WAXED_OXIDIZED_INDUCTOR_RAIL);

    private static ResourceKey<Item> blockIdToItemId(ResourceKey<Block> blockId) {
        return ResourceKey.create(Registries.ITEM, blockId.location());
    }

    private static ResourceKey<Item> moddedItemId(String name) {
        return ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
    }

    public static Item registerBlock(Block block) {
        return registerBlock(block, BlockItem::new);
    }

    public static Item registerBlock(Block block, BiFunction<Block, Item.Properties, Item> factory) {
        return registerBlock(block, factory, new Item.Properties());
    }

    public static Item registerBlock(Block block, BiFunction<Block, Item.Properties, Item> factory, Item.Properties properties) {
        return registerItem(
                blockIdToItemId(block.builtInRegistryHolder().key()), p_370785_ -> factory.apply(block, p_370785_), properties.useBlockDescriptionPrefix()
        );
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
