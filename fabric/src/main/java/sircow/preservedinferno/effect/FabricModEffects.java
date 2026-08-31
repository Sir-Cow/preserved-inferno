package sircow.preservedinferno.effect;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;

public class FabricModEffects {
    public static void registerFabricModEffects() {
        ModEffects.getEffects().forEach((id, effect) -> {
            Holder<MobEffect> holder = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, id, effect);
            ModEffects.getHolders().put(id, holder);
        });
    }
}
