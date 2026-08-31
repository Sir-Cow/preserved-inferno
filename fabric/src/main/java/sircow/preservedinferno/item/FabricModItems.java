package sircow.preservedinferno.item;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class FabricModItems {
    public static void registerFabricModItems() {
        ModItems.getItems().forEach((id, supplier) -> Registry.register(BuiltInRegistries.ITEM, id, supplier.get()));
    }
}
