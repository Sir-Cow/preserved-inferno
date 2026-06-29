package sircow.preservedinferno.mixin;

import net.minecraft.advancements.Advancement;
import net.minecraft.resources.Identifier;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Mixin(ServerAdvancementManager.class)
public class ServerAdvancementManagerMixin {
    @Inject(method = "apply*", at = @At("HEAD"))
    private void pinferno$onApply(Map<Identifier, Advancement> advancementsIn, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo ci) {
        Set<Identifier> blacklist = new HashSet<>(Set.of(
                Identifier.withDefaultNamespace("adventure/adventuring_time"),
                Identifier.withDefaultNamespace("adventure/brush_armadillo"),
                Identifier.withDefaultNamespace("adventure/fall_from_world_height"),
                Identifier.withDefaultNamespace("adventure/heart_transplanter"),
                Identifier.withDefaultNamespace("adventure/honey_block_slide"),
                Identifier.withDefaultNamespace("adventure/read_power_of_chiseled_bookshelf"),
                Identifier.withDefaultNamespace("adventure/salvage_sherd"),
                Identifier.withDefaultNamespace("adventure/sleep_in_bed"),
                Identifier.withDefaultNamespace("adventure/spyglass_at_dragon"),
                Identifier.withDefaultNamespace("adventure/spyglass_at_ghast"),
                Identifier.withDefaultNamespace("adventure/spyglass_at_parrot"),
                Identifier.withDefaultNamespace("adventure/summon_iron_golem"),
                Identifier.withDefaultNamespace("adventure/trade"),
                Identifier.withDefaultNamespace("adventure/trade_at_world_height"),
                Identifier.withDefaultNamespace("adventure/use_lodestone"),
                Identifier.withDefaultNamespace("adventure/walk_on_powder_snow_with_leather_boots"),
                Identifier.withDefaultNamespace("husbandry/axolotl_in_a_bucket"),
                Identifier.withDefaultNamespace("husbandry/balanced_diet"),
                Identifier.withDefaultNamespace("husbandry/breed_an_animal"),
                Identifier.withDefaultNamespace("husbandry/bred_all_animals"),
                Identifier.withDefaultNamespace("husbandry/complete_catalogue"),
                Identifier.withDefaultNamespace("husbandry/feed_snifflet"),
                Identifier.withDefaultNamespace("husbandry/fishy_business"),
                Identifier.withDefaultNamespace("husbandry/froglights"),
                Identifier.withDefaultNamespace("husbandry/kill_axolotl_target"),
                Identifier.withDefaultNamespace("husbandry/leash_all_frog_variants"),
                Identifier.withDefaultNamespace("husbandry/obtain_netherite_hoe"),
                Identifier.withDefaultNamespace("husbandry/obtain_sniffer_egg"),
                Identifier.withDefaultNamespace("husbandry/place_dried_ghast_in_water"),
                Identifier.withDefaultNamespace("husbandry/plant_any_sniffer_seed"),
                Identifier.withDefaultNamespace("husbandry/plant_seed"),
                Identifier.withDefaultNamespace("husbandry/repair_wolf_armor"),
                Identifier.withDefaultNamespace("husbandry/ride_a_boat_with_a_goat"),
                Identifier.withDefaultNamespace("husbandry/root"),
                Identifier.withDefaultNamespace("husbandry/safely_harvest_honey"),
                Identifier.withDefaultNamespace("husbandry/silk_touch_nest"),
                Identifier.withDefaultNamespace("husbandry/tactical_fishing"),
                Identifier.withDefaultNamespace("husbandry/tadpole_in_a_bucket"),
                Identifier.withDefaultNamespace("husbandry/tame_an_animal"),
                Identifier.withDefaultNamespace("husbandry/wax_on"),
                Identifier.withDefaultNamespace("husbandry/wax_off"),
                Identifier.withDefaultNamespace("husbandry/whole_pack"),
                Identifier.withDefaultNamespace("nether/root"),
                Identifier.withDefaultNamespace("nether/netherite_armor"),
                Identifier.withDefaultNamespace("story/cure_zombie_villager"),
                Identifier.withDefaultNamespace("story/deflect_arrow"),
                Identifier.withDefaultNamespace("story/enter_the_nether"),
                Identifier.withDefaultNamespace("story/enter_the_end"),
                Identifier.withDefaultNamespace("story/follow_ender_eye"),
                Identifier.withDefaultNamespace("story/iron_tools"),
                Identifier.withDefaultNamespace("story/mine_diamond"),
                Identifier.withDefaultNamespace("story/obtain_armor"),
                Identifier.withDefaultNamespace("story/shiny_gear"),
                Identifier.withDefaultNamespace("story/smelt_iron"),
                Identifier.withDefaultNamespace("story/upgrade_tools"),

                // removed entirely
                Identifier.withDefaultNamespace("adventure/crafters_crafting_crafters"),
                Identifier.withDefaultNamespace("adventure/hero_of_the_village"),
                Identifier.withDefaultNamespace("adventure/lighten_up"),
                Identifier.withDefaultNamespace("adventure/lightning_rod_with_villager_no_fire"),
                Identifier.withDefaultNamespace("adventure/play_jukebox_in_meadows"),
                Identifier.withDefaultNamespace("adventure/totem_of_undying"),
                Identifier.withDefaultNamespace("adventure/very_very_frightening"),
                Identifier.withDefaultNamespace("husbandry/allay_deliver_cake_to_note_block"),
                Identifier.withDefaultNamespace("husbandry/allay_deliver_item_to_player"),
                Identifier.withDefaultNamespace("husbandry/make_a_sign_glow"),
                Identifier.withDefaultNamespace("husbandry/remove_wolf_armor"),
                Identifier.withDefaultNamespace("husbandry/uh_oh"),
                Identifier.withDefaultNamespace("nether/explore_nether"),
                Identifier.withDefaultNamespace("nether/fast_travel"),
                Identifier.withDefaultNamespace("nether/ride_strider"),
                Identifier.withDefaultNamespace("nether/ride_strider_in_overworld_lava"),
                Identifier.withDefaultNamespace("nether/uneasy_alliance"),
                Identifier.withDefaultNamespace("story/enchant_item"),
                Identifier.withDefaultNamespace("story/form_obsidian"),
                Identifier.withDefaultNamespace("story/lava_bucket")
        ));

        advancementsIn.keySet().removeIf(blacklist::contains);
    }
}
