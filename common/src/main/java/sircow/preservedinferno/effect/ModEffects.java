package sircow.preservedinferno.effect;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import java.util.List;
import java.util.function.Supplier;

public class ModEffects {
    public static class EffectEntry {
        public final String id;
        public final Supplier<MobEffect> factory;
        public Holder<MobEffect> holder;

        public EffectEntry(String id, Supplier<MobEffect> factory) {
            this.id = id;
            this.factory = factory;
        }
    }

    public static final EffectEntry HINDERED = new EffectEntry("hindered", () -> new HinderedEffect(MobEffectCategory.HARMFUL, 0x6C4EB7));
    public static final EffectEntry WELL_RESTED = new EffectEntry("well_rested", () -> new WellRestedEffect(MobEffectCategory.BENEFICIAL, 0xE3884E));

    public static final List<EffectEntry> ALL_EFFECTS = List.of(
            HINDERED, WELL_RESTED
    );
}
