package sircow.preservedinferno.mixin;

import net.minecraft.world.item.ShovelItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import sircow.preservedinferno.RegisterItemChecker;

@Mixin(ShovelItem.class)
public class ShovelItemMixin {
    // modify shovel attackDamage
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;shovel(Lnet/minecraft/world/item/ToolMaterial;FF)Lnet/minecraft/world/item/Item$Properties;"), index = 1)
    private static float preserved_inferno$modifyAttackDamage(float attackDamage) {
        if (RegisterItemChecker.flip) {
            if (RegisterItemChecker.SHOVELS.contains(RegisterItemChecker.itemName)) {
                switch (RegisterItemChecker.itemName) {
                    case "wooden_shovel", "stone_shovel" -> attackDamage = 0.5F;
                    case "golden_shovel" -> attackDamage = 3.5F;
                    case "diamond_shovel" -> attackDamage = 2.5F;
                    case null, default -> {
                        return attackDamage;
                    }
                }
            }
        }
        return attackDamage;
    }
    // modify shovel attackSpeed
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;shovel(Lnet/minecraft/world/item/ToolMaterial;FF)Lnet/minecraft/world/item/Item$Properties;"), index = 2, remap = false)
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
