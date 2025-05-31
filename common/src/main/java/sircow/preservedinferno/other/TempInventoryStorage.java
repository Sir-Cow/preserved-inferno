package sircow.preservedinferno.other;

import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import sircow.preservedinferno.effect.ModEffects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// used for handling inventory keeping when dying with well rested effect
public class TempInventoryStorage {
    private static final Map<UUID, ListTag> SAVED_INVENTORIES = new HashMap<>();
    private static final Map<UUID, Integer> SAVED_EXPERIENCE_LEVELS = new HashMap<>();
    private static final Map<UUID, Float> SAVED_EXPERIENCE_PROGRESS = new HashMap<>();
    private static final Map<UUID, Boolean> PLAYER_HAD_WELL_RESTED_ON_DEATH = new HashMap<>();

    public static void savePlayerInventory(Player player) {
        if (player.hasEffect(ModEffects.WELL_RESTED)) {
            ListTag inventoryTag = new ListTag();
            player.getInventory().save(inventoryTag);
            SAVED_INVENTORIES.put(player.getUUID(), inventoryTag);

            if (player instanceof ServerPlayer serverPlayer) {
                SAVED_EXPERIENCE_LEVELS.put(serverPlayer.getUUID(), serverPlayer.experienceLevel);
                SAVED_EXPERIENCE_PROGRESS.put(serverPlayer.getUUID(), serverPlayer.experienceProgress);
            }

            PLAYER_HAD_WELL_RESTED_ON_DEATH.put(player.getUUID(), true);
        }
    }

    public static boolean restorePlayerInventory(ServerPlayer player) {
        UUID playerUUID = player.getUUID();
        boolean hadWellRestedEffectOnDeath = PLAYER_HAD_WELL_RESTED_ON_DEATH.remove(playerUUID) != null;

        if (SAVED_INVENTORIES.containsKey(playerUUID)) {
            ListTag inventoryTag = SAVED_INVENTORIES.remove(playerUUID);
            player.getInventory().clearContent();
            player.getInventory().load(inventoryTag);

            if (SAVED_EXPERIENCE_LEVELS.containsKey(playerUUID)) {
                player.experienceLevel = SAVED_EXPERIENCE_LEVELS.remove(playerUUID);
            }
            if (SAVED_EXPERIENCE_PROGRESS.containsKey(playerUUID)) {
                player.experienceProgress = SAVED_EXPERIENCE_PROGRESS.remove(playerUUID);
            }
        }

        if (hadWellRestedEffectOnDeath) {
            player.removeEffect(ModEffects.WELL_RESTED);
        }

        return hadWellRestedEffectOnDeath;
    }
}
