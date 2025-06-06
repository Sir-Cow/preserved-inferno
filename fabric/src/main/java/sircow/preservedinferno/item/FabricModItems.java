package sircow.preservedinferno.item;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemContainerContents;
import sircow.preservedinferno.item.custom.CacheItem;
import sircow.preservedinferno.Constants;

import java.util.function.Function;

public class FabricModItems {
    public static final Item CACHE = registerItem("cache", properties ->
            new CacheItem(properties, 9), new Item.Properties()
            .component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
            .rarity(Rarity.UNCOMMON)
            .stacksTo(1)
    );

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
        Constants.LOG.info("Registering Fabric Mod Items for " + Constants.MOD_ID);
    }
}
