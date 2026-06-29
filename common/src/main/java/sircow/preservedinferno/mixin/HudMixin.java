package sircow.preservedinferno.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.item.custom.PreservedShieldItem;
import sircow.preservedinferno.other.HeatAccessor;
import sircow.preservedinferno.other.ShieldStaminaHandler;

@Mixin(Hud.class)
public class HudMixin {
    @Unique private static final Identifier HEAT_EMPTY_SPRITE = Constants.id("textures/gui/sprites/hud/heat_bar_empty.png");
    @Unique private static final Identifier HEAT_FILLED_SPRITE = Constants.id("textures/gui/sprites/hud/heat_bar_filled.png");
    @Unique private static final Identifier HEAT_100_SPRITE = Constants.id("textures/gui/sprites/hud/heat_100.png");
    @Unique private static final Identifier HEAT_OVER_100_SPRITE = Constants.id("textures/gui/sprites/hud/heat_over_100.png");
    @Unique private static final Identifier NEW_ARMOUR_BAR_EMPTY = Constants.id("hud/armor_bar_empty");
    @Unique private static final Identifier NEW_ARMOUR_BAR_FILLED = Constants.id("hud/armor_bar_filled");
    @Unique private static final Identifier SHIELD_BAR_BACKGROUND_SPRITE = Constants.id("textures/gui/sprites/hud/shield_bar_background.png");
    @Unique private static final Identifier SHIELD_BAR_COOLDOWN_SPRITE = Constants.id("textures/gui/sprites/hud/shield_bar_cooldown.png");
    @Unique private static final Identifier SHIELD_BAR_PROGRESS_SPRITE = Constants.id("textures/gui/sprites/hud/shield_bar_progress.png");

    @Unique private int heat;
    @Unique private Player player;

