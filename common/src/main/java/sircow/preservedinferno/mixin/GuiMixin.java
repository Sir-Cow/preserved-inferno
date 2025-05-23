package sircow.preservedinferno.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.item.custom.PreservedShieldItem;
import sircow.preservedinferno.other.HeatAccessor;
import sircow.preservedinferno.other.ShieldStaminaHandler;

@Mixin(Gui.class)
public class GuiMixin {
    @Unique private static final ResourceLocation HEAT_EMPTY_SPRITE = Constants.id("textures/gui/sprites/hud/heat_bar_empty.png");
    @Unique private static final ResourceLocation HEAT_FILLED_SPRITE = Constants.id("textures/gui/sprites/hud/heat_bar_filled.png");
    @Unique private static final ResourceLocation HEAT_100_SPRITE = Constants.id("textures/gui/sprites/hud/heat_100.png");
    @Unique private static final ResourceLocation HEAT_OVER_100_SPRITE = Constants.id("textures/gui/sprites/hud/heat_over_100.png");
    @Unique private static final ResourceLocation NEW_ARMOUR_BAR_EMPTY = Constants.id("hud/armor_bar_empty");
    @Unique private static final ResourceLocation NEW_ARMOUR_BAR_FILLED = Constants.id("hud/armor_bar_filled");
    @Unique private static final ResourceLocation SHIELD_BAR_BACKGROUND_SPRITE = Constants.id("textures/gui/sprites/hud/shield_bar_background.png");
    @Unique private static final ResourceLocation SHIELD_BAR_COOLDOWN_SPRITE = Constants.id("textures/gui/sprites/hud/shield_bar_cooldown.png");
    @Unique private static final ResourceLocation SHIELD_BAR_PROGRESS_SPRITE = Constants.id("textures/gui/sprites/hud/shield_bar_progress.png");

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

    // replace vanilla with custom armour bar
    @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
    private static void preserved_inferno$modifyArmourBar(GuiGraphics guiGraphics, Player player, int y, int heartRows, int height, int x, CallbackInfo ci) {
        double maxArmourVal = 100.0F; // its actually 150 but this is a cap for only the bar
        int armourVal = player.getArmorValue();
        double percentageMultiplier;
        int barWidth = 81;
        int barHeight = 9;

        int j = y - (heartRows - 1) * height - 10;

        if (armourVal > 0) {
            guiGraphics.blitSprite(RenderType::guiTextured, NEW_ARMOUR_BAR_EMPTY, x, j, barWidth, barHeight);

            if (armourVal >= 100) {
                percentageMultiplier = 1.0F;
            }
            else {
                percentageMultiplier = armourVal / maxArmourVal;
            }

            guiGraphics.blitSprite(RenderType::guiTextured, NEW_ARMOUR_BAR_FILLED, barWidth, barHeight, 0, 0, x, j, (int)(percentageMultiplier * barWidth), barHeight);
        }
        ci.cancel();
    }

    // shield stamina
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
                    if (currentStamina <= maxStamina) {
                        guiGraphics.blit(RenderType::guiTextured, SHIELD_BAR_PROGRESS_SPRITE, x, y, 0, 0, filledWidth, barHeight, barWidth, barHeight);
                    }
                }

                if (ShieldStaminaHandler.isOnCooldown(client.player)) {
                    guiGraphics.blit(RenderType::guiTextured, SHIELD_BAR_COOLDOWN_SPRITE, x, y, 0, 0, barWidth, barHeight, barWidth, barHeight);
                }
            }
        }
    }
}
