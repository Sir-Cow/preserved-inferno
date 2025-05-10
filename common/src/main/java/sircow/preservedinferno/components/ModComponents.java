package sircow.preservedinferno.components;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import sircow.preservedinferno.Constants;

public class ModComponents {
    public static final DataComponentType<Integer> SHIELD_MAX_STAMINA_COMPONENT = DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build();
    public static final DataComponentType<Float> SHIELD_REGEN_RATE_COMPONENT = DataComponentType.<Float>builder().persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).build();

    public static void registerModComponents() {
        Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Constants.id("shield_max_stamina"), SHIELD_MAX_STAMINA_COMPONENT);
        Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Constants.id("shield_regen_rate"), SHIELD_REGEN_RATE_COMPONENT);
        Constants.LOG.info("Registering Mod Components for " + Constants.MOD_ID);
    }
}
