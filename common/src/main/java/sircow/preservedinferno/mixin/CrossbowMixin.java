package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.Captures;

@Mixin(CrossbowItem.class)
public class CrossbowMixin {
    // make the crossbow only use 1 durability when firing a rocket
    @ModifyConstant(method = "getDurabilityUse", constant = @Constant(intValue = 3))
    private int pinferno$modifyIntValue(int original) {
        return 1;
    }

    // reduce damage by 75% if multishot is present
    @Inject(method = "shootProjectile", at = @At("HEAD"))
    private void pinferno$modifyArrowDamage(LivingEntity livingEntity, Projectile projectileEntity, int index, float power, float uncertainty, float angle, LivingEntity targetOverride, CallbackInfo ci) {
        ItemStack[] hands = {livingEntity.getMainHandItem(), livingEntity.getOffhandItem()};
        if (projectileEntity instanceof AbstractArrow arrow) {
            for (ItemStack itemStack : hands) {
                if (itemStack.getItem() instanceof CrossbowItem &&
                        EnchantmentHelper.getItemEnchantmentLevel(livingEntity.level().registryAccess()
                                .lookupOrThrow(Enchantments.MULTISHOT.registryKey())
                                .getOrThrow(Enchantments.MULTISHOT), itemStack) > 0) {
                    double originalDamage = Captures.arrowBaseDamage;
                    double modifiedDamage = originalDamage * 0.75; // Reduce damage to 75%
                    arrow.setBaseDamage(modifiedDamage);
                    break;
                }
            }
        }
    }

    // modify crossbow draw speed
    @ModifyVariable(method = "getChargeDuration", at = @At("STORE"), name = "duration")
    private static float pinferno$modifyDrawSpd(float duration) {
        return 1.0F;
    }
}
