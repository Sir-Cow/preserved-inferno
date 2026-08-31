package sircow.preservedinferno.trigger;

import net.minecraft.advancements.triggers.CriterionTrigger;
import net.minecraft.resources.Identifier;
import sircow.preservedinferno.Constants;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModTriggers {
    private static final Map<Identifier, CriterionTrigger<?>> TRIGGERS = new LinkedHashMap<>();

    public static final CustomTrigger ARMOR_VALUE = register("armor_value", new CustomTrigger());
    public static final CustomTrigger BREAK_GROWN_CROP = register("break_grown_crop", new CustomTrigger());
    public static final CustomTrigger BREAK_CREAKING_HEART = register("break_creaking_heart", new CustomTrigger());
    public static final CustomTrigger BREAK_SCULK_SHRIEKER = register("break_sculk_shrieker", new CustomTrigger());
    public static final CustomTrigger BREAK_SPARKLING_BLACKSTONE = register("break_sparkling_blackstone", new CustomTrigger());
    public static final CustomTrigger BRUSH_BLOCK = register("brush_block", new CustomTrigger());
    public static final CustomTrigger CONDUIT_POWER = register("conduit_power", new CustomTrigger());
    public static final CustomTrigger CONDUIT_POWER_FULL = register("conduit_power_full", new CustomTrigger());
    public static final CustomTrigger CONSUME_SCULK_INFUSION = register("consume_sculk_infusion", new CustomTrigger());
    public static final CustomTrigger CRAFT_ARROWS = register("craft_arrows", new CustomTrigger());
    public static final CustomTrigger CRIT_DAMAGE = register("crit_damage", new CustomTrigger());
    public static final CustomTrigger DRINK_WATER = register("reduce_heat", new CustomTrigger());
    public static final CustomTrigger EAT_CAKE_FAST = register("eat_cake_fast", new CustomTrigger());
    public static final CustomTrigger FISH_ON_NAUTILUS = register("fish_on_nautilus", new CustomTrigger());
    public static final CustomTrigger FREEZE_COOL = register("freeze_cool", new CustomTrigger());
    public static final CustomTrigger HAPPY_GHAST_BUILD_HEIGHT = register("happy_ghast_build_height", new CustomTrigger());
    public static final CustomTrigger MASTERY_BEGINNER = register("mastery_beginner", new CustomTrigger());
    public static final CustomTrigger MASTERY_CENTURION = register("mastery_centurion", new CustomTrigger());
    public static final CustomTrigger MASTERY_CHAMPION = register("mastery_champion", new CustomTrigger());
    public static final CustomTrigger MASTERY_DISCIPLE = register("mastery_disciple", new CustomTrigger());
    public static final CustomTrigger MASTERY_INFERNAL = register("mastery_infernal", new CustomTrigger());
    public static final CustomTrigger MASTERY_KNIGHT = register("mastery_knight", new CustomTrigger());
    public static final CustomTrigger MASTERY_MASTER = register("mastery_master", new CustomTrigger());
    public static final CustomTrigger MASTERY_NOVICE = register("mastery_novice", new CustomTrigger());
    public static final CustomTrigger MASTERY_SQUIRE = register("mastery_squire", new CustomTrigger());
    public static final CustomTrigger MASTERY_STARTER = register("mastery_starter", new CustomTrigger());
    public static final CustomTrigger MAX_ENCHANTING_TABLE = register("max_enchanting_table", new CustomTrigger());
    public static final CustomTrigger MAX_HORSE_STATS = register("max_horse_stats", new CustomTrigger());
    public static final CustomTrigger MAX_VILLAGER = register("max_villager", new CustomTrigger());
    public static final CustomTrigger OPENED_ADVANCEMENT_SCREEN = register("open_advancement_menu", new CustomTrigger());
    public static final CustomTrigger PLACE_BOOKSHELF = register("place_bookshelf", new CustomTrigger());
    public static final CustomTrigger RIDE_MINECART = register("ride_minecart", new CustomTrigger());
    public static final CustomTrigger RIDE_MINECART_MAX_SPEED = register("ride_minecart_max_speed", new CustomTrigger());
    public static final CustomTrigger RIDE_MINECART_FAR = register("ride_minecart_far", new CustomTrigger());
    public static final CustomTrigger SCRAPE_COPPER = register("scrape_copper", new CustomTrigger());
    public static final CustomTrigger SHEAR_SHEEP = register("shear_sheep", new CustomTrigger());
    public static final CustomTrigger SMELT_LEATHER_FABRIC = register("smelt_leather_fabric", new CustomTrigger());
    public static final CustomTrigger STAND_ON_ICE = register("stand_on_ice", new CustomTrigger());
    public static final CustomTrigger TRADE_EVERY_VILLAGER = register("trade_every_villager", new CustomTrigger());
    public static final CustomTrigger TRIPLE_KILL = register("triple_kill", new CustomTrigger());
    public static final CustomTrigger UPGRADE_HORSE_ATTRIBUTE = register("upgrade_horse_attribute", new CustomTrigger());
    public static final CustomTrigger UPGRADE_HORSE_HEALTH = register("upgrade_horse_health", new CustomTrigger());
    public static final CustomTrigger USE_REVERB_COMPASS = register("use_reverb_compass", new CustomTrigger());
    public static final CustomTrigger USE_TIPPED_ARROW = register("use_tipped_arrow", new CustomTrigger());
    public static final CustomTrigger USED_ANVIL_REPAIR = register("used_anvil_repair", new CustomTrigger());
    public static final CustomTrigger USED_FORGE_DUST = register("used_forge_dust", new CustomTrigger());
    public static final CustomTrigger VILLAGER_RESTOCK = register("villager_restock", new CustomTrigger());
    public static final CustomTrigger WOOL_FROM_LOOM = register("wool_from_loom", new CustomTrigger());
    public static final CustomTrigger WORLD_JOIN = register("world_join", new CustomTrigger());

    private static <T extends CriterionTrigger<?>> T register(String name, T trigger) {
        TRIGGERS.put(Constants.id(name), trigger);
        return trigger;
    }

    public static Map<Identifier, CriterionTrigger<?>> getTriggers() {
        return TRIGGERS;
    }
}
