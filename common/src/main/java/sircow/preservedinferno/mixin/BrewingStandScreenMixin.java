package sircow.preservedinferno.mixin;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.BrewingStandScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.BrewingStandMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.Constants;

@Mixin(BrewingStandScreen.class)
public abstract class BrewingStandScreenMixin extends AbstractContainerScreen<BrewingStandMenu> {
    @Unique private static final Identifier BREWING_GUIDE_LEFT = Constants.id("container/brewing_stand/brewing_guide_left");
    @Unique private static final Identifier BREWING_MODIFIERS_RIGHT = Constants.id("container/brewing_stand/brewing_modifiers_right");

    public BrewingStandScreenMixin(BrewingStandMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @ModifyConstant(method = "extractBackground", constant = @Constant(floatValue = 400.0F))
    private float pinferno$modifyBrewTime(float original) {
        return 160;
    }

    @Inject(method = "extractBackground", at = @At("TAIL"))
    private void pinferno$addBrewingGUIs(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BREWING_GUIDE_LEFT, this.leftPos - 112, this.topPos, 112, 176);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BREWING_MODIFIERS_RIGHT, this.leftPos + 180, this.topPos, 112, 64);
    }
}
