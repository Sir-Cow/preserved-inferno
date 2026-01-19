package sircow.preservedinferno.enchantment;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.effects.*;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.other.ModTags;

public class ModEnchantments {
    public static final ResourceKey<Enchantment> BUCKLER = key("buckler");
    public static final ResourceKey<Enchantment> RESPITE = key("respite");
    public static final ResourceKey<Enchantment> ENDURANCE = key("endurance");
    public static final ResourceKey<Enchantment> VIGOR = key("vigor");
    public static final ResourceKey<Enchantment> BASHFUL = key("bashful");

    private static ResourceKey<Enchantment> key(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT, Constants.id(name));
    }

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);

        register(context, BUCKLER, Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(ModTags.SHIELDS_COMMON),
                                5,
                                1,
                                Enchantment.dynamicCost(1, 11),
                                Enchantment.dynamicCost(12, 11),
                                1,
                                EquipmentSlotGroup.OFFHAND
                        ))
        );
        register(context, RESPITE, Enchantment.enchantment(
                Enchantment.definition(
                        items.getOrThrow(ModTags.SHIELDS_COMMON),
                        2,
                        1,
                        Enchantment.dynamicCost(1, 11),
                        Enchantment.dynamicCost(12, 11),
                        1,
                        EquipmentSlotGroup.OFFHAND
                ))
        );
        register(context, ENDURANCE, Enchantment.enchantment(
                Enchantment.definition(
                        items.getOrThrow(ModTags.SHIELDS_COMMON),
                        5,
                        1,
                        Enchantment.dynamicCost(1, 11),
                        Enchantment.dynamicCost(12, 11),
                        1,
                        EquipmentSlotGroup.OFFHAND
                ))
        );
        register(context, VIGOR, Enchantment.enchantment(
                Enchantment.definition(
                        items.getOrThrow(ModTags.SHIELDS_COMMON),
                        5,
                        1,
                        Enchantment.dynamicCost(1, 11),
                        Enchantment.dynamicCost(12, 11),
                        1,
                        EquipmentSlotGroup.OFFHAND
                ))
        );
        register(context, BASHFUL, Enchantment.enchantment(
                Enchantment.definition(
                        items.getOrThrow(ModTags.SHIELDS_COMMON),
                        5,
                        1,
                        Enchantment.dynamicCost(1, 11),
                        Enchantment.dynamicCost(12, 11),
                        1,
                        EquipmentSlotGroup.OFFHAND
                ))
        );
    }

    private static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        context.register(key, builder.build(key.identifier()));
    }
}
