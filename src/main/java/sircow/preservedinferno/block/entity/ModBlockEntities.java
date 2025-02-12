package sircow.preservedinferno.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import sircow.preservedinferno.PreservedInferno;
import sircow.preservedinferno.block.ModBlocks;

public class ModBlockEntities {
    public static final BlockEntityType<NewCauldronBlockEntity> NEW_CAULDRON_BLOCK_ENTITY = register("new_cauldron_entity",
                    FabricBlockEntityTypeBuilder.create(NewCauldronBlockEntity::new, ModBlocks.NEW_CAULDRON_BLOCK).build());

    public static final BlockEntityType<NewLoomBlockEntity> NEW_LOOM_BLOCK_ENTITY = register("new_loom_entity",
                    FabricBlockEntityTypeBuilder.create(NewLoomBlockEntity::new, ModBlocks.NEW_LOOM_BLOCK).build());

    public static final BlockEntityType<NewFletchingTableBlockEntity> NEW_FLETCHING_TABLE_BLOCK_ENTITY = register("new_fletching_table_entity",
                    FabricBlockEntityTypeBuilder.create(NewFletchingTableBlockEntity::new, ModBlocks.NEW_FLETCHING_TABLE_BLOCK).build());

    public static final BlockEntityType<NewEnchantingTableBlockEntity> NEW_ENCHANTING_TABLE_BLOCK_ENTITY = register("new_enchanting_table_entity",
            FabricBlockEntityTypeBuilder.create(NewEnchantingTableBlockEntity::new, ModBlocks.NEW_ENCHANTING_TABLE_BLOCK).build());

    public static <T extends BlockEntityType<?>> T register(String path, T blockEntityType) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(PreservedInferno.MOD_ID, path), blockEntityType);
    }

    public static void registerBlockEntities() {
        PreservedInferno.LOGGER.info("Registering Block Entities for " + PreservedInferno.MOD_ID);
    }
}
