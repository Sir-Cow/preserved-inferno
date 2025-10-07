package sircow.preservedinferno.trade;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.StructureTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import sircow.preservedinferno.block.ModBlocks;
import sircow.preservedinferno.item.FabricModItems;
import sircow.preservedinferno.item.ModItems;
import sircow.preservedinferno.other.ModTags;

import java.util.*;
import java.util.function.Function;
import java.util.stream.StreamSupport;

public class TradeRotator {
    private static final Map<ResourceKey<VillagerProfession>, Map<Integer, List<Function<Villager, MerchantOffer>>>> TRADE_POOLS = new HashMap<>();

    static ItemStack enchantedIronAxe, enchantedIronHoe, enchantedIronPickaxe, enchantedIronShovel, enchantedIronSword, enchantedCrossbow, enchantedBow, waterBreatherPot, tippedArrowRandom, bannerRandom, carpetRandom, clothRandom, dyeRandom, woolRandom = ItemStack.EMPTY;
    static Item bannerRandomAsItem, carpetRandomAsItem, clothRandomAsItem, dyeRandomAsItem, woolRandomAsItem;

    private static List<Holder<Potion>> getAllPotions(RegistryAccess registryAccess) {
        return registryAccess
                .lookupOrThrow(Registries.POTION)
                .listElements()
                .map(holderRef -> (Holder<Potion>) holderRef)
                .toList();
    }

