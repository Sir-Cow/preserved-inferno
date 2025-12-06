package sircow.preservedinferno.other;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;
import sircow.preservedinferno.effect.ModEffects;

public class NeoForgeEvents {
    @EventBusSubscriber(modid = "pinferno")
    public static class EventHandler {
        @SubscribeEvent
        public static void modifySleeping(PlayerWakeUpEvent event) {
            LivingEntity entity = event.getEntity();
            if (entity instanceof Player player) {
                if (player.getSleepTimer() > 20 && !player.level().isMoonVisible()) {
                    MinecraftServer server = player.level().getServer();
                    if (server != null) {
                        for (ServerPlayer serverPlayer : server.getPlayerList().getPlayers()) {
                            serverPlayer.addEffect(new MobEffectInstance(ModEffects.WELL_RESTED.holder, 24000, 0, false, false, true));
                            if (player.getUUID() == serverPlayer.getUUID()) {
                                serverPlayer.displayClientMessage(Component.translatable("effect.pinferno.well_rested_awake"), true);
                            }
                            else {
                                serverPlayer.displayClientMessage(Component.translatable("effect.pinferno.well_rested_awake_not_sleeping", player.getName()), true);
                            }
                        }
                    }
                }
            }
        }
    }
}
