package sircow.preservedinferno.mixin;

import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.effect.ModEffects;
import sircow.preservedinferno.enchantment.ModEnchantments;
import sircow.preservedinferno.item.custom.PreservedShieldItem;
import sircow.preservedinferno.other.HeatAccessor;
import sircow.preservedinferno.other.ModDamageTypes;
import sircow.preservedinferno.other.ModEntityData;
import sircow.preservedinferno.other.ShieldStaminaHandler;
import sircow.preservedinferno.sound.ModSounds;
import sircow.preservedinferno.trade.PlayerMixinAccess;
import sircow.preservedinferno.trade.VillagerProfessionBits;
import sircow.preservedinferno.trigger.ModTriggers;

import java.util.Objects;
import java.util.Optional;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements HeatAccessor, PlayerMixinAccess {
    @Unique private boolean hasWaterBreathingFromHelmet;
    @Unique private int heatIncreaseTickCounter, heatDecreaseTickCounter, heatDamageTickCounter;
    @Unique private Vec3 rideStartPos;
    @Unique private static final int INCREASE_CAP = 120;
    @Unique private static final int DECREASE_CAP = 80;
    @Unique private static final int IN_WATER_CAP_REDUCTION = 10;
    @Unique private static final int FIRE_RES_INCREASE = 48;
    @Unique private static final int FIRE_PROT_INCREASE = 8;
    @Unique private @Nullable BlockPos lastSteppedOnIcePos;
    @Unique DamageSource heatDamageSource = ModDamageTypes.of(level(), ModDamageTypes.HEAT);

    @Shadow @Nullable public abstract GameType gameMode();
    @Shadow protected abstract boolean canCriticalAttack(Entity entity);
    @Shadow public abstract boolean isCreative();

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @ModifyVariable(method = "hurtServer", at = @At("HEAD"), argsOnly = true, name = "damage")
    private float pinferno$damageIntercept(float damage, ServerLevel level, DamageSource damageSource) {
        Player player = (Player)(Object)this;

        if (damageSource.is(DamageTypes.ON_FIRE) && !player.hasEffect(MobEffects.FIRE_RESISTANCE) && player.level().dimension() == Level.NETHER) pinferno$increaseHeat(1);
        ItemStack blockingStack = player.getUseItem();

        if (player.isBlocking() && blockingStack.getItem() instanceof PreservedShieldItem) {
            Vec3 sourcePos;
            if (damageSource.getEntity() != null) sourcePos = damageSource.getEntity().position();
            else sourcePos = damageSource.getSourcePosition();

            // make shield only block damage from 120°
            if (sourcePos != null) {
                Vec3 playerPos = player.position();
                Vec3 toSource = new Vec3(sourcePos.x - playerPos.x, sourcePos.y - playerPos.y, sourcePos.z - playerPos.z);

                double horizX = toSource.x;
                double horizZ = toSource.z;
                double horizLen = Math.sqrt(horizX * horizX + horizZ * horizZ);

                if (horizLen > 1e-6) {
                    toSource = new Vec3(horizX / horizLen, 0.0D, horizZ / horizLen);
                    Vec3 look = player.getViewVector(1.0F);
                    double lookX = look.x;
                    double lookZ = look.z;
                    double lookLen = Math.sqrt(lookX * lookX + lookZ * lookZ);

                    if (lookLen > 1e-6) look = new Vec3(lookX / lookLen, 0.0D, lookZ / lookLen);
                    else look = new Vec3(0.0D, 0.0D, 0.0D);

                    double dot = toSource.x * look.x + toSource.z * look.z;

                    if (dot < 0.5D) return damage;
                }
            }

            float currentStamina = player.getEntityData().get(ModEntityData.PLAYER_SHIELD_STAMINA);

            if (damageSource.is(DamageTypeTags.IS_FIRE) || damageSource.is(DamageTypes.ON_FIRE)) {
                player.clearFire();
                player.setSharedFlagOnFire(false);
            }

            if (damageSource.is(DamageTypeTags.BYPASSES_SHIELD)) {
                ShieldStaminaHandler.lastBypassingSource = damageSource;
                return damage;
            }
            else {
                ShieldStaminaHandler.lastBypassingSource = null;
                float finalDamageToApply = Math.max(0, damage - currentStamina);
                float newStamina = Math.max(0, currentStamina - damage);

                if (newStamina != currentStamina) player.getEntityData().set(ModEntityData.PLAYER_SHIELD_STAMINA, newStamina);

                if (newStamina <= 0) ShieldStaminaHandler.triggerCooldown(player, blockingStack);

                Optional<BlocksAttacks> blocksAttacks = Optional.ofNullable(blockingStack.get(DataComponents.BLOCKS_ATTACKS));
                if (blocksAttacks.isPresent()) {
                    blocksAttacks.get().onBlocked(level, player);
                    blocksAttacks.get().hurtBlockingItem(level, blockingStack, player, player.getUsedItemHand(), damage);
                }

                if (player instanceof ServerPlayer serverPlayer) {
                    CriteriaTriggers.ENTITY_HURT_PLAYER.trigger(serverPlayer, damageSource, damage, damage, true);
                    serverPlayer.awardStat(Stats.DAMAGE_BLOCKED_BY_SHIELD, Math.round(damage * 10.0F));
                }
                return finalDamageToApply;
            }
        }
        return damage;
    }

    @Inject(method = "hurtServer", at = @At("RETURN"))
    private void pinferno$freezeCoolDown(ServerLevel level, DamageSource damageSource, float damage, CallbackInfoReturnable<Boolean> cir) {
        Player player = (Player)(Object)this;
        if (damageSource.is(DamageTypes.FREEZE) && cir.getReturnValue()) {
            if (player instanceof ServerPlayer serverPlayer && pinferno$getHeat() >= 1) ModTriggers.FREEZE_COOL.get().trigger(serverPlayer);
            pinferno$decreaseHeat(10);
        }
    }

    // modify bed sleeping
    @ModifyConstant(method = "tick", constant = @Constant(intValue = 100))
    private int pinferno$modifyIntValue(int original) {
        return 200;
    }
    @ModifyConstant(method = "tick", constant = @Constant(intValue = 110))
    private int pinferno$modifyIntValue2(int original) {
        return 210;
    }
    @ModifyConstant(method = "stopSleepInBed", constant = @Constant(intValue = 100))
    private int pinferno$modifyIntValue3(int original) {
        return 200;
    }
    @ModifyConstant(method = "isSleepingLongEnough", constant = @Constant(intValue = 100))
    private int pinferno$modifyIntValue4(int original) {
        return 200;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void pinferno$tick(CallbackInfo ci) {
        Player player = (Player)(Object)this;
        // turtle helmet
        boolean isInWater = this.isEyeInFluid(FluidTags.WATER);
        boolean isWearingHelmet = this.getItemBySlot(EquipmentSlot.HEAD).is(Items.TURTLE_HELMET);

        if (isInWater && isWearingHelmet) {
            if (!hasWaterBreathingFromHelmet) {
                this.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, -1, 0, false, false, true));
                hasWaterBreathingFromHelmet = true;
            }
        }
        else {
            if (hasWaterBreathingFromHelmet) {
                this.removeEffect(MobEffects.WATER_BREATHING);
                hasWaterBreathingFromHelmet = false;
            }
        }

        // nether heat
        if (!this.level().isClientSide() && Objects.requireNonNull(this.gameMode()).isSurvival() && !this.isDeadOrDying()) {
            if (this.level().dimension() == Level.NETHER) {
                BlockPos blockBelow = this.blockPosition().below();
                boolean onColdBlock = false;

                if (pinferno$standingOnBlock(this)) {
                    BlockState stateAtFeet = this.level().getBlockState(this.blockPosition().below());
                    if (stateAtFeet.getBlock() instanceof IceBlock ||
                            stateAtFeet.getBlock() instanceof SnowLayerBlock ||
                            stateAtFeet.getBlock() == Blocks.SNOW_BLOCK ||
                            stateAtFeet.getBlock() == Blocks.PACKED_ICE ||
                            stateAtFeet.getBlock() == Blocks.BLUE_ICE
                    ) {
                        onColdBlock = true;
                    }
                }

                if (onColdBlock) {
                    pinferno$setCanDoHeatChange(false);
                    lastSteppedOnIcePos = blockBelow.immutable();

                    if ((Player)(Object)this instanceof ServerPlayer serverPlayer) {
                        if (this.level().getBlockState(this.blockPosition().below()).getBlock() instanceof IceBlock || this.level().getBlockState(this.blockPosition().below()).getBlock() == Blocks.PACKED_ICE || this.level().getBlockState(this.blockPosition().below()).getBlock() == Blocks.BLUE_ICE) {
                            ModTriggers.STAND_ON_ICE.get().trigger(serverPlayer);
                        }
                    }
                }
                else if (lastSteppedOnIcePos != null && !lastSteppedOnIcePos.equals(blockBelow)) {
                    pinferno$setCanDoHeatChange(true);
                    lastSteppedOnIcePos = null;
                }
                else if (lastSteppedOnIcePos != null && lastSteppedOnIcePos.equals(blockBelow)) pinferno$setCanDoHeatChange(false);
                else if (lastSteppedOnIcePos == null) pinferno$setCanDoHeatChange(true);
            }
            else pinferno$setCanDoHeatChange(true);

            if (pinferno$canDoHeatChange()) pinferno$doHeatChange();
        }

        // minecart advancements
        if (this.getVehicle() instanceof AbstractMinecart minecart) {
            Vec3 currentPos = minecart.position();

            if (rideStartPos == null) {
                rideStartPos = currentPos;
                return;
            }

            double dx = currentPos.x - rideStartPos.x;
            double dz = currentPos.z - rideStartPos.z;
            double displacement = Math.sqrt(dx * dx + dz * dz);

            if (displacement >= 500) {
                if (player instanceof ServerPlayer serverPlayer) ModTriggers.RIDE_MINECART_FAR.get().trigger(serverPlayer);
            }
            if (displacement >= 50) {
                if (player instanceof ServerPlayer serverPlayer) ModTriggers.RIDE_MINECART.get().trigger(serverPlayer);
            }
            if (minecart.getDeltaMovement().horizontalDistance() >= 1.6) {
                if (player instanceof ServerPlayer serverPlayer) ModTriggers.RIDE_MINECART_MAX_SPEED.get().trigger(serverPlayer);
            }
        }
        else rideStartPos = null;
    }

    @Unique
    private boolean pinferno$standingOnBlock(Entity entity) {
        AABB box = new AABB(entity.blockPosition());
        Vec3 pos = entity.position();
        float expand = entity.getBbWidth() / 2;

        return !box.intersect(new AABB(pos, pos).inflate(expand, 0, expand)).equals(box);
    }

    @Inject(method = "turtleHelmetTick", at = @At("HEAD"), cancellable = true)
    private void pinferno$cancelTurtleHelmetTick(CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void pinferno$registerDataEarly(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(ModEntityData.PLAYER_SHIELD_STAMINA, 0.0F);
        builder.define(ModEntityData.PLAYER_SHIELD_REGEN_DURATION, 0);
        builder.define(ModEntityData.PLAYER_HEAT, 0);
        builder.define(ModEntityData.PLAYER_WAS_BLOCKING, false);
        builder.define(ModEntityData.PLAYER_CAN_DO_HEAT_CHANGE, false);
        builder.define(ModEntityData.PLAYER_HARDCORE_REGEN_COOLDOWN, 0L);
        builder.define(ModEntityData.PLAYER_HUNGER_INITIALIZED, false);
        builder.define(ModEntityData.RESET_HARDCORE_HEALTH, true);
        builder.define(ModEntityData.PLAYER_TRADED_PROFESSIONS, 0);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void pinferno$readAdditionalSaveData(ValueInput input, CallbackInfo ci) {
        this.entityData.set(ModEntityData.PLAYER_HEAT, input.getIntOr("pinferno$heat", 0));
        this.entityData.set(ModEntityData.PLAYER_SHIELD_STAMINA, input.getFloatOr("pinferno$stamina", 0));
        this.entityData.set(ModEntityData.PLAYER_SHIELD_REGEN_DURATION, input.getIntOr("pinferno$shieldRegen", 0));
        this.entityData.set(ModEntityData.PLAYER_WAS_BLOCKING, input.getBooleanOr("pinferno$wasBlocking", false));
        this.entityData.set(ModEntityData.PLAYER_CAN_DO_HEAT_CHANGE, input.getBooleanOr("pinferno$canDoHeatChange", false));
        this.entityData.set(ModEntityData.PLAYER_HARDCORE_REGEN_COOLDOWN, input.getLongOr("pinferno$hardcoreRegenCooldown", 0L));
        this.entityData.set(ModEntityData.PLAYER_HUNGER_INITIALIZED, input.getBooleanOr("pinferno$hungerInitialized", false));
        this.entityData.set(ModEntityData.RESET_HARDCORE_HEALTH, input.getBooleanOr("pinferno$resetHardcoreHealthOnJoin", true));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void pinferno$addAdditionalSaveData(ValueOutput output, CallbackInfo ci) {
        output.putInt("pinferno$heat", this.entityData.get(ModEntityData.PLAYER_HEAT));
        output.putFloat("pinferno$stamina", this.entityData.get(ModEntityData.PLAYER_SHIELD_STAMINA));
        output.putFloat("pinferno$shieldRegen", this.entityData.get(ModEntityData.PLAYER_SHIELD_REGEN_DURATION));
        output.putBoolean("pinferno$wasBlocking", this.entityData.get(ModEntityData.PLAYER_WAS_BLOCKING));
        output.putBoolean("pinferno$canDoHeatChange", this.entityData.get(ModEntityData.PLAYER_CAN_DO_HEAT_CHANGE));
        output.putLong("pinferno$hardcoreRegenCooldown", this.entityData.get(ModEntityData.PLAYER_HARDCORE_REGEN_COOLDOWN));
        output.putBoolean("pinferno$hungerInitialized", this.entityData.get(ModEntityData.PLAYER_HUNGER_INITIALIZED));
        output.putBoolean("pinferno$resetHardcoreHealthOnJoin", this.entityData.get(ModEntityData.RESET_HARDCORE_HEALTH));
    }

    @Unique
    public int getTradedProfessions() {
        return this.entityData.get(ModEntityData.PLAYER_TRADED_PROFESSIONS);
    }

    @Unique
    public void setTradedProfessions(int mask) {
        this.entityData.set(ModEntityData.PLAYER_TRADED_PROFESSIONS, mask);
    }

    @Unique
    public void markTraded(ResourceKey<VillagerProfession> key) {
        int bit = VillagerProfessionBits.getBit(key);
        if (bit == -1) return;

        int current = getTradedProfessions();
        setTradedProfessions(current | (1 << bit));
    }

    @Unique
    public boolean hasTradedAll() {
        return VillagerProfessionBits.hasAll(getTradedProfessions());
    }

    @Override
    public int getTradedCount() {
        int mask = getTradedProfessions();
        int count = 0;
        while (mask != 0) {
            count += mask & 1;
            mask >>= 1;
        }
        return count;
    }

    @Unique public int pinferno$getHeat() {
        return this.entityData.get(ModEntityData.PLAYER_HEAT);
    }
    @Unique public void pinferno$setHeat(int heat) {
        this.entityData.set(ModEntityData.PLAYER_HEAT, heat);
    }
    @Unique
    public boolean pinferno$canDoHeatChange() {
        return this.entityData.get(ModEntityData.PLAYER_CAN_DO_HEAT_CHANGE);
    }
    @Unique
    public void pinferno$setCanDoHeatChange(boolean canDoHeatChange) {
        this.entityData.set(ModEntityData.PLAYER_CAN_DO_HEAT_CHANGE, canDoHeatChange);
    }

    @Unique
    public void pinferno$increaseHeat(int heat) {
        int i = this.pinferno$getHeat();
        this.entityData.set(ModEntityData.PLAYER_HEAT, i + heat);
        double randomNum = random.nextDouble();
        if (randomNum <= 0.4) this.level().playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.HEAT_UP, SoundSource.PLAYERS);
//        Constants.LOG.info("heat increase: {}", pinferno$getHeat());
    }

    @Unique
    public void pinferno$decreaseHeat(int heat) {
        if (this.level().dimension() == Level.NETHER) this.entityData.set(ModEntityData.PLAYER_HEAT, Math.max(1, this.pinferno$getHeat() - heat));
        else this.entityData.set(ModEntityData.PLAYER_HEAT, Math.max(0, this.pinferno$getHeat() - heat));
    }

    @Unique
    public void pinferno$doHeatChange() {
        int currentHeat = pinferno$getHeat();

        // heat increase
        int tickCap;
        if (this.level().dimension() == Level.NETHER) {
            tickCap = INCREASE_CAP;

            if (this.hasEffect(MobEffects.FIRE_RESISTANCE)) tickCap += FIRE_RES_INCREASE;

            int fireProt = pinferno$fireProtCheck();
            if (fireProt > 0) tickCap += FIRE_PROT_INCREASE * fireProt;

            if (currentHeat == 0) pinferno$increaseHeat(1);

            if (currentHeat < 200) {
                this.heatIncreaseTickCounter += 1;
                if (this.heatIncreaseTickCounter >= tickCap) {
                    pinferno$increaseHeat(1);
                    this.heatIncreaseTickCounter = 0;
                }
            }
        }
        // heat decrease
        if (this.level().dimension() != Level.NETHER) {
            tickCap = DECREASE_CAP;
            if (currentHeat > 0) {
                if (this.isInWater()) tickCap -= IN_WATER_CAP_REDUCTION;
                this.heatDecreaseTickCounter += 1;
                if (this.heatDecreaseTickCounter >= tickCap) {
                    pinferno$decreaseHeat(1);
                    this.heatDecreaseTickCounter = 0;
                }
            }
        }
        // heat damage
        currentHeat = pinferno$getHeat();

        if (currentHeat >= 100) {
            this.heatDamageTickCounter++;

            if (this.heatDamageTickCounter >= 20) {
                int step = (currentHeat - 100) / 5;
                float damage = 0.5F * (step + 1);

                if (damage > 10.5F) damage = 10.5F;

                this.hurt(this.heatDamageSource, damage);
                this.heatDamageTickCounter = 0;
            }
        }
        else this.heatDamageTickCounter = 0;
    }

    @Unique
    public int pinferno$fireProtCheck() {
        int totalLevel = 0;

        for (EquipmentSlot slot : new EquipmentSlot[]{
                EquipmentSlot.HEAD,
                EquipmentSlot.CHEST,
                EquipmentSlot.LEGS,
                EquipmentSlot.FEET
        }) {
            ItemStack itemStack = this.getItemBySlot(slot);
            totalLevel += EnchantmentHelper.getItemEnchantmentLevel(this.level().registryAccess()
                    .lookupOrThrow(Enchantments.FIRE_PROTECTION.registryKey())
                    .getOrThrow(Enchantments.FIRE_PROTECTION), itemStack);
        }

        return totalLevel;
    }

    @Inject(method = "dropEquipment", at = @At("HEAD"), cancellable = true)
    public void pinferno$preventInvDrop(ServerLevel level, CallbackInfo ci) {
        Player player = (Player)(Object)this;
        if (player.hasEffect(ModEffects.WELL_RESTED.holder)) {
            ci.cancel();
        }
    }

    @Inject(method = "getBaseExperienceReward", at = @At("HEAD"), cancellable = true)
    public void pinferno$preventExpDrop(ServerLevel level, CallbackInfoReturnable<Integer> cir) {
        Player player = (Player)(Object)this;
        if (player.hasEffect(ModEffects.WELL_RESTED.holder) || player.isSpectator()) {
            cir.setReturnValue(0);
            cir.cancel();
        }
    }

    // hardcore mode
    @Inject(method = "updatePlayerPose", at = @At("HEAD"))
    private void pinferno$blockSprinting(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        if ((player.level().getLevelData().isHardcore() || this.hasEffect(ModEffects.HINDERED.holder)) && !player.isSpectator() && !player.isCreative()) {
            player.setSprinting(false);
            player.getAbilities().mayfly = false;
            player.getAbilities().flying = false;
        }
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void pinferno$blockSprinting2(CallbackInfo ci) {
        if (this.level().getLevelData().isHardcore() && !this.isSpectator() && !this.isCreative()) this.setSprinting(false);
    }

    @Inject(method = "getDestroySpeed", at = @At("HEAD"), cancellable = true)
    private void pinferno$hinderedMining(BlockState state, CallbackInfoReturnable<Float> cir) {
        Player self = (Player)(Object)this;
        if (self.hasEffect(ModEffects.HINDERED.holder)) {
            int level = Objects.requireNonNull(this.getEffect(ModEffects.HINDERED.holder)).getAmplifier() + 1;
            cir.setReturnValue((float)Math.pow(0.5, level));
        }
    }

    @ModifyVariable(method = "getDestroySpeed", at = @At(value = "STORE", ordinal = 0), name = "speed")
    private float pinferno$applyConduitModifier(float speed, BlockState state) {
        Player self = (Player)(Object)this;
        MobEffectInstance conduit = self.getEffect(MobEffects.CONDUIT_POWER);
        if (conduit == null) return speed;

        float conduitModifier = (conduit.getAmplifier() + 1) * 0.05F;

        if (MobEffectUtil.hasDigSpeed(self)) {
            float hasteBonus = (MobEffectUtil.getDigSpeedAmplification(self) + 1) * 0.2F;
            speed /= 1.0F + hasteBonus;
            speed *= 1.0F + hasteBonus + conduitModifier;
        }
        else speed *= 1.0F + conduitModifier;
        return speed;
    }

    // cancel the multiplier
    @ModifyConstant(method = "attack", constant = @Constant(floatValue = 1.5F))
    private float pinferno$removeCritMulti(float original) {
        return 1.0F;
    }

    // change crit multiplier to additive
    @ModifyVariable(method = "attack", at = @At(value = "STORE", ordinal = 0), name = "baseDamage")
    private float pinferno$applyFlatCritDamage(float baseDamage, Entity entity) {
        Player self = (Player) (Object) this;
        boolean isFullStrength = self.getAttackStrengthScale(0.5F) > 0.9F;
        float finalDamage = baseDamage;

        if (isFullStrength && canCriticalAttack(entity)) {
            int level = EnchantmentHelper.getItemEnchantmentLevel(self.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ModEnchantments.SPLATTERING), self.getMainHandItem());

            if (self instanceof ServerPlayer serverPlayer) ModTriggers.CRIT_DAMAGE.get().trigger(serverPlayer);
            if (level > 0) finalDamage += (3.0F * level);
            finalDamage += 3.0F;
        }
        return finalDamage;
    }

    @Inject(method = "canCriticalAttack", at = @At("HEAD"), cancellable = true)
    private void pinferno$disableCritForNonWeapons(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        Player self = (Player)(Object)this;
        ItemStack stack = self.getMainHandItem();

        if (!stack.is(ItemTags.SHARP_WEAPON_ENCHANTABLE) && !stack.is(ItemTags.MACE_ENCHANTABLE) && !stack.is(ItemTags.TRIDENT_ENCHANTABLE)) {
            cir.setReturnValue(false);
        }
    }

    @Redirect(method = "getDestroySpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;onGround()Z"))
    private boolean pinferno$removeMountMiningSpeedPenalty(Player player) {
        return player.onGround() || player.isPassenger();
    }
}
