package sircow.preservedinferno.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.notenoughupdates.moulconfig.gui.GuiContext;
import io.github.notenoughupdates.moulconfig.gui.GuiElementComponent;
import io.github.notenoughupdates.moulconfig.gui.MoulConfigEditor;
import io.github.notenoughupdates.moulconfig.platform.MoulConfigScreenComponent;
import net.minecraft.network.chat.Component;
import sircow.preservedinferno.FabricPreservedInferno;

public class ModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            if (FabricPreservedInferno.clientConfig == null) FabricPreservedInferno.clientConfig = new PreservedInfernoConfig();

            MoulConfigEditor<PreservedInfernoConfig> editor = new MoulConfigEditor<>(FabricPreservedInferno.configManager.processor);
            return new MoulConfigScreenComponent(Component.empty(), new GuiContext(new GuiElementComponent(editor)), null) {
                @Override
                public void onClose() {
                    super.onClose();
                    FabricPreservedInferno.configManager.saveConfig(FabricPreservedInferno.clientConfig);
                }
            };
        };
    }
}
