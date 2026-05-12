package sircow.preservedinferno.other.cooldowns;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ElytraCooldown {
    private static final ItemStack ELYTRA_STACK = new ItemStack(Items.ELYTRA);

    public static void applyCooldown(ServerPlayer player, int ticks) {
        player.getCooldowns().addCooldown(ELYTRA_STACK, ticks);
    }
}
