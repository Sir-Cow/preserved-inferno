package sircow.preservedinferno.mixin;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.*;

import static net.minecraft.world.item.Item.BASE_ATTACK_DAMAGE_ID;
import static net.minecraft.world.item.Item.BASE_ATTACK_SPEED_ID;

@Mixin(TridentItem.class)
public class TridentItemMixin {
    // allow riptide to be used outside of rain or touching water while having conduit power effect
    @Redirect(method = {"releaseUsing", "use"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isInWaterOrRain()Z"))
    private boolean pinferno$replaceWaterCheck(Player playerEntity) {
        if (playerEntity.hasEffect(MobEffects.CONDUIT_POWER) || playerEntity.isInWaterOrRain()) return true;
        else if (!playerEntity.hasEffect(MobEffects.CONDUIT_POWER) && !playerEntity.isInWaterOrRain()) return false;
        return false;
    }
    // modify throw damage
    @ModifyArg(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;startAutoSpinAttack(IFLnet/minecraft/world/item/ItemStack;)V"), index = 1)
    private float pinferno$modifySpinAttackDamage(float originalDamage) {
        return 11.0F;
    }

    // modify velocity
    @ModifyArg(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/Projectile;spawnProjectileFromRotation(Lnet/minecraft/world/entity/projectile/Projectile$ProjectileFactory;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;FFF)Lnet/minecraft/world/entity/projectile/Projectile;"), index = 5)
    private float pinferno$modifyVelocity(float inaccuracy) {
        return 3.0F;
    }
    // modify accuracy
    @ModifyArg(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/Projectile;spawnProjectileFromRotation(Lnet/minecraft/world/entity/projectile/Projectile$ProjectileFactory;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;FFF)Lnet/minecraft/world/entity/projectile/Projectile;"), index = 6)
    private float pinferno$modifyAccuracy(float inaccuracy) {
        return 0.0F;
    }

    // modify time it takes to charge up throw
    @ModifyConstant(method = "releaseUsing", constant = @Constant(intValue = 10), require = 0)
    private int pinferno$modifyChargeTicks(int originalValue) {
        return 4;
    }

    // modify base damage and attack speed
    @Overwrite
    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 11.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -2.8, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
    }
}
