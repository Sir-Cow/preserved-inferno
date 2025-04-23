package sircow.preservedinferno.other;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.item.ModItems;

public class FabricModEvents {
    public static void modifySleeping() {
        // only allow sleeping if holding a dreamcatcher
        EntitySleepEvents.ALLOW_SLEEPING.register((Player player, BlockPos pos) -> {
            if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() != ModItems.DREAMCATCHER) {
                if (player.level().isMoonVisible()) {
                    player.displayClientMessage(Component.translatable("block.minecraft.bed.no_dreamcatcher"), true);
                }
                return Player.BedSleepingProblem.OTHER_PROBLEM;
            }
            return null;
        });
    }

    public static void removeTridentDropFromDrowned() {
        ServerLivingEntityEvents.AFTER_DEATH.register((livingEntity, damageSource) -> {
            if (livingEntity instanceof Drowned drowned) {
                ItemStack heldItem = drowned.getItemInHand(InteractionHand.MAIN_HAND);
                if (heldItem.getItem() instanceof TridentItem) {
                    ((Drowned) livingEntity).setDropChance(EquipmentSlot.MAINHAND, 0);
                }
            }
        });
    }

    public static void registerModEvents() {
        Constants.LOG.info("Registering Fabric Mod Events for " + Constants.MOD_ID);
        modifySleeping();
        removeTridentDropFromDrowned();
    }
}
