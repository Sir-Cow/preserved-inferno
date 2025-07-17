package sircow.preservedinferno.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import sircow.preservedinferno.item.ModArmourMaterials;

import java.util.HashMap;
import java.util.Map;

@Mixin(Item.Properties.class)
public class ItemPropertiesMixin {
    @Unique
    private static final Map<ArmorMaterial, Map<ArmorType, Integer>> DURABILITY_MAP = new HashMap<>();

    static {
        Map<ArmorType, Integer> leatherDurability = new HashMap<>();
        leatherDurability.put(ArmorType.HELMET, 106);
        leatherDurability.put(ArmorType.CHESTPLATE, 170);
        leatherDurability.put(ArmorType.LEGGINGS, 150);
        leatherDurability.put(ArmorType.BOOTS, 86);
        DURABILITY_MAP.put(ArmorMaterials.LEATHER, leatherDurability);

        Map<ArmorType, Integer> goldDurability = new HashMap<>();
        goldDurability.put(ArmorType.HELMET, 106);
        goldDurability.put(ArmorType.CHESTPLATE, 170);
        goldDurability.put(ArmorType.LEGGINGS, 150);
        goldDurability.put(ArmorType.BOOTS, 86);
        DURABILITY_MAP.put(ArmorMaterials.GOLD, goldDurability);

        Map<ArmorType, Integer> chainmailDurability = new HashMap<>();
        chainmailDurability.put(ArmorType.HELMET, 213);
        chainmailDurability.put(ArmorType.CHESTPLATE, 341);
        chainmailDurability.put(ArmorType.LEGGINGS, 299);
        chainmailDurability.put(ArmorType.BOOTS, 171);
        DURABILITY_MAP.put(ArmorMaterials.CHAINMAIL, chainmailDurability);

        Map<ArmorType, Integer> ironDurability = new HashMap<>();
        ironDurability.put(ArmorType.HELMET, 213);
        ironDurability.put(ArmorType.CHESTPLATE, 341);
        ironDurability.put(ArmorType.LEGGINGS, 299);
        ironDurability.put(ArmorType.BOOTS, 171);
        DURABILITY_MAP.put(ArmorMaterials.IRON, ironDurability);

        Map<ArmorType, Integer> diamondDurability = new HashMap<>();
        diamondDurability.put(ArmorType.HELMET, 854);
        diamondDurability.put(ArmorType.CHESTPLATE, 1366);
        diamondDurability.put(ArmorType.LEGGINGS, 1194);
        diamondDurability.put(ArmorType.BOOTS, 682);
        DURABILITY_MAP.put(ArmorMaterials.DIAMOND, diamondDurability);

        Map<ArmorType, Integer> netheriteDurability = new HashMap<>();
        netheriteDurability.put(ArmorType.HELMET, 854);
        netheriteDurability.put(ArmorType.CHESTPLATE, 1366);
        netheriteDurability.put(ArmorType.LEGGINGS, 1194);
        netheriteDurability.put(ArmorType.BOOTS, 682);
        DURABILITY_MAP.put(ArmorMaterials.NETHERITE, netheriteDurability);

        Map<ArmorType, Integer> turtleDurability = new HashMap<>();
        turtleDurability.put(ArmorType.HELMET, 512);
        DURABILITY_MAP.put(ArmorMaterials.TURTLE_SCUTE, turtleDurability);

        Map<ArmorType, Integer> copperDurability = new HashMap<>();
        copperDurability.put(ArmorType.HELMET, 106);
        copperDurability.put(ArmorType.CHESTPLATE, 170);
        copperDurability.put(ArmorType.LEGGINGS, 150);
        copperDurability.put(ArmorType.BOOTS, 86);
        DURABILITY_MAP.put(ModArmourMaterials.COPPER, copperDurability);
    }

    @ModifyReturnValue(method = "humanoidArmor", at = @At("RETURN"))
    public Item.Properties preserved_inferno$modifyHumanoidArmorProperties(Item.Properties original, ArmorMaterial material, ArmorType type) {
        if (DURABILITY_MAP.containsKey(material) && DURABILITY_MAP.get(material).containsKey(type)) {
            return original.durability(DURABILITY_MAP.get(material).get(type));
        }
        return original;
    }

    @ModifyReturnValue(method = "axe", at = @At("RETURN"))
    public Item.Properties preserved_inferno$modifyAxeProperties(Item.Properties original, ToolMaterial material, float attackDamage, float attackSpeed) {
        return original.tool(material, BlockTags.MINEABLE_WITH_AXE, attackDamage, attackSpeed, 0.0F);
    }
}
