package sircow.preservedinferno.other;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import sircow.preservedinferno.PreservedInferno;
import sircow.preservedinferno.item.ModItems;

public class ModEvents {
    public static void modifySleeping() {
        // only allow sleeping if holding a dreamcatcher
        EntitySleepEvents.ALLOW_SLEEPING.register((PlayerEntity player, BlockPos pos) -> {
            if (player.getStackInHand(Hand.MAIN_HAND).getItem() != ModItems.DREAMCATCHER) {
                if (player.getWorld().isNight()) {
                    player.sendMessage(Text.literal("You may not rest now; nightmares haunt you in your sleep"), true);
                }
                return PlayerEntity.SleepFailureReason.OTHER_PROBLEM;
            }
            return null;
        });
    }

    public static void registerModEvents() {
        PreservedInferno.LOGGER.info("Registering Mod Events for " + PreservedInferno.MOD_ID);
    }
}
