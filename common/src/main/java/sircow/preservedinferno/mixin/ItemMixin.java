package sircow.preservedinferno.mixin;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.components.RodComponentResolver;
import sircow.preservedinferno.components.RodTooltipComponent;

import java.util.Optional;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "getTooltipImage", at = @At("HEAD"), cancellable = true)
    private void pinferno$fishingRodTooltip(ItemStack stack, CallbackInfoReturnable<Optional<TooltipComponent>> cir) {
        if (!(stack.getItem() instanceof FishingRodItem)) return;

        ItemStack hook = RodComponentResolver.resolveHook(stack);
        ItemStack line = RodComponentResolver.resolveLine(stack);
        ItemStack sinker = RodComponentResolver.resolveSinker(stack);

        cir.setReturnValue(Optional.of(new RodTooltipComponent(stack, hook, line, sinker)));
    }
}
