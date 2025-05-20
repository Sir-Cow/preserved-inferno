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
    public static final ResourceKey<CreativeModeTab> PRESERVED_INFERNO_TAB_KEY =
            ResourceKey.create(Registries.CREATIVE_MODE_TAB, Constants.id("pinferno"));

    public static CreativeModeTab PRESERVED_INFERNO_GROUP;

    public static void register() {
        PRESERVED_INFERNO_GROUP = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                .title(Component.translatable("itemgroup.pinferno.items"))
                .icon(() -> new ItemStack(ModItems.DREAMCATCHER))
                .displayItems((displayContext, entries) -> {
                    entries.accept(ModItems.DREAMCATCHER);
                    entries.accept(ModItems.ELDER_GUARDIAN_SPINE);
                    entries.accept(ModItems.HOLLOW_TWINE);
                    entries.accept(ModItems.PHANTOM_SINEW);
                    entries.accept(ModItems.RAW_HIDE);
                    entries.accept(ModItems.LEATHER_FABRIC);
                    entries.accept(ModItems.GILDEN_BERRIES);

                    entries.accept(ModItems.COPPER_NUGGET);
                    entries.accept(ModItems.RAW_COPPER_CHUNK);
                    entries.accept(ModItems.RAW_IRON_CHUNK);
                    entries.accept(ModItems.RAW_GOLD_CHUNK);

                    entries.accept(ModItems.COPPER_SHOVEL);
                    entries.accept(ModItems.COPPER_PICKAXE);
                    entries.accept(ModItems.COPPER_AXE);
                    entries.accept(ModItems.COPPER_SCYTHE);
                    entries.accept(ModItems.COPPER_SWORD);

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

                    entries.accept(ModItems.NETHER_GOLD_PLATE);
                    entries.accept(ModItems.NETHER_ALLOY_INGOT);
                    entries.accept(ModItems.NETHER_ALLOY_UPGRADE_SMITHING_TEMPLATE);

                    entries.accept(ModBlocks.ANGLING_TABLE.asItem());
                    entries.accept(ModItems.AQUATIC_FIBER);
                    entries.accept(FabricModItems.CACHE);
                    entries.accept(ModItems.COPPER_FISHING_HOOK);
                    entries.accept(ModItems.IRON_FISHING_HOOK);
                    entries.accept(ModItems.DIAMOND_FISHING_HOOK);
                    entries.accept(ModItems.NETHERITE_FISHING_HOOK);
                    entries.accept(ModItems.COPPER_LACED_FISHING_LINE);
                    entries.accept(ModItems.IRON_LACED_FISHING_LINE);
                    entries.accept(ModItems.DIAMOND_LACED_FISHING_LINE);
                    entries.accept(ModItems.NETHERITE_LACED_FISHING_LINE);
                    entries.accept(ModItems.COPPER_SINKER);
                    entries.accept(ModItems.IRON_SINKER);
                    entries.accept(ModItems.DIAMOND_SINKER);
                    entries.accept(ModItems.NETHERITE_SINKER);
                    entries.accept(ModItems.MUSIC_DISC_AQUA);

                    entries.accept(ModBlocks.INDUCTOR_RAIL.asItem());
                    entries.accept(ModBlocks.EXPOSED_INDUCTOR_RAIL.asItem());
                    entries.accept(ModBlocks.WEATHERED_INDUCTOR_RAIL.asItem());
                    entries.accept(ModBlocks.OXIDIZED_INDUCTOR_RAIL.asItem());
                    entries.accept(ModBlocks.WAXED_INDUCTOR_RAIL.asItem());
                    entries.accept(ModBlocks.WAXED_EXPOSED_INDUCTOR_RAIL.asItem());
                    entries.accept(ModBlocks.WAXED_WEATHERED_INDUCTOR_RAIL.asItem());
                    entries.accept(ModBlocks.WAXED_OXIDIZED_INDUCTOR_RAIL.asItem());
                })
                .build();
        registerCreativeTab(PRESERVED_INFERNO_GROUP);
    }

    private static void registerCreativeTab(CreativeModeTab tab){
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, FabricModItemGroups.PRESERVED_INFERNO_TAB_KEY, tab);
    }

    public static void registerItemGroups() {
        register();
        //Constants.LOG.info("Registering Mod Item Groups for " + Constants.MOD_ID);
    }
}
