package sircow.preservedinferno.mixin;

import net.minecraft.world.item.ShovelItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import sircow.preservedinferno.RegisterItemChecker;

import java.util.Objects;

@Mixin(ShovelItem.class)
public class ShovelItemMixin {
    // modify shovel attackDamage
    @ModifyArg(method = "<init>", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item$Properties;shovel(Lnet/minecraft/world/item/ToolMaterial;FF)Lnet/minecraft/world/item/Item$Properties;"),
            index = 1
    )
    private static float preserved_inferno$modifyAttackDamage(float attackDamage) {
        if (RegisterItemChecker.flip) {
            if (RegisterItemChecker.SHOVELS.contains(RegisterItemChecker.itemName)) {
                if (Objects.equals(RegisterItemChecker.itemName, "wooden_shovel")
                        || Objects.equals(RegisterItemChecker.itemName, "golden_shovel")
                        || Objects.equals(RegisterItemChecker.itemName, "stone_shovel")) {
                    attackDamage = 0.5F;
                }
                else {
                    return attackDamage;
                }
            }
        }
        return attackDamage;
    }
    // modify shovel attackSpeed
    @ModifyArg(method = "<init>", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item$Properties;shovel(Lnet/minecraft/world/item/ToolMaterial;FF)Lnet/minecraft/world/item/Item$Properties;"),
            index = 2,
            remap = false
    )
    private static float preserved_inferno$modifyAttackSpeed(float attackSpeed) {
        if (RegisterItemChecker.flip) {
            if (RegisterItemChecker.SHOVELS.contains(RegisterItemChecker.itemName)) {
                attackSpeed = -2.5F;
            }
            RegisterItemChecker.flip = false;
        }
        return attackSpeed;
    }
}
