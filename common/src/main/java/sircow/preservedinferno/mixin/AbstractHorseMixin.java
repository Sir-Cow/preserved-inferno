package sircow.preservedinferno.mixin;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.equine.Mule;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.trigger.ModTriggers;

import java.util.Objects;

@Mixin(AbstractHorse.class)
public class AbstractHorseMixin {
    @Unique private double pinferno$horizontalDistance, pinferno$verticalDistance, pinferno$lastX, pinferno$lastY, pinferno$lastZ;
    @Unique private int pinferno$movementParticleTicks, pinferno$jumpParticleTicks;
    @Unique private boolean pinferno$hasLastPosition;

    @Inject(method = "createBaseHorseAttributes", at = @At("RETURN"), cancellable = true)
    private static void pinferno$overwriteAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.setReturnValue(Animal.createAnimalAttributes()
                .add(Attributes.JUMP_STRENGTH, 0.7)
                .add(Attributes.MAX_HEALTH, 53.0)
                .add(Attributes.MOVEMENT_SPEED, 0.225F)
                .add(Attributes.STEP_HEIGHT, 1.1)
                .add(Attributes.SAFE_FALL_DISTANCE, 6.0)
                .add(Attributes.FALL_DAMAGE_MULTIPLIER, 0.5)
        );
    }

    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    private void pinferno$changeEquineSpawnStats(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData, CallbackInfoReturnable<SpawnGroupData> cir) {
        AbstractHorse horse = (AbstractHorse)(Object)this;

        if (!(horse instanceof Mule)) return;

        Objects.requireNonNull(horse.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(20.0F + horse.getRandom().nextInt(6));
        Objects.requireNonNull(horse.getAttribute(Attributes.JUMP_STRENGTH)).setBaseValue(0.5F + horse.getRandom().nextDouble() * 0.25F);
    }

    @Inject(method = "tickRidden", at = @At("TAIL"))
    private void pinferno$trainEquine(Player player, Vec3 riddenInput, CallbackInfo ci) {
        AbstractHorse horse = (AbstractHorse) (Object) this;

        if (horse.level().isClientSide()) return;

        if (!pinferno$hasLastPosition) {
            pinferno$lastX = horse.getX();
            pinferno$lastY = horse.getY();
            pinferno$lastZ = horse.getZ();
            pinferno$hasLastPosition = true;
            return;
        }

        double dx = horse.getX() - pinferno$lastX;
        double dy = horse.getY() - pinferno$lastY;
        double dz = horse.getZ() - pinferno$lastZ;

        pinferno$lastX = horse.getX();
        pinferno$lastY = horse.getY();
        pinferno$lastZ = horse.getZ();

        pinferno$horizontalDistance += Math.sqrt(dx * dx + dz * dz);
        pinferno$verticalDistance += Math.abs(dy);

        if (pinferno$horizontalDistance >= 750.0D) {
            pinferno$horizontalDistance -= 750.0D;

            if (horse.getRandom().nextFloat() < 0.3F) {
                var attribute = horse.getAttribute(Attributes.MOVEMENT_SPEED);

                if (attribute != null) {
                    double oldValue = attribute.getBaseValue();
                    double newValue = Math.min(oldValue + 0.025D, 0.4633D);

                    if (newValue > oldValue) {
                        attribute.setBaseValue(newValue);
                        pinferno$onMovementSpeedIncrease();
                        if (player instanceof ServerPlayer serverPlayer) {
                            ModTriggers.UPGRADE_HORSE_ATTRIBUTE.trigger(serverPlayer);
                        }
                        pinferno$checkMaxHorseStats(horse, player);
                    }
                }
            }
        }

        if (pinferno$verticalDistance >= 750.0D) {
            pinferno$verticalDistance -= 750.0D;

            if (horse.getRandom().nextFloat() < 0.5F) {
                var attribute = horse.getAttribute(Attributes.JUMP_STRENGTH);

                if (attribute != null) {
                    double oldValue = attribute.getBaseValue();
                    double newValue = Math.min(oldValue + 0.04D, 1.0D);

                    if (newValue > oldValue) {
                        attribute.setBaseValue(newValue);
                        pinferno$onJumpStrengthIncrease();
                        if (player instanceof ServerPlayer serverPlayer) {
                            ModTriggers.UPGRADE_HORSE_ATTRIBUTE.trigger(serverPlayer);
                        }
                        pinferno$checkMaxHorseStats(horse, player);
                    }
                }
            }
        }
    }

    @Overwrite
    public static double createOffspringAttribute(double parentAValue, double parentBValue, double attributeRangeMin, double attributeRangeMax, RandomSource random) {
        if (attributeRangeMax <= attributeRangeMin) throw new IllegalArgumentException("Incorrect range for an attribute");

        parentAValue = Mth.clamp(parentAValue, attributeRangeMin, attributeRangeMax);
        parentBValue = Mth.clamp(parentBValue, attributeRangeMin, attributeRangeMax);

        double average = (parentAValue + parentBValue) * 0.5;
        double multiplier = 1.0 + (random.nextDouble() * 0.2 - 0.1);

        double value = average * multiplier;
        value = Mth.clamp(value, attributeRangeMin, attributeRangeMax);

        return value;
    }

    @Inject(method = "setOffspringAttributes", at = @At("TAIL"))
    private void pinferno$reduceBabyHealth(AgeableMob partner, AbstractHorse baby, CallbackInfo ci) {
        var health = baby.getAttribute(Attributes.MAX_HEALTH);

        if (health != null) health.setBaseValue(Math.max(health.getBaseValue() * 0.75, 1.0));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void pinferno$tickParticles(CallbackInfo ci) {
        AbstractHorse horse = (AbstractHorse) (Object) this;

        if (!(horse.level() instanceof ServerLevel)) return;

        if (pinferno$movementParticleTicks > 0) {
            pinferno$movementParticleTicks--;
            addParticlesAroundSelf(horse, ParticleTypes.HAPPY_VILLAGER);
        }

        if (pinferno$jumpParticleTicks > 0) {
            pinferno$jumpParticleTicks--;
            addParticlesAroundSelf(horse, ParticleTypes.GLOW);
        }
    }

    @Unique
    private void pinferno$onMovementSpeedIncrease() {
        pinferno$movementParticleTicks = 10;
    }

    @Unique
    private void pinferno$onJumpStrengthIncrease() {
        pinferno$jumpParticleTicks = 10;
    }

    @Unique
    protected void addParticlesAroundSelf(AbstractHorse horse, final ParticleOptions particle) {
        if (!(horse.level() instanceof ServerLevel level)) return;

        level.sendParticles(particle, horse.getX(), horse.getY() + horse.getBbHeight() * 0.5, horse.getZ(),
                5, 0.7, 0.5, 0.7, 0.02
        );
    }

    @Unique
    private void pinferno$checkMaxHorseStats(AbstractHorse horse, Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        var speed = horse.getAttribute(Attributes.MOVEMENT_SPEED);
        var jump = horse.getAttribute(Attributes.JUMP_STRENGTH);
        var health = horse.getAttribute(Attributes.MAX_HEALTH);

        if (speed == null || jump == null || health == null) return;

        if (speed.getBaseValue() >= 0.4633D && jump.getBaseValue() >= 1.0D && health.getBaseValue() >= 40.0D) {
            ModTriggers.MAX_HORSE_STATS.trigger(serverPlayer);
        }
    }
}
