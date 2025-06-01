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
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.block.custom.AnglingTableBlock;
import sircow.preservedinferno.block.custom.InductorRailBlock;
import sircow.preservedinferno.block.custom.PreservedStairBlock;
import sircow.preservedinferno.block.custom.WeatheringInductorRailBlock;

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
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).noCollission().strength(0.7F).sound(SoundType.COPPER), true);
    public static final Block EXPOSED_INDUCTOR_RAIL = register("exposed_inductor_rail",
            (properties -> new WeatheringInductorRailBlock(WeatheringCopper.WeatherState.WEATHERED, properties)),
            BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).noCollission().strength(0.7F).sound(SoundType.COPPER), true);
    public static final Block WEATHERED_INDUCTOR_RAIL = register("weathered_inductor_rail",
            (properties -> new WeatheringInductorRailBlock(WeatheringCopper.WeatherState.EXPOSED, properties)),
            BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_STEM).noCollission().strength(0.7F).sound(SoundType.COPPER), true);
    public static final Block OXIDIZED_INDUCTOR_RAIL = register("oxidized_inductor_rail",
            (properties -> new WeatheringInductorRailBlock(WeatheringCopper.WeatherState.OXIDIZED, properties)),
            BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_NYLIUM).noCollission().strength(0.7F).sound(SoundType.COPPER), true);
    public static final Block WAXED_INDUCTOR_RAIL = register("waxed_inductor_rail",
            InductorRailBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).noCollission().strength(0.7F).sound(SoundType.COPPER), true);
    public static final Block WAXED_EXPOSED_INDUCTOR_RAIL = register("waxed_exposed_inductor_rail",
            InductorRailBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).noCollission().strength(0.7F).sound(SoundType.COPPER), true);
    public static final Block WAXED_WEATHERED_INDUCTOR_RAIL = register("waxed_weathered_inductor_rail",
            InductorRailBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_NYLIUM).noCollission().strength(0.7F).sound(SoundType.COPPER), true);
    public static final Block WAXED_OXIDIZED_INDUCTOR_RAIL = register("waxed_oxidized_inductor_rail",
            InductorRailBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).noCollission().strength(0.7F).sound(SoundType.COPPER), true);

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
        Constants.LOG.info("Registering Mod Blocks for " + Constants.MOD_ID);
    }
}
