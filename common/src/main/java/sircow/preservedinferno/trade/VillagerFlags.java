package sircow.preservedinferno.trade;

public interface VillagerFlags {
    boolean pi$didSleep();
    void pi$setDidSleep(boolean value);

    boolean pi$didGather();
    void pi$setDidGather(boolean value);

    boolean pi$didPanic();
    void pi$setDidPanic(boolean value);

    boolean pi$tradesRotatedToday();
    void pi$setTradesRotatedToday(boolean value);
}
