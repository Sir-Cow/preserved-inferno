package sircow.preservedinferno.block;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.dispenser.ShulkerBoxDispenseBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;

public class FabricModBlocks {
    public static void registerFabricModBlocks() {
        ModBlocks.getBlocks().forEach((id, definition) -> {
            Block block = definition.factory().get();
            Registry.register(BuiltInRegistries.BLOCK, id.block(), block);
            Registry.register(BuiltInRegistries.ITEM, id.item(), new BlockItem(block, new Item.Properties().setId(id.item())));
        });

        OxidizableBlocksRegistry.registerNextStage(ModBlocks.INDUCTOR_RAIL.get(), ModBlocks.EXPOSED_INDUCTOR_RAIL.get());
        OxidizableBlocksRegistry.registerNextStage(ModBlocks.EXPOSED_INDUCTOR_RAIL.get(), ModBlocks.WEATHERED_INDUCTOR_RAIL.get());
        OxidizableBlocksRegistry.registerNextStage(ModBlocks.WEATHERED_INDUCTOR_RAIL.get(), ModBlocks.OXIDIZED_INDUCTOR_RAIL.get());
        OxidizableBlocksRegistry.registerWaxable(ModBlocks.INDUCTOR_RAIL.get(), ModBlocks.WAXED_INDUCTOR_RAIL.get());
        OxidizableBlocksRegistry.registerWaxable(ModBlocks.EXPOSED_INDUCTOR_RAIL.get(), ModBlocks.WAXED_EXPOSED_INDUCTOR_RAIL.get());
        OxidizableBlocksRegistry.registerWaxable(ModBlocks.WEATHERED_INDUCTOR_RAIL.get(), ModBlocks.WAXED_WEATHERED_INDUCTOR_RAIL.get());
        OxidizableBlocksRegistry.registerWaxable(ModBlocks.OXIDIZED_INDUCTOR_RAIL.get(), ModBlocks.WAXED_OXIDIZED_INDUCTOR_RAIL.get());

        OxidizableBlocksRegistry.registerNextStage(ModBlocks.REINFORCED_COPPER_DOOR.get(), ModBlocks.REINFORCED_EXPOSED_COPPER_DOOR.get());
        OxidizableBlocksRegistry.registerNextStage(ModBlocks.REINFORCED_EXPOSED_COPPER_DOOR.get(), ModBlocks.REINFORCED_WEATHERED_COPPER_DOOR.get());
        OxidizableBlocksRegistry.registerNextStage(ModBlocks.REINFORCED_WEATHERED_COPPER_DOOR.get(), ModBlocks.REINFORCED_OXIDIZED_COPPER_DOOR.get());
        OxidizableBlocksRegistry.registerWaxable(ModBlocks.REINFORCED_COPPER_DOOR.get(), ModBlocks.REINFORCED_WAXED_COPPER_DOOR.get());
        OxidizableBlocksRegistry.registerWaxable(ModBlocks.REINFORCED_EXPOSED_COPPER_DOOR.get(), ModBlocks.REINFORCED_WAXED_EXPOSED_COPPER_DOOR.get());
        OxidizableBlocksRegistry.registerWaxable(ModBlocks.REINFORCED_WEATHERED_COPPER_DOOR.get(), ModBlocks.REINFORCED_WAXED_WEATHERED_COPPER_DOOR.get());
        OxidizableBlocksRegistry.registerWaxable(ModBlocks.REINFORCED_OXIDIZED_COPPER_DOOR.get(), ModBlocks.REINFORCED_WAXED_OXIDIZED_COPPER_DOOR.get());

        DispenserBlock.registerBehavior(ModBlocks.BOOM_BOX.get().asItem(), new ShulkerBoxDispenseBehavior());
        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.BOOM_BOX.get(), 15, 100);
    }
}
