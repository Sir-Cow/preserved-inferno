package sircow.preservedinferno.potion;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import sircow.preservedinferno.Constants;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModPotions {
    private static final Map<Identifier, Potion> POTIONS = new LinkedHashMap<>();
    private static final Map<Identifier, Holder.Reference<Potion>> HOLDERS = new LinkedHashMap<>();

    public static final Potion NAUTILUS_BLESSING = register("nautilus_blessing", new Potion("nautilus_blessing", new MobEffectInstance(MobEffects.BREATH_OF_THE_NAUTILUS, 6000)));
    public static final Potion HASTE = register("haste", new Potion("haste", new MobEffectInstance(MobEffects.HASTE, 6000)));
    public static final Potion LONG_HASTE = register("long_haste", new Potion("haste", new MobEffectInstance(MobEffects.HASTE, 12000)));
    public static final Potion STRONG_HASTE = register("strong_haste", new Potion("haste", new MobEffectInstance(MobEffects.HASTE, 6000, 1)));
    public static final Potion MINING_FATIGUE = register("mining_fatigue", new Potion("mining_fatigue", new MobEffectInstance(MobEffects.MINING_FATIGUE, 6000)));
    public static final Potion LONG_MINING_FATIGUE = register("long_mining_fatigue", new Potion("mining_fatigue", new MobEffectInstance(MobEffects.MINING_FATIGUE, 12000)));
    public static final Potion STRONG_MINING_FATIGUE = register("strong_mining_fatigue", new Potion("mining_fatigue", new MobEffectInstance(MobEffects.MINING_FATIGUE, 6000, 1)));
    public static final Potion BLINDNESS = register("blindness", new Potion("blindness", new MobEffectInstance(MobEffects.BLINDNESS, 600)));
    public static final Potion LONG_BLINDNESS = register("long_blindness", new Potion("blindness", new MobEffectInstance(MobEffects.BLINDNESS, 1200)));
    public static final Potion LUCK = register("luck", new Potion("luck", new MobEffectInstance(MobEffects.LUCK, 6000)));
    public static final Potion LONG_LUCK = register("long_luck", new Potion("luck", new MobEffectInstance(MobEffects.LUCK, 12000)));
    public static final Potion STRONG_LUCK = register("strong_luck", new Potion("luck", new MobEffectInstance(MobEffects.LUCK, 6000, 1)));

    private static Potion register(String name, Potion potion) {
        POTIONS.put(Constants.id(name), potion);
        return potion;
    }

    public static Holder.Reference<Potion> nautilusBlessingHolder() {
        return HOLDERS.get(Constants.id("nautilus_blessing"));
    }
    public static Holder.Reference<Potion> hasteHolder() {
        return HOLDERS.get(Constants.id("haste"));
    }
    public static Holder.Reference<Potion> longHasteHolder() {
        return HOLDERS.get(Constants.id("long_haste"));
    }
    public static Holder.Reference<Potion> strongHasteHolder() {
        return HOLDERS.get(Constants.id("strong_haste"));
    }
    public static Holder.Reference<Potion> miningFatigueHolder() {
        return HOLDERS.get(Constants.id("mining_fatigue"));
    }
    public static Holder.Reference<Potion> longMiningFatigueHolder() {
        return HOLDERS.get(Constants.id("long_mining_fatigue"));
    }
    public static Holder.Reference<Potion> strongMiningFatigueHolder() {
        return HOLDERS.get(Constants.id("strong_mining_fatigue"));
    }
    public static Holder.Reference<Potion> blindnessHolder() {
        return HOLDERS.get(Constants.id("blindness"));
    }
    public static Holder.Reference<Potion> longBlindnessHolder() {
        return HOLDERS.get(Constants.id("long_blindness"));
    }
    public static Holder.Reference<Potion> luckHolder() {
        return HOLDERS.get(Constants.id("luck"));
    }
    public static Holder.Reference<Potion> longLuckHolder() {
        return HOLDERS.get(Constants.id("long_luck"));
    }
    public static Holder.Reference<Potion> strongLuckHolder() {
        return HOLDERS.get(Constants.id("strong_luck"));
    }

    public static Map<Identifier, Potion> getPotions() {
        return POTIONS;
    }

    public static Map<Identifier, Holder.Reference<Potion>> getHolders() {
        return HOLDERS;
    }
}
