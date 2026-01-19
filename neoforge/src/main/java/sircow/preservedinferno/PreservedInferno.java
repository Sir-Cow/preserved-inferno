package sircow.preservedinferno;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import sircow.preservedinferno.effect.NeoForgeModEffects;
import sircow.preservedinferno.other.NeoforgeVersionChecker;
import sircow.preservedinferno.trigger.NeoForgeModTriggers;

@Mod(Constants.MOD_ID)
public class PreservedInferno {
    public PreservedInferno(IEventBus eventBus) {
        CommonClass.init();
        Constants.INSTANCE = new NeoforgeVersionChecker();
        NeoForgeModEffects.init(eventBus);
        NeoForgeModTriggers.registerNeoForgeModTriggers(eventBus);
    }
}
