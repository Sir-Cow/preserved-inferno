package sircow.preservedinferno.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.Monster;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Monster.class)
public class MonsterMixin {
    @ModifyExpressionValue(method = "checkSurfaceMonstersSpawnRules", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ServerLevelAccessor;canSeeSky(Lnet/minecraft/core/BlockPos;)Z"))
    private static boolean pinferno$ignoreSkyCheck(boolean original, @Local(argsOnly = true, name = "type") EntityType<?> type) {
        if (type == EntityTypes.HUSK || type == EntityTypes.PARCHED) return true;

        return original;
    }
}
