package sircow.preservedinferno.config;

import io.github.notenoughupdates.moulconfig.gui.GuiContext;
import io.github.notenoughupdates.moulconfig.gui.GuiElementComponent;
import io.github.notenoughupdates.moulconfig.gui.MoulConfigEditor;
import io.github.notenoughupdates.moulconfig.platform.MoulConfigScreenComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import sircow.preservedinferno.PreservedInferno;

public class ConfigGuiManager {
    public static MoulConfigEditor<PreservedInfernoConfig> editor = null;

    public static void openConfigGui(String search) {
        if (editor == null) editor = new MoulConfigEditor<>(PreservedInferno.configManager.processor);
        if (search != null) editor.search(search);

        MoulConfigScreenComponent screen = new MoulConfigScreenComponent(Component.empty(), new GuiContext(new GuiElementComponent(editor)), null) {
            @Override
            public void onClose() {
                super.onClose();
                PreservedInferno.configManager.saveConfig();
            }
        };

        Minecraft.getInstance().setScreen(screen);
    }
}
