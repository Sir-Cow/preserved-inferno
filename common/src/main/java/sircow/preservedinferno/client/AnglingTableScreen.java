package sircow.preservedinferno.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.screen.AnglingTableMenu;

public class AnglingTableScreen extends AbstractContainerScreen<AnglingTableMenu> {
    private static final ResourceLocation BG_LOCATION = Constants.id("textures/gui/container/angling_table_gui.png");
    private static final ResourceLocation ROD_SLOT_TEXTURE = Constants.id("container/slot/fishing_rod");
    private static final ResourceLocation HOOK_SLOT_TEXTURE = Constants.id("container/slot/hook");
    private static final ResourceLocation LINE_SLOT_TEXTURE = Constants.id("container/slot/line");
    private static final ResourceLocation SINKER_SLOT_TEXTURE = Constants.id("container/slot/sinker");

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
        this.renderEmptySlotIcons(context, this.leftPos, this.topPos);
    }

    @Override
    public void render(@NotNull GuiGraphics context, int mouseX, int mouseY, float delta) {
        renderBg(context, delta, mouseX, mouseY);
        super.render(context, mouseX, mouseY, delta);
        this.renderTooltip(context, mouseX, mouseY);
    }

    private void renderEmptySlotIcons(GuiGraphics context, int x, int y) {
        context.blitSprite(RenderType::guiTextured, ROD_SLOT_TEXTURE, x + 79, y + 17, 16, 16);
        context.blitSprite(RenderType::guiTextured, HOOK_SLOT_TEXTURE, x + 56, y + 51, 16, 16);
        context.blitSprite(RenderType::guiTextured, LINE_SLOT_TEXTURE , x + 79, y + 58, 16, 16);
        context.blitSprite(RenderType::guiTextured, SINKER_SLOT_TEXTURE , x + 102, y + 51, 16, 16);
    }
}
