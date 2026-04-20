package sircow.preservedinferno.trade;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.ItemStack;
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

        MerchantOffers offers = villager.getOffers();
        offers.clear();

        for (int lvl = 1; lvl <= masteryLevel; lvl++) {
            addTradesForLevel(villager, level, lvl, random);
        }

        villager.setOffers(offers);
    }

    public static void addTradesForLevel(Villager villager, ServerLevel level, int levelIndex, RandomSource random) {
        Registry<TradeSet> registry = level.registryAccess().lookupOrThrow(Registries.TRADE_SET);
        VillagerProfession profession = villager.getVillagerData().profession().value();

        ResourceKey<TradeSet> key = profession.getTrades(levelIndex);
        if (key == null) return;

        TradeSet set = registry.getValueOrThrow(key);
        List<VillagerTrade> trades = new ArrayList<>();
        set.getTrades().forEach(h -> trades.add(h.value()));

        if (trades.isEmpty()) return;

        int picks = Math.min(getMaxTrades(levelIndex), trades.size());
        MerchantOffers offers = villager.getOffers();

        for (int i = 0; i < picks; i++) {
            VillagerTrade trade = trades.remove(random.nextInt(trades.size()));
            MerchantOffer newOffer = createOffer(villager, level, trade);

            if (newOffer != null) {
                boolean duplicate = false;
                for (MerchantOffer existingOffer : offers) {
                    if (isSameTrade(existingOffer, newOffer)) {
                        duplicate = true;
                        break;
                    }
                }

                if (!duplicate) {
                    offers.add(newOffer);
                }
            }
        }
    }

    private static boolean isSameTrade(MerchantOffer a, MerchantOffer b) {
        return ItemStack.isSameItemSameComponents(a.getBaseCostA(), b.getBaseCostA()) &&
                ItemStack.isSameItemSameComponents(a.getCostB(), b.getCostB()) &&
                ItemStack.isSameItemSameComponents(a.getResult(), b.getResult());
    }

    private static MerchantOffer createOffer(Villager villager, ServerLevel level, VillagerTrade trade) {
        LootParams params = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, villager.position())
                .withParameter(LootContextParams.THIS_ENTITY, villager)
                .withParameter(LootContextParams.ADDITIONAL_COST_COMPONENT_ALLOWED, Unit.INSTANCE)
                .create(LootContextParamSets.VILLAGER_TRADE);

        LootContext context = new LootContext.Builder(params).create(Optional.empty());

        MerchantOffer offer = trade.getOffer(context);
        if (offer == null) return null;

        MerchantOfferAccessor accessor = (MerchantOfferAccessor) offer;
        int intendedMaxUses = ((VillagerTradeAccessor)trade).getMaxUses().getInt(context);

        accessor.setMaxUses(Math.max(intendedMaxUses, 1));
        offer.resetUses();
        accessor.setDemand(0);

        return offer;
    }

    public static int getMaxTrades(int level) {
        return switch (level) {
            case 1 -> 2;
            case 2 -> 3;
            case 3 -> 4;
            case 4 -> 5;
            case 5 -> 6;
            default -> 2;
        };
    }
}
