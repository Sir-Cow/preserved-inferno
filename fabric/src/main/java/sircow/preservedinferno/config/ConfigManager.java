package sircow.preservedinferno.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.notenoughupdates.moulconfig.processor.BuiltinMoulConfigGuis;
import io.github.notenoughupdates.moulconfig.processor.ConfigProcessorDriver;
import io.github.notenoughupdates.moulconfig.processor.MoulConfigProcessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FileUtils;
import sircow.preservedinferno.FabricPreservedInferno;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ConfigManager {
    private static final File configFile = new File("config/preservedinferno/preservedinferno.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    public MoulConfigProcessor<PreservedInfernoConfig> processor;

    public void firstLoad() {
        PreservedInfernoConfig loaded;

        if (configFile.exists()) {
            try {
                String json = FileUtils.readFileToString(configFile, StandardCharsets.UTF_8);
                loaded = gson.fromJson(json, PreservedInfernoConfig.class);
            }
            catch (Exception e) {
                e.printStackTrace();
                loaded = new PreservedInfernoConfig();
            }
        }
        else {
            loaded = new PreservedInfernoConfig();
            saveConfig(loaded);
        }

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) FabricPreservedInferno.clientConfig = loaded;
        else FabricPreservedInferno.serverConfig = loaded;

        recreateProcessor();
    }

    public void saveConfig(PreservedInfernoConfig config) {
        try {
            File parent = configFile.getParentFile();
            if (parent != null && !parent.exists() && !parent.mkdirs()) {
                throw new IOException("Failed to create directory: " + parent);
            }

            FileUtils.writeStringToFile(configFile, gson.toJson(config), StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void recreateProcessor() {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) return;
        processor = new MoulConfigProcessor<>(FabricPreservedInferno.clientConfig);
        BuiltinMoulConfigGuis.addProcessors(processor);
        ConfigProcessorDriver driver = new ConfigProcessorDriver(processor);
        driver.warnForPrivateFields = false;
        driver.processConfig(FabricPreservedInferno.clientConfig);
    }
}
