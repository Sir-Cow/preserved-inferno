package sircow.preservedinferno.other;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.item.ModItems;

public class FabricModEvents {
    public static void modifySleeping() {
        // only allow sleeping if holding a dreamcatcher
        EntitySleepEvents.ALLOW_SLEEPING.register((Player player, BlockPos pos) -> {
            if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() != ModItems.DREAMCATCHER) {
                if (player.level().isMoonVisible()) {
                    player.displayClientMessage(Component.literal("You may not rest now; nightmares haunt you in your sleep"), true);
                }
                return Player.BedSleepingProblem.OTHER_PROBLEM;
            }
            return null;
        });
    }

    public static void registerModEvents() {
        Constants.LOG.info("Registering Fabric Mod Events for " + Constants.MOD_ID);
        modifySleeping();
    }
}
