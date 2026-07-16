package sircow.preservedinferno.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NearestAttackableTargetGoal.class)
public abstract class NearestAttackableTargetGoalMixin extends TargetGoal {
    private NearestAttackableTargetGoalMixin(Mob mob, boolean mustSee, boolean mustReach) {
        super(mob, mustSee, mustReach);
    }

    @ModifyReturnValue(method = "getTargetConditions", at = @At("RETURN"))
    private TargetingConditions pinferno$modifyMobTargetConditions(TargetingConditions original) {
        TargetingConditions vanillaConditions = original.copy().selector(null);

        return original.selector((LivingEntity candidate, ServerLevel level) -> {
            if (this.mob instanceof Creeper) {
                if (candidate instanceof Player player && player.isCrouching()) return player.distanceTo(this.mob) <= 20.0D;
            }

            return vanillaConditions.test(level, this.mob, candidate);
        });
    }
}
