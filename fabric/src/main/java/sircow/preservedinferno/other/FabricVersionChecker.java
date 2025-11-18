package sircow.preservedinferno.other;

import net.fabricmc.loader.api.FabricLoader;

public final class FabricVersionChecker implements VersionChecker {
    @Override
    public String getVersion() {
        return FabricLoader.getInstance()
                .getModContainer("pinferno")
                .orElseThrow()
                .getMetadata()
                .getVersion()
                .getFriendlyString();
    }
}
