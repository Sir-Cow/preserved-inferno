package sircow.preservedinferno.potion;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import sircow.preservedinferno.Constants;

public class ModPotions {
    public static final Holder<Potion> HASTE = register("haste", new Potion("haste", new MobEffectInstance(MobEffects.HASTE, 6000)));
    public static final Holder<Potion> LONG_HASTE = register("long_haste", new Potion("haste", new MobEffectInstance(MobEffects.HASTE, 12000)));
    public static final Holder<Potion> STRONG_HASTE = register("strong_haste", new Potion("haste", new MobEffectInstance(MobEffects.HASTE, 6000, 3)));
    public static final Holder<Potion> MINING_FATIGUE = register("mining_fatigue", new Potion("mining_fatigue", new MobEffectInstance(MobEffects.MINING_FATIGUE, 6000)));
    public static final Holder<Potion> LONG_MINING_FATIGUE = register("long_mining_fatigue", new Potion("mining_fatigue", new MobEffectInstance(MobEffects.MINING_FATIGUE, 12000)));
    public static final Holder<Potion> STRONG_MINING_FATIGUE = register("strong_mining_fatigue", new Potion("mining_fatigue", new MobEffectInstance(MobEffects.MINING_FATIGUE, 6000, 3)));
    public static final Holder<Potion> BLINDNESS = register("blindness", new Potion("blindness", new MobEffectInstance(MobEffects.BLINDNESS, 600)));
    public static final Holder<Potion> LONG_BLINDNESS = register("long_blindness", new Potion("blindness", new MobEffectInstance(MobEffects.BLINDNESS, 1200)));
    public static final Holder<Potion> LUCK = register("luck", new Potion("luck", new MobEffectInstance(MobEffects.LUCK, 6000)));
    public static final Holder<Potion> LONG_LUCK = register("long_luck", new Potion("luck", new MobEffectInstance(MobEffects.LUCK, 12000)));
    public static final Holder<Potion> STRONG_LUCK = register("strong_luck", new Potion("luck", new MobEffectInstance(MobEffects.LUCK, 6000, 3)));

    private static Holder<Potion> register(String name, Potion potion) {
        return Registry.registerForHolder(BuiltInRegistries.POTION, Constants.id(name), potion);
    }

    public static void registerModPotions() {
        Constants.LOG.info("Registering Mod Potions for " + Constants.MOD_ID);
    }
}
