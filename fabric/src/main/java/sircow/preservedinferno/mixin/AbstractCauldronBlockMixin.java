package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.block.entity.PreservedCauldronBlockEntity;

@Mixin(AbstractCauldronBlock.class)
public class AbstractCauldronBlockMixin {
    // cancel vanilla interactions
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void preserved_inferno$cancel(BlockState state, ServerLevel level, BlockPos blockPos, RandomSource randomSource, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "hasAnalogOutputSignal", at = @At("HEAD"), cancellable = true)
    public void preserved_inferno$cancel2(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    public void preserved_inferno$cancel3(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult stack, CallbackInfoReturnable<InteractionResult> cir) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof PreservedCauldronBlockEntity menuProvider) {
                player.openMenu(menuProvider);
                cir.setReturnValue(InteractionResult.SUCCESS);
            } else {
                // do nothing!
            }
        }
    }
}
