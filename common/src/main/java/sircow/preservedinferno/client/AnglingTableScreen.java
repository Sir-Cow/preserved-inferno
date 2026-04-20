package sircow.preservedinferno.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.screen.AnglingTableMenu;

public class AnglingTableScreen extends AbstractContainerScreen<AnglingTableMenu> {
    private static final Identifier BG_LOCATION = Constants.id("textures/gui/container/angling_table_gui.png");
    private static final Identifier ROD_SLOT_TEXTURE = Constants.id("container/slot/fishing_rod");
    private static final Identifier HOOK_SLOT_TEXTURE = Constants.id("container/slot/hook");
    private static final Identifier LINE_SLOT_TEXTURE = Constants.id("container/slot/line");
    private static final Identifier SINKER_SLOT_TEXTURE = Constants.id("container/slot/sinker");

    public AnglingTableScreen(AnglingTableMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        int i = this.leftPos;
        int j = this.topPos;
        graphics.blit(RenderPipelines.GUI_TEXTURED, BG_LOCATION, i, j, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
        this.renderEmptySlotIcons(graphics, this.leftPos, this.topPos);
    }

    private void renderEmptySlotIcons(GuiGraphicsExtractor graphics, int x, int y) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ROD_SLOT_TEXTURE, x + 79, y + 17, 16, 16);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOOK_SLOT_TEXTURE, x + 56, y + 51, 16, 16);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, LINE_SLOT_TEXTURE , x + 79, y + 58, 16, 16);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SINKER_SLOT_TEXTURE , x + 102, y + 51, 16, 16);
    }
}
