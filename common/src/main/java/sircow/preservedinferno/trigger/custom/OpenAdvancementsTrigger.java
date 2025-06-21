package sircow.preservedinferno.trigger.custom;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.CriterionValidator;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class OpenAdvancementsTrigger implements CriterionTrigger<OpenAdvancementsTrigger.Instance> {
    private final Map<PlayerAdvancements, Set<CriterionTrigger.Listener<Instance>>> listeners = new HashMap<>();

    @Override
    public @NotNull Codec<Instance> codec() {
        return Instance.CODEC;
    }

    @Override
    public void addPlayerListener(@NotNull PlayerAdvancements playerAdvancements, @NotNull Listener<Instance> listener) {
        this.listeners.computeIfAbsent(playerAdvancements, k -> new HashSet<>()).add(listener);
    }

    @Override
    public void removePlayerListener(@NotNull PlayerAdvancements playerAdvancements, @NotNull Listener<Instance> listener) {
        Set<Listener<Instance>> set = this.listeners.get(playerAdvancements);
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
        Set<Listener<Instance>> set = this.listeners.get(player.getAdvancements());
        if (set != null) {
            for (Listener<Instance> listener : set) {
                listener.run(player.getAdvancements());
            }
        }
    }

    public static class Instance implements CriterionTriggerInstance {
        public static final Codec<Instance> CODEC = Codec.unit(new Instance());

        public Instance() {}

        @Override
        public void validate(@NotNull CriterionValidator validator) {}
    }
}
