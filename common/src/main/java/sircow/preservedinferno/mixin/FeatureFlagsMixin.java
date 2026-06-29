package sircow.preservedinferno.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import sircow.preservedinferno.other.ExperimentsUtil;

@Mixin(FeatureFlags.class)
public class FeatureFlagsMixin {
    @Shadow @Final public static FeatureFlag VANILLA;
    @Shadow @Final public static FeatureFlag MINECART_IMPROVEMENTS;

    // enable minecraft experiments by default
    @ModifyExpressionValue(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/flag/FeatureFlagSet;of(Lnet/minecraft/world/flag/FeatureFlag;)Lnet/minecraft/world/flag/FeatureFlagSet;"))
    private static FeatureFlagSet pinferno$forceDefault(FeatureFlagSet original) {
        FeatureFlagSet featureFlagSet = FeatureFlagSet.of(VANILLA);
        featureFlagSet = featureFlagSet.join(FeatureFlagSet.of(MINECART_IMPROVEMENTS));
        ExperimentsUtil.addGlobalFeature("minecart_improvements");
        return featureFlagSet;
    }
}
