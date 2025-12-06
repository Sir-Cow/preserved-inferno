package sircow.preservedinferno.mixin;

import net.minecraft.advancements.AdvancementType;
import net.minecraft.client.gui.screens.advancements.AdvancementWidgetType;
import net.minecraft.resources.ResourceLocation;
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
    @Unique private ResourceLocation progressingFrameSprite;
    @Unique private ResourceLocation hiddenTaskSprite;
    @Unique private ResourceLocation hiddenChallengeSprite;
    @Unique private ResourceLocation hiddenGoalSprite;
    @Unique private ResourceLocation hiddenProgressingSprite;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void preserved_inferno$initialiseSprites(String boxSprite, int taskFrameSprite, ResourceLocation challengeFrameSprite, ResourceLocation goalFrameSprite, ResourceLocation par5, ResourceLocation par6, CallbackInfo ci) {
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
    private void preserved_inferno$injectProgressingCase(AdvancementType type, CallbackInfoReturnable<ResourceLocation> cir) {
        if (type.name().equals("PROGRESSING") || type.name().equals("ROOT") || type.name().equals("MASTERY")) {
            cir.setReturnValue(this.progressingFrameSprite);
        }
    }

    @Override
    public ResourceLocation preserved_inferno$getHiddenSprite(AdvancementType type) {
        if (type.name().equals("PROGRESSING")) {
            return this.hiddenProgressingSprite;
        }
        return switch (type) {
            case CHALLENGE -> this.hiddenChallengeSprite;
            case GOAL -> this.hiddenGoalSprite;
            default -> this.hiddenTaskSprite;
        };
    }
}
