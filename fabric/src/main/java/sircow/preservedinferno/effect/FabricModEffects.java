package sircow.preservedinferno.effect;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import sircow.preservedinferno.Constants;

public class FabricModEffects {
    public static void registerFabricModEffects() {
        register(ModEffects.HINDERED);
        register(ModEffects.PINFERNO_CONDUIT_POWER);
        register(ModEffects.WELL_RESTED);
    }

    private static void register(ModEffects.EffectEntry entry) {
        entry.holder = Registry.registerForHolder(
                BuiltInRegistries.MOB_EFFECT,
                Constants.id(entry.id),
                entry.factory.get()
        );
    }
}
