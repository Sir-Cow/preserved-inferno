package sircow.preservedinferno.trade;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.trading.MerchantOffer;

import java.util.ArrayList;
import java.util.List;

public record WeightedTradePool(List<WeightedTradeEntry> entries, int rolls) {

    public List<WeightedTradeResult> rollOffers(WanderingTrader trader, RandomSource random) {
        List<WeightedTradeResult> results = new ArrayList<>();

        // Always include guaranteed entries
        for (WeightedTradeEntry entry : entries) {
            if (entry.guaranteed()) {
                MerchantOffer offer = entry.getOffer(trader, random);
                if (offer != null) {
                    results.add(new WeightedTradeResult(entry, offer));
                }
            }
        }

        // Then do weighted rolls for the rest
        for (int i = 0; i < rolls; i++) {
            WeightedTradeEntry entry = pickWeighted(random);
            if (entry.guaranteed()) continue; // don’t re-roll guaranteed
            MerchantOffer offer = entry.getOffer(trader, random);
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
