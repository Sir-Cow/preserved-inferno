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

import java.util.ArrayList;
import java.util.List;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {
    @Unique private static final List<BlockPos> BOOKSHELF_HORIZONTAL_OFFSETS;

    static {
        BOOKSHELF_HORIZONTAL_OFFSETS = new ArrayList<>();
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                if ((Math.abs(x) == 2 || Math.abs(z) == 2) && !(Math.abs(x) <= 1 && Math.abs(z) <= 1)) {
                    BOOKSHELF_HORIZONTAL_OFFSETS.add(new BlockPos(x, 0, z));
                }
            }
        }
    }

    @Inject(method = "useItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;useOn(Lnet/minecraft/world/item/context/UseOnContext;)Lnet/minecraft/world/InteractionResult;"))
    private void preserved_inferno$onUseItemOnBlock(ServerPlayer player, Level level, ItemStack stack, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (stack.getItem() == Blocks.BOOKSHELF.asItem()) {
            BlockPos placedPos = hitResult.getBlockPos().relative(hitResult.getDirection());
            BlockPos enchantmentTablePos = getEnchantmentTableInValidSpot(level, placedPos);
            if (enchantmentTablePos != null) {
                ModTriggers.PLACE_BOOKSHELF.trigger(player);

                int bookshelfCount = countValidBookshelvesAroundEnchantingTable(level, enchantmentTablePos);
                if (bookshelfCount >= 12) {
                    ModTriggers.MAX_ENCHANTING_TABLE.trigger(player);
                }
            }
        }
    }

    @Unique
    private BlockPos getEnchantmentTableInValidSpot(Level level, BlockPos bookshelfPos) {
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                if (Math.abs(x) <= 1 && Math.abs(z) <= 1) {
                    continue;
                }

                BlockPos potentialEnchantmentTablePosSameY = bookshelfPos.offset(x, 0, z);
                if (level.getBlockState(potentialEnchantmentTablePosSameY).is(Blocks.ENCHANTING_TABLE)) {
                    return potentialEnchantmentTablePosSameY;
                }

                BlockPos potentialEnchantmentTablePosDownY = bookshelfPos.offset(x, -1, z);
                if (level.getBlockState(potentialEnchantmentTablePosDownY).is(Blocks.ENCHANTING_TABLE)) {
                    return potentialEnchantmentTablePosDownY;
                }
            }
        }
        return null;
    }

    @Unique
    private int countValidBookshelvesAroundEnchantingTable(Level level, BlockPos enchantmentTablePos) {
        int bookshelfCount = 0;

        for (BlockPos offset : BOOKSHELF_HORIZONTAL_OFFSETS) {
            BlockPos potentialBookshelfPosSameY = enchantmentTablePos.offset(offset.getX(), 0, offset.getZ());
            if (level.getBlockState(potentialBookshelfPosSameY).is(Blocks.BOOKSHELF)) {
                bookshelfCount++;
            }

            BlockPos potentialBookshelfPosUpY = enchantmentTablePos.offset(offset.getX(), 1, offset.getZ());
            if (level.getBlockState(potentialBookshelfPosUpY).is(Blocks.BOOKSHELF)) {
                bookshelfCount++;
            }
        }

        bookshelfCount = bookshelfCount + 1;
        return bookshelfCount;
    }
}
