package sircow.preservedinferno.other;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import sircow.preservedinferno.enchantment.ModEnchantments;
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
    public static DamageSource lastBypassingSource;

    public static void onServerTick(ServerPlayer player) {
        handleShieldUsage(player);
        handleStaminaRegeneration(player);
        handleCooldown(player);
        handleRegenBlock(player);
        checkBlockingOnCooldown(player);
        checkStopBlocking(player);
    }

    private static void handleShieldUsage(ServerPlayer player) {
        int regenDelay = player.getEntityData().get(ModEntityData.PLAYER_SHIELD_REGEN_DURATION);
        if (regenDelay > 0) return;

        ItemStack heldStack = player.getOffhandItem();
        if (heldStack.getItem() instanceof PreservedShieldItem && player.isBlocking() && !isOnCooldown(player) && !player.isCreative()) {
            float currentStamina = player.getEntityData().get(ModEntityData.PLAYER_SHIELD_STAMINA);
            float drainRate = STAMINA_LOSS;

            if (lastBypassingSource != null) drainRate *= 1.5F;

            float newStamina = Math.max(0, currentStamina - drainRate);
            if (newStamina != currentStamina) player.getEntityData().set(ModEntityData.PLAYER_SHIELD_STAMINA, newStamina);

            if (newStamina <= 0 && currentStamina > 0) triggerCooldown(player, heldStack);
        }
    }

    private static void handleStaminaRegeneration(ServerPlayer player) {
        int regenDelay = player.getEntityData().get(ModEntityData.PLAYER_SHIELD_REGEN_DURATION);
        if (regenDelay > 0) {
            player.getEntityData().set(ModEntityData.PLAYER_SHIELD_REGEN_DURATION, regenDelay - 1);
            return;
        }

        float stamina = player.getEntityData().get(ModEntityData.PLAYER_SHIELD_STAMINA);
        if (stamina <= 0) regenBlockMap.remove(player.getUUID());

        ItemStack stack = player.getOffhandItem();
        float maxStamina = getShieldMaxStamina(stack);

        if (stamina > maxStamina) {
            float newStamina = Math.max(maxStamina, stamina - 0.05F);
            player.getEntityData().set(ModEntityData.PLAYER_SHIELD_STAMINA, newStamina);
            return;
        }

        if (stack.getItem() instanceof PreservedShieldItem shieldItem) {
            if (!player.isBlocking() && !isOnCooldown(player) && !isRegenBlocked(player)) {
                float regenRate = shieldItem.getRegenerationRate(stack);
                float newStamina = Math.min(maxStamina, stamina + regenRate);

                if (newStamina != stamina) player.getEntityData().set(ModEntityData.PLAYER_SHIELD_STAMINA, newStamina);
            }
        }
    }

    private static void handleCooldown(ServerPlayer player) {
        if (playerShieldCooldownMap.containsKey(player.getUUID())) {
            int cooldown = playerShieldCooldownMap.get(player.getUUID());
            if (cooldown > 0) playerShieldCooldownMap.put(player.getUUID(), cooldown - 1);
            else playerShieldCooldownMap.remove(player.getUUID());
        }
    }

    private static void handleRegenBlock(ServerPlayer player) {
        if (regenBlockMap.containsKey(player.getUUID())) {
            int ticks = regenBlockMap.get(player.getUUID());
            if (ticks > 0) regenBlockMap.put(player.getUUID(), ticks - 1);
            else regenBlockMap.remove(player.getUUID());
        }
    }

    public static void triggerRegenBlock(Player player, int ticks) {
        regenBlockMap.put(player.getUUID(), ticks);
    }

    private static void checkStopBlocking(ServerPlayer player) {
        UUID id = player.getUUID();
        boolean isBlocking = player.isBlocking();
        boolean wasBlocking = player.getEntityData().get(ModEntityData.PLAYER_WAS_BLOCKING);

        if (wasBlocking && !isBlocking) regenBlockMap.put(id, REGEN_BLOCK_TICKS);
        player.getEntityData().set(ModEntityData.PLAYER_WAS_BLOCKING, isBlocking);
    }

    public static void triggerCooldown(Player player, ItemStack shield) {
        if (shield.getItem() instanceof PreservedShieldItem) {
            if (player.level() instanceof ServerLevel level) {
                Holder<Enchantment> repulsor = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ModEnchantments.REPULSOR);

                if (EnchantmentHelper.getItemEnchantmentLevel(repulsor, shield) > 0) {
                    Vec3 look = player.getViewVector(1.0F);
                    double lookX = look.x;
                    double lookZ = look.z;
                    double lookLen = Math.sqrt(lookX * lookX + lookZ * lookZ);
                    Vec3 lookDir = lookLen > 1e-6 ? new Vec3(lookX / lookLen, 0.0D, lookZ / lookLen) : new Vec3(0.0D, 0.0D, 0.0D);
                    Vec3 playerPos = player.position();
                    AABB box = player.getBoundingBox().inflate(8.0);

                    for (LivingEntity mob : level.getEntitiesOfClass(LivingEntity.class, box, target -> target != player && target.isAlive() && !target.isInvulnerable())) {
                        Vec3 toTarget = new Vec3(mob.getX() - playerPos.x, 0.0D, mob.getZ() - playerPos.z);
                        double len = toTarget.length();

                        if (len <= 8.0 && len > 1e-6) {
                            Vec3 toDir = toTarget.normalize();
                            if (toDir.x * lookDir.x + toDir.z * lookDir.z >= 0.5D) {
                                mob.knockback(2.0, -toDir.x, -toDir.z, mob.damageSources().playerAttack(player), 0.0F, false);
                            }
                        }
                    }
                    level.sendParticles(ParticleTypes.GUST, playerPos.x, playerPos.y + 1.0, playerPos.z, 8, 2.0, 1.0, 2.0, 0.02);
                    if (player instanceof ServerPlayer serverPlayer) {
                        serverPlayer.level().playSound(null,
                                serverPlayer.getX(),
                                serverPlayer.getY(),
                                serverPlayer.getZ(),
                                SoundEvents.WIND_CHARGE_BURST,
                                SoundSource.PLAYERS,
                                1.0F,
                                1.0F
                        );
                    }
                }
            }

            playerShieldCooldownMap.put(player.getUUID(), COOLDOWN_TICKS);
            stopBlocking(player);
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack itemStack = player.getInventory().getItem(i);
                if (itemStack.getItem() instanceof PreservedShieldItem) player.getCooldowns().addCooldown(itemStack, COOLDOWN_TICKS);
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
        if (isOnCooldown(player) && player.isBlocking() && player.getUseItem().getItem() instanceof PreservedShieldItem) stopBlocking(player);
    }

    public static float getShieldStamina(ItemStack stack, Player player) {
        if (stack.getItem() instanceof PreservedShieldItem) return player.getEntityData().get(ModEntityData.PLAYER_SHIELD_STAMINA);
        return 0.0F;
    }

    public static int getShieldMaxStamina(ItemStack stack) {
        if (stack.getItem() instanceof PreservedShieldItem) return ((PreservedShieldItem) stack.getItem()).getMaxStamina(stack);
        return 0;
    }

    public static boolean isOnCooldown(Player player) {
        return playerShieldCooldownMap.containsKey(player.getUUID());
    }

    public static boolean isRegenBlocked(Player player) {
        return regenBlockMap.containsKey(player.getUUID());
    }
}
