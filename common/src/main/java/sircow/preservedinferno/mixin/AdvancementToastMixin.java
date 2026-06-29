package sircow.preservedinferno.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.network.chat.TextColor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Objects;

@Mixin(AdvancementToast.class)
public class AdvancementToastMixin {
    @Shadow @Final private AdvancementHolder advancement;

    @ModifyVariable(method = "extractRenderState", at = @At("STORE"), name = "titleColor")
    private int pinferno$modifyTitleColor(int titleColor) {
        DisplayInfo display = this.advancement.value().display().orElse(null);
        if (display == null) return titleColor;

        int color;

        ChatFormatting chatColor = display.getType().getChatColor();
        TextColor textColor = TextColor.fromLegacyFormat(chatColor);

        color = Objects.requireNonNullElseGet(textColor, () -> Objects.requireNonNull(TextColor.fromLegacyFormat(ChatFormatting.GREEN))).getValue();
        return 0xFF000000 | color;
    }
}
