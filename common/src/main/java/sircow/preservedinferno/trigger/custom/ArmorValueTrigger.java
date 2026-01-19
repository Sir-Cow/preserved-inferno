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

public class ArmorValueTrigger implements CriterionTrigger<ArmorValueTrigger.Instance> {
    private final Map<PlayerAdvancements, Set<Listener<ArmorValueTrigger.Instance>>> listeners = new HashMap<>();

    @Override
    public @NotNull Codec<ArmorValueTrigger.Instance> codec() {
        return ArmorValueTrigger.Instance.CODEC;
    }

    @Override
    public void addPlayerListener(@NotNull PlayerAdvancements playerAdvancements, @NotNull Listener<ArmorValueTrigger.Instance> listener) {
        this.listeners.computeIfAbsent(playerAdvancements, k -> new HashSet<>()).add(listener);
    }

    @Override
    public void removePlayerListener(@NotNull PlayerAdvancements playerAdvancements, @NotNull Listener<ArmorValueTrigger.Instance> listener) {
        Set<Listener<ArmorValueTrigger.Instance>> set = this.listeners.get(playerAdvancements);
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
        Set<Listener<ArmorValueTrigger.Instance>> set = this.listeners.get(player.getAdvancements());
        if (set != null) {
            for (Listener<ArmorValueTrigger.Instance> listener : set) {
                listener.run(player.getAdvancements());
            }
        }
    }

    public static class Instance implements CriterionTriggerInstance {
        public static final Codec<ArmorValueTrigger.Instance> CODEC = MapCodec.unit(new ArmorValueTrigger.Instance()).codec();

        public Instance() {}

        @Override
        public void validate(@NotNull CriterionValidator validator) {}
    }
}
