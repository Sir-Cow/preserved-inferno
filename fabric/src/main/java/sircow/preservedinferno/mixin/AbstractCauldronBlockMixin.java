package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.block.ModBlockProperties;
import sircow.preservedinferno.block.entity.PreservedCauldronBlockEntity;
import sircow.preservedinferno.fluid.CauldronFluid;
import sircow.preservedinferno.fluid.ModFluids;
import sircow.preservedinferno.item.ModItems;

@Mixin(AbstractCauldronBlock.class)
public class AbstractCauldronBlockMixin {
    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    public void pinferno$useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof PreservedCauldronBlockEntity cauldron) {
            ItemStack heldItem = player.getItemInHand(hand);
            PotionContents potionContentsComponent = heldItem.get(DataComponents.POTION_CONTENTS);

            if (!level.isClientSide()) {
                boolean interactionHandled = false;

                if (cauldron.fluidAmount < cauldron.maxFluidAmount) {
                    CauldronFluid insertedFluid = null;
                    boolean bucketInsert = false;
                    boolean isHoneyBlock = false;

                    if (heldItem.is(Items.WATER_BUCKET)) {
                        insertedFluid = CauldronFluid.WATER;
                        bucketInsert = true;
                    }
                    else if (heldItem.is(Items.LAVA_BUCKET)) {
                        insertedFluid = CauldronFluid.LAVA;
                        bucketInsert = true;
                    }
                    else if (heldItem.is(Items.MILK_BUCKET)) {
                        insertedFluid = CauldronFluid.MILK;
                        bucketInsert = true;
                    }
                    else if (heldItem.is(Items.POWDER_SNOW_BUCKET)) {
                        insertedFluid = CauldronFluid.SNOW;
                        bucketInsert = true;
                    }
                    else if (heldItem.is(Items.POTION) && potionContentsComponent != null && potionContentsComponent.is(Potions.WATER)) insertedFluid = CauldronFluid.WATER;
                    else if (heldItem.is(ModItems.MILK_BOTTLE)) insertedFluid = CauldronFluid.MILK;
                    else if (heldItem.is(Items.HONEY_BOTTLE)) insertedFluid = CauldronFluid.HONEY;
                    else if (heldItem.is(ModItems.LAVA_BOTTLE)) insertedFluid = CauldronFluid.LAVA;
                    else if (heldItem.is(Items.HONEY_BLOCK)) {
                        insertedFluid = CauldronFluid.HONEY;
                        isHoneyBlock = true;
                    }

                    if (insertedFluid != null && (cauldron.fluid == CauldronFluid.EMPTY || cauldron.fluid == insertedFluid)) {
                        cauldron.fluid = insertedFluid;

                        if (bucketInsert) {
                            cauldron.fluidAmount = Math.min(cauldron.maxFluidAmount, cauldron.fluidAmount + cauldron.fluidValueBucket);

                            if (!player.isCreative()) {
                                heldItem.shrink(1);
                                player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                            }

                            SoundEvent emptySound = switch (cauldron.fluid) {
                                case LAVA -> SoundEvents.BUCKET_EMPTY_LAVA;
                                case SNOW -> SoundEvents.BUCKET_EMPTY_POWDER_SNOW;
                                default -> SoundEvents.BUCKET_EMPTY;
                            };
                            level.playSound(null, pos, emptySound, SoundSource.BLOCKS, 1F, 1F);
                        }
                        else {
                            cauldron.fluidAmount = Math.min(cauldron.maxFluidAmount, cauldron.fluidAmount + (isHoneyBlock ? cauldron.fluidValueHoneyBlock : cauldron.fluidValueBottle));

                            if (!player.isCreative()) {
                                heldItem.shrink(1);
                                player.addItem(new ItemStack(Items.GLASS_BOTTLE));
                            }

                            level.playSound(null, pos, isHoneyBlock ? SoundEvents.HONEY_BLOCK_PLACE : SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1F, 1F);
                        }
                        interactionHandled = true;
                    }
                }

                if (!interactionHandled && cauldron.fluidAmount > 0) {
                    if (heldItem.is(Items.BUCKET)) {
                        if (cauldron.fluidAmount >= cauldron.fluidValueBucket) {
                            ItemStack resultBucket = switch (cauldron.fluid) {
                                case LAVA -> new ItemStack(Items.LAVA_BUCKET);
                                case MILK -> new ItemStack(Items.MILK_BUCKET);
                                case SNOW -> new ItemStack(Items.POWDER_SNOW_BUCKET);
                                case WATER -> new ItemStack(Items.WATER_BUCKET);
                                default -> ItemStack.EMPTY;
                            };

                            if (!resultBucket.isEmpty()) {
                                cauldron.fluidAmount -= cauldron.fluidValueBucket;

                                if (!player.isCreative()) {
                                    heldItem.shrink(1);
                                    player.setItemInHand(hand, resultBucket);
                                }

                                SoundEvent fillSound = switch (cauldron.fluid) {
                                    case LAVA -> SoundEvents.BUCKET_FILL_LAVA;
                                    case SNOW -> SoundEvents.BUCKET_FILL_POWDER_SNOW;
                                    default -> SoundEvents.BUCKET_FILL;
                                };
                                level.playSound(null, pos, fillSound, SoundSource.BLOCKS, 1F, 1F);
                                interactionHandled = true;
                            }
                        }
                    }
                    else if (heldItem.is(Items.GLASS_BOTTLE)) {
                        if (cauldron.fluidAmount >= cauldron.fluidValueBottle) {
                            ItemStack resultBottle = switch (cauldron.fluid) {
                                case HONEY -> new ItemStack(Items.HONEY_BOTTLE);
                                case LAVA -> new ItemStack(ModItems.LAVA_BOTTLE);
                                case MILK -> new ItemStack(ModItems.MILK_BOTTLE);
                                case WATER -> {
                                    ItemStack stack = new ItemStack(Items.POTION);
                                    stack.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER));
                                    yield stack;
                                }
                                default -> ItemStack.EMPTY;
                            };

                            if (!resultBottle.isEmpty()) {
                                cauldron.fluidAmount -= cauldron.fluidValueBottle;

                                if (!player.isCreative()) {
                                    heldItem.shrink(1);
                                    player.addItem(resultBottle);
                                }
                                level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1F, 1F);
                                interactionHandled = true;
                            }
                        }
                    }
                }

                if (cauldron.fluidAmount <= 0) {
                    cauldron.fluidAmount = 0;
                    cauldron.fluid = CauldronFluid.EMPTY;
                }

                if (interactionHandled) {
                    cauldron.setChanged();

                    BlockState currentState = level.getBlockState(pos);
                    if (currentState.hasProperty(ModBlockProperties.IS_LIT)) {
                        BlockState newState = currentState.setValue(ModBlockProperties.IS_LIT, cauldron.fluid == CauldronFluid.LAVA);
                        level.setBlock(pos, newState, 3);
                        level.sendBlockUpdated(pos, currentState, newState, 3);
                    }
                    else level.sendBlockUpdated(pos, currentState, currentState, 3);
                }
                else player.openMenu(cauldron);
            }
            cir.setReturnValue(InteractionResult.SUCCESS);
            return;
        }
        cir.setReturnValue(InteractionResult.PASS);
    }

    @Overwrite
    protected boolean canReceiveStalactiteDrip(@NonNull Fluid fluid) {
        return fluid == Fluids.WATER || fluid == Fluids.LAVA || fluid == ModFluids.HONEY;
    }
}
