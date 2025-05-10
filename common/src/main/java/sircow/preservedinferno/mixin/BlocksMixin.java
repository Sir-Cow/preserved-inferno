package sircow.preservedinferno.mixin;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

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
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=netherrack")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", ordinal = 0), index = 2)
    private static BlockBehaviour.Properties preserved_inferno$modifyNetherrack(BlockBehaviour.Properties original) {
        return original.mapColor(MapColor.NETHER)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(1.0F)
                .sound(SoundType.NETHERRACK);
    }
}
