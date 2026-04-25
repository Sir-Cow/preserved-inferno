package sircow.preservedinferno.trade;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.item.trading.TradeSet;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import sircow.preservedinferno.mixin.MerchantOfferAccessor;
import sircow.preservedinferno.mixin.VillagerTradeAccessor;

import java.util.*;

public final class TradeRotator {

    private TradeRotator() {}

    public static void rebuildTrades(Villager villager, int masteryLevel, RandomSource random) {
        if (!(villager.level() instanceof ServerLevel level)) return;

        MerchantOffers offers = new MerchantOffers();
        addSlotOffers(villager, level, offers, 1, 2, random);

        if (masteryLevel >= 2) addSlotOffers(villager, level, offers, 2, 1, random);
        if (masteryLevel >= 3) addSlotOffers(villager, level, offers, 3, 1, random);
        if (masteryLevel >= 4) addSlotOffers(villager, level, offers, 4, 1, random);
        if (masteryLevel >= 5) addSlotOffers(villager, level, offers, 5, 1, random);

        villager.setOffers(offers);
    }

    private static void addSlotOffers(Villager villager, ServerLevel level, MerchantOffers offers, int poolLevel, int count, RandomSource random) {
        Registry<TradeSet> registry = level.registryAccess().lookupOrThrow(Registries.TRADE_SET);
        VillagerProfession profession = villager.getVillagerData().profession().value();
        ResourceKey<TradeSet> key = profession.getTrades(poolLevel);
        if (key == null) return;
        TradeSet set = registry.getValueOrThrow(key);
        List<VillagerTrade> trades = new ArrayList<>();
        set.getTrades().forEach(h -> trades.add(h.value()));

        if (trades.isEmpty()) return;

        for (int i = 0; i < count && !trades.isEmpty(); i++) {
            VillagerTrade trade = trades.remove(random.nextInt(trades.size()));
            MerchantOffer offer = createOffer(villager, level, trade);
            if (offer != null) offers.add(offer);
        }
    }

    private static MerchantOffer createOffer(Villager villager, ServerLevel level, VillagerTrade trade) {
        LootParams params = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, villager.position())
                .withParameter(LootContextParams.THIS_ENTITY, villager)
                .withParameter(LootContextParams.ADDITIONAL_COST_COMPONENT_ALLOWED, Unit.INSTANCE)
                .create(LootContextParamSets.VILLAGER_TRADE);

        LootContext context = new LootContext.Builder(params).create(java.util.Optional.empty());
        MerchantOffer offer = trade.getOffer(context);
        if (offer == null) return null;

        MerchantOfferAccessor accessor = (MerchantOfferAccessor) offer;
        int intendedMaxUses = ((VillagerTradeAccessor) trade).getMaxUses().getInt(context);

        accessor.setMaxUses(Math.max(intendedMaxUses, 1));
        offer.resetUses();
        accessor.setDemand(0);

        return offer;
    }
}
