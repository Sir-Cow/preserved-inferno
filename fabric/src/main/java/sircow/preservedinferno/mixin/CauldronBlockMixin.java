package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.InsideBlockEffectType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.block.ModBlockProperties;
import sircow.preservedinferno.block.entity.PreservedCauldronBlockEntity;
import sircow.preservedinferno.effect.ModEffects;
import sircow.preservedinferno.fluid.CauldronFluid;
import sircow.preservedinferno.fluid.ModFluids;

import java.util.Objects;

@Mixin(CauldronBlock.class)
public abstract class CauldronBlockMixin extends AbstractCauldronBlock implements EntityBlock {
    @Unique private static final VoxelShape[] CUSTOM_FILLED_SHAPES = new VoxelShape[8];

    static {
        for (int i = 0; i < 8; i++) {
            CUSTOM_FILLED_SHAPES[i] = Shapes.or(
                    AbstractCauldronBlock.SHAPE,
                    Block.column(12.0, 4.0, 6.0 + (i + 1) * 3.0)
            );
        }
    }

    @Shadow protected static boolean shouldHandlePrecipitation(final Level level, final Biome.Precipitation precipitation) {
        if (precipitation == Biome.Precipitation.RAIN || precipitation == Biome.Precipitation.SNOW) return level.getRandom().nextFloat() < 0.8F;
        else return false;
    }

    public CauldronBlockMixin(Properties properties, CauldronInteraction.Dispatcher interactions) {
        super(properties, interactions);
        this.registerDefaultState(this.stateDefinition.any().setValue(ModBlockProperties.IS_LIT, false));
    }

    @Unique
    @Override
    public @Nullable BlockEntity newBlockEntity(@NonNull BlockPos pos, @NonNull BlockState state) {
        return new PreservedCauldronBlockEntity(pos, state);
    }

