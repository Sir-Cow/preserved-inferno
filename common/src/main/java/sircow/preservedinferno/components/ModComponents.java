package sircow.preservedinferno.components;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import sircow.preservedinferno.Constants;

public class ModComponents {
    // shields
    public static final DataComponentType<Integer> SHIELD_MAX_STAMINA_COMPONENT = DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build();
    public static final DataComponentType<Float> SHIELD_REGEN_RATE_COMPONENT = DataComponentType.<Float>builder().persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).build();
    // fishing
    public static final DataComponentType<Integer> HOOK_DURABILITY = DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build();
    public static final DataComponentType<String> HOOK_COMPONENT = DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build();
    public static final DataComponentType<Integer> LINE_DURABILITY = DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build();
    public static final DataComponentType<String> LINE_COMPONENT = DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build();
    public static final DataComponentType<Integer> SINKER_DURABILITY = DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build();
    public static final DataComponentType<String> SINKER_COMPONENT = DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build();
    public static final DataComponentType<Boolean> IS_FISHING = DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build();

    public static void registerModComponents() {
        Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Constants.id("shield_max_stamina"), SHIELD_MAX_STAMINA_COMPONENT);
        Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Constants.id("shield_regen_rate"), SHIELD_REGEN_RATE_COMPONENT);
        Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Constants.id("hook_durability"), HOOK_DURABILITY);
        Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Constants.id("hook_component"), HOOK_COMPONENT);
        Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Constants.id("line_durability"), LINE_DURABILITY);
        Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Constants.id("line_component"), LINE_COMPONENT);
        Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Constants.id("sinker_durability"), SINKER_DURABILITY);
        Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Constants.id("sinker_component"), SINKER_COMPONENT);
        Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Constants.id("is_fishing"), IS_FISHING);
        Constants.LOG.info("Registering Mod Components for " + Constants.MOD_ID);
    }
}
