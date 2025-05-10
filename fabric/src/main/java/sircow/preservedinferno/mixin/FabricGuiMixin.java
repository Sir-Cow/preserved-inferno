package sircow.preservedinferno.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.item.custom.PreservedShieldItem;
import sircow.preservedinferno.other.ShieldStaminaHandler;

@Mixin(Gui.class)
public class FabricGuiMixin {
    // shield stamina
    @Unique private static final ResourceLocation SHIELD_BAR_BACKGROUND_SPRITE = Constants.id("textures/gui/sprites/hud/shield_bar_background.png");
    @Unique private static final ResourceLocation SHIELD_BAR_COOLDOWN_SPRITE = Constants.id("textures/gui/sprites/hud/shield_bar_cooldown.png");
    @Unique private static final ResourceLocation SHIELD_BAR_PROGRESS_SPRITE = Constants.id("textures/gui/sprites/hud/shield_bar_progress.png");

    @Inject(method = "renderExperienceBar", at = @At("TAIL"))
    private void preserved_inferno$renderShield(GuiGraphics guiGraphics, int x, CallbackInfo ci) {
        int barWidth = 182;
        int barHeight = 5;
        double percentageMultiplier;

        Minecraft client = Minecraft.getInstance();
        if (client.player != null) {
            ItemStack heldStack = client.player.getOffhandItem();
            x = client.getWindow().getGuiScaledWidth() / 2 - 91;
            int y = client.getWindow().getGuiScaledHeight() - 32 + 3;

            if (!heldStack.isEmpty() && heldStack.getItem() instanceof PreservedShieldItem) {
                guiGraphics.blit(RenderType::guiTextured, SHIELD_BAR_BACKGROUND_SPRITE, x, y, 0, 0, barWidth, barHeight, barWidth, barHeight);

                float currentStamina = ShieldStaminaHandler.getShieldStamina(heldStack, client.player);
                int maxStamina = ShieldStaminaHandler.getShieldMaxStamina(heldStack);

                if (maxStamina > 0) {
                    percentageMultiplier = (double) currentStamina / maxStamina;
                    int filledWidth = (int) (percentageMultiplier * barWidth);
                    guiGraphics.blit(RenderType::guiTextured, SHIELD_BAR_PROGRESS_SPRITE, x, y, 0, 0, filledWidth, barHeight, barWidth, barHeight);
                }

                if (ShieldStaminaHandler.isOnCooldown(client.player)) {
                    guiGraphics.blit(RenderType::guiTextured, SHIELD_BAR_COOLDOWN_SPRITE, x, y, 0, 0, barWidth, barHeight, barWidth, barHeight);
                }
            }
        }
    }
}
