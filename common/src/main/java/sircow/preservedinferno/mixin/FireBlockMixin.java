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
    private static void preserved_inferno$removePaleOakBurn(CallbackInfo ci) {
        FireBlockAccessor accessor = (FireBlockAccessor) Blocks.FIRE;

        accessor.preserved_inferno$getIgniteOdds().removeInt(Blocks.PALE_OAK_PLANKS);
        accessor.preserved_inferno$getBurnOdds().removeInt(Blocks.PALE_OAK_PLANKS);
        accessor.preserved_inferno$getIgniteOdds().removeInt(Blocks.PALE_OAK_SLAB);
        accessor.preserved_inferno$getBurnOdds().removeInt(Blocks.PALE_OAK_SLAB);
        accessor.preserved_inferno$getIgniteOdds().removeInt(Blocks.PALE_OAK_FENCE_GATE);
        accessor.preserved_inferno$getBurnOdds().removeInt(Blocks.PALE_OAK_FENCE_GATE);
        accessor.preserved_inferno$getIgniteOdds().removeInt(Blocks.PALE_OAK_FENCE);
        accessor.preserved_inferno$getBurnOdds().removeInt(Blocks.PALE_OAK_FENCE);
        accessor.preserved_inferno$getIgniteOdds().removeInt(Blocks.PALE_OAK_STAIRS);
        accessor.preserved_inferno$getBurnOdds().removeInt(Blocks.PALE_OAK_STAIRS);
        accessor.preserved_inferno$getIgniteOdds().removeInt(Blocks.PALE_OAK_LOG);
        accessor.preserved_inferno$getBurnOdds().removeInt(Blocks.PALE_OAK_LOG);
        accessor.preserved_inferno$getIgniteOdds().removeInt(Blocks.STRIPPED_PALE_OAK_LOG);
        accessor.preserved_inferno$getBurnOdds().removeInt(Blocks.STRIPPED_PALE_OAK_LOG);
        accessor.preserved_inferno$getIgniteOdds().removeInt(Blocks.PALE_OAK_WOOD);
        accessor.preserved_inferno$getBurnOdds().removeInt(Blocks.PALE_OAK_WOOD);
        accessor.preserved_inferno$getIgniteOdds().removeInt(Blocks.STRIPPED_PALE_OAK_WOOD);
        accessor.preserved_inferno$getBurnOdds().removeInt(Blocks.STRIPPED_PALE_OAK_WOOD);
        accessor.preserved_inferno$getIgniteOdds().removeInt(Blocks.PALE_OAK_SHELF);
        accessor.preserved_inferno$getBurnOdds().removeInt(Blocks.PALE_OAK_SHELF);
        accessor.preserved_inferno$getIgniteOdds().removeInt(Blocks.PALE_OAK_LEAVES);
        accessor.preserved_inferno$getBurnOdds().removeInt(Blocks.PALE_OAK_LEAVES);
        accessor.preserved_inferno$getIgniteOdds().removeInt(Blocks.PALE_MOSS_BLOCK);
        accessor.preserved_inferno$getBurnOdds().removeInt(Blocks.PALE_MOSS_BLOCK);
        accessor.preserved_inferno$getIgniteOdds().removeInt(Blocks.PALE_MOSS_CARPET);
        accessor.preserved_inferno$getBurnOdds().removeInt(Blocks.PALE_MOSS_CARPET);
        accessor.preserved_inferno$getIgniteOdds().removeInt(Blocks.PALE_HANGING_MOSS);
        accessor.preserved_inferno$getBurnOdds().removeInt(Blocks.PALE_HANGING_MOSS);
    }
}
