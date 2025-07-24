package sircow.preservedinferno.other;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.ZombifiedPiglin;

import java.util.EnumSet;
import java.util.List;

public class HeatAggroGoal extends Goal {
    private final ZombifiedPiglin piglin;
    private final double checkRadius = 80.0;
    private ServerPlayer targetCandidate = null;

    public HeatAggroGoal(ZombifiedPiglin piglin) {
        this.piglin = piglin;
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (piglin.isAngry() || piglin.getTarget() != null) {
            return false;
        }

        List<ServerPlayer> nearbyPlayers = piglin.level().getEntitiesOfClass(
                ServerPlayer.class,
                piglin.getBoundingBox().inflate(checkRadius),
                player -> !player.isSpectator() && player.isAlive()
        );

        for (ServerPlayer player : nearbyPlayers) {
            int heat = ((HeatAccessor) player).preserved_inferno$getHeat();
            double heatRadius = 0.4 * heat;
            double distSqr = piglin.distanceToSqr(player);

            if (distSqr <= heatRadius * heatRadius) {
                targetCandidate = player;
                return true;
            }
        }
        return false;
    }

    @Override
    public void start() {
        if (targetCandidate != null) {
            piglin.setTarget(targetCandidate);
            piglin.setAggressive(true);
            targetCandidate = null;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (!(piglin.getTarget() instanceof ServerPlayer player)) return false;

        int heat = ((HeatAccessor) player).preserved_inferno$getHeat();
        double heatRadius = 0.4 * heat;
        double distSqr = piglin.distanceToSqr(player);

        return player.isAlive() && heat > 0 && distSqr <= heatRadius * heatRadius;
    }

    @Override
    public void stop() {
        piglin.setTarget(null);
        piglin.setAggressive(false);
    }
}
