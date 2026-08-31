package sircow.preservedinferno.sound;

import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import sircow.preservedinferno.Constants;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModSounds {
    private static final Map<Identifier, SoundEvent> SOUNDS = new LinkedHashMap<>();

    public static final SoundEvent ANGLING_TABLE_USE = register("angling_table_use");
    public static final SoundEvent BOOM_BOX_LOAD = register("boom_box_load");
    public static final SoundEvent BOOM_BOX_PRIMED = register("boom_box_primed");
    public static final SoundEvent CACHE_OPEN = register("cache_open");
    public static final SoundEvent CACHE_CLOSE = register("cache_close");
    public static final SoundEvent CAULDRON_BUBBLE = register("cauldron_bubble");
    public static final SoundEvent DYNAMITE_THROW = register("dynamite_throw");
    public static final SoundEvent ENCHANT_CLICK = register("enchant_click");
    public static final SoundEvent ENCHANT = register("enchant");
    public static final SoundEvent ENCHANT_CLOSE = register("enchant_close");
    public static final SoundEvent HEAT_UP = register("heat_up");
    public static final SoundEvent REVERB_COMPASS_USE = register("reverb_compass_use");
    public static final SoundEvent REVERB_COMPASS_USE1 = register("enderpearl_land_silent");
    public static final SoundEvent REVERB_COMPASS_USE2 = register("sculk_catalyst_break_silent");
    public static final SoundEvent REVERB_COMPASS_USE3 = register("ender_eye_dead_silent");
    public static final SoundEvent SCULK_INFUSION = register("sculk_infusion");
    public static final SoundEvent SCULK_INFUSION1 = register("experience_orb_pickup_silent");
    public static final SoundEvent SCULK_INFUSION2 = register("sculk_catalyst_bloom_silent");
    public static final SoundEvent SCULK_INFUSION3 = register("splash_potion_break_silent");
    public static final SoundEvent SHIELD_COOLDOWN = register("shield_cooldown");

    private static SoundEvent register(String name) {
        return register(Constants.id(name));
    }

    private static SoundEvent register(Identifier name) {
        return register(name, name);
    }

    private static SoundEvent register(Identifier name, Identifier location) {
        SoundEvent soundEvent = SoundEvent.createVariableRangeEvent(location);
        SOUNDS.put(name, soundEvent);
        return soundEvent;
    }

    public static Map<Identifier, SoundEvent> getSounds() {
        return SOUNDS;
    }
}
