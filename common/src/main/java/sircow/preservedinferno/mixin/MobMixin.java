package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Mob.class)
public class MobMixin {
    @Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/gamerules/GameRules;get(Lnet/minecraft/world/level/gamerules/GameRule;)Ljava/lang/Object;"))
    private <T> Object preserved_inferno$piglinIgnoreMobGriefing(GameRules instance, GameRule<T> gameRule) {
        if (gameRule == GameRules.MOB_GRIEFING) {
            if ((Object) this instanceof Piglin) return true;
        }
        return instance.get(gameRule);
    }
}
