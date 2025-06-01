package sircow.preservedinferno.entity.custom;

import com.mojang.serialization.DataResult;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.components.ModComponents;
import sircow.preservedinferno.entity.ModEntities;
import sircow.preservedinferno.item.ModItems;

public class FlareGunProjectileEntity extends ThrowableItemProjectile {
    private static final String TAG_FIRED_FROM = "FiredFromItem";
    private ItemStack firedFrom;

    public FlareGunProjectileEntity(EntityType<? extends FlareGunProjectileEntity> entityType, Level level) {
        super(entityType, level);
        this.firedFrom = ItemStack.EMPTY;
    }

    public FlareGunProjectileEntity(Level level, LivingEntity owner, ItemStack item) {
        super(ModEntities.FLARE_GUN_PROJECTILE, owner, level, item);
        this.setItem(item);
        if (owner.getItemInHand(InteractionHand.MAIN_HAND).getItem() == ModItems.FLARE_GUN) {
            firedFrom = owner.getItemInHand(InteractionHand.MAIN_HAND);
        }
        else if (owner.getItemInHand(InteractionHand.OFF_HAND).getItem() == ModItems.FLARE_GUN) {
            firedFrom = owner.getItemInHand(InteractionHand.OFF_HAND);
        }
        else {
            firedFrom = ItemStack.EMPTY;
        }
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        double d0 = Math.sqrt(x * x + y * y + z * z);
        if (d0 == 0) d0 = 1;
        x /= d0;
        y /= d0;
        z /= d0;

        this.setDeltaMovement(x * velocity, y * velocity, z * velocity);
    }

    @Override
    public void shootFromRotation(Entity shooter, float x, float y, float z, float velocity, float inaccuracy) {
        float f = -shooter.getXRot() * ((float)Math.PI / 180F);
        float f1 = -shooter.getYRot() * ((float)Math.PI / 180F);
        float f2 = Mth.cos(f1);
        float f3 = Mth.sin(f1);
        float f4 = Mth.cos(f);
        float f5 = Mth.sin(f);
        double d0 = f3 * f4;
        double d1 = f5;
        double d2 = f2 * f4;

        this.setDeltaMovement(d0 * velocity, d1 * velocity, d2 * velocity);
        this.setPos(shooter.getX(), shooter.getEyeY() - 0.1, shooter.getZ());
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return Items.FIREWORK_ROCKET;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (this.firedFrom.isEmpty()) {
                Constants.LOG.error("FlareGunProjectileEntity ticked with null or empty firedFrom ItemStack. Discarding.");
                this.discard();
                return;
            }
            if (this.level() instanceof ServerLevel serverLevel) {
                String colourString = firedFrom.get(ModComponents.FLARE_PARTICLE_COMPONENT);
                int particleColour = 0xFFFFFF;

                if (colourString != null && colourString.startsWith("#")) {
                    try {
                        particleColour = Integer.parseInt(colourString.substring(1), 16);
                    }
                    catch (NumberFormatException e) {
                        Constants.LOG.error("Invalid flare particle colour format: {} Please report this!", colourString);
                    }
                }

                ClientboundLevelParticlesPacket packet = getClientboundLevelParticlesPacket(particleColour);
                for (ServerPlayer player : serverLevel.players()) {
                    player.connection.send(packet);
                }
            }

            if (this.tickCount >= 80) {
                this.discard();
            }
        }
    }

    private @NotNull ClientboundLevelParticlesPacket getClientboundLevelParticlesPacket(int particleColour) {
        float particleScale = (float) this.tickCount / 5;
        DustParticleOptions whiteDust = new DustParticleOptions(particleColour, particleScale);

        return new ClientboundLevelParticlesPacket(
                whiteDust,
                true,
                true,
                this.getX(),
                this.getY(),
                this.getZ(),
                0.0F,
                0.0F,
                0.0F,
                0.0F,
                10
        );
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide) {
            if (result.getEntity() instanceof EnderMan enderman) {
                Vec3 oldPos = enderman.position();
                Vec3 vec3 = new Vec3(enderman.getX() - this.getX(), enderman.getY() - this.getY(), enderman.getZ() - this.getZ());
                vec3 = vec3.normalize();

                double range = 16.0;
                double eX = enderman.getX() + (enderman.getRandom().nextDouble() - 0.5) * 8.0 - vec3.x * range;
                double eY = enderman.getY() + (enderman.getRandom().nextInt(16) - 8) - vec3.y * range;
                double eZ = enderman.getZ() + (enderman.getRandom().nextDouble() - 0.5) * 8.0 - vec3.z * range;

                if (enderman.randomTeleport(eX, eY, eZ, true)) {
                    enderman.level().gameEvent(GameEvent.TELEPORT, oldPos, GameEvent.Context.of(enderman));
                    if (!enderman.isSilent()) {
                        enderman.level().playSound(null, oldPos.x, oldPos.y, oldPos.z, SoundEvents.ENDERMAN_TELEPORT, enderman.getSoundSource(), 1.0F, 1.0F);
                        enderman.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    }
                    enderman.getNavigation().stop();
                    this.discard();
                    return;
                }
                else {
                    this.discard();
                    return;
                }
            }

            if (result.getEntity() instanceof LivingEntity livingEntity) {
                livingEntity.invulnerableTime = 0;

                if (livingEntity.isOnFire()) {
                    livingEntity.clearFire();
                    livingEntity.igniteForTicks(60);
                    if (this.getOwner() instanceof Player) {
                        result.getEntity().hurt(this.damageSources().playerAttack((Player) this.getOwner()), 6.0F);
                    }
                    else {
                        result.getEntity().hurt(this.damageSources().generic(), 6.0F);
                    }
                }
                else {
                    livingEntity.igniteForTicks(60);
                    if (this.getOwner() instanceof Player) {
                        result.getEntity().hurt(this.damageSources().playerAttack((Player) this.getOwner()), 2.0F);
                    }
                    else {
                        result.getEntity().hurt(this.damageSources().generic(), 2.0F);
                    }
                }
            }
            this.discard();
        }
    }

    @Override
    protected void onHit(@NotNull HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
        }
    }

    @Override
    protected double getDefaultGravity() {
        return 0.00;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < (128 * 128);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.firedFrom != null && !this.firedFrom.isEmpty()) {
            RegistryOps<Tag> registryOps = this.registryAccess().createSerializationContext(NbtOps.INSTANCE);
            DataResult<Tag> result = ItemStack.CODEC.encodeStart(registryOps, this.firedFrom);
            result.resultOrPartial(error -> System.err.println("Failed to encode firedFrom ItemStack: " + error))
                    .ifPresent(tag -> compound.put(TAG_FIRED_FROM, tag));
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        RegistryOps<Tag> registryOps = this.registryAccess().createSerializationContext(NbtOps.INSTANCE);

        if (compound.contains(TAG_FIRED_FROM)) {
            Tag tag = compound.get(TAG_FIRED_FROM);

            if (tag != null && tag.getId() == Tag.TAG_COMPOUND) {
                this.firedFrom = ItemStack.CODEC.parse(registryOps, tag)
                        .resultOrPartial(error -> System.err.println("Failed to decode firedFrom ItemStack: " + error))
                        .orElse(ItemStack.EMPTY);
            }
            else {
                System.err.println("Found tag '" + TAG_FIRED_FROM + "' but it's not a CompoundTag or is null. Setting to empty ItemStack.");
                this.firedFrom = ItemStack.EMPTY;
            }
        }
        else {
            this.firedFrom = ItemStack.EMPTY;
        }
    }
}
