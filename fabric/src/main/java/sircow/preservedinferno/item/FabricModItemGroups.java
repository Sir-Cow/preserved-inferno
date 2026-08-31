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
                .icon(() -> new ItemStack(ModItems.DREAMCATCHER.get()))
                .displayItems((displayContext, entries) -> {
                    entries.accept(ModBlocks.RHYOLITE.get().asItem());
                    entries.accept(ModBlocks.POLISHED_RHYOLITE.get().asItem());
                    entries.accept(ModBlocks.RHYOLITE_BRICKS.get().asItem());
                    entries.accept(ModBlocks.CRACKED_RHYOLITE_BRICKS.get().asItem());
                    entries.accept(ModBlocks.RHYOLITE_WALL.get().asItem());
                    entries.accept(ModBlocks.RHYOLITE_BRICK_WALL.get().asItem());
                    entries.accept(ModBlocks.RHYOLITE_STAIRS.get().asItem());
                    entries.accept(ModBlocks.POLISHED_RHYOLITE_STAIRS.get().asItem());
                    entries.accept(ModBlocks.RHYOLITE_BRICK_STAIRS.get().asItem());
                    entries.accept(ModBlocks.RHYOLITE_SLAB.get().asItem());
                    entries.accept(ModBlocks.POLISHED_RHYOLITE_SLAB.get().asItem());
                    entries.accept(ModBlocks.RHYOLITE_BRICK_SLAB.get().asItem());

                    entries.accept(ModBlocks.SPARKLING_BLACKSTONE.get().asItem());

                    entries.accept(ModItems.REVERB_COMPASS.get());
                    entries.accept(ModItems.DREAMCATCHER.get());
                    entries.accept(ModItems.ELDER_GUARDIAN_SPINE.get());
                    entries.accept(ModItems.HOLLOW_TWINE.get());
                    entries.accept(ModItems.PHANTOM_SINEW.get());
                    entries.accept(ModItems.RAW_HIDE.get());
                    entries.accept(ModItems.LEATHER_FABRIC.get());
                    entries.accept(ModItems.GILDEN_BERRIES.get());
                    entries.accept(ModItems.RESIN_SPECK.get());

                    entries.accept(ModItems.ECHOING_PRISM.get());
                    entries.accept(ModItems.ECHOING_PRISM_UPGRADE_SMITHING_TEMPLATE.get());
                    entries.accept(ModItems.SCULK_INFUSION.get());

                    entries.accept(ModItems.REPAIR_KIT.get());
                    entries.accept(ModItems.FORGE_DUST.get());

                    entries.accept(ModItems.RAW_COPPER_CHUNK.get());
                    entries.accept(ModItems.RAW_IRON_CHUNK.get());
                    entries.accept(ModItems.RAW_GOLD_CHUNK.get());

                    entries.accept(ModItems.QUARTZITE.get());
                    entries.accept(ModItems.QUARTZITE_SHOVEL.get());
                    entries.accept(ModItems.QUARTZITE_PICKAXE.get());
                    entries.accept(ModItems.QUARTZITE_AXE.get());
                    entries.accept(ModItems.QUARTZITE_SCYTHE.get());
                    entries.accept(ModItems.QUARTZITE_SWORD.get());

                    entries.accept(ModItems.COPPER_TRIDENT.get());

                    entries.accept(ModItems.WOODEN_MULTITOOL.get());
                    entries.accept(ModItems.STONE_MULTITOOL.get());
                    entries.accept(ModItems.COPPER_MULTITOOL.get());
                    entries.accept(ModItems.IRON_MULTITOOL.get());
                    entries.accept(ModItems.DIAMOND_MULTITOOL.get());
                    entries.accept(ModItems.NETHERITE_MULTITOOL.get());
                    entries.accept(ModItems.QUARTZITE_MULTITOOL.get());
                    entries.accept(ModItems.GOLDEN_MULTITOOL.get());
                    entries.accept(ModItems.NETHER_ALLOY_MULTITOOL.get());

                    entries.accept(ModItems.BLACK_CLOTH.get());
                    entries.accept(ModItems.BLUE_CLOTH.get());
                    entries.accept(ModItems.BROWN_CLOTH.get());
                    entries.accept(ModItems.CYAN_CLOTH.get());
                    entries.accept(ModItems.GRAY_CLOTH.get());
                    entries.accept(ModItems.GREEN_CLOTH.get());
                    entries.accept(ModItems.LIGHT_BLUE_CLOTH.get());
                    entries.accept(ModItems.LIGHT_GRAY_CLOTH.get());
                    entries.accept(ModItems.LIME_CLOTH.get());
                    entries.accept(ModItems.MAGENTA_CLOTH.get());
                    entries.accept(ModItems.ORANGE_CLOTH.get());
                    entries.accept(ModItems.PINK_CLOTH.get());
                    entries.accept(ModItems.PURPLE_CLOTH.get());
                    entries.accept(ModItems.RED_CLOTH.get());
                    entries.accept(ModItems.WHITE_CLOTH.get());
                    entries.accept(ModItems.YELLOW_CLOTH.get());

                    entries.accept(ModItems.COPPER_SHIELD.get());
                    entries.accept(ModItems.IRON_SHIELD.get());
                    entries.accept(ModItems.GOLDEN_SHIELD.get());
                    entries.accept(ModItems.DIAMOND_SHIELD.get());
                    entries.accept(ModItems.NETHERITE_SHIELD.get());

                    entries.accept(ModItems.NETHER_ALLOY_PLATE.get());
                    entries.accept(ModItems.NETHER_ALLOY_INGOT.get());
                    entries.accept(ModItems.NETHER_ALLOY_UPGRADE_SMITHING_TEMPLATE.get());
                    entries.accept(ModItems.NETHER_ALLOY_SHOVEL.get());
                    entries.accept(ModItems.NETHER_ALLOY_PICKAXE.get());
                    entries.accept(ModItems.NETHER_ALLOY_AXE.get());
                    entries.accept(ModItems.NETHER_ALLOY_SCYTHE.get());
                    entries.accept(ModItems.NETHER_ALLOY_SWORD.get());

                    entries.accept(ModBlocks.ANGLING_TABLE.get().asItem());
                    entries.accept(ModItems.AQUATIC_FIBER.get());
                    entries.accept(ModItems.CACHE.get());
                    entries.accept(ModItems.COPPER_FISHING_HOOK.get());
                    entries.accept(ModItems.IRON_FISHING_HOOK.get());
                    entries.accept(ModItems.PRISMARINE_FISHING_HOOK.get());
                    entries.accept(ModItems.GOLDEN_FISHING_HOOK.get());
                    entries.accept(ModItems.DIAMOND_FISHING_HOOK.get());
                    entries.accept(ModItems.NETHERITE_FISHING_HOOK.get());
                    entries.accept(ModItems.COPPER_LACED_FISHING_LINE.get());
                    entries.accept(ModItems.IRON_LACED_FISHING_LINE.get());
                    entries.accept(ModItems.PRISMARINE_LACED_FISHING_LINE.get());
                    entries.accept(ModItems.GOLDEN_LACED_FISHING_LINE.get());
                    entries.accept(ModItems.DIAMOND_LACED_FISHING_LINE.get());
                    entries.accept(ModItems.NETHERITE_LACED_FISHING_LINE.get());
                    entries.accept(ModItems.COPPER_SINKER.get());
                    entries.accept(ModItems.IRON_SINKER.get());
                    entries.accept(ModItems.PRISMARINE_SINKER.get());
                    entries.accept(ModItems.GOLDEN_SINKER.get());
                    entries.accept(ModItems.DIAMOND_SINKER.get());
                    entries.accept(ModItems.NETHERITE_SINKER.get());
                    entries.accept(ModItems.MUSIC_DISC_AQUA.get());

                    entries.accept(ModBlocks.BOOM_BOX.get().asItem());
                    entries.accept(ModItems.DYNAMITE.get());
                    entries.accept(ModItems.FLARE_GUN.get());
                    entries.accept(ModItems.LAVA_BOTTLE.get());
                    entries.accept(ModItems.SPLASH_LAVA_BOTTLE.get());
                    entries.accept(ModItems.LINGERING_LAVA_BOTTLE.get());
                    entries.accept(ModItems.MILK_BOTTLE.get());
                    entries.accept(ModItems.SPLASH_MILK_BOTTLE.get());
                    entries.accept(ModItems.LINGERING_MILK_BOTTLE.get());
                    entries.accept(ModItems.SPLASH_HONEY_BOTTLE.get());
                    entries.accept(ModItems.LINGERING_HONEY_BOTTLE.get());

                    entries.accept(ModBlocks.INDUCTOR_RAIL.get().asItem());
                    entries.accept(ModBlocks.EXPOSED_INDUCTOR_RAIL.get().asItem());
                    entries.accept(ModBlocks.WEATHERED_INDUCTOR_RAIL.get().asItem());
                    entries.accept(ModBlocks.OXIDIZED_INDUCTOR_RAIL.get().asItem());
                    entries.accept(ModBlocks.WAXED_INDUCTOR_RAIL.get().asItem());
                    entries.accept(ModBlocks.WAXED_EXPOSED_INDUCTOR_RAIL.get().asItem());
                    entries.accept(ModBlocks.WAXED_WEATHERED_INDUCTOR_RAIL.get().asItem());
                    entries.accept(ModBlocks.WAXED_OXIDIZED_INDUCTOR_RAIL.get().asItem());

                    entries.accept(ModBlocks.REINFORCED_OAK_DOOR.get().asItem());
                    entries.accept(ModBlocks.REINFORCED_SPRUCE_DOOR.get().asItem());
                    entries.accept(ModBlocks.REINFORCED_BIRCH_DOOR.get().asItem());
                    entries.accept(ModBlocks.REINFORCED_JUNGLE_DOOR.get().asItem());
                    entries.accept(ModBlocks.REINFORCED_ACACIA_DOOR.get().asItem());
                    entries.accept(ModBlocks.REINFORCED_CHERRY_DOOR.get().asItem());
                    entries.accept(ModBlocks.REINFORCED_DARK_OAK_DOOR.get().asItem());
                    entries.accept(ModBlocks.REINFORCED_PALE_OAK_DOOR.get().asItem());
                    entries.accept(ModBlocks.REINFORCED_MANGROVE_DOOR.get().asItem());
                    entries.accept(ModBlocks.REINFORCED_BAMBOO_DOOR.get().asItem());
                    entries.accept(ModBlocks.REINFORCED_CRIMSON_DOOR.get().asItem());
                    entries.accept(ModBlocks.REINFORCED_WARPED_DOOR.get().asItem());
                    entries.accept(ModBlocks.REINFORCED_COPPER_DOOR.get().asItem());
                    entries.accept(ModBlocks.REINFORCED_EXPOSED_COPPER_DOOR.get().asItem());
                    entries.accept(ModBlocks.REINFORCED_OXIDIZED_COPPER_DOOR.get().asItem());
                    entries.accept(ModBlocks.REINFORCED_WEATHERED_COPPER_DOOR.get().asItem());
                    entries.accept(ModBlocks.REINFORCED_WAXED_COPPER_DOOR.get().asItem());
                    entries.accept(ModBlocks.REINFORCED_WAXED_EXPOSED_COPPER_DOOR.get().asItem());
                    entries.accept(ModBlocks.REINFORCED_WAXED_OXIDIZED_COPPER_DOOR.get().asItem());
                    entries.accept(ModBlocks.REINFORCED_WAXED_WEATHERED_COPPER_DOOR.get().asItem());
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
