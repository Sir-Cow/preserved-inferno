package sircow.preservedinferno.mixin;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.item.ModItems;

@Mixin(GrindstoneMenu.class)
public class GrindstoneMenuMixin {
    @Shadow @Final private Container resultSlots;
    @Shadow @Final private Container repairSlots;

    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    private void pinferno$disableForbiddenItems(CallbackInfo ci) {
        ItemStack left = this.repairSlots.getItem(0);
        ItemStack right = this.repairSlots.getItem(1);

        if (left.is(ModItems.SCULK_INFUSION) || right.is(ModItems.SCULK_INFUSION) || left.is(ModItems.DREAMCATCHER) || right.is(ModItems.DREAMCATCHER)) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
            ((GrindstoneMenu)(Object) this).broadcastChanges();
            ci.cancel();
        }
    }
}
