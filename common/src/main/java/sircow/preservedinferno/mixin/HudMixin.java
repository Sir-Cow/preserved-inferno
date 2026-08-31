package sircow.preservedinferno.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.effect.ModEffects;
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

    @Shadow @Final private Minecraft minecraft;
    @Shadow private static Identifier HEART_VEHICLE_CONTAINER_SPRITE = Identifier.withDefaultNamespace("hud/heart/vehicle_container");
    @Shadow private static Identifier HEART_VEHICLE_FULL_SPRITE = Identifier.withDefaultNamespace("hud/heart/vehicle_full");
    @Shadow private static Identifier HEART_VEHICLE_HALF_SPRITE = Identifier.withDefaultNamespace("hud/heart/vehicle_half");

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
        int y = screenHeight - 39;

        if (player.isEyeInFluid(FluidTags.WATER) || player.getAirSupply() < player.getMaxAirSupply()) y -= 10;

        Entity vehicle = player.getVehicle();
        if (vehicle instanceof LivingEntity living && living.showVehicleHealth()) {
            float maxHealth = living.getMaxHealth();
            int hearts = Math.min((int) (maxHealth + 0.5F) / 2, 30);
            int rows = (int) Math.ceil(hearts / 10.0);

            y -= (rows - 1) * 10;
        }

        y -= 10;
        renderHeat(guiGraphics, x, y, 81, 9);
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

    @ModifyExpressionValue(method = "extractFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;hasEffect(Lnet/minecraft/core/Holder;)Z"))
    private boolean pinferno$useHungerForFumigated(boolean original) {
        return original || player.hasEffect(ModEffects.fumigatedHolder());
    }

    @Overwrite
    private void extractVehicleHealth(final GuiGraphicsExtractor graphics) {
        Player player = this.minecraft.player;
        if (player == null) return;

        Entity vehicle = player.getVehicle();
        if (!(vehicle instanceof LivingEntity vehicleWithHearts) || !vehicleWithHearts.showVehicleHealth()) return;

        int hearts = Math.min((int) (vehicleWithHearts.getMaxHealth() + 0.5F) / 2, 30);
        if (hearts == 0) return;

        float currentHealth = vehicleWithHearts.getHealth();

        Profiler.get().popPush("mountHealth");

        int y = graphics.guiHeight() - 39;
        int xRight = graphics.guiWidth() / 2 + 91;

        for (int baseHealth = 0; hearts > 0; baseHealth += 20) {
            int rowHearts = Math.min(hearts, 10);
            hearts -= rowHearts;

            for (int i = 0; i < rowHearts; i++) {
                int x = xRight - i * 8 - 9;

                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HEART_VEHICLE_CONTAINER_SPRITE, x, y, 9, 9);

                float heartStart = baseHealth + i * 2.0F;

                if (currentHealth >= heartStart + 2.0F) {
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HEART_VEHICLE_FULL_SPRITE, x, y, 9, 9);
                }
                else if (currentHealth > heartStart) {
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HEART_VEHICLE_HALF_SPRITE, x, y, 9, 9);
                }
            }

            y -= 10;
        }
    }
}
