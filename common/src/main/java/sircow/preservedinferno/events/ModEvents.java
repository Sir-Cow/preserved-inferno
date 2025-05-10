package sircow.preservedinferno.events;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import sircow.preservedinferno.Constants;

import java.util.ArrayList;
import java.util.List;

public class ModEvents {
    public static final ResourceLocation PLAYER_DAMAGE_EVENT_ID = Constants.id("player_damage_event");

    public interface PlayerDamageEvent {
        float onPlayerDamage(ServerPlayer player, DamageSource source, float amount);
    }

    private static final List<PlayerDamageEvent> PLAYER_DAMAGE_LISTENERS = new ArrayList<>();

    public static void register(PlayerDamageEvent listener) {
        PLAYER_DAMAGE_LISTENERS.add(listener);
    }

    public static float onPlayerDamage(ServerPlayer player, DamageSource source, float amount) {
        float modifiedAmount = amount;
        for (PlayerDamageEvent listener : PLAYER_DAMAGE_LISTENERS) {
            modifiedAmount = listener.onPlayerDamage(player, source, modifiedAmount);
        }
        return modifiedAmount;
    }

    private ModEvents() {
        // nothing
    }
}
