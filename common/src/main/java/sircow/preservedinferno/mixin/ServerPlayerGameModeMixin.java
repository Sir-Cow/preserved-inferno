package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EnchantingTableBlock;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.trigger.ModTriggers;

import java.util.List;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {
    @Unique
    private static final List<BlockPos> BOOKSHELF_HORIZONTAL_OFFSETS = BlockPos.betweenClosedStream(-3, -3, -3, 3, 3, 3)
            .filter(pos -> !pos.equals(BlockPos.ZERO))
            .map(BlockPos::immutable)
            .toList();

    @Inject(method = "useItemOn", at = @At(value = "RETURN"))
    private void pinferno$afterUseItemOnBlock(ServerPlayer player, Level level, ItemStack stack, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (cir.getReturnValue().consumesAction() && stack.is(Blocks.BOOKSHELF.asItem())) {
            BlockPos enchantmentTablePos = getEnchantmentTableInValidSpot(level, hitResult.getBlockPos().relative(hitResult.getDirection()));

            if (enchantmentTablePos != null) {
                ModTriggers.PLACE_BOOKSHELF.get().trigger(player);
                int bookshelfCount = countValidBookshelvesAroundEnchantingTable(level, enchantmentTablePos);
                if (bookshelfCount >= 10) {
                    ModTriggers.MAX_ENCHANTING_TABLE.get().trigger(player);
                }
            }
        }
    }

    @Unique
    private BlockPos getEnchantmentTableInValidSpot(Level level, BlockPos bookshelfPos) {
        for (int x = -3; x <= 3; x++) {
            for (int y = -3; y <= 3; y++) {
                for (int z = -3; z <= 3; z++) {
                    BlockPos potentialTablePos = bookshelfPos.offset(x, y, z);
                    if (level.getBlockState(potentialTablePos).is(Blocks.ENCHANTING_TABLE)) {
                        return potentialTablePos;
                    }
                }
            }
        }
        return null;
    }

    @Unique
    private int countValidBookshelvesAroundEnchantingTable(Level level, BlockPos enchantmentTablePos) {
        int bookshelfCount = 0;
        for (BlockPos offset : BOOKSHELF_HORIZONTAL_OFFSETS) {
            if (EnchantingTableBlock.isValidBookShelf(level, enchantmentTablePos, offset)) {
                bookshelfCount++;
            }
        }
        return bookshelfCount;
    }
}
