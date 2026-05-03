package sircow.preservedinferno.other;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;
import java.util.List;

public class HeatAggroGoal extends Goal {
    private final ZombifiedPiglin piglin;
    private final double checkRadius = 80.0;
    private final double rangeMultiplier = 0.3;
    private final int minimumHeatAggro = 25;
    private ServerPlayer targetCandidate;
    private boolean shouldAlertOthers;
    private int lastHurtTimestamp;

    public HeatAggroGoal(ZombifiedPiglin piglin) {
        this.piglin = piglin;
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (piglin.getTarget() != null) return false;

        shouldAlertOthers = false;

        LivingEntity lastHurt = piglin.getLastHurtByMob();
        int timestamp = piglin.getLastHurtByMobTimestamp();

        if (lastHurt instanceof ServerPlayer player && timestamp != lastHurtTimestamp) {
            lastHurtTimestamp = timestamp;

            if (!player.isCreative() && !player.isSpectator()) {
                if (piglin.distanceToSqr(player) <= 20 * 20) {
                    targetCandidate = player;
                    shouldAlertOthers = false;
                    return true;
                }
            }
        }

        List<ServerPlayer> nearbyPlayers = piglin.level().getEntitiesOfClass(
                ServerPlayer.class,
                piglin.getBoundingBox().inflate(checkRadius),
                player -> !player.isSpectator() && !player.isCreative() && player.isAlive()
        );

        for (ServerPlayer player : nearbyPlayers) {
            int heat = ((HeatAccessor) player).preserved_inferno$getHeat();
            if (heat < minimumHeatAggro) continue;

            double heatRadius = rangeMultiplier * heat;

            if (piglin.distanceToSqr(player) <= heatRadius * heatRadius) {
                targetCandidate = player;
                shouldAlertOthers = true;
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
            if (shouldAlertOthers) alertOthers(targetCandidate);
            targetCandidate = null;
        }
    }

    private void alertOthers(ServerPlayer target) {
        double range = piglin.getAttributeValue(Attributes.FOLLOW_RANGE);
        AABB box = piglin.getBoundingBox().inflate(range, 10.0, range);
        List<ZombifiedPiglin> others = piglin.level().getEntitiesOfClass(ZombifiedPiglin.class, box, other -> other != piglin && other.getTarget() == null);

        for (ZombifiedPiglin other : others) {
            other.setTarget(target);
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (!(piglin.getTarget() instanceof ServerPlayer player)) return false;
        if (player.isCreative() || player.isSpectator()) return false;

        int heat = ((HeatAccessor) player).preserved_inferno$getHeat();
        double distSqr = piglin.distanceToSqr(player);

        return player.isAlive() && ((heat >= minimumHeatAggro && distSqr <= (rangeMultiplier * heat) * (rangeMultiplier * heat)) || distSqr <= 20 * 20);
    }

    @Override
    public void stop() {
        piglin.setTarget(null);
        piglin.setAggressive(false);
    }
}
