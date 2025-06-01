package sircow.preservedinferno.mixin;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import sircow.preservedinferno.block.ModBlocks;

@Mixin(LiquidBlock.class)
public class LiquidBlockMixin {
    // replace obsidian with block of choice
    @ModifyArg(
            method = "shouldSpreadLiquid(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z",
                    ordinal = 0
            ),
            index = 1
    )
    private BlockState preserved_blizzard$replaceObsidian(BlockState originalState) {
        if (originalState.getBlock() == Blocks.OBSIDIAN) {
            return ModBlocks.RHYOLITE.defaultBlockState();
        }
        return originalState;
    }
}
