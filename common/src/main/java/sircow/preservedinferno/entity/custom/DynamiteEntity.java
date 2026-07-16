package sircow.preservedinferno.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.block.ModBlocks;
import sircow.preservedinferno.block.custom.BoomBoxBlock;
import sircow.preservedinferno.entity.ModEntities;
import sircow.preservedinferno.item.ModItems;

public class DynamiteEntity extends ThrowableItemProjectile {
    public DynamiteEntity(final EntityType<? extends DynamiteEntity> type, final Level level) {
        super(type, level);
    }

    public DynamiteEntity(final Level level, final LivingEntity mob, final ItemStack itemStack) {
        super(ModEntities.DYNAMITE_PROJECTILE, mob, level, itemStack);
    }

    public DynamiteEntity(final Level level, final double x, final double y, final double z, final ItemStack itemStack) {
        super(ModEntities.DYNAMITE_PROJECTILE, x, y, z, level, itemStack);
    }

    @Override
    protected @NonNull Item getDefaultItem() {
        return ModItems.DYNAMITE;
    }

    private ParticleOptions getParticle() {
        ItemStack item = this.getItem();
        return item.isEmpty() ? ParticleTypes.SMOKE : new ItemParticleOption(ParticleTypes.ITEM, ItemStackTemplate.fromNonEmptyStack(item));
    }

    @Override
    public void handleEntityEvent(final byte id) {
        if (id == 3) {
            ParticleOptions particle = this.getParticle();

            for (int i = 0; i < 8; i++) {
                this.level().addParticle(particle, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void onHitEntity(final @NonNull EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        Entity entity = hitResult.getEntity();
        int damage = entity instanceof Blaze ? 3 : 0;
        entity.hurt(this.damageSources().thrown(this, this.getOwner()), damage);
    }

    @Override
    protected void onHit(final @NonNull HitResult hitResult) {
        super.onHit(hitResult);

        if (this.level().isClientSide()) return;

        Level level = this.level();

        level.broadcastEntityEvent(this, (byte) 3);
        level.explode(this, Explosion.getDefaultDamageSource(level, this), new ExplosionDamageCalculator(), this.getX(), this.getY(), this.getZ(), 1.0F, false, Level.ExplosionInteraction.NONE);

        if (!this.isInWater()) {
            BlockPos centrePos = BlockPos.containing(this.position());
            LivingEntity explosionOwner = this.getOwner() instanceof LivingEntity living ? living : null;

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        BlockPos targetPos = centrePos.offset(dx, dy, dz);
                        BlockState state = level.getBlockState(targetPos);

                        if (state.isAir()) continue;
                        if (!level.getFluidState(targetPos).isEmpty()) continue;

                        if (state.is(Blocks.TNT)) {
                            level.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 11);
                            PrimedTnt primedTnt = new PrimedTnt(level, targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5, explosionOwner);
                            primedTnt.setFuse((short)(level.getRandom().nextInt(primedTnt.getFuse() / 4) + primedTnt.getFuse() / 8));
                            level.addFreshEntity(primedTnt);
                        }
                        else if (state.is(ModBlocks.BOOM_BOX) && state.getValue(BoomBoxBlock.DYNAMITE) > 0) {
                            level.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 3);
                            PrimedBoomBox primedBoomBox = new PrimedBoomBox(level, targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5, explosionOwner);
                            primedBoomBox.setBlockState(state);
                            primedBoomBox.setFuse(PrimedBoomBox.getRandomShortFuse(80, level.getRandom()));
                            level.addFreshEntity(primedBoomBox);
                        }
                        else {
                            if (state.getBlock().getExplosionResistance() >= 30.0F) continue;

                            BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(targetPos) : null;

                            Block.dropResources(state, level, targetPos, blockEntity, null, ItemStack.EMPTY);
                            level.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 3);
                        }
                    }
                }
            }
        }
        this.discard();
    }
}
