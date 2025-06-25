package sircow.preservedinferno.mixin;

import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.platform.Services;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Mixin(ServerAdvancementManager.class)
public class ServerAdvancementManagerMixin {
    @Inject(method = "apply*", at = @At("HEAD"))
    private void preserved_inferno$onApply(Map<ResourceLocation, Advancement> advancementsIn, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo ci) {
        Set<ResourceLocation> blacklist = new HashSet<>(Set.of(
                ResourceLocation.withDefaultNamespace("adventure/avoid_vibration"),
                ResourceLocation.withDefaultNamespace("adventure/fall_from_world_height"),
                ResourceLocation.withDefaultNamespace("adventure/kill_a_mob"),
                ResourceLocation.withDefaultNamespace("adventure/salvage_shard"),
                ResourceLocation.withDefaultNamespace("adventure/spyglass_at_dragon"),
                ResourceLocation.withDefaultNamespace("adventure/spyglass_at_ghast"),
                ResourceLocation.withDefaultNamespace("adventure/spyglass_at_parrot"),
                ResourceLocation.withDefaultNamespace("adventure/summon_iron_golem"),
                ResourceLocation.withDefaultNamespace("adventure/trim_with_all_exclusive_armor_patterns"),
                ResourceLocation.withDefaultNamespace("adventure/trim_with_any_armor_pattern"),
                ResourceLocation.withDefaultNamespace("adventure/use_lodestone"),
                ResourceLocation.withDefaultNamespace("adventure/walk_on_powder_snow_with_leather_boots"),

                ResourceLocation.withDefaultNamespace("husbandry/fishy_business"),
                ResourceLocation.withDefaultNamespace("husbandry/plant_seed"),

                ResourceLocation.withDefaultNamespace("nether/root"),

                ResourceLocation.withDefaultNamespace("story/deflect_arrow"),
                ResourceLocation.withDefaultNamespace("story/enter_the_nether"),
                ResourceLocation.withDefaultNamespace("story/iron_tools"),
                ResourceLocation.withDefaultNamespace("story/mine_diamond"),
                ResourceLocation.withDefaultNamespace("story/obtain_armor"),
                ResourceLocation.withDefaultNamespace("story/shiny_gear"),
                ResourceLocation.withDefaultNamespace("story/smelt_iron"),
                ResourceLocation.withDefaultNamespace("story/upgrade_tools")
        ));

        if (Services.PLATFORM.isModLoaded("pblizzard")) {
            Constants.LOG.info("blizzard loaded");
            blacklist.add(ResourceLocation.withDefaultNamespace("nether/all_effects"));
        }
        else {
            Constants.LOG.info("inferno loaded");
            blacklist.add(Constants.id("nether/all_effects"));
        }
        advancementsIn.keySet().removeIf(blacklist::contains);
    }
}
