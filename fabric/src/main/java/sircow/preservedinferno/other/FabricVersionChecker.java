package sircow.preservedinferno.other;

import net.fabricmc.loader.api.FabricLoader;
import sircow.preservedinferno.Constants;

public final class FabricVersionChecker implements VersionChecker {
    @Override
    public String getVersion() {
        return FabricLoader.getInstance()
                .getModContainer(Constants.MOD_ID)
                .orElseThrow()
                .getMetadata()
                .getVersion()
                .getFriendlyString();
    }
}
