package sircow.preservedinferno.mixin;

import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Blocks.class)
public class BlocksMixin {
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=prismarine")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;strength(FF)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 0), index = 1)
    private static float preserved_inferno$modifyPrismarineBlastRes(float old) { return 30.0F; }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=prismarine_bricks")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;strength(FF)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 0), index = 1)
    private static float preserved_inferno$modifyPrismarineBricksBlastRes(float old) { return 30.0F; }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=dark_prismarine")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;strength(FF)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 0), index = 1)
    private static float preserved_inferno$modifyDarkPrismarineBlastRes(float old) { return 30.0F; }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=prismarine_slab")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;strength(FF)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 0), index = 1)
    private static float preserved_inferno$modifyPrismarineSlabBlastRes(float old) { return 30.0F; }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=prismarine_brick_slab")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;strength(FF)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 0), index = 1)
    private static float preserved_inferno$modifyPrismarineBrickSlabBlastRes(float old) { return 30.0F; }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=dark_prismarine_slab")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;strength(FF)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 0), index = 1)
    private static float preserved_inferno$modifyDarkPrismarineSlabBlastRes(float old) { return 30.0F; }
}
