package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.Captures;

@Mixin(CrossbowItem.class)
public class CrossbowMixin {
    // make the crossbow only use 1 durability when firing a rocket
    @ModifyConstant(method = "getDurabilityUse", constant = @Constant(intValue = 3))
    private int modifyIntValue(int original) {
        return 1;
    }
    
    // reduce damage by 75% if multishot is present
    @Inject(method = "shootProjectile", at = @At("HEAD"))
    private void modifyArrowDamage(LivingEntity shooter, Projectile projectile, int index, float speed, float divergence, float yaw, LivingEntity target, CallbackInfo ci) {
        ItemStack[] hands = {shooter.getMainHandItem(), shooter.getOffhandItem()};
        if (projectile instanceof AbstractArrow arrow) {
            for (ItemStack itemStack : hands) {
                if (itemStack.getItem() instanceof CrossbowItem &&
                        EnchantmentHelper.getItemEnchantmentLevel(shooter.level().registryAccess()
                                .lookupOrThrow(Enchantments.MULTISHOT.registryKey())
                                .getOrThrow(Enchantments.MULTISHOT), itemStack) > 0) {
                    double originalDamage = Captures.arrowBaseDamage;
                    double modifiedDamage = originalDamage * 0.75; // Reduce damage to 75%
                    arrow.setBaseDamage(modifiedDamage);
                    break;
                }
                /*
                else {
                    Constants.LOG.info("multishot not present {}", EnchantmentHelper.getLevel(shooter.getWorld().getRegistryManager().getWrapperOrThrow(Enchantments.MULTISHOT.getRegistryRef()).getOrThrow(Enchantments.MULTISHOT), itemStack));
                }
                */
            }
        }
    }
}