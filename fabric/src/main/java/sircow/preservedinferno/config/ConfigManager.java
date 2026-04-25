package sircow.preservedinferno.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.notenoughupdates.moulconfig.processor.BuiltinMoulConfigGuis;
import io.github.notenoughupdates.moulconfig.processor.ConfigProcessorDriver;
import io.github.notenoughupdates.moulconfig.processor.MoulConfigProcessor;
import org.apache.commons.io.FileUtils;
import sircow.preservedinferno.PreservedInferno;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ConfigManager {
    private static final File configFile = new File("config/preservedinferno/preservedinferno.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    public MoulConfigProcessor<PreservedInfernoConfig> processor;

    public void firstLoad() {
        if (configFile.exists()) {
            try {
                String json = FileUtils.readFileToString(configFile, StandardCharsets.UTF_8);
                PreservedInferno.config = gson.fromJson(json, PreservedInfernoConfig.class);
            }
            catch (Exception e) {
                e.printStackTrace();
                PreservedInferno.config = new PreservedInfernoConfig();
            }
        }
        else {
            PreservedInferno.config = new PreservedInfernoConfig();
            saveConfig();
        }
        recreateProcessor();
    }

    public void saveConfig() {
        try {
            File parent = configFile.getParentFile();
            if (parent != null && !parent.exists() && !parent.mkdirs()) {
                throw new IOException("Failed to create directory: " + parent);
            }

            FileUtils.writeStringToFile(configFile, gson.toJson(PreservedInferno.config), StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void recreateProcessor() {
        processor = new MoulConfigProcessor<>(PreservedInferno.config);
        BuiltinMoulConfigGuis.addProcessors(processor);
        ConfigProcessorDriver driver = new ConfigProcessorDriver(processor);
        driver.warnForPrivateFields = false;
        driver.processConfig(PreservedInferno.config);
    }
}
