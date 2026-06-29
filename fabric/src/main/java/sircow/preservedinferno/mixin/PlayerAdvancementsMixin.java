package sircow.preservedinferno.mixin;

import net.minecraft.advancements.*;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.advancement.ModAdvancements;
import sircow.preservedinferno.other.FabricModEvents;
import sircow.preservedinferno.other.FabricWorldDataManager;
import sircow.preservedinferno.other.WorldDataManager;
import sircow.preservedinferno.trigger.ModTriggers;

import java.util.*;

@Mixin(PlayerAdvancements.class)
public abstract class PlayerAdvancementsMixin {
    @Shadow private ServerPlayer player;
    @Shadow @Final private Set<AdvancementHolder> visible;
    @Shadow private AdvancementTree tree;

    @Inject(method = "award", at = @At("RETURN"))
    private void pinferno$onAwardAdvancement(AdvancementHolder advancementHolder, String criterionKey, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;

        MinecraftServer server = player.level().getServer();

        if (ModAdvancements.EXCLUDED_ADVANCEMENTS.contains(advancementHolder.id()) || advancementHolder.id().getPath().startsWith("recipes/")) return;

        FabricWorldDataManager.syncPlayerPointsWithAdvancements(server, player);

        int currentPoints = WorldDataManager.getPlayerPoints(server, player.getUUID());
        String oldRank = WorldDataManager.getPlayerRank(server, player.getUUID());
        String newRank = WorldDataManager.calculateRank(server, player, currentPoints);

        if (!oldRank.equals(newRank)) {
            WorldDataManager.setPlayerRank(server, player.getUUID(), newRank);
            FabricModEvents.assignPlayerToRankTeam(player);
            triggerRankAdvancement(newRank, player);
        }
        else triggerRankAdvancement(newRank, player);
    }

    @Inject(method = "revoke", at = @At("RETURN"))
    private void pinferno$onRevokeAdvancement(AdvancementHolder advancementHolder, String criterionKey, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;
        FabricWorldDataManager.syncPlayerPointsWithAdvancements(player.level().getServer(), player);
    }

    @Unique
    private void triggerRankAdvancement(String rank, ServerPlayer player) {
        switch (rank) {
            case "infernal" -> ModTriggers.MASTERY_INFERNAL.get().trigger(player);
            case "champion" -> ModTriggers.MASTERY_CHAMPION.get().trigger(player);
            case "centurion" -> ModTriggers.MASTERY_CENTURION.get().trigger(player);
            case "master" -> ModTriggers.MASTERY_MASTER.get().trigger(player);
            case "knight" -> ModTriggers.MASTERY_KNIGHT.get().trigger(player);
            case "squire" -> ModTriggers.MASTERY_SQUIRE.get().trigger(player);
            case "disciple" -> ModTriggers.MASTERY_DISCIPLE.get().trigger(player);
            case "novice" -> ModTriggers.MASTERY_NOVICE.get().trigger(player);
            case "beginner" -> ModTriggers.MASTERY_BEGINNER.get().trigger(player);
            case "starter" -> ModTriggers.MASTERY_STARTER.get().trigger(player);
        }
    }

    @Overwrite
    private void updateTreeVisibility(AdvancementNode root, Set<AdvancementHolder> advancementOutput, Set<Identifier> idOutput) {
        Set<AdvancementHolder> previouslyVisible = new HashSet<>(this.visible);

        this.visible.clear();
        idOutput.clear();

        for (AdvancementNode node : this.tree.nodes()) {
            AdvancementHolder advancementHolder = node.holder();

            this.visible.add(advancementHolder);

            if (!previouslyVisible.contains(advancementHolder)) advancementOutput.add(advancementHolder);
        }

        for (AdvancementHolder holder : previouslyVisible) {
            if (!this.visible.contains(holder)) idOutput.add(holder.id());
        }
    }
}
