package sircow.preservedinferno.trade;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.trading.TradeSet;

public record WeightedSet(ResourceKey<TradeSet> key, int weight, boolean guaranteed) {}
