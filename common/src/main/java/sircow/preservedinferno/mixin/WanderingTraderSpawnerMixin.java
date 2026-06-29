package sircow.preservedinferno.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTraderSpawner;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.saveddata.WanderingTraderData;
import net.minecraft.world.level.storage.SavedDataStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(WanderingTraderSpawner.class)
public abstract class WanderingTraderSpawnerMixin {
    @Shadow @Final private SavedDataStorage savedDataStorage;
    @Shadow private WanderingTraderData traderData;
    @Unique private static final int START_DELAY = 24000;
    @Unique private static final int CHANCE_INCREMENT = 8;
    @Unique private int countdown = START_DELAY;

    @Shadow protected abstract boolean spawn(ServerLevel serverLevel);

    @Unique
    private WanderingTraderData getData() {
        if (this.traderData == null) {
            this.traderData = this.savedDataStorage.computeIfAbsent(WanderingTraderData.TYPE);
        }
        return this.traderData;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void pinferno$onInit(SavedDataStorage savedDataStorage, CallbackInfo ci) {
        WanderingTraderData data = this.getData();
        this.countdown = START_DELAY;
        data.setSpawnChance(0);
        data.setSpawnDelay(START_DELAY);
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void pinferno$onTick(ServerLevel level, boolean spawnEnemies, CallbackInfo ci) {
        if (!level.getGameRules().get(GameRules.SPAWN_WANDERING_TRADERS)) return;

        countdown--;
        if (countdown > 0) return;
        countdown = START_DELAY;

        WanderingTraderData data = this.getData();
        List<ServerPlayer> players = level.players();
        boolean success = false;

        for (Player player : players) {
            if (this.spawn(level)) {
                success = true;
                break;
            }
        }

        if (success) {
            data.setSpawnChance(0);
        }
        else {
            int newChance = Math.min(100, data.spawnChance() + CHANCE_INCREMENT);
            data.setSpawnChance(newChance);
        }
        ci.cancel();
    }
}
