package sircow.preservedinferno.item;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.components.ModComponents;
import sircow.preservedinferno.item.custom.CopperTridentItem;
import sircow.preservedinferno.item.custom.PreservedFlareGunItem;
import sircow.preservedinferno.item.custom.PreservedShieldItem;
import sircow.preservedinferno.item.custom.SculkInfusionItem;
import sircow.preservedinferno.other.ModTags;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

public class ModItems {
    // shield stuff
    static DecimalFormat df = new DecimalFormat("0.000", new DecimalFormatSymbols(Locale.US));
    static double COPPER_REGEN_PARSE = 2F / 75F;
    static double NETHERITE_REGEN_PARSE = 2F / 15F;
    static float COPPER_REGEN = Float.parseFloat(df.format(COPPER_REGEN_PARSE));
    static float IRON_REGEN = 6F / 125F;
    static float DIAMOND_REGEN = 2F / 25F;
    static float NETHERITE_REGEN = Float.parseFloat(df.format(NETHERITE_REGEN_PARSE));
    static float GOLD_REGEN = 1F / 10F;

    // items
    public static final Item DREAMCATCHER = registerItem("dreamcatcher", Item::new, new Item.Properties()
            .durability(0)
            .stacksTo(1)
    );
    public static final Item ELDER_GUARDIAN_SPINE = registerItem("elder_guardian_spine", Item::new, new Item.Properties().rarity(Rarity.EPIC));
    public static final Item HOLLOW_TWINE = registerItem("hollow_twine", Item::new, new Item.Properties());
    public static final Item PHANTOM_SINEW = registerItem("phantom_sinew", Item::new, new Item.Properties());
    public static final Item RAW_HIDE = registerItem("raw_hide", Item::new, new Item.Properties());
    public static final Item LEATHER_FABRIC = registerItem("leather_fabric", Item::new, new Item.Properties());
    public static final Item GILDEN_BERRIES = registerItem("gilden_berries", Item::new, new Item.Properties().food(
            new FoodProperties.Builder().nutrition(6).saturationModifier(1.2F).alwaysEdible().build(),
            defaultFood().consumeSeconds(0.8F).onConsume(new ApplyStatusEffectsConsumeEffect(List.of(new MobEffectInstance(MobEffects.REGENERATION, 100, 0)))).build()));

    public static final Item ECHOING_PRISM = registerItem("echoing_prism", Item::new, new Item.Properties()
            .rarity(Rarity.UNCOMMON)
    );
    public static final Item ECHOING_PRISM_UPGRADE_SMITHING_TEMPLATE = registerItem("echoing_prism_upgrade_smithing_template", Item::new, new Item.Properties()
            .rarity(Rarity.UNCOMMON)
    );
    public static final Item SCULK_INFUSION = registerItem("sculk_infusion", SculkInfusionItem::new, new Item.Properties()
            .component(DataComponents.CONSUMABLE, Consumables.defaultDrink().build())
            .rarity(Rarity.RARE)
            .durability(1397)
    );

    public static final Item RAW_IRON_CHUNK = registerItem("raw_iron_chunk", Item::new, new Item.Properties());
    public static final Item RAW_GOLD_CHUNK = registerItem("raw_gold_chunk", Item::new, new Item.Properties());
    public static final Item RAW_COPPER_CHUNK = registerItem("raw_copper_chunk", Item::new, new Item.Properties());

    public static final Item COPPER_TRIDENT = registerItem(
            "copper_trident",
            CopperTridentItem::new,
            new Item.Properties()
                    .rarity(Rarity.UNCOMMON)
                    .durability(512)
                    .attributes(CopperTridentItem.createAttributes())
                    .component(DataComponents.TOOL, CopperTridentItem.createToolProperties())
                    .enchantable(1)
                    .component(DataComponents.WEAPON, new Weapon(1))
                    .repairable(ItemTags.COPPER_TOOL_MATERIALS)
    );

