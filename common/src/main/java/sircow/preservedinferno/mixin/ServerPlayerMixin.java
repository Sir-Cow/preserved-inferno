package sircow.preservedinferno.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.effect.ModEffects;
import sircow.preservedinferno.other.ModDamageTypes;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    // prevent advancements where player needs to kill a mob from granting when killed by conduit
    @Inject(method = "awardKillScore", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$preventAdvancementStatIncrease(Entity entityKilled, DamageSource damageSource, CallbackInfo ci) {
        if (damageSource.is(ModDamageTypes.CONDUIT)) {
            ci.cancel();
        }
    }

    @Redirect(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;resetStat(Lnet/minecraft/stats/Stat;)V", ordinal = 1))
    private void preserved_inferno$preventTimeSinceRestResetOnDeath(ServerPlayer instance, Stat<?> stat) {
        if (stat != Stats.CUSTOM.get(Stats.TIME_SINCE_REST)) {
            instance.resetStat(stat);
        }
    }

    @ModifyConstant(method = "startSleepInBed", constant = @Constant(doubleValue = 5.0))
    private double preserved_inferno$modifyDoubleValue(double original) {
        return 3.0;
    }

    @WrapOperation(
            method = "restoreFrom",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"
            )
    )
    private boolean preserved_inferno$modifyKeepInventoryRule(GameRules instance, GameRules.Key<GameRules.BooleanValue> key, Operation<Boolean> original) {
        ServerPlayer self = (ServerPlayer)(Object)this;
        if (key == GameRules.RULE_KEEPINVENTORY) {
            return original.call(instance, key) || self.hasEffect(ModEffects.WELL_RESTED);
        }

        return original.call(instance, key);
    }
}
