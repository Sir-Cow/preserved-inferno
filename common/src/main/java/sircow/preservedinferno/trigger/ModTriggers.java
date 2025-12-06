package sircow.preservedinferno.trigger;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.trigger.custom.*;

public class ModTriggers {
    public static final ArmorValueTrigger ARMOR_VALUE = register("armor_value", new ArmorValueTrigger());
    public static final BreakGrownCropTrigger BREAK_GROWN_CROP = register("break_grown_crop", new BreakGrownCropTrigger());
    public static final BreakSculkShriekerTrigger BREAK_SCULK_SHRIEKER = register("break_sculk_shrieker", new BreakSculkShriekerTrigger());
    public static final BrushBlockTrigger BRUSH_BLOCK = register("brush_block", new BrushBlockTrigger());
    public static final ConduitPowerTrigger CONDUIT_POWER = register("conduit_power", new ConduitPowerTrigger());
    public static final ConduitFullPowerTrigger CONDUIT_POWER_FULL = register("conduit_power_full", new ConduitFullPowerTrigger());
    public static final ConsumeSculkInfusionTrigger CONSUME_SCULK_INFUSION = register("consume_sculk_infusion", new ConsumeSculkInfusionTrigger());
    public static final CritDamageTrigger CRIT_DAMAGE = register("crit_damage", new CritDamageTrigger());
    public static final DrinkWaterTrigger DRINK_WATER = register("reduce_heat", new DrinkWaterTrigger());
    public static final FishTreasureTrigger FISH_TREASURE = register("fish_treasure", new FishTreasureTrigger());
    public static final FreezeCoolTrigger FREEZE_COOL = register("freeze_cool", new FreezeCoolTrigger());
    public static final MasteryAdequateTrigger MASTERY_ADEQUATE = register("mastery_adequate", new MasteryAdequateTrigger());
    public static final MasteryAdvancedTrigger MASTERY_ADVANCED = register("mastery_advanced", new MasteryAdvancedTrigger());
    public static final MasteryBeginnerTrigger MASTERY_BEGINNER = register("mastery_beginner", new MasteryBeginnerTrigger());
    public static final MasteryChampionTrigger MASTERY_CHAMPION = register("mastery_champion", new MasteryChampionTrigger());
    public static final MasteryDiscipleTrigger MASTERY_DISCIPLE = register("mastery_disciple", new MasteryDiscipleTrigger());
    public static final MasteryInfernalTrigger MASTERY_INFERNAL = register("mastery_infernal", new MasteryInfernalTrigger());
    public static final MasteryMasterTrigger MASTERY_MASTER = register("mastery_master", new MasteryMasterTrigger());
    public static final MasteryNoviceTrigger MASTERY_NOVICE = register("mastery_novice", new MasteryNoviceTrigger());
    public static final MasteryStarterTrigger MASTERY_STARTER = register("mastery_starter", new MasteryStarterTrigger());
    public static final MaxEnchantingTableTrigger MAX_ENCHANTING_TABLE = register("max_enchanting_table", new MaxEnchantingTableTrigger());
    public static final MaxVillagerTrigger MAX_VILLAGER = register("max_villager", new MaxVillagerTrigger());
    public static final OpenAdvancementsTrigger OPENED_ADVANCEMENT_SCREEN = register("open_advancement_menu", new OpenAdvancementsTrigger());
    public static final PlaceBookshelfTrigger PLACE_BOOKSHELF = register("place_bookshelf", new PlaceBookshelfTrigger());
    public static final RideMinecartTrigger RIDE_MINECART = register("ride_minecart", new RideMinecartTrigger());
    public static final RideMinecartMaxSpeedTrigger RIDE_MINECART_MAX_SPEED = register("ride_minecart_max_speed", new RideMinecartMaxSpeedTrigger());
    public static final RideMinecartFarTrigger RIDE_MINECART_FAR = register("ride_minecart_far", new RideMinecartFarTrigger());
    public static final ScrapeCopperTrigger SCRAPE_COPPER = register("scrape_copper", new ScrapeCopperTrigger());
    public static final ShearSheepTrigger SHEAR_SHEEP = register("shear_sheep", new ShearSheepTrigger());
    public static final StandOnIceTrigger STAND_ON_ICE = register("stand_on_ice", new StandOnIceTrigger());
    public static final TradeEveryVillagerTrigger TRADE_EVERY_VILLAGER = register("trade_every_villager", new TradeEveryVillagerTrigger());
    public static final TripleKillTrigger TRIPLE_KILL = register("triple_kill", new TripleKillTrigger());
    public static final UseAnvilRepairTrigger USED_ANVIL_REPAIR = register("used_anvil_repair", new UseAnvilRepairTrigger());
    public static final VillagerRestockTrigger VILLAGER_RESTOCK = register("villager_restock", new VillagerRestockTrigger());
    public static final WoolFromLoomTrigger WOOL_FROM_LOOM = register("wool_from_loom", new WoolFromLoomTrigger());
    public static final WorldJoinTrigger WORLD_JOIN = register("world_join", new WorldJoinTrigger());

    public static <T extends CriterionTrigger<?>> T register(String name, T trigger) {
        return Registry.register(BuiltInRegistries.TRIGGER_TYPES, Constants.id(name), trigger);
    }

    public static void registerTriggers() {
        // Constants.LOG.info("Registering Mod Triggers for " + Constants.MOD_ID);
    }
}
