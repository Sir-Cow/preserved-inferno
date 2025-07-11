package sircow.preservedinferno.item;

import com.google.common.collect.Maps;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import sircow.preservedinferno.other.ModTags;

import java.util.Map;

public class ModArmourMaterials {
    public static ArmorMaterial COPPER = new ArmorMaterial(
            7, makeDefense(9, 15, 13, 8, 25), 12, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, ModTags.COPPER_TOOL_MATERIALS, ModEquipmentAssets.COPPER
    );

    private static Map<ArmorType, Integer> makeDefense(int boots, int leggings, int chestplate, int helmet, int body) {
        return Maps.newEnumMap(
                Map.of(ArmorType.BOOTS, boots, ArmorType.LEGGINGS, leggings, ArmorType.CHESTPLATE, chestplate, ArmorType.HELMET, helmet, ArmorType.BODY, body)
        );
    }
}
