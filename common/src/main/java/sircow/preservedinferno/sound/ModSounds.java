package sircow.preservedinferno.sound;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
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
    public static final SoundEvent SCULK_INFUSION = register("sculk_infusion");
    public static final SoundEvent SCULK_INFUSION1 = register("experience_orb_pickup_silent");
    public static final SoundEvent SCULK_INFUSION2 = register("sculk_catalyst_bloom_silent");
    public static final SoundEvent SCULK_INFUSION3 = register("splash_potion_break_silent");

    private static SoundEvent register(String name) {
        return register(Constants.id(name));
    }

    private static SoundEvent register(Identifier name) {
        return register(name, name);
    }

    private static SoundEvent register(Identifier name, Identifier location) {
        return Registry.register(BuiltInRegistries.SOUND_EVENT, name, SoundEvent.createVariableRangeEvent(location));
    }

    public static void registerSounds() {
        // Constants.LOG.info("Registering Mod Sounds for " + Constants.MOD_ID);
    }
}
