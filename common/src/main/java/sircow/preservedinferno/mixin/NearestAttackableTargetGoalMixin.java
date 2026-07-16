package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NearestAttackableTargetGoal.class)
public class NearestAttackableTargetGoalMixin<T extends LivingEntity> {
    @Shadow @Final protected TargetingConditions targetConditions;

    @Inject(method = "<init>(Lnet/minecraft/world/entity/Mob;Ljava/lang/Class;ZLnet/minecraft/world/entity/ai/targeting/TargetingConditions$Selector;)V", at = @At("TAIL"))
    private void pinferno$modifyMobTargetConditions(Mob mob, Class<T> targetType, boolean mustSee, TargetingConditions.Selector selector, CallbackInfo ci) {
        if (!(mob instanceof Creeper)) return;

        TargetingConditions.Selector originalSelector = selector;

        this.targetConditions.selector((candidate, level) -> {
            if (originalSelector != null && !originalSelector.test(candidate, level)) return false;

            if (candidate instanceof Player player && player.isCrouching()) return player.distanceTo(mob) <= 20.0D;

            return true;
        });
    }
}
