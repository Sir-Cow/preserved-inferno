package sircow.preservedinferno.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.Unit;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRules;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.effect.ModEffects;
import sircow.preservedinferno.other.ModDamageTypes;
import sircow.preservedinferno.other.TempInventoryStorage;
import sircow.preservedinferno.trigger.ModTriggers;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    public ServerPlayerMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Shadow public abstract @NotNull Level level();
    @Shadow private ServerPlayer.RespawnConfig respawnConfig;

    // prevent advancements where player needs to kill a mob from granting when killed by conduit
    @Inject(method = "awardKillScore", at = @At("HEAD"), cancellable = true)
    private void pinferno$preventAdvancementStatIncrease(Entity entityKilled, DamageSource damageSource, CallbackInfo ci) {
        if (damageSource.is(ModDamageTypes.CONDUIT)) {
            ci.cancel();
        }
    }

    @Redirect(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;resetStat(Lnet/minecraft/stats/Stat;)V", ordinal = 1))
    private void pinferno$preventTimeSinceRestResetOnDeath(ServerPlayer instance, Stat<?> stat) {
        if (stat != Stats.CUSTOM.get(Stats.TIME_SINCE_REST)) {
            instance.resetStat(stat);
        }
    }

    @Inject(method = "die", at = @At("HEAD"))
    private void pinferno$saveInventory(DamageSource source, CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        TempInventoryStorage.savePlayerInventory(player);
    }

    @ModifyConstant(method = "startSleepInBed", constant = @Constant(doubleValue = 5.0))
    private double pinferno$modifyDoubleValue(double original) {
        return 3.0;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void pinferno$tick(CallbackInfo ci) {
        ServerPlayer self = (ServerPlayer)(Object)this;
        if (this.getArmorValue() >= 100) {
            ModTriggers.ARMOR_VALUE.get().trigger(self);
        }
        // hardcore
        if (this.level().getLevelData().isHardcore()) {
            if (this.getFoodData().getFoodLevel() <= 6) {
                this.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 20, 0, false, true, true));
                this.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, 20, 0, false, true, true));
            }
        }
    }

    @Inject(method = "setRespawnPosition", at = @At("HEAD"), cancellable = true)
    private void pinferno$removeMsgInHardcore(ServerPlayer.RespawnConfig respawnConfig, boolean displayInChat, CallbackInfo ci) {
        if (this.level().getLevelData().isHardcore()) {
            this.respawnConfig = respawnConfig;
            ci.cancel();
        }
    }

    @Inject(method = "jumpFromGround", at = @At("TAIL"))
    private void pinferno$modifyJumpExhaustionHardcore(CallbackInfo ci) {
        ServerPlayer self = (ServerPlayer)(Object)this;
        if (self.level().getServer().isHardcore()) {
            this.causeFoodExhaustion(0.01F);
        }
        else {
            // do nothing
        }
    }

    @Redirect(method = "checkMovementStatistics", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;causeFoodExhaustion(F)V"))
    private void pinferno$modifyWalkExhaustionHardcore(ServerPlayer player, float originalExhaustion) {
        if (player.onGround() && player.level().getLevelData().isHardcore()) {
            this.causeFoodExhaustion(0.01F);
        }
        else {
            this.causeFoodExhaustion(originalExhaustion);
        }
    }

    @Inject(method = "startSleepInBed", at = @At("RETURN"), cancellable = true)
    private void pinferno$overrideTooFarMessage(BlockPos at, CallbackInfoReturnable<Either<BedSleepingProblem, Unit>> cir) {
        Either<Player.BedSleepingProblem, Unit> result = cir.getReturnValue();

        if (result.left().isPresent() && result.left().get() == Player.BedSleepingProblem.TOO_FAR_AWAY) {
            if (this.level().getLevelData().isHardcore()) {
                this.sendOverlayMessage(Component.translatable("block.minecraft.bed.too_far_away_hardcore"));
                cir.setReturnValue(Either.left(Player.BedSleepingProblem.OTHER_PROBLEM));
            }
        }
    }

    @WrapOperation(method = "restoreFrom", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/gamerules/GameRules;get(Lnet/minecraft/world/level/gamerules/GameRule;)Ljava/lang/Object;"))
    private Object pinferno$modifyKeepInventoryRule(GameRules instance, GameRule<?> key, Operation<Object> original) {
        ServerPlayer self = (ServerPlayer) (Object) this;
        Object result = original.call(instance, key);

        if (key == GameRules.KEEP_INVENTORY) {
            boolean ruleValue = false;
            if (result instanceof Boolean) ruleValue = (Boolean) result;
            return ruleValue || self.hasEffect(ModEffects.WELL_RESTED.holder);
        }

        return result;
    }
}
