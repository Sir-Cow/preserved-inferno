package sircow.preservedinferno.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.monster.skeleton.Stray;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Stray.class)
public class StrayMixin {
    @ModifyExpressionValue(method = "checkStraySpawnRules", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ServerLevelAccessor;canSeeSky(Lnet/minecraft/core/BlockPos;)Z"))
    private static boolean pinferno$ignoreSkyCheck(boolean original) {
        return true;
    }
}
