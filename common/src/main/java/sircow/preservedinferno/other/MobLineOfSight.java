package sircow.preservedinferno.other;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class MobLineOfSight {
    private static final double MONSTER_DETECTION_RADIUS = 32.0;

    public static boolean hasMonsterLineOfSight(Level level, BlockPos bedPos) {
        AABB searchBox = new AABB(
                bedPos.getX() - MONSTER_DETECTION_RADIUS, bedPos.getY() - MONSTER_DETECTION_RADIUS, bedPos.getZ() - MONSTER_DETECTION_RADIUS,
                bedPos.getX() + MONSTER_DETECTION_RADIUS, bedPos.getY() + MONSTER_DETECTION_RADIUS, bedPos.getZ() + MONSTER_DETECTION_RADIUS
        );
        List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, searchBox, (entity) -> entity instanceof Monster);
        Vec3 bedVec = Vec3.atCenterOf(bedPos).add(0, 0.5, 0);

        for (LivingEntity entity : nearbyEntities) {
            if (entity instanceof Monster) {
                Vec3 monsterEyePos = entity.getEyePosition();
                HitResult hitResult = level.clip(
                        new ClipContext(
                                bedVec,
                                monsterEyePos,
                                ClipContext.Block.VISUAL,
                                ClipContext.Fluid.NONE,
                                entity
                        )
                );

                if (hitResult.getType() == HitResult.Type.MISS) {
                    return true;
                }
            }
        }
        return false;
    }
}
