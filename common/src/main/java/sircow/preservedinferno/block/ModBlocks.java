package sircow.preservedinferno.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.block.custom.*;
import sircow.preservedinferno.fluid.ModFluids;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {
    private static final Map<BlockItemId, Definition> BLOCKS = new LinkedHashMap<>();

    public static Supplier<Block> RHYOLITE = register(
            ModBlockItemIds.RHYOLITE,
            Block::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DIRT)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(1.5F, 6.0F)
    );
    public static Supplier<Block> POLISHED_RHYOLITE = register(
            ModBlockItemIds.POLISHED_RHYOLITE,
            Block::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DIRT)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(1.5F, 6.0F)
    );
    public static Supplier<Block> RHYOLITE_BRICKS = register(
            ModBlockItemIds.RHYOLITE_BRICKS,
            Block::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DIRT)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(1.5F, 6.0F)
    );
    public static Supplier<Block> CRACKED_RHYOLITE_BRICKS = register(
            ModBlockItemIds.CRACKED_RHYOLITE_BRICKS,
            Block::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DIRT)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(1.5F, 6.0F)
    );
    public static Supplier<Block> RHYOLITE_STAIRS = register(
            ModBlockItemIds.RHYOLITE_STAIRS,
            properties -> new PreservedStairBlock(RHYOLITE.get().defaultBlockState(), properties),
            BlockBehaviour.Properties.ofFullCopy(RHYOLITE.get())
    );
    public static Supplier<Block> POLISHED_RHYOLITE_STAIRS = register(
            ModBlockItemIds.POLISHED_RHYOLITE_STAIRS,
            properties -> new PreservedStairBlock(POLISHED_RHYOLITE.get().defaultBlockState(), properties),
            BlockBehaviour.Properties.ofFullCopy(POLISHED_RHYOLITE.get())
    );
    public static Supplier<Block> RHYOLITE_BRICK_STAIRS = register(
            ModBlockItemIds.RHYOLITE_BRICK_STAIRS,
            properties -> new PreservedStairBlock(RHYOLITE_BRICKS.get().defaultBlockState(), properties),
            BlockBehaviour.Properties.ofFullCopy(RHYOLITE_BRICKS.get())
    );
    public static Supplier<Block> RHYOLITE_SLAB = register(
            ModBlockItemIds.RHYOLITE_SLAB,
            SlabBlock::new,
            BlockBehaviour.Properties.ofFullCopy(RHYOLITE.get())
    );
    public static Supplier<Block> POLISHED_RHYOLITE_SLAB = register(
            ModBlockItemIds.POLISHED_RHYOLITE_SLAB,
            SlabBlock::new,
            BlockBehaviour.Properties.ofFullCopy(POLISHED_RHYOLITE.get())
    );
    public static Supplier<Block> RHYOLITE_BRICK_SLAB = register(
            ModBlockItemIds.RHYOLITE_BRICK_SLAB,
            SlabBlock::new,
            BlockBehaviour.Properties.ofFullCopy(RHYOLITE_BRICKS.get())
    );
    public static Supplier<Block> RHYOLITE_WALL = register(
            ModBlockItemIds.RHYOLITE_WALL,
            WallBlock::new,
            BlockBehaviour.Properties.ofFullCopy(RHYOLITE.get()).forceSolidOn()
    );
    public static Supplier<Block> RHYOLITE_BRICK_WALL = register(
            ModBlockItemIds.RHYOLITE_BRICK_WALL,
            WallBlock::new,
            BlockBehaviour.Properties.ofFullCopy(RHYOLITE_BRICKS.get()).forceSolidOn()
    );
    public static Supplier<Block> SPARKLING_BLACKSTONE = register(
            ModBlockItemIds.SPARKLING_BLACKSTONE,
            SparklingBlackstoneBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(1.5F, 6.0F)
                    .sound(SoundType.GILDED_BLACKSTONE)
                    .pushReaction(PushReaction.DESTROY)
                    .randomTicks()
    );
    public static Supplier<Block> ANGLING_TABLE = register(
            ModBlockItemIds.ANGLING_TABLE,
            AnglingTableBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.5F)
                    .sound(SoundType.WOOD)
                    .ignitedByLava()
    );
    public static Supplier<Block> BOOM_BOX = register(
            ModBlockItemIds.BOOM_BOX,
            BoomBoxBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .instrument(NoteBlockInstrument.HARP)
                    .strength(2.5F)
                    .sound(SoundType.WOOD)
                    .ignitedByLava()
                    .isRedstoneConductor(ModBlocks::never)
    );
    public static Supplier<Block> INDUCTOR_RAIL = register(
            ModBlockItemIds.INDUCTOR_RAIL,
            properties -> new WeatheringInductorRailBlock(WeatheringCopper.WeatherState.UNAFFECTED, properties),
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).noCollision().strength(0.7F).sound(SoundType.COPPER)
    );
    public static Supplier<Block> EXPOSED_INDUCTOR_RAIL = register(
            ModBlockItemIds.EXPOSED_INDUCTOR_RAIL,
            properties -> new WeatheringInductorRailBlock(WeatheringCopper.WeatherState.WEATHERED, properties),
            BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).noCollision().strength(0.7F).sound(SoundType.COPPER)
    );
    public static Supplier<Block> WEATHERED_INDUCTOR_RAIL = register(
            ModBlockItemIds.WEATHERED_INDUCTOR_RAIL,
            properties -> new WeatheringInductorRailBlock(WeatheringCopper.WeatherState.EXPOSED, properties),
            BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_STEM).noCollision().strength(0.7F).sound(SoundType.COPPER)
    );
    public static Supplier<Block> OXIDIZED_INDUCTOR_RAIL = register(
            ModBlockItemIds.OXIDIZED_INDUCTOR_RAIL,
            properties -> new WeatheringInductorRailBlock(WeatheringCopper.WeatherState.OXIDIZED, properties),
            BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_NYLIUM).noCollision().strength(0.7F).sound(SoundType.COPPER)
    );
    public static Supplier<Block> WAXED_INDUCTOR_RAIL = register(
            ModBlockItemIds.WAXED_INDUCTOR_RAIL,
            InductorRailBlock::new,
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).noCollision().strength(0.7F).sound(SoundType.COPPER)
    );
    public static Supplier<Block> WAXED_EXPOSED_INDUCTOR_RAIL = register(
            ModBlockItemIds.WAXED_EXPOSED_INDUCTOR_RAIL,
            InductorRailBlock::new,
            BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).noCollision().strength(0.7F).sound(SoundType.COPPER)
    );
    public static Supplier<Block> WAXED_WEATHERED_INDUCTOR_RAIL = register(
            ModBlockItemIds.WAXED_WEATHERED_INDUCTOR_RAIL,
            InductorRailBlock::new,
            BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_NYLIUM).noCollision().strength(0.7F).sound(SoundType.COPPER)
    );
    public static Supplier<Block> WAXED_OXIDIZED_INDUCTOR_RAIL = register(
            ModBlockItemIds.WAXED_OXIDIZED_INDUCTOR_RAIL,
            InductorRailBlock::new,
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).noCollision().strength(0.7F).sound(SoundType.COPPER)
    );
    public static Supplier<Block> REINFORCED_OAK_DOOR = register(
            ModBlockItemIds.REINFORCED_OAK_DOOR,
            properties -> new PreservedDoorBlock(BlockSetType.OAK, properties),
            BlockBehaviour.Properties.of().mapColor(Blocks.OAK_PLANKS.defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY)
    );
    public static Supplier<Block> REINFORCED_SPRUCE_DOOR = register(
            ModBlockItemIds.REINFORCED_SPRUCE_DOOR,
            properties -> new PreservedDoorBlock(BlockSetType.SPRUCE, properties),
            BlockBehaviour.Properties.of().mapColor(Blocks.SPRUCE_PLANKS.defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY)
    );
    public static Supplier<Block> REINFORCED_BIRCH_DOOR = register(
            ModBlockItemIds.REINFORCED_BIRCH_DOOR,
            properties -> new PreservedDoorBlock(BlockSetType.BIRCH, properties),
            BlockBehaviour.Properties.of().mapColor(Blocks.BIRCH_PLANKS.defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY)
    );
    public static Supplier<Block> REINFORCED_JUNGLE_DOOR = register(
            ModBlockItemIds.REINFORCED_JUNGLE_DOOR,
            properties -> new PreservedDoorBlock(BlockSetType.JUNGLE, properties),
            BlockBehaviour.Properties.of().mapColor(Blocks.JUNGLE_PLANKS.defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY)
    );
    public static Supplier<Block> REINFORCED_ACACIA_DOOR = register(
            ModBlockItemIds.REINFORCED_ACACIA_DOOR,
            properties -> new PreservedDoorBlock(BlockSetType.ACACIA, properties),
            BlockBehaviour.Properties.of().mapColor(Blocks.ACACIA_PLANKS.defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY)
    );
    public static Supplier<Block> REINFORCED_CHERRY_DOOR = register(
            ModBlockItemIds.REINFORCED_CHERRY_DOOR,
            properties -> new PreservedDoorBlock(BlockSetType.CHERRY, properties),
            BlockBehaviour.Properties.of().mapColor(Blocks.CHERRY_PLANKS.defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY)
    );
    public static Supplier<Block> REINFORCED_DARK_OAK_DOOR = register(
            ModBlockItemIds.REINFORCED_DARK_OAK_DOOR,
            properties -> new PreservedDoorBlock(BlockSetType.DARK_OAK, properties),
            BlockBehaviour.Properties.of().mapColor(Blocks.DARK_OAK_PLANKS.defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY)
    );
    public static Supplier<Block> REINFORCED_PALE_OAK_DOOR = register(
            ModBlockItemIds.REINFORCED_PALE_OAK_DOOR,
            properties -> new PreservedDoorBlock(BlockSetType.PALE_OAK, properties),
            BlockBehaviour.Properties.of().mapColor(Blocks.PALE_OAK_PLANKS.defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().pushReaction(PushReaction.DESTROY)
    );
    public static Supplier<Block> REINFORCED_MANGROVE_DOOR = register(
            ModBlockItemIds.REINFORCED_MANGROVE_DOOR,
            properties -> new PreservedDoorBlock(BlockSetType.MANGROVE, properties),
            BlockBehaviour.Properties.of().mapColor(Blocks.MANGROVE_PLANKS.defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY)
    );
    public static Supplier<Block> REINFORCED_BAMBOO_DOOR = register(
            ModBlockItemIds.REINFORCED_BAMBOO_DOOR,
            properties -> new PreservedDoorBlock(BlockSetType.BAMBOO, properties),
            BlockBehaviour.Properties.of().mapColor(Blocks.BAMBOO_PLANKS.defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY)
    );
    public static Supplier<Block> REINFORCED_CRIMSON_DOOR = register(
            ModBlockItemIds.REINFORCED_CRIMSON_DOOR,
            properties -> new PreservedDoorBlock(BlockSetType.CRIMSON, properties),
            BlockBehaviour.Properties.of().mapColor(Blocks.CRIMSON_PLANKS.defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().pushReaction(PushReaction.DESTROY)
    );
    public static Supplier<Block> REINFORCED_WARPED_DOOR = register(
            ModBlockItemIds.REINFORCED_WARPED_DOOR,
            properties -> new PreservedDoorBlock(BlockSetType.WARPED, properties),
            BlockBehaviour.Properties.of().mapColor(Blocks.WARPED_PLANKS.defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().pushReaction(PushReaction.DESTROY)
    );
    public static Supplier<Block> REINFORCED_COPPER_DOOR = register(
            ModBlockItemIds.REINFORCED_COPPER_DOOR,
            properties -> new PreservedWeatheringCopperDoorBlock(BlockSetType.COPPER, WeatheringCopper.WeatherState.UNAFFECTED, properties),
            BlockBehaviour.Properties.of().mapColor(var1x -> Blocks.COPPER_BLOCK.weathering().pick(WeatheringCopper.WeatherState.UNAFFECTED).defaultMapColor()).strength(3.0F, 6.0F).noOcclusion().pushReaction(PushReaction.DESTROY)
    );
    public static Supplier<Block> REINFORCED_EXPOSED_COPPER_DOOR = register(
            ModBlockItemIds.REINFORCED_EXPOSED_COPPER_DOOR,
            properties -> new PreservedWeatheringCopperDoorBlock(BlockSetType.COPPER, WeatheringCopper.WeatherState.EXPOSED, properties),
            BlockBehaviour.Properties.ofFullCopy(REINFORCED_COPPER_DOOR.get()).mapColor(var1x -> Blocks.COPPER_BLOCK.weathering().pick(WeatheringCopper.WeatherState.EXPOSED).defaultMapColor())
    );
    public static Supplier<Block> REINFORCED_OXIDIZED_COPPER_DOOR = register(
            ModBlockItemIds.REINFORCED_OXIDIZED_COPPER_DOOR,
            properties -> new PreservedWeatheringCopperDoorBlock(BlockSetType.COPPER, WeatheringCopper.WeatherState.OXIDIZED, properties),
            BlockBehaviour.Properties.ofFullCopy(REINFORCED_COPPER_DOOR.get()).mapColor(var1x -> Blocks.COPPER_BLOCK.weathering().pick(WeatheringCopper.WeatherState.OXIDIZED).defaultMapColor())
    );
    public static Supplier<Block> REINFORCED_WEATHERED_COPPER_DOOR = register(
            ModBlockItemIds.REINFORCED_WEATHERED_COPPER_DOOR,
            properties -> new PreservedWeatheringCopperDoorBlock(BlockSetType.COPPER, WeatheringCopper.WeatherState.WEATHERED, properties),
            BlockBehaviour.Properties.ofFullCopy(REINFORCED_COPPER_DOOR.get()).mapColor(var1x -> Blocks.COPPER_BLOCK.weathering().pick(WeatheringCopper.WeatherState.WEATHERED).defaultMapColor())
    );
    public static Supplier<Block> REINFORCED_WAXED_COPPER_DOOR = register(
            ModBlockItemIds.REINFORCED_WAXED_COPPER_DOOR,
            properties -> new PreservedDoorBlock(BlockSetType.COPPER, properties),
            BlockBehaviour.Properties.ofFullCopy(REINFORCED_COPPER_DOOR.get())
    );
    public static Supplier<Block> REINFORCED_WAXED_EXPOSED_COPPER_DOOR = register(
            ModBlockItemIds.REINFORCED_WAXED_EXPOSED_COPPER_DOOR,
            properties -> new PreservedDoorBlock(BlockSetType.COPPER, properties),
            BlockBehaviour.Properties.ofFullCopy(REINFORCED_EXPOSED_COPPER_DOOR.get())
    );
    public static Supplier<Block> REINFORCED_WAXED_OXIDIZED_COPPER_DOOR = register(
            ModBlockItemIds.REINFORCED_WAXED_OXIDIZED_COPPER_DOOR,
            properties -> new PreservedDoorBlock(BlockSetType.COPPER, properties),
            BlockBehaviour.Properties.ofFullCopy(REINFORCED_OXIDIZED_COPPER_DOOR.get())
    );
    public static Supplier<Block> REINFORCED_WAXED_WEATHERED_COPPER_DOOR = register(
            ModBlockItemIds.REINFORCED_WAXED_WEATHERED_COPPER_DOOR,
            properties -> new PreservedDoorBlock(BlockSetType.COPPER, properties),
            BlockBehaviour.Properties.ofFullCopy(REINFORCED_WEATHERED_COPPER_DOOR.get())
    );

    // fluid
    // (THESE DON'T EXIST AS ACTUAL FLUID THIS IS FOR JADE COMPAT
    public static final Block HONEY = registerFluid(
            "honey",
            properties -> new PreservedLiquidBlock(ModFluids.HONEY, properties),
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.GOLD)
                    .replaceable()
                    .noCollision()
                    .strength(100.0F)
                    .pushReaction(PushReaction.DESTROY)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
    );
    public static final Block MILK = registerFluid(
            "milk",
            properties -> new PreservedLiquidBlock(ModFluids.MILK, properties),
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.SNOW)
                    .replaceable()
                    .noCollision()
                    .strength(100.0F)
                    .pushReaction(PushReaction.DESTROY)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
    );
    public static final Block SNOW = registerFluid(
            "snow",
            properties -> new PreservedLiquidBlock(ModFluids.SNOW, properties),
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.SNOW)
                    .replaceable()
                    .noCollision()
                    .strength(100.0F)
                    .pushReaction(PushReaction.DESTROY)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
    );

    private static Supplier<Block> register(BlockItemId id, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties) {
        Supplier<Block> memoizedSupplier = new Supplier<>() {
            private Block instance;

            @Override
            public Block get() {
                if (instance == null) instance = factory.apply(properties.setId(id.block()));
                return instance;
            }
        };

        BLOCKS.put(id, new Definition(memoizedSupplier));
        return memoizedSupplier;
    }

    private static Block registerFluid(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties) {
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, Constants.id(name));
        return Registry.register(BuiltInRegistries.BLOCK, blockKey, factory.apply(properties.setId(blockKey)));
    }

    public static Map<BlockItemId, Definition> getBlocks() {
        return BLOCKS;
    }

    public record Definition(Supplier<Block> factory) {}

    public static boolean never(final BlockState state, final BlockGetter blockGetter, final BlockPos blockPos) {
        return false;
    }

    public static void registerModBlocks() {}
}
