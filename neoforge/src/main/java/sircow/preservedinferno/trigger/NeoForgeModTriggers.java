package sircow.preservedinferno.trigger;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import sircow.preservedinferno.Constants;

public class NeoForgeModTriggers {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(Registries.TRIGGER_TYPE, Constants.MOD_ID);

    public static void registerNeoForgeModTriggers(IEventBus eventBus) {
        register(ModTriggers.ARMOR_VALUE);
        register(ModTriggers.BREAK_GROWN_CROP);
        register(ModTriggers.BREAK_SCULK_SHRIEKER);
        register(ModTriggers.BRUSH_BLOCK);
        register(ModTriggers.CONDUIT_POWER);
        register(ModTriggers.CONDUIT_POWER_FULL);
        register(ModTriggers.CONSUME_SCULK_INFUSION);
        register(ModTriggers.CRIT_DAMAGE);
        register(ModTriggers.DRINK_WATER);
        register(ModTriggers.FISH_TREASURE);
        register(ModTriggers.FREEZE_COOL);
        register(ModTriggers.MASTERY_ADEQUATE);
        register(ModTriggers.MASTERY_ADVANCED);
        register(ModTriggers.MASTERY_BEGINNER);
        register(ModTriggers.MASTERY_CHAMPION);
        register(ModTriggers.MASTERY_DISCIPLE);
        register(ModTriggers.MASTERY_INFERNAL);
        register(ModTriggers.MASTERY_MASTER);
        register(ModTriggers.MASTERY_NOVICE);
        register(ModTriggers.MASTERY_STARTER);
        register(ModTriggers.MAX_ENCHANTING_TABLE);
        register(ModTriggers.MAX_VILLAGER);
        register(ModTriggers.OPENED_ADVANCEMENT_SCREEN);
        register(ModTriggers.PLACE_BOOKSHELF);
        register(ModTriggers.RIDE_MINECART);
        register(ModTriggers.RIDE_MINECART_MAX_SPEED);
        register(ModTriggers.RIDE_MINECART_FAR);
        register(ModTriggers.SCRAPE_COPPER);
        register(ModTriggers.SHEAR_SHEEP);
        register(ModTriggers.STAND_ON_ICE);
        register(ModTriggers.TRADE_EVERY_VILLAGER);
        register(ModTriggers.TRIPLE_KILL);
        register(ModTriggers.USED_ANVIL_REPAIR);
        register(ModTriggers.VILLAGER_RESTOCK);
        register(ModTriggers.WOOL_FROM_LOOM);
        register(ModTriggers.WORLD_JOIN);
        TRIGGERS.register(eventBus);
    }

    private static <T extends CriterionTrigger<?>> void register(ModTriggers.TriggerEntry<T> entry) {
        entry.bind(TRIGGERS.register(entry.id, entry.factory));
    }
}
