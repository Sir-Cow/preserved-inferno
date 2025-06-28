package sircow.preservedinferno.trigger;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.trigger.custom.*;

public class ModTriggers {
    public static final BedSpawnPointTrigger BED_SPAWN_POINT = register("bed_spawn_point", new BedSpawnPointTrigger());
    public static final ChannelingTrigger CHANNELING = register("channeling", new ChannelingTrigger());
    public static final DrinkWaterTrigger DRINK_WATER = register("reduce_heat", new DrinkWaterTrigger());
    public static final FishTreasureTrigger FISH_TREASURE = register("fish_treasure", new FishTreasureTrigger());
    public static final OpenAdvancementsTrigger OPENED_ADVANCEMENT_SCREEN = register("open_advancement_menu", new OpenAdvancementsTrigger());
    public static final PlaceBookshelfTrigger PLACE_BOOKSHELF = register("place_bookshelf", new PlaceBookshelfTrigger());
    public static final ShearSheepTrigger SHEAR_SHEEP = register("shear_sheep", new ShearSheepTrigger());
    public static final StandOnIceTrigger STAND_ON_ICE = register("stand_on_ice", new StandOnIceTrigger());
    public static final UseAnvilRepairTrigger USED_ANVIL_REPAIR = register("used_anvil_repair", new UseAnvilRepairTrigger());

    public static <T extends CriterionTrigger<?>> T register(String name, T trigger) {
        return Registry.register(BuiltInRegistries.TRIGGER_TYPES, Constants.id(name), trigger);
    }

    public static void registerTriggers() {
        Constants.LOG.info("Registering Mod Triggers for " + Constants.MOD_ID);
    }
}
