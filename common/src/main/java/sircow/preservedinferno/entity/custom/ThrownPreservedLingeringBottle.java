package sircow.preservedinferno.entity.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.entity.ModEntities;
import sircow.preservedinferno.item.ModItems;

public class ThrownPreservedLingeringBottle extends ThrowableItemProjectile {
    public ThrownPreservedLingeringBottle(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super(type, level);
    }

    public ThrownPreservedLingeringBottle(Level level, LivingEntity owner, ItemStack stack) {
        super(ModEntities.PRESERVED_LINGERING_BOTTLE, owner, level, stack);
    }

    public ThrownPreservedLingeringBottle(Level level, double x, double y, double z, ItemStack stack) {
        super(ModEntities.PRESERVED_LINGERING_BOTTLE, x, y, z, level, stack);
    }

    @Override
    protected @NonNull Item getDefaultItem() {
        return Items.GLASS_BOTTLE;
    }

    @Override
    protected double getDefaultGravity() {
        return 0.05;
    }

    @Override
    protected void onHit(@NonNull HitResult hitResult) {
        super.onHit(hitResult);

        if (!(this.level() instanceof ServerLevel level)) return;

        Item item = this.getItem().getItem();

        AreaEffectCloud cloud;

        if (item == ModItems.LINGERING_HONEY_BOTTLE.get() || item == ModItems.LINGERING_LAVA_BOTTLE.get() || item == ModItems.LINGERING_MILK_BOTTLE.get()) cloud = new PreservedAreaEffectCloud(level, this.getX(), this.getY(), this.getZ(), item);
        else cloud = new AreaEffectCloud(level, this.getX(), this.getY(), this.getZ());

        if (this.getOwner() instanceof LivingEntity owner) cloud.setOwner(owner);

        cloud.setRadius(3.0F);
        cloud.setRadiusOnUse(-0.5F);
        cloud.setDuration(600);
        cloud.setWaitTime(10);
        cloud.setRadiusPerTick(-cloud.getRadius() / cloud.getDuration());

        level.addFreshEntity(cloud);
        level.playSound(null, this.blockPosition(), SoundEvents.SPLASH_POTION_BREAK, SoundSource.NEUTRAL, 1.0F, 1.0F);

        this.discard();
    }
}
