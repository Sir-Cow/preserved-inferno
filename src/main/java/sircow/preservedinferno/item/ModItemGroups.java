package sircow.preservedinferno.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import sircow.preservedinferno.PreservedInferno;
import sircow.preservedinferno.block.ModBlocks;

public class ModItemGroups {
    public static final ItemGroup preservedinferno_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(PreservedInferno.MOD_ID, "preservedinferno"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.preservedinferno.preservedinferno_items"))
                    .icon(() -> new ItemStack(ModBlocks.INDUCTOR_RAIL)).entries((displayContext, entries) -> {
                        entries.add(ModItems.RAW_HIDE);
                        entries.add(ModItems.PHANTOM_SINEW);
                        entries.add(ModItems.HOLLOW_TWINE);
                        entries.add(ModItems.DREAMCATCHER);

                        entries.add(ModItems.RAW_IRON_CHUNK);
                        entries.add(ModItems.RAW_GOLD_CHUNK);
                        entries.add(ModItems.RAW_COPPER_CHUNK);
                        entries.add(ModItems.COPPER_NUGGET);

                        entries.add(ModItems.BLACK_CLOTH);
                        entries.add(ModItems.BLUE_CLOTH);
                        entries.add(ModItems.BROWN_CLOTH);
                        entries.add(ModItems.CYAN_CLOTH);
                        entries.add(ModItems.GRAY_CLOTH);
                        entries.add(ModItems.GREEN_CLOTH);
                        entries.add(ModItems.LIGHT_BLUE_CLOTH);
                        entries.add(ModItems.LIGHT_GRAY_CLOTH);
                        entries.add(ModItems.LIME_CLOTH);
                        entries.add(ModItems.MAGENTA_CLOTH);
                        entries.add(ModItems.ORANGE_CLOTH);
                        entries.add(ModItems.PINK_CLOTH);
                        entries.add(ModItems.PURPLE_CLOTH);
                        entries.add(ModItems.RED_CLOTH);
                        entries.add(ModItems.WHITE_CLOTH);
                        entries.add(ModItems.YELLOW_CLOTH);

                        entries.add(ModItems.NEW_CAULDRON_BLOCK);
                        entries.add(ModItems.NEW_LOOM_BLOCK);
                        entries.add(ModItems.NEW_FLETCHING_TABLE_BLOCK);
                        entries.add(ModItems.NEW_ENCHANTING_TABLE_BLOCK);

                        entries.add(ModItems.INDUCTOR_RAIL);
                        entries.add(ModItems.EXPOSED_INDUCTOR_RAIL);
                        entries.add(ModItems.WEATHERED_INDUCTOR_RAIL);
                        entries.add(ModItems.OXIDIZED_INDUCTOR_RAIL);
                        entries.add(ModItems.WAXED_INDUCTOR_RAIL);
                        entries.add(ModItems.WAXED_EXPOSED_INDUCTOR_RAIL);
                        entries.add(ModItems.WAXED_WEATHERED_INDUCTOR_RAIL);
                        entries.add(ModItems.WAXED_OXIDIZED_INDUCTOR_RAIL);
                    }).build());

    public static void registerItemGroups() {
        PreservedInferno.LOGGER.info("Registering Mod Item Groups for " + PreservedInferno.MOD_ID);
    }
}
