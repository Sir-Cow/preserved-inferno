package sircow.preservedinferno.mixin;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BaseFireBlock.class)
public class BaseFireBlockMixin {
    @Redirect(method = "isPortal", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z"))
    private static boolean preserved_inferno$allowCryingObsidian(BlockState state, Object obj) {
        if (obj == Blocks.OBSIDIAN) {
            return state.is(Blocks.OBSIDIAN) || state.is(Blocks.CRYING_OBSIDIAN);
        }
        else if (obj instanceof Block block) {
            return state.is(block);
        }
        else if (obj instanceof TagKey<?> tag) {
            @SuppressWarnings("unchecked")
            TagKey<Block> blockTag = (TagKey<Block>) tag;
            return state.is(blockTag);
        }
        return false;
    }
}
