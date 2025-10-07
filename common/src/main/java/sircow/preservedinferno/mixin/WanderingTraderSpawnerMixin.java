package sircow.preservedinferno.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.WanderingTraderSpawner;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.storage.ServerLevelData;
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
    @Final @Shadow private ServerLevelData serverLevelData;
    @Shadow private int spawnChance;

    @Shadow
    protected abstract boolean spawn(ServerLevel serverLevel);

    @Unique private static final int START_DELAY = 24000;
    @Unique private static final int CHANCE_INCREMENT = 8;
    @Unique private int countdown = START_DELAY;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void preserved_inferno$onInit(ServerLevelData serverLevelData, CallbackInfo ci) {
        this.spawnChance = 0;
        this.countdown = START_DELAY;
        serverLevelData.setWanderingTraderSpawnChance(0);
        serverLevelData.setWanderingTraderSpawnDelay(START_DELAY);
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$onTick(ServerLevel level, boolean spawnEnemies, boolean spawnFriendlies, CallbackInfo ci) {
        if (!level.getGameRules().getBoolean(GameRules.RULE_DO_TRADER_SPAWNING)) return;

        countdown--;
        if (countdown > 0) return;
        countdown = START_DELAY;

        List<ServerPlayer> players = level.players();
        boolean success = false;

        for (Player player : players) {
            if (this.spawn(level)) {
                success = true;
                break;
            }
        }

        if (success) {
            this.spawnChance = 0;
            this.serverLevelData.setWanderingTraderSpawnChance(0);
        }
        else {
            this.spawnChance = Math.min(100, this.spawnChance + CHANCE_INCREMENT);
            this.serverLevelData.setWanderingTraderSpawnChance(this.spawnChance);
        }
        ci.cancel();
    }
}
