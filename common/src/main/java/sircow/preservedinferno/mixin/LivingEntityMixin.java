package sircow.preservedinferno.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.effect.ModEffects;
import sircow.preservedinferno.enchantment.ModEnchantments;
import sircow.preservedinferno.item.custom.PreservedShieldItem;
import sircow.preservedinferno.other.FreezeAccessor;
import sircow.preservedinferno.other.cooldowns.ElytraCooldown;

import java.util.Objects;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow public abstract boolean hasEffect(Holder<MobEffect> effect);
    @Shadow public abstract MobEffectInstance getEffect(Holder<MobEffect> effect);
    @Shadow public abstract AttributeInstance getAttribute(Holder<Attribute> attribute);
    @Unique private float preDamageHealth;
    @Unique private static final Identifier HINDERED_SPEED_ID = Constants.id("hindered_speed");
    @Unique private static final Identifier HINDERED_ATTACK_ID = Constants.id("hindered_attack");

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At("HEAD"), cancellable = true)
    private void pinferno$preventEffectsOnShieldBlock(MobEffectInstance newEffect, Entity source, CallbackInfoReturnable<Boolean> cir) {
        if ((LivingEntity)(Object)this instanceof Player player) {
            if (player.isBlocking()) {
                if (source instanceof Monster) cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "knockback(DDDLnet/minecraft/world/damagesource/DamageSource;FZ)V", at = @At("HEAD"), cancellable = true)
    private void pinferno$cancelKnockback(double power, double xd, double zd, final DamageSource source, final float damage, final boolean comesFromEffect, CallbackInfo ci) {
        if ((Object)this instanceof Player player) {
            if (player.isBlocking() && player.getUseItem().getItem() instanceof PreservedShieldItem) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "travel", at = @At("HEAD"))
    private void pinferno$applyHindered(Vec3 input, CallbackInfo ci) {
        if (!this.hasEffect(ModEffects.HINDERED.holder)) {
            AttributeInstance speedAttr = this.getAttribute(Attributes.MOVEMENT_SPEED);
            if (speedAttr != null && speedAttr.getModifier(HINDERED_SPEED_ID) != null) speedAttr.removeModifier(HINDERED_SPEED_ID);
            AttributeInstance atk = this.getAttribute(Attributes.ATTACK_SPEED);
            if (atk != null && atk.getModifier(HINDERED_ATTACK_ID) != null) atk.removeModifier(HINDERED_ATTACK_ID);
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
    private void pinferno$onDamage(ServerLevel serverLevel, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity)(Object)this;
        this.preDamageHealth = self.getHealth();
        // hindered effect on freeze
        if (!self.level().isClientSide() && source.is(DamageTypes.FREEZE)) {
            MobEffectInstance newInstance = new MobEffectInstance(ModEffects.HINDERED.holder, 160, 0, false, true, true);
            self.addEffect(newInstance);
        }
    }

    @Inject(method = "hurtServer", at = @At("RETURN"))
    private void pinferno$onDamage2(ServerLevel serverLevel, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ()) return;

        LivingEntity self = (LivingEntity)(Object)this;

        // elytra cooldown
        if (self.getHealth() >= this.preDamageHealth) return;
        if (self instanceof ServerPlayer serverPlayer) ElytraCooldown.applyCooldown(serverPlayer, 20 * 12);
    }

    @Inject(method = "canGlide", at = @At("HEAD"), cancellable = true)
    private void pinferno$disableElytraOnCooldown(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity)(Object)this;

        if (!(self instanceof Player player)) return;
        if (player.getCooldowns().isOnCooldown(new ItemStack(Items.ELYTRA))) cir.setReturnValue(false);
    }

    @Inject(method = "jumpFromGround", at = @At("TAIL"))
    private void pinferno$reduceJumpHeight(CallbackInfo ci) {
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
    private void pinferno$modifyMobDrops(ServerLevel level, DamageSource damageSource, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof Player) return; // if the entity is a player, always allow drops
        else if (damageSource.getEntity() instanceof Player // allow drops by mob
                || damageSource.getEntity() instanceof IronGolem
                || damageSource.getEntity() instanceof Frog
                || (damageSource.getEntity() instanceof Skeleton && entity instanceof Creeper)
        ) return;

        ci.cancel();
    }

    @Inject(method = "getItemBlockingWith", at = @At("HEAD"), cancellable = true)
    private void pinferno$bucklerEnchant(CallbackInfoReturnable<ItemStack> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (!entity.isUsingItem()) return;

        ItemStack stack = entity.getUseItem();
        if (!(stack.getItem() instanceof PreservedShieldItem)) return;

        BlocksAttacks blocks = stack.get(DataComponents.BLOCKS_ATTACKS);
        if (blocks == null) return;

        int usedTicks = stack.getUseDuration(entity) - entity.getUseItemRemainingTicks();
        int delay = stack.getEnchantments().getLevel(level().registryAccess().lookupOrThrow(ModEnchantments.BUCKLER.registryKey()).getOrThrow(ModEnchantments.BUCKLER)) > 0 ? 1 : blocks.blockDelayTicks();

        if (usedTicks >= delay) cir.setReturnValue(stack);
        else cir.setReturnValue(null);
    }

    @ModifyConstant(method = "aiStep", constant = @Constant(intValue = 40))
    private int pinferno$freezeDamageInterval(int original) {
        return 1;
    }

    @Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setTicksFrozen(I)V"))
    private void pinferno$delayThawing(LivingEntity entity, int ticksFrozen) {
        FreezeAccessor access = (FreezeAccessor) entity;

        if (ticksFrozen < entity.getTicksFrozen() && access.pinferno$getFreezeDelay() < 20) return;

        entity.setTicksFrozen(ticksFrozen);
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void pinferno$tickFreezeDelay(CallbackInfo ci) {
        FreezeAccessor access = (FreezeAccessor) this;

        if (access.pinferno$getFreezeDelay() < 20) access.pinferno$setFreezeDelay(access.pinferno$getFreezeDelay() + 1);
    }
}
