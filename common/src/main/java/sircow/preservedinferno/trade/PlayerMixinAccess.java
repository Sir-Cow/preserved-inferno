package sircow.preservedinferno.trade;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.villager.VillagerProfession;

public interface PlayerMixinAccess {
    void markTraded(ResourceKey<VillagerProfession> key);
    boolean hasTradedAll();
    int getTradedProfessions();
    void setTradedProfessions(int mask);
    int getTradedCount();
}
