package sircow.preservedinferno.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

import java.util.function.ToIntFunction;

@Mixin(Blocks.class)
public class BlocksMixin {
    // prismarine
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=prismarine")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 1)
    private static BlockBehaviour.Properties preserved_inferno$modifyPrismarine(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.COLOR_CYAN)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .pushReaction(PushReaction.BLOCK)
                .strength(50.0F, 30.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=prismarine_bricks")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 1)
    private static BlockBehaviour.Properties preserved_inferno$modifyPrismarineBricks(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.DIAMOND)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .pushReaction(PushReaction.BLOCK)
                .strength(50.0F, 30.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=dark_prismarine")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 1)
    private static BlockBehaviour.Properties preserved_inferno$modifyDarkPrismarine(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.DIAMOND)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .pushReaction(PushReaction.BLOCK)
                .strength(50.0F, 30.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=prismarine_slab")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyPrismarineSlab(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.COLOR_CYAN)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .pushReaction(PushReaction.BLOCK)
                .strength(50.0F, 30.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=prismarine_brick_slab")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyPrismarineBrickSlab(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.DIAMOND)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .pushReaction(PushReaction.BLOCK)
                .strength(50.0F, 30.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=dark_prismarine_slab")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyDarkPrismarineSlab(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.DIAMOND)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .pushReaction(PushReaction.BLOCK)
                .strength(50.0F, 30.0F);
    }
    // netherrack
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=netherrack")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyNetherrack(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.NETHER)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(1.0F)
                .sound(SoundType.NETHERRACK);
    }
    // ores
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=gold_ore")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyGoldOre(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(7.0F, 3.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=iron_ore")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyIronOre(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(6.0F, 3.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=coal_ore")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyCoalOre(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(4.0F, 3.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=lapis_ore")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyLapisOre(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(5.0F, 3.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=diamond_ore")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyDiamondOre(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(7.5F, 3.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=redstone_ore")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyRedstoneOre(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .randomTicks()
                .lightLevel(litBlockEmission(9))
                .strength(7.0F, 3.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=emerald_ore")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyEmeraldOre(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(7.5F, 3.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=copper_ore")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyCopperOre(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE).mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(4.0F, 3.0F);
    }
    // deepslate ores
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=deepslate_gold_ore")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyDeepGoldOre(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_ORE).mapColor(MapColor.DEEPSLATE)
                .strength(14.0F, 3.0F)
                .sound(SoundType.DEEPSLATE);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=deepslate_iron_ore")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyDeepIronOre(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE).mapColor(MapColor.DEEPSLATE)
                .strength(12.0F, 3.0F)
                .sound(SoundType.DEEPSLATE);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=deepslate_coal_ore")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyDeepCoalOre(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.COAL_ORE).mapColor(MapColor.DEEPSLATE)
                .strength(8.0F, 3.0F)
                .sound(SoundType.DEEPSLATE);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=deepslate_lapis_ore")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyDeepLapisOre(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.LAPIS_ORE).mapColor(MapColor.DEEPSLATE)
                .strength(10.0F, 3.0F)
                .sound(SoundType.DEEPSLATE);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=deepslate_diamond_ore")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyDeepDiamondOre(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_ORE).mapColor(MapColor.DEEPSLATE)
                .strength(15.0F, 3.0F)
                .sound(SoundType.DEEPSLATE);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=deepslate_redstone_ore")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyDeepRedstoneOre(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_ORE).mapColor(MapColor.DEEPSLATE)
                .strength(14.0F, 3.0F)
                .sound(SoundType.DEEPSLATE);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=deepslate_emerald_ore")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyDeepEmeraldOre(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.EMERALD_ORE).mapColor(MapColor.DEEPSLATE)
                .strength(15.0F, 3.0F)
                .sound(SoundType.DEEPSLATE);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=deepslate_copper_ore")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyDeepCopperOre(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_ORE).mapColor(MapColor.DEEPSLATE)
                .strength(8.0F, 3.0F)
                .sound(SoundType.DEEPSLATE);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=nether_gold_ore")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyNetherGoldOre(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.NETHER)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(8.0F, 3.0F)
                .sound(SoundType.NETHER_GOLD_ORE);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=nether_quartz_ore")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyNetherQuartzOre(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.NETHER)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(8.0F, 3.0F)
                .sound(SoundType.NETHER_ORE);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=ancient_debris")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 1)
    private static BlockBehaviour.Properties preserved_inferno$modifyAncientDebris(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.COLOR_BLACK)
                .requiresCorrectToolForDrops()
                .strength(20.0F, 1200.0F)
                .sound(SoundType.ANCIENT_DEBRIS);
    }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=wheat")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyCrop(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.of()
                .mapColor(blockStatex -> blockStatex.getValue(CropBlock.AGE) >= 6 ? MapColor.COLOR_YELLOW : MapColor.PLANT)
                .noCollission()
                .randomTicks()
                .strength(0.1F, 0.0F)
                .sound(SoundType.CROP)
                .pushReaction(PushReaction.DESTROY);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=beetroots")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyCrop2(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .randomTicks()
                .strength(0.1F, 0.0F)
                .sound(SoundType.CROP)
                .pushReaction(PushReaction.DESTROY);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=carrots")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyCrop3(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .randomTicks()
                .strength(0.1F, 0.0F)
                .sound(SoundType.CROP)
                .pushReaction(PushReaction.DESTROY);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=potatoes")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyCrop4(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .randomTicks()
                .strength(0.1F, 0.0F)
                .sound(SoundType.CROP)
                .pushReaction(PushReaction.DESTROY);
    }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=nether_wart")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyCrop5(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_RED)
                .noCollission()
                .strength(0.1F, 0.0F)
                .sound(SoundType.NETHER_WART)
                .pushReaction(PushReaction.DESTROY);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=sugar_cane")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyCrop6(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .randomTicks()
                .strength(0.1F, 0.0F)
                .sound(SoundType.GRASS)
                .pushReaction(PushReaction.DESTROY);
    }

    @ModifyVariable(method = "register(Lnet/minecraft/resources/ResourceKey;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", at = @At("HEAD"), index = 2, argsOnly = true)
    private static BlockBehaviour.Properties preserved_inferno$modifyCrop7(BlockBehaviour.Properties properties, @Local(argsOnly = true, ordinal = 0) ResourceKey<Block> resourceKey) {
        String blockId = resourceKey.location().getPath();

        if ("attached_pumpkin_stem".equals(blockId) || "attached_melon_stem".equals(blockId)) {
            return properties
                    .mapColor(MapColor.PLANT)
                    .noCollission()
                    .strength(0.1F, 0.0F)
                    .sound(SoundType.WOOD)
                    .pushReaction(PushReaction.DESTROY);
        }
        else if ("pumpkin_stem".equals(blockId) || "melon_stem".equals(blockId)) {
            return properties
                    .mapColor(MapColor.PLANT)
                    .noCollission()
                    .randomTicks()
                    .strength(0.1F, 0.0F)
                    .sound(SoundType.HARD_CROP)
                    .pushReaction(PushReaction.DESTROY);
        }

        return properties;
    }

    @Shadow
    private static ToIntFunction<BlockState> litBlockEmission(int lightValue) {
        return blockState -> blockState.getValue(BlockStateProperties.LIT) ? lightValue : 0;
    }
}
