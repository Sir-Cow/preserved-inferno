package sircow.preservedinferno.mixin;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementNode;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.advancement.ModAdvancements;

import java.util.Map;

@Mixin(AdvancementsScreen.class)
public class AdvancementsScreenMixin extends Screen {
    @Shadow @Final private ClientAdvancements advancements;
    @Shadow private int leftPos;
    @Shadow private int topPos;

    protected AdvancementsScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "extractWindow", at = @At("TAIL"))
    private void pinferno$renderPoints(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        int playerPoints = ModAdvancements.getPlayerPoints(mc.player.getUUID());
        Component pointsText = Component.translatable("advancements.pinferno.menu.mastery_points", playerPoints);

        guiGraphics.text(mc.font, pointsText, this.leftPos + 142, this.topPos + 6, -12566464, false);
    }


    @Inject(method = "onAddAdvancementRoot", at = @At("HEAD"), cancellable = true)
    private void pinferno$preventTabCreationWithoutRootProgress(AdvancementNode advancement, CallbackInfo ci) {
        Map<AdvancementHolder, AdvancementProgress> progress = ((ClientAdvancementsAccessor)this.advancements).getProgress();

        if (progress == null) {
            ci.cancel();
            return;
        }

        AdvancementHolder rootAdvancement = advancement.holder();

        if (!progress.containsKey(rootAdvancement)) {
            ci.cancel();
        }
    }
}