    public static final Item BLACK_CLOTH = registerItem("black_cloth", Item::new, new Item.Properties());
    public static final Item BLUE_CLOTH = registerItem("blue_cloth", Item::new, new Item.Properties());
    public static final Item BROWN_CLOTH = registerItem("brown_cloth", Item::new, new Item.Properties());
    public static final Item CYAN_CLOTH = registerItem("cyan_cloth", Item::new, new Item.Properties());
    public static final Item GRAY_CLOTH = registerItem("gray_cloth", Item::new, new Item.Properties());
    public static final Item GREEN_CLOTH = registerItem("green_cloth", Item::new, new Item.Properties());
    public static final Item LIGHT_BLUE_CLOTH = registerItem("light_blue_cloth", Item::new, new Item.Properties());
    public static final Item LIGHT_GRAY_CLOTH = registerItem("light_gray_cloth", Item::new, new Item.Properties());
    public static final Item LIME_CLOTH = registerItem("lime_cloth", Item::new, new Item.Properties());
    public static final Item MAGENTA_CLOTH = registerItem("magenta_cloth", Item::new, new Item.Properties());
    public static final Item ORANGE_CLOTH = registerItem("orange_cloth", Item::new, new Item.Properties());
    public static final Item PINK_CLOTH = registerItem("pink_cloth", Item::new, new Item.Properties());
    public static final Item PURPLE_CLOTH = registerItem("purple_cloth", Item::new, new Item.Properties());
    public static final Item RED_CLOTH = registerItem("red_cloth", Item::new, new Item.Properties());
    public static final Item WHITE_CLOTH = registerItem("white_cloth", Item::new, new Item.Properties());
    public static final Item YELLOW_CLOTH = registerItem("yellow_cloth", Item::new, new Item.Properties());

