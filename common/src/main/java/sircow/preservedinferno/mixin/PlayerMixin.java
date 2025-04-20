package sircow.preservedinferno.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.other.HeatAccessor;
import sircow.preservedinferno.other.ModDamageTypes;

import java.util.Objects;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements HeatAccessor {
    @Unique private boolean hasWaterBreathingFromHelmet = false;
    @Unique private int heatIncreaseTickCounter = 0;
    @Unique private int heatDecreaseTickCounter = 0;
    @Unique private int heatDamageTickCounter = 0;
    @Unique private int tickCap = 800;
    @Unique private static final int IN_WATER_CAP_REDUCTION = 200;
    @Unique private static final int IN_POWDER_SNOW_CAP_REDUCTION = 400;
    @Unique private static final EntityDataAccessor<Integer> DATA_HEAT = SynchedEntityData.defineId(PlayerMixin.class, EntityDataSerializers.INT);
    @Unique DamageSource damageSource = ModDamageTypes.of(this.level(), ModDamageTypes.HEAT, this);

    @Shadow @Nullable public abstract GameType gameMode();

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
        if (!this.level().isClientSide() && !Objects.requireNonNull(this.gameMode()).isCreative()) {
            preserved_inferno$doHeatChange();
        }
    }

    @Inject(method = "turtleHelmetTick", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$cancelTurtleHelmetTick(CallbackInfo ci) {
        ci.cancel();
    }

    // nether heat
    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void preserved_inferno$defineHeat(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(DATA_HEAT, 0);
    }

    @Inject(method = "die", at = @At("TAIL"))
    private void preserved_inferno$resetHeatOnDeath(DamageSource cause, CallbackInfo ci) {
        preserved_inferno$setHeat(0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("preserved_inferno$heat")) {
            this.preserved_inferno$setHeat(tag.getIntOr("preserved_inferno$heat", 0));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("preserved_inferno$heat", this.preserved_inferno$getHeat());
    }

    @Unique
    public int preserved_inferno$getHeat() {
        return this.entityData.get(DATA_HEAT);
    }

    @Unique
    public void preserved_inferno$setHeat(int heat) {
        this.entityData.set(DATA_HEAT, heat);
    }

    @Unique
    public void preserved_inferno$increaseHeat(int heat) {
        int i = this.preserved_inferno$getHeat();
        this.entityData.set(DATA_HEAT, i + heat);
    }

    @Unique
    public void preserved_inferno$decreaseHeat(int heat) {
        int i = this.preserved_inferno$getHeat();
        this.entityData.set(DATA_HEAT, i - heat);
    }

    @Unique
    public void preserved_inferno$doHeatChange() {
        int currentHeat = preserved_inferno$getHeat();
        this.tickCap = 800;

        // heat increase
        if (this.level().dimension() == Level.NETHER) {
            if (this.hasEffect(MobEffects.FIRE_RESISTANCE)) {
                this.tickCap += 200;
            }

            if (fireProtCheck() == 1) {
                this.tickCap += 50;
            }
            if (fireProtCheck() == 2) {
                this.tickCap += 100;
            }
            if (fireProtCheck() == 3) {
                this.tickCap += 150;
            }
            if (fireProtCheck() == 4) {
                this.tickCap += 200;
            }

            if (currentHeat < 200) {
                this.heatIncreaseTickCounter += 2;
                if (this.heatIncreaseTickCounter >= this.tickCap) {
                    preserved_inferno$increaseHeat(1);
                    this.heatIncreaseTickCounter = 0;
                }
            }
        }
        // heat decrease
        if (this.level().dimension() != Level.NETHER) {
            if (currentHeat > 0) {
                if (this.isInWater()) {
                    this.tickCap -= IN_WATER_CAP_REDUCTION;
                }
                if (this.isInPowderSnow) {
                    this.tickCap -= IN_POWDER_SNOW_CAP_REDUCTION;
                }
                this.heatDecreaseTickCounter += 1;
                if (this.heatDecreaseTickCounter >= this.tickCap) {
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
                if (currentHeat < 110) {
                    this.hurt((this.damageSource), 1.0F);
                }
                if (currentHeat >= 110 && currentHeat < 120) {
                    this.hurt((this.damageSource), 2.0F);
                }
                if (currentHeat >= 120 && currentHeat < 130) {
                    this.hurt((this.damageSource), 3.0F);
                }
                if (currentHeat >= 130 && currentHeat < 140) {
                    this.hurt((this.damageSource), 4.0F);
                }
                if (currentHeat >= 140 && currentHeat < 150) {
                    this.hurt((this.damageSource), 5.0F);
                }
                if (currentHeat >= 150 && currentHeat < 160) {
                    this.hurt((this.damageSource), 6.0F);
                }
                if (currentHeat >= 160 && currentHeat < 170) {
                    this.hurt((this.damageSource), 7.0F);
                }
                if (currentHeat >= 170 && currentHeat < 180) {
                    this.hurt((this.damageSource), 8.0F);
                }
                if (currentHeat >= 180 && currentHeat < 190) {
                    this.hurt((this.damageSource), 9.0F);
                }
                if (currentHeat >= 190 && currentHeat < 200) {
                    this.hurt((this.damageSource), 10.0F);
                }
                if (currentHeat >= 200) {
                    this.hurt((this.damageSource), 11.0F);
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
