package sircow.preservedinferno.trade;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.npc.villager.VillagerTrades;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.item.trading.MerchantOffer;

public record WeightedTradeEntry(VillagerTrades.ItemListing listing, int weight, boolean guaranteed) {
    public int weight() {
        return weight;
    }

    public MerchantOffer getOffer(ServerLevel serverLevel, WanderingTrader trader, RandomSource random) {
        return listing.getOffer(serverLevel, trader, random);
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
