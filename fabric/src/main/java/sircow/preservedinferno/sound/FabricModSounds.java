package sircow.preservedinferno.sound;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class FabricModSounds {
    public static void registerFabricModSounds() {
        ModSounds.getSounds().forEach((id, soundEvent) ->
                Registry.register(BuiltInRegistries.SOUND_EVENT, id, soundEvent)
        );
    }
}
