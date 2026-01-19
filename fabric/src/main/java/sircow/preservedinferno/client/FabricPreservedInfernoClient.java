package sircow.preservedinferno.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.MenuTypes;
import sircow.preservedinferno.PreservedInferno;
import sircow.preservedinferno.block.ModBlocks;
import sircow.preservedinferno.block.entity.PreservedCauldronBlockEntityRenderer;
import sircow.preservedinferno.components.ModComponents;
import sircow.preservedinferno.enchantment.ModEnchantments;
import sircow.preservedinferno.entity.ModEntities;
import sircow.preservedinferno.item.ModItems;
import sircow.preservedinferno.mixin.ClientAdvancementsAccessor;
import sircow.preservedinferno.network.ModMessages;
import sircow.preservedinferno.other.IMinecraftMixin;
import sircow.preservedinferno.other.ModTags;
import sircow.preservedinferno.screen.CacheScreen;
import sircow.preservedinferno.screen.PreservedCauldronScreen;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FabricPreservedInfernoClient implements ClientModInitializer {
    DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));
    public static boolean waitingForAdvancement, suppressNextOpen, hasTriggeredOnce, advancementsSynced, advancementGranted = false;
    public static int advancementDelayTicks = -1;
    private static int initialMessageDelay = 40;

    @Override
    public void onInitializeClient() {
        registerMenuScreens();
        registerEntities();
        configureBlockRenderLayers();
        registerCustomTooltip();
        tickAdvancement();
        BlockEntityRenderers.register(PreservedInferno.PRESERVED_CAULDRON_BLOCK_ENTITY, PreservedCauldronBlockEntityRenderer::new);
        ModMessages.registerS2CPackets();
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
        EntityRenderers.register(ModEntities.FLARE_GUN_PROJECTILE, (ThrownItemRenderer::new));
        EntityRenderers.register(ModEntities.COPPER_TRIDENT, (ThrownCopperTridentRenderer::new));
        EntityModelLayerRegistry.registerModelLayer(ThrownCopperTridentRenderer.COPPER_TRIDENT, CopperTridentModel::createLayer);
    }

    private void configureBlockRenderLayers() {
        BlockRenderLayerMap.putBlocks(ChunkSectionLayer.CUTOUT,
                ModBlocks.INDUCTOR_RAIL,
                ModBlocks.EXPOSED_INDUCTOR_RAIL,
                ModBlocks.WEATHERED_INDUCTOR_RAIL,
                ModBlocks.OXIDIZED_INDUCTOR_RAIL,
                ModBlocks.WAXED_INDUCTOR_RAIL,
                ModBlocks.WAXED_EXPOSED_INDUCTOR_RAIL,
                ModBlocks.WAXED_WEATHERED_INDUCTOR_RAIL,
                ModBlocks.WAXED_OXIDIZED_INDUCTOR_RAIL,
                ModBlocks.REINFORCED_OAK_DOOR,
                ModBlocks.REINFORCED_SPRUCE_DOOR,
                ModBlocks.REINFORCED_BIRCH_DOOR,
                ModBlocks.REINFORCED_JUNGLE_DOOR,
                ModBlocks.REINFORCED_ACACIA_DOOR,
                ModBlocks.REINFORCED_CHERRY_DOOR,
                ModBlocks.REINFORCED_DARK_OAK_DOOR,
                ModBlocks.REINFORCED_PALE_OAK_DOOR,
                ModBlocks.REINFORCED_MANGROVE_DOOR,
                ModBlocks.REINFORCED_BAMBOO_DOOR,
                ModBlocks.REINFORCED_CRIMSON_DOOR,
                ModBlocks.REINFORCED_WARPED_DOOR,
                ModBlocks.REINFORCED_COPPER_DOOR,
                ModBlocks.REINFORCED_EXPOSED_COPPER_DOOR,
                ModBlocks.REINFORCED_OXIDIZED_COPPER_DOOR,
                ModBlocks.REINFORCED_WEATHERED_COPPER_DOOR,
                ModBlocks.REINFORCED_WAXED_COPPER_DOOR,
                ModBlocks.REINFORCED_WAXED_EXPOSED_COPPER_DOOR,
                ModBlocks.REINFORCED_WAXED_OXIDIZED_COPPER_DOOR,
                ModBlocks.REINFORCED_WAXED_WEATHERED_COPPER_DOOR
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
            String hook = stack.get(ModComponents.HOOK_COMPONENT);
            String line = stack.get(ModComponents.LINE_COMPONENT);
            String sinker = stack.get(ModComponents.SINKER_COMPONENT);

            if (maxStamina != null && staminaRegenRate != null) {
                int displayedMaxStamina = maxStamina;
                float displayedRegenRate = staminaRegenRate;

                if (Minecraft.getInstance().level != null) {
                    var enchantmentRegistry = Minecraft.getInstance().level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);

                    int enduranceLevel = stack.getEnchantments().getLevel(enchantmentRegistry.getOrThrow(ModEnchantments.ENDURANCE));
                    displayedMaxStamina += (enduranceLevel * 4);

                    int vigorLevel = stack.getEnchantments().getLevel(enchantmentRegistry.getOrThrow(ModEnchantments.VIGOR));
                    if (vigorLevel > 0) {
                        displayedRegenRate *= (1.0f + (vigorLevel * 0.5f));
                    }
                }
                addShieldTooltip(lines, insertIndex, displayedMaxStamina, displayedRegenRate);
            }
            if (stack.is(ModTags.ROD_UPGRADES)) {
                addFishingUpgradeTooltip(lines, insertIndex, stack.getItem());
            }
            if ((hook != null && !hook.equals("none")) || (line != null && !line.equals("none")) || (sinker != null && !sinker.equals("none"))) {
                addFishingUpgradeTooltip(lines, insertIndex, hook, line, sinker);
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
            if (lineString.contains(textBeforeSplit) || (!lineString.contains(textBeforeSplit) && (lineString.contains("minecraft") || lineString.contains("pinferno")))) {
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

    private void addFishingUpgradeTooltip(List<Component> lines, int insertIndex, String hook, String line, String sinker) {
        lines.add(insertIndex++, Component.empty());
        lines.add(insertIndex++, Component.translatable("item.pinferno.modifiers.rod_in_hand").withStyle(ChatFormatting.GRAY));

        Map<String, Double> valuesMap = Map.of(
                "copper", 0.5,
                "iron", 1.0,
                "prismarine", 1.5,
                "golden", 1.5,
                "diamond", 2.0,
                "netherite", 3.0
        );

        if (valuesMap.containsKey(hook)) lines.add(insertIndex++, Component.translatable("item.pinferno.modifiers.fishing_speed", valuesMap.get(hook)).withStyle(ChatFormatting.BLUE));
        if (valuesMap.containsKey(line)) lines.add(insertIndex++, Component.translatable("item.pinferno.modifiers.fortune", valuesMap.get(line)).withStyle(ChatFormatting.BLUE));
        if (valuesMap.containsKey(sinker)) lines.add(insertIndex, Component.translatable("item.pinferno.modifiers.luck", valuesMap.get(sinker)).withStyle(ChatFormatting.BLUE));
    }

    private void addFishingUpgradeTooltip(List<Component> lines, int insertIndex, Item item) {
        lines.add(insertIndex++, Component.empty());
        lines.add(insertIndex++, Component.translatable("item.pinferno.modifiers.on_rod").withStyle(ChatFormatting.GRAY));

        Map<Item, Double> fishingSpeedMap = Map.of(
                ModItems.COPPER_FISHING_HOOK, 0.5,
                ModItems.IRON_FISHING_HOOK, 1.0,
                ModItems.PRISMARINE_FISHING_HOOK, 1.5,
                ModItems.GOLDEN_FISHING_HOOK, 1.5,
                ModItems.DIAMOND_FISHING_HOOK, 2.0,
                ModItems.NETHERITE_FISHING_HOOK, 3.0
        );
        Map<Item, Double> fortuneMap = Map.of(
                ModItems.COPPER_LACED_FISHING_LINE, 0.5,
                ModItems.IRON_LACED_FISHING_LINE, 1.0,
                ModItems.PRISMARINE_LACED_FISHING_LINE, 1.5,
                ModItems.GOLDEN_LACED_FISHING_LINE, 1.5,
                ModItems.DIAMOND_LACED_FISHING_LINE, 2.0,
                ModItems.NETHERITE_LACED_FISHING_LINE, 3.0
        );
        Map<Item, Double> luckMap = Map.of(
                ModItems.COPPER_SINKER, 0.5,
                ModItems.IRON_SINKER, 1.0,
                ModItems.PRISMARINE_SINKER, 1.5,
                ModItems.GOLDEN_SINKER, 1.5,
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

    private void tickAdvancement() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (advancementDelayTicks > 0) advancementDelayTicks--;
            else if (advancementDelayTicks == 0) {
                advancementDelayTicks = -1;
                ((IMinecraftMixin) client).startWaitForAdvancement(client, 0);
            }

            if (initialMessageDelay > 0) {
                initialMessageDelay--;
                return;
            }

            if (client.player == null) return;

            var advancements = client.player.connection.getAdvancements();
            var rootAdvancementHolder = advancements.get(Identifier.withDefaultNamespace("story/root"));
            if (rootAdvancementHolder != null) {
                var progressMap = ((ClientAdvancementsAccessor) advancements).getProgress();
                var rootProgress = progressMap.get(rootAdvancementHolder);
                advancementGranted = rootProgress != null && rootProgress.isDone();
            }
            else advancementGranted = false;

            if (!advancementGranted) {
                KeyMapping key = Minecraft.getInstance().options.keyAdvancements;
                Component actionbar = Component.translatable("advancement.pinferno.actionbar.open_advancements", Component.keybind(key.getName()));
                client.gui.setOverlayMessage(actionbar, false);
            }
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            advancementGranted = false;
            hasTriggeredOnce = false;
            suppressNextOpen = false;
            waitingForAdvancement = false;
            advancementDelayTicks = -1;
            advancementsSynced = false;
            initialMessageDelay = 40;
        });
    }
}
