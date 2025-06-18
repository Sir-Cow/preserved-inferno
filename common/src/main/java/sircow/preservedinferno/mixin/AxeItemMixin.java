package sircow.preservedinferno.mixin;

import net.minecraft.world.item.AxeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import sircow.preservedinferno.RegisterItemChecker;

import java.util.Objects;

@Mixin(AxeItem.class)
public class AxeItemMixin {
    // modify axe attackDamage
    @ModifyArg(method = "<init>", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item$Properties;axe(Lnet/minecraft/world/item/ToolMaterial;FF)Lnet/minecraft/world/item/Item$Properties;"),
            index = 1
    )
    private static float preserved_inferno$modifyAttackDamage(float attackDamage) {
        if (RegisterItemChecker.flip) {
            if (RegisterItemChecker.AXES.contains(RegisterItemChecker.itemName)) {
                if (Objects.equals(RegisterItemChecker.itemName, "iron_axe")) {
                    attackDamage = 4.0F;
                }
                else {
                    attackDamage = 3.0F;
                }
            }
        }
        return attackDamage;
    }
    // modify axe attackSpeed
    @ModifyArg(method = "<init>", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item$Properties;axe(Lnet/minecraft/world/item/ToolMaterial;FF)Lnet/minecraft/world/item/Item$Properties;"),
            index = 2,
            remap = false
    )
    private static float preserved_inferno$modifyAttackSpeed(float attackSpeed) {
        if (RegisterItemChecker.flip) {
            if (RegisterItemChecker.AXES.contains(RegisterItemChecker.itemName)) {
                attackSpeed = -3.0F;
            }
            RegisterItemChecker.flip = false;
        }
        return attackSpeed;
    }
}
