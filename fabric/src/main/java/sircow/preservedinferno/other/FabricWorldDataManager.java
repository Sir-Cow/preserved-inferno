package sircow.preservedinferno.other;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.advancement.ModAdvancements;
import sircow.preservedinferno.network.ModMessages;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FabricWorldDataManager {
    public static void setPlayerPoints(MinecraftServer server, UUID playerUUID, int points) {
        WorldDataManager.getWorldData(server).playerPoints.put(playerUUID, points);
        WorldDataManager.getWorldData(server).setDirty();

        ServerPlayer player = server.getPlayerList().getPlayer(playerUUID);
        if (player != null) {
            ServerPlayNetworking.send(player, new ModMessages.PlayerPointsPayload(playerUUID, points));
            for (ServerPlayer other : server.getPlayerList().getPlayers()) {
                if (!other.getUUID().equals(playerUUID)) {
                    ServerPlayNetworking.send(other, new ModMessages.PlayerPointsPayload(playerUUID, points));
                }
            }
        }
    }

    public static void syncPlayerPointsWithAdvancements(MinecraftServer server, ServerPlayer player) {
        UUID playerUUID = player.getUUID();
        ModWorldData data = WorldDataManager.getWorldData(server);
        int recalculatedPoints = 0;
        Set<ResourceLocation> newAwardedAdvancements = new HashSet<>();
        for (AdvancementHolder holder : server.getAdvancements().getAllAdvancements()) {
            ResourceLocation id = holder.id();
            String namespace = id.getNamespace();
            if (!(namespace.equals("minecraft") || namespace.equals("pinferno"))) continue;
            if (!ModAdvancements.EXCLUDED_ADVANCEMENTS.contains(id) && !id.getPath().startsWith("recipes/")) {
                AdvancementProgress progress = player.getAdvancements().getOrStartProgress(holder);
                if (progress.isDone()) {
                    Advancement advancement = holder.value();
                    DisplayInfo display = advancement.display().orElse(null);
                    if (display != null) {
                        switch (display.getType()) {
                            case TASK -> recalculatedPoints += 2;
                            case GOAL -> recalculatedPoints += 3;
                            case CHALLENGE -> recalculatedPoints += 7;
                        }
                        if (display.getType().getSerializedName().equals("progressing")) {
                            recalculatedPoints += 1;
                        }
                    }
                    newAwardedAdvancements.add(id);
                }
            }
        }

        data.playerAwardedAdvancements.put(playerUUID, newAwardedAdvancements);
        setPlayerPoints(server, playerUUID, recalculatedPoints);
        String oldRank = WorldDataManager.getPlayerRank(server, playerUUID);
        String newRank = WorldDataManager.calculateRank(server, player, recalculatedPoints);
        if (!oldRank.equals(newRank)) {
            WorldDataManager.setPlayerRank(server, playerUUID, newRank);
            FabricModEvents.assignPlayerToRankTeam(player);
            if (WorldDataManager.isRankUp(oldRank, newRank)) {
                String prefixString = WorldDataManager.RANK_PREFIXES.getOrDefault(newRank, Component.empty()).getString();
                if (prefixString.endsWith(" ")) {
                    prefixString = prefixString.substring(0, prefixString.length() - 1);
                }
                MutableComponent message = Component.translatable(
                        "message.pinferno.mastery_rank_up",
                        player.getName(),
                        Component.literal(prefixString)
                ).withStyle(style -> style
                        .withHoverEvent(new HoverEvent.ShowText(Component.translatable("message.pinferno.mastery_rank_up_hover")))
                        .withClickEvent(new ClickEvent.SuggestCommand("/msg " + player.getName().getString() + " GG!"))
                );

                for (ServerPlayer onlinePlayer : server.getPlayerList().getPlayers()) {
                    onlinePlayer.sendSystemMessage(message);
                }
            }
            WorldDataManager.removeMasteryAdvancementIfDowngraded(player, oldRank, newRank);
        }
    }
}
