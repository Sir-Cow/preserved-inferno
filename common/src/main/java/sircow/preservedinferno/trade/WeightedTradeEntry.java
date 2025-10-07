package sircow.preservedinferno.trade;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.trading.MerchantOffer;

public record WeightedTradeEntry(VillagerTrades.ItemListing listing, int weight, boolean guaranteed) {
    public int weight() {
        return weight;
    }

    public MerchantOffer getOffer(WanderingTrader trader, RandomSource random) {
        return listing.getOffer(trader, random);
    }

    public boolean isEmeraldsForItems() {
        return listing instanceof ModEmeraldsForItems;
    }

    public VillagerTrades.ItemListing getListing() {
        return listing;
    }

    public WeightedTradeEntry(VillagerTrades.ItemListing listing, int weight) {
        this(listing, weight, false);
    }
}
