package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
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
    public void preserved_inferno$useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof PreservedCauldronBlockEntity cauldron) {
            ItemStack heldItem = player.getItemInHand(hand);
            PotionContents potionContentsComponent = heldItem.get(DataComponents.POTION_CONTENTS);

            if (!level.isClientSide()) {
                boolean interactionHandled = false;

                if (cauldron.progressWater < cauldron.maxWaterProgress) {
                    if (heldItem.getItem() == Items.WATER_BUCKET) {
                        cauldron.progressWater = Math.min(cauldron.maxWaterProgress, cauldron.progressWater + 8);
                        if (!player.isCreative()) {
                            player.getItemInHand(hand).setCount(player.getItemInHand(hand).getCount() - 1);
                            player.addItem(new ItemStack(Items.BUCKET));
                        }
                        level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                        interactionHandled = true;
                    }
                    else if (heldItem.getItem() == Items.POTION && (potionContentsComponent != null && potionContentsComponent.is(Potions.WATER))) {
                        cauldron.progressWater = Math.min(cauldron.maxWaterProgress, cauldron.progressWater + 2);
                        if (!player.isCreative()) {
                            player.getItemInHand(hand).setCount(player.getItemInHand(hand).getCount() - 1);
                            player.addItem(new ItemStack(Items.GLASS_BOTTLE));
                        }
                        level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                        interactionHandled = true;
                    }
                }

                if (!interactionHandled && cauldron.progressWater > 0) {
                    if (heldItem.getItem() == Items.BUCKET) {
                        if (cauldron.progressWater >= 8) {
                            cauldron.progressWater = Math.max(0, cauldron.progressWater - 8);
                            if (!player.isCreative()) {
                                player.getItemInHand(hand).setCount(player.getItemInHand(hand).getCount() - 1);
                                player.addItem(new ItemStack(Items.WATER_BUCKET));
                            }
                            level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                            interactionHandled = true;
                        }
                    }
                    else if (heldItem.getItem() == Items.GLASS_BOTTLE) {
                        if (cauldron.progressWater >= 2) {
                            cauldron.progressWater = Math.max(0, cauldron.progressWater - 2);
                            if (!player.isCreative()) {
                                ItemStack newWaterStack = new ItemStack(Items.POTION);
                                newWaterStack.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER));
                                player.getItemInHand(hand).setCount(player.getItemInHand(hand).getCount() - 1);
                                player.addItem(newWaterStack);
                            }
                            level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                            interactionHandled = true;
                        }
                    }
                }

                if (interactionHandled) {
                    cauldron.setChanged();
                    level.sendBlockUpdated(pos, state, state, 3);
                }
                else {
                    player.openMenu(cauldron);
                }

            }
            cir.setReturnValue(InteractionResult.SUCCESS);
            return;
        }
        cir.setReturnValue(InteractionResult.PASS);
    }
}
