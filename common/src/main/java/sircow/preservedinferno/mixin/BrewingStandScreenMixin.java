package sircow.preservedinferno.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.BrewingStandScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
    @Unique private static final ResourceLocation BREWING_GUIDE_LEFT = Constants.id("container/brewing_stand/brewing_guide_left");
    @Unique private static final ResourceLocation BREWING_MODIFIERS_RIGHT = Constants.id("container/brewing_stand/brewing_modifiers_right");

    public BrewingStandScreenMixin(BrewingStandMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @ModifyConstant(method = "renderBg", constant = @Constant(floatValue = 400.0F))
    private float preserved_inferno$modifyBrewTime(float original) {
        return 160;
    }

    @Inject(method = "renderBg", at = @At("TAIL"))
    private void preserved_inferno$addBrewingGUIs(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, BREWING_GUIDE_LEFT, this.leftPos - 112, this.topPos, 112, 176);
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, BREWING_MODIFIERS_RIGHT, this.leftPos + 180, this.topPos, 112, 64);
    }
}
