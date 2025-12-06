package sircow.preservedinferno.other;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;

public class AdvancementEnumExtender implements Runnable {
    @Override
    public void run() {
        var resolver = FabricLoader.getInstance().getMappingResolver();
        String targetClass = resolver.mapClassName("intermediary", "net.minecraft.class_189");
        String formattingInternal = resolver.mapClassName("intermediary", "net.minecraft.class_124");
        String formattingDesc = "L" + formattingInternal.replace('.', '/') + ";";

        ClassTinkerers.enumBuilder(targetClass, String.class, formattingDesc)
                .addEnum("PROGRESSING", "progressing", ChatFormatting.GREEN)
                .addEnum("ROOT", "root", ChatFormatting.GREEN)
                .addEnum("MASTERY", "mastery", ChatFormatting.LIGHT_PURPLE)
                .build();
    }
}
