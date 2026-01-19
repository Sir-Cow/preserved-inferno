package sircow.preservedinferno.other;

import net.neoforged.fml.ModList;
import sircow.preservedinferno.Constants;

public final class NeoforgeVersionChecker implements VersionChecker {
    @Override
    public String getVersion() {
        return ModList.get()
                .getModContainerById(Constants.MOD_ID)
                .orElseThrow()
                .getModInfo()
                .getVersion()
                .toString();
    }
}
