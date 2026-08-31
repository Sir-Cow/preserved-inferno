package sircow.preservedinferno.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import sircow.preservedinferno.block.ModBlocks;
import sircow.preservedinferno.block.custom.BoomBoxBlock;
import sircow.preservedinferno.entity.ModEntities;

import java.util.Optional;

public class PrimedBoomBox extends Entity implements TraceableEntity {
    private static final EntityDataAccessor<Integer> DATA_FUSE_ID = SynchedEntityData.defineId(PrimedBoomBox.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<BlockState> DATA_BLOCK_STATE_ID = SynchedEntityData.defineId(PrimedBoomBox.class, EntityDataSerializers.BLOCK_STATE);
    private static final BlockState DEFAULT_BLOCK_STATE = ModBlocks.BOOM_BOX.get().defaultBlockState();
    public static final ExplosionDamageCalculator USED_PORTAL_DAMAGE_CALCULATOR = new ExplosionDamageCalculator() {
        @Override
        public boolean shouldBlockExplode(final @NonNull Explosion explosion, final @NonNull BlockGetter level, final @NonNull BlockPos pos, final BlockState state, final float power) {
            return !state.is(Blocks.NETHER_PORTAL) && super.shouldBlockExplode(explosion, level, pos, state, power);
        }

        @Override
        public @NonNull Optional<Float> getBlockExplosionResistance(final @NonNull Explosion explosion, final @NonNull BlockGetter level, final @NonNull BlockPos pos, final BlockState block, final @NonNull FluidState fluid) {
            return block.is(Blocks.NETHER_PORTAL) ? Optional.empty() : super.getBlockExplosionResistance(explosion, level, pos, block, fluid);
        }
    };
    private @Nullable EntityReference<LivingEntity> owner;
    private boolean usedPortal;

    public PrimedBoomBox(final EntityType<? extends PrimedBoomBox> type, final Level level) {
        super(type, level);
        this.blocksBuilding = true;
    }

    public PrimedBoomBox(final Level level, final double x, final double y, final double z, final @Nullable LivingEntity owner) {
        this(ModEntities.PRIMED_BOOM_BOX, level);
        this.setPos(x, y, z);
        this.setFuse(80);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.owner = EntityReference.of(owner);
    }

    @Override
    protected void defineSynchedData(final SynchedEntityData.Builder entityData) {
        entityData.define(DATA_FUSE_ID, 80);
        entityData.define(DATA_BLOCK_STATE_ID, DEFAULT_BLOCK_STATE);
    }

    @Override
    protected Entity.@NonNull MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    protected double getDefaultGravity() {
        return 0.04;
    }

    @Override
    public void tick() {
        this.handlePortal();
        this.applyGravity();
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.applyEffectsFromBlocks();
        this.setDeltaMovement(this.getDeltaMovement().scale(this.getAirDrag()));
        if (this.onGround()) this.setDeltaMovement(this.getDeltaMovement().multiply(0.7, -0.5, 0.7));

        int fuse = this.getFuse() - 1;
        this.setFuse(fuse);
        if (fuse <= 0) {
            this.discard();
            if (!this.level().isClientSide()) this.explode();
        }
        else {
            this.updateFluidInteraction();
            if (this.level().isClientSide()) this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5, this.getZ(), 0.0, 0.0, 0.0);
        }
    }

