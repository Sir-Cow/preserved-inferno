package sircow.placeholder.block;

import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import sircow.placeholder.Placeholder;
import sircow.placeholder.block.custom.*;

import java.util.function.Function;

import static net.minecraft.block.MapColor.ORANGE;

public class ModBlocks {
    public static final Block INDUCTOR_RAIL = register("inductor_rail",
            settings -> new InductorRailBlock(Oxidizable.OxidationLevel.UNAFFECTED, settings), AbstractBlock.Settings.copy(Blocks.RAIL).noCollision().strength(0.7F).mapColor(ORANGE).sounds(BlockSoundGroup.COPPER));
    public static final Block EXPOSED_INDUCTOR_RAIL = register("exposed_inductor_rail",
            settings -> new InductorRailBlock(Oxidizable.OxidationLevel.EXPOSED, settings), AbstractBlock.Settings.copy(Blocks.RAIL).noCollision().strength(0.7F).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).sounds(BlockSoundGroup.COPPER));
    public static final Block WEATHERED_INDUCTOR_RAIL = register("weathered_inductor_rail",
            settings -> new InductorRailBlock(Oxidizable.OxidationLevel.WEATHERED, settings), AbstractBlock.Settings.copy(Blocks.RAIL).noCollision().strength(0.7F).mapColor(MapColor.DARK_AQUA).sounds(BlockSoundGroup.COPPER));
    public static final Block OXIDIZED_INDUCTOR_RAIL = register("oxidized_inductor_rail",
            settings -> new InductorRailBlock(Oxidizable.OxidationLevel.OXIDIZED, settings), AbstractBlock.Settings.copy(Blocks.RAIL).noCollision().strength(0.7F).mapColor(MapColor.TEAL).sounds(BlockSoundGroup.COPPER));
    public static final Block WAXED_INDUCTOR_RAIL = register("waxed_inductor_rail",
            settings -> new InductorRailBlock(Oxidizable.OxidationLevel.UNAFFECTED, settings), AbstractBlock.Settings.copy(Blocks.RAIL).noCollision().strength(0.7F).mapColor(ORANGE).sounds(BlockSoundGroup.COPPER));
    public static final Block WAXED_EXPOSED_INDUCTOR_RAIL = register("waxed_exposed_inductor_rail",
            settings -> new InductorRailBlock(Oxidizable.OxidationLevel.EXPOSED, settings), AbstractBlock.Settings.copy(Blocks.RAIL).noCollision().strength(0.7F).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).sounds(BlockSoundGroup.COPPER));
    public static final Block WAXED_WEATHERED_INDUCTOR_RAIL = register("waxed_weathered_inductor_rail",
            settings -> new InductorRailBlock(Oxidizable.OxidationLevel.WEATHERED, settings), AbstractBlock.Settings.copy(Blocks.RAIL).noCollision().strength(0.7F).mapColor(MapColor.DARK_AQUA).sounds(BlockSoundGroup.COPPER));
    public static final Block WAXED_OXIDIZED_INDUCTOR_RAIL = register("waxed_oxidized_inductor_rail",
            settings -> new InductorRailBlock(Oxidizable.OxidationLevel.OXIDIZED, settings), AbstractBlock.Settings.copy(Blocks.RAIL).noCollision().strength(0.7F).mapColor(MapColor.TEAL).sounds(BlockSoundGroup.COPPER));

    public static final Block NEW_CAULDRON_BLOCK = register("new_cauldron",
            NewCauldronBlock::new, AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).requiresTool().strength(2.0F).nonOpaque());
    public static final Block NEW_LOOM_BLOCK = register("new_loom",
            NewLoomBlock::new, AbstractBlock.Settings.create().mapColor(MapColor.OAK_TAN).instrument(NoteBlockInstrument.BASS).strength(2.5F).sounds(BlockSoundGroup.WOOD).burnable());
    public static final Block NEW_FLETCHING_TABLE_BLOCK = register("new_fletching_table",
            NewFletchingTableBlock::new, AbstractBlock.Settings.create().mapColor(MapColor.OAK_TAN).instrument(NoteBlockInstrument.BASS).strength(2.5F).sounds(BlockSoundGroup.WOOD).burnable());
    public static final Block NEW_ENCHANTING_TABLE_BLOCK = register("new_enchanting_table",
            NewEnchantingTableBlock::new, AbstractBlock.Settings.create().mapColor(MapColor.RED).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().luminance(state -> 7).strength(5.0F, 1200.0F));

    public static void initialize() {
        OxidizableBlocksRegistry.registerOxidizableBlockPair(ModBlocks.INDUCTOR_RAIL, ModBlocks.EXPOSED_INDUCTOR_RAIL);
        OxidizableBlocksRegistry.registerOxidizableBlockPair(ModBlocks.EXPOSED_INDUCTOR_RAIL, ModBlocks.WEATHERED_INDUCTOR_RAIL);
        OxidizableBlocksRegistry.registerOxidizableBlockPair(ModBlocks.WEATHERED_INDUCTOR_RAIL, ModBlocks.OXIDIZED_INDUCTOR_RAIL);
        OxidizableBlocksRegistry.registerWaxableBlockPair(ModBlocks.INDUCTOR_RAIL, ModBlocks.WAXED_INDUCTOR_RAIL);
        OxidizableBlocksRegistry.registerWaxableBlockPair(ModBlocks.EXPOSED_INDUCTOR_RAIL, ModBlocks.WAXED_EXPOSED_INDUCTOR_RAIL);
        OxidizableBlocksRegistry.registerWaxableBlockPair(ModBlocks.WEATHERED_INDUCTOR_RAIL, ModBlocks.WAXED_WEATHERED_INDUCTOR_RAIL);
        OxidizableBlocksRegistry.registerWaxableBlockPair(ModBlocks.OXIDIZED_INDUCTOR_RAIL, ModBlocks.WAXED_OXIDIZED_INDUCTOR_RAIL);
    }

    private static Block register(String path, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        return Blocks.register(keyOf(path), factory, settings);
    }

    private static RegistryKey<Block> keyOf(String path) {
        return RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Placeholder.MOD_ID, path));
    }

    public static void registerModBlocks(){
        Placeholder.LOGGER.info("Registering Mod Blocks for " + Placeholder.MOD_ID);
    }
}
