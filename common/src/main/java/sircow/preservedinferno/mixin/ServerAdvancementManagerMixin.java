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
                ResourceLocation.withDefaultNamespace("adventure/arbalistic"),
                ResourceLocation.withDefaultNamespace("adventure/avoid_vibration"),
                ResourceLocation.withDefaultNamespace("adventure/blowback"),
                ResourceLocation.withDefaultNamespace("adventure/brush_armadillo"),
                ResourceLocation.withDefaultNamespace("adventure/fall_from_world_height"),
                ResourceLocation.withDefaultNamespace("adventure/heart_transplanter"),
                ResourceLocation.withDefaultNamespace("adventure/honey_block_slide"),
                ResourceLocation.withDefaultNamespace("adventure/kill_a_mob"),
                ResourceLocation.withDefaultNamespace("adventure/kill_mob_near_sculk_catalyst"),
                ResourceLocation.withDefaultNamespace("adventure/lighten_up"),
                ResourceLocation.withDefaultNamespace("adventure/minecraft_trials_edition"),
                ResourceLocation.withDefaultNamespace("adventure/ol_betsy"),
                ResourceLocation.withDefaultNamespace("adventure/overoverkill"),
                ResourceLocation.withDefaultNamespace("adventure/read_power_of_chiseled_bookshelf"),
                ResourceLocation.withDefaultNamespace("adventure/revaulting"),
                ResourceLocation.withDefaultNamespace("adventure/root"),
                ResourceLocation.withDefaultNamespace("adventure/salvage_sherd"),
                ResourceLocation.withDefaultNamespace("adventure/shoot_arrow"),
                ResourceLocation.withDefaultNamespace("adventure/sleep_in_bed"),
                ResourceLocation.withDefaultNamespace("adventure/spyglass_at_dragon"),
                ResourceLocation.withDefaultNamespace("adventure/spyglass_at_ghast"),
                ResourceLocation.withDefaultNamespace("adventure/spyglass_at_parrot"),
                ResourceLocation.withDefaultNamespace("adventure/summon_iron_golem"),
                ResourceLocation.withDefaultNamespace("adventure/throw_trident"),
                ResourceLocation.withDefaultNamespace("adventure/trim_with_all_exclusive_armor_patterns"),
                ResourceLocation.withDefaultNamespace("adventure/trim_with_any_armor_pattern"),
                ResourceLocation.withDefaultNamespace("adventure/two_birds_one_arrow"),
                ResourceLocation.withDefaultNamespace("adventure/under_lock_and_key"),
                ResourceLocation.withDefaultNamespace("adventure/use_lodestone"),
                ResourceLocation.withDefaultNamespace("adventure/voluntary_exile"),
                ResourceLocation.withDefaultNamespace("adventure/walk_on_powder_snow_with_leather_boots"),
                ResourceLocation.withDefaultNamespace("adventure/who_needs_rockets"),
                ResourceLocation.withDefaultNamespace("adventure/whos_the_pillager_now"),

                ResourceLocation.withDefaultNamespace("husbandry/axolotl_in_a_bucket"),
                ResourceLocation.withDefaultNamespace("husbandry/balanced_diet"),
                ResourceLocation.withDefaultNamespace("husbandry/breed_an_animal"),
                ResourceLocation.withDefaultNamespace("husbandry/bred_all_animals"),
                ResourceLocation.withDefaultNamespace("husbandry/complete_catalogue"),
                ResourceLocation.withDefaultNamespace("husbandry/feed_snifflet"),
                ResourceLocation.withDefaultNamespace("husbandry/fishy_business"),
                ResourceLocation.withDefaultNamespace("husbandry/froglights"),
                ResourceLocation.withDefaultNamespace("husbandry/kill_axolotl_target"),
                ResourceLocation.withDefaultNamespace("husbandry/leash_all_frog_variants"),
                ResourceLocation.withDefaultNamespace("husbandry/obtain_netherite_hoe"),
                ResourceLocation.withDefaultNamespace("husbandry/obtain_sniffer_egg"),
                ResourceLocation.withDefaultNamespace("husbandry/place_dried_ghast_in_water"),
                ResourceLocation.withDefaultNamespace("husbandry/plant_any_sniffer_seed"),
                ResourceLocation.withDefaultNamespace("husbandry/plant_seed"),
                ResourceLocation.withDefaultNamespace("husbandry/repair_wolf_armor"),
                ResourceLocation.withDefaultNamespace("husbandry/ride_a_boat_with_a_goat"),
                ResourceLocation.withDefaultNamespace("husbandry/root"),
                ResourceLocation.withDefaultNamespace("husbandry/safely_harvest_honey"),
                ResourceLocation.withDefaultNamespace("husbandry/silk_touch_nest"),
                ResourceLocation.withDefaultNamespace("husbandry/tactical_fishing"),
                ResourceLocation.withDefaultNamespace("husbandry/tadpole_in_a_bucket"),
                ResourceLocation.withDefaultNamespace("husbandry/tame_an_animal"),
                ResourceLocation.withDefaultNamespace("husbandry/wax_on"),
                ResourceLocation.withDefaultNamespace("husbandry/wax_off"),
                ResourceLocation.withDefaultNamespace("husbandry/whole_pack"),

                ResourceLocation.withDefaultNamespace("nether/root"),

                ResourceLocation.withDefaultNamespace("story/deflect_arrow"),
                ResourceLocation.withDefaultNamespace("story/enter_the_nether"),
                ResourceLocation.withDefaultNamespace("story/enter_the_end"),
                ResourceLocation.withDefaultNamespace("story/follow_ender_eye"),
                ResourceLocation.withDefaultNamespace("story/iron_tools"),
                ResourceLocation.withDefaultNamespace("story/mine_diamond"),
                ResourceLocation.withDefaultNamespace("story/obtain_armor"),
                ResourceLocation.withDefaultNamespace("story/shiny_gear"),
                ResourceLocation.withDefaultNamespace("story/smelt_iron"),
                ResourceLocation.withDefaultNamespace("story/upgrade_tools"),

                // removed entirely
                ResourceLocation.withDefaultNamespace("adventure/adventuring_time"),
                ResourceLocation.withDefaultNamespace("adventure/bullseye"),
                ResourceLocation.withDefaultNamespace("adventure/craft_decorated_pot_using_only_sherds"),
                ResourceLocation.withDefaultNamespace("adventure/crafters_crafting_crafters"),
                ResourceLocation.withDefaultNamespace("adventure/hero_of_the_village"),
                ResourceLocation.withDefaultNamespace("adventure/kill_all_mobs"),
                ResourceLocation.withDefaultNamespace("adventure/lightning_rod_with_villager_no_fire"),
                ResourceLocation.withDefaultNamespace("adventure/play_jukebox_in_meadows"),
                ResourceLocation.withDefaultNamespace("adventure/sniper_duel"),
                ResourceLocation.withDefaultNamespace("adventure/totem_of_undying"),
                ResourceLocation.withDefaultNamespace("adventure/trade"),
                ResourceLocation.withDefaultNamespace("adventure/trade_at_world_height"),
                ResourceLocation.withDefaultNamespace("adventure/very_very_frightening"),

                ResourceLocation.withDefaultNamespace("husbandry/allay_deliver_cake_to_note_block"),
                ResourceLocation.withDefaultNamespace("husbandry/allay_deliver_item_to_player"),
                ResourceLocation.withDefaultNamespace("husbandry/make_a_sign_glow"),
                ResourceLocation.withDefaultNamespace("husbandry/remove_wolf_armor"),

                ResourceLocation.withDefaultNamespace("nether/explore_nether"),
                ResourceLocation.withDefaultNamespace("nether/fast_travel"),
                ResourceLocation.withDefaultNamespace("nether/ride_strider"),
                ResourceLocation.withDefaultNamespace("nether/ride_strider_in_overworld_lava"),

                ResourceLocation.withDefaultNamespace("story/cure_zombie_villager"),
                ResourceLocation.withDefaultNamespace("story/enchant_item"),
                ResourceLocation.withDefaultNamespace("story/form_obsidian"),
                ResourceLocation.withDefaultNamespace("story/lava_bucket")
        ));

        if (Services.PLATFORM.isModLoaded("pblizzard")) {
            //Constants.LOG.info("blizzard loaded");
            blacklist.add(ResourceLocation.withDefaultNamespace("nether/all_effects"));
        }
        else {
            //Constants.LOG.info("inferno loaded");
            blacklist.add(Constants.id("nether/all_effects"));
        }
        advancementsIn.keySet().removeIf(blacklist::contains);
    }
}
