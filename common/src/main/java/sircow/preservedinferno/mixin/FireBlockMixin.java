package sircow.preservedinferno.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.block.ModBlocks;
import sircow.preservedinferno.block.custom.BoomBoxBlock;

@Mixin(FireBlock.class)
public class FireBlockMixin {
    @Inject(method = "bootStrap", at = @At("TAIL"))
    private static void pinferno$modifyFlammableBlocks(CallbackInfo ci) {
        FireBlockAccessor accessor = (FireBlockAccessor) Blocks.FIRE;

        accessor.pinferno$getIgniteOdds().removeInt(Blocks.PALE_OAK_PLANKS);
        accessor.pinferno$getBurnOdds().removeInt(Blocks.PALE_OAK_PLANKS);
        accessor.pinferno$getIgniteOdds().removeInt(Blocks.PALE_OAK_SLAB);
        accessor.pinferno$getBurnOdds().removeInt(Blocks.PALE_OAK_SLAB);
        accessor.pinferno$getIgniteOdds().removeInt(Blocks.PALE_OAK_FENCE_GATE);
        accessor.pinferno$getBurnOdds().removeInt(Blocks.PALE_OAK_FENCE_GATE);
        accessor.pinferno$getIgniteOdds().removeInt(Blocks.PALE_OAK_FENCE);
        accessor.pinferno$getBurnOdds().removeInt(Blocks.PALE_OAK_FENCE);
        accessor.pinferno$getIgniteOdds().removeInt(Blocks.PALE_OAK_STAIRS);
        accessor.pinferno$getBurnOdds().removeInt(Blocks.PALE_OAK_STAIRS);
        accessor.pinferno$getIgniteOdds().removeInt(Blocks.PALE_OAK_LOG);
        accessor.pinferno$getBurnOdds().removeInt(Blocks.PALE_OAK_LOG);
        accessor.pinferno$getIgniteOdds().removeInt(Blocks.STRIPPED_PALE_OAK_LOG);
        accessor.pinferno$getBurnOdds().removeInt(Blocks.STRIPPED_PALE_OAK_LOG);
        accessor.pinferno$getIgniteOdds().removeInt(Blocks.PALE_OAK_WOOD);
        accessor.pinferno$getBurnOdds().removeInt(Blocks.PALE_OAK_WOOD);
        accessor.pinferno$getIgniteOdds().removeInt(Blocks.STRIPPED_PALE_OAK_WOOD);
        accessor.pinferno$getBurnOdds().removeInt(Blocks.STRIPPED_PALE_OAK_WOOD);
        accessor.pinferno$getIgniteOdds().removeInt(Blocks.PALE_OAK_SHELF);
        accessor.pinferno$getBurnOdds().removeInt(Blocks.PALE_OAK_SHELF);
        accessor.pinferno$getIgniteOdds().removeInt(Blocks.PALE_OAK_LEAVES);
        accessor.pinferno$getBurnOdds().removeInt(Blocks.PALE_OAK_LEAVES);
        accessor.pinferno$getIgniteOdds().removeInt(Blocks.PALE_MOSS_BLOCK);
        accessor.pinferno$getBurnOdds().removeInt(Blocks.PALE_MOSS_BLOCK);
        accessor.pinferno$getIgniteOdds().removeInt(Blocks.PALE_MOSS_CARPET);
        accessor.pinferno$getBurnOdds().removeInt(Blocks.PALE_MOSS_CARPET);
        accessor.pinferno$getIgniteOdds().removeInt(Blocks.PALE_HANGING_MOSS);
        accessor.pinferno$getBurnOdds().removeInt(Blocks.PALE_HANGING_MOSS);
    }

    @Inject(method = "checkBurnOut", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"), cancellable = true)
    private void pinferno$preventEmptyBoomBoxFire(Level level, BlockPos pos, int chance, RandomSource random, int age, CallbackInfo ci, @Local(name = "oldState") BlockState oldState) {
        if (oldState.is(ModBlocks.BOOM_BOX) && oldState.getValue(BoomBoxBlock.DYNAMITE) <= 0) {
            ci.cancel();
        }
    }

    @Inject(method = "checkBurnOut", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z"), cancellable = true)
    private void pinferno$handleBoomBoxBurnout(Level level, BlockPos pos, int chance, RandomSource random, int age, CallbackInfo ci, @Local(name = "oldState") BlockState oldState) {
        if (!oldState.is(ModBlocks.BOOM_BOX)) return;

        if (oldState.getValue(BoomBoxBlock.DYNAMITE) <= 0) {
            ci.cancel();
            return;
        }

        if (BoomBoxBlock.prime(level, pos, oldState, null)) {
            level.removeBlock(pos, false);
            ci.cancel();
        }
    }
}
