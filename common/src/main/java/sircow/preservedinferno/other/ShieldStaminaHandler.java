package sircow.preservedinferno.other;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import sircow.preservedinferno.item.custom.PreservedShieldItem;
import sircow.preservedinferno.sound.ModSounds;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShieldStaminaHandler {
    public static final Map<UUID, Integer> playerShieldCooldownMap = new HashMap<>();
    private static final Map<UUID, Integer> regenBlockMap = new HashMap<>();
    private static final int COOLDOWN_TICKS = 20 * 10;
    private static final int REGEN_BLOCK_TICKS = 40;
    private static final float STAMINA_LOSS = 0.15F;
    public static DamageSource lastBypassingSource = null;

    public static void onServerTick(ServerPlayer player) {
        handleShieldUsage(player);
        handleStaminaRegeneration(player);
        handleCooldown(player);
        handleRegenBlock(player);
        checkBlockingOnCooldown(player);
        checkStopBlocking(player);
    }

    private static void handleShieldUsage(ServerPlayer player) {
        ItemStack heldStack = player.getOffhandItem();
        if (heldStack.getItem() instanceof PreservedShieldItem && player.isBlocking() && !isOnCooldown(player) && !player.isCreative()) {
            float currentStamina = player.getEntityData().get(ModEntityData.PLAYER_SHIELD_STAMINA);
            float drainRate = STAMINA_LOSS;

            if (lastBypassingSource != null) {
                drainRate *= 1.5F;
            }

            float newStamina = Math.max(0, currentStamina - drainRate);
            if (newStamina != currentStamina) {
                player.getEntityData().set(ModEntityData.PLAYER_SHIELD_STAMINA, newStamina);
            }

            if (newStamina <= 0 && currentStamina > 0) {
                triggerCooldown(player, heldStack);
            }
        }
    }

    private static void handleStaminaRegeneration(ServerPlayer player) {
        ItemStack stack = player.getOffhandItem();
        if (stack.getItem() instanceof PreservedShieldItem shieldItem && !player.isBlocking() && !isOnCooldown(player)  && !isRegenBlocked(player)) {
            float currentStamina = player.getEntityData().get(ModEntityData.PLAYER_SHIELD_STAMINA);
            int maxStamina = shieldItem.getMaxStamina(stack);
            float regenRate = shieldItem.getRegenerationRate(stack);

            float newStamina = Math.min(maxStamina, currentStamina + regenRate);
            if (newStamina != currentStamina) {
                player.getEntityData().set(ModEntityData.PLAYER_SHIELD_STAMINA, newStamina);
            }
        }
    }

    private static void handleCooldown(ServerPlayer player) {
        if (playerShieldCooldownMap.containsKey(player.getUUID())) {
            int cooldown = playerShieldCooldownMap.get(player.getUUID());
            if (cooldown > 0) {
                playerShieldCooldownMap.put(player.getUUID(), cooldown - 1);
            }
            else {
                playerShieldCooldownMap.remove(player.getUUID());
            }
        }
    }

    private static void handleRegenBlock(ServerPlayer player) {
        if (regenBlockMap.containsKey(player.getUUID())) {
            int ticks = regenBlockMap.get(player.getUUID());

            if (ticks > 0) {
                regenBlockMap.put(player.getUUID(), ticks - 1);
            }
            else {
                regenBlockMap.remove(player.getUUID());
            }
        }
    }

    private static void checkStopBlocking(ServerPlayer player) {
        UUID id = player.getUUID();
        boolean isBlocking = player.isBlocking();
        boolean wasBlocking = player.getEntityData().get(ModEntityData.PLAYER_WAS_BLOCKING);

        if (wasBlocking && !isBlocking) {
            regenBlockMap.put(id, REGEN_BLOCK_TICKS);
        }
        player.getEntityData().set(ModEntityData.PLAYER_WAS_BLOCKING, isBlocking);
    }

    public static void triggerCooldown(Player player, ItemStack shield) {
        if (shield.getItem() instanceof PreservedShieldItem) {
            playerShieldCooldownMap.put(player.getUUID(), COOLDOWN_TICKS);
            stopBlocking(player);
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack itemStack = player.getInventory().getItem(i);
                if (itemStack.getItem() instanceof PreservedShieldItem) {
                    player.getCooldowns().addCooldown(itemStack, COOLDOWN_TICKS);
                }
            }
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.level().playSound(null,
                        serverPlayer.getX(),
                        serverPlayer.getY(),
                        serverPlayer.getZ(),
                        ModSounds.SHIELD_COOLDOWN,
                        SoundSource.PLAYERS,
                        1.0F,
                        1.0F
                );
            }
        }
    }

    private static void stopBlocking(Player player) {
        player.stopUsingItem();
    }

    private static void checkBlockingOnCooldown(ServerPlayer player) {
        if (isOnCooldown(player) && player.isBlocking() && player.getUseItem().getItem() instanceof PreservedShieldItem) {
            stopBlocking(player);
        }
    }

    public static float getShieldStamina(ItemStack stack, Player player) {
        if (stack.getItem() instanceof PreservedShieldItem) {
            return player.getEntityData().get(ModEntityData.PLAYER_SHIELD_STAMINA);
        }
        return 0.0F;
    }

    public static int getShieldMaxStamina(ItemStack stack) {
        if (stack.getItem() instanceof PreservedShieldItem) {
            return ((PreservedShieldItem) stack.getItem()).getMaxStamina(stack);
        }
        return 0;
    }

    public static boolean isOnCooldown(Player player) {
        return playerShieldCooldownMap.containsKey(player.getUUID());
    }

    public static boolean isRegenBlocked(Player player) {
        return regenBlockMap.containsKey(player.getUUID());
    }
}
