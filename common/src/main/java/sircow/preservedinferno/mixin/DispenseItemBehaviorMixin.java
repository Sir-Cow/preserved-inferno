package sircow.preservedinferno.mixin;

import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.level.block.DispenserBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.item.ModItems;

@Mixin(DispenseItemBehavior.class)
public interface DispenseItemBehaviorMixin {
    @Inject(method = "bootStrap", at = @At("TAIL"))
    private static void pinferno$newDispenserBehavior(CallbackInfo ci) {
        DispenserBlock.registerProjectileBehavior(ModItems.DYNAMITE.get());
    }
}
