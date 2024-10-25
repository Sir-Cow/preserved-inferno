package sircow.placeholder.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import sircow.placeholder.Placeholder;

public class NewCauldronBlockScreen extends HandledScreen<NewCauldronBlockScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of(Placeholder.MOD_ID, "textures/gui/new_cauldron_gui.png");

    public NewCauldronBlockScreen(NewCauldronBlockScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(RenderLayer::getGuiTextured, TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight, 256, 256);

        renderProgressArrow(context, x, y);
        renderProgressWater(context, x, y);
    }

    private void renderProgressArrow(DrawContext context, int x, int y) {
        if(handler.isCrafting()) {
            context.drawTexture(RenderLayer::getGuiTextured, TEXTURE, x + 85, y + 34, 176, 0, 8, handler.getScaledProgressArrow(), 256, 256);
        }
    }

    private void renderProgressWater(DrawContext context, int x, int y) {
        context.drawTexture(RenderLayer::getGuiTextured, TEXTURE, x + 152, y + 15 + (32 - handler.getScaledProgressWater()), 176, 32 + (32 - handler.getScaledProgressWater()), 16, handler.getScaledProgressWater(), 256, 256);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
