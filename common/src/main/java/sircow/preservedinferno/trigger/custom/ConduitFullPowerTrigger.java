package sircow.preservedinferno.trigger.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.criterion.CriterionValidator;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ConduitFullPowerTrigger implements CriterionTrigger<ConduitFullPowerTrigger.Instance> {
    private final Map<PlayerAdvancements, Set<Listener<ConduitFullPowerTrigger.Instance>>> listeners = new HashMap<>();

    @Override
    public @NotNull Codec<ConduitFullPowerTrigger.Instance> codec() {
        return ConduitFullPowerTrigger.Instance.CODEC;
    }

    @Override
    public void addPlayerListener(@NotNull PlayerAdvancements playerAdvancements, @NotNull Listener<ConduitFullPowerTrigger.Instance> listener) {
        this.listeners.computeIfAbsent(playerAdvancements, k -> new HashSet<>()).add(listener);
    }

    @Override
    public void removePlayerListener(@NotNull PlayerAdvancements playerAdvancements, @NotNull Listener<ConduitFullPowerTrigger.Instance> listener) {
        Set<Listener<ConduitFullPowerTrigger.Instance>> set = this.listeners.get(playerAdvancements);
        if (set != null) {
            set.remove(listener);
            if (set.isEmpty()) {
                this.listeners.remove(playerAdvancements);
            }
        }
    }

    @Override
    public void removePlayerListeners(@NotNull PlayerAdvancements playerAdvancements) {
        this.listeners.remove(playerAdvancements);
    }

    public void trigger(ServerPlayer player) {
        Set<Listener<ConduitFullPowerTrigger.Instance>> set = this.listeners.get(player.getAdvancements());
        if (set != null) {
            for (Listener<ConduitFullPowerTrigger.Instance> listener : set) {
                listener.run(player.getAdvancements());
            }
        }
    }

    public static class Instance implements CriterionTriggerInstance {
        public static final Codec<ConduitFullPowerTrigger.Instance> CODEC = MapCodec.unit(new ConduitFullPowerTrigger.Instance()).codec();

        public Instance() {}

        @Override
        public void validate(@NotNull CriterionValidator validator) {}
    }
}
