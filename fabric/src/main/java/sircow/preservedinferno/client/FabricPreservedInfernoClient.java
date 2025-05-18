package sircow.preservedinferno.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.MenuTypes;
import sircow.preservedinferno.block.ModBlocks;
import sircow.preservedinferno.components.ModComponents;
import sircow.preservedinferno.item.ModItems;
import sircow.preservedinferno.screen.CacheScreen;
import sircow.preservedinferno.screen.PreservedCauldronScreen;

import java.text.DecimalFormat;

public class FabricPreservedInfernoClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // menus
        MenuScreens.register(Constants.ANGLING_TABLE_MENU_TYPE.get(), AnglingTableScreen::new);
        MenuScreens.register(MenuTypes.CACHE_MENU_TYPE.get(), CacheScreen::new);
        MenuScreens.register(Constants.PRESERVED_ENCHANT_MENU_TYPE.get(), PreservedEnchantingTableScreen::new);
        MenuScreens.register(Constants.PRESERVED_FLETCHING_TABLE_MENU_TYPE.get(), PreservedFletchingTableScreen::new);
        MenuScreens.register(Constants.PRESERVED_LOOM_MENU_TYPE.get(), PreservedLoomScreen::new);
        MenuScreens.register(MenuTypes.PRESERVED_CAULDRON_MENU_TYPE.get(), PreservedCauldronScreen::new);

        // enable rail textures to be transparent
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.INDUCTOR_RAIL, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.EXPOSED_INDUCTOR_RAIL, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WEATHERED_INDUCTOR_RAIL, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.OXIDIZED_INDUCTOR_RAIL, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WAXED_INDUCTOR_RAIL, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WAXED_EXPOSED_INDUCTOR_RAIL, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WAXED_WEATHERED_INDUCTOR_RAIL, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WAXED_OXIDIZED_INDUCTOR_RAIL, RenderType.cutout());

        // custom tooltip
        ItemTooltipCallback.EVENT.register((stack, context, tooltipType, lines) -> {
            // shields
            Integer maxStamina = stack.get(ModComponents.SHIELD_MAX_STAMINA_COMPONENT);
            Float staminaRegenRate = stack.get(ModComponents.SHIELD_REGEN_RATE_COMPONENT);
            DecimalFormat df = new DecimalFormat("#.####");
            if (maxStamina != null) {
                int insertIndex = lines.size();
                for (int i = 0; i < lines.size(); i++) {
                    if (lines.get(i).getString().contains("pinferno")) {
                        insertIndex = i;
                        break;
                    }
                }
                lines.add(insertIndex++, Component.empty());
                lines.add(insertIndex++, Component.translatable("item.modifiers.offhand").withStyle(ChatFormatting.GRAY));
                lines.add(insertIndex++, Component.translatable("item.pinferno.shield_max_stamina", maxStamina).withStyle(ChatFormatting.DARK_GREEN));
                lines.add(insertIndex++, Component.empty());
                lines.add(insertIndex++, Component.translatable("item.pinferno.modifiers.not_active").withStyle(ChatFormatting.GRAY));
                lines.add(insertIndex, Component.translatable("item.pinferno.shield_regen_rate", df.format(staminaRegenRate * 20)).withStyle(ChatFormatting.BLUE));
            }
            // fishing upgrades
            int insertIndex = lines.size();
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).getString().contains("pinferno")) {
                    insertIndex = i;
                    break;
                }
            }
            if (stack.is(ModItems.IRON_FISHING_HOOK)) {
                lines.add(insertIndex, Component.translatable("item.pinferno.modifiers.fishing_speed", 1).withStyle(ChatFormatting.BLUE));
            }
            if (stack.is(ModItems.DIAMOND_FISHING_HOOK)) {
                lines.add(insertIndex, Component.translatable("item.pinferno.modifiers.fishing_speed", 2).withStyle(ChatFormatting.BLUE));
            }
            if (stack.is(ModItems.NETHERITE_FISHING_HOOK)) {
                lines.add(insertIndex, Component.translatable("item.pinferno.modifiers.fishing_speed", 3).withStyle(ChatFormatting.BLUE));
            }
            if (stack.is(ModItems.IRON_LACED_FISHING_LINE)) {
                lines.add(insertIndex, Component.translatable("item.pinferno.modifiers.fortune", 1).withStyle(ChatFormatting.BLUE));
            }
            if (stack.is(ModItems.DIAMOND_LACED_FISHING_LINE)) {
                lines.add(insertIndex, Component.translatable("item.pinferno.modifiers.fortune", 2).withStyle(ChatFormatting.BLUE));
            }
            if (stack.is(ModItems.NETHERITE_LACED_FISHING_LINE)) {
                lines.add(insertIndex, Component.translatable("item.pinferno.modifiers.fortune", 3).withStyle(ChatFormatting.BLUE));
            }
            if (stack.is(ModItems.IRON_SINKER)) {
                lines.add(insertIndex, Component.translatable("item.pinferno.modifiers.luck", 1).withStyle(ChatFormatting.BLUE));
            }
            if (stack.is(ModItems.DIAMOND_SINKER)) {
                lines.add(insertIndex, Component.translatable("item.pinferno.modifiers.luck", 2).withStyle(ChatFormatting.BLUE));
            }
            if (stack.is(ModItems.NETHERITE_SINKER)) {
                lines.add(insertIndex, Component.translatable("item.pinferno.modifiers.luck", 3).withStyle(ChatFormatting.BLUE));
            }
        });
    }
}
