package sircow.preservedinferno.mixin;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.advancements.AdvancementNode;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.advancements.AdvancementTab;
import net.minecraft.client.gui.screens.advancements.AdvancementWidget;
import net.minecraft.client.gui.screens.advancements.AdvancementWidgetType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.advancement.HiddenSpriteAccessor;

import java.util.List;

@Mixin(AdvancementWidget.class)
public abstract class AdvancementWidgetMixin {
    @Shadow private AdvancementWidget parent;
    @Shadow @Final private List<FormattedCharSequence> description;
    @Shadow private AdvancementProgress progress;
    @Shadow private static Identifier TITLE_BOX_SPRITE = Identifier.withDefaultNamespace("advancements/title_box");

    @Shadow protected abstract void extractMultilineText(GuiGraphicsExtractor guiGraphics, List<FormattedCharSequence> text, int x, int y, int color);

    @Redirect(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/advancements/AdvancementWidgetType;frameSprite(Lnet/minecraft/advancements/AdvancementType;)Lnet/minecraft/resources/Identifier;"))
    private Identifier modifyFrameSprite(AdvancementWidgetType instance, AdvancementType type) {
        AdvancementProgress prog = this.progress;

        if (prog != null && prog.isDone()) {
            return instance.frameSprite(type);
        }

        AdvancementProgress parentProg = null;
        if (this.parent != null) {
            parentProg = ((AdvancementWidgetAccessor)this.parent).preserved_inferno$getProgress();
        }

        if (parent == null || (parentProg != null && parentProg.isDone())) {
            return instance.frameSprite(type);
        }

        return ((HiddenSpriteAccessor)(Object)instance).preserved_inferno$getHiddenSprite(type);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void preserved_inferno$attachToParent(AdvancementTab tab, Minecraft minecraft, AdvancementNode node, DisplayInfo display, CallbackInfo ci) {
        if (parent == null && node.parent() != null) {
            parent = tab.getWidget(node.parent().holder());
            if (parent != null) parent.addChild((AdvancementWidget)(Object)this);
        }
    }

    @Redirect(method = "extractHover", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/advancements/AdvancementWidget;extractMultilineText(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Ljava/util/List;III)V"))
    private void preserved_inferno$hideDescription(AdvancementWidget self, GuiGraphicsExtractor gfx, List<FormattedCharSequence> text, int x, int y, int color) {
        if (text == this.description) {
            AdvancementProgress prog = this.progress;
            AdvancementWidget parentWidget = this.parent;
            AdvancementProgress parentProg = parentWidget == null ? null : ((AdvancementWidgetAccessor) parentWidget).preserved_inferno$getProgress();
            boolean isThisDone = prog != null && prog.isDone();
            boolean isParentDone = parentProg != null && parentProg.isDone();

            if (!isThisDone && parentWidget != null && !isParentDone) {
                return;
            }
        }

        this.extractMultilineText(gfx, text, x, y, color);
    }

    @Redirect(method = "extractHover", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V"))
    private void preserved_inferno$$hideBox(GuiGraphicsExtractor guiGraphics, RenderPipeline pipeline, Identifier sprite, int x, int y, int width, int height) {
        if (sprite != TITLE_BOX_SPRITE) {
            guiGraphics.blitSprite(pipeline, sprite, x, y, width, height);
            return;
        }

        AdvancementProgress prog = this.progress;
        AdvancementWidget parentWidget = this.parent;
        AdvancementProgress parentProg = parentWidget == null ? null : ((AdvancementWidgetAccessor) parentWidget).preserved_inferno$getProgress();

        boolean isThisDone = prog != null && prog.isDone();
        boolean isParentDone = parentProg != null && parentProg.isDone();

        if (!isThisDone && parentWidget != null && !isParentDone) {
            return;
        }

        guiGraphics.blitSprite(pipeline, sprite, x, y, width, height);
    }
}
