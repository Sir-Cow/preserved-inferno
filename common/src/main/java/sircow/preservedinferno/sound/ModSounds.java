package sircow.preservedinferno.sound;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import sircow.preservedinferno.Constants;

public class ModSounds {
    // sounds
    public static final SoundEvent ENCHANT_CLICK = register("enchant_click");
    public static final SoundEvent ENCHANT = register("enchant");
    public static final SoundEvent ENCHANT_CLOSE = register("enchant_close");
    public static final SoundEvent SHIELD_COOLDOWN = register("shield_cooldown");
    public static final SoundEvent HEAT_UP = register("heat_up");
    public static final SoundEvent CAULDRON_BUBBLE = register("cauldron_bubble");
    public static final SoundEvent CACHE_OPEN = register("cache_open");
    public static final SoundEvent CACHE_CLOSE = register("cache_close");

    private static SoundEvent register(String name) {
        return register(Constants.id(name));
    }

    private static SoundEvent register(ResourceLocation name) {
        return register(name, name);
    }

    private static SoundEvent register(ResourceLocation name, ResourceLocation location) {
        return Registry.register(BuiltInRegistries.SOUND_EVENT, name, SoundEvent.createVariableRangeEvent(location));
    }

    public static void registerSounds() {
        Constants.LOG.info("Registering Mod Sounds for " + Constants.MOD_ID);
    }
}
