package sircow.preservedinferno.other;

import com.chocohead.mm.api.ClassTinkerers;
import net.minecraft.ChatFormatting;

public class AdvancementEnumExtender implements Runnable {
    @Override
    public void run() {
        String targetClass = "net.minecraft.advancements.AdvancementType";
        String formattingInternal = "net.minecraft.ChatFormatting";
        String formattingDesc = "L" + formattingInternal.replace('.', '/') + ";";

        ClassTinkerers.enumBuilder(targetClass, String.class, formattingDesc)
                .addEnum("PROGRESSING", "progressing", ChatFormatting.GREEN)
                .addEnum("ROOT", "root", ChatFormatting.GREEN)
                .addEnum("MASTERY", "mastery", ChatFormatting.LIGHT_PURPLE)
                .build();
    }
}
