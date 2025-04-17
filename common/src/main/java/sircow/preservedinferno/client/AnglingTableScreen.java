package sircow.preservedinferno.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.screen.AnglingTableMenu;

public class AnglingTableScreen extends AbstractContainerScreen<AnglingTableMenu> {
    private static final ResourceLocation BG_LOCATION = Constants.id("textures/gui/container/angling_table_gui.png");

    public AnglingTableScreen(AnglingTableMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(GuiGraphics context, float partialTick, int mouseX, int mouseY) {
        int i = this.leftPos;
        int j = this.topPos;
        context.blit(RenderType::guiTextured, BG_LOCATION, i, j, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        renderBg(context, delta, mouseX, mouseY);
        super.render(context, mouseX, mouseY, delta);
        this.renderTooltip(context, mouseX, mouseY);
    }
}
