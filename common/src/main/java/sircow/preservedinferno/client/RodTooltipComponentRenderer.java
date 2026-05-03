package sircow.preservedinferno.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.components.ModComponents;
import sircow.preservedinferno.components.RodTooltipComponent;

import java.awt.*;

public class RodTooltipComponentRenderer implements ClientTooltipComponent {
    private final RodTooltipComponent component;
    private static final Identifier SLOT = Identifier.withDefaultNamespace("container/bundle/slot_background");
    private static final Identifier HOOK_SLOT_TEXTURE = Constants.id("container/slot/hook");
    private static final Identifier LINE_SLOT_TEXTURE = Constants.id("container/slot/line");
    private static final Identifier SINKER_SLOT_TEXTURE = Constants.id("container/slot/sinker");

    public RodTooltipComponentRenderer(RodTooltipComponent component) {
        this.component = component;
    }

    private enum RodPart {
        HOOK,
        LINE,
        SINKER
    }

    @Override
    public int getHeight(@NonNull Font font) {
        return 24;
    }

    @Override
    public int getWidth(@NonNull Font font) {
        return 72;
    }

    @Override
    public void extractImage(@NonNull Font font, int x, int y, int w, int h, @NonNull GuiGraphicsExtractor graphics) {
        renderSlot(graphics, component.hook(), x, y, RodPart.HOOK);
        renderSlot(graphics, component.line(), x + 24, y, RodPart.LINE);
        renderSlot(graphics, component.sinker(), x + 48, y, RodPart.SINKER);
    }

    private void renderSlot(GuiGraphicsExtractor graphics, ItemStack stack, int x, int y, RodPart part) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT, x, y, 24, 24);
        if (stack.isEmpty()) {
            Identifier texture = switch (part) {
                case HOOK -> HOOK_SLOT_TEXTURE;
                case LINE -> LINE_SLOT_TEXTURE;
                case SINKER -> SINKER_SLOT_TEXTURE;
            };
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, texture, x + 4, y + 4, 16, 16);
            return;
        }
        graphics.item(stack, x + 4, y + 4);
        graphics.itemDecorations(Minecraft.getInstance().font, stack, x + 4, y + 4);
        renderDurabilityBar(graphics, x, y, part);
    }

    private void renderDurabilityBar(GuiGraphicsExtractor graphics, int x, int y, RodPart part) {
        ItemStack rod = component.rod();
        int damage = getCurrentDurability(rod, part);
        int max = getMaxDurability(part);

        if (damage <= 0 && max > 0) return;
        if (max <= 0) return;

        float remainingRatio = Math.max(0.0F, (float) (max - damage) / (float) max);
        int width = Math.round(13.0F * remainingRatio);
        int color = getDurabilityColor(remainingRatio);
        int left = x + 5;
        int top = y + 18;

        graphics.fill(RenderPipelines.GUI, left, top, left + 13, top + 2, 0xFF000000);
        graphics.fill(RenderPipelines.GUI, left, top, left + width, top + 1, color | 0xFF000000);
    }

    private int getCurrentDurability(ItemStack rod, RodPart part) {
        return switch (part) {
            case HOOK -> rod.getOrDefault(ModComponents.HOOK_DURABILITY, -1);
            case LINE -> rod.getOrDefault(ModComponents.LINE_DURABILITY, -1);
            case SINKER -> rod.getOrDefault(ModComponents.SINKER_DURABILITY, -1);
        };
    }

    private int getDurabilityColor(float remainingRatio) {
        return Mth.hsvToRgb(remainingRatio / 3.0F, 1.0F, 1.0F);
    }

    private int getMaxDurability(RodPart part) {
        ItemStack stack = switch (part) {
            case HOOK -> component.hook();
            case LINE -> component.line();
            case SINKER -> component.sinker();
        };
        return stack.getMaxDamage();
    }
}
