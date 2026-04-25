package sircow.preservedinferno.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.AdvancementType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AdvancementType.class)
enum AdvancementTypeFabricMixin {
    PINFERNO_PROGRESSING("progressing", ChatFormatting.GREEN),
    PINFERNO_ROOT("root", ChatFormatting.GREEN),
    PINFERNO_MASTERY("mastery", ChatFormatting.LIGHT_PURPLE);

    @Shadow
    AdvancementTypeFabricMixin(String name, ChatFormatting chatColor) {
    }
}
