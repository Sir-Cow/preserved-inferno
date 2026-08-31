package sircow.preservedinferno.potion;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.alchemy.Potion;

public class FabricModPotions {
    public static void registerFabricModPotions() {
        ModPotions.getPotions().forEach((id, potion) -> {
            Holder.Reference<Potion> holder = Registry.registerForHolder(BuiltInRegistries.POTION, id, potion);
            ModPotions.getHolders().put(id, holder);
        });
    }
}
