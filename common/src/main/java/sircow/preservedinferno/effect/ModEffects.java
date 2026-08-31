package sircow.preservedinferno.effect;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.effect.custom.FumigatedEffect;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModEffects {
    private static final Map<Identifier, MobEffect> EFFECTS = new LinkedHashMap<>();
    private static final Map<Identifier, Holder<MobEffect>> HOLDERS = new LinkedHashMap<>();

    public static final MobEffect FUMIGATED = register("fumigated", FumigatedEffect::new);
    public static final MobEffect HINDERED = register("hindered", () -> new PreservedEffect(MobEffectCategory.HARMFUL, 0x6C4EB7));
    public static final MobEffect WELL_RESTED = register("well_rested", () -> new PreservedEffect(MobEffectCategory.BENEFICIAL, 0xE3884E));

    private static MobEffect register(String name, Supplier<MobEffect> factory) {
        MobEffect effect = factory.get();
        EFFECTS.put(Constants.id(name), effect);
        return effect;
    }

    public static Holder<MobEffect> fumigatedHolder() {
        return HOLDERS.get(Constants.id("fumigated"));
    }
    public static Holder<MobEffect> hinderedHolder() {
        return HOLDERS.get(Constants.id("hindered"));
    }
    public static Holder<MobEffect> wellRestedHolder() {
        return HOLDERS.get(Constants.id("well_rested"));
    }

    public static Map<Identifier, MobEffect> getEffects() {
        return EFFECTS;
    }

    public static Map<Identifier, Holder<MobEffect>> getHolders() {
        return HOLDERS;
    }
}
