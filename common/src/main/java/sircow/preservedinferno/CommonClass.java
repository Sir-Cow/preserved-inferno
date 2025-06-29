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

import java.util.Arrays;
import java.util.List;

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
            suppressSpecificLogLines();
        }
    }

    public static void suppressSpecificLogLines() {
        Logger rootLogger = (Logger) LogManager.getRootLogger();
        final List<String> suppressedMessages = Arrays.asList(
                "Couldn't parse data file 'pinferno:nether/all_effects'",
                "Couldn't parse data file 'minecraft:netherite_upgrade_smithing_template'",
                "Couldn't parse data file 'minecraft:repair_item'",
                "Couldn't parse data file 'minecraft:shield'",
                "Couldn't parse data file 'minecraft:white_wool_from_string'"
        );

        rootLogger.addFilter(new AbstractFilter() {
            @Override
            public Result filter(LogEvent event) {
                String msg = event.getMessage().getFormattedMessage();
                if (msg != null) {
                    for (String suppressedMsgPart : suppressedMessages) {
                        if (msg.contains(suppressedMsgPart)) {
                            return Result.DENY;
                        }
                    }
                }
                return Filter.Result.NEUTRAL;
            }
        });
    }
}
