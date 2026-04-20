package sircow.preservedinferno.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.Constants;

public class PreservedCauldronScreen extends AbstractContainerScreen<PreservedCauldronMenu> {
    private static final Identifier BG_LOCATION = Constants.id("textures/gui/container/preserved_cauldron_gui.png");
    private static final Identifier SOAK_PROGRESS_SPRITE = Constants.id("textures/gui/container/cauldron/soak_progress.png");
    private static final Identifier WATER_SPRITE = Constants.id("textures/gui/container/cauldron/water.png");

    public PreservedCauldronScreen(PreservedCauldronMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        int x = this.leftPos;
        int y = this.topPos;
        graphics.blit(RenderPipelines.GUI_TEXTURED, BG_LOCATION, x, y, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        renderProgressArrow(graphics, x, y);
        renderProgressWater(graphics, x, y);
    }

    private void renderProgressArrow(GuiGraphicsExtractor graphics, int x, int y) {
        if (menu.isCrafting()) {
            int arrowX = x + 85;
            int arrowY = y + 34;

            graphics.blit(RenderPipelines.GUI_TEXTURED, SOAK_PROGRESS_SPRITE,
                    arrowX, arrowY,
                    0, 0,
                    8, menu.getScaledProgressArrow(),
                    8, 16
            );
        }
    }

    private void renderProgressWater(GuiGraphicsExtractor graphics, int x, int y) {
        int waterX = x + 152;
        int waterY = y + 15 + (32 - menu.getScaledProgressWater());

        graphics.blit(RenderPipelines.GUI_TEXTURED, WATER_SPRITE,
                waterX, waterY,
                0, 32 - menu.getScaledProgressWater(),
                16, menu.getScaledProgressWater(),
                16, 32
        );
    }
}
