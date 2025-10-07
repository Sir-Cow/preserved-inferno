package sircow.preservedinferno.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.trade.TradeRotator;
import sircow.preservedinferno.trade.VillagerFlags;

@Mixin(Villager.class)
public class VillagerMixin implements VillagerFlags {
    @Inject(method = "spawnGolemIfNeeded", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$cancelGolemSpawn(ServerLevel serverLevel, long gameTime, int minVillagerAmount, CallbackInfo ci) {
        ci.cancel();
    }

    @ModifyReturnValue(method = "canBreed", at = @At("RETURN"))
    private boolean preserved_inferno$preventBreeding(boolean original) {
        return false;
    }

    @Inject(method = "updateSpecialPrices", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$noCuringDiscounts(Player player, CallbackInfo ci) {
        if (player.hasEffect(MobEffects.HERO_OF_THE_VILLAGE)) {
            return;
        }
        ci.cancel();
    }

    @Inject(method = "setVillagerData", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$keepProfession(VillagerData data, CallbackInfo ci) {
        Villager self = (Villager)(Object)this;
        VillagerData currentData = self.getVillagerData();

        if (data.level() > currentData.level() && data.profession() == currentData.profession()) {
            self.getOffers().clear();
        }
        if (self == null || !self.isAlive()) {
            return;
        }
        if (data.profession().is(VillagerProfession.NONE) && !self.getVillagerData().profession().is(VillagerProfession.NONE)) {
            ci.cancel();
        }
    }

    @Inject(method = "setVillagerData", at = @At("TAIL"))
    private void preserved_inferno$clearTradesOnLevelUp(VillagerData data, CallbackInfo ci) {
        Villager self = (Villager)(Object)this;
        VillagerData currentData = self.getVillagerData();

        if (data.level() > currentData.level() && data.profession() == currentData.profession()) {
            RandomSource random = self.getRandom();
            int newMasteryLevel = data.level();

            self.getOffers().clear();
            TradeRotator.rotateTrades(self, newMasteryLevel, random);
        }
    }

    @Unique private boolean pi$didSleep = false;
    @Unique private boolean pi$didGather = false;
    @Unique private boolean pi$didPanic = false;
    @Unique private boolean pi$tradesRotatedToday = false;
    @Unique boolean didDailyRefresh = false;

    // === Flag accessors ===
    @Override public boolean pi$didSleep() { return pi$didSleep; }
    @Override public void pi$setDidSleep(boolean value) { this.pi$didSleep = value; }
    @Override public boolean pi$didGather() { return pi$didGather; }
    @Override public void pi$setDidGather(boolean value) { this.pi$didGather = value; }
    @Override public boolean pi$didPanic() { return pi$didPanic; }
    @Override public void pi$setDidPanic(boolean value) { this.pi$didPanic = value; }
    @Override public boolean pi$tradesRotatedToday() { return this.pi$tradesRotatedToday; }
    @Override public void pi$setTradesRotatedToday(boolean value) { this.pi$tradesRotatedToday = value; }

    @Inject(method = "stopSleeping", at = @At("TAIL"))
    private void preserved_inferno$markSlept(CallbackInfo ci) {
        this.pi$setDidSleep(true);
    }

    @Inject(method = "customServerAiStep", at = @At("HEAD"))
    private void preserved_inferno$dailyCheckAndReset(ServerLevel level, CallbackInfo ci) {
        Villager self = (Villager)(Object)this;
        long dayTime = level.getDayTime() % 24000L;

        if (self.getBrain().hasMemoryValue(MemoryModuleType.MEETING_POINT)) {
            ((VillagerFlags) self).pi$setDidGather(true);
        }

        if (self.getBrain().isActive(Activity.PANIC)) {
            this.pi$setDidPanic(true);
        }

        // start of day
        if (!level.isMoonVisible() && dayTime < 50) {
            this.pi$setTradesRotatedToday(false);
            didDailyRefresh = false;
        }

        // end of the work day
        if (!this.pi$tradesRotatedToday() && dayTime > 6000 && dayTime < 8000) {
            if (!didDailyRefresh) {
                //Constants.LOG.info("sleep {}, gather {}, panic {}", this.pi$didSleep, this.pi$didGather, this.pi$didPanic);
                if (this.pi$didSleep && this.pi$didGather && !this.pi$didPanic) {
                    int masteryLevel = self.getVillagerData().level();
                    RandomSource random = self.getRandom();
                    TradeRotator.rotateTrades(self, masteryLevel, random);
                }
                this.pi$setDidSleep(false);
                this.pi$setDidGather(false);
                this.pi$setDidPanic(false);
                this.pi$setTradesRotatedToday(true);
                didDailyRefresh = true;
            }
        }
    }

    @Inject(method = "updateTrades", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$replaceInitialTrades(CallbackInfo ci) {
        Villager self = (Villager)(Object)this;
        if (self.getOffers().isEmpty()) {
            int masteryLevel = self.getVillagerData().level();
            RandomSource random = self.getRandom();
            TradeRotator.rotateTrades(self, masteryLevel, random);
            ci.cancel();
        }
    }

    @ModifyReturnValue(method = "canRestock", at = @At("RETURN"))
    private boolean preserved_inferno$blockCanRestock(boolean original) {
        return false;
    }

    @ModifyReturnValue(method = "shouldRestock", at = @At("RETURN"))
    private boolean preserved_inferno$conditionalShouldRestock(boolean original) {
        return false;
    }

    @Inject(method = "catchUpDemand", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$cancelCatchup(CallbackInfo ci) {
        ci.cancel();
    }
}
