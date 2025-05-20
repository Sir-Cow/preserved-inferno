package sircow.preservedinferno.block;

import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import sircow.preservedinferno.Constants;

public class FabricModBlocks {
    public static void registerBlocks() {
        OxidizableBlocksRegistry.registerOxidizableBlockPair(ModBlocks.INDUCTOR_RAIL, ModBlocks.EXPOSED_INDUCTOR_RAIL);
        OxidizableBlocksRegistry.registerOxidizableBlockPair(ModBlocks.EXPOSED_INDUCTOR_RAIL, ModBlocks.WEATHERED_INDUCTOR_RAIL);
        OxidizableBlocksRegistry.registerOxidizableBlockPair(ModBlocks.WEATHERED_INDUCTOR_RAIL, ModBlocks.OXIDIZED_INDUCTOR_RAIL);
        OxidizableBlocksRegistry.registerWaxableBlockPair(ModBlocks.INDUCTOR_RAIL, ModBlocks.WAXED_INDUCTOR_RAIL);
        OxidizableBlocksRegistry.registerWaxableBlockPair(ModBlocks.EXPOSED_INDUCTOR_RAIL, ModBlocks.WAXED_EXPOSED_INDUCTOR_RAIL);
        OxidizableBlocksRegistry.registerWaxableBlockPair(ModBlocks.WEATHERED_INDUCTOR_RAIL, ModBlocks.WAXED_WEATHERED_INDUCTOR_RAIL);
        OxidizableBlocksRegistry.registerWaxableBlockPair(ModBlocks.OXIDIZED_INDUCTOR_RAIL, ModBlocks.WAXED_OXIDIZED_INDUCTOR_RAIL);

        Constants.LOG.info("Registering Fabric Mod Blocks for " + Constants.MOD_ID);
    }
}
