package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.trigger.ModTriggers;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {
    // bookshelf next to enchanting table advancement trigger
    @Inject(method = "useItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;useOn(Lnet/minecraft/world/item/context/UseOnContext;)Lnet/minecraft/world/InteractionResult;"))
    private void onUseItemOnBlock(ServerPlayer player, Level level, ItemStack stack, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (stack.getItem() == Blocks.BOOKSHELF.asItem()) {
            BlockPos placedPos = hitResult.getBlockPos().relative(hitResult.getDirection());
            if (isBookshelfInValidEnchantmentSpot(level, placedPos)) {
                ModTriggers.PLACE_BOOKSHELF.trigger(player);
            }
        }
    }

    @Unique
    private boolean isBookshelfInValidEnchantmentSpot(Level level, BlockPos bookshelfPos) {
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                if (Math.abs(x) <= 1 && Math.abs(z) <= 1) {
                    continue;
                }

                BlockPos potentialEnchantmentTablePosSameY = bookshelfPos.offset(x, 0, z);
                if (level.getBlockState(potentialEnchantmentTablePosSameY).is(Blocks.ENCHANTING_TABLE)) {
                    return true;
                }

                BlockPos potentialEnchantmentTablePosUpY = bookshelfPos.offset(x, -1, z);
                if (level.getBlockState(potentialEnchantmentTablePosUpY).is(Blocks.ENCHANTING_TABLE)) {
                    return true;
                }
            }
        }
        return false;
    }
}
