package sircow.preservedinferno.mixin;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(ArmorMaterial.class)
public class ArmorMaterialMixin {
    @Final @Shadow private float knockbackResistance;
    @Final @Shadow private float toughness;
    @Final @Shadow private Map<ArmorType, Integer> defense;
    @Shadow @Final private ResourceKey<EquipmentAsset> assetId;

    @Unique private static final Map<ArmorType, Float> LEATHER_TOUGHNESS = new HashMap<>();
    @Unique private static final Map<ArmorType, Float> CHAINMAIL_TOUGHNESS = new HashMap<>();
    @Unique private static final Map<ArmorType, Float> IRON_TOUGHNESS = new HashMap<>();
    @Unique private static final Map<ArmorType, Float> GOLD_TOUGHNESS = new HashMap<>();
    @Unique private static final Map<ArmorType, Float> DIAMOND_TOUGHNESS = new HashMap<>();
    @Unique private static final Map<ArmorType, Float> NETHERITE_TOUGHNESS = new HashMap<>();
    @Unique private static final Map<ArmorType, Float> TURTLE_TOUGHNESS = new HashMap<>();

    static {
        LEATHER_TOUGHNESS.put(ArmorType.HELMET, 0.0f); LEATHER_TOUGHNESS.put(ArmorType.CHESTPLATE, 1.0f);
        LEATHER_TOUGHNESS.put(ArmorType.LEGGINGS, 1.0f); LEATHER_TOUGHNESS.put(ArmorType.BOOTS, 0.0f);
    }

    static {
        CHAINMAIL_TOUGHNESS.put(ArmorType.HELMET, 0.0f); CHAINMAIL_TOUGHNESS.put(ArmorType.CHESTPLATE, 2.0f);
        CHAINMAIL_TOUGHNESS.put(ArmorType.LEGGINGS, 1.0f); CHAINMAIL_TOUGHNESS.put(ArmorType.BOOTS, 0.0f);
    }

    static {
        IRON_TOUGHNESS.put(ArmorType.HELMET, 1.0f); IRON_TOUGHNESS.put(ArmorType.CHESTPLATE, 1.0f);
        IRON_TOUGHNESS.put(ArmorType.LEGGINGS, 1.0f); IRON_TOUGHNESS.put(ArmorType.BOOTS, 1.0f);
    }

    static {
        GOLD_TOUGHNESS.put(ArmorType.HELMET, 0.0f); GOLD_TOUGHNESS.put(ArmorType.CHESTPLATE, 2.0f);
        GOLD_TOUGHNESS.put(ArmorType.LEGGINGS, 1.0f); GOLD_TOUGHNESS.put(ArmorType.BOOTS, 0.0f);
    }

    static {
        DIAMOND_TOUGHNESS.put(ArmorType.HELMET, 1.0f); DIAMOND_TOUGHNESS.put(ArmorType.CHESTPLATE, 2.0f);
        DIAMOND_TOUGHNESS.put(ArmorType.LEGGINGS, 2.0f); DIAMOND_TOUGHNESS.put(ArmorType.BOOTS, 1.0f);
    }

    static {
        NETHERITE_TOUGHNESS.put(ArmorType.HELMET, 2.0f); NETHERITE_TOUGHNESS.put(ArmorType.CHESTPLATE, 2.0f);
        NETHERITE_TOUGHNESS.put(ArmorType.LEGGINGS, 2.0f); NETHERITE_TOUGHNESS.put(ArmorType.BOOTS, 2.0f);
    }

    static {
        TURTLE_TOUGHNESS.put(ArmorType.HELMET, 0.0f); TURTLE_TOUGHNESS.put(ArmorType.CHESTPLATE, 0.0f);
        TURTLE_TOUGHNESS.put(ArmorType.LEGGINGS, 0.0f); TURTLE_TOUGHNESS.put(ArmorType.BOOTS, 0.0f);
    }

