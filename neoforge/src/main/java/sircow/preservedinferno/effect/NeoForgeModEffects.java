package sircow.preservedinferno.effect;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import sircow.preservedinferno.Constants;

import java.util.HashMap;
import java.util.Map;

public class NeoForgeModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(
            BuiltInRegistries.MOB_EFFECT,
            Constants.MOD_ID
    );

    private static final Map<ModEffects.EffectEntry, Holder<MobEffect>> registryMap = new HashMap<>();

    public static void init(IEventBus modEventBus) {
        registerNeoForgeModEffects();
        EFFECTS.register(modEventBus);
        modEventBus.addListener(NeoForgeModEffects::commonSetup);
    }

    public static void registerNeoForgeModEffects() {
        for (ModEffects.EffectEntry effect : ModEffects.ALL_EFFECTS) {
            registryMap.put(effect, EFFECTS.register(effect.id, effect.factory));
        }
    }

    private static void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(NeoForgeModEffects::assignToCommon);
    }

    public static void assignToCommon() {
        for (Map.Entry<ModEffects.EffectEntry, Holder<MobEffect>> entry : registryMap.entrySet()) {
            entry.getKey().holder = entry.getValue();
        }
    }
}
