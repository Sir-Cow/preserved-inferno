package sircow.preservedinferno.mixin;

import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.item.ModItems;
import sircow.preservedinferno.trade.*;

import java.util.*;

@Mixin(WanderingTrader.class)
public abstract class WanderingTraderMixin extends AbstractVillager {

    @Unique
    private static final List<WeightedTradeTable> WANDERING_TRADER_TRADES = new ArrayList<>();

    public WanderingTraderMixin(EntityType<? extends AbstractVillager> entityType, Level level) {
        super(entityType, level);
    }

    static {
        // set 1
        WANDERING_TRADER_TRADES.add(new WeightedTradeTable(
                new WeightedTradePool(List.of(
                        new WeightedTradeEntry(new ModEmeraldsForItems(Items.HAY_BLOCK, 4, 1, 1, 1), 10),
                        new WeightedTradeEntry(new ModEmeraldsForItems(Items.CARROT, 16, 1, 1, 1), 10),
                        new WeightedTradeEntry(new ModEmeraldsForItems(Items.GLASS_BOTTLE, 12, 1, 1, 1), 10),
                        new WeightedTradeEntry(new ModEmeraldsForItems(Items.FEATHER, 6, 1, 1, 1), 10),
                        new WeightedTradeEntry(new ModEmeraldsForItems(Items.FERMENTED_SPIDER_EYE, 1, 1, 1, 1), 10)
                ), 2), 10, true));
        // set 2
        WANDERING_TRADER_TRADES.add(new WeightedTradeTable(
                new WeightedTradePool(List.of(
                        new WeightedTradeEntry(new ModEmeraldsForItems(Items.REDSTONE, 16, 1, 1, 1), 10),
                        new WeightedTradeEntry(new ModEmeraldsForItems(Items.COPPER_INGOT, 9, 1, 1, 1), 10),
                        new WeightedTradeEntry(new ModEmeraldsForItems(Items.IRON_INGOT, 4, 1, 1, 1), 10),
                        new WeightedTradeEntry(new ModEmeraldsForItems(Items.GOLD_INGOT, 2, 1, 1, 1), 10)
                ), 1), 10, true));
        // set 3
        WANDERING_TRADER_TRADES.add(new WeightedTradeTable(
                new WeightedTradePool(List.of(
                        new WeightedTradeEntry(new ModItemsForEmeralds(potion(Potions.LONG_INVISIBILITY), 3, 1, 3, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(potion(Potions.LONG_WATER_BREATHING), 3, 1, 3, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.RESIN_CLUMP, 1, 1, 9, 1), 8),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.NAUTILUS_SHELL, 16, 1, 4, 1), 8),
                        new WeightedTradeEntry(new ModEnchantedItemForEmeralds(Items.IRON_PICKAXE, 14, 1, 1, 1.0F), 5),
                        new WeightedTradeEntry(new ModEnchantedItemForEmeralds(Items.IRON_AXE, 14, 1, 1, 1.0F), 5),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.LAPIS_LAZULI, 18, 1, 1, 1), 2),
                        new WeightedTradeEntry(new ModItemsForEmeralds(ModItems.COPPER_TRIDENT, 55, 1, 1, 1), 2)
                ), 2), 10, true));
        // set 4
        WANDERING_TRADER_TRADES.add(new WeightedTradeTable(
                new WeightedTradePool(List.of(
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.DANDELION, 1, 3, 4, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.POPPY, 1, 3, 4, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.ALLIUM, 1, 3, 4, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.AZURE_BLUET, 1, 3, 4, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.RED_TULIP, 1, 3, 4, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.ORANGE_TULIP, 1, 3, 4, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.WHITE_TULIP, 1, 3, 4, 1),10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.PINK_TULIP, 1, 3, 4, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.OXEYE_DAISY, 1, 3, 4, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.CORNFLOWER, 1, 3, 4, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.BLUE_ORCHID, 1, 3, 4, 1), 5),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.LILY_OF_THE_VALLEY, 1, 3, 4, 1),3),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.OPEN_EYEBLOSSOM, 1, 3, 4, 1), 2)
                ), 3), 2, false));
        // set 4
        WANDERING_TRADER_TRADES.add(new WeightedTradeTable(
                new WeightedTradePool(List.of(
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.OAK_SAPLING, 2, 1, 4, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.BIRCH_SAPLING, 2, 1, 4, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.SPRUCE_SAPLING, 2, 1, 4, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.JUNGLE_SAPLING, 2, 1, 4, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.DARK_OAK_SAPLING, 2, 1, 4, 1), 8),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.ACACIA_SAPLING, 2, 1, 4, 1), 8),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.CHERRY_SAPLING, 2, 1, 4, 1), 6),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.MANGROVE_PROPAGULE, 2, 1, 4, 1),4),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.PALE_OAK_SAPLING, 2, 1, 4, 1), 4)
                ), 3), 3, false));
        // set 4
        WANDERING_TRADER_TRADES.add(new WeightedTradeTable(
                new WeightedTradePool(List.of(
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.WHEAT_SEEDS, 3, 2, 3, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.PUMPKIN_SEEDS, 3, 2, 3, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.SUGAR_CANE, 3, 2, 3, 1), 8),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.KELP, 3, 2, 3, 1),8),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.BEETROOT_SEEDS, 3, 2, 3, 1), 5),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.MELON_SEEDS, 3, 2, 3, 1), 5),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.RED_MUSHROOM, 1, 2, 8, 1), 4),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.BROWN_MUSHROOM, 1, 2, 8, 1),4),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.CACTUS, 3, 2, 3, 1), 2),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.COCOA_BEANS, 3, 2, 3, 1), 2),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.SEA_PICKLE, 3, 2, 3, 1), 2)
                ), 3), 4, false));
        // set 4
        WANDERING_TRADER_TRADES.add(new WeightedTradeTable(
                new WeightedTradePool(List.of(
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.FERN, 1, 4, 8, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.VINE, 1, 4, 8, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.PALE_HANGING_MOSS, 1, 4, 8, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.LILY_PAD, 1, 4, 8, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.SMALL_DRIPLEAF, 1, 4, 8, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.WILDFLOWERS, 1, 4, 8, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.DRY_TALL_GRASS, 1, 4, 8, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.FIREFLY_BUSH, 1, 4, 8, 1), 10)
                ), 3), 2, false));
        // set 5
        WANDERING_TRADER_TRADES.add(new WeightedTradeTable(
                new WeightedTradePool(List.of(
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.RED_DYE, 1, 8, 8, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.WHITE_DYE, 1, 8, 8, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.BLUE_DYE, 1, 8, 8, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.PINK_DYE, 1, 8, 8, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.BLACK_DYE, 1, 8, 8, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.GREEN_DYE, 1, 8, 8, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.LIGHT_GRAY_DYE, 1, 8, 8, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.MAGENTA_DYE, 1, 8, 8, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.YELLOW_DYE, 1, 8, 8, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.GRAY_DYE, 1, 8, 8, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.PURPLE_DYE, 1, 8, 8, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.LIGHT_GRAY_DYE, 1, 8, 8, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.LIME_DYE, 1, 8, 8, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.ORANGE_DYE, 1, 8, 8, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.BROWN_DYE, 1, 8, 8, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.CYAN_DYE, 1, 8, 8, 1), 10)
                ), 2), 4, false));
        // set 5
        WANDERING_TRADER_TRADES.add(new WeightedTradeTable(
                new WeightedTradePool(List.of(
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.BRAIN_CORAL_BLOCK, 3, 8, 3, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.BUBBLE_CORAL_BLOCK, 3, 8, 3, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.FIRE_CORAL_BLOCK, 3, 8, 3, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.HORN_CORAL_BLOCK, 3, 8, 3, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.TUBE_CORAL_BLOCK, 3, 8, 3, 1), 10)
                ), 2), 2, false));
        // set 5
        WANDERING_TRADER_TRADES.add(new WeightedTradeTable(
                new WeightedTradePool(List.of(
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.SAND, 3, 8, 4, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.RED_SAND, 3, 8, 4, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.POINTED_DRIPSTONE, 3, 8, 4, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.ROOTED_DIRT, 3, 8, 4, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.MOSS_BLOCK, 3, 8, 4, 1), 10),
                        new WeightedTradeEntry(new ModItemsForEmeralds(Items.PALE_MOSS_BLOCK, 3, 8, 4, 1), 10)
                ), 2), 4, false));
    }

    @Inject(method = "updateTrades", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$overrideTrades(CallbackInfo ci) {
        this.getOffers().clear();
        RandomSource random = this.random;

        int maxTables = 5;
        int maxTrades = 10;

        // split guaranteed vs weighted tables
        List<WeightedTradeTable> guaranteed = WANDERING_TRADER_TRADES.stream()
                .filter(WeightedTradeTable::guaranteed)
                .toList();

        List<WeightedTradeTable> weighted = new ArrayList<>(WANDERING_TRADER_TRADES.stream()
                .filter(t -> !t.guaranteed())
                .toList());

        // always include guaranteed tables
        List<WeightedTradeTable> selectedSets = new ArrayList<>(guaranteed);

        // fill the rest up to maxTables with weighted picks
        int remaining = maxTables - selectedSets.size();
        for (int i = 0; i < remaining && !weighted.isEmpty(); i++) {
            WeightedTradeTable picked = pickWeightedTable(weighted, random);
            selectedSets.add(picked);
            weighted.remove(picked);
        }

        // build trades
        Set<String> seenKeys = new HashSet<>();
        int tradesAdded = 0;

        List<MerchantOffer> emeraldsForItems = new ArrayList<>();
        List<MerchantOffer> others = new ArrayList<>();

        for (WeightedTradeTable table : selectedSets) {
            WeightedTradePool pool = table.pool();

            if (table.guaranteed()) {
                int needed = pool.rolls();
                int successful = 0;
                int attempts = 0;
                int attemptCap = Math.max(needed * 40, 200); // guard to avoid infinite loops

                while (successful < needed && attempts < attemptCap) {
                    attempts++;

                    WeightedTradeEntry entry = pool.entries().get(random.nextInt(pool.entries().size()));
                    MerchantOffer offer = entry.getOffer((WanderingTrader) (Object) this, random);
                    if (offer == null) continue;

                    String key = offer.getResult().getItem().toString()
                            + "|" + offer.getBaseCostA().getItem().toString()
                            + "|" + (offer.getCostB() != null ? offer.getCostB().getItem().toString() : "NONE");

                    if (seenKeys.contains(key)) continue;

                    if (entry.isEmeraldsForItems()) {
                        emeraldsForItems.add(offer);
                    }
                    else {
                        others.add(offer);
                    }

                    seenKeys.add(key);
                    tradesAdded++;
                    successful++;
                }
            }
            else {
                for (int i = 0; i < pool.rolls(); i++) {
                    if (tradesAdded >= maxTrades) break;

                    WeightedTradeEntry entry = pool.entries()
                            .get(random.nextInt(pool.entries().size()));
                    MerchantOffer offer = entry.getOffer((WanderingTrader) (Object) this, random);
                    if (offer == null) continue;

                    String key = offer.getResult().getItem().toString()
                            + "|" + offer.getBaseCostA().getItem().toString()
                            + "|" + (offer.getCostB() != null ? offer.getCostB().getItem().toString() : "NONE");

                    if (seenKeys.contains(key)) continue;

                    if (entry.isEmeraldsForItems()) {
                        emeraldsForItems.add(offer);
                    }
                    else {
                        others.add(offer);
                    }

                    seenKeys.add(key);
                    tradesAdded++;
                }
            }
        }

        for (MerchantOffer o : emeraldsForItems) {
            if (this.getOffers().size() >= maxTrades) break;
            this.getOffers().add(o);
        }
        for (MerchantOffer o : others) {
            if (this.getOffers().size() >= maxTrades) break;
            this.getOffers().add(o);
        }

        ci.cancel();
    }

    @Unique
    private static WeightedTradeTable pickWeightedTable(List<WeightedTradeTable> tables, RandomSource random) {
        int total = tables.stream().mapToInt(WeightedTradeTable::weight).sum();
        int choice = random.nextInt(total);
        int cumulative = 0;
        for (WeightedTradeTable t : tables) {
            cumulative += t.weight();
            if (choice < cumulative) return t;
        }
        return tables.getLast();
    }

    @Unique
    private static ItemStack potion(Holder<Potion> potion) {
        return PotionContents.createItemStack(Items.POTION, potion);
    }
}
