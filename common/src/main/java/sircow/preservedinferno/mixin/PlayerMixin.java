package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.other.HeatAccessor;
import sircow.preservedinferno.other.ModDamageTypes;
import sircow.preservedinferno.other.ModEntityData;
import sircow.preservedinferno.other.ShieldStaminaHandler;

import java.util.Objects;
import java.util.Optional;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements HeatAccessor {
    @Unique private boolean hasWaterBreathingFromHelmet = false;
    @Unique private int heatIncreaseTickCounter = 0;
    @Unique private int heatDecreaseTickCounter = 0;
    @Unique private int heatDamageTickCounter = 0;
    @Unique private static final int INCREASE_CAP = 60;
    @Unique private static final int DECREASE_CAP = 80;
    @Unique private static final int IN_WATER_CAP_REDUCTION = 10;
    @Unique private static final int IN_POWDER_SNOW_CAP_REDUCTION = 30;
    @Unique private static final int FIRE_RES_INCREASE = 80;
    @Unique private static final int FIRE_PROT_INCREASE = 10;
    @Unique private @Nullable BlockPos lastSteppedOnIcePos = null;
    @Unique DamageSource damageSource = ModDamageTypes.of(this.level(), ModDamageTypes.HEAT, this);

    @Shadow @Nullable public abstract GameType gameMode();

    @Shadow public abstract void awardStat(Stat<?> stat);

    @Shadow public abstract void resetStat(Stat<?> stat);

    @Shadow public abstract void setLastDeathLocation(Optional<GlobalPos> lastDeathLocation);

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
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
        if (!this.level().isClientSide() && Objects.requireNonNull(this.gameMode()).isSurvival()) {
            if (this.level().dimension() == Level.NETHER) {
                BlockPos blockBelow = this.blockPosition().below();

                boolean onColdBlock = false;
                if (standingOnBlock(this) && !this.isInPowderSnow) {
                    BlockState stateAtFeet = this.level().getBlockState(this.blockPosition().below());
                    if (stateAtFeet.getBlock() instanceof IceBlock ||
                            stateAtFeet.getBlock() instanceof SnowLayerBlock ||
                            stateAtFeet.getBlock() == Blocks.SNOW_BLOCK ||
                            stateAtFeet.getBlock() == Blocks.PACKED_ICE ||
                            stateAtFeet.getBlock() == Blocks.BLUE_ICE
                    ){
                        onColdBlock = true;
                    }
                }

                if (onColdBlock) {
                    preserved_inferno$setCanDoHeatChange(false);
                    lastSteppedOnIcePos = blockBelow.immutable();
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

            if (preserved_inferno$canDoHeatChange()) {
                preserved_inferno$doHeatChange();
            }
        }
    }

    @Unique
    private boolean standingOnBlock(Entity entity) {
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
        if (!this.isSpectator() && this.level() instanceof ServerLevel serverLevel) {
            this.dropAllDeathLoot(serverLevel, damageSource);
        }

        if (damageSource != null) {
            this.setDeltaMovement(
                    -Mth.cos((this.getHurtDir() + this.getYRot()) * (float) (Math.PI / 180.0)) * 0.1F,
                    0.1F,
                    -Mth.sin((this.getHurtDir() + this.getYRot()) * (float) (Math.PI / 180.0)) * 0.1F
            );
        } else {
            this.setDeltaMovement(0.0, 0.1, 0.0);
        }

        preserved_inferno$setHeat(0);
        ShieldStaminaHandler.onPlayerDeath(player);

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
        builder.define(ModEntityData.PLAYER_HEAT, 0);
        builder.define(ModEntityData.PLAYER_CAN_DO_HEAT_CHANGE, false);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void preserved_inferno$readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        this.entityData.set(ModEntityData.PLAYER_HEAT, tag.getIntOr("preserved_inferno$heat", 0));
        this.entityData.set(ModEntityData.PLAYER_SHIELD_STAMINA, tag.getFloatOr("preserved_inferno$stamina", 0));
        this.entityData.set(ModEntityData.PLAYER_CAN_DO_HEAT_CHANGE, tag.getBooleanOr("preserved_inferno$canDoHeatChange", false));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        tag.putInt("preserved_inferno$heat", this.entityData.get(ModEntityData.PLAYER_HEAT));
        tag.putFloat("preserved_inferno$stamina", this.entityData.get(ModEntityData.PLAYER_SHIELD_STAMINA));
        tag.putBoolean("preserved_inferno$canDoHeatChange", this.entityData.get(ModEntityData.PLAYER_CAN_DO_HEAT_CHANGE));
    }

    @Unique public int preserved_inferno$getHeat() {
        return this.entityData.get(ModEntityData.PLAYER_HEAT);
    }
    @Unique public float preserved_inferno$getStamina() {
        return this.entityData.get(ModEntityData.PLAYER_SHIELD_STAMINA);
    }
    @Unique public void preserved_inferno$setHeat(int heat) {
        this.entityData.set(ModEntityData.PLAYER_HEAT, heat);
    }
    @Unique public void preserved_inferno$setStamina(float stamina) {
        this.entityData.set(ModEntityData.PLAYER_SHIELD_STAMINA, stamina);
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
        //Constants.LOG.info("heat increase: {}", preserved_inferno$getHeat());
    }

    @Unique
    public void preserved_inferno$decreaseHeat(int heat) {
        int i = this.preserved_inferno$getHeat();
        this.entityData.set(ModEntityData.PLAYER_HEAT, i - heat);
        //Constants.LOG.info("heat decrease: {}", preserved_inferno$getHeat());
    }

    @Unique
    public void preserved_inferno$doHeatChange() {
        int currentHeat = preserved_inferno$getHeat();

        // heat increase
        int tickCap;
        if (this.level().dimension() == Level.NETHER && !this.isInPowderSnow) {
            tickCap = INCREASE_CAP;

            if (this.hasEffect(MobEffects.FIRE_RESISTANCE)) {
                tickCap += FIRE_RES_INCREASE;
            }

            if (fireProtCheck() == 1) {
                tickCap += FIRE_PROT_INCREASE;
            }
            if (fireProtCheck() == 2) {
                tickCap += (FIRE_PROT_INCREASE * 2);
            }
            if (fireProtCheck() == 3) {
                tickCap += (FIRE_PROT_INCREASE * 3);
            }
            if (fireProtCheck() == 4) {
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
        else if (this.level().dimension() == Level.NETHER && this.isInPowderSnow) {
            tickCap = 20;
            if (currentHeat > 4) {
                this.heatDecreaseTickCounter += 1;
                if (this.heatDecreaseTickCounter >= tickCap) {
                    preserved_inferno$decreaseHeat(5);
                    this.heatDecreaseTickCounter = 0;
                }
            }
            if (currentHeat <= 4 && currentHeat > 0) {
                this.heatDecreaseTickCounter += 1;
                if (this.heatDecreaseTickCounter >= tickCap) {
                    preserved_inferno$decreaseHeat(0);
                    this.heatDecreaseTickCounter = 0;
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
                if (this.isInPowderSnow) {
                    tickCap -= IN_POWDER_SNOW_CAP_REDUCTION;
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
    public int fireProtCheck() {
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
}
