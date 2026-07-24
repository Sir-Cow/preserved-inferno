package sircow.preservedinferno.compat.mixin;

import betteradvancements.common.gui.BetterAdvancementsScreen;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementNode;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.advancement.ModAdvancements;
import sircow.preservedinferno.mixin.ClientAdvancementsAccessor;

import java.util.Map;

@Mixin(BetterAdvancementsScreen.class)
public class BetterAdvancementsScreenMixin extends Screen {
    @Shadow @Final private ClientAdvancements clientAdvancements;
    @Shadow protected int internalWidth;
    @Shadow protected int internalHeight;

    protected BetterAdvancementsScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void pinferno$renderPoints(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        int left = 30 + (this.width - this.internalWidth) / 2;
        int right = this.internalWidth - left;
        int top = 40 + (this.height - this.internalHeight) / 2;

        int playerPoints = ModAdvancements.getPlayerPoints(mc.player.getUUID());
        Component pointsText = Component.translatable("advancements.pinferno.menu.mastery_points", playerPoints);

        int x = right - 8 - mc.font.width(pointsText);
        int y = top + 6;

        guiGraphics.text(mc.font, pointsText, x, y, -12566464, false);
    }

    @Inject(method = "onAddAdvancementRoot", at = @At("HEAD"), cancellable = true)
    private void pinferno$preventTabCreationWithoutRootProgress(AdvancementNode advancement, CallbackInfo ci) {
        Map<AdvancementHolder, AdvancementProgress> progress = ((ClientAdvancementsAccessor) this.clientAdvancements).getProgress();

        if (progress == null) {
            ci.cancel();
            return;
        }

        if (!progress.containsKey(advancement.holder())) {
            ci.cancel();
        }
    }
}
