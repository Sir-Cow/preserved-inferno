package sircow.preservedinferno.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.other.HeatAccessor;

@Mixin(Gui.class)
public class GuiMixin {
    @Unique private static final ResourceLocation HEAT_EMPTY_SPRITE = Constants.id("textures/gui/sprites/hud/heat_bar_empty.png");
    @Unique private static final ResourceLocation HEAT_FILLED_SPRITE = Constants.id("textures/gui/sprites/hud/heat_bar_filled.png");
    @Unique private static final ResourceLocation HEAT_100_SPRITE = Constants.id("textures/gui/sprites/hud/heat_100.png");
    @Unique private static final ResourceLocation HEAT_OVER_100_SPRITE = Constants.id("textures/gui/sprites/hud/heat_over_100.png");

    @Unique private int heat;
    @Unique private Player player;

    @Inject(method = "render", at = @At("HEAD"))
    public void preserved_inferno$renderHeat(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            this.player = minecraft.player;
            this.heat = ((HeatAccessor) player).preserved_inferno$getHeat();
        }
    }

    @Inject(method = "renderPlayerHealth",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Gui;renderFood(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/entity/player/Player;II)V",
                    shift = At.Shift.BEFORE
            )
    )
    public void preserved_inferno$renderHeatBar(GuiGraphics guiGraphics, CallbackInfo ci) {
        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();
        int x = screenWidth / 2 + 91;
        int baseY = screenHeight - 39;
        int heatBarY = baseY - 10;
        int barWidth = 81;
        int barHeight = 9;

        int maxAir = player.getMaxAirSupply();
        if (player.getAirSupply() < maxAir) {
            heatBarY -= 10;
        }

        this.renderHeat(guiGraphics, x, heatBarY, barWidth, barHeight);
    }

    @Unique
    private void renderHeat(GuiGraphics guiGraphics, int x, int y, int barWidth, int barHeight) {
        double maxHeatVal = 100.0F; // Cap for the bar
        int heatVal = this.heat;
        double percentageMultiplier;

        if (heatVal > 0) {
            guiGraphics.blit(RenderType::guiTextured, HEAT_EMPTY_SPRITE, x - barWidth, y, 0, 0, barWidth, barHeight, barWidth, barHeight);

            if (heatVal >= 100) {
                barWidth = 83;
                guiGraphics.blit(RenderType::guiTextured, HEAT_100_SPRITE, (x - barWidth) + 1, y, 0, 0, barWidth, barHeight, barWidth, barHeight);
                if (heatVal > 100) {
                    percentageMultiplier = (heatVal - 100) / maxHeatVal;
                    int filledWidth = (int)(percentageMultiplier * barWidth);
                    guiGraphics.blit(RenderType::guiTextured, HEAT_OVER_100_SPRITE, ((x - barWidth) + (barWidth - filledWidth)) + 1, y, barWidth - filledWidth, 0, filledWidth, barHeight, barWidth, barHeight);
                }
            }
            else {
                percentageMultiplier = heatVal / maxHeatVal;
                int filledWidth = (int)(percentageMultiplier * barWidth);
                guiGraphics.blit(RenderType::guiTextured, HEAT_FILLED_SPRITE, x - barWidth, y, 0, 0, filledWidth, barHeight, barWidth, barHeight);
            }
        }
    }
}
