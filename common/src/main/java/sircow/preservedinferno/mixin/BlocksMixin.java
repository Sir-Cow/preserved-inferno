package sircow.preservedinferno.mixin;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

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
                .strength(6.0F, 3.0F);
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
                .strength(6.0F, 3.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=lapis_ore")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyLapisOre(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(6.0F, 3.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=diamond_ore")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyDiamondOre(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(6.0F, 3.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=redstone_ore")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyRedstoneOre(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .randomTicks()
                .lightLevel(litBlockEmission(9))
                .strength(6.0F, 3.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=emerald_ore")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyEmeraldOre(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(6.0F, 3.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=copper_ore")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyCopperOre(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE).mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(6.0F, 3.0F);
    }
    // deepslate ores
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=deepslate_gold_ore")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyDeepGoldOre(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_ORE).mapColor(MapColor.DEEPSLATE)
                .strength(12.0F, 3.0F)
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
                .strength(12.0F, 3.0F)
                .sound(SoundType.DEEPSLATE);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=deepslate_lapis_ore")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyDeepLapisOre(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.LAPIS_ORE).mapColor(MapColor.DEEPSLATE)
                .strength(12.0F, 3.0F)
                .sound(SoundType.DEEPSLATE);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=deepslate_diamond_ore")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyDeepDiamondOre(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_ORE).mapColor(MapColor.DEEPSLATE)
                .strength(12.0F, 3.0F)
                .sound(SoundType.DEEPSLATE);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=deepslate_redstone_ore")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyDeepRedstoneOre(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_ORE).mapColor(MapColor.DEEPSLATE)
                .strength(12.0F, 3.0F)
                .sound(SoundType.DEEPSLATE);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=deepslate_emerald_ore")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyDeepEmeraldOre(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.EMERALD_ORE).mapColor(MapColor.DEEPSLATE)
                .strength(12.0F, 3.0F)
                .sound(SoundType.DEEPSLATE);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=deepslate_copper_ore")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyDeepCopperOre(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_ORE).mapColor(MapColor.DEEPSLATE)
                .strength(12.0F, 3.0F)
                .sound(SoundType.DEEPSLATE);
    }

    @Shadow
    private static ToIntFunction<BlockState> litBlockEmission(int lightValue) {
        return blockState -> blockState.getValue(BlockStateProperties.LIT) ? lightValue : 0;
    }
}
