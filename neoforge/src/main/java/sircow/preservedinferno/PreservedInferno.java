package sircow.preservedinferno;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import sircow.preservedinferno.effect.NeoForgeModEffects;

@Mod(Constants.MOD_ID)
public class PreservedInferno {
    public PreservedInferno(IEventBus eventBus) {
        CommonClass.init();
        NeoForgeModEffects.init(eventBus);
    }
}
