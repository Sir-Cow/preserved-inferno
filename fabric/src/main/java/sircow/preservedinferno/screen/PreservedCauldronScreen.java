package sircow.preservedinferno.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CyclingSlotBackground;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.Constants;

import java.util.List;

public class PreservedCauldronScreen extends AbstractContainerScreen<PreservedCauldronMenu> {
    private static final Identifier BG_LOCATION = Constants.id("textures/gui/container/preserved_cauldron_gui.png");
    private static final Identifier SOAK_PROGRESS_SPRITE = Constants.id("textures/gui/sprites/container/cauldron/soak_progress.png");
    private static final Identifier HONEY_SPRITE = Constants.id("textures/gui/sprites/container/cauldron/honey.png");
    private static final Identifier LAVA_SPRITE = Constants.id("textures/gui/sprites/container/cauldron/lava.png");
    private static final Identifier MILK_SPRITE = Constants.id("textures/gui/sprites/container/cauldron/milk.png");
    private static final Identifier SNOW_SPRITE = Constants.id("textures/gui/sprites/container/cauldron/snow.png");
    private static final Identifier WATER_SPRITE = Constants.id("textures/gui/sprites/container/cauldron/water.png");
    private static final Identifier BOTTLE_SPRITE = Constants.id("container/slot/bottle");
    private static final Identifier BUCKET_SPRITE = Constants.id("container/slot/bucket");
    private static final List<Identifier> FUEL_ICONS = List.of(
            BOTTLE_SPRITE, BUCKET_SPRITE
    );
    private final CyclingSlotBackground fuelIcon = new CyclingSlotBackground(1);

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
        this.fuelIcon.extractRenderState(this.menu, graphics, a, x, y);
        renderProgressArrow(graphics, x, y);
        renderProgressFluid(graphics, x, y);
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.fuelIcon.tick(FUEL_ICONS);
    }

    private void renderProgressArrow(GuiGraphicsExtractor graphics, int x, int y) {
        if (menu.isCrafting()) {
            int arrowX = x + 85;
            int arrowY = y + 34;
            graphics.blit(RenderPipelines.GUI_TEXTURED, SOAK_PROGRESS_SPRITE, arrowX, arrowY, 0, 0, 8, menu.getScaledProgressArrow(), 8, 16);
        }
    }

    private void renderProgressFluid(GuiGraphicsExtractor graphics, int x, int y) {
        int fluid = menu.getFluidAmount();
        if (fluid <= 0) return;

        Identifier sprite = switch (menu.getFluid()) {
            case HONEY -> HONEY_SPRITE;
            case LAVA -> LAVA_SPRITE;
            case MILK -> MILK_SPRITE;
            case SNOW -> SNOW_SPRITE;
            default -> WATER_SPRITE;
        };

        int maxHeight = 32;
        int height;

        if (fluid == 1) height = 1;
        else height = Math.min(fluid / 2, maxHeight);

        int fluidX = x + 152;
        int fluidY = y + 15 + (maxHeight - height);

        graphics.blit(RenderPipelines.GUI_TEXTURED, sprite, fluidX, fluidY, 0, maxHeight - height, 16, height, 16, maxHeight);
    }
}