    static {
        // ===== ARMORER =====
        register(VillagerProfession.ARMORER, 1, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.COAL, 20), new ItemStack(Items.EMERALD, 1), 2, 2, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.RAW_COPPER, 24), new ItemStack(Items.EMERALD, 1), 2, 2, 1.2F)
        ));
        register(VillagerProfession.ARMORER, 2, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(Items.CHAINMAIL_HELMET, 1), 1, 5, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 2), new ItemStack(Items.CHAINMAIL_CHESTPLATE, 1), 1, 5, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 2), new ItemStack(Items.CHAINMAIL_LEGGINGS, 1), 1, 5, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(Items.CHAINMAIL_BOOTS, 1), 1, 5, 1.0F)
        ));
        register(VillagerProfession.ARMORER, 3, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.LAVA_BUCKET, 1), new ItemStack(Items.EMERALD, 1), 2, 10, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 5), new ItemStack(ModItems.IRON_SHIELD, 1), 2, 10, 1.0F)
        ));
        register(VillagerProfession.ARMORER, 4, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.ARMADILLO_SCUTE, 18), new ItemStack(Items.EMERALD, 1), 1, 20, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD_BLOCK, 21), new ItemStack(ModItems.DIAMOND_SHIELD, 1), 1, 20, 1.0F)
        ));
        register(VillagerProfession.ARMORER, 5, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD_BLOCK, 15), new ItemStack(Items.DIAMOND_HELMET, 1), 1, 50, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD_BLOCK, 24), new ItemStack(Items.DIAMOND_CHESTPLATE, 1), 1, 50, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD_BLOCK, 21), new ItemStack(Items.DIAMOND_LEGGINGS, 1), 1, 50, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD_BLOCK, 12), new ItemStack(Items.DIAMOND_BOOTS, 1), 1, 50, 1.0F)
        ));

        // ===== BUTCHER =====
        register(VillagerProfession.BUTCHER, 1, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.CHICKEN, 22), new ItemStack(Items.EMERALD, 1), 2, 2, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.PORKCHOP, 22), new ItemStack(Items.EMERALD, 1), 2, 2, 1.2F)
        ));
        register(VillagerProfession.BUTCHER, 2, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(Items.COOKED_PORKCHOP, 8), 1, 5, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(Items.COOKED_CHICKEN, 8), 2, 5, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.BEEF, 22), new ItemStack(Items.EMERALD, 1), 1, 5, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.MUTTON, 22), new ItemStack(Items.EMERALD, 1), 1, 5, 1.2F)
        ));
        register(VillagerProfession.BUTCHER, 3, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.APPLE, 8), new ItemStack(Items.EMERALD, 1), 2, 10, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(Items.RABBIT_STEW, 4), 2, 10, 1.0F)
        ));
        register(VillagerProfession.BUTCHER, 4, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.RABBIT, 5), new ItemStack(Items.EMERALD, 1), 2, 20, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.DRIED_KELP_BLOCK, 24), new ItemStack(Items.EMERALD, 1), 1, 20, 1.2F)
        ));
        register(VillagerProfession.BUTCHER, 5, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.SWEET_BERRIES, 42), new ItemStack(Items.EMERALD, 1), 1, 50, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 2), new ItemStack(Items.GOLDEN_APPLE, 1), 1, 50, 1.0F)
        ));

        // ===== CARTOGRAPHER =====
        register(VillagerProfession.CARTOGRAPHER, 1, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.PAPER, 36), new ItemStack(Items.EMERALD, 1), 2, 2, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.GLASS, 40), new ItemStack(Items.EMERALD, 1), 2, 2, 1.2F)
        ));
        register(VillagerProfession.CARTOGRAPHER, 2, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.ITEM_FRAME, 6), new ItemStack(Items.EMERALD, 1), 2, 5, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.COMPASS, 1), new ItemStack(Items.EMERALD, 1), 2, 5, 1.0F)
        ));
        register(VillagerProfession.CARTOGRAPHER, 3, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 3), new ItemStack(Items.MAP, 1), 2, 10, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), bannerRandom, 2, 10, 1.0F)
        ));
        register(VillagerProfession.CARTOGRAPHER, 4, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 8), new ItemStack(Items.GLOBE_BANNER_PATTERN, 1), 1, 20, 1.0F),
                new TreasureMapSupplier(12, StructureTags.ON_TRIAL_CHAMBERS_MAPS, "filled_map.trial_chambers", MapDecorationTypes.TRIAL_CHAMBERS, 1, 20)
        ));
        register(VillagerProfession.CARTOGRAPHER, 5, List.of(
                new TreasureMapSupplier(24, StructureTags.ON_OCEAN_EXPLORER_MAPS, "filled_map.monument", MapDecorationTypes.OCEAN_MONUMENT, 1, 50),
                new TreasureMapSupplier(26, StructureTags.ON_WOODLAND_EXPLORER_MAPS, "filled_map.mansion", MapDecorationTypes.WOODLAND_MANSION, 1, 50)
        ));

        // ===== CLERIC =====
        register(VillagerProfession.CLERIC, 1, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.ROTTEN_FLESH, 50), new ItemStack(Items.EMERALD, 1), 2, 2, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.GOLD_INGOT, 4), new ItemStack(Items.EMERALD, 1), 2, 2, 1.2F)
        ));
        register(VillagerProfession.CLERIC, 2, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.REDSTONE, 28), new ItemStack(Items.EMERALD, 1), 2, 5, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.RABBIT_FOOT, 2), new ItemStack(Items.EMERALD, 1), 2, 5, 1.0F)
        ));
        register(VillagerProfession.CLERIC, 3, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(Items.GLOWSTONE, 1), 2, 10, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 6), new ItemStack(Items.TURTLE_SCUTE, 1), 1, 10, 1.0F)
        ));
        register(VillagerProfession.CLERIC, 4, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.NETHER_WART, 35), new ItemStack(Items.EMERALD, 1), 2, 20, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 3), new ItemStack(Items.ENDER_PEARL, 1), 2, 20, 1.0F)
        ));
        register(VillagerProfession.CLERIC, 5, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.SCULK, 22), new ItemStack(Items.EMERALD, 1), 1, 50, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 23), new ItemStack(Items.LAPIS_LAZULI, 1), 1, 50, 1.2F)
        ));

        // ===== FARMER =====
        register(VillagerProfession.FARMER, 1, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.WHEAT, 26), new ItemStack(Items.EMERALD, 1), 2, 2, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.BEETROOT, 22), new ItemStack(Items.EMERALD, 1), 2, 2, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.POTATO, 46), new ItemStack(Items.EMERALD, 1), 2, 2, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.CARROT, 50), new ItemStack(Items.EMERALD, 1), 2, 2, 1.2F)
        ));
        register(VillagerProfession.FARMER, 2, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(Items.BREAD, 12), 2, 5, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(Items.PUMPKIN_PIE, 6), 2, 5, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.RED_MUSHROOM, 13), new ItemStack(Items.EMERALD, 1), 1, 5, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.BROWN_MUSHROOM, 13), new ItemStack(Items.EMERALD, 1), 1, 5, 1.2F)
        ));
        register(VillagerProfession.FARMER, 3, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.PUMPKIN, 30), new ItemStack(Items.EMERALD, 1), 2, 10, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.MELON, 28), new ItemStack(Items.EMERALD, 1), 2, 10, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(Items.APPLE, 6), 1, 10, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), enchantedIronHoe, 1, 10, 1.0F)
        ));
        register(VillagerProfession.FARMER, 4, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(Items.CAKE, 1), 1, 20, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD_BLOCK, 6), new ItemStack(Items.DIAMOND_HOE, 1), 1, 20, 1.0F)
        ));
        register(VillagerProfession.FARMER, 5, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 4), new ItemStack(Items.GOLDEN_CARROT, 3), 1, 50, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 4), new ItemStack(Items.GLISTERING_MELON_SLICE, 2), 1, 50, 1.0F)
        ));

        // ===== FISHERMAN =====
        register(VillagerProfession.FISHERMAN, 1, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.STRING, 21), new ItemStack(Items.EMERALD, 1), 2, 2, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.CLAY, 20), new ItemStack(Items.EMERALD, 1), 2, 2, 1.2F)
        ));
        register(VillagerProfession.FISHERMAN, 2, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.COD, 16), new ItemStack(Items.EMERALD, 1), 2, 5, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.SALMON, 14), new ItemStack(Items.EMERALD, 1), 2, 5, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(Items.COOKED_COD, 10), 1, 5, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(Items.COOKED_SALMON, 10), 1, 5, 1.0F)
        ));
        register(VillagerProfession.FISHERMAN, 3, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(Items.FISHING_ROD, 1), 1, 10, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 12), new ItemStack(ModItems.IRON_FISHING_HOOK, 1), 1, 10, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 12), new ItemStack(ModItems.IRON_LACED_FISHING_LINE, 1), 1, 10, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 12), new ItemStack(ModItems.IRON_SINKER, 1), 1, 10, 1.2F)
        ));
        register(VillagerProfession.FISHERMAN, 4, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.PUFFERFISH, 10), new ItemStack(Items.EMERALD, 1), 2, 20, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 4), waterBreatherPot, 1, 20, 1.0F)
        ));
        register(VillagerProfession.FISHERMAN, 5, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 22), new ItemStack(ModItems.AQUATIC_FIBER, 1), 1, 50, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 64), new ItemStack(FabricModItems.CACHE, 1), 1, 50, 1.0F)
        ));

        // ===== FLETCHER =====
        register(VillagerProfession.FLETCHER, 1, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.GRAVEL, 50), new ItemStack(Items.EMERALD, 1), 2, 2, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.FEATHER, 8), new ItemStack(Items.EMERALD, 1), 2, 2, 1.2F)
        ));
        register(VillagerProfession.FLETCHER, 2, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.FLINT, 14), new ItemStack(Items.EMERALD, 1), 2, 5, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.STRING, 21), new ItemStack(Items.EMERALD, 1), 2, 5, 1.2F)
        ));
        register(VillagerProfession.FLETCHER, 3, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 3), new ItemStack(Items.CROSSBOW, 1), 1, 10, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 3), new ItemStack(Items.BOW, 1), 1, 10, 1.2F)
        ));
        register(VillagerProfession.FLETCHER, 4, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.TRIPWIRE_HOOK, 16), new ItemStack(Items.EMERALD, 1), 2, 20, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 8), enchantedCrossbow, 1, 20, 1.2F)
        ));
        register(VillagerProfession.FLETCHER, 5, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 6), tippedArrowRandom, 2, 50, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 12), enchantedBow, 1, 50, 1.2F)
        ));

        // ===== LEATHERWORKER =====
        register(VillagerProfession.LEATHERWORKER, 1, List.of(
                villager -> new MerchantOffer(new ItemCost(ModItems.RAW_HIDE, 8), new ItemStack(Items.EMERALD, 1), 2, 2, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.RABBIT_HIDE, 2), new ItemStack(Items.EMERALD, 1), 2, 2, 1.2F)
        ));
        register(VillagerProfession.LEATHERWORKER, 2, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(Items.BUCKET, 2), 1, 10, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 2), new ItemStack(Items.CAULDRON, 1), 1, 10, 1.0F)
        ));
        register(VillagerProfession.LEATHERWORKER, 3, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(Items.LEATHER_HELMET, 1), 1, 5, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(Items.LEATHER_CHESTPLATE, 1), 1, 5, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(Items.LEATHER_LEGGINGS, 1), 1, 5, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(Items.LEATHER_BOOTS, 1), 1, 5, 1.0F)
        ));
        register(VillagerProfession.LEATHERWORKER, 4, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(Items.LEATHER_HORSE_ARMOR, 1), 1, 20, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(ModItems.LEATHER_FABRIC, 4), 2, 20, 1.0F)
        ));
        register(VillagerProfession.LEATHERWORKER, 5, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(Items.ITEM_FRAME, 8), 2, 50, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 4), new ItemStack(Items.SADDLE, 1), 1, 50, 1.0F)
        ));

        // ===== LIBRARIAN (includes a two-cost example) =====
        register(VillagerProfession.LIBRARIAN, 1, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.PAPER, 36), new ItemStack(Items.EMERALD, 1), 2, 2, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.BOOK, 8), new ItemStack(Items.EMERALD, 1), 2, 2, 1.2F)
        ));
        register(VillagerProfession.LIBRARIAN, 2, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 3), new ItemStack(Items.BOOKSHELF, 1), 2, 5, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(Items.GLASS, 16), 2, 5, 1.0F)
        ));
        register(VillagerProfession.LIBRARIAN, 3, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.INK_SAC, 12), new ItemStack(Items.EMERALD, 1), 2, 10, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.LANTERN, 4), new ItemStack(Items.EMERALD, 1), 2, 10, 1.0F)
        ));
        register(VillagerProfession.LIBRARIAN, 4, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.CLOCK, 1), new ItemStack(Items.EMERALD, 1), 2, 20, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 24), new ItemStack(Items.ENCHANTING_TABLE, 1), 2, 20, 1.2F)
        ));
        register(VillagerProfession.LIBRARIAN, 5, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 10), new ItemStack(Items.NAME_TAG, 1), 2, 50, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(Items.ENCHANTED_BOOK, 1), 1, 50, 1.2F)
        ));

        // ===== MASON =====
        register(VillagerProfession.MASON, 1, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.STONE, 50), new ItemStack(Items.EMERALD, 1), 2, 2, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.DEEPSLATE, 42), new ItemStack(Items.EMERALD, 1), 2, 2, 1.2F)
        ));
        register(VillagerProfession.MASON, 2, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.GRANITE, 32), new ItemStack(Items.EMERALD, 1), 2, 5, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.ANDESITE, 32), new ItemStack(Items.EMERALD, 1), 2, 5, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.DIORITE, 32), new ItemStack(Items.EMERALD, 1), 2, 5, 1.2F),
                villager -> new MerchantOffer(new ItemCost(ModBlocks.RHYOLITE.asItem(), 16), new ItemStack(Items.EMERALD, 1), 2, 5, 1.2F)
        ));
        register(VillagerProfession.MASON, 3, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.INK_SAC, 22), new ItemStack(Items.EMERALD, 1), 2, 10, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(Items.BRICKS, 16), 2, 10, 1.0F)
        ));
        register(VillagerProfession.MASON, 4, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.QUARTZ, 12), new ItemStack(Items.EMERALD, 1), 2, 20, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(Items.DRIPSTONE_BLOCK, 16), 2, 20, 1.0F)
        ));
        register(VillagerProfession.MASON, 5, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(Items.QUARTZ_BLOCK, 16), 1, 50, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(Items.OBSIDIAN, 1), 1, 50, 1.0F)
        ));

        // ===== SHEPHERD =====
        register(VillagerProfession.SHEPHERD, 1, List.of(
                villager -> new MerchantOffer(new ItemCost(woolRandomAsItem, 20), new ItemStack(Items.EMERALD, 1), 2, 2, 1.2F),
                villager -> new MerchantOffer(new ItemCost(dyeRandomAsItem, 32), new ItemStack(Items.EMERALD, 1), 2, 2, 1.2F)
        ));
        register(VillagerProfession.SHEPHERD, 2, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(Items.STRING, 12), 1, 5, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(Items.SHEARS, 1), 1, 5, 1.0F)
        ));
        register(VillagerProfession.SHEPHERD, 3, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 3), woolRandom, 2, 10, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 3), carpetRandom, 2, 10, 1.0F)
        ));
        register(VillagerProfession.SHEPHERD, 4, List.of(
                villager -> new MerchantOffer(new ItemCost(clothRandomAsItem, 8), new ItemStack(Items.EMERALD, 1), 2, 20, 1.2F),
                villager -> new MerchantOffer(new ItemCost(ModItems.HOLLOW_TWINE, 1), new ItemStack(Items.EMERALD, 1), 2, 20, 1.0F)
        ));
        register(VillagerProfession.SHEPHERD, 5, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(Items.PAINTING, 12), 1, 50, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 3), new ItemStack(ModItems.DREAMCATCHER, 1), 1, 50, 1.0F)
        ));

        // ===== TOOLSMITH =====
        register(VillagerProfession.TOOLSMITH, 1, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.RAW_IRON, 8), new ItemStack(Items.EMERALD, 1), 2, 2, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.RAW_GOLD, 8), new ItemStack(Items.EMERALD, 1), 2, 2, 1.2F)
        ));
        register(VillagerProfession.TOOLSMITH, 2, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(Items.IRON_INGOT, 2), 2, 5, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(Items.GOLD_INGOT, 2), 2, 5, 1.2F)
        ));
        register(VillagerProfession.TOOLSMITH, 3, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 16), enchantedIronPickaxe, 1, 10, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 16), enchantedIronShovel, 1, 10, 1.0F)
        ));
        register(VillagerProfession.TOOLSMITH, 4, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.DIAMOND, 1), new ItemStack(Items.EMERALD, 10), 2, 20, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 3), new ItemStack(Items.BELL, 1), 1, 20, 1.0F)
        ));
        register(VillagerProfession.TOOLSMITH, 5, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD_BLOCK, 9), new ItemStack(Items.DIAMOND_PICKAXE, 1), 1, 50, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD_BLOCK, 3), new ItemStack(Items.DIAMOND_SHOVEL, 1), 1, 50, 1.0F)
        ));

        // ===== WEAPONSMITH =====
        register(VillagerProfession.WEAPONSMITH, 1, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.IRON_INGOT, 4), new ItemStack(Items.EMERALD, 1), 2, 2, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.COPPER_INGOT, 12), new ItemStack(Items.EMERALD, 1), 2, 2, 1.2F)
        ));
        register(VillagerProfession.WEAPONSMITH, 2, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 1), new ItemStack(ModItems.FLARE_GUN, 1), 1, 5, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.GUNPOWDER, 14), new ItemStack(Items.EMERALD, 1), 1, 5, 1.2F)
        ));
        register(VillagerProfession.WEAPONSMITH, 3, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 16), enchantedIronSword, 1, 10, 1.2F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 16), enchantedIronAxe, 1, 10, 1.2F)
        ));
        register(VillagerProfession.WEAPONSMITH, 4, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 6), new ItemStack(Items.GOAT_HORN, 1), 2, 20, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD, 10), new ItemStack(Items.ANVIL, 1), 2, 20, 1.0F)
        ));
        register(VillagerProfession.WEAPONSMITH, 5, List.of(
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD_BLOCK, 6), new ItemStack(Items.DIAMOND_SWORD, 1), 1, 50, 1.0F),
                villager -> new MerchantOffer(new ItemCost(Items.EMERALD_BLOCK, 9), new ItemStack(Items.DIAMOND_AXE, 1), 1, 50, 1.0F)
        ));
    }

    private static void register(ResourceKey<VillagerProfession> professionKey, int masteryLevel, List<Function<Villager, MerchantOffer>> offers) {
        TRADE_POOLS.computeIfAbsent(professionKey, k -> new HashMap<>()).put(masteryLevel, offers);
    }

    public static void rotateTrades(Villager villager, int masteryLevel, RandomSource random) {
        ResourceKey<VillagerProfession> profKey = villager.getVillagerData().profession().unwrapKey().orElse(null);
        if (profKey == null) return;

        RegistryAccess registryAccess = villager.level().registryAccess();
        enchantedIronAxe = EnchantmentHelper.enchantItem(random, new ItemStack(Items.IRON_AXE), 1, registryAccess, registryAccess.lookupOrThrow(Registries.ENCHANTMENT).get(EnchantmentTags.ON_TRADED_EQUIPMENT));
        enchantedIronHoe = EnchantmentHelper.enchantItem(random, new ItemStack(Items.IRON_HOE), 1, registryAccess, registryAccess.lookupOrThrow(Registries.ENCHANTMENT).get(EnchantmentTags.ON_TRADED_EQUIPMENT));
        enchantedIronPickaxe = EnchantmentHelper.enchantItem(random, new ItemStack(Items.IRON_PICKAXE), 1, registryAccess, registryAccess.lookupOrThrow(Registries.ENCHANTMENT).get(EnchantmentTags.ON_TRADED_EQUIPMENT));
        enchantedIronShovel = EnchantmentHelper.enchantItem(random, new ItemStack(Items.IRON_SHOVEL), 1, registryAccess, registryAccess.lookupOrThrow(Registries.ENCHANTMENT).get(EnchantmentTags.ON_TRADED_EQUIPMENT));
        enchantedIronSword = EnchantmentHelper.enchantItem(random, new ItemStack(Items.IRON_SWORD), 1, registryAccess, registryAccess.lookupOrThrow(Registries.ENCHANTMENT).get(EnchantmentTags.ON_TRADED_EQUIPMENT));
        enchantedCrossbow = EnchantmentHelper.enchantItem(random, new ItemStack(Items.CROSSBOW), 1, registryAccess, registryAccess.lookupOrThrow(Registries.ENCHANTMENT).get(EnchantmentTags.ON_TRADED_EQUIPMENT));
        enchantedBow = EnchantmentHelper.enchantItem(random, new ItemStack(Items.BOW), 1, registryAccess, registryAccess.lookupOrThrow(Registries.ENCHANTMENT).get(EnchantmentTags.ON_TRADED_EQUIPMENT));

        waterBreatherPot = new ItemStack(Items.POTION, 1);
        tippedArrowRandom = new ItemStack(Items.TIPPED_ARROW, 8);
        if (waterBreatherPot.get(DataComponents.POTION_CONTENTS) != null) {
            waterBreatherPot.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER_BREATHING));
        }
        if (tippedArrowRandom.get(DataComponents.POTION_CONTENTS) != null) {
            List<Holder<Potion>> allPotions = getAllPotions(registryAccess);
            if (!allPotions.isEmpty()) {
                Holder<Potion> chosen = allPotions.get(random.nextInt(allPotions.size()));
                tippedArrowRandom.set(DataComponents.POTION_CONTENTS, new PotionContents(chosen));
            }
        }

        List<Holder<Item>> bannerTag = StreamSupport.stream(villager.level().registryAccess().lookupOrThrow(Registries.ITEM).getTagOrEmpty(ModTags.BANNERS).spliterator(), false).toList();
        if (!bannerTag.isEmpty()) {
            bannerRandom = new ItemStack(bannerTag.get(villager.level().random.nextInt(bannerTag.size())).value());
            bannerRandomAsItem = bannerRandom.getItem();
        }
        List<Holder<Item>> carpetTag = StreamSupport.stream(villager.level().registryAccess().lookupOrThrow(Registries.ITEM).getTagOrEmpty(ModTags.CARPETS).spliterator(), false).toList();
        if (!carpetTag.isEmpty()) {
            carpetRandom = new ItemStack(carpetTag.get(villager.level().random.nextInt(carpetTag.size())).value());
            carpetRandomAsItem = carpetRandom.getItem();
        }
        List<Holder<Item>> clothTag = StreamSupport.stream(villager.level().registryAccess().lookupOrThrow(Registries.ITEM).getTagOrEmpty(ModTags.CLOTH).spliterator(), false).toList();
        if (!clothTag.isEmpty()) {
            clothRandom = new ItemStack(clothTag.get(villager.level().random.nextInt(clothTag.size())).value());
            clothRandomAsItem = clothRandom.getItem();
        }
        List<Holder<Item>> dyeTag = StreamSupport.stream(villager.level().registryAccess().lookupOrThrow(Registries.ITEM).getTagOrEmpty(ModTags.DYES).spliterator(), false).toList();
        if (!dyeTag.isEmpty()) {
            dyeRandom = new ItemStack(dyeTag.get(villager.level().random.nextInt(dyeTag.size())).value());
            dyeRandomAsItem = dyeRandom.getItem();
        }
        List<Holder<Item>> woolTag = StreamSupport.stream(villager.level().registryAccess().lookupOrThrow(Registries.ITEM).getTagOrEmpty(ItemTags.WOOL).spliterator(), false).toList();
        if (!woolTag.isEmpty()) {
            woolRandom = new ItemStack(woolTag.get(villager.level().random.nextInt(woolTag.size())).value());
            woolRandomAsItem = woolRandom.getItem();
        }


        Map<Integer, List<Function<Villager, MerchantOffer>>> byLevel = TRADE_POOLS.get(profKey);
        if (byLevel == null) return;

        List<Function<Villager, MerchantOffer>> pool = new ArrayList<>();
        for (int lvl = 1; lvl <= masteryLevel; lvl++) {
            List<Function<Villager, MerchantOffer>> tierPool = byLevel.get(lvl);
            if (tierPool != null) {
                pool.addAll(tierPool);
            }
        }
        if (pool.isEmpty()) return;

        Collections.shuffle(pool, new Random(random.nextLong()));
        int slots = Math.min(masteryLevel + 1, pool.size());

        villager.getOffers().clear();
        for (int i = 0; i < slots; i++) {
            MerchantOffer offer = pool.get(i).apply(villager);
            if (offer != null) {
                villager.getOffers().add(offer);
            }
        }
    }
}
