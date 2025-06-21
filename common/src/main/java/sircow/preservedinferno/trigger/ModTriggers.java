package sircow.preservedinferno.trigger;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.trigger.custom.OpenAdvancementsTrigger;

public class ModTriggers {
    public static final OpenAdvancementsTrigger OPENED_ADVANCEMENT_SCREEN = register("open_advancement_menu", new OpenAdvancementsTrigger());

    public static <T extends CriterionTrigger<?>> T register(String name, T trigger) {
        return Registry.register(BuiltInRegistries.TRIGGER_TYPES, Constants.id(name), trigger);
    }

    public static void registerTriggers() {
        Constants.LOG.info("Registering Mod Triggers for " + Constants.MOD_ID);
    }
}
