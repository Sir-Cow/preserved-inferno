package sircow.preservedinferno.component;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import sircow.preservedinferno.Constants;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModComponents {
    private static final Map<Identifier, DataComponentType<?>> COMPONENTS = new LinkedHashMap<>();

    // shields
    public static final DataComponentType<Integer> SHIELD_MAX_STAMINA_COMPONENT = register("shield_max_stamina", DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
    public static final DataComponentType<Float> SHIELD_REGEN_RATE_COMPONENT = register("shield_regen_rate", DataComponentType.<Float>builder().persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).build());
    // fishing
    public static final DataComponentType<Integer> HOOK_DURABILITY = register("hook_durability", DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
    public static final DataComponentType<String> HOOK_COMPONENT = register("hook_component", DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build());
    public static final DataComponentType<Integer> HOOK_UNBREAKING = register("hook_unbreaking", DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
    public static final DataComponentType<Integer> LINE_DURABILITY = register("line_durability", DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
    public static final DataComponentType<String> LINE_COMPONENT = register("line_component", DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build());
    public static final DataComponentType<Integer> LINE_UNBREAKING = register("line_unbreaking", DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
    public static final DataComponentType<Integer> SINKER_DURABILITY = register("sinker_durability", DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
    public static final DataComponentType<String> SINKER_COMPONENT = register("sinker_component", DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build());
    public static final DataComponentType<Integer> SINKER_UNBREAKING = register("sinker_unbreaking", DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
    // other
    public static final DataComponentType<String> FLARE_PARTICLE_COMPONENT = register("flare_particle_component", DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build());
    public static final DataComponentType<String> FORGE_MATERIAL_COMPONENT = register("forge_material_component", DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build());
    public static final DataComponentType<Boolean> ON_COOLDOWN = register("on_cooldown", DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
    public static final DataComponentType<Boolean> EXHAUSTED_TEMPLATE = register("exhausted_template", DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());

    private static <T> DataComponentType<T> register(String name, DataComponentType<T> componentType) {
        COMPONENTS.put(Constants.id(name), componentType);
        return componentType;
    }

    public static Map<Identifier, DataComponentType<?>> getComponents() {
        return COMPONENTS;
    }
}
