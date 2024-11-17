package sircow.placeholder.sound;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import sircow.placeholder.Placeholder;

public class ModSounds {
    // sounds
    public static final SoundEvent ENCHANT_CLICK = registerSoundEvent("enchant_click");
    public static final SoundEvent ENCHANT_ONE = registerSoundEvent("enchant_one");
    public static final SoundEvent ENCHANT_TWO = registerSoundEvent("enchant_two");
    public static final SoundEvent ENCHANT_THREE = registerSoundEvent("enchant_three");
    public static final SoundEvent ENCHANT_OPEN_FLIP_ONE = registerSoundEvent("enchant_open_flip_one");
    public static final SoundEvent ENCHANT_OPEN_FLIP_TWO = registerSoundEvent("enchant_open_flip_two");
    public static final SoundEvent ENCHANT_OPEN_FLIP_THREE = registerSoundEvent("enchant_open_flip_three");
    public static final SoundEvent ENCHANT_CLOSE_ONE = registerSoundEvent("enchant_close_one");
    public static final SoundEvent ENCHANT_CLOSE_TWO = registerSoundEvent("enchant_close_two");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(Placeholder.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        Placeholder.LOGGER.info("Registering Mod Sounds for " + Placeholder.MOD_ID);
    }
}
