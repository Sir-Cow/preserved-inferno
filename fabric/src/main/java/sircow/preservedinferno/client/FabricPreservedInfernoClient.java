package sircow.preservedinferno.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.MenuTypes;
import sircow.preservedinferno.block.ModBlocks;
import sircow.preservedinferno.components.ModComponents;
import sircow.preservedinferno.entity.ModEntities;
import sircow.preservedinferno.item.ModItems;
import sircow.preservedinferno.other.ModTags;
import sircow.preservedinferno.screen.CacheScreen;
import sircow.preservedinferno.screen.PreservedCauldronScreen;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class FabricPreservedInfernoClient implements ClientModInitializer {
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    public void onInitializeClient() {
        registerMenuScreens();
        registerEntities();
        configureRailRenderLayers();
        registerCustomTooltip();
    }

    private void registerMenuScreens() {
        MenuScreens.register(Constants.ANGLING_TABLE_MENU_TYPE.get(), AnglingTableScreen::new);
        MenuScreens.register(MenuTypes.CACHE_MENU_TYPE.get(), CacheScreen::new);
        MenuScreens.register(Constants.PRESERVED_ENCHANT_MENU_TYPE.get(), PreservedEnchantingTableScreen::new);
        MenuScreens.register(Constants.PRESERVED_FLETCHING_TABLE_MENU_TYPE.get(), PreservedFletchingTableScreen::new);
        MenuScreens.register(Constants.PRESERVED_LOOM_MENU_TYPE.get(), PreservedLoomScreen::new);
        MenuScreens.register(MenuTypes.PRESERVED_CAULDRON_MENU_TYPE.get(), PreservedCauldronScreen::new);
    }

    private void registerEntities() {
        EntityRendererRegistry.register(ModEntities.FLARE_GUN_PROJECTILE, (ThrownItemRenderer::new));
    }

    private void configureRailRenderLayers() {
        BlockRenderLayerMap.putBlocks(ChunkSectionLayer.CUTOUT,
                ModBlocks.INDUCTOR_RAIL,
                ModBlocks.EXPOSED_INDUCTOR_RAIL,
                ModBlocks.WEATHERED_INDUCTOR_RAIL,
                ModBlocks.OXIDIZED_INDUCTOR_RAIL,
                ModBlocks.WAXED_INDUCTOR_RAIL,
                ModBlocks.WAXED_EXPOSED_INDUCTOR_RAIL,
                ModBlocks.WAXED_WEATHERED_INDUCTOR_RAIL,
                ModBlocks.WAXED_OXIDIZED_INDUCTOR_RAIL
        );
    }

    private void registerCustomTooltip() {
        ItemTooltipCallback.EVENT.register((stack, context, tooltipType, lines) -> {
            String durabilityTranslatable = Component.translatable("item.durability").getString();
            String textBeforeSplit = durabilityTranslatable.substring(0, durabilityTranslatable.indexOf(':')).trim();
            int insertIndex = findTooltipInsertIndex(lines, textBeforeSplit);
            Integer maxStamina = stack.get(ModComponents.SHIELD_MAX_STAMINA_COMPONENT);
            Float staminaRegenRate = stack.get(ModComponents.SHIELD_REGEN_RATE_COMPONENT);
            String particleVal = stack.get(ModComponents.FLARE_PARTICLE_COMPONENT);

            if (maxStamina != null) {
                addShieldTooltip(lines, insertIndex, maxStamina, staminaRegenRate);
            }
            if (stack.is(ModTags.ROD_UPGRADES)) {
                addFishingUpgradeTooltip(lines, insertIndex, stack.getItem());
            }
            if (stack.is(ModItems.FLARE_GUN)) {
                if (particleVal != null) {
                    if (particleVal.equals("0xFFFFFF")) {
                        particleVal = "#FFFFFF";
                    }
                    int parsedParticleVal = Integer.parseInt(particleVal.replace("#", ""), 16);
                    lines.add(insertIndex, Component.translatable("item.color", Component.literal(particleVal).withStyle(Style.EMPTY.withColor(parsedParticleVal))).withStyle(ChatFormatting.GRAY));
                }
            }
            addSmithingTemplateTooltip(lines, insertIndex, stack);
        });
    }

    private int findTooltipInsertIndex(List<Component> lines, String textBeforeSplit) {
        for (int i = 0; i < lines.size(); i++) {
            String lineString = lines.get(i).getString();
            if (lineString.contains(textBeforeSplit) || (!lineString.contains(textBeforeSplit) && lineString.contains("pinferno"))) {
                return i;
            }
        }
        return lines.size();
    }

    private void addShieldTooltip(List<Component> lines, int insertIndex, Integer maxStamina, Float staminaRegenRate) {
        lines.add(insertIndex++, Component.empty());
        lines.add(insertIndex++, Component.translatable("item.modifiers.offhand").withStyle(ChatFormatting.GRAY));
        lines.add(insertIndex++, Component.literal(" ").append(Component.translatable("item.pinferno.shield_max_stamina", maxStamina).withStyle(ChatFormatting.DARK_GREEN)));
        lines.add(insertIndex++, Component.empty());
        lines.add(insertIndex++, Component.translatable("item.pinferno.modifiers.not_active").withStyle(ChatFormatting.GRAY));
        lines.add(insertIndex, Component.literal(" ").append(Component.translatable("item.pinferno.shield_regen_rate", df.format(staminaRegenRate * 20)).withStyle(ChatFormatting.BLUE)));
    }

    private void addSmithingTemplateTooltip(List<Component> lines, int insertIndex, ItemStack stack) {
        if (stack.is(ModItems.NETHER_ALLOY_UPGRADE_SMITHING_TEMPLATE)) {
            lines.add(insertIndex++, Component.translatable("item.minecraft.smithing_template").withStyle(ChatFormatting.GRAY));
            lines.add(insertIndex++, Component.empty());
            lines.add(insertIndex++, Component.translatable("item.minecraft.smithing_template.applies_to").withStyle(ChatFormatting.GRAY));
            lines.add(insertIndex++, Component.literal(" ").append(Component.translatable("item.pinferno.helmets").withStyle(ChatFormatting.BLUE)));
            lines.add(insertIndex++, Component.translatable("item.minecraft.smithing_template.ingredients").withStyle(ChatFormatting.GRAY));
            lines.add(insertIndex, Component.literal(" ").append(Component.translatable("item.pinferno.nether_alloy_ingot").withStyle(ChatFormatting.BLUE)));
        }
        if (stack.is(ModItems.ECHOING_PRISM_UPGRADE_SMITHING_TEMPLATE)) {
            lines.add(insertIndex++, Component.translatable("item.minecraft.smithing_template").withStyle(ChatFormatting.GRAY));
            lines.add(insertIndex++, Component.empty());
            lines.add(insertIndex++, Component.translatable("item.minecraft.smithing_template.applies_to").withStyle(ChatFormatting.GRAY));
            lines.add(insertIndex++, Component.literal(" ").append(Component.translatable("item.pinferno.leggings").withStyle(ChatFormatting.BLUE)));
            lines.add(insertIndex++, Component.translatable("item.minecraft.smithing_template.ingredients").withStyle(ChatFormatting.GRAY));
            lines.add(insertIndex, Component.literal(" ").append(Component.translatable("item.pinferno.echoing_prism").withStyle(ChatFormatting.BLUE)));
        }
    }

    private void addFishingUpgradeTooltip(List<Component> lines, int insertIndex, Item item) {
        lines.add(insertIndex++, Component.empty());
        lines.add(insertIndex++, Component.translatable("item.pinferno.modifiers.on_rod").withStyle(ChatFormatting.GRAY));

        Map<Item, Double> fishingSpeedMap = Map.of(
                ModItems.COPPER_FISHING_HOOK, 0.5,
                ModItems.PRISMARINE_FISHING_HOOK, 1.5,
                ModItems.IRON_FISHING_HOOK, 1.0,
                ModItems.GOLDEN_FISHING_HOOK, 3.0,
                ModItems.DIAMOND_FISHING_HOOK, 2.0,
                ModItems.NETHERITE_FISHING_HOOK, 3.0
        );
        Map<Item, Double> fortuneMap = Map.of(
                ModItems.COPPER_LACED_FISHING_LINE, 0.5,
                ModItems.PRISMARINE_LACED_FISHING_LINE, 1.5,
                ModItems.IRON_LACED_FISHING_LINE, 1.0,
                ModItems.GOLDEN_LACED_FISHING_LINE, 3.0,
                ModItems.DIAMOND_LACED_FISHING_LINE, 2.0,
                ModItems.NETHERITE_LACED_FISHING_LINE, 3.0
        );
        Map<Item, Double> luckMap = Map.of(
                ModItems.COPPER_SINKER, 0.5,
                ModItems.PRISMARINE_SINKER, 1.5,
                ModItems.IRON_SINKER, 1.0,
                ModItems.GOLDEN_SINKER, 3.0,
                ModItems.DIAMOND_SINKER, 2.0,
                ModItems.NETHERITE_SINKER, 3.0
        );

        addIfPresent(lines, insertIndex, item, fishingSpeedMap, "item.pinferno.modifiers.fishing_speed");
        addIfPresent(lines, insertIndex, item, fortuneMap, "item.pinferno.modifiers.fortune");
        addIfPresent(lines, insertIndex, item, luckMap, "item.pinferno.modifiers.luck");
    }

    private void addIfPresent(List<Component> lines, int insertIndex, Item item, Map<Item, Double> map, String translationKey) {
        if (map.containsKey(item)) {
            lines.add(insertIndex, Component.literal(" ").append(Component.translatable(translationKey, map.get(item)).withStyle(ChatFormatting.BLUE)));
        }
    }
}
