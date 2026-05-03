package sircow.preservedinferno.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(AdvancementToast.class)
public class AdvancementToastMixin {
    @Shadow @Final private AdvancementHolder advancement;

    @ModifyVariable(method = "extractRenderState", at = @At("STORE"), ordinal = 0)
    private int preserved_inferno$modifyTitleColor(int titleColor) {
        DisplayInfo display = this.advancement.value().display().orElse(null);
        if (display == null) return titleColor;

        int color;

        if (display.getType() == AdvancementType.CHALLENGE) color = AdvancementType.CHALLENGE.getChatColor().getColor();
        else if (display.getType() == AdvancementType.GOAL) color = AdvancementType.GOAL.getChatColor().getColor();
        else if (display.getType() == AdvancementType.TASK) color = AdvancementType.TASK.getChatColor().getColor();
        else if (display.getType().name().equals("PINFERNO_MASTERY")) color = display.getType().getChatColor().getColor();
        else color = ChatFormatting.GREEN.getColor();

        return 0xFF000000 | color;
    }
}
