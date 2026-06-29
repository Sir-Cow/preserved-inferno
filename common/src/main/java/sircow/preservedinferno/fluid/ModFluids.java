package sircow.preservedinferno.fluid;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.FlowingFluid;
import sircow.preservedinferno.Constants;

public class ModFluids {
    // note that flowing variants are just placeholders
    public static FlowingFluid HONEY, MILK, SNOW, FLOWING_HONEY, FLOWING_MILK, FLOWING_SNOW;

    public static void registerModFluids() {
        HONEY = Registry.register(BuiltInRegistries.FLUID, Constants.id("honey"), new HoneyFluid.Source());
        FLOWING_HONEY = Registry.register(BuiltInRegistries.FLUID, Constants.id("flowing_honey"), new HoneyFluid.Flowing());
        MILK = Registry.register(BuiltInRegistries.FLUID, Constants.id("milk"), new MilkFluid.Source());
        FLOWING_MILK = Registry.register(BuiltInRegistries.FLUID, Constants.id("flowing_milk"), new MilkFluid.Flowing());
        SNOW = Registry.register(BuiltInRegistries.FLUID, Constants.id("snow"), new SnowFluid.Source());
        FLOWING_SNOW = Registry.register(BuiltInRegistries.FLUID, Constants.id("flowing_snow"), new SnowFluid.Flowing());
    }
}
