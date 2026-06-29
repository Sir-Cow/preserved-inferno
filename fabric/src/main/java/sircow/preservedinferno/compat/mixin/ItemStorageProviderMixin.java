package sircow.preservedinferno.compat.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.block.entity.PreservedCauldronBlockEntity;
import snownee.jade.addon.universal.ItemStorageProvider;
import snownee.jade.api.Accessor;
import snownee.jade.api.BlockAccessor;

@Mixin(ItemStorageProvider.class)
public class ItemStorageProviderMixin {
    @Inject(method = "shouldRequestData", at = @At("HEAD"), cancellable = true)
    private void pinferno$disableItems(Accessor<?> accessor, CallbackInfoReturnable<Boolean> cir) {
        if (accessor instanceof BlockAccessor blockAccessor && blockAccessor.getBlockEntity() instanceof PreservedCauldronBlockEntity) {
            cir.setReturnValue(false);
        }
    }
}
