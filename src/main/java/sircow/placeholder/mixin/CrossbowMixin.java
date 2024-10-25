package sircow.placeholder.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrossbowItem.class)
public class CrossbowMixin {
    // make the crossbow only use 1 durability when firing a rocket
    @ModifyConstant(method = "getWeaponStackDamage", constant = @Constant(intValue = 3))
    private int modifyIntValue(int original) {
        return 1;
    }
    
    // reduce damage by 75% if multishot is present
    @Inject(method = "shoot", at = @At("HEAD"))
    private void modifyArrowDamage(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, LivingEntity target, CallbackInfo ci) {
        if (projectile instanceof PersistentProjectileEntity arrow) {
            for (ItemStack itemStack : shooter.getHandItems()) {
                if (itemStack.getItem() instanceof CrossbowItem &&
                        EnchantmentHelper.getLevel(shooter.getWorld().getRegistryManager()
                                .getOrThrow(Enchantments.MULTISHOT.getRegistryRef())
                                .getOrThrow(Enchantments.MULTISHOT), itemStack) > 0) {
                    double originalDamage = arrow.getDamage();
                    double modifiedDamage = originalDamage * 0.75; // Reduce damage to 75%
                    arrow.setDamage(modifiedDamage);
                    break;
                }
                /*
                else {
                    Placeholder.LOGGER.info("multishot not present {}", EnchantmentHelper.getLevel(shooter.getWorld().getRegistryManager().getWrapperOrThrow(Enchantments.MULTISHOT.getRegistryRef()).getOrThrow(Enchantments.MULTISHOT), itemStack));
                }
                */
            }
        }
    }
}