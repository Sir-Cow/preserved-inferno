package sircow.preservedinferno.component;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class FabricModComponents {
    public static void registerFabricModComponents() {
        ModComponents.getComponents().forEach((id, component) ->
                Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id, component)
        );
    }
}
