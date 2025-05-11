package sircow.preservedinferno.other;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.events.ModEvents;
import sircow.preservedinferno.item.ModItems;
import sircow.preservedinferno.item.custom.PreservedShieldItem;

public class FabricModEvents {
    public static void modifySleeping() {
        // only allow sleeping if holding a dreamcatcher
        EntitySleepEvents.ALLOW_SLEEPING.register((Player player, BlockPos pos) -> {
            boolean holdingDreamcatcher = player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == ModItems.DREAMCATCHER ||
                    player.getItemInHand(InteractionHand.OFF_HAND).getItem() == ModItems.DREAMCATCHER;
            if (!holdingDreamcatcher) {
                if (player.level().isMoonVisible()) {
                    player.displayClientMessage(Component.translatable("block.minecraft.bed.no_dreamcatcher"), true);
                }
                return Player.BedSleepingProblem.OTHER_PROBLEM;
            }
            return null;
        });
    }

    public static void handleEntityDeath() {
        ServerLivingEntityEvents.AFTER_DEATH.register((livingEntity, damageSource) -> {
            // prevent drowned dropping trident
            if (livingEntity instanceof Drowned drowned) {
                ItemStack heldItem = drowned.getItemInHand(InteractionHand.MAIN_HAND);
                if (heldItem.getItem() instanceof TridentItem) {
                    drowned.setDropChance(EquipmentSlot.MAINHAND, 0);
                }
            }
            // reset shield cooldown
            if (livingEntity instanceof Player player) {
                ShieldStaminaHandler.playerShieldCooldownMap.remove(player.getUUID());
            }
        });
    }

    public static void damageThing() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (entity instanceof ServerPlayer player) {
                float modifiedAmount = ModEvents.onPlayerDamage(player, source, amount);
                return true;
            }
            return true;
        });
    }

    public static float onPlayerDamageWhileBlocking(ServerPlayer player, DamageSource source, float amount) {
        ItemStack blockingStack = player.getUseItem();
        if (player.isBlocking() && blockingStack.getItem() instanceof PreservedShieldItem) {
            ShieldStaminaHandler.onPlayerDamagedWhileBlocking(player, blockingStack, amount, source);
        }
        return amount;
    }

    public static void registerModEvents() {
        Constants.LOG.info("Registering Fabric Mod Events for " + Constants.MOD_ID);
        modifySleeping();
        handleEntityDeath();
        damageThing();
    }
}