    @Inject(method = "extractRenderState", at = @At("HEAD"))
    public void pinferno$renderHeat(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            this.player = minecraft.player;
            this.heat = ((HeatAccessor) player).pinferno$getHeat();
        }
    }

    @Inject(method = "extractPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Hud;extractAirBubbles(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/world/entity/player/Player;III)V", shift = At.Shift.AFTER))
    public void pinferno$renderHeatBar(GuiGraphicsExtractor guiGraphics, CallbackInfo ci) {
        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();
        int x = screenWidth / 2 + 91;
        int baseY = screenHeight - 39;
        int heatBarY = baseY - 10;
        int barWidth = 81;
        int barHeight = 9;
        boolean hasAirBar = player.isEyeInFluid(FluidTags.WATER) || player.getAirSupply() < player.getMaxAirSupply();

        if (hasAirBar) heatBarY -= 10;
        this.renderHeat(guiGraphics, x, heatBarY, barWidth, barHeight);
    }

    @Unique
    private void renderHeat(GuiGraphicsExtractor guiGraphics, int x, int y, int barWidth, int barHeight) {
        double maxHeatVal = 100.0F; // Cap for the bar
        int heatVal = this.heat;
        double percentageMultiplier;

        if (heatVal > 0) {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, HEAT_EMPTY_SPRITE, x - barWidth, y, 0, 0, barWidth, barHeight, barWidth, barHeight);

            if (heatVal >= 100) {
                barWidth = 83;
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED, HEAT_100_SPRITE, (x - barWidth) + 1, y, 0, 0, barWidth, barHeight, barWidth, barHeight);
                if (heatVal > 100) {
                    percentageMultiplier = (heatVal - 100) / maxHeatVal;
                    int filledWidth = (int)(percentageMultiplier * barWidth);
                    guiGraphics.blit(RenderPipelines.GUI_TEXTURED, HEAT_OVER_100_SPRITE, ((x - barWidth) + (barWidth - filledWidth)) + 1, y, barWidth - filledWidth, 0, filledWidth, barHeight, barWidth, barHeight);
                }
            }
            else {
                percentageMultiplier = heatVal / maxHeatVal;
                int filledWidth = (int)(percentageMultiplier * barWidth);
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED, HEAT_FILLED_SPRITE, x - barWidth, y, 0, 0, filledWidth, barHeight, barWidth, barHeight);
            }
        }
    }

    // replace vanilla with custom armour bar
    @Inject(method = "extractArmor", at = @At("HEAD"), cancellable = true)
    private static void pinferno$modifyArmourBar(GuiGraphicsExtractor guiGraphics, Player player, int y, int heartRows, int height, int x, CallbackInfo ci) {
        double maxArmourVal = 100.0F; // its actually 150 but this is a cap for only the bar
        int armourVal = player.getArmorValue();
        double percentageMultiplier;
        int barWidth = 81;
        int barHeight = 9;

        int j = y - (heartRows - 1) * height - 10;

        if (armourVal > 0) {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, NEW_ARMOUR_BAR_EMPTY, x, j, barWidth, barHeight);

            if (armourVal >= 100) percentageMultiplier = 1.0F;
            else percentageMultiplier = armourVal / maxArmourVal;

            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, NEW_ARMOUR_BAR_FILLED, barWidth, barHeight, 0, 0, x, j, (int)(percentageMultiplier * barWidth), barHeight);
        }
        ci.cancel();
    }

    // shield stamina
    @Inject(method = "extractHotbarAndDecorations", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/contextualbar/ContextualBar;extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/DeltaTracker;)V", shift = At.Shift.AFTER))
    private void pinferno$renderShieldBar(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        int barWidth = 182;
        int barHeight = 5;
        double percentageMultiplier;

        Minecraft client = Minecraft.getInstance();
        if (client.player != null) {
            ItemStack heldStack = client.player.getOffhandItem();
            int x = client.getWindow().getGuiScaledWidth() / 2 - 91;
            int y = client.getWindow().getGuiScaledHeight() - 32 + 3;

            if (!heldStack.isEmpty() && heldStack.getItem() instanceof PreservedShieldItem && !player.isCreative() && !player.isSpectator()) {
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SHIELD_BAR_BACKGROUND_SPRITE, x, y, 0, 0, barWidth, barHeight, barWidth, barHeight);

                float currentStamina = ShieldStaminaHandler.getShieldStamina(heldStack, client.player);
                int maxStamina = ShieldStaminaHandler.getShieldMaxStamina(heldStack);

                if (maxStamina > 0) {
                    percentageMultiplier = (double) currentStamina / maxStamina;
                    int filledWidth = (int) (percentageMultiplier * barWidth);
                    if (currentStamina <= maxStamina) guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SHIELD_BAR_PROGRESS_SPRITE, x, y, 0, 0, filledWidth, barHeight, barWidth, barHeight);
                }

                if (ShieldStaminaHandler.isOnCooldown(client.player)) guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SHIELD_BAR_COOLDOWN_SPRITE, x, y, 0, 0, barWidth, barHeight, barWidth, barHeight);
            }
        }
    }

    // extend sleep overlay time
    @ModifyConstant(method = "extractSleepOverlay", constant = @Constant(floatValue = 100.0F))
    private float pinferno$modifyFloatValue(float original) {
        return 200.0F;
    }

    @Redirect(method = "extractHotbarAndDecorations", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/contextualbar/ContextualBar;extractExperienceLevel(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/gui/Font;I)V"))
    private void pinferno$moveXPNumber(GuiGraphicsExtractor guiGraphics, Font font, int experienceLevel) {
        Component component = Component.translatable("gui.experience.level", experienceLevel);
        int i = (guiGraphics.guiWidth() - font.width(component)) / 2;
        int j = guiGraphics.guiHeight() - 24 - 9 - 5;

        guiGraphics.text(font, component, i + 1, j, -16777216, false);
        guiGraphics.text(font, component, i - 1, j, -16777216, false);
        guiGraphics.text(font, component, i, j + 1, -16777216, false);
        guiGraphics.text(font, component, i, j - 1, -16777216, false);
        guiGraphics.text(font, component, i, j, -8323296, false);
    }
}
