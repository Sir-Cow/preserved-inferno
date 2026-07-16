package sircow.preservedinferno.item;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.block.ModBlocks;

public class FabricModItemGroups {
    public static final ResourceKey<CreativeModeTab> PRESERVED_INFERNO_TAB_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Constants.id("pinferno"));
    public static CreativeModeTab PRESERVED_INFERNO_GROUP;

    public static void register() {
        PRESERVED_INFERNO_GROUP = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                .title(Component.translatable("itemgroup.pinferno.items"))
                .icon(() -> new ItemStack(ModItems.DREAMCATCHER))
                .displayItems((displayContext, entries) -> {
                    entries.accept(ModBlocks.RHYOLITE.asItem());
                    entries.accept(ModBlocks.POLISHED_RHYOLITE.asItem());
                    entries.accept(ModBlocks.RHYOLITE_BRICKS.asItem());
                    entries.accept(ModBlocks.CRACKED_RHYOLITE_BRICKS.asItem());
                    entries.accept(ModBlocks.RHYOLITE_WALL.asItem());
                    entries.accept(ModBlocks.RHYOLITE_BRICK_WALL.asItem());
                    entries.accept(ModBlocks.RHYOLITE_STAIRS.asItem());
                    entries.accept(ModBlocks.POLISHED_RHYOLITE_STAIRS.asItem());
                    entries.accept(ModBlocks.RHYOLITE_BRICK_STAIRS.asItem());
                    entries.accept(ModBlocks.RHYOLITE_SLAB.asItem());
                    entries.accept(ModBlocks.POLISHED_RHYOLITE_SLAB.asItem());
                    entries.accept(ModBlocks.RHYOLITE_BRICK_SLAB.asItem());

                    entries.accept(ModBlocks.SPARKLING_BLACKSTONE.asItem());

                    entries.accept(ModItems.REVERB_COMPASS);
                    entries.accept(ModItems.DREAMCATCHER);
                    entries.accept(ModItems.ELDER_GUARDIAN_SPINE);
                    entries.accept(ModItems.HOLLOW_TWINE);
                    entries.accept(ModItems.PHANTOM_SINEW);
                    entries.accept(ModItems.RAW_HIDE);
                    entries.accept(ModItems.LEATHER_FABRIC);
                    entries.accept(ModItems.GILDEN_BERRIES);
                    entries.accept(ModItems.RESIN_SPECK);

                    entries.accept(ModItems.ECHOING_PRISM);
                    entries.accept(ModItems.ECHOING_PRISM_UPGRADE_SMITHING_TEMPLATE);
                    entries.accept(ModItems.SCULK_INFUSION);

                    entries.accept(ModItems.REPAIR_KIT);
                    entries.accept(ModItems.FORGE_DUST);

                    entries.accept(ModItems.RAW_COPPER_CHUNK);
                    entries.accept(ModItems.RAW_IRON_CHUNK);
                    entries.accept(ModItems.RAW_GOLD_CHUNK);

                    entries.accept(ModItems.QUARTZITE);
                    entries.accept(ModItems.QUARTZITE_SHOVEL);
                    entries.accept(ModItems.QUARTZITE_PICKAXE);
                    entries.accept(ModItems.QUARTZITE_AXE);
                    entries.accept(ModItems.QUARTZITE_SCYTHE);
                    entries.accept(ModItems.QUARTZITE_SWORD);

                    entries.accept(ModItems.COPPER_TRIDENT);

                    entries.accept(ModItems.WOODEN_MULTITOOL);
                    entries.accept(ModItems.STONE_MULTITOOL);
                    entries.accept(ModItems.COPPER_MULTITOOL);
                    entries.accept(ModItems.IRON_MULTITOOL);
                    entries.accept(ModItems.DIAMOND_MULTITOOL);
                    entries.accept(ModItems.NETHERITE_MULTITOOL);
                    entries.accept(ModItems.QUARTZITE_MULTITOOL);
                    entries.accept(ModItems.GOLDEN_MULTITOOL);
                    entries.accept(ModItems.NETHER_ALLOY_MULTITOOL);

                    entries.accept(ModItems.BLACK_CLOTH);
                    entries.accept(ModItems.BLUE_CLOTH);
                    entries.accept(ModItems.BROWN_CLOTH);
                    entries.accept(ModItems.CYAN_CLOTH);
                    entries.accept(ModItems.GRAY_CLOTH);
                    entries.accept(ModItems.GREEN_CLOTH);
                    entries.accept(ModItems.LIGHT_BLUE_CLOTH);
                    entries.accept(ModItems.LIGHT_GRAY_CLOTH);
                    entries.accept(ModItems.LIME_CLOTH);
                    entries.accept(ModItems.MAGENTA_CLOTH);
                    entries.accept(ModItems.ORANGE_CLOTH);
                    entries.accept(ModItems.PINK_CLOTH);
                    entries.accept(ModItems.PURPLE_CLOTH);
                    entries.accept(ModItems.RED_CLOTH);
                    entries.accept(ModItems.WHITE_CLOTH);
                    entries.accept(ModItems.YELLOW_CLOTH);

                    entries.accept(ModItems.COPPER_SHIELD);
                    entries.accept(ModItems.IRON_SHIELD);
                    entries.accept(ModItems.GOLDEN_SHIELD);
                    entries.accept(ModItems.DIAMOND_SHIELD);
                    entries.accept(ModItems.NETHERITE_SHIELD);

                    entries.accept(ModItems.NETHER_ALLOY_PLATE);
                    entries.accept(ModItems.NETHER_ALLOY_INGOT);
                    entries.accept(ModItems.NETHER_ALLOY_UPGRADE_SMITHING_TEMPLATE);
                    entries.accept(ModItems.NETHER_ALLOY_SHOVEL);
                    entries.accept(ModItems.NETHER_ALLOY_PICKAXE);
                    entries.accept(ModItems.NETHER_ALLOY_AXE);
                    entries.accept(ModItems.NETHER_ALLOY_SCYTHE);
                    entries.accept(ModItems.NETHER_ALLOY_SWORD);

                    entries.accept(ModBlocks.ANGLING_TABLE.asItem());
                    entries.accept(ModItems.AQUATIC_FIBER);
                    entries.accept(FabricModItems.CACHE);
                    entries.accept(ModItems.COPPER_FISHING_HOOK);
                    entries.accept(ModItems.IRON_FISHING_HOOK);
                    entries.accept(ModItems.PRISMARINE_FISHING_HOOK);
                    entries.accept(ModItems.GOLDEN_FISHING_HOOK);
                    entries.accept(ModItems.DIAMOND_FISHING_HOOK);
                    entries.accept(ModItems.NETHERITE_FISHING_HOOK);
                    entries.accept(ModItems.COPPER_LACED_FISHING_LINE);
                    entries.accept(ModItems.IRON_LACED_FISHING_LINE);
                    entries.accept(ModItems.PRISMARINE_LACED_FISHING_LINE);
                    entries.accept(ModItems.GOLDEN_LACED_FISHING_LINE);
                    entries.accept(ModItems.DIAMOND_LACED_FISHING_LINE);
                    entries.accept(ModItems.NETHERITE_LACED_FISHING_LINE);
                    entries.accept(ModItems.COPPER_SINKER);
                    entries.accept(ModItems.IRON_SINKER);
                    entries.accept(ModItems.PRISMARINE_SINKER);
                    entries.accept(ModItems.GOLDEN_SINKER);
                    entries.accept(ModItems.DIAMOND_SINKER);
                    entries.accept(ModItems.NETHERITE_SINKER);
                    entries.accept(ModItems.MUSIC_DISC_AQUA);

                    entries.accept(ModBlocks.BOOM_BOX.asItem());
                    entries.accept(ModItems.DYNAMITE);
                    entries.accept(ModItems.FLARE_GUN);
                    entries.accept(ModItems.LAVA_BOTTLE);
                    entries.accept(ModItems.SPLASH_LAVA_BOTTLE);
                    entries.accept(ModItems.LINGERING_LAVA_BOTTLE);
                    entries.accept(ModItems.MILK_BOTTLE);
                    entries.accept(ModItems.SPLASH_MILK_BOTTLE);
                    entries.accept(ModItems.LINGERING_MILK_BOTTLE);
                    entries.accept(ModItems.SPLASH_HONEY_BOTTLE);
                    entries.accept(ModItems.LINGERING_HONEY_BOTTLE);

                    entries.accept(ModBlocks.INDUCTOR_RAIL.asItem());
                    entries.accept(ModBlocks.EXPOSED_INDUCTOR_RAIL.asItem());
                    entries.accept(ModBlocks.WEATHERED_INDUCTOR_RAIL.asItem());
                    entries.accept(ModBlocks.OXIDIZED_INDUCTOR_RAIL.asItem());
                    entries.accept(ModBlocks.WAXED_INDUCTOR_RAIL.asItem());
                    entries.accept(ModBlocks.WAXED_EXPOSED_INDUCTOR_RAIL.asItem());
                    entries.accept(ModBlocks.WAXED_WEATHERED_INDUCTOR_RAIL.asItem());
                    entries.accept(ModBlocks.WAXED_OXIDIZED_INDUCTOR_RAIL.asItem());

                    entries.accept(ModBlocks.REINFORCED_OAK_DOOR.asItem());
                    entries.accept(ModBlocks.REINFORCED_SPRUCE_DOOR.asItem());
                    entries.accept(ModBlocks.REINFORCED_BIRCH_DOOR.asItem());
                    entries.accept(ModBlocks.REINFORCED_JUNGLE_DOOR.asItem());
                    entries.accept(ModBlocks.REINFORCED_ACACIA_DOOR.asItem());
                    entries.accept(ModBlocks.REINFORCED_CHERRY_DOOR.asItem());
                    entries.accept(ModBlocks.REINFORCED_DARK_OAK_DOOR.asItem());
                    entries.accept(ModBlocks.REINFORCED_PALE_OAK_DOOR.asItem());
                    entries.accept(ModBlocks.REINFORCED_MANGROVE_DOOR.asItem());
                    entries.accept(ModBlocks.REINFORCED_BAMBOO_DOOR.asItem());
                    entries.accept(ModBlocks.REINFORCED_CRIMSON_DOOR.asItem());
                    entries.accept(ModBlocks.REINFORCED_WARPED_DOOR.asItem());
                    entries.accept(ModBlocks.REINFORCED_COPPER_DOOR.asItem());
                    entries.accept(ModBlocks.REINFORCED_EXPOSED_COPPER_DOOR.asItem());
                    entries.accept(ModBlocks.REINFORCED_OXIDIZED_COPPER_DOOR.asItem());
                    entries.accept(ModBlocks.REINFORCED_WEATHERED_COPPER_DOOR.asItem());
                    entries.accept(ModBlocks.REINFORCED_WAXED_COPPER_DOOR.asItem());
                    entries.accept(ModBlocks.REINFORCED_WAXED_EXPOSED_COPPER_DOOR.asItem());
                    entries.accept(ModBlocks.REINFORCED_WAXED_OXIDIZED_COPPER_DOOR.asItem());
                    entries.accept(ModBlocks.REINFORCED_WAXED_WEATHERED_COPPER_DOOR.asItem());
                })
                .build();
        registerCreativeTab(PRESERVED_INFERNO_GROUP);
    }

    private static void registerCreativeTab(CreativeModeTab tab){
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, FabricModItemGroups.PRESERVED_INFERNO_TAB_KEY, tab);
    }

    public static void registerItemGroups() {
        register();
    }
}
