package sircow.preservedinferno.mixin;

import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BaseFireBlock.class)
public class BaseFireBlockMixin {
    @Redirect(method = "isPortal", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    private static boolean preserved_inferno$allowCryingObsidian(BlockState state, Block block) {
        if (block == Blocks.OBSIDIAN) {
            return state.is(Blocks.OBSIDIAN) || state.is(Blocks.CRYING_OBSIDIAN);
        }
        return state.is(block);
    }
}
