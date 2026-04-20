package sircow.preservedinferno.block;

import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;

public class FabricModBlocks {
    public static void registerBlocks() {
        OxidizableBlocksRegistry.registerNextStage(ModBlocks.INDUCTOR_RAIL, ModBlocks.EXPOSED_INDUCTOR_RAIL);
        OxidizableBlocksRegistry.registerNextStage(ModBlocks.EXPOSED_INDUCTOR_RAIL, ModBlocks.WEATHERED_INDUCTOR_RAIL);
        OxidizableBlocksRegistry.registerNextStage(ModBlocks.WEATHERED_INDUCTOR_RAIL, ModBlocks.OXIDIZED_INDUCTOR_RAIL);
        OxidizableBlocksRegistry.registerWaxable(ModBlocks.INDUCTOR_RAIL, ModBlocks.WAXED_INDUCTOR_RAIL);
        OxidizableBlocksRegistry.registerWaxable(ModBlocks.EXPOSED_INDUCTOR_RAIL, ModBlocks.WAXED_EXPOSED_INDUCTOR_RAIL);
        OxidizableBlocksRegistry.registerWaxable(ModBlocks.WEATHERED_INDUCTOR_RAIL, ModBlocks.WAXED_WEATHERED_INDUCTOR_RAIL);
        OxidizableBlocksRegistry.registerWaxable(ModBlocks.OXIDIZED_INDUCTOR_RAIL, ModBlocks.WAXED_OXIDIZED_INDUCTOR_RAIL);

        OxidizableBlocksRegistry.registerNextStage(ModBlocks.REINFORCED_COPPER_DOOR, ModBlocks.REINFORCED_EXPOSED_COPPER_DOOR);
        OxidizableBlocksRegistry.registerNextStage(ModBlocks.REINFORCED_EXPOSED_COPPER_DOOR, ModBlocks.REINFORCED_WEATHERED_COPPER_DOOR);
        OxidizableBlocksRegistry.registerNextStage(ModBlocks.REINFORCED_WEATHERED_COPPER_DOOR, ModBlocks.REINFORCED_OXIDIZED_COPPER_DOOR);
        OxidizableBlocksRegistry.registerWaxable(ModBlocks.REINFORCED_COPPER_DOOR, ModBlocks.REINFORCED_WAXED_COPPER_DOOR);
        OxidizableBlocksRegistry.registerWaxable(ModBlocks.REINFORCED_EXPOSED_COPPER_DOOR, ModBlocks.REINFORCED_WAXED_EXPOSED_COPPER_DOOR);
        OxidizableBlocksRegistry.registerWaxable(ModBlocks.REINFORCED_WEATHERED_COPPER_DOOR, ModBlocks.REINFORCED_WAXED_WEATHERED_COPPER_DOOR);
        OxidizableBlocksRegistry.registerWaxable(ModBlocks.REINFORCED_OXIDIZED_COPPER_DOOR, ModBlocks.REINFORCED_WAXED_OXIDIZED_COPPER_DOOR);
        // Constants.LOG.info("Registering Fabric Mod Blocks for " + Constants.MOD_ID);
    }
}
