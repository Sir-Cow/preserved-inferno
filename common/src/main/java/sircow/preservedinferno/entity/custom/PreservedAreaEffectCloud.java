package sircow.preservedinferno.entity.custom;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import sircow.preservedinferno.effect.ModEffects;
import sircow.preservedinferno.entity.ModEntities;
import sircow.preservedinferno.item.ModItems;
import sircow.preservedinferno.other.HeatAccessor;

public class PreservedAreaEffectCloud extends AreaEffectCloud {
    private Item bottleItem;

    public PreservedAreaEffectCloud(EntityType<? extends AreaEffectCloud> type, Level level) {
        super(type, level);
    }

    public PreservedAreaEffectCloud(Level level, double x, double y, double z, Item bottleItem) {
        super(ModEntities.PRESERVED_AREA_EFFECT_CLOUD, level);
        this.setPos(x, y, z);
        this.bottleItem = bottleItem;

        if (bottleItem == ModItems.LINGERING_HONEY_BOTTLE) this.setCustomParticle(ParticleTypes.LANDING_HONEY);
        else if (bottleItem == ModItems.LINGERING_LAVA_BOTTLE) this.setCustomParticle(ParticleTypes.LAVA);
        else if (bottleItem == ModItems.LINGERING_MILK_BOTTLE) this.setCustomParticle(ParticleTypes.ITEM_SNOWBALL);
    }

    @Override
    public void tick() {
        if (this.level().isClientSide()) {
            this.tickCustomParticles();
            return;
        }

        super.tick();

        if (this.isWaiting() || this.tickCount % 5 != 0) return;
        if (!(this.level() instanceof ServerLevel level)) return;

        float radius = this.getRadius();
        var entities = level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(radius));
        boolean consumedStage = false;

        for (LivingEntity livingEntity : entities) {
            double xd = livingEntity.getX() - this.getX();
            double zd = livingEntity.getZ() - this.getZ();
            double dist = xd * xd + zd * zd;

            if (dist > radius * radius) continue;

            if (this.bottleItem == ModItems.LINGERING_HONEY_BOTTLE) {
                livingEntity.addEffect(new MobEffectInstance(ModEffects.HINDERED.holder, 160, 0));
                livingEntity.removeEffect(MobEffects.POISON);
                consumedStage = true;
            }
            else if (this.bottleItem == ModItems.LINGERING_LAVA_BOTTLE && !livingEntity.fireImmune()) {
                livingEntity.igniteForSeconds(15.0F);
                consumedStage = true;
            }
            else if (this.bottleItem == ModItems.LINGERING_MILK_BOTTLE) {
                if (livingEntity instanceof Player player && !livingEntity.getActiveEffects().isEmpty()) {
                    if (!level.isClientSide()) {
                        HeatAccessor heatAccessor = (HeatAccessor) player;
                        heatAccessor.pinferno$decreaseHeat(5);
                    }
                }
                if (!livingEntity.getActiveEffects().isEmpty()) {
                    livingEntity.removeAllEffects();
                    consumedStage = true;
                }
            }
        }

        if (consumedStage) {
            this.setRadius(this.getRadius() + this.getRadiusOnUse());

            if (this.getRadius() < 0.5F) {
                this.discard();
            }
        }
    }

    private void tickCustomParticles() {
        if (this.random.nextBoolean()) return;

        ParticleOptions particle = this.getParticle();
        float radius = this.getRadius();

        int particleCount = 8;

        for (int i = 0; i < particleCount; i++) {
            float angle = this.random.nextFloat() * ((float) Math.PI * 2F);
            float distance = Mth.sqrt(this.random.nextFloat()) * radius;

            double x = this.getX() + Mth.cos(angle) * distance;
            double y = this.getY() + 0.05;
            double z = this.getZ() + Mth.sin(angle) * distance;

            this.level().addAlwaysVisibleParticle(particle, x, y, z, 0.0, 0.01, 0.0);
        }
    }
}
