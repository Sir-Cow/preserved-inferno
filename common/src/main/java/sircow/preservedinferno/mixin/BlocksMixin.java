package sircow.preservedinferno.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import sircow.preservedinferno.block.ModBlockProperties;

import java.util.function.ToIntFunction;

@Mixin(Blocks.class)
public class BlocksMixin {
    // prismarine
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;PRISMARINE:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 1)
    private static BlockBehaviour.Properties pinferno$modifyPrismarine(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.COLOR_CYAN)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .pushReaction(PushReaction.BLOCK)
                .strength(50.0F, 30.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;PRISMARINE_BRICKS:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 1)
    private static BlockBehaviour.Properties pinferno$modifyPrismarineBricks(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.DIAMOND)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .pushReaction(PushReaction.BLOCK)
                .strength(50.0F, 30.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;DARK_PRISMARINE:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 1)
    private static BlockBehaviour.Properties pinferno$modifyDarkPrismarine(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.DIAMOND)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .pushReaction(PushReaction.BLOCK)
                .strength(50.0F, 30.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;PRISMARINE_SLAB:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyPrismarineSlab(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.COLOR_CYAN)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .pushReaction(PushReaction.BLOCK)
                .strength(50.0F, 30.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;PRISMARINE_BRICK_SLAB:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyPrismarineBrickSlab(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.DIAMOND)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .pushReaction(PushReaction.BLOCK)
                .strength(50.0F, 30.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;DARK_PRISMARINE_SLAB:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyDarkPrismarineSlab(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.DIAMOND)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .pushReaction(PushReaction.BLOCK)
                .strength(50.0F, 30.0F);
    }
    // netherrack
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;NETHERRACK:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyNetherrack(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.NETHER)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(1.0F)
                .sound(SoundType.NETHERRACK);
    }
    // ores
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;GOLD_ORE:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyGoldOre(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(7.0F, 3.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;IRON_ORE:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyIronOre(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(6.0F, 3.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;COAL_ORE:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyCoalOre(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(5.0F, 3.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;LAPIS_ORE:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyLapisOre(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(5.0F, 3.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;DIAMOND_ORE:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyDiamondOre(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(8.0F, 3.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;REDSTONE_ORE:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyRedstoneOre(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .randomTicks()
                .lightLevel(litBlockEmission(9))
                .strength(7.0F, 3.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;EMERALD_ORE:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyEmeraldOre(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(8.0F, 3.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;COPPER_ORE:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyCopperOre(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE).mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(4.0F, 3.0F);
    }
    // deepslate ores
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;DEEPSLATE_GOLD_ORE:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyDeepGoldOre(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_ORE).mapColor(MapColor.DEEPSLATE)
                .strength(10.5F, 3.0F)
                .sound(SoundType.DEEPSLATE);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;DEEPSLATE_IRON_ORE:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyDeepIronOre(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE).mapColor(MapColor.DEEPSLATE)
                .strength(9.0F, 3.0F)
                .sound(SoundType.DEEPSLATE);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;DEEPSLATE_COAL_ORE:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyDeepCoalOre(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.COAL_ORE).mapColor(MapColor.DEEPSLATE)
                .strength(7.5F, 3.0F)
                .sound(SoundType.DEEPSLATE);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;DEEPSLATE_LAPIS_ORE:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyDeepLapisOre(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.LAPIS_ORE).mapColor(MapColor.DEEPSLATE)
                .strength(7.5F, 3.0F)
                .sound(SoundType.DEEPSLATE);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;DEEPSLATE_DIAMOND_ORE:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyDeepDiamondOre(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_ORE).mapColor(MapColor.DEEPSLATE)
                .strength(12.0F, 3.0F)
                .sound(SoundType.DEEPSLATE);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;DEEPSLATE_REDSTONE_ORE:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyDeepRedstoneOre(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_ORE).mapColor(MapColor.DEEPSLATE)
                .strength(10.5F, 3.0F)
                .sound(SoundType.DEEPSLATE);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;DEEPSLATE_EMERALD_ORE:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyDeepEmeraldOre(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.EMERALD_ORE).mapColor(MapColor.DEEPSLATE)
                .strength(12.0F, 3.0F)
                .sound(SoundType.DEEPSLATE);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;DEEPSLATE_COPPER_ORE:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyDeepCopperOre(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_ORE).mapColor(MapColor.DEEPSLATE)
                .strength(6.0F, 3.0F)
                .sound(SoundType.DEEPSLATE);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;NETHER_GOLD_ORE:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyNetherGoldOre(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.NETHER)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(8.0F, 3.0F)
                .sound(SoundType.NETHER_GOLD_ORE);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;NETHER_QUARTZ_ORE:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyNetherQuartzOre(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.NETHER)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(8.0F, 3.0F)
                .sound(SoundType.NETHER_ORE);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;WHEAT_CROP:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyCrop(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.of()
                .mapColor(blockStatex -> blockStatex.getValue(CropBlock.AGE) >= 6 ? MapColor.COLOR_YELLOW : MapColor.PLANT)
                .noCollision()
                .randomTicks()
                .strength(0.25F, 0.0F)
                .sound(SoundType.CROP)
                .pushReaction(PushReaction.DESTROY);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;BEETROOT_CROP:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyCrop2(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollision()
                .randomTicks()
                .strength(0.25F, 0.0F)
                .sound(SoundType.CROP)
                .pushReaction(PushReaction.DESTROY);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;CARROT_CROP:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyCrop3(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollision()
                .randomTicks()
                .strength(0.25F, 0.0F)
                .sound(SoundType.CROP)
                .pushReaction(PushReaction.DESTROY);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;POTATO_CROP:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyCrop4(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollision()
                .randomTicks()
                .strength(0.25F, 0.0F)
                .sound(SoundType.CROP)
                .pushReaction(PushReaction.DESTROY);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;NETHER_WART:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyCrop5(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_RED)
                .noCollision()
                .strength(0.25F, 0.0F)
                .sound(SoundType.NETHER_WART)
                .pushReaction(PushReaction.DESTROY);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;SUGAR_CANE:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyCrop6(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollision()
                .randomTicks()
                .strength(0.25F, 0.0F)
                .sound(SoundType.GRASS)
                .pushReaction(PushReaction.DESTROY);
    }

    @ModifyVariable(method = "register(Lnet/minecraft/resources/ResourceKey;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", at = @At("HEAD"), argsOnly = true, name = "properties")
    private static BlockBehaviour.Properties pinferno$modifyCrop7(BlockBehaviour.Properties properties, @Local(argsOnly = true, name = "id") ResourceKey<Block> id) {
        String blockId = id.identifier().getPath();

        if ("attached_pumpkin_stem".equals(blockId) || "attached_melon_stem".equals(blockId)) {
            return properties
                    .mapColor(MapColor.PLANT)
                    .noCollision()
                    .strength(0.25F, 0.0F)
                    .sound(SoundType.WOOD)
                    .pushReaction(PushReaction.DESTROY);
        }
        else if ("pumpkin_stem".equals(blockId) || "melon_stem".equals(blockId)) {
            return properties
                    .mapColor(MapColor.PLANT)
                    .noCollision()
                    .randomTicks()
                    .strength(0.25F, 0.0F)
                    .sound(SoundType.HARD_CROP)
                    .pushReaction(PushReaction.DESTROY);
        }
        return properties;
    }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;POWDER_SNOW:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyPowderSnow(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.SNOW)
                .strength(1.0F)
                .sound(SoundType.POWDER_SNOW)
                .dynamicShape()
                .noOcclusion()
                .isRedstoneConductor(BlocksMixin::never);
    }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;CREAKING_HEART:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyCreakingHeart(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.COLOR_ORANGE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .strength(22.5F)
                .sound(SoundType.CREAKING_HEART);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;PALE_OAK_WOOD:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyPaleOakWood(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASS)
                .strength(5.0F)
                .sound(SoundType.WOOD);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;PALE_OAK_PLANKS:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 1)
    private static BlockBehaviour.Properties pinferno$modifyPaleOakPlanks(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.QUARTZ)
                .instrument(NoteBlockInstrument.BASS)
                .strength(5.0F, 3.0F)
                .sound(SoundType.WOOD);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;PALE_OAK_LOG:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyPaleOakLog(BlockBehaviour.Properties original) {
        return original.mapColor(state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? Blocks.PALE_OAK_PLANKS.defaultMapColor() : Blocks.PALE_OAK_WOOD.defaultMapColor())
                .instrument(NoteBlockInstrument.BASS)
                .strength(5.0F)
                .sound(SoundType.WOOD);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;STRIPPED_PALE_OAK_LOG:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyStrippedPaleOakLog(BlockBehaviour.Properties original) {
        return original.mapColor(Blocks.PALE_OAK_PLANKS.defaultMapColor())
                .instrument(NoteBlockInstrument.BASS)
                .strength(5.0F)
                .sound(SoundType.WOOD);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;STRIPPED_PALE_OAK_WOOD:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyStrippedPaleOakWood(BlockBehaviour.Properties original) {
        return original.mapColor(Blocks.PALE_OAK_PLANKS.defaultMapColor())
                .instrument(NoteBlockInstrument.BASS)
                .strength(5.0F)
                .sound(SoundType.WOOD);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;PALE_OAK_SHELF:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyPaleOakShelf(BlockBehaviour.Properties original) {
        return original.mapColor(Blocks.PALE_OAK_PLANKS.defaultMapColor())
                .instrument(NoteBlockInstrument.BASS)
                .sound(SoundType.SHELF)
                .strength(5.0F, 3.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;PALE_OAK_SIGN:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyPaleOakSign(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.of()
                .mapColor(Blocks.PALE_OAK_PLANKS.defaultMapColor())
                .forceSolidOn()
                .instrument(NoteBlockInstrument.BASS)
                .noCollision()
                .strength(1.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockIds;PALE_OAK_WALL_SIGN:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyPaleOakWallSign(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.of()
                .mapColor(Blocks.PALE_OAK_PLANKS.defaultMapColor())
                .forceSolidOn()
                .instrument(NoteBlockInstrument.BASS)
                .noCollision()
                .strength(1.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;PALE_OAK_HANGING_SIGN:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyPaleOakHangingSign(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.of()
                .mapColor(Blocks.PALE_OAK_PLANKS.defaultMapColor())
                .forceSolidOn()
                .instrument(NoteBlockInstrument.BASS)
                .noCollision()
                .strength(1.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockIds;PALE_OAK_WALL_HANGING_SIGN:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyPaleOakWallHangingSign(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.of()
                .mapColor(Blocks.PALE_OAK_PLANKS.defaultMapColor())
                .forceSolidOn()
                .instrument(NoteBlockInstrument.BASS)
                .noCollision()
                .strength(1.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;PALE_OAK_PRESSURE_PLATE:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyPaleOakPressurePlate(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.of()
                .mapColor(Blocks.PALE_OAK_PLANKS.defaultMapColor())
                .forceSolidOn()
                .instrument(NoteBlockInstrument.BASS)
                .noCollision()
                .strength(0.5F)
                .pushReaction(PushReaction.DESTROY);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;PALE_OAK_TRAPDOOR:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyPaleOakTrapdoor(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.of()
                .mapColor(Blocks.PALE_OAK_PLANKS.defaultMapColor())
                .instrument(NoteBlockInstrument.BASS)
                .strength(3.0F)
                .noOcclusion()
                .isValidSpawn(BlocksMixin::never);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;PALE_OAK_SLAB:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyPaleOakSlab(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.of()
                .mapColor(Blocks.PALE_OAK_PLANKS.defaultMapColor())
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F, 3.0F)
                .sound(SoundType.WOOD);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;PALE_OAK_FENCE_GATE:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyPaleOakFenceGate(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.of()
                .mapColor(Blocks.PALE_OAK_PLANKS.defaultMapColor())
                .forceSolidOn()
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F, 3.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;PALE_OAK_FENCE:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyPaleOakFence(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.of()
                .mapColor(Blocks.PALE_OAK_PLANKS.defaultMapColor())
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F, 3.0F)
                .sound(SoundType.WOOD);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;PALE_OAK_DOOR:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyPaleOakDoor(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.of()
                .mapColor(Blocks.PALE_OAK_PLANKS.defaultMapColor())
                .instrument(NoteBlockInstrument.BASS)
                .strength(3.0F)
                .noOcclusion()
                .pushReaction(PushReaction.DESTROY);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;PALE_OAK_LEAVES:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyPaleOakLeaves(BlockBehaviour.Properties original) {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .strength(0.2F)
                .randomTicks()
                .sound(SoundType.GRASS)
                .noOcclusion()
                .isValidSpawn(BlocksMixin::ocelotOrParrot)
                .isSuffocating(BlocksMixin::never)
                .isViewBlocking(BlocksMixin::never)
                .pushReaction(PushReaction.DESTROY)
                .isRedstoneConductor(BlocksMixin::never);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;PALE_MOSS_BLOCK:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyPaleMossBlock(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.COLOR_LIGHT_GRAY)
                .strength(0.1F)
                .sound(SoundType.MOSS)
                .pushReaction(PushReaction.DESTROY);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;PALE_MOSS_CARPET:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyPaleMossCarpet(BlockBehaviour.Properties original) {
        return original.mapColor(Blocks.PALE_MOSS_BLOCK.defaultMapColor())
                .strength(0.1F)
                .sound(SoundType.MOSS_CARPET)
                .pushReaction(PushReaction.DESTROY)
                .noOcclusion();
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;PALE_HANGING_MOSS:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyPaleHangingMoss(BlockBehaviour.Properties original) {
        return original.mapColor(Blocks.PALE_MOSS_BLOCK.defaultMapColor())
                .mapColor(Blocks.PALE_MOSS_BLOCK.defaultMapColor())
                .noCollision()
                .sound(SoundType.MOSS_CARPET)
                .pushReaction(PushReaction.DESTROY);
    }
    // other
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;CAULDRON:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyCauldron(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.STONE)
                .requiresCorrectToolForDrops()
                .strength(2.0F)
                .noOcclusion()
                .lightLevel(state -> state.hasProperty(ModBlockProperties.IS_LIT) && state.getValue(ModBlockProperties.IS_LIT) ? 15 : 0);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;MANGROVE_ROOTS:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties pinferno$modifyMangroveRoots(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.PODZOL)
                .instrument(NoteBlockInstrument.BASS)
                .strength(0.5F)
                .sound(SoundType.MANGROVE_ROOTS)
                .noOcclusion()
                .isSuffocating(BlocksMixin::never)
                .isViewBlocking(BlocksMixin::never)
                .noOcclusion()
                .ignitedByLava();
    }
    @Redirect(method = "shulkerBoxProperties", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;strength(F)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;"))
    private static BlockBehaviour.Properties pInferno$modifyShulkerBoxStrength(BlockBehaviour.Properties properties, float destroyTime) {
        return properties.strength(0.5F);
    }

    @Shadow
    private static ToIntFunction<BlockState> litBlockEmission(int lightEmission) {
        return blockState -> blockState.getValue(BlockStateProperties.LIT) ? lightEmission : 0;
    }

    @Shadow
    private static Boolean ocelotOrParrot(final BlockState state, final BlockGetter blockGetter, final BlockPos blockPos, final EntityType<?> entityType) {
        return entityType == EntityTypes.OCELOT || entityType == EntityTypes.PARROT;
    }

    @Shadow
    private static boolean never(BlockState state, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }

    @Shadow
    private static Boolean never(final BlockState state, final BlockGetter blockGetter, final BlockPos blockPos, final EntityType<?> entityType) {
        return false;
    }
}
