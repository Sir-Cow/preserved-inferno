package sircow.preservedinferno.mixin;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
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
import sircow.preservedinferno.item.custom.PreservedShieldItem;
import sircow.preservedinferno.other.*;
import sircow.preservedinferno.sound.ModSounds;
import sircow.preservedinferno.trade.PlayerMixinAccess;
import sircow.preservedinferno.trade.VillagerProfessionBits;
import sircow.preservedinferno.trigger.ModTriggers;

import java.util.Objects;
import java.util.Optional;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements HeatAccessor, PlayerMixinAccess {
    @Unique private boolean hasWaterBreathingFromHelmet = false;
    @Unique private int heatIncreaseTickCounter, heatDecreaseTickCounter, heatDamageTickCounter = 0;
    @Unique private Vec3 rideStartPos = null;
    @Unique private static final int INCREASE_CAP = 120;
    @Unique private static final int DECREASE_CAP = 80;
    @Unique private static final int IN_WATER_CAP_REDUCTION = 10;
    @Unique private static final int FIRE_RES_INCREASE = 48;
    @Unique private static final int FIRE_PROT_INCREASE = 8;
    @Unique private @Nullable BlockPos lastSteppedOnIcePos = null;
    @Unique DamageSource damageSource = ModDamageTypes.of(this.level(), ModDamageTypes.HEAT, this);

    @Shadow @Nullable public abstract GameType gameMode();
    @Shadow public abstract void awardStat(Stat<?> stat);
    @Shadow public abstract void resetStat(Stat<?> stat);
    @Shadow public abstract void setLastDeathLocation(Optional<GlobalPos> lastDeathLocation);
    @Shadow protected abstract boolean canCriticalAttack(Entity target);

    @Shadow public abstract boolean isCreative();

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @ModifyVariable(method = "hurtServer", at = @At("HEAD"), argsOnly = true)
    private float preserved_inferno$shieldDamageIntercept(float originalAmount, ServerLevel level, DamageSource damageSource) {
        Player player = (Player)(Object)this;

        if (damageSource.is(DamageTypes.FREEZE)) {
            if (player instanceof ServerPlayer serverPlayer && preserved_inferno$getHeat() >= 1) {
                ModTriggers.FREEZE_COOL.get().trigger(serverPlayer);
            }
            preserved_inferno$decreaseHeat(10);
        }

        if (damageSource.is(DamageTypes.ON_FIRE) && !player.hasEffect(MobEffects.FIRE_RESISTANCE) && player.level().dimension() == Level.NETHER) {
            preserved_inferno$increaseHeat(1);
        }

        ItemStack blockingStack = player.getUseItem();

        if (player.isBlocking() && blockingStack.getItem() instanceof PreservedShieldItem) {
            Vec3 sourcePos;
            if (damageSource.getEntity() != null) {
                sourcePos = damageSource.getEntity().position();
            }
            else {
                sourcePos = damageSource.getSourcePosition();
            }

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

                    if (lookLen > 1e-6) {
                        look = new Vec3(lookX / lookLen, 0.0D, lookZ / lookLen);
                    }
                    else {
                        look = new Vec3(0.0D, 0.0D, 0.0D);
                    }

                    double dot = toSource.x * look.x + toSource.z * look.z;

                    if (dot < 0.5D) {
                        return originalAmount;
                    }
                }
            }

            float currentStamina = player.getEntityData().get(ModEntityData.PLAYER_SHIELD_STAMINA);

            if (damageSource.is(DamageTypeTags.IS_FIRE) || damageSource.is(DamageTypes.ON_FIRE)) {
                player.clearFire();
                player.setSharedFlagOnFire(false);
            }

            if (damageSource.is(DamageTypeTags.BYPASSES_SHIELD)) {
                ShieldStaminaHandler.lastBypassingSource = damageSource;
                Optional<BlocksAttacks> blocksAttacks = Optional.ofNullable(blockingStack.get(DataComponents.BLOCKS_ATTACKS));
                if (blocksAttacks.isPresent()) {
                    blocksAttacks.get().onBlocked(level, player);
                    blocksAttacks.get().hurtBlockingItem(level, blockingStack, player, player.getUsedItemHand(), originalAmount);
                }
                return originalAmount;
            }
            else {
                ShieldStaminaHandler.lastBypassingSource = null;
                float finalDamageToApply = Math.max(0, originalAmount - currentStamina);
                float newStamina = Math.max(0, currentStamina - originalAmount);

                if (newStamina != currentStamina) {
                    player.getEntityData().set(ModEntityData.PLAYER_SHIELD_STAMINA, newStamina);
                }

                if (newStamina <= 0) {
                    ShieldStaminaHandler.triggerCooldown(player, blockingStack);
                }

                Optional<BlocksAttacks> blocksAttacks = Optional.ofNullable(blockingStack.get(DataComponents.BLOCKS_ATTACKS));
                if (blocksAttacks.isPresent()) {
                    blocksAttacks.get().onBlocked(level, player);
                    blocksAttacks.get().hurtBlockingItem(level, blockingStack, player, player.getUsedItemHand(), originalAmount);
                }

                if (player instanceof ServerPlayer serverPlayer) {
                    CriteriaTriggers.ENTITY_HURT_PLAYER.trigger(serverPlayer, damageSource, originalAmount, originalAmount, true);
                    serverPlayer.awardStat(Stats.DAMAGE_BLOCKED_BY_SHIELD, Math.round(originalAmount * 10.0F));

                    if (finalDamageToApply == 0.0F) {
                        serverPlayer.hurtTime = 0;
                        serverPlayer.hurtDuration = 0;
                        serverPlayer.hurtMarked = false;
                        serverPlayer.invulnerableTime = 0;
                    }
                }
                return finalDamageToApply;
            }
        }
        return originalAmount;
    }

    // modify bed sleeping
    @ModifyConstant(method = "tick", constant = @Constant(intValue = 100))
    private int preserved_inferno$modifyIntValue(int original) {
        return 200;
    }
    @ModifyConstant(method = "tick", constant = @Constant(intValue = 110))
    private int preserved_inferno$modifyIntValue2(int original) {
        return 210;
    }
    @ModifyConstant(method = "stopSleepInBed", constant = @Constant(intValue = 100))
    private int preserved_inferno$modifyIntValue3(int original) {
        return 200;
    }
    @ModifyConstant(method = "isSleepingLongEnough", constant = @Constant(intValue = 100))
    private int preserved_inferno$modifyIntValue4(int original) {
        return 200;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void preserved_inferno$tick(CallbackInfo ci) {
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

                if (preserved_inferno$standingOnBlock(this)) {
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
                    preserved_inferno$setCanDoHeatChange(false);
                    lastSteppedOnIcePos = blockBelow.immutable();

                    if ((Player)(Object)this instanceof ServerPlayer serverPlayer) {
                        if (this.level().getBlockState(this.blockPosition().below()).getBlock() instanceof IceBlock || this.level().getBlockState(this.blockPosition().below()).getBlock() == Blocks.PACKED_ICE || this.level().getBlockState(this.blockPosition().below()).getBlock() == Blocks.BLUE_ICE) {
                            ModTriggers.STAND_ON_ICE.get().trigger(serverPlayer);
                        }
                    }
                }
                else if (lastSteppedOnIcePos != null && !lastSteppedOnIcePos.equals(blockBelow)) {
                    preserved_inferno$setCanDoHeatChange(true);
                    lastSteppedOnIcePos = null;
                }
                else if (lastSteppedOnIcePos != null && lastSteppedOnIcePos.equals(blockBelow)) {
                    preserved_inferno$setCanDoHeatChange(false);
                }
                else if (lastSteppedOnIcePos == null) {
                    preserved_inferno$setCanDoHeatChange(true);
                }
            }
            else {
                preserved_inferno$setCanDoHeatChange(true);
            }

            if (preserved_inferno$canDoHeatChange()) {
                preserved_inferno$doHeatChange();
            }
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
                if ((Player)(Object)this instanceof ServerPlayer serverPlayer) {
                    ModTriggers.RIDE_MINECART_FAR.get().trigger(serverPlayer);
                }
            }
            if (displacement >= 50) {
                if ((Player)(Object)this instanceof ServerPlayer serverPlayer) {
                    ModTriggers.RIDE_MINECART.get().trigger(serverPlayer);
                }
            }
            if (minecart.getDeltaMovement().horizontalDistance() >= 3.2) {
                if ((Player)(Object)this instanceof ServerPlayer serverPlayer) {
                    ModTriggers.RIDE_MINECART_MAX_SPEED.get().trigger(serverPlayer);
                }
            }
        }
        else {
            rideStartPos = null;
        }
    }

    @Unique
    private boolean preserved_inferno$standingOnBlock(Entity entity) {
        AABB box = new AABB(entity.blockPosition());
        Vec3 pos = entity.position();
        float expand = entity.getBbWidth() / 2;

        return !box.intersect(new AABB(pos, pos).inflate(expand, 0, expand)).equals(box);
    }

    @Inject(method = "turtleHelmetTick", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$cancelTurtleHelmetTick(CallbackInfo ci) {
        ci.cancel();
    }
    
    @Inject(method = "die", at = @At(value = "HEAD"), cancellable = true)
    private void preserved_inferno$handleDie(DamageSource damageSource, CallbackInfo ci) {
        Player player = (Player)(Object)this;
        super.die(damageSource);
        this.reapplyPosition();
        if (!this.isSpectator() && this.level() instanceof ServerLevel serverLevel && !this.hasEffect(ModEffects.WELL_RESTED.holder)) {
            this.dropAllDeathLoot(serverLevel, damageSource);
        }

        if (damageSource != null) {
            this.setDeltaMovement(
                    -Mth.cos((this.getHurtDir() + this.getYRot()) * (float) (Math.PI / 180.0)) * 0.1F,
                    0.1F,
                    -Mth.sin((this.getHurtDir() + this.getYRot()) * (float) (Math.PI / 180.0)) * 0.1F
            );
        }
        else {
            this.setDeltaMovement(0.0, 0.1, 0.0);
        }

        this.awardStat(Stats.CUSTOM.get(Stats.DEATHS));
        this.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_DEATH));
        this.clearFire();
        this.setSharedFlagOnFire(false);
        this.setLastDeathLocation(Optional.of(GlobalPos.of(this.level().dimension(), this.blockPosition())));
        ci.cancel();
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void preserved_inferno$registerDataEarly(SynchedEntityData.Builder builder, CallbackInfo ci) {
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
    public void preserved_inferno$readAdditionalSaveData(ValueInput input, CallbackInfo ci) {
        this.entityData.set(ModEntityData.PLAYER_HEAT, input.getIntOr("preserved_inferno$heat", 0));
        this.entityData.set(ModEntityData.PLAYER_SHIELD_STAMINA, input.getFloatOr("preserved_inferno$stamina", 0));
        this.entityData.set(ModEntityData.PLAYER_SHIELD_REGEN_DURATION, input.getIntOr("preserved_inferno$shieldRegen", 0));
        this.entityData.set(ModEntityData.PLAYER_WAS_BLOCKING, input.getBooleanOr("preserved_inferno$wasBlocking", false));
        this.entityData.set(ModEntityData.PLAYER_CAN_DO_HEAT_CHANGE, input.getBooleanOr("preserved_inferno$canDoHeatChange", false));
        this.entityData.set(ModEntityData.PLAYER_HARDCORE_REGEN_COOLDOWN, input.getLongOr("preserved_inferno$hardcoreRegenCooldown", 0L));
        this.entityData.set(ModEntityData.PLAYER_HUNGER_INITIALIZED, input.getBooleanOr("preserved_inferno$hungerInitialized", false));
        this.entityData.set(ModEntityData.RESET_HARDCORE_HEALTH, input.getBooleanOr("preserved_inferno$resetHardcoreHealthOnJoin", true));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void preserved_inferno$addAdditionalSaveData(ValueOutput output, CallbackInfo ci) {
        output.putInt("preserved_inferno$heat", this.entityData.get(ModEntityData.PLAYER_HEAT));
        output.putFloat("preserved_inferno$stamina", this.entityData.get(ModEntityData.PLAYER_SHIELD_STAMINA));
        output.putFloat("preserved_inferno$shieldRegen", this.entityData.get(ModEntityData.PLAYER_SHIELD_REGEN_DURATION));
        output.putBoolean("preserved_inferno$wasBlocking", this.entityData.get(ModEntityData.PLAYER_WAS_BLOCKING));
        output.putBoolean("preserved_inferno$canDoHeatChange", this.entityData.get(ModEntityData.PLAYER_CAN_DO_HEAT_CHANGE));
        output.putLong("preserved_inferno$hardcoreRegenCooldown", this.entityData.get(ModEntityData.PLAYER_HARDCORE_REGEN_COOLDOWN));
        output.putBoolean("preserved_inferno$hungerInitialized", this.entityData.get(ModEntityData.PLAYER_HUNGER_INITIALIZED));
        output.putBoolean("preserved_inferno$resetHardcoreHealthOnJoin", this.entityData.get(ModEntityData.RESET_HARDCORE_HEALTH));
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

    @Unique public int preserved_inferno$getHeat() {
        return this.entityData.get(ModEntityData.PLAYER_HEAT);
    }
    @Unique public void preserved_inferno$setHeat(int heat) {
        this.entityData.set(ModEntityData.PLAYER_HEAT, heat);
    }
    @Unique
    public boolean preserved_inferno$canDoHeatChange() {
        return this.entityData.get(ModEntityData.PLAYER_CAN_DO_HEAT_CHANGE);
    }
    @Unique
    public void preserved_inferno$setCanDoHeatChange(boolean canDoHeatChange) {
        this.entityData.set(ModEntityData.PLAYER_CAN_DO_HEAT_CHANGE, canDoHeatChange);
    }

    @Unique
    public void preserved_inferno$increaseHeat(int heat) {
        int i = this.preserved_inferno$getHeat();
        this.entityData.set(ModEntityData.PLAYER_HEAT, i + heat);
        double randomNum = random.nextDouble();
        if (randomNum <= 0.4) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.HEAT_UP, SoundSource.PLAYERS);
        }
//        Constants.LOG.info("heat increase: {}", preserved_inferno$getHeat());
    }

    @Unique
    public void preserved_inferno$decreaseHeat(int heat) {
        this.entityData.set(ModEntityData.PLAYER_HEAT, Math.max(0, this.preserved_inferno$getHeat() - heat));
    }

    @Unique
    public void preserved_inferno$doHeatChange() {
        int currentHeat = preserved_inferno$getHeat();

        // heat increase
        int tickCap;
        if (this.level().dimension() == Level.NETHER) {
            tickCap = INCREASE_CAP;

            if (this.hasEffect(MobEffects.FIRE_RESISTANCE)) {
                tickCap += FIRE_RES_INCREASE;
            }

            if (preserved_inferno$fireProtCheck() == 1) {
                tickCap += FIRE_PROT_INCREASE;
            }
            if (preserved_inferno$fireProtCheck() == 2) {
                tickCap += (FIRE_PROT_INCREASE * 2);
            }
            if (preserved_inferno$fireProtCheck() == 3) {
                tickCap += (FIRE_PROT_INCREASE * 3);
            }
            if (preserved_inferno$fireProtCheck() == 4) {
                tickCap += (FIRE_PROT_INCREASE * 4);
            }

            if (currentHeat == 0) {
                preserved_inferno$increaseHeat(1);
            }

            if (currentHeat < 200) {
                this.heatIncreaseTickCounter += 1;
                if (this.heatIncreaseTickCounter >= tickCap) {
                    preserved_inferno$increaseHeat(1);
                    this.heatIncreaseTickCounter = 0;
                }
            }
        }
        // heat decrease
        if (this.level().dimension() != Level.NETHER) {
            tickCap = DECREASE_CAP;
            if (currentHeat > 0) {
                if (this.isInWater()) {
                    tickCap -= IN_WATER_CAP_REDUCTION;
                }
                this.heatDecreaseTickCounter += 1;
                if (this.heatDecreaseTickCounter >= tickCap) {
                    preserved_inferno$decreaseHeat(1);
                    this.heatDecreaseTickCounter = 0;
                }
            }
        }
        // heat damage
        currentHeat = preserved_inferno$getHeat();
        if (currentHeat >= 100) {
            this.heatDamageTickCounter++;
            if (this.heatDamageTickCounter >= 20) {
                if (currentHeat < 105) {
                    this.hurt((this.damageSource), 0.5F);
                }
                if (currentHeat >= 105 && currentHeat < 110) {
                    this.hurt((this.damageSource), 1.0F);
                }
                if (currentHeat >= 110 && currentHeat < 115) {
                    this.hurt((this.damageSource), 1.5F);
                }
                if (currentHeat >= 115 && currentHeat < 120) {
                    this.hurt((this.damageSource), 2.0F);
                }
                if (currentHeat >= 120 && currentHeat < 125) {
                    this.hurt((this.damageSource), 2.5F);
                }
                if (currentHeat >= 125 && currentHeat < 130) {
                    this.hurt((this.damageSource), 3.0F);
                }
                if (currentHeat >= 130 && currentHeat < 135) {
                    this.hurt((this.damageSource), 3.5F);
                }
                if (currentHeat >= 135 && currentHeat < 140) {
                    this.hurt((this.damageSource), 4.0F);
                }
                if (currentHeat >= 140 && currentHeat < 145) {
                    this.hurt((this.damageSource), 4.5F);
                }
                if (currentHeat >= 145 && currentHeat < 150) {
                    this.hurt((this.damageSource), 5.0F);
                }
                if (currentHeat >= 150 && currentHeat < 155) {
                    this.hurt((this.damageSource), 5.5F);
                }
                if (currentHeat >= 155 && currentHeat < 160) {
                    this.hurt((this.damageSource), 6.0F);
                }
                if (currentHeat >= 160 && currentHeat < 165) {
                    this.hurt((this.damageSource), 6.5F);
                }
                if (currentHeat >= 165 && currentHeat < 170) {
                    this.hurt((this.damageSource), 7.0F);
                }
                if (currentHeat >= 170 && currentHeat < 175) {
                    this.hurt((this.damageSource), 7.5F);
                }
                if (currentHeat >= 175 && currentHeat < 180) {
                    this.hurt((this.damageSource), 8.0F);
                }
                if (currentHeat >= 180 && currentHeat < 185) {
                    this.hurt((this.damageSource), 8.5F);
                }
                if (currentHeat >= 185 && currentHeat < 190) {
                    this.hurt((this.damageSource), 9.0F);
                }
                if (currentHeat >= 190 && currentHeat < 195) {
                    this.hurt((this.damageSource), 9.5F);
                }
                if (currentHeat >= 195 && currentHeat < 200) {
                    this.hurt((this.damageSource), 10.0F);
                }
                if (currentHeat >= 200) {
                    this.hurt((this.damageSource), 10.5F);
                }

                this.heatDamageTickCounter = 0;
            }
        }
        else {
            this.heatDamageTickCounter = 0;
        }
    }

    @Unique
    public int preserved_inferno$fireProtCheck() {
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
    public void preserved_inferno$preventInvDrop(ServerLevel level, CallbackInfo ci) {
        Player player = (Player)(Object)this;
        if (player.hasEffect(ModEffects.WELL_RESTED.holder)) {
            ci.cancel();
        }
    }

    @Inject(method = "getBaseExperienceReward", at = @At("HEAD"), cancellable = true)
    public void preserved_inferno$preventExpDrop(ServerLevel level, CallbackInfoReturnable<Integer> cir) {
        Player player = (Player)(Object)this;
        if (player.hasEffect(ModEffects.WELL_RESTED.holder) || player.isSpectator()) {
            cir.setReturnValue(0);
            cir.cancel();
        }
    }

    // hardcore mode
    @Inject(method = "updatePlayerPose", at = @At("HEAD"))
    private void preserved_inferno$blockSprinting(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        if ((player.level().getLevelData().isHardcore() || this.hasEffect(ModEffects.HINDERED.holder)) && !player.isSpectator() && !player.isCreative()) {
            player.setSprinting(false);
            player.getAbilities().mayfly = false;
            player.getAbilities().flying = false;
        }
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void preserved_inferno$blockSprinting2(CallbackInfo ci) {
        if (this.level().getLevelData().isHardcore() && !this.isSpectator() && !this.isCreative()) {
            this.setSprinting(false);
        }
    }

    @Inject(method = "getDestroySpeed", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$hinderedMining(BlockState state, CallbackInfoReturnable<Float> cir) {
        Player self = (Player)(Object)this;
        if (self.hasEffect(ModEffects.HINDERED.holder)) {
            int level = Objects.requireNonNull(this.getEffect(ModEffects.HINDERED.holder)).getAmplifier() + 1;
            cir.setReturnValue((float)Math.pow(0.5, level));
        }
    }

    // cancel the multiplier
    @ModifyConstant(method = "attack", constant = @Constant(floatValue = 1.5F))
    private float preserved_inferno$removeCritMulti(float original) {
        return 1.0F;
    }

    // change crit multiplier to additive
    @ModifyVariable(method = "attack", at = @At(value = "STORE", ordinal = 0), index = 2)
    private float preserved_inferno$applyFlatCritDamage(float f, Entity target) {
        Player self = (Player) (Object) this;
        boolean isFullStrength = self.getAttackStrengthScale(0.5F) > 0.9F;

        if (isFullStrength && canCriticalAttack(target)) {
            if (self instanceof ServerPlayer serverPlayer) {
                ModTriggers.CRIT_DAMAGE.get().trigger(serverPlayer);
            }
            return f + 3.0F;
        }
        return f;
    }
}
