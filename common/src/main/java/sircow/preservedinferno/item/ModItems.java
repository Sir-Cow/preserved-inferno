package sircow.preservedinferno.item;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BlocksAttacks;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.components.ModComponents;
import sircow.preservedinferno.item.custom.PreservedShieldItem;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ModItems {
    // shield stuff
    static DecimalFormat df = new DecimalFormat("0.000");
    static double IRON_REGEN_PARSE = 1F / 28F;
    static float WOOD_REGEN = 1F / 64F;
    static float IRON_REGEN = Float.parseFloat(df.format(IRON_REGEN_PARSE));
    static float DIAMOND_REGEN = 1F / 16F;
    static float NETHERITE_REGEN = 1F / 10F;
    static float GOLD_REGEN = 2F / 25F;

    // items
    public static final Item DREAMCATCHER = registerItem("dreamcatcher", Item::new, new Item.Properties().stacksTo(1));
    public static final Item ELDER_GUARDIAN_SPINE = registerItem("elder_guardian_spine", Item::new, new Item.Properties().rarity(Rarity.EPIC));
    public static final Item HOLLOW_TWINE = registerItem("hollow_twine", Item::new, new Item.Properties());
    public static final Item PHANTOM_SINEW = registerItem("phantom_sinew", Item::new, new Item.Properties());
    public static final Item RAW_HIDE = registerItem("raw_hide", Item::new, new Item.Properties());

    public static final Item RAW_IRON_CHUNK = registerItem("raw_iron_chunk", Item::new, new Item.Properties());
    public static final Item RAW_GOLD_CHUNK = registerItem("raw_gold_chunk", Item::new, new Item.Properties());
    public static final Item RAW_COPPER_CHUNK = registerItem("raw_copper_chunk", Item::new, new Item.Properties());
    public static final Item COPPER_NUGGET = registerItem("copper_nugget", Item::new, new Item.Properties());

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

    public static final Item WOODEN_SHIELD = registerItem("wooden_shield", PreservedShieldItem::new,
            new Item.Properties()
                    .durability(256)
                    .stacksTo(1)
                    .repairable(ItemTags.WOODEN_TOOL_MATERIALS)
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
                    .component(ModComponents.SHIELD_MAX_STAMINA_COMPONENT, 5)
                    .component(ModComponents.SHIELD_REGEN_RATE_COMPONENT, WOOD_REGEN)
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
                    .component(ModComponents.SHIELD_MAX_STAMINA_COMPONENT, 10)
                    .component(ModComponents.SHIELD_REGEN_RATE_COMPONENT, IRON_REGEN)
    );
    public static final Item DIAMOND_SHIELD = registerItem("diamond_shield", PreservedShieldItem::new,
            new Item.Properties()
            .durability(1024)
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
                    .component(ModComponents.SHIELD_MAX_STAMINA_COMPONENT, 15)
                    .component(ModComponents.SHIELD_REGEN_RATE_COMPONENT, DIAMOND_REGEN)
    );
    public static final Item NETHERITE_SHIELD = registerItem("netherite_shield", PreservedShieldItem::new,
            new Item.Properties()
            .durability(2048)
            .stacksTo(1)
            .repairable(ItemTags.NETHERITE_TOOL_MATERIALS)
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
                    .component(ModComponents.SHIELD_MAX_STAMINA_COMPONENT, 8)
                    .component(ModComponents.SHIELD_REGEN_RATE_COMPONENT, GOLD_REGEN)
    );

    public static final Item AQUATIC_FIBER = registerItem("aquatic_fiber", Item::new, new Item.Properties());

    public static final Item NETHER_GOLD_PLATE = registerItem("nether_gold_plate", Item::new, new Item.Properties());
    public static final Item NETHER_ALLOY_INGOT = registerItem("nether_alloy_ingot", Item::new, new Item.Properties());
    public static final Item NETHER_ALLOY_UPGRADE_SMITHING_TEMPLATE = registerItem("nether_alloy_upgrade_smithing_template", Item::new, new Item.Properties());

    public static final Item IRON_FISHING_HOOK = registerItem("iron_fishing_hook", Item::new, new Item.Properties()
            .durability(250)
            .stacksTo(1)
            .repairable(ItemTags.IRON_TOOL_MATERIALS)
    );
    public static final Item DIAMOND_FISHING_HOOK = registerItem("diamond_fishing_hook", Item::new, new Item.Properties()
            .durability(1561)
            .stacksTo(1)
            .repairable(ItemTags.DIAMOND_TOOL_MATERIALS)
    );
    public static final Item NETHERITE_FISHING_HOOK = registerItem("netherite_fishing_hook", Item::new, new Item.Properties()
            .durability(2031)
            .stacksTo(1)
            .repairable(ItemTags.NETHERITE_TOOL_MATERIALS)
            .fireResistant()
    );
    public static final Item IRON_LACED_FISHING_LINE = registerItem("iron_laced_fishing_line", Item::new, new Item.Properties()
            .durability(250)
            .stacksTo(1)
            .repairable(ItemTags.IRON_TOOL_MATERIALS)
    );
    public static final Item DIAMOND_LACED_FISHING_LINE = registerItem("diamond_laced_fishing_line", Item::new, new Item.Properties()
            .durability(1561)
            .stacksTo(1)
            .repairable(ItemTags.DIAMOND_TOOL_MATERIALS)
    );
    public static final Item NETHERITE_LACED_FISHING_LINE = registerItem("netherite_laced_fishing_line", Item::new, new Item.Properties()
            .durability(2031)
            .stacksTo(1)
            .repairable(ItemTags.NETHERITE_TOOL_MATERIALS)
            .fireResistant()
    );
    public static final Item IRON_SINKER = registerItem("iron_sinker", Item::new, new Item.Properties()
            .durability(250)
            .stacksTo(1)
            .repairable(ItemTags.IRON_TOOL_MATERIALS)
    );
    public static final Item DIAMOND_SINKER = registerItem("diamond_sinker", Item::new, new Item.Properties()
            .durability(1561)
            .stacksTo(1)
            .repairable(ItemTags.DIAMOND_TOOL_MATERIALS)
    );
    public static final Item NETHERITE_SINKER = registerItem("netherite_sinker", Item::new, new Item.Properties()
            .durability(2031)
            .stacksTo(1)
            .repairable(ItemTags.NETHERITE_TOOL_MATERIALS)
            .fireResistant()
    );
    public static final Item MUSIC_DISC_AQUA = registerItem("music_disc_aqua", Item::new, new Item.Properties().stacksTo(1));

    public static Item registerItem(String name, Function<Item.Properties, Item> itemFactory, Item.Properties properties) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Constants.id(name));
        Item item = itemFactory.apply(properties.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

    public static void registerModItems() {
        Constants.LOG.info("Registering Mod Items for " + Constants.MOD_ID);
    }
}
