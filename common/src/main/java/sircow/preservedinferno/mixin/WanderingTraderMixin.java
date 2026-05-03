package sircow.preservedinferno.mixin;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.item.trading.TradeSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.trade.*;

import java.util.*;

@Mixin(WanderingTrader.class)
public abstract class WanderingTraderMixin extends AbstractVillager {
    @Unique private static final int MAX_SETS = 5;

    @Unique private static final List<WeightedSet> SETS = List.of(
            new WeightedSet(key("essential"), 10, true),
            new WeightedSet(key("secondary"), 10, true),
            new WeightedSet(key("premium"), 10, true),
            new WeightedSet(key("floral"), 2, false),
            new WeightedSet(key("arbor"), 3, false),
            new WeightedSet(key("cultured"), 4, false),
            new WeightedSet(key("misc_plant"), 2, false),
            new WeightedSet(key("artistry"), 4, false),
            new WeightedSet(key("misc_block"), 4, false),
            new WeightedSet(key("coral"), 2, false)
    );

    public WanderingTraderMixin(EntityType<? extends AbstractVillager> entityType, net.minecraft.world.level.Level level) {
        super(entityType, level);
    }

    @Inject(method = "updateTrades", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$overrideTrades(CallbackInfo ci) {
        this.getOffers().clear();

        ServerLevel level = (ServerLevel) this.level();
        RandomSource random = this.random;
        List<ResourceKey<TradeSet>> selected = pickTradeSets(random);

        for (ResourceKey<TradeSet> key : selected) {
            this.addOffersFromTradeSet(level, this.getOffers(), key);
        }

        ci.cancel();
    }

    @Unique
    private static List<ResourceKey<TradeSet>> pickTradeSets(RandomSource random) {
        List<WeightedSet> guaranteed = SETS.stream().filter(WeightedSet::guaranteed).toList();
        List<WeightedSet> weighted = new ArrayList<>(SETS.stream().filter(s -> !s.guaranteed()).toList());
        List<ResourceKey<TradeSet>> result = new ArrayList<>();

        for (WeightedSet set : guaranteed) {
            result.add(set.key());
        }

        int remaining = MAX_SETS - result.size();

        for (int i = 0; i < remaining && !weighted.isEmpty(); i++) {
            WeightedSet picked = pick(weighted, random);
            result.add(picked.key());
            weighted.remove(picked);
        }
        return result;
    }

    @Unique
    private static WeightedSet pick(List<WeightedSet> list, RandomSource random) {
        int total = 0;
        for (WeightedSet s : list) {
            total += s.weight();
        }

        int r = random.nextInt(total);
        int acc = 0;

        for (WeightedSet s : list) {
            acc += s.weight();
            if (r < acc) {
                return s;
            }
        }

        return list.getLast();
    }

    @Unique
    private static ResourceKey<TradeSet> key(String id) {
        return ResourceKey.create(Registries.TRADE_SET, Constants.id("wandering_trader/" + id));
    }
}
