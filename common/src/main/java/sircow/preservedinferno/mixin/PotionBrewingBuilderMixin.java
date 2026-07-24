package sircow.preservedinferno.mixin;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PotionBrewing.Builder.class)
public class PotionBrewingBuilderMixin {
    @Inject(method = "expectPotion", at = @At("HEAD"), cancellable = true)
    private static void pinferno$allowCustomContainers(Item from, CallbackInfo ci) {
        ci.cancel();
    }
}
