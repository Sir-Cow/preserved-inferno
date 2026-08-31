package sircow.preservedinferno.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.equipment.trim.TrimMaterials;
import sircow.preservedinferno.component.ModComponents;
import sircow.preservedinferno.item.custom.*;
import sircow.preservedinferno.tag.ModTags;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModItems {
    static DecimalFormat df = new DecimalFormat("0.000", new DecimalFormatSymbols(Locale.US));
    static double COPPER_REGEN_PARSE = 1F / 30F;
    static double NETHERITE_REGEN_PARSE = 1F / 6F;
    static float COPPER_REGEN = Float.parseFloat(df.format(COPPER_REGEN_PARSE));
    static float IRON_REGEN = 3F / 50F;
    static float DIAMOND_REGEN = 1F / 10F;
    static float NETHERITE_REGEN = (float) NETHERITE_REGEN_PARSE;
    static float GOLD_REGEN = 1F / 10F;
    public static final ToolMaterial NETHER_ALLOY_TOOL = new ToolMaterial(ModTags.INCORRECT_FOR_NETHER_ALLOY_TOOL, 512, 16.0F, 0.0F, 5, ModTags.NETHER_ALLOY_TOOL_MATERIALS);
    public static final ToolMaterial QUARTZITE_TOOL = new ToolMaterial(ModTags.INCORRECT_FOR_QUARTZITE_TOOL, 128, 8.0F, 0.0F, 4, ModTags.QUARTZITE_TOOL_MATERIALS);

    public static final LinkedHashMap<ResourceKey<Item>, Supplier<Item>> ITEMS = new LinkedHashMap<>();

    public static final Supplier<Item> DREAMCATCHER = registerItem(ModItemIds.DREAMCATCHER, () -> new Item.Properties()
            .durability(0)
            .stacksTo(1)
    );
    public static final Supplier<Item> ELDER_GUARDIAN_SPINE = registerItem(ModItemIds.ELDER_GUARDIAN_SPINE, () -> new Item.Properties().rarity(Rarity.EPIC));
    public static final Supplier<Item> HOLLOW_TWINE = registerItem(ModItemIds.HOLLOW_TWINE, Item.Properties::new);
    public static final Supplier<Item> PHANTOM_SINEW = registerItem(ModItemIds.PHANTOM_SINEW, Item.Properties::new);
    public static final Supplier<Item> RAW_HIDE = registerItem(ModItemIds.RAW_HIDE, Item.Properties::new);
    public static final Supplier<Item> LEATHER_FABRIC = registerItem(ModItemIds.LEATHER_FABRIC, Item.Properties::new);
    public static final Supplier<Item> GILDEN_BERRIES = registerItem(ModItemIds.GILDEN_BERRIES, () -> new Item.Properties().food(
            new FoodProperties.Builder().nutrition(4).saturationModifier(1.2F).alwaysEdible().build(),
            defaultFood().consumeSeconds(0.8F).onConsume(new ApplyStatusEffectsConsumeEffect(List.of(new MobEffectInstance(MobEffects.REGENERATION, 100, 0)))).build()));
    public static final Supplier<Item> RESIN_SPECK = registerItem(ModItemIds.RESIN_SPECK, Item.Properties::new);

    public static final Supplier<Item> ECHOING_PRISM = registerItem(ModItemIds.ECHOING_PRISM, () -> new Item.Properties()
            .rarity(Rarity.UNCOMMON)
    );
    public static final Supplier<Item> ECHOING_PRISM_UPGRADE_SMITHING_TEMPLATE = registerItem(ModItemIds.ECHOING_PRISM_UPGRADE_SMITHING_TEMPLATE, () -> new Item.Properties()
            .rarity(Rarity.UNCOMMON)
    );
    public static final Supplier<Item> SCULK_INFUSION = registerItem(ModItemIds.SCULK_INFUSION, SculkInfusionItem::new, () -> new Item.Properties()
            .component(DataComponents.CONSUMABLE, Consumables.defaultDrink().build())
            .rarity(Rarity.RARE)
            .durability(1397)
    );
    public static final Supplier<Item> REVERB_COMPASS = registerItem(ModItemIds.REVERB_COMPASS, ReverbCompassItem::new, Item.Properties::new);

    public static final Supplier<Item> REPAIR_KIT = registerItem(ModItemIds.REPAIR_KIT, Item.Properties::new);
    public static final Supplier<Item> FORGE_DUST = registerItem(ModItemIds.FORGE_DUST, () -> new Item.Properties()
            .component(ModComponents.FORGE_MATERIAL_COMPONENT, "None")
    );
    public static final Supplier<Item> RAW_IRON_CHUNK = registerItem(ModItemIds.RAW_IRON_CHUNK, Item.Properties::new);
    public static final Supplier<Item> RAW_GOLD_CHUNK = registerItem(ModItemIds.RAW_GOLD_CHUNK, Item.Properties::new);
    public static final Supplier<Item> RAW_COPPER_CHUNK = registerItem(ModItemIds.RAW_COPPER_CHUNK, Item.Properties::new);

    public static final Supplier<Item> QUARTZITE = registerItem(ModItemIds.QUARTZITE, () -> new Item.Properties().trimMaterial(TrimMaterials.QUARTZ));
    public static final Supplier<Item> QUARTZITE_SHOVEL = registerItem(ModItemIds.QUARTZITE_SHOVEL, properties -> new ShovelItem(QUARTZITE_TOOL, 2.0F, -2.6F, properties), () -> new Item.Properties().repairable(ModTags.QUARTZITE_TOOL_MATERIALS));
    public static final Supplier<Item> QUARTZITE_PICKAXE = registerItem(ModItemIds.QUARTZITE_PICKAXE, () -> new Item.Properties().pickaxe(QUARTZITE_TOOL, 3.0F, -3.0F).repairable(ModTags.QUARTZITE_TOOL_MATERIALS));
    public static final Supplier<Item> QUARTZITE_AXE = registerItem(ModItemIds.QUARTZITE_AXE, properties -> new AxeItem(QUARTZITE_TOOL, 4.0F, -2.8F, properties), () -> new Item.Properties().repairable(ModTags.QUARTZITE_TOOL_MATERIALS));
    public static final Supplier<Item> QUARTZITE_SCYTHE = registerItem(ModItemIds.QUARTZITE_SCYTHE, properties -> new HoeItem(QUARTZITE_TOOL, 1.0F, -2.2F, properties), () -> new Item.Properties().repairable(ModTags.QUARTZITE_TOOL_MATERIALS));
    public static final Supplier<Item> QUARTZITE_SWORD = registerItem(ModItemIds.QUARTZITE_SWORD, () -> new Item.Properties().sword(QUARTZITE_TOOL, 2.0F, -2.4F).repairable(ModTags.QUARTZITE_TOOL_MATERIALS));

    public static final Supplier<Item> COPPER_TRIDENT = registerItem(
            ModItemIds.COPPER_TRIDENT,
            CopperTridentItem::new,
            () -> new Item.Properties()
                    .rarity(Rarity.UNCOMMON)
                    .durability(512)
                    .attributes(CopperTridentItem.createAttributes())
                    .component(DataComponents.TOOL, CopperTridentItem.createToolProperties())
                    .enchantable(1)
                    .component(DataComponents.WEAPON, new Weapon(1))
                    .repairable(ItemTags.COPPER_TOOL_MATERIALS)
    );

    public static final Supplier<Item> COPPER_MULTITOOL = registerItem(ModItemIds.COPPER_MULTITOOL, properties -> new PreservedMultitoolItem(ToolMaterial.COPPER, 3.0F, -3.2F, properties), () -> new Item.Properties().repairable(ItemTags.COPPER_TOOL_MATERIALS));
    public static final Supplier<Item> DIAMOND_MULTITOOL = registerItem(ModItemIds.DIAMOND_MULTITOOL, properties -> new PreservedMultitoolItem(ToolMaterial.DIAMOND, 4.0F, -3.2F, properties), () -> new Item.Properties().repairable(ItemTags.DIAMOND_TOOL_MATERIALS));
    public static final Supplier<Item> GOLDEN_MULTITOOL = registerItem(ModItemIds.GOLDEN_MULTITOOL, properties -> new PreservedMultitoolItem(ToolMaterial.GOLD, 4.0F, -3.2F, properties), () -> new Item.Properties().repairable(ItemTags.GOLD_TOOL_MATERIALS));
    public static final Supplier<Item> IRON_MULTITOOL = registerItem(ModItemIds.IRON_MULTITOOL, properties -> new PreservedMultitoolItem(ToolMaterial.IRON, 3.0F, -3.2F, properties), () -> new Item.Properties().repairable(ItemTags.IRON_TOOL_MATERIALS));
    public static final Supplier<Item> NETHER_ALLOY_MULTITOOL = registerItem(ModItemIds.NETHER_ALLOY_MULTITOOL, properties -> new PreservedMultitoolItem(NETHER_ALLOY_TOOL, 5.0F, -3.2F, properties), () -> new Item.Properties().repairable(ModTags.NETHER_ALLOY_TOOL_MATERIALS));
    public static final Supplier<Item> NETHERITE_MULTITOOL = registerItem(ModItemIds.NETHERITE_MULTITOOL, properties -> new PreservedMultitoolItem(ToolMaterial.NETHERITE, 4.0F, -3.2F, properties), () -> new Item.Properties().repairable(ItemTags.NETHERITE_TOOL_MATERIALS).fireResistant());
    public static final Supplier<Item> QUARTZITE_MULTITOOL = registerItem(ModItemIds.QUARTZITE_MULTITOOL, properties -> new PreservedMultitoolItem(QUARTZITE_TOOL, 3.0F, -3.2F, properties), () -> new Item.Properties().repairable(ModTags.QUARTZITE_TOOL_MATERIALS));
    public static final Supplier<Item> STONE_MULTITOOL = registerItem(ModItemIds.STONE_MULTITOOL, properties -> new PreservedMultitoolItem(ToolMaterial.STONE, 2.0F, -3.2F, properties), () -> new Item.Properties().repairable(ItemTags.STONE_TOOL_MATERIALS));
    public static final Supplier<Item> WOODEN_MULTITOOL = registerItem(ModItemIds.WOODEN_MULTITOOL, properties -> new PreservedMultitoolItem(ToolMaterial.WOOD, 2.0F, -3.2F, properties), () -> new Item.Properties().repairable(ItemTags.WOODEN_TOOL_MATERIALS));

    public static final Supplier<Item> BLACK_CLOTH = registerItem(ModItemIds.BLACK_CLOTH, Item.Properties::new);
    public static final Supplier<Item> BLUE_CLOTH = registerItem(ModItemIds.BLUE_CLOTH, Item.Properties::new);
    public static final Supplier<Item> BROWN_CLOTH = registerItem(ModItemIds.BROWN_CLOTH, Item.Properties::new);
    public static final Supplier<Item> CYAN_CLOTH = registerItem(ModItemIds.CYAN_CLOTH, Item.Properties::new);
    public static final Supplier<Item> GRAY_CLOTH = registerItem(ModItemIds.GRAY_CLOTH, Item.Properties::new);
    public static final Supplier<Item> GREEN_CLOTH = registerItem(ModItemIds.GREEN_CLOTH, Item.Properties::new);
    public static final Supplier<Item> LIGHT_BLUE_CLOTH = registerItem(ModItemIds.LIGHT_BLUE_CLOTH, Item.Properties::new);
    public static final Supplier<Item> LIGHT_GRAY_CLOTH = registerItem(ModItemIds.LIGHT_GRAY_CLOTH, Item.Properties::new);
    public static final Supplier<Item> LIME_CLOTH = registerItem(ModItemIds.LIME_CLOTH, Item.Properties::new);
    public static final Supplier<Item> MAGENTA_CLOTH = registerItem(ModItemIds.MAGENTA_CLOTH, Item.Properties::new);
    public static final Supplier<Item> ORANGE_CLOTH = registerItem(ModItemIds.ORANGE_CLOTH, Item.Properties::new);
    public static final Supplier<Item> PINK_CLOTH = registerItem(ModItemIds.PINK_CLOTH, Item.Properties::new);
    public static final Supplier<Item> PURPLE_CLOTH = registerItem(ModItemIds.PURPLE_CLOTH, Item.Properties::new);
    public static final Supplier<Item> RED_CLOTH = registerItem(ModItemIds.RED_CLOTH, Item.Properties::new);
    public static final Supplier<Item> WHITE_CLOTH = registerItem(ModItemIds.WHITE_CLOTH, Item.Properties::new);
    public static final Supplier<Item> YELLOW_CLOTH = registerItem(ModItemIds.YELLOW_CLOTH, Item.Properties::new);

    public static final Supplier<Item> COPPER_SHIELD = registerItem(ModItemIds.COPPER_SHIELD, PreservedShieldItem::new, () -> new Item.Properties()
            .durability(256)
            .stacksTo(1)
            .repairable(ItemTags.COPPER_TOOL_MATERIALS)
            .equippableUnswappable(EquipmentSlot.OFFHAND)
            .delayedComponent(
                    DataComponents.BLOCKS_ATTACKS,
                    context -> new BlocksAttacks(
                            0.25F,
                            1.0F,
                            List.of(new BlocksAttacks.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
                            new BlocksAttacks.ItemDamageFunction(1.0F, 1.0F, 1.0F),
                            Optional.of(context.getOrThrow(DamageTypeTags.BYPASSES_SHIELD)),
                            Optional.of(SoundEvents.SHIELD_BLOCK),
                            Optional.of(SoundEvents.SHIELD_BREAK)
                    )
            )
            .component(DataComponents.BREAK_SOUND, SoundEvents.SHIELD_BREAK)
            .component(ModComponents.SHIELD_MAX_STAMINA_COMPONENT, 10)
            .component(ModComponents.SHIELD_REGEN_RATE_COMPONENT, COPPER_REGEN)
    );
    public static final Supplier<Item> IRON_SHIELD = registerItem(ModItemIds.IRON_SHIELD, PreservedShieldItem::new, () -> new Item.Properties()
            .durability(512)
            .stacksTo(1)
            .repairable(ItemTags.IRON_TOOL_MATERIALS)
            .equippableUnswappable(EquipmentSlot.OFFHAND)
            .delayedComponent(
                    DataComponents.BLOCKS_ATTACKS,
                    context -> new BlocksAttacks(
                            0.25F,
                            1.0F,
                            List.of(new BlocksAttacks.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
                            new BlocksAttacks.ItemDamageFunction(1.0F, 1.0F, 1.0F),
                            Optional.of(context.getOrThrow(DamageTypeTags.BYPASSES_SHIELD)),
                            Optional.of(SoundEvents.SHIELD_BLOCK),
                            Optional.of(SoundEvents.SHIELD_BREAK)
                    )
            )
            .component(DataComponents.BREAK_SOUND, SoundEvents.SHIELD_BREAK)
            .component(ModComponents.SHIELD_MAX_STAMINA_COMPONENT, 15)
            .component(ModComponents.SHIELD_REGEN_RATE_COMPONENT, IRON_REGEN)
    );
    public static final Supplier<Item> DIAMOND_SHIELD = registerItem(ModItemIds.DIAMOND_SHIELD, PreservedShieldItem::new, () -> new Item.Properties()
            .durability(2048)
            .stacksTo(1)
            .repairable(ItemTags.DIAMOND_TOOL_MATERIALS)
            .equippableUnswappable(EquipmentSlot.OFFHAND)
            .delayedComponent(
                    DataComponents.BLOCKS_ATTACKS,
                    context -> new BlocksAttacks(
                            0.25F,
                            1.0F,
                            List.of(new BlocksAttacks.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
                            new BlocksAttacks.ItemDamageFunction(1.0F, 1.0F, 1.0F),
                            Optional.of(context.getOrThrow(DamageTypeTags.BYPASSES_SHIELD)),
                            Optional.of(SoundEvents.SHIELD_BLOCK),
                            Optional.of(SoundEvents.SHIELD_BREAK)
                    )
            )
            .component(DataComponents.BREAK_SOUND, SoundEvents.SHIELD_BREAK)
            .component(ModComponents.SHIELD_MAX_STAMINA_COMPONENT, 20)
            .component(ModComponents.SHIELD_REGEN_RATE_COMPONENT, DIAMOND_REGEN)
    );
    public static final Supplier<Item> NETHERITE_SHIELD = registerItem(ModItemIds.NETHERITE_SHIELD, PreservedShieldItem::new, () -> new Item.Properties()
            .durability(4096)
            .stacksTo(1)
            .repairable(ModTags.REPAIRS_NETHERITE_TOOL)
            .equippableUnswappable(EquipmentSlot.OFFHAND)
            .fireResistant()
            .delayedComponent(
                    DataComponents.BLOCKS_ATTACKS,
                    context -> new BlocksAttacks(
                            0.25F,
                            1.0F,
                            List.of(new BlocksAttacks.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
                            new BlocksAttacks.ItemDamageFunction(1.0F, 1.0F, 1.0F),
                            Optional.of(context.getOrThrow(DamageTypeTags.BYPASSES_SHIELD)),
                            Optional.of(SoundEvents.SHIELD_BLOCK),
                            Optional.of(SoundEvents.SHIELD_BREAK)
                    )
            )
            .component(DataComponents.BREAK_SOUND, SoundEvents.SHIELD_BREAK)
            .component(ModComponents.SHIELD_MAX_STAMINA_COMPONENT, 25)
            .component(ModComponents.SHIELD_REGEN_RATE_COMPONENT, NETHERITE_REGEN)
    );
    public static final Supplier<Item> GOLDEN_SHIELD = registerItem(ModItemIds.GOLDEN_SHIELD, PreservedShieldItem::new, () -> new Item.Properties()
            .durability(256)
            .stacksTo(1)
            .repairable(ItemTags.GOLD_TOOL_MATERIALS)
            .equippableUnswappable(EquipmentSlot.OFFHAND)
            .delayedComponent(
                    DataComponents.BLOCKS_ATTACKS,
                    context -> new BlocksAttacks(
                            0.25F,
                            1.0F,
                            List.of(new BlocksAttacks.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
                            new BlocksAttacks.ItemDamageFunction(1.0F, 1.0F, 1.0F),
                            Optional.of(context.getOrThrow(DamageTypeTags.BYPASSES_SHIELD)),
                            Optional.of(SoundEvents.SHIELD_BLOCK),
                            Optional.of(SoundEvents.SHIELD_BREAK)
                    )
            )
            .component(DataComponents.BREAK_SOUND, SoundEvents.SHIELD_BREAK)
            .component(ModComponents.SHIELD_MAX_STAMINA_COMPONENT, 10)
            .component(ModComponents.SHIELD_REGEN_RATE_COMPONENT, GOLD_REGEN)
    );

    public static final Supplier<Item> AQUATIC_FIBER = registerItem(ModItemIds.AQUATIC_FIBER, Item.Properties::new);
    public static final Supplier<Item> CACHE = registerItem(
            ModItemIds.CACHE,
            properties -> new CacheItem(properties, 18),
            () -> new Item.Properties()
                    .component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
                    .rarity(Rarity.UNCOMMON)
                    .stacksTo(1)
    );
    public static final Supplier<Item> NETHER_ALLOY_PLATE = registerItem(ModItemIds.NETHER_ALLOY_PLATE, Item.Properties::new);
    public static final Supplier<Item> NETHER_ALLOY_INGOT = registerItem(ModItemIds.NETHER_ALLOY_INGOT, Item.Properties::new);
    public static final Supplier<Item> NETHER_ALLOY_UPGRADE_SMITHING_TEMPLATE = registerItem(ModItemIds.NETHER_ALLOY_UPGRADE_SMITHING_TEMPLATE, () -> new Item.Properties()
            .rarity(Rarity.UNCOMMON)
    );
    public static final Supplier<Item> NETHER_ALLOY_SHOVEL = registerItem(ModItemIds.NETHER_ALLOY_SHOVEL, properties -> new ShovelItem(NETHER_ALLOY_TOOL, 4.0F, -2.6F, properties), () -> new Item.Properties().repairable(ModTags.NETHER_ALLOY_TOOL_MATERIALS));
    public static final Supplier<Item> NETHER_ALLOY_PICKAXE = registerItem(ModItemIds.NETHER_ALLOY_PICKAXE, () -> new Item.Properties().pickaxe(NETHER_ALLOY_TOOL, 5.0F, -3.0F).repairable(ModTags.NETHER_ALLOY_TOOL_MATERIALS));
    public static final Supplier<Item> NETHER_ALLOY_AXE = registerItem(ModItemIds.NETHER_ALLOY_AXE, properties -> new AxeItem(NETHER_ALLOY_TOOL, 6.0F, -2.8F, properties), () -> new Item.Properties().repairable(ModTags.NETHER_ALLOY_TOOL_MATERIALS));
    public static final Supplier<Item> NETHER_ALLOY_SCYTHE = registerItem(ModItemIds.NETHER_ALLOY_SCYTHE, properties -> new HoeItem(NETHER_ALLOY_TOOL, 3.0F, -2.2F, properties), () -> new Item.Properties().repairable(ModTags.NETHER_ALLOY_TOOL_MATERIALS));
    public static final Supplier<Item> NETHER_ALLOY_SWORD = registerItem(ModItemIds.NETHER_ALLOY_SWORD, () -> new Item.Properties().sword(NETHER_ALLOY_TOOL, 4.0F, -2.4F).repairable(ModTags.NETHER_ALLOY_TOOL_MATERIALS));

    public static final Supplier<Item> COPPER_FISHING_HOOK = registerItem(ModItemIds.COPPER_FISHING_HOOK, () -> new Item.Properties()
            .durability(256)
            .stacksTo(1)
            .repairable(ItemTags.COPPER_TOOL_MATERIALS)
    );
    public static final Supplier<Item> IRON_FISHING_HOOK = registerItem(ModItemIds.IRON_FISHING_HOOK, () -> new Item.Properties()
            .durability(512)
            .stacksTo(1)
            .repairable(ItemTags.IRON_TOOL_MATERIALS)
    );
    public static final Supplier<Item> PRISMARINE_FISHING_HOOK = registerItem(ModItemIds.PRISMARINE_FISHING_HOOK, () -> new Item.Properties()
            .durability(768)
            .stacksTo(1)
    );
    public static final Supplier<Item> GOLDEN_FISHING_HOOK = registerItem(ModItemIds.GOLDEN_FISHING_HOOK, () -> new Item.Properties()
            .durability(256)
            .stacksTo(1)
            .repairable(ItemTags.GOLD_TOOL_MATERIALS)
    );
    public static final Supplier<Item> DIAMOND_FISHING_HOOK = registerItem(ModItemIds.DIAMOND_FISHING_HOOK, () -> new Item.Properties()
            .durability(2048)
            .stacksTo(1)
            .repairable(ItemTags.DIAMOND_TOOL_MATERIALS)
    );
    public static final Supplier<Item> NETHERITE_FISHING_HOOK = registerItem(ModItemIds.NETHERITE_FISHING_HOOK, () -> new Item.Properties()
            .durability(4096)
            .stacksTo(1)
            .repairable(ModTags.REPAIRS_NETHERITE_TOOL)
            .fireResistant()
    );
    public static final Supplier<Item> COPPER_LACED_FISHING_LINE = registerItem(ModItemIds.COPPER_LACED_FISHING_LINE, () -> new Item.Properties()
            .durability(256)
            .stacksTo(1)
            .repairable(ItemTags.COPPER_TOOL_MATERIALS)
    );
    public static final Supplier<Item> IRON_LACED_FISHING_LINE = registerItem(ModItemIds.IRON_LACED_FISHING_LINE, () -> new Item.Properties()
            .durability(512)
            .stacksTo(1)
            .repairable(ItemTags.IRON_TOOL_MATERIALS)
    );
    public static final Supplier<Item> PRISMARINE_LACED_FISHING_LINE = registerItem(ModItemIds.PRISMARINE_LACED_FISHING_LINE, () -> new Item.Properties()
            .durability(768)
            .stacksTo(1)
    );
    public static final Supplier<Item> GOLDEN_LACED_FISHING_LINE = registerItem(ModItemIds.GOLDEN_LACED_FISHING_LINE, () -> new Item.Properties()
            .durability(256)
            .stacksTo(1)
            .repairable(ItemTags.GOLD_TOOL_MATERIALS)
    );
    public static final Supplier<Item> DIAMOND_LACED_FISHING_LINE = registerItem(ModItemIds.DIAMOND_LACED_FISHING_LINE, () -> new Item.Properties()
            .durability(2048)
            .stacksTo(1)
            .repairable(ItemTags.DIAMOND_TOOL_MATERIALS)
    );
    public static final Supplier<Item> NETHERITE_LACED_FISHING_LINE = registerItem(ModItemIds.NETHERITE_LACED_FISHING_LINE, () -> new Item.Properties()
            .durability(4096)
            .stacksTo(1)
            .repairable(ModTags.REPAIRS_NETHERITE_TOOL)
            .fireResistant()
    );
    public static final Supplier<Item> COPPER_SINKER = registerItem(ModItemIds.COPPER_SINKER, () -> new Item.Properties()
            .durability(256)
            .stacksTo(1)
            .repairable(ItemTags.COPPER_TOOL_MATERIALS)
    );
    public static final Supplier<Item> IRON_SINKER = registerItem(ModItemIds.IRON_SINKER, () -> new Item.Properties()
            .durability(512)
            .stacksTo(1)
            .repairable(ItemTags.IRON_TOOL_MATERIALS)
    );
    public static final Supplier<Item> PRISMARINE_SINKER = registerItem(ModItemIds.PRISMARINE_SINKER, () -> new Item.Properties()
            .durability(768)
            .stacksTo(1)
    );
    public static final Supplier<Item> GOLDEN_SINKER = registerItem(ModItemIds.GOLDEN_SINKER, () -> new Item.Properties()
            .durability(256)
            .stacksTo(1)
            .repairable(ItemTags.GOLD_TOOL_MATERIALS)
    );
    public static final Supplier<Item> DIAMOND_SINKER = registerItem(ModItemIds.DIAMOND_SINKER, () -> new Item.Properties()
            .durability(2048)
            .stacksTo(1)
            .repairable(ItemTags.DIAMOND_TOOL_MATERIALS)
    );
    public static final Supplier<Item> NETHERITE_SINKER = registerItem(ModItemIds.NETHERITE_SINKER, () -> new Item.Properties()
            .durability(4096)
            .stacksTo(1)
            .repairable(ModTags.REPAIRS_NETHERITE_TOOL)
            .fireResistant()
    );
    public static final Supplier<Item> MUSIC_DISC_AQUA = registerItem(ModItemIds.MUSIC_DISC_AQUA, () -> new Item.Properties()
            .rarity(Rarity.UNCOMMON)
            .stacksTo(1)
    );

    public static final Supplier<Item> FLARE_GUN = registerItem(ModItemIds.FLARE_GUN, PreservedFlareGunItem::new, () -> new Item.Properties()
            .durability(256)
            .stacksTo(1)
            .useCooldown(1.0F)
            .repairable(ItemTags.COPPER_TOOL_MATERIALS)
            .component(ModComponents.FLARE_PARTICLE_COMPONENT, "0xFFFFFF")
            .component(ModComponents.ON_COOLDOWN, true)
    );
    public static final Supplier<Item> DYNAMITE = registerItem(ModItemIds.DYNAMITE, DynamiteItem::new, () -> new Item.Properties()
            .useCooldown(1.0F)
    );

    public static final Supplier<Item> LAVA_BOTTLE = registerItem(ModItemIds.LAVA_BOTTLE, () -> new Item.Properties()
            .component(DataComponents.CONSUMABLE, ModConsumables.LAVA_BOTTLE).usingConvertsTo(Items.GLASS_BOTTLE)
            .component(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
            .stacksTo(4)
    );
    public static final Supplier<Item> SPLASH_LAVA_BOTTLE = registerItem(ModItemIds.SPLASH_LAVA_BOTTLE, PreservedBottleItem::new, () -> new Item.Properties()
            .component(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
            .stacksTo(4)
    );
    public static final Supplier<Item> LINGERING_LAVA_BOTTLE = registerItem(ModItemIds.LINGERING_LAVA_BOTTLE, PreservedBottleItem::new, () -> new Item.Properties()
            .component(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
            .stacksTo(4)
    );
    public static final Supplier<Item> MILK_BOTTLE = registerItem(ModItemIds.MILK_BOTTLE, () -> new Item.Properties()
            .component(DataComponents.CONSUMABLE, ModConsumables.MILK_BOTTLE).usingConvertsTo(Items.GLASS_BOTTLE)
            .component(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
            .stacksTo(4)
    );
    public static final Supplier<Item> SPLASH_MILK_BOTTLE = registerItem(ModItemIds.SPLASH_MILK_BOTTLE, PreservedBottleItem::new, () -> new Item.Properties()
            .component(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
            .stacksTo(4)
    );
    public static final Supplier<Item> LINGERING_MILK_BOTTLE = registerItem(ModItemIds.LINGERING_MILK_BOTTLE, PreservedBottleItem::new, () -> new Item.Properties()
            .component(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
            .stacksTo(4)
    );
    public static final Supplier<Item> SPLASH_HONEY_BOTTLE = registerItem(ModItemIds.SPLASH_HONEY_BOTTLE, PreservedBottleItem::new, () -> new Item.Properties()
            .component(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
            .stacksTo(4)
    );
    public static final Supplier<Item> LINGERING_HONEY_BOTTLE = registerItem(ModItemIds.LINGERING_HONEY_BOTTLE, PreservedBottleItem::new, () -> new Item.Properties()
            .component(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
            .stacksTo(4)
    );

    // registering
    public static Consumable.Builder defaultFood() {
        return Consumable.builder().consumeSeconds(1.6F).animation(ItemUseAnimation.EAT).sound(SoundEvents.GENERIC_EAT).hasConsumeParticles(true);
    }

    private static Supplier<Item> registerItem(ResourceKey<Item> id, Supplier<Item.Properties> propertiesSupplier) {
        return registerItem(id, Item::new, propertiesSupplier);
    }

    private static Supplier<Item> registerItem(ResourceKey<Item> id, Function<Item.Properties, Item> factory, Supplier<Item.Properties> propertiesSupplier) {
        Supplier<Item> memoizedSupplier = new Supplier<>() {
            private Item instance;

            @Override
            public Item get() {
                if (instance == null) instance = factory.apply(propertiesSupplier.get().setId(id));
                return instance;
            }
        };

        ITEMS.put(id, memoizedSupplier);
        return memoizedSupplier;
    }

    public static Map<ResourceKey<Item>, Supplier<Item>> getItems() {
        return ITEMS;
    }

    public static void registerModItems() {}
}