    // modify toughness values for individual armour pieces
    @Inject(method = "createAttributes", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$modifyValues(ArmorType armorType, CallbackInfoReturnable<ItemAttributeModifiers> cir) {

        float toughVal;
        ItemAttributeModifiers.Builder itemattributemodifiers$builder = ItemAttributeModifiers.builder();
        EquipmentSlotGroup equipmentslotgroup = EquipmentSlotGroup.bySlot(armorType.getSlot());
        ResourceLocation resourcelocation = ResourceLocation.withDefaultNamespace("armor." + armorType.getName());

        if (assetId.toString().contains("leather")) {
            toughVal = LEATHER_TOUGHNESS.getOrDefault(armorType, 0.0F);
            this.defense.put(ArmorType.BOOTS, 3);
            this.defense.put(ArmorType.LEGGINGS, 6);
            this.defense.put(ArmorType.CHESTPLATE, 7);
            this.defense.put(ArmorType.HELMET, 4);
            this.defense.put(ArmorType.BODY, 20);
        }
        else if (assetId.toString().contains("chainmail")) {
            toughVal = CHAINMAIL_TOUGHNESS.getOrDefault(armorType, 0.0F);
            this.defense.put(ArmorType.BOOTS, 5);
            this.defense.put(ArmorType.LEGGINGS, 12);
            this.defense.put(ArmorType.CHESTPLATE, 15);
            this.defense.put(ArmorType.HELMET, 8);
            this.defense.put(ArmorType.BODY, 4);
        }
        else if (assetId.toString().contains("iron")) {
            toughVal = IRON_TOUGHNESS.getOrDefault(armorType, 0.0F);
            this.defense.put(ArmorType.BOOTS, 7);
            this.defense.put(ArmorType.LEGGINGS, 11);
            this.defense.put(ArmorType.CHESTPLATE, 13);
            this.defense.put(ArmorType.HELMET, 9);
            this.defense.put(ArmorType.BODY, 30);
        }
        else if (assetId.toString().contains("gold")) {
            toughVal = GOLD_TOUGHNESS.getOrDefault(armorType, 0.0F);
            this.defense.put(ArmorType.BOOTS, 4);
            this.defense.put(ArmorType.LEGGINGS, 9);
            this.defense.put(ArmorType.CHESTPLATE, 11);
            this.defense.put(ArmorType.HELMET, 6);
            this.defense.put(ArmorType.BODY, 40);
        }
        else if (assetId.toString().contains("diamond")) {
            toughVal = DIAMOND_TOUGHNESS.getOrDefault(armorType, 0.0F);
            this.defense.put(ArmorType.BOOTS, 10);
            this.defense.put(ArmorType.LEGGINGS, 17);
            this.defense.put(ArmorType.CHESTPLATE, 20);
            this.defense.put(ArmorType.HELMET, 13);
            this.defense.put(ArmorType.BODY, 50);
        }
        else if (assetId.toString().contains("netherite")) {
            toughVal = NETHERITE_TOUGHNESS.getOrDefault(armorType, 0.0F);
            this.defense.put(ArmorType.BOOTS, 13);
            this.defense.put(ArmorType.LEGGINGS, 24);
            this.defense.put(ArmorType.CHESTPLATE, 28);
            this.defense.put(ArmorType.HELMET, 15);
            this.defense.put(ArmorType.BODY, 11);
        }
        else if (assetId.toString().contains("turtle")) {
            toughVal = TURTLE_TOUGHNESS.getOrDefault(armorType, 0.0F);
            this.defense.put(ArmorType.BOOTS, 2);
            this.defense.put(ArmorType.LEGGINGS, 5);
            this.defense.put(ArmorType.CHESTPLATE, 6);
            this.defense.put(ArmorType.HELMET, 12);
            this.defense.put(ArmorType.BODY, 5);
        }
        else {
            toughVal = this.toughness;
        }

        int defenseVal = this.defense.getOrDefault(armorType, 0);

        itemattributemodifiers$builder.add(Attributes.ARMOR, new AttributeModifier(resourcelocation, defenseVal, AttributeModifier.Operation.ADD_VALUE), equipmentslotgroup);
        itemattributemodifiers$builder.add(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(resourcelocation, toughVal, AttributeModifier.Operation.ADD_VALUE), equipmentslotgroup);
        if (this.knockbackResistance > 0.0F) {
            itemattributemodifiers$builder.add(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(resourcelocation, this.knockbackResistance, AttributeModifier.Operation.ADD_VALUE), equipmentslotgroup);
        }

        cir.setReturnValue(itemattributemodifiers$builder.build());
        cir.cancel();
    }
}
