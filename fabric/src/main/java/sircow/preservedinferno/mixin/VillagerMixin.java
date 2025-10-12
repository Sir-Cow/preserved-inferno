package sircow.preservedinferno.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.trade.PlayerMixinAccess;
import sircow.preservedinferno.trade.TradeRotator;
import sircow.preservedinferno.trade.VillagerFlags;
import sircow.preservedinferno.trigger.ModTriggers;

import java.util.Optional;

@Mixin(Villager.class)
public class VillagerMixin implements VillagerFlags {
    @Unique private VillagerData pi$previousData;

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
        this.pi$previousData = self.getVillagerData();

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
        if (!(self.level() instanceof ServerLevel level)) return;
        VillagerData prev = this.pi$previousData;
        this.pi$previousData = null;

        if (prev == null) return;

        if (data.profession() == prev.profession() && data.level() > prev.level()) {
            RandomSource random = self.getRandom();
            int newMasteryLevel = data.level();

            self.getOffers().clear();
            TradeRotator.rotateTrades(self, newMasteryLevel, random);

            if (data.level() == 5) {
                level.getPlayers(player -> player.distanceToSqr(self) <= 100 * 100).forEach(ModTriggers.MAX_VILLAGER::trigger);
            }
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
                if (this.pi$didSleep && this.pi$didGather && !this.pi$didPanic) {
                    int masteryLevel = self.getVillagerData().level();
                    RandomSource random = self.getRandom();
                    TradeRotator.rotateTrades(self, masteryLevel, random);
                    level.getPlayers(player -> player.distanceToSqr(self) <= 100 * 100).forEach(ModTriggers.VILLAGER_RESTOCK::trigger);
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
        int masteryLevel = self.getVillagerData().level();
        RandomSource random = self.getRandom();
        self.getOffers().clear();
        TradeRotator.rotateTrades(self, masteryLevel, random);
        MerchantOffers offers = self.getOffers();

        // limit max trades per mastery level
        int maxOffers = switch (masteryLevel) {
            case 1 -> 2; // Novice
            case 2 -> 3; // Apprentice
            case 3 -> 4; // Journeyman
            case 4 -> 5; // Expert
            case 5 -> 6; // Master
            default -> 2;
        };
        while (offers.size() > maxOffers) {
            offers.removeLast();
        }
        ci.cancel();
    }

    @Inject(method = "rewardTradeXp", at = @At("RETURN"))
    private void preserved_inferno$onTrade(MerchantOffer offer, CallbackInfo ci) {
        Villager villager = (Villager)(Object)this;

        if (!(villager.getTradingPlayer() instanceof ServerPlayer player)) return;
        if (!(villager.level() instanceof ServerLevel)) return;

        if (player instanceof PlayerMixinAccess access) {
            Optional<ResourceKey<VillagerProfession>> profKeyOpt = villager.getVillagerData().profession().unwrapKey();
            profKeyOpt.ifPresent(access::markTraded);

            if (access.hasTradedAll()) {
                ModTriggers.TRADE_EVERY_VILLAGER.trigger(player);
            }
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
