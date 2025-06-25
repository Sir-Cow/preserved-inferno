package sircow.preservedinferno;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import sircow.preservedinferno.block.ModBlocks;
import sircow.preservedinferno.components.ModComponents;
import sircow.preservedinferno.effect.ModEffects;
import sircow.preservedinferno.entity.ModEntities;
import sircow.preservedinferno.item.ModItems;
import sircow.preservedinferno.other.ModDamageTypes;
import sircow.preservedinferno.other.ModEntityData;
import sircow.preservedinferno.other.ModTags;
import sircow.preservedinferno.platform.Services;
import sircow.preservedinferno.potion.ModPotions;
import sircow.preservedinferno.recipe.ModRecipes;
import sircow.preservedinferno.sound.ModSounds;
import sircow.preservedinferno.trigger.ModTriggers;

public class CommonClass {
    public static void init() {
        if (Services.PLATFORM.isModLoaded("pinferno")) {
            Constants.LOG.info("Initialising " + Constants.MOD_NAME);
            // registering
            ModBlocks.registerModBlocks();
            ModItems.registerModItems();
            ModTags.registerModTags();
            ModDamageTypes.registerModDamageTypes();
            ModSounds.registerSounds();
            ModComponents.registerModComponents();
            ModEntityData.registerModEntityData();
            ModEntities.registerModEntities();
            ModRecipes.registerModRecipes();
            ModEffects.registerModEffects();
            ModPotions.registerModPotions();
            ModTriggers.registerTriggers();
            // other
            suppressSpecificLogLine();
        }
    }

    public static void suppressSpecificLogLine() {
        Logger rootLogger = (Logger) LogManager.getRootLogger();
        rootLogger.addFilter(new AbstractFilter() {
            @Override
            public Result filter(LogEvent event) {
                String msg = event.getMessage().getFormattedMessage();
                if (msg != null && msg.contains("Couldn't parse data file 'pinferno:nether/all_effects'")) {
                    return Result.DENY;
                }
                return Filter.Result.NEUTRAL;
            }
        });
    }
}
