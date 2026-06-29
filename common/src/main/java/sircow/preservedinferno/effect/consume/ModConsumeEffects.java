package sircow.preservedinferno.effect.consume;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

public class ModConsumeEffects {
    public static final ConsumeEffect.Type<IgniteConsumeEffect> IGNITE = Registry.register(BuiltInRegistries.CONSUME_EFFECT_TYPE, "pinferno:ignite", new ConsumeEffect.Type<>(IgniteConsumeEffect.CODEC, IgniteConsumeEffect.STREAM_CODEC));

    public static void init() {}
}
