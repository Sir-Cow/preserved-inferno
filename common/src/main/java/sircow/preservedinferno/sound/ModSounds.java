package sircow.preservedinferno.sound;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import sircow.preservedinferno.Constants;

public class ModSounds {
    // sounds
    public static final SoundEvent ENCHANT_CLICK = register("enchant_click");
    public static final SoundEvent ENCHANT_ONE = register("enchant_one");
    public static final SoundEvent ENCHANT_TWO = register("enchant_two");
    public static final SoundEvent ENCHANT_THREE = register("enchant_three");
    public static final SoundEvent ENCHANT_OPEN_FLIP_ONE = register("enchant_open_flip_one");
    public static final SoundEvent ENCHANT_OPEN_FLIP_TWO = register("enchant_open_flip_two");
    public static final SoundEvent ENCHANT_OPEN_FLIP_THREE = register("enchant_open_flip_three");
    public static final SoundEvent ENCHANT_CLOSE_ONE = register("enchant_close_one");
    public static final SoundEvent ENCHANT_CLOSE_TWO = register("enchant_close_two");

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
