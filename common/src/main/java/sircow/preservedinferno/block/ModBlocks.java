package sircow.preservedinferno.block;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.block.custom.*;

import java.util.function.Function;

public class ModBlocks {
    public static final Block RHYOLITE = register("rhyolite",
            Block::new, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DIRT)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(1.5F, 6.0F), true);
    public static final Block POLISHED_RHYOLITE = register("polished_rhyolite",
            Block::new, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DIRT)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(1.5F, 6.0F), true);
    public static final Block RHYOLITE_STAIRS = registerStair("rhyolite_stairs", RHYOLITE);
    public static final Block POLISHED_RHYOLITE_STAIRS = registerStair("polished_rhyolite_stairs", POLISHED_RHYOLITE);
    public static final Block RHYOLITE_SLAB = register("rhyolite_slab", SlabBlock::new, BlockBehaviour.Properties.ofFullCopy(RHYOLITE), true);
    public static final Block POLISHED_RHYOLITE_SLAB = register("polished_rhyolite_slab", SlabBlock::new, BlockBehaviour.Properties.ofFullCopy(POLISHED_RHYOLITE), true);
    public static final Block RHYOLITE_WALL = register("rhyolite_wall", WallBlock::new, BlockBehaviour.Properties.ofFullCopy(RHYOLITE).forceSolidOn(), true);

    public static final Block SPARKLING_BLACKSTONE = register("sparkling_blackstone",
            SparklingBlackstoneBlock::new, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(1.5F, 6.0F)
                    .sound(SoundType.GILDED_BLACKSTONE)
                    .pushReaction(PushReaction.DESTROY)
                    .randomTicks(),
            true
    );

    public static final Block ANGLING_TABLE = register("angling_table",
            AnglingTableBlock::new, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.5F)
                    .sound(SoundType.WOOD)
                    .ignitedByLava(),
            true
    );

    public static final Block INDUCTOR_RAIL = register("inductor_rail",
            (properties -> new WeatheringInductorRailBlock(WeatheringCopper.WeatherState.UNAFFECTED, properties)),
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).noCollision().strength(0.7F).sound(SoundType.COPPER), true);
    public static final Block EXPOSED_INDUCTOR_RAIL = register("exposed_inductor_rail",
            (properties -> new WeatheringInductorRailBlock(WeatheringCopper.WeatherState.WEATHERED, properties)),
            BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).noCollision().strength(0.7F).sound(SoundType.COPPER), true);
    public static final Block WEATHERED_INDUCTOR_RAIL = register("weathered_inductor_rail",
            (properties -> new WeatheringInductorRailBlock(WeatheringCopper.WeatherState.EXPOSED, properties)),
            BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_STEM).noCollision().strength(0.7F).sound(SoundType.COPPER), true);
    public static final Block OXIDIZED_INDUCTOR_RAIL = register("oxidized_inductor_rail",
            (properties -> new WeatheringInductorRailBlock(WeatheringCopper.WeatherState.OXIDIZED, properties)),
            BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_NYLIUM).noCollision().strength(0.7F).sound(SoundType.COPPER), true);
    public static final Block WAXED_INDUCTOR_RAIL = register("waxed_inductor_rail",
            InductorRailBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).noCollision().strength(0.7F).sound(SoundType.COPPER), true);
    public static final Block WAXED_EXPOSED_INDUCTOR_RAIL = register("waxed_exposed_inductor_rail",
            InductorRailBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).noCollision().strength(0.7F).sound(SoundType.COPPER), true);
    public static final Block WAXED_WEATHERED_INDUCTOR_RAIL = register("waxed_weathered_inductor_rail",
            InductorRailBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_NYLIUM).noCollision().strength(0.7F).sound(SoundType.COPPER), true);
    public static final Block WAXED_OXIDIZED_INDUCTOR_RAIL = register("waxed_oxidized_inductor_rail",
            InductorRailBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).noCollision().strength(0.7F).sound(SoundType.COPPER), true);

    public static final Block REINFORCED_OAK_DOOR = register("reinforced_oak_door",
            properties -> new PreservedDoorBlock(BlockSetType.OAK, properties),
            BlockBehaviour.Properties.of().mapColor(Blocks.OAK_PLANKS.defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY), true);
    public static final Block REINFORCED_SPRUCE_DOOR = register("reinforced_spruce_door",
            properties -> new PreservedDoorBlock(BlockSetType.SPRUCE, properties),
            BlockBehaviour.Properties.of().mapColor(Blocks.SPRUCE_PLANKS.defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY), true);
    public static final Block REINFORCED_BIRCH_DOOR = register("reinforced_birch_door",
            properties -> new PreservedDoorBlock(BlockSetType.BIRCH, properties),
            BlockBehaviour.Properties.of().mapColor(Blocks.BIRCH_PLANKS.defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY), true
    );
    public static final Block REINFORCED_JUNGLE_DOOR = register("reinforced_jungle_door",
            properties -> new PreservedDoorBlock(BlockSetType.JUNGLE, properties),
            BlockBehaviour.Properties.of().mapColor(Blocks.JUNGLE_PLANKS.defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY), true
    );
    public static final Block REINFORCED_ACACIA_DOOR = register("reinforced_acacia_door",
            properties -> new PreservedDoorBlock(BlockSetType.ACACIA, properties),
            BlockBehaviour.Properties.of().mapColor(Blocks.ACACIA_PLANKS.defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY), true
    );
    public static final Block REINFORCED_CHERRY_DOOR = register(
            "reinforced_cherry_door",
            properties -> new PreservedDoorBlock(BlockSetType.CHERRY, properties),
            BlockBehaviour.Properties.of().mapColor(Blocks.CHERRY_PLANKS.defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY), true
    );
    public static final Block REINFORCED_DARK_OAK_DOOR = register(
            "reinforced_dark_oak_door",
            properties -> new PreservedDoorBlock(BlockSetType.DARK_OAK, properties),
            BlockBehaviour.Properties.of().mapColor(Blocks.DARK_OAK_PLANKS.defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY), true
    );
    public static final Block REINFORCED_PALE_OAK_DOOR = register(
            "reinforced_pale_oak_door",
            properties -> new PreservedDoorBlock(BlockSetType.PALE_OAK, properties),
            BlockBehaviour.Properties.of().mapColor(Blocks.PALE_OAK_PLANKS.defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().pushReaction(PushReaction.DESTROY), true
    );
    public static final Block REINFORCED_MANGROVE_DOOR = register(
            "reinforced_mangrove_door",
            properties -> new PreservedDoorBlock(BlockSetType.MANGROVE, properties),
            BlockBehaviour.Properties.of().mapColor(Blocks.MANGROVE_PLANKS.defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY), true
    );
    public static final Block REINFORCED_BAMBOO_DOOR = register(
            "reinforced_bamboo_door",
            properties -> new PreservedDoorBlock(BlockSetType.BAMBOO, properties),
            BlockBehaviour.Properties.of().mapColor(Blocks.BAMBOO_PLANKS.defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY), true
    );
    public static final Block REINFORCED_CRIMSON_DOOR = register(
            "reinforced_crimson_door",
            properties -> new PreservedDoorBlock(BlockSetType.CRIMSON, properties),
            BlockBehaviour.Properties.of().mapColor(Blocks.CRIMSON_PLANKS.defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().pushReaction(PushReaction.DESTROY), true
    );
    public static final Block REINFORCED_WARPED_DOOR = register(
            "reinforced_warped_door",
            properties -> new PreservedDoorBlock(BlockSetType.WARPED, properties),
            BlockBehaviour.Properties.of().mapColor(Blocks.WARPED_PLANKS.defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().pushReaction(PushReaction.DESTROY), true
    );
    public static final Block REINFORCED_COPPER_DOOR = register(
            "reinforced_copper_door",
            properties -> new PreservedWeatheringCopperDoorBlock(BlockSetType.COPPER, WeatheringCopper.WeatherState.UNAFFECTED, properties),
            BlockBehaviour.Properties.of().mapColor(Blocks.COPPER_BLOCK.defaultMapColor()).strength(3.0F, 6.0F).noOcclusion().pushReaction(PushReaction.DESTROY), true
    );
    public static final Block REINFORCED_EXPOSED_COPPER_DOOR = register(
            "reinforced_exposed_copper_door",
            properties -> new PreservedWeatheringCopperDoorBlock(BlockSetType.COPPER, WeatheringCopper.WeatherState.EXPOSED, properties),
            BlockBehaviour.Properties.ofFullCopy(REINFORCED_COPPER_DOOR).mapColor(Blocks.EXPOSED_COPPER.defaultMapColor()), true
    );
    public static final Block REINFORCED_OXIDIZED_COPPER_DOOR = register(
            "reinforced_oxidized_copper_door",
            properties -> new PreservedWeatheringCopperDoorBlock(BlockSetType.COPPER, WeatheringCopper.WeatherState.OXIDIZED, properties),
            BlockBehaviour.Properties.ofFullCopy(REINFORCED_COPPER_DOOR).mapColor(Blocks.OXIDIZED_COPPER.defaultMapColor()), true
    );
    public static final Block REINFORCED_WEATHERED_COPPER_DOOR = register(
            "reinforced_weathered_copper_door",
            properties -> new PreservedWeatheringCopperDoorBlock(BlockSetType.COPPER, WeatheringCopper.WeatherState.WEATHERED, properties),
            BlockBehaviour.Properties.ofFullCopy(REINFORCED_COPPER_DOOR).mapColor(Blocks.WEATHERED_COPPER.defaultMapColor()), true
    );
    public static final Block REINFORCED_WAXED_COPPER_DOOR = register(
            "reinforced_waxed_copper_door", properties -> new PreservedDoorBlock(BlockSetType.COPPER, properties), BlockBehaviour.Properties.ofFullCopy(REINFORCED_COPPER_DOOR), true
    );
    public static final Block REINFORCED_WAXED_EXPOSED_COPPER_DOOR = register(
            "reinforced_waxed_exposed_copper_door", properties -> new PreservedDoorBlock(BlockSetType.COPPER, properties), BlockBehaviour.Properties.ofFullCopy(REINFORCED_EXPOSED_COPPER_DOOR), true
    );
    public static final Block REINFORCED_WAXED_OXIDIZED_COPPER_DOOR = register(
            "reinforced_waxed_oxidized_copper_door", properties -> new PreservedDoorBlock(BlockSetType.COPPER, properties), BlockBehaviour.Properties.ofFullCopy(REINFORCED_OXIDIZED_COPPER_DOOR), true
    );
    public static final Block REINFORCED_WAXED_WEATHERED_COPPER_DOOR = register(
            "reinforced_waxed_weathered_copper_door", properties -> new PreservedDoorBlock(BlockSetType.COPPER, properties), BlockBehaviour.Properties.ofFullCopy(REINFORCED_WEATHERED_COPPER_DOOR), true
    );

    private static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties settings, boolean shouldRegisterItem) {
        ResourceKey<Block> blockKey = keyOfBlock(name);
        Block block = blockFactory.apply(settings.setId(blockKey));

        if (shouldRegisterItem) {
            ResourceKey<Item> itemKey = keyOfItem(name);

            BlockItem blockItem = new BlockItem(block, new Item.Properties().setId(itemKey));
            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
        }

        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    private static ResourceKey<Block> keyOfBlock(String name) {
        return ResourceKey.create(Registries.BLOCK, Constants.id(name));
    }

    private static ResourceKey<Item> keyOfItem(String name) {
        return ResourceKey.create(Registries.ITEM, Constants.id(name));
    }

    private static Block registerStair(String name, Block baseBlock) {
        BlockState baseBlockState = baseBlock.defaultBlockState();
        return register(name, (properties) -> new PreservedStairBlock(baseBlockState, properties), BlockBehaviour.Properties.ofFullCopy(baseBlock), true);
    }

    public static void registerModBlocks() {
        // Constants.LOG.info("Registering Mod Blocks for " + Constants.MOD_ID);
    }
}
