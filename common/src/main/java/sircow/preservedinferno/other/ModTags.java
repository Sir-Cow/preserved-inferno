package sircow.preservedinferno.other;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import sircow.preservedinferno.Constants;

public class ModTags {
    public static final TagKey<Item> CLOTH = TagKey.create(Registries.ITEM, Constants.id("cloth"));
    public static final TagKey<Item> SHIELDS = TagKey.create(Registries.ITEM, Constants.id("shields"));

    public static void registerModTags() {
        Constants.LOG.info("Registering Mod Tags for " + Constants.MOD_ID);
    }
}
