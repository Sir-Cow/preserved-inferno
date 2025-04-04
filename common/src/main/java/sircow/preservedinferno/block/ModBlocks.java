package sircow.preservedinferno.block;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.block.custom.*;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {
    public static final Block INDUCTOR_RAIL = register("inductor_rail",
            settings -> new InductorRailBlock(WeatheringCopper.WeatherState.UNAFFECTED, settings), BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).noCollission().strength(0.7F).sound(SoundType.COPPER));
    public static final Block EXPOSED_INDUCTOR_RAIL = register("exposed_inductor_rail",
            settings -> new InductorRailBlock(WeatheringCopper.WeatherState.EXPOSED, settings), BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).noCollission().strength(0.7F).sound(SoundType.COPPER));
    public static final Block WEATHERED_INDUCTOR_RAIL = register("weathered_inductor_rail",
            settings -> new InductorRailBlock(WeatheringCopper.WeatherState.WEATHERED, settings), BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_STEM).noCollission().strength(0.7F).sound(SoundType.COPPER));
    public static final Block OXIDIZED_INDUCTOR_RAIL = register("oxidized_inductor_rail",
            settings -> new InductorRailBlock(WeatheringCopper.WeatherState.OXIDIZED, settings), BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_NYLIUM).noCollission().strength(0.7F).sound(SoundType.COPPER));
    public static final Block WAXED_INDUCTOR_RAIL = register("waxed_inductor_rail",
            settings -> new InductorRailBlock(WeatheringCopper.WeatherState.UNAFFECTED, settings), BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).noCollission().strength(0.7F).sound(SoundType.COPPER));
    public static final Block WAXED_EXPOSED_INDUCTOR_RAIL = register("waxed_exposed_inductor_rail",
            settings -> new InductorRailBlock(WeatheringCopper.WeatherState.EXPOSED, settings), BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).noCollission().strength(0.7F).sound(SoundType.COPPER));
    public static final Block WAXED_WEATHERED_INDUCTOR_RAIL = register("waxed_weathered_inductor_rail",
            settings -> new InductorRailBlock(WeatheringCopper.WeatherState.WEATHERED, settings), BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_NYLIUM).noCollission().strength(0.7F).sound(SoundType.COPPER));
    public static final Block WAXED_OXIDIZED_INDUCTOR_RAIL = register("waxed_oxidized_inductor_rail",
            settings -> new InductorRailBlock(WeatheringCopper.WeatherState.OXIDIZED, settings), BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).noCollission().strength(0.7F).sound(SoundType.COPPER));

    public static void initialize() {
        Supplier<BiMap<Block, Block>> NEXT_BY_BLOCK = Suppliers.memoize(
                () -> ImmutableBiMap.<Block, Block>builder()
                        .put(ModBlocks.INDUCTOR_RAIL, ModBlocks.EXPOSED_INDUCTOR_RAIL)
                        .put(ModBlocks.EXPOSED_INDUCTOR_RAIL, ModBlocks.WEATHERED_INDUCTOR_RAIL)
                        .put(ModBlocks.WEATHERED_INDUCTOR_RAIL, ModBlocks.OXIDIZED_INDUCTOR_RAIL)
                        .build()
        );
        Supplier<BiMap<Block, Block>> PREVIOUS_BY_BLOCK = Suppliers.memoize(() -> NEXT_BY_BLOCK.get().inverse());
    }

    private static Block register(ResourceKey<Block> resourceKey, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties) {
        Block block = factory.apply(properties.setId(resourceKey));
        return Registry.register(BuiltInRegistries.BLOCK, resourceKey, block);
    }

    private static Block register(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties) {
        return register(keyOf(name), factory, properties);
    }

    private static ResourceKey<Block> keyOf(String name) {
        return ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
    }

    private static Block register(String name, BlockBehaviour.Properties properties) {
        return register(name, Block::new, properties);
    }

    static {
        for (Block block : BuiltInRegistries.BLOCK) {
            for (BlockState blockstate : block.getStateDefinition().getPossibleStates()) {
                Block.BLOCK_STATE_REGISTRY.add(blockstate);
                blockstate.initCache();
            }
        }
    }

    public static void registerModBlocks(){
        Constants.LOG.info("Registering Mod Blocks for " + Constants.MOD_ID);
    }
}
