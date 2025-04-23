package sircow.preservedinferno;

import sircow.preservedinferno.block.ModBlocks;
import sircow.preservedinferno.item.ModItems;
import sircow.preservedinferno.other.ModDamageTypes;
import sircow.preservedinferno.platform.Services;
import sircow.preservedinferno.sound.ModSounds;

public class CommonClass {
    public static void init() {
        if (Services.PLATFORM.isModLoaded("pinferno")) {
            Constants.LOG.info("Initialising Preserved: Inferno");
            // registering
            ModBlocks.registerModBlocks();
            ModItems.registerModItems();
            ModDamageTypes.registerModDamageTypes();
            ModSounds.registerSounds();
        }
    }
}