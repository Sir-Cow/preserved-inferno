package sircow.preservedinferno.trigger;

import net.minecraft.advancements.CriterionTrigger;
import sircow.preservedinferno.trigger.custom.*;

import java.util.function.Supplier;

public class ModTriggers {
    public static final TriggerEntry<CustomTrigger> ARMOR_VALUE = new TriggerEntry<>("armor_value", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> BREAK_GROWN_CROP = new TriggerEntry<>("break_grown_crop", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> BREAK_CREAKING_HEART = new TriggerEntry<>("break_creaking_heart", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> BREAK_SCULK_SHRIEKER = new TriggerEntry<>("break_sculk_shrieker", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> BREAK_SPARKLING_BLACKSTONE = new TriggerEntry<>("break_sparkling_blackstone", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> BRUSH_BLOCK = new TriggerEntry<>("brush_block", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> CONDUIT_POWER = new TriggerEntry<>("conduit_power", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> CONDUIT_POWER_FULL = new TriggerEntry<>("conduit_power_full", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> CONSUME_SCULK_INFUSION = new TriggerEntry<>("consume_sculk_infusion", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> CRAFT_ARROWS = new TriggerEntry<>("craft_arrows", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> CRIT_DAMAGE = new TriggerEntry<>("crit_damage", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> DRINK_WATER = new TriggerEntry<>("reduce_heat", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> EAT_CAKE_FAST = new TriggerEntry<>("eat_cake_fast", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> FISH_ON_NAUTILUS = new TriggerEntry<>("fish_on_nautilus", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> FISH_TREASURE = new TriggerEntry<>("fish_treasure", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> FREEZE_COOL = new TriggerEntry<>("freeze_cool", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> HAPPY_GHAST_BUILD_HEIGHT = new TriggerEntry<>("happy_ghast_build_height", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> MASTERY_BEGINNER = new TriggerEntry<>("mastery_beginner", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> MASTERY_CENTURION = new TriggerEntry<>("mastery_centurion", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> MASTERY_CHAMPION = new TriggerEntry<>("mastery_champion", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> MASTERY_DISCIPLE = new TriggerEntry<>("mastery_disciple", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> MASTERY_INFERNAL = new TriggerEntry<>("mastery_infernal", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> MASTERY_KNIGHT = new TriggerEntry<>("mastery_knight", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> MASTERY_MASTER = new TriggerEntry<>("mastery_master", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> MASTERY_NOVICE = new TriggerEntry<>("mastery_novice", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> MASTERY_SQUIRE = new TriggerEntry<>("mastery_squire", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> MASTERY_STARTER = new TriggerEntry<>("mastery_starter", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> MAX_ENCHANTING_TABLE = new TriggerEntry<>("max_enchanting_table", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> MAX_VILLAGER = new TriggerEntry<>("max_villager", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> OPENED_ADVANCEMENT_SCREEN = new TriggerEntry<>("open_advancement_menu", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> PLACE_BOOKSHELF = new TriggerEntry<>("place_bookshelf", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> RIDE_MINECART = new TriggerEntry<>("ride_minecart", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> RIDE_MINECART_MAX_SPEED = new TriggerEntry<>("ride_minecart_max_speed", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> RIDE_MINECART_FAR = new TriggerEntry<>("ride_minecart_far", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> SCRAPE_COPPER = new TriggerEntry<>("scrape_copper", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> SHEAR_SHEEP = new TriggerEntry<>("shear_sheep", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> SMELT_LEATHER_FABRIC = new TriggerEntry<>("smelt_leather_fabric", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> STAND_ON_ICE = new TriggerEntry<>("stand_on_ice", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> TRADE_EVERY_VILLAGER = new TriggerEntry<>("trade_every_villager", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> TRIPLE_KILL = new TriggerEntry<>("triple_kill", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> USE_REVERB_COMPASS = new TriggerEntry<>("use_reverb_compass", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> USE_TIPPED_ARROW = new TriggerEntry<>("use_tipped_arrow", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> USED_ANVIL_REPAIR = new TriggerEntry<>("used_anvil_repair", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> USED_FORGE_DUST = new TriggerEntry<>("used_forge_dust", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> VILLAGER_RESTOCK = new TriggerEntry<>("villager_restock", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> WOOL_FROM_LOOM = new TriggerEntry<>("wool_from_loom", CustomTrigger::new);
    public static final TriggerEntry<CustomTrigger> WORLD_JOIN = new TriggerEntry<>("world_join", CustomTrigger::new);

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