    private void explode() {
        if (this.level() instanceof ServerLevel level && level.getGameRules().get(GameRules.TNT_EXPLODES)) {
            BlockState state = this.getBlockState();
            int dynamite = state.hasProperty(BoomBoxBlock.DYNAMITE) ? state.getValue(BoomBoxBlock.DYNAMITE) : 0;

            float power = (float) (dynamite + 1);
            int radius = dynamite;
            ExplosionDamageCalculator calculator = this.usedPortal ? USED_PORTAL_DAMAGE_CALCULATOR : new ExplosionDamageCalculator();
            DamageSource damageSource = Explosion.getDefaultDamageSource(level, this);
            BlockPos centrePos = BlockPos.containing(this.getX(), this.getY(0.0625), this.getZ());

            level.explode(this, damageSource, calculator, this.getX(), this.getY(0.0625), this.getZ(), power, false, Level.ExplosionInteraction.NONE);

            if (this.isInWater()) return;

            AABB area = new AABB(centrePos).inflate(radius);

            for (ItemEntity item : level.getEntitiesOfClass(ItemEntity.class, area)) {
                item.discard();
            }

            for (int dx = -radius; dx <= radius; dx++) {
                for (int dy = -radius; dy <= radius; dy++) {
                    for (int dz = -radius; dz <= radius; dz++) {
                        BlockPos targetPos = centrePos.offset(dx, dy, dz);
                        BlockState targetState = level.getBlockState(targetPos);

                        if (!targetState.isAir()) {
                            if (!level.getFluidState(targetPos).isEmpty()) continue;
                            if (this.usedPortal && targetState.is(Blocks.NETHER_PORTAL)) continue;

                            if (targetState.is(Blocks.TNT)) {
                                level.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 11);
                                PrimedTnt primedTnt = new PrimedTnt(level, targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5, this.getOwner());
                                primedTnt.setFuse((short)(level.getRandom().nextInt(primedTnt.getFuse() / 4) + primedTnt.getFuse() / 8));
                                level.addFreshEntity(primedTnt);
                            }
                            else if (targetState.is(ModBlocks.BOOM_BOX.get()) && targetState.getValue(BoomBoxBlock.DYNAMITE) > 0) {
                                level.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 3);

                                PrimedBoomBox primedBoomBox = new PrimedBoomBox(level, targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5, this.getOwner());

                                primedBoomBox.setBlockState(targetState);
                                primedBoomBox.setFuse(PrimedBoomBox.getRandomShortFuse(80, level.getRandom()));
                                level.addFreshEntity(primedBoomBox);
                            }
                            else {
                                float resistance = targetState.getBlock().getExplosionResistance();
                                if (resistance < 30) {
                                    BlockEntity blockEntity = targetState.hasBlockEntity() ? level.getBlockEntity(targetPos) : null;

                                    Block.dropResources(targetState, level, targetPos, blockEntity, null, ItemStack.EMPTY);
                                    level.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 3);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(final ValueOutput output) {
        output.putShort("fuse", (short)this.getFuse());
        output.store("block_state", BlockState.CODEC, this.getBlockState());

        EntityReference.store(this.owner, output, "owner");
    }

    @Override
    protected void readAdditionalSaveData(final ValueInput input) {
        this.setFuse(input.getShortOr("fuse", (short)80));
        this.setBlockState(input.read("block_state", BlockState.CODEC).orElse(DEFAULT_BLOCK_STATE));

        this.owner = EntityReference.read(input, "owner");
    }

    public @Nullable LivingEntity getOwner() {
        return EntityReference.getLivingEntity(this.owner, this.level());
    }

    @Override
    public void restoreFrom(final @NonNull Entity oldEntity) {
        super.restoreFrom(oldEntity);
        if (oldEntity instanceof PrimedBoomBox primedBoomBox) this.owner = primedBoomBox.owner;
    }

    public void setFuse(final int time) {
        this.entityData.set(DATA_FUSE_ID, time);
    }

    public int getFuse() {
        return this.entityData.get(DATA_FUSE_ID);
    }

    public static int getRandomShortFuse(final int fuse, final RandomSource random) {
        return random.nextInt(Math.max(1, fuse / 4)) + fuse / 8;
    }

    public void setBlockState(final BlockState blockState) {
        this.entityData.set(DATA_BLOCK_STATE_ID, blockState);
    }

    public BlockState getBlockState() {
        return this.entityData.get(DATA_BLOCK_STATE_ID);
    }

    private void setUsedPortal(final boolean usedPortal) {
        this.usedPortal = usedPortal;
    }

    @Override
    public @Nullable Entity teleport(final @NonNull TeleportTransition transition) {
        Entity newEntity = super.teleport(transition);
        if (newEntity instanceof PrimedBoomBox boomBox) boomBox.setUsedPortal(true);

        return newEntity;
    }

    @Override
    public final boolean hurtServer(final @NonNull ServerLevel level, final @NonNull DamageSource source, final float damage) {
        return false;
    }
}
