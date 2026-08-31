package sircow.preservedinferno.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.item.ModItems;

@Mixin(targets = "net.minecraft.world.inventory.BrewingStandMenu$PotionSlot")
public class BrewingStandMenuPotionSlotMixin {
    @Inject(method = "mayPlaceItem", at = @At("HEAD"), cancellable = true)
    private static void pinferno$allowCustomBottlesInPotionSlot(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        if (itemStack.is(Items.HONEY_BOTTLE)
                || itemStack.is(ModItems.SPLASH_HONEY_BOTTLE.get())
                || itemStack.is(ModItems.LINGERING_HONEY_BOTTLE.get())
                || itemStack.is(ModItems.LAVA_BOTTLE.get())
                || itemStack.is(ModItems.SPLASH_LAVA_BOTTLE.get())
                || itemStack.is(ModItems.LINGERING_LAVA_BOTTLE.get())
                || itemStack.is(ModItems.MILK_BOTTLE.get())
                || itemStack.is(ModItems.SPLASH_MILK_BOTTLE.get())
                || itemStack.is(ModItems.LINGERING_MILK_BOTTLE.get())) {
            cir.setReturnValue(true);
        }
    }
}
