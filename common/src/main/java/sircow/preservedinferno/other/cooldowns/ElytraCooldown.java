package sircow.preservedinferno.other.cooldowns;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ElytraCooldown {
    public static void applyCooldown(ServerPlayer player, int ticks) {
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);

        if (chest.is(Items.ELYTRA)) {
            player.getCooldowns().addCooldown(chest, ticks);
        }
    }
}
