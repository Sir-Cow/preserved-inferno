package sircow.preservedinferno.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.item.custom.PreservedShieldItem;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {
    @Inject(at = @At("HEAD"), method = "shouldInstantlyReplaceVisibleItem", cancellable = true)
    private void onShouldInstantlyReplaceVisibleItem(ItemStack oldItem, ItemStack newItem, CallbackInfoReturnable<Boolean> cir) {
        Minecraft client = Minecraft.getInstance();
        LocalPlayer player = client.player;

        if (newItem.getItem() instanceof PreservedShieldItem && player != null) {
            float swingProgress = player.getAttackAnim(client.getFrameTimeNs());

            if (swingProgress > 0.0F) {
                return;
            }
            cir.setReturnValue(true);
        }
    }
}
