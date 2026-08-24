package sircow.preservedinferno.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.NetherWorldCarver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NetherWorldCarver.class)
public class NetherWorldCarverMixin {
    @ModifyExpressionValue(method = "carveBlock(Lnet/minecraft/world/level/levelgen/carver/CarvingContext;Lnet/minecraft/world/level/levelgen/carver/CaveCarverConfiguration;Lnet/minecraft/world/level/chunk/ChunkAccess;Ljava/util/function/Function;Lnet/minecraft/world/level/chunk/CarvingMask;Lnet/minecraft/core/BlockPos$MutableBlockPos;Lnet/minecraft/core/BlockPos$MutableBlockPos;Lnet/minecraft/world/level/levelgen/Aquifer;Lorg/apache/commons/lang3/mutable/MutableBoolean;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/carver/CarvingContext;getMinGenY()I"))
    private int pinferno$useConfiguredLavaLevel(int original, @Local(argsOnly = true, name = "context") CarvingContext context, @Local(argsOnly = true, name = "configuration") CaveCarverConfiguration configuration) {
        return configuration.lavaLevel.resolveY(context) - 31;
    }
}