    public static final Item COPPER_SHIELD = registerItem("copper_shield", PreservedShieldItem::new,
            new Item.Properties()
                    .durability(256)
                    .stacksTo(1)
                    .repairable(ItemTags.COPPER_TOOL_MATERIALS)
                    .equippableUnswappable(EquipmentSlot.OFFHAND)
                    .component(
                            DataComponents.BLOCKS_ATTACKS,
                            new BlocksAttacks(
                                    0.25F,
                                    1.0F,
                                    List.of(new BlocksAttacks.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
                                    new BlocksAttacks.ItemDamageFunction(3.0F, 1.0F, 1.0F),
                                    Optional.of(DamageTypeTags.BYPASSES_SHIELD),
                                    Optional.of(SoundEvents.SHIELD_BLOCK),
                                    Optional.of(SoundEvents.SHIELD_BREAK)
                            )
                    )
                    .component(DataComponents.BREAK_SOUND, SoundEvents.SHIELD_BREAK)
                    .component(ModComponents.SHIELD_MAX_STAMINA_COMPONENT, 8)
                    .component(ModComponents.SHIELD_REGEN_RATE_COMPONENT, COPPER_REGEN)
    );
    public static final Item IRON_SHIELD = registerItem("iron_shield", PreservedShieldItem::new,
            new Item.Properties()
                    .durability(512)
                    .stacksTo(1)
                    .repairable(ItemTags.IRON_TOOL_MATERIALS)
                    .equippableUnswappable(EquipmentSlot.OFFHAND)
                    .component(
                            DataComponents.BLOCKS_ATTACKS,
                            new BlocksAttacks(
                                    0.25F,
                                    1.0F,
                                    List.of(new BlocksAttacks.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
                                    new BlocksAttacks.ItemDamageFunction(3.0F, 1.0F, 1.0F),
                                    Optional.of(DamageTypeTags.BYPASSES_SHIELD),
                                    Optional.of(SoundEvents.SHIELD_BLOCK),
                                    Optional.of(SoundEvents.SHIELD_BREAK)
                            )
                    )
                    .component(DataComponents.BREAK_SOUND, SoundEvents.SHIELD_BREAK)
                    .component(ModComponents.SHIELD_MAX_STAMINA_COMPONENT, 12)
                    .component(ModComponents.SHIELD_REGEN_RATE_COMPONENT, IRON_REGEN)
    );
    public static final Item DIAMOND_SHIELD = registerItem("diamond_shield", PreservedShieldItem::new,
            new Item.Properties()
            .durability(2048)
            .stacksTo(1)
            .repairable(ItemTags.DIAMOND_TOOL_MATERIALS)
            .equippableUnswappable(EquipmentSlot.OFFHAND)
            .component(
                    DataComponents.BLOCKS_ATTACKS,
                    new BlocksAttacks(
                            0.25F,
                            1.0F,
                            List.of(new BlocksAttacks.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
                            new BlocksAttacks.ItemDamageFunction(3.0F, 1.0F, 1.0F),
                            Optional.of(DamageTypeTags.BYPASSES_SHIELD),
                            Optional.of(SoundEvents.SHIELD_BLOCK),
                            Optional.of(SoundEvents.SHIELD_BREAK)
                    )
            )
            .component(DataComponents.BREAK_SOUND, SoundEvents.SHIELD_BREAK)
                    .component(ModComponents.SHIELD_MAX_STAMINA_COMPONENT, 16)
                    .component(ModComponents.SHIELD_REGEN_RATE_COMPONENT, DIAMOND_REGEN)
    );
    public static final Item NETHERITE_SHIELD = registerItem("netherite_shield", PreservedShieldItem::new,
            new Item.Properties()
            .durability(4096)
            .stacksTo(1)
            .repairable(ModTags.REPAIRS_NETHERITE_TOOL)
            .equippableUnswappable(EquipmentSlot.OFFHAND)
            .fireResistant()
            .component(
                    DataComponents.BLOCKS_ATTACKS,
                    new BlocksAttacks(
                            0.25F,
                            1.0F,
                            List.of(new BlocksAttacks.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
                            new BlocksAttacks.ItemDamageFunction(3.0F, 1.0F, 1.0F),
                            Optional.of(DamageTypeTags.BYPASSES_SHIELD),
                            Optional.of(SoundEvents.SHIELD_BLOCK),
                            Optional.of(SoundEvents.SHIELD_BREAK)
                    )
            )
            .component(DataComponents.BREAK_SOUND, SoundEvents.SHIELD_BREAK)
                    .component(ModComponents.SHIELD_MAX_STAMINA_COMPONENT, 20)
                    .component(ModComponents.SHIELD_REGEN_RATE_COMPONENT, NETHERITE_REGEN)
    );
    public static final Item GOLDEN_SHIELD = registerItem("golden_shield", PreservedShieldItem::new,
            new Item.Properties()
                    .durability(256)
                    .stacksTo(1)
                    .repairable(ItemTags.GOLD_TOOL_MATERIALS)
                    .equippableUnswappable(EquipmentSlot.OFFHAND)
                    .component(
                            DataComponents.BLOCKS_ATTACKS,
                            new BlocksAttacks(
                                    0.25F,
                                    1.0F,
                                    List.of(new BlocksAttacks.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
                                    new BlocksAttacks.ItemDamageFunction(3.0F, 1.0F, 1.0F),
                                    Optional.of(DamageTypeTags.BYPASSES_SHIELD),
                                    Optional.of(SoundEvents.SHIELD_BLOCK),
                                    Optional.of(SoundEvents.SHIELD_BREAK)
                            )
                    )
                    .component(DataComponents.BREAK_SOUND, SoundEvents.SHIELD_BREAK)
                    .component(ModComponents.SHIELD_MAX_STAMINA_COMPONENT, 10)
                    .component(ModComponents.SHIELD_REGEN_RATE_COMPONENT, GOLD_REGEN)
    );

    public static final Item AQUATIC_FIBER = registerItem("aquatic_fiber", Item::new, new Item.Properties());

    public static final Item NETHER_ALLOY_PLATE = registerItem("nether_alloy_plate", Item::new, new Item.Properties());
    public static final Item NETHER_ALLOY_INGOT = registerItem("nether_alloy_ingot", Item::new, new Item.Properties());
    public static final Item NETHER_ALLOY_UPGRADE_SMITHING_TEMPLATE = registerItem("nether_alloy_upgrade_smithing_template", Item::new, new Item.Properties()
            .rarity(Rarity.UNCOMMON)
    );

    public static final Item COPPER_FISHING_HOOK = registerItem("copper_fishing_hook", Item::new, new Item.Properties()
            .durability(256)
            .stacksTo(1)
            .repairable(ItemTags.COPPER_TOOL_MATERIALS)
    );
    public static final Item IRON_FISHING_HOOK = registerItem("iron_fishing_hook", Item::new, new Item.Properties()
            .durability(512)
            .stacksTo(1)
            .repairable(ItemTags.IRON_TOOL_MATERIALS)
    );
    public static final Item PRISMARINE_FISHING_HOOK = registerItem("prismarine_fishing_hook", Item::new, new Item.Properties()
            .durability(768)
            .stacksTo(1)
    );
    public static final Item GOLDEN_FISHING_HOOK = registerItem("golden_fishing_hook", Item::new, new Item.Properties()
            .durability(256)
            .stacksTo(1)
            .repairable(ItemTags.GOLD_TOOL_MATERIALS)
    );
    public static final Item DIAMOND_FISHING_HOOK = registerItem("diamond_fishing_hook", Item::new, new Item.Properties()
            .durability(2048)
            .stacksTo(1)
            .repairable(ItemTags.DIAMOND_TOOL_MATERIALS)
    );
    public static final Item NETHERITE_FISHING_HOOK = registerItem("netherite_fishing_hook", Item::new, new Item.Properties()
            .durability(4096)
            .stacksTo(1)
            .repairable(ModTags.REPAIRS_NETHERITE_TOOL)
            .fireResistant()
    );
    public static final Item COPPER_LACED_FISHING_LINE = registerItem("copper_laced_fishing_line", Item::new, new Item.Properties()
            .durability(256)
            .stacksTo(1)
            .repairable(ItemTags.COPPER_TOOL_MATERIALS)
    );
    public static final Item IRON_LACED_FISHING_LINE = registerItem("iron_laced_fishing_line", Item::new, new Item.Properties()
            .durability(512)
            .stacksTo(1)
            .repairable(ItemTags.IRON_TOOL_MATERIALS)
    );
    public static final Item PRISMARINE_LACED_FISHING_LINE = registerItem("prismarine_laced_fishing_line", Item::new, new Item.Properties()
            .durability(768)
            .stacksTo(1)
    );
    public static final Item GOLDEN_LACED_FISHING_LINE = registerItem("golden_laced_fishing_line", Item::new, new Item.Properties()
            .durability(256)
            .stacksTo(1)
            .repairable(ItemTags.GOLD_TOOL_MATERIALS)
    );
    public static final Item DIAMOND_LACED_FISHING_LINE = registerItem("diamond_laced_fishing_line", Item::new, new Item.Properties()
            .durability(2048)
            .stacksTo(1)
            .repairable(ItemTags.DIAMOND_TOOL_MATERIALS)
    );
    public static final Item NETHERITE_LACED_FISHING_LINE = registerItem("netherite_laced_fishing_line", Item::new, new Item.Properties()
            .durability(4096)
            .stacksTo(1)
            .repairable(ModTags.REPAIRS_NETHERITE_TOOL)
            .fireResistant()
    );
    public static final Item COPPER_SINKER = registerItem("copper_sinker", Item::new, new Item.Properties()
            .durability(256)
            .stacksTo(1)
            .repairable(ItemTags.COPPER_TOOL_MATERIALS)
    );
    public static final Item IRON_SINKER = registerItem("iron_sinker", Item::new, new Item.Properties()
            .durability(512)
            .stacksTo(1)
            .repairable(ItemTags.IRON_TOOL_MATERIALS)
    );
    public static final Item PRISMARINE_SINKER = registerItem("prismarine_sinker", Item::new, new Item.Properties()
            .durability(768)
            .stacksTo(1)
    );
    public static final Item GOLDEN_SINKER = registerItem("golden_sinker", Item::new, new Item.Properties()
            .durability(256)
            .stacksTo(1)
            .repairable(ItemTags.GOLD_TOOL_MATERIALS)
    );
    public static final Item DIAMOND_SINKER = registerItem("diamond_sinker", Item::new, new Item.Properties()
            .durability(2048)
            .stacksTo(1)
            .repairable(ItemTags.DIAMOND_TOOL_MATERIALS)
    );
    public static final Item NETHERITE_SINKER = registerItem("netherite_sinker", Item::new, new Item.Properties()
            .durability(4096)
            .stacksTo(1)
            .repairable(ModTags.REPAIRS_NETHERITE_TOOL)
            .fireResistant()
    );
    public static final Item MUSIC_DISC_AQUA = registerItem("music_disc_aqua", Item::new, new Item.Properties()
            .rarity(Rarity.UNCOMMON)
            .stacksTo(1)
    );

    public static final Item FLARE_GUN = registerItem("flare_gun", PreservedFlareGunItem::new, new Item.Properties()
            .durability(256)
            .stacksTo(1)
            .useCooldown(1.0F)
            .repairable(ItemTags.COPPER_TOOL_MATERIALS)
            .component(ModComponents.FLARE_PARTICLE_COMPONENT, "0xFFFFFF")
            .component(ModComponents.ON_COOLDOWN, true)
    );

    // registering
    public static Item registerItem(String name, Function<Item.Properties, Item> itemFactory, Item.Properties properties) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Constants.id(name));
        Item item = itemFactory.apply(properties.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

    public static Consumable.Builder defaultFood() {
        return Consumable.builder().consumeSeconds(1.6F).animation(ItemUseAnimation.EAT).sound(SoundEvents.GENERIC_EAT).hasConsumeParticles(true);
    }

    public static void registerModItems() {
        // Constants.LOG.info("Registering Mod Items for " + Constants.MOD_ID);
    }
}
