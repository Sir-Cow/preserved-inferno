package sircow.preservedinferno.mixin;

import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.SculkCatalystBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SculkCatalystBlock.class)
public class SculkCatalystBlockMixin {
    @Shadow @Final @Mutable private IntProvider xpRange;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void pinferno$modifyXpRange(CallbackInfo ci) {
        this.xpRange = ConstantInt.of(50);
    }
}
