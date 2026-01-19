package sircow.preservedinferno.trade;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.item.trading.MerchantOffer;

import java.util.ArrayList;
import java.util.List;

public record WeightedTradePool(List<WeightedTradeEntry> entries, int rolls) {

    public List<WeightedTradeResult> rollOffers(ServerLevel serverLevel, WanderingTrader trader, RandomSource random) {
        List<WeightedTradeResult> results = new ArrayList<>();

        for (WeightedTradeEntry entry : entries) {
            if (entry.guaranteed()) {
                MerchantOffer offer = entry.getOffer(serverLevel, trader, random);
                if (offer != null) {
                    results.add(new WeightedTradeResult(entry, offer));
                }
            }
        }

        for (int i = 0; i < rolls; i++) {
            WeightedTradeEntry entry = pickWeighted(random);
            if (entry.guaranteed()) continue;
            MerchantOffer offer = entry.getOffer(serverLevel, trader, random);
            if (offer != null) {
                results.add(new WeightedTradeResult(entry, offer));
            }
        }

        return results;
    }


    private WeightedTradeEntry pickWeighted(RandomSource random) {
        List<WeightedTradeEntry> nonGuaranteed = entries.stream()
                .filter(e -> !e.guaranteed())
                .toList();

        int total = nonGuaranteed.stream().mapToInt(WeightedTradeEntry::weight).sum();
        if (total <= 0) return nonGuaranteed.get(random.nextInt(nonGuaranteed.size()));

        int choice = random.nextInt(total);
        int cumulative = 0;
        for (WeightedTradeEntry e : nonGuaranteed) {
            cumulative += e.weight();
            if (choice < cumulative) return e;
        }
        return nonGuaranteed.getLast();
    }

    public record WeightedTradeResult(WeightedTradeEntry entry, MerchantOffer offer) {}
}
