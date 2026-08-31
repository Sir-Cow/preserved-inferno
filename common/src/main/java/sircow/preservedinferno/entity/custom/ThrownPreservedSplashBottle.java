package sircow.preservedinferno.entity.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.effect.ModEffects;
import sircow.preservedinferno.entity.ModEntities;
import sircow.preservedinferno.item.ModItems;
import sircow.preservedinferno.other.HeatAccessor;

public class ThrownPreservedSplashBottle extends ThrowableItemProjectile {
    public ThrownPreservedSplashBottle(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super(type, level);
    }

    public ThrownPreservedSplashBottle(Level level, LivingEntity owner, ItemStack stack) {
        super(ModEntities.PRESERVED_SPLASH_BOTTLE, owner, level, stack);
    }

    public ThrownPreservedSplashBottle(Level level, double x, double y, double z, ItemStack stack) {
        super(ModEntities.PRESERVED_SPLASH_BOTTLE, x, y, z, level, stack);
    }

    @Override
    protected @NonNull Item getDefaultItem() {
        return Items.GLASS_BOTTLE;
    }

    @Override
    protected double getDefaultGravity() {
        return 0.07;
    }

    @Override
    protected void onHit(@NonNull HitResult hitResult) {
        super.onHit(hitResult);

        if (!(this.level() instanceof ServerLevel level)) return;

        Item item = this.getItem().getItem();
        AABB potionAabb = this.getBoundingBox().move(hitResult.getLocation().subtract(this.position()));
        AABB effectArea = potionAabb.inflate(4.0, 2.0, 4.0);
        float margin = ProjectileUtil.computeMargin(this);

        for (LivingEntity livingEntity : level.getEntitiesOfClass(LivingEntity.class, effectArea)) {
            if (!livingEntity.isAffectedByPotions()) continue;
            double dist = potionAabb.distanceToSqr(livingEntity.getBoundingBox().inflate(margin));

            if (dist >= 16.0) continue;
            double scale = 1.0 - Math.sqrt(dist) / 4.0;

            if (item == ModItems.SPLASH_LAVA_BOTTLE.get()) {
                float scaledIgniteDuration = (float) (scale * 15.0);

                if (scaledIgniteDuration > 0.0F && !livingEntity.fireImmune()) livingEntity.igniteForSeconds(scaledIgniteDuration);
            }
            else if (item == ModItems.SPLASH_MILK_BOTTLE.get()) {
                if (livingEntity instanceof Player player && !livingEntity.getActiveEffects().isEmpty()) {
                    if (!level.isClientSide()) {
                        HeatAccessor heatAccessor = (HeatAccessor) player;
                        heatAccessor.pinferno$decreaseHeat(5);
                    }
                }
                livingEntity.removeAllEffects();
            }
            else if (item == ModItems.SPLASH_HONEY_BOTTLE.get()) {
                int baseDuration = 8 * 20;
                int finalDuration = (int) (scale * baseDuration + 0.5);

                if (finalDuration > 20) livingEntity.addEffect(new MobEffectInstance(ModEffects.hinderedHolder(), finalDuration, 0), this.getEffectSource());
                livingEntity.removeEffect(ModEffects.fumigatedHolder());
                livingEntity.removeEffect(MobEffects.HUNGER);
                livingEntity.removeEffect(MobEffects.POISON);
            }
        }

        level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.SPLASH_POTION_BREAK, SoundSource.NEUTRAL, 1.0F, 1.0F);
        spawnImpactParticles(level, item);
        this.discard();
    }

    private void spawnImpactParticles(ServerLevel level, Item item) {
        var pos = this.position();

        if (item == ModItems.SPLASH_LAVA_BOTTLE.get()) {
            level.sendParticles(
                    ParticleTypes.LAVA,
                    pos.x(), pos.y(), pos.z(),
                    16, 0.8, 0.2, 0.8, 0.02
            );
        }
        else if (item == ModItems.SPLASH_MILK_BOTTLE.get()) {
            level.sendParticles(
                    ParticleTypes.ITEM_SNOWBALL,
                    pos.x(), pos.y(), pos.z(),
                    16, 0.8, 0.2, 0.8, 0.02
            );
        }
        else if (item == ModItems.SPLASH_HONEY_BOTTLE.get()) {
            level.sendParticles(
                    ParticleTypes.LANDING_HONEY,
                    pos.x(), pos.y(), pos.z(),
                    16, 0.8, 0.2, 0.8, 0.02
            );
        }
    }
}
