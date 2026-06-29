package sircow.preservedinferno.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.ServerPacksSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import sircow.preservedinferno.other.ExperimentsUtil;

@Mixin(ServerPacksSource.class)
public class ServerPacksSourceMixin {
    @ModifyExpressionValue(method = "createBuiltInPackLocation", at = @At(value = "FIELD", target = "Lnet/minecraft/server/packs/repository/PackSource;FEATURE:Lnet/minecraft/server/packs/repository/PackSource;"))
    private static PackSource pinferno$changePackSource(PackSource original, @Local(argsOnly = true, name = "id") String id) {
        if (ExperimentsUtil.getGlobalFeatures().contains(id)) return PackSource.BUILT_IN;
        return original;
    }
}
