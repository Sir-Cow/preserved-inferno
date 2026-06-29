package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.SculkSensorBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SculkSensorBlock.class)
public abstract class SculkSensorBlockMixin extends BaseEntityBlock {
    protected SculkSensorBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "spawnAfterBreak", at = @At("TAIL"), cancellable = true)
    private void pinferno$increaseXp(BlockState state, ServerLevel level, BlockPos pos, ItemStack stack, boolean dropExperience, CallbackInfo ci) {
        if (dropExperience) {
            this.tryDropExperience(level, pos, stack, ConstantInt.of(10));
        }
        ci.cancel();
    }
}
