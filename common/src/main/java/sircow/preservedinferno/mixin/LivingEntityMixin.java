package sircow.preservedinferno.mixin;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.effect.ModEffects;
import sircow.preservedinferno.item.custom.PreservedShieldItem;

import java.util.Objects;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow public abstract boolean hasEffect(Holder<MobEffect> effect);
    @Shadow public abstract MobEffectInstance getEffect(Holder<MobEffect> effect);
    @Shadow public abstract AttributeInstance getAttribute(Holder<Attribute> attribute);

    @Unique private static final ResourceLocation HINDERED_SPEED_ID = Constants.id("hindered_speed");
    @Unique private static final ResourceLocation HINDERED_ATTACK_ID = Constants.id("hindered_attack");


    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$preventEffectsOnShieldBlock(MobEffectInstance effectInstance, Entity source, CallbackInfoReturnable<Boolean> cir) {
        if ((LivingEntity)(Object)this instanceof Player player) {
            if (player.isBlocking()) {
                if (source instanceof Monster) {
                    cir.setReturnValue(false);
                }
            }
        }
    }

    @Inject(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V", shift = At.Shift.BEFORE), cancellable = true)
    private void preserved_inferno$cancelKnockbackIfBlockingWithCustomShield(ServerLevel level, DamageSource damageSource, float amount, CallbackInfoReturnable<Boolean> cir) {
        if ((Object)this instanceof Player player) {
            if (player.isBlocking()) {
                ItemStack blockingItem = player.getUseItem();
                if (blockingItem.getItem() instanceof PreservedShieldItem) {
                    cir.setReturnValue(true);
                }
            }
        }
    }

    @Inject(method = "travel", at = @At("HEAD"))
    private void preserved_inferno$applyHindered(Vec3 travelVector, CallbackInfo ci) {
        if (!this.hasEffect(ModEffects.HINDERED.holder)) {
            AttributeInstance speedAttr = this.getAttribute(Attributes.MOVEMENT_SPEED);
            if (speedAttr != null && speedAttr.getModifier(HINDERED_SPEED_ID) != null) {
                speedAttr.removeModifier(HINDERED_SPEED_ID);
            }
            AttributeInstance atk = this.getAttribute(Attributes.ATTACK_SPEED);
            if (atk != null && atk.getModifier(HINDERED_ATTACK_ID) != null) {
                atk.removeModifier(HINDERED_ATTACK_ID);
            }
            return;
        }

        int level = Objects.requireNonNull(this.getEffect(ModEffects.HINDERED.holder)).getAmplifier() + 1;
        AttributeInstance speedAttr = this.getAttribute(Attributes.MOVEMENT_SPEED);

        if (speedAttr.getModifier(HINDERED_SPEED_ID) == null) {
            speedAttr.addPermanentModifier(new AttributeModifier(
                    HINDERED_SPEED_ID,
                    -Math.min(1.0, 0.1 * level),
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            ));
        }

        AttributeInstance atk = this.getAttribute(Attributes.ATTACK_SPEED);
        if (atk != null && atk.getModifier(HINDERED_ATTACK_ID) == null) {
            atk.addPermanentModifier(new AttributeModifier(
                    HINDERED_ATTACK_ID,
                    -0.1 * level,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            ));
        }

        this.setSprinting(false);
    }

    @Inject(method = "hurtServer", at = @At("HEAD"))
    private void preserved_inferno$applyHinderedOnFrostDamage(ServerLevel serverLevel, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity)(Object)this;

        if (!self.level().isClientSide() && source.is(DamageTypes.FREEZE)) {
            MobEffectInstance newInstance = new MobEffectInstance(ModEffects.HINDERED.holder, 160, 0, false, true, true);
            self.addEffect(newInstance);
        }
    }

    @Inject(method = "jumpFromGround", at = @At("TAIL"))
    private void preserved_inferno$reduceJumpHeight(CallbackInfo ci) {
        if (this.hasEffect(ModEffects.HINDERED.holder)) {
            int level = this.getEffect(ModEffects.HINDERED.holder).getAmplifier() + 1;
            double heightFraction = Math.pow(0.3, level);
            float baseJump = 0.42F;
            float jumpVelocity = (float) (baseJump * Math.sqrt(heightFraction));
            Vec3 current = this.getDeltaMovement();

            this.setDeltaMovement(current.x, jumpVelocity, current.z);
        }
    }

    @Inject(method = "dropAllDeathLoot", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$modifyMobDrops(ServerLevel level, DamageSource damageSource, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof Player) {
            // if the entity is a player, always allow drops
        }
        else if (damageSource.getEntity() instanceof Player || damageSource.getEntity() instanceof IronGolem) {
            // if the entity is a mob and killed by a player or iron golem, allow drops
        }
        else {
            // otherwise, cancel drops
            ci.cancel();
        }
    }
}
