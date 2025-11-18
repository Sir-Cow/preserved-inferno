package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.WaterFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WaterFluid.class)
public abstract class WaterFluidMixin {
    @Inject(method = "beforeDestroyingBlock", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$preventDestroyingPowderSnow(LevelAccessor level, BlockPos pos, BlockState state, CallbackInfo ci) {
        if (state.is(Blocks.POWDER_SNOW)) {
            ci.cancel();
        }
    }
}
