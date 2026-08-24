package sircow.preservedinferno.config;

import io.github.notenoughupdates.moulconfig.gui.GuiContext;
import io.github.notenoughupdates.moulconfig.gui.GuiElementComponent;
import io.github.notenoughupdates.moulconfig.gui.MoulConfigEditor;
import io.github.notenoughupdates.moulconfig.platform.MoulConfigScreenComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import sircow.preservedinferno.FabricPreservedInferno;

public class ConfigGuiManager {
    public static MoulConfigEditor<PreservedInfernoConfig> editor;

    public static void openConfigGui(String search) {
        if (FabricPreservedInferno.configManager.processor == null) return;
        if (editor == null) editor = new MoulConfigEditor<>(FabricPreservedInferno.configManager.processor);
        if (search != null) editor.search(search);

        MoulConfigScreenComponent screen = new MoulConfigScreenComponent(Component.empty(), new GuiContext(new GuiElementComponent(editor)), null) {
            @Override
            public void onClose() {
                super.onClose();
                FabricPreservedInferno.configManager.saveConfig(FabricPreservedInferno.clientConfig);
            }
        };

        Minecraft.getInstance().gui.setScreen(screen);
    }
}
