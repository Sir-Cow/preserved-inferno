package sircow.preservedinferno.other;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

import java.util.*;

public final class AdvancementDelayCache {
    private static final Map<ResourceLocation, Set<UUID>> worldCompletedCache = new HashMap<>();

    public static boolean hasCompleted(ServerLevel level, UUID uuid) {
        ResourceLocation dimension = level.dimension().location();
        return worldCompletedCache.getOrDefault(dimension, Set.of()).contains(uuid);
    }

    public static void markCompleted(ServerLevel level, UUID uuid) {
        ResourceLocation dimension = level.dimension().location();
        worldCompletedCache.computeIfAbsent(dimension, k -> new HashSet<>()).add(uuid);
    }
}
