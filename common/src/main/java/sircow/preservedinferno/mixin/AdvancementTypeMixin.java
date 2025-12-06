package sircow.preservedinferno.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.AdvancementType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AdvancementType.class)
public abstract class AdvancementTypeMixin {
    @Inject(method = "getChatColor", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$overrideColour(CallbackInfoReturnable<ChatFormatting> cir) {
        AdvancementType self = (AdvancementType)(Object)this;
        if (self == AdvancementType.CHALLENGE) {
            cir.setReturnValue(ChatFormatting.GOLD);
        }

        switch (self) {
            case AdvancementType.TASK -> cir.setReturnValue(ChatFormatting.DARK_AQUA);
            case AdvancementType.GOAL -> cir.setReturnValue(ChatFormatting.DARK_PURPLE);
            case AdvancementType.CHALLENGE -> cir.setReturnValue(ChatFormatting.GOLD);
        }
    }
}
