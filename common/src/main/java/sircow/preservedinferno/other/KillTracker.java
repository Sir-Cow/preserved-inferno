package sircow.preservedinferno.other;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import sircow.preservedinferno.trigger.ModTriggers;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class KillTracker {
    private static final Map<UUID, KillSession> ACTIVE_SESSIONS = new HashMap<>();

    public static void onEntityKilled(ServerPlayer serverPlayer, LivingEntity livingEntity, DamageSource source) {
        if (!Objects.requireNonNull(source.getWeaponItem()).is(ItemTags.SWORDS)) return;

        var session = ACTIVE_SESSIONS.computeIfAbsent(serverPlayer.getUUID(), id -> new KillSession());
        session.registerKill(serverPlayer.level().getGameTime());

        if (session.kills >= 3) {
            ModTriggers.TRIPLE_KILL.trigger(serverPlayer);
            ACTIVE_SESSIONS.remove(serverPlayer.getUUID());
        }
    }

    private static class KillSession {
        int kills = 0;
        long lastKillTick = 0;

        void registerKill(long currentTick) {
            if (currentTick - lastKillTick > 5) {
                kills = 0;
            }
            kills++;
            lastKillTick = currentTick;
        }
    }
}
