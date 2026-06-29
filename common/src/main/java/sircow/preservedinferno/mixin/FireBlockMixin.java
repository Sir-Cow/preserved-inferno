package sircow.preservedinferno.mixin;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireBlock.class)
public class FireBlockMixin {
    @Inject(method = "bootStrap", at = @At("TAIL"))
    private static void pinferno$removePaleOakBurn(CallbackInfo ci) {
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
}
