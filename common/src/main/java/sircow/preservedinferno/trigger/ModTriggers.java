package sircow.preservedinferno.trigger;

import net.minecraft.advancements.CriterionTrigger;
import sircow.preservedinferno.trigger.custom.*;

import java.util.function.Supplier;

public class ModTriggers {
    public static final TriggerEntry<ArmorValueTrigger> ARMOR_VALUE = new TriggerEntry<>("armor_value", ArmorValueTrigger::new);
    public static final TriggerEntry<BreakGrownCropTrigger> BREAK_GROWN_CROP = new TriggerEntry<>("break_grown_crop", BreakGrownCropTrigger::new);
    public static final TriggerEntry<BreakSculkShriekerTrigger> BREAK_SCULK_SHRIEKER = new TriggerEntry<>("break_sculk_shrieker", BreakSculkShriekerTrigger::new);
    public static final TriggerEntry<BrushBlockTrigger> BRUSH_BLOCK = new TriggerEntry<>("brush_block", BrushBlockTrigger::new);
    public static final TriggerEntry<ConduitPowerTrigger> CONDUIT_POWER = new TriggerEntry<>("conduit_power", ConduitPowerTrigger::new);
    public static final TriggerEntry<ConduitFullPowerTrigger> CONDUIT_POWER_FULL = new TriggerEntry<>("conduit_power_full", ConduitFullPowerTrigger::new);
    public static final TriggerEntry<ConsumeSculkInfusionTrigger> CONSUME_SCULK_INFUSION = new TriggerEntry<>("consume_sculk_infusion", ConsumeSculkInfusionTrigger::new);
    public static final TriggerEntry<CritDamageTrigger> CRIT_DAMAGE = new TriggerEntry<>("crit_damage", CritDamageTrigger::new);
    public static final TriggerEntry<DrinkWaterTrigger> DRINK_WATER = new TriggerEntry<>("reduce_heat", DrinkWaterTrigger::new);
    public static final TriggerEntry<FishTreasureTrigger> FISH_TREASURE = new TriggerEntry<>("fish_treasure", FishTreasureTrigger::new);
    public static final TriggerEntry<FreezeCoolTrigger> FREEZE_COOL = new TriggerEntry<>("freeze_cool", FreezeCoolTrigger::new);
    public static final TriggerEntry<MasteryAdequateTrigger> MASTERY_ADEQUATE = new TriggerEntry<>("mastery_adequate", MasteryAdequateTrigger::new);
    public static final TriggerEntry<MasteryAdvancedTrigger> MASTERY_ADVANCED = new TriggerEntry<>("mastery_advanced", MasteryAdvancedTrigger::new);
    public static final TriggerEntry<MasteryBeginnerTrigger> MASTERY_BEGINNER = new TriggerEntry<>("mastery_beginner", MasteryBeginnerTrigger::new);
    public static final TriggerEntry<MasteryChampionTrigger> MASTERY_CHAMPION = new TriggerEntry<>("mastery_champion", MasteryChampionTrigger::new);
    public static final TriggerEntry<MasteryDiscipleTrigger> MASTERY_DISCIPLE = new TriggerEntry<>("mastery_disciple", MasteryDiscipleTrigger::new);
    public static final TriggerEntry<MasteryInfernalTrigger> MASTERY_INFERNAL = new TriggerEntry<>("mastery_infernal", MasteryInfernalTrigger::new);
    public static final TriggerEntry<MasteryMasterTrigger> MASTERY_MASTER = new TriggerEntry<>("mastery_master", MasteryMasterTrigger::new);
    public static final TriggerEntry<MasteryNoviceTrigger> MASTERY_NOVICE = new TriggerEntry<>("mastery_novice", MasteryNoviceTrigger::new);
    public static final TriggerEntry<MasteryStarterTrigger> MASTERY_STARTER = new TriggerEntry<>("mastery_starter", MasteryStarterTrigger::new);
    public static final TriggerEntry<MaxEnchantingTableTrigger> MAX_ENCHANTING_TABLE = new TriggerEntry<>("max_enchanting_table", MaxEnchantingTableTrigger::new);
    public static final TriggerEntry<MaxVillagerTrigger> MAX_VILLAGER = new TriggerEntry<>("max_villager", MaxVillagerTrigger::new);
    public static final TriggerEntry<OpenAdvancementsTrigger> OPENED_ADVANCEMENT_SCREEN = new TriggerEntry<>("open_advancement_menu", OpenAdvancementsTrigger::new);
    public static final TriggerEntry<PlaceBookshelfTrigger> PLACE_BOOKSHELF = new TriggerEntry<>("place_bookshelf", PlaceBookshelfTrigger::new);
    public static final TriggerEntry<RideMinecartTrigger> RIDE_MINECART = new TriggerEntry<>("ride_minecart", RideMinecartTrigger::new);
    public static final TriggerEntry<RideMinecartMaxSpeedTrigger> RIDE_MINECART_MAX_SPEED = new TriggerEntry<>("ride_minecart_max_speed", RideMinecartMaxSpeedTrigger::new);
    public static final TriggerEntry<RideMinecartFarTrigger> RIDE_MINECART_FAR = new TriggerEntry<>("ride_minecart_far", RideMinecartFarTrigger::new);
    public static final TriggerEntry<ScrapeCopperTrigger> SCRAPE_COPPER = new TriggerEntry<>("scrape_copper", ScrapeCopperTrigger::new);
    public static final TriggerEntry<ShearSheepTrigger> SHEAR_SHEEP = new TriggerEntry<>("shear_sheep", ShearSheepTrigger::new);
    public static final TriggerEntry<StandOnIceTrigger> STAND_ON_ICE = new TriggerEntry<>("stand_on_ice", StandOnIceTrigger::new);
    public static final TriggerEntry<TradeEveryVillagerTrigger> TRADE_EVERY_VILLAGER = new TriggerEntry<>("trade_every_villager", TradeEveryVillagerTrigger::new);
    public static final TriggerEntry<TripleKillTrigger> TRIPLE_KILL = new TriggerEntry<>("triple_kill", TripleKillTrigger::new);
    public static final TriggerEntry<UseAnvilRepairTrigger> USED_ANVIL_REPAIR = new TriggerEntry<>("used_anvil_repair", UseAnvilRepairTrigger::new);
    public static final TriggerEntry<VillagerRestockTrigger> VILLAGER_RESTOCK = new TriggerEntry<>("villager_restock", VillagerRestockTrigger::new);
    public static final TriggerEntry<WoolFromLoomTrigger> WOOL_FROM_LOOM = new TriggerEntry<>("wool_from_loom", WoolFromLoomTrigger::new);
    public static final TriggerEntry<WorldJoinTrigger> WORLD_JOIN = new TriggerEntry<>("world_join", WorldJoinTrigger::new);

    public static class TriggerEntry<T extends CriterionTrigger<?>> {
        public final String id;
        public final Supplier<T> factory;
        private Supplier<T> trigger;

        public TriggerEntry(String id, Supplier<T> factory) {
            this.id = id;
            this.factory = factory;
        }

        public void bind(Supplier<T> supplier) {
            this.trigger = supplier;
        }

        public T get() {
            return this.trigger.get();
        }
    }
}