    @SuppressWarnings("unchecked")
    @Unique
    private static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> serverType, BlockEntityType<E> clientType, BlockEntityTicker<? super E> ticker) {
        return clientType.equals(serverType) ? (BlockEntityTicker<A>) ticker : null;
    }

    @Unique
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NonNull Level world, @NonNull BlockState state, @NonNull BlockEntityType<T> blockEntityType) {
        return createCauldronTicker(world, blockEntityType);
    }

    @Unique
    private static <T extends BlockEntity> BlockEntityTicker<T> createCauldronTicker(Level level, BlockEntityType<T> blockEntityType) {
        return level instanceof ServerLevel serverlevel
                ? createTickerHelper(
                blockEntityType,
                blockEntityType,
                (world1, pos, state1, blockEntity) -> PreservedCauldronBlockEntity.tick(serverlevel, pos, state1, (PreservedCauldronBlockEntity) blockEntity)
        ) : null;
    }

    @Overwrite
    protected boolean canReceiveStalactiteDrip(@NonNull Fluid fluid) {
        return fluid == Fluids.WATER || fluid == Fluids.LAVA || fluid == ModFluids.HONEY;
    }

    @Inject(method = "receiveStalactiteDrip", at = @At("HEAD"), cancellable = true)
    public void pinferno$modifyReceiveStalactiteDrip(BlockState state, Level level, BlockPos pos, Fluid fluid, CallbackInfo ci) {
        if (level.getBlockEntity(pos) instanceof PreservedCauldronBlockEntity cauldron) {
            if (fluid == Fluids.WATER) {
                if (cauldron.fluid == CauldronFluid.EMPTY) cauldron.fluid = CauldronFluid.WATER;
                if (cauldron.fluid == CauldronFluid.WATER) {
                    int oldAmount = cauldron.fluidAmount;
                    cauldron.fluidAmount = Math.min(cauldron.maxFluidAmount, cauldron.fluidAmount + 1);

                    if (cauldron.fluidAmount != oldAmount) {
                        cauldron.setChanged();
                        level.sendBlockUpdated(pos, state, state, 3);
                        level.updateNeighbourForOutputSignal(pos, state.getBlock());
                    }
                    level.levelEvent(1047, pos, 0);
                }
            }
            else if (fluid == Fluids.LAVA) {
                if (cauldron.fluid == CauldronFluid.EMPTY) cauldron.fluid = CauldronFluid.LAVA;
                if (cauldron.fluid == CauldronFluid.LAVA) {
                    int oldAmount = cauldron.fluidAmount;
                    cauldron.fluidAmount = Math.min(cauldron.maxFluidAmount, cauldron.fluidAmount + 1);

                    if (cauldron.fluidAmount != oldAmount) {
                        cauldron.setChanged();
                        BlockState newState = state.setValue(ModBlockProperties.IS_LIT, cauldron.fluid == CauldronFluid.LAVA);
                        level.setBlock(pos, newState, 3);
                        level.sendBlockUpdated(pos, state, newState, 3);
                        level.updateNeighbourForOutputSignal(pos, state.getBlock());
                    }
                    level.levelEvent(1046, pos, 0);
                }
            }
            else if (fluid == ModFluids.HONEY) {
                if (cauldron.fluid == CauldronFluid.EMPTY) cauldron.fluid = CauldronFluid.HONEY;

                if (cauldron.fluid == CauldronFluid.HONEY) {
                    int oldAmount = cauldron.fluidAmount;
                    cauldron.fluidAmount = Math.min(cauldron.maxFluidAmount, cauldron.fluidAmount + 1);

                    if (cauldron.fluidAmount != oldAmount) {
                        cauldron.setChanged();
                        level.sendBlockUpdated(pos, state, state, 3);
                        level.updateNeighbourForOutputSignal(pos, state.getBlock());
                    }
                    level.levelEvent(1047, pos, 0);
                }
            }
        }
        ci.cancel();
    }

    @Overwrite
    public void handlePrecipitation(final @NonNull BlockState state, final @NonNull Level level, final @NonNull BlockPos pos, final Biome.@NonNull Precipitation precipitation) {
        if (level.getBlockEntity(pos) instanceof PreservedCauldronBlockEntity cauldron) {
            if (shouldHandlePrecipitation(level, precipitation)) {
                if (precipitation == Biome.Precipitation.RAIN) {
                    if (cauldron.fluid == CauldronFluid.EMPTY) cauldron.fluid = CauldronFluid.WATER;
                    if (cauldron.fluid == CauldronFluid.WATER) {
                        int oldAmount = cauldron.fluidAmount;
                        cauldron.fluidAmount = Math.min(cauldron.maxFluidAmount, cauldron.fluidAmount + 1);

                        if (cauldron.fluidAmount != oldAmount) {
                            cauldron.setChanged();
                            level.sendBlockUpdated(pos, state, state, 3);
                            level.updateNeighbourForOutputSignal(pos, state.getBlock());
                        }
                    }
                }
                else if (precipitation == Biome.Precipitation.SNOW) {
                    if (cauldron.fluid == CauldronFluid.EMPTY) cauldron.fluid = CauldronFluid.SNOW;
                    if (cauldron.fluid == CauldronFluid.SNOW) {
                        int oldAmount = cauldron.fluidAmount;
                        cauldron.fluidAmount = Math.min(cauldron.maxFluidAmount, cauldron.fluidAmount + 1);

                        if (cauldron.fluidAmount != oldAmount) {
                            cauldron.setChanged();
                            level.sendBlockUpdated(pos, state, state, 3);
                            level.updateNeighbourForOutputSignal(pos, state.getBlock());
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void entityInside(final @NonNull BlockState state, final @NonNull Level level, final @NonNull BlockPos pos, final @NonNull Entity entity, final @NonNull InsideBlockEffectApplier effectApplier, final boolean isPrecise) {
        if (level instanceof ServerLevel serverLevel && level.getBlockEntity(pos) instanceof PreservedCauldronBlockEntity cauldron) {
            if (cauldron.fluid == CauldronFluid.SNOW || cauldron.fluid == CauldronFluid.WATER) {
                BlockPos blockPos = pos.immutable();
                effectApplier.runBefore(InsideBlockEffectType.EXTINGUISH, e -> {
                    if (e.isOnFire() && e.mayInteract(serverLevel, blockPos)) this.handleEntityOnFireInside(state, level, blockPos);
                });
                if (cauldron.fluid == CauldronFluid.SNOW) {
                    if (cauldron.fluidAmount >= 8) {
                        effectApplier.apply(InsideBlockEffectType.FREEZE);

                        if (cauldron.incrementSnowCauldronTimer(entity) >= 20) {
                            if (level.dimension() == Level.NETHER) consumeCauldronFluid(state, level, pos, cauldron, 1);
                            cauldron.resetSnowCauldronTimer(entity);
                        }
                        return;
                    }
                    else cauldron.resetSnowCauldronTimer(entity);
                }
            }
            if (cauldron.fluid == CauldronFluid.LAVA && cauldron.fluidAmount >= 1) {
                effectApplier.apply(InsideBlockEffectType.LAVA_IGNITE);
                return;
            }
            if (entity instanceof LivingEntity livingEntity) {
                if (cauldron.fluid == CauldronFluid.HONEY && cauldron.fluidAmount >= 8) {
                    livingEntity.addEffect(new MobEffectInstance(ModEffects.HINDERED.holder, 8 * 20, 0, true, true, true));
                    livingEntity.removeEffect(MobEffects.POISON);
                    if (cauldron.markEntityInHoney(entity)) {
                        level.playSound(null, pos, SoundEvents.HONEY_BLOCK_SLIDE, SoundSource.BLOCKS, 1F, 1F);
                    }
                    return;
                }
                if (cauldron.fluid == CauldronFluid.MILK && cauldron.fluidAmount >= 8) {
                    if (!livingEntity.getActiveEffects().isEmpty()) {
                        consumeCauldronFluid(state, level, pos, cauldron, 8);
                        livingEntity.removeAllEffects();
                        level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1F, 1F);
                    }
                    return;
                }
            }
        }
        effectApplier.apply(InsideBlockEffectType.EXTINGUISH);
    }

    @Unique
    private void handleEntityOnFireInside(final BlockState state, final Level level, final BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof PreservedCauldronBlockEntity cauldron) {
            if (cauldron.fluid == CauldronFluid.WATER) consumeCauldronFluid(state, level, pos, cauldron, 8);
        }
    }

    @Unique
    private void consumeCauldronFluid(final BlockState state, final Level level, final BlockPos pos, PreservedCauldronBlockEntity cauldron, int reductionAmount) {
        int oldAmount = cauldron.fluidAmount;
        cauldron.fluidAmount = Math.max(0, cauldron.fluidAmount - reductionAmount);

        if (cauldron.fluidAmount != oldAmount) {
            if (cauldron.fluidAmount == 0) cauldron.fluid = CauldronFluid.EMPTY;
            cauldron.setChanged();
            level.sendBlockUpdated(pos, state, state, 3);
            level.updateNeighbourForOutputSignal(pos, state.getBlock());
        }
    }

    @Unique
    @Override
    public boolean hasAnalogOutputSignal(@NonNull BlockState state) {
        return true;
    }

    @Unique
    @Override
    public int getAnalogOutputSignal(@NonNull BlockState state, Level level, @NonNull BlockPos pos, @NonNull Direction direction)  {
        if (level.getBlockEntity(pos) instanceof PreservedCauldronBlockEntity cauldron) {
            return (int) Math.floor((double) cauldron.fluidAmount / cauldron.maxFluidAmount * 15.0);
        }
        return 0;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NonNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ModBlockProperties.IS_LIT);
    }

    @Override
    public BlockState getStateForPlacement(@NonNull BlockPlaceContext context) {
        return Objects.requireNonNull(super.getStateForPlacement(context)).setValue(ModBlockProperties.IS_LIT, false);
    }

    @Override
    protected @NonNull VoxelShape getEntityInsideCollisionShape(@NonNull BlockState state, @NonNull BlockGetter level, @NonNull BlockPos pos, @NonNull Entity entity) {
        if (level.getBlockEntity(pos) instanceof PreservedCauldronBlockEntity cauldron) {
            int amount = cauldron.fluidAmount;

            if (amount <= 0) return Shapes.empty();

            int shapeIndex = Math.min(7, (amount - 1) / 8);
            return CUSTOM_FILLED_SHAPES[shapeIndex];
        }
        return Shapes.empty();
    }
}
