package sircow.preservedinferno.trade;

public record WeightedTradeTable(WeightedTradePool pool, int weight, boolean guaranteed) {
    public WeightedTradeTable(WeightedTradePool pool, int weight) {
        this(pool, weight, false);
    }
}
