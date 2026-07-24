package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.ai.goal.EatBlockGoal;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EatBlockGoal.class)
public class EatBlockGoalMixin {
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/gamerules/GameRules;get(Lnet/minecraft/world/level/gamerules/GameRule;)Ljava/lang/Object;"))
    private Object pinferno$allowMobGriefing(GameRules gameRules, GameRule<Boolean> gameRule) {
        if (gameRule == GameRules.MOB_GRIEFING) return Boolean.TRUE;

        return gameRules.get(gameRule);
    }
}
