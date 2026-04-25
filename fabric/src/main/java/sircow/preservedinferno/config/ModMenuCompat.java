package sircow.preservedinferno.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.notenoughupdates.moulconfig.gui.GuiContext;
import io.github.notenoughupdates.moulconfig.gui.GuiElementComponent;
import io.github.notenoughupdates.moulconfig.gui.MoulConfigEditor;
import io.github.notenoughupdates.moulconfig.platform.MoulConfigScreenComponent;
import net.minecraft.network.chat.Component;
import sircow.preservedinferno.PreservedInferno;

public class ModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            MoulConfigEditor<PreservedInfernoConfig> editor = new MoulConfigEditor<>(PreservedInferno.configManager.processor);
            return new MoulConfigScreenComponent(Component.empty(), new GuiContext(new GuiElementComponent(editor)), null) {
                @Override
                public void onClose() {
                    super.onClose();
                    PreservedInferno.configManager.saveConfig();
                }
            };
        };
    }
}
