package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.PreservedInferno;
import sircow.preservedinferno.other.BlueIceSequenceTask;
import sircow.preservedinferno.other.PackedIceSequenceTask;
import sircow.preservedinferno.other.SimpleBlockTransformationTask;

@Mixin(BlockItem.class)
public class BlockItemMixin {
    // make cold blocks go poof in the nether
    @Inject(method = "place", at = @At("HEAD"))
    private void preserved_inferno$onPlace(BlockPlaceContext context, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemStack = context.getItemInHand();
        BlockPos blockPos = context.getClickedPos();
        Level level = context.getLevel();

        if (!level.isClientSide && itemStack.getItem() instanceof BlockItem && level.dimension() == Level.NETHER) {
            Block placedBlock = ((BlockItem) itemStack.getItem()).getBlock();
            ServerLevel serverLevel = (ServerLevel) level;
            PreservedInferno instance = PreservedInferno.INSTANCE;

            if (placedBlock == Blocks.ICE) {
                PreservedInferno.scheduleDelayedTask(new SimpleBlockTransformationTask(instance, serverLevel, blockPos, Blocks.AIR.defaultBlockState(), Blocks.ICE, 2 * 20));
            }
            else if (placedBlock == Blocks.SNOW_BLOCK) {
                PreservedInferno.scheduleDelayedTask(new SimpleBlockTransformationTask(instance, serverLevel, blockPos, Blocks.AIR.defaultBlockState(), Blocks.SNOW_BLOCK, 5 * 20));
            }
            else if (placedBlock instanceof SnowLayerBlock) {
                PreservedInferno.scheduleDelayedTask(new SimpleBlockTransformationTask(instance, serverLevel, blockPos, Blocks.AIR.defaultBlockState(), placedBlock, 2 * 20));
            }
            else if (placedBlock == Blocks.PACKED_ICE) {
                schedulePackedIceSequence(instance, serverLevel, blockPos);
            }
            else if (placedBlock == Blocks.BLUE_ICE) {
                scheduleBlueIceSequence(instance, serverLevel, blockPos);
            }
            else if (placedBlock == Blocks.POWDER_SNOW) {
                PreservedInferno.scheduleDelayedTask(new SimpleBlockTransformationTask(instance, serverLevel, blockPos, Blocks.AIR.defaultBlockState(), Blocks.POWDER_SNOW, 3 * 20));
            }
        }
    }

    @Unique
    private void schedulePackedIceSequence(PreservedInferno modInstance, ServerLevel serverLevel, BlockPos pos) {
        int packedToIceDelay = 3 * 20;
        int iceToAirDelay = 2 * 20;
        PreservedInferno.scheduleDelayedTask(new PackedIceSequenceTask(modInstance, serverLevel, pos, packedToIceDelay, iceToAirDelay));
    }

    @Unique
    private void scheduleBlueIceSequence(PreservedInferno modInstance, ServerLevel serverLevel, BlockPos pos) {
        int blueToPackedDelay = 15 * 20;
        int packedToIceDelay = 3 * 20;
        int iceToAirDelay = 2 * 20;
        PreservedInferno.scheduleDelayedTask(new BlueIceSequenceTask(modInstance, serverLevel, pos, blueToPackedDelay, packedToIceDelay, iceToAirDelay));
    }
}

