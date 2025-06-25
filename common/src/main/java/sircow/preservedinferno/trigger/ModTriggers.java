package sircow.preservedinferno.trigger;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.trigger.custom.DrinkWaterTrigger;
import sircow.preservedinferno.trigger.custom.OpenAdvancementsTrigger;
import sircow.preservedinferno.trigger.custom.StandOnIceTrigger;
import sircow.preservedinferno.trigger.custom.UseAnvilRepairTrigger;

public class ModTriggers {
    public static final DrinkWaterTrigger DRINK_WATER = register("reduce_heat", new DrinkWaterTrigger());
    public static final OpenAdvancementsTrigger OPENED_ADVANCEMENT_SCREEN = register("open_advancement_menu", new OpenAdvancementsTrigger());
    public static final StandOnIceTrigger STAND_ON_ICE = register("stand_on_ice", new StandOnIceTrigger());
    public static final UseAnvilRepairTrigger USED_ANVIL_REPAIR = register("used_anvil_repair", new UseAnvilRepairTrigger());

    public static <T extends CriterionTrigger<?>> T register(String name, T trigger) {
        return Registry.register(BuiltInRegistries.TRIGGER_TYPES, Constants.id(name), trigger);
    }

    public static void registerTriggers() {
        Constants.LOG.info("Registering Mod Triggers for " + Constants.MOD_ID);
    }
}
