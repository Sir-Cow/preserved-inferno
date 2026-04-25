package sircow.preservedinferno.mixin;

import net.minecraft.advancements.AdvancementType;
import net.minecraft.client.gui.screens.advancements.AdvancementWidgetType;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.advancement.HiddenSpriteAccessor;

@Mixin(AdvancementWidgetType.class)
public abstract class AdvancementWidgetTypeMixin implements HiddenSpriteAccessor {
    @Unique private Identifier progressingFrameSprite;
    @Unique private Identifier hiddenTaskSprite;
    @Unique private Identifier hiddenChallengeSprite;
    @Unique private Identifier hiddenGoalSprite;
    @Unique private Identifier hiddenProgressingSprite;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void preserved_inferno$initialiseSprites(String boxSprite, int taskFrameSprite, Identifier challengeFrameSprite, Identifier goalFrameSprite, Identifier par5, Identifier par6, CallbackInfo ci) {
        this.hiddenTaskSprite = Constants.id("advancements/task_frame_hidden");
        this.hiddenChallengeSprite = Constants.id("advancements/challenge_frame_hidden");
        this.hiddenGoalSprite = Constants.id("advancements/goal_frame_hidden");
        this.hiddenProgressingSprite = Constants.id("advancements/progressing_frame_hidden");

        AdvancementWidgetType currentInstance = (AdvancementWidgetType)(Object)this;

        if (currentInstance.name().equals("OBTAINED")) {
            this.progressingFrameSprite = Constants.id("advancements/progressing_frame_obtained");
        }
        else if (currentInstance.name().equals("UNOBTAINED")) {
            this.progressingFrameSprite = Constants.id("advancements/progressing_frame_unobtained");
        }
        else {
            this.progressingFrameSprite = Constants.id("advancements/progressing_frame_hidden");
        }
    }

    @Inject(method = "frameSprite", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$injectProgressingCase(AdvancementType type, CallbackInfoReturnable<Identifier> cir) {
        if (type.name().equals("PINFERNO_PROGRESSING") || type.name().equals("PINFERNO_ROOT") || type.name().equals("PINFERNO_MASTERY")) {
            cir.setReturnValue(this.progressingFrameSprite);
        }
    }

    @Override
    public Identifier preserved_inferno$getHiddenSprite(AdvancementType type) {
        if (type.name().equals("PINFERNO_PROGRESSING")) {
            return this.hiddenProgressingSprite;
        }
        return switch (type) {
            case CHALLENGE -> this.hiddenChallengeSprite;
            case GOAL -> this.hiddenGoalSprite;
            default -> this.hiddenTaskSprite;
        };
    }
}
