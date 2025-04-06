package sircow.preservedinferno.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.screen.PreservedCauldronMenu;

public class PreservedCauldronScreen extends AbstractContainerScreen<PreservedCauldronMenu> {
    private static final ResourceLocation BG_LOCATION = Constants.id("textures/gui/container/preserved_cauldron_gui.png");
    private static final ResourceLocation SOAK_PROGRESS_SPRITE = Constants.id("textures/gui/container/cauldron/soak_progress.png");
    private static final ResourceLocation WATER_SPRITE = Constants.id("textures/gui/container/cauldron/water.png");

    public PreservedCauldronScreen(PreservedCauldronMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;
        context.blit(RenderType::guiTextured, BG_LOCATION, x, y, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        renderProgressArrow(context, x, y);
        renderProgressWater(context, x, y);
    }

    private void renderProgressArrow(GuiGraphics context, int x, int y) {
        if (menu.isCrafting()) {
            int arrowX = x + 85;
            int arrowY = y + 34;

            context.blit(RenderType::guiTextured, SOAK_PROGRESS_SPRITE,
                    arrowX, arrowY,
                    0, 0,
                    8, menu.getScaledProgressArrow(),
                    8, 16
            );
        }
    }

    private void renderProgressWater(GuiGraphics context, int x, int y) {
        int waterX = x + 152;
        int waterY = y + 15 + (32 - menu.getScaledProgressWater());

        context.blit(RenderType::guiTextured, WATER_SPRITE,
                waterX, waterY,
                0, 32 - menu.getScaledProgressWater(),
                16, menu.getScaledProgressWater(),
                16, 32
        );
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        this.renderTooltip(context, mouseX, mouseY);
    }
}
