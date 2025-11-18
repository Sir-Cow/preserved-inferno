package sircow.preservedinferno.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.recipebook.RecipeButton;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeButton.class)
public abstract class RecipeButtonMixin extends AbstractWidget {
    public RecipeButtonMixin(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @Inject(method = "renderWidget", at = @At("TAIL"))
    private void preserved_inferno$renderDurabilityBar(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        ItemStack stack = ((RecipeButton)(Object)this).getDisplayStack();

        if (!stack.isEmpty() && stack.isDamaged()) {
            int x = this.getX();
            int y = this.getY();

            guiGraphics.fill(RenderPipelines.GUI, x + 6, y + 17, x + 6 + 13, y + 17 + 2, -16777216);
            guiGraphics.fill(RenderPipelines.GUI, x + 6, y + 17, x + 6 + stack.getBarWidth(), y + 17 + 2, ARGB.opaque(stack.getBarColor()));
        }
    }
}
