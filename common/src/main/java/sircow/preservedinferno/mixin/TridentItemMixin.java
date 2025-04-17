package sircow.preservedinferno.mixin;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TridentItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import static net.minecraft.world.item.Item.BASE_ATTACK_DAMAGE_ID;
import static net.minecraft.world.item.Item.BASE_ATTACK_SPEED_ID;

@Mixin(value = TridentItem.class, priority = 1100)
public class TridentItemMixin {
    // allow riptide to be used outside of rain or touching water while having conduit power effect
    @Redirect(method = {"releaseUsing", "use"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isInWaterOrRain()Z"))
    private boolean preserved_inferno$replaceWaterCheck(Player playerEntity) {
        if (playerEntity.hasEffect(MobEffects.CONDUIT_POWER) || playerEntity.isInWaterOrRain()) {
            return true;
        }
        else if (!playerEntity.hasEffect(MobEffects.CONDUIT_POWER) && !playerEntity.isInWaterOrRain()) {
            return false;
        }
        return false;
    }
    // modify throw damage
    @ModifyArg(
            method = "releaseUsing",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;startAutoSpinAttack(IFLnet/minecraft/world/item/ItemStack;)V"),
            index = 1
    )
    private float preserved_inferno$modifySpinAttackDamage(float originalDamage) {
        return 9.0F;
    }

    // modify velocity
    @ModifyArg(
            method = "releaseUsing",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/Projectile;spawnProjectileFromRotation(Lnet/minecraft/world/entity/projectile/Projectile$ProjectileFactory;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;FFF)Lnet/minecraft/world/entity/projectile/Projectile;"),
            index = 5
    )
    private float preserved_inferno$modifyVelocity(float inaccuracy) {
        return 3.5F;
    }
    // modify accuracy
    @ModifyArg(
            method = "releaseUsing",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/Projectile;spawnProjectileFromRotation(Lnet/minecraft/world/entity/projectile/Projectile$ProjectileFactory;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;FFF)Lnet/minecraft/world/entity/projectile/Projectile;"),
            index = 6
    )
    private float preserved_inferno$modifyAccuracy(float inaccuracy) {
        return 0.0F;
    }

    // modify time it takes to charge up throw
    @ModifyConstant(method = "releaseUsing", constant = @Constant(intValue = 10), require = 0)
    private int preserved_inferno$modifyChargeTicks(int originalValue) {
        return 2;
    }

    // modify base damage and attack speed
    @ModifyArg(
            method = "createAttributes",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/component/ItemAttributeModifiers$Builder;add(Lnet/minecraft/core/Holder;Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;Lnet/minecraft/world/entity/EquipmentSlotGroup;)Lnet/minecraft/world/item/component/ItemAttributeModifiers$Builder;",
                    ordinal = 0),
            index = 1
    )
    private static AttributeModifier preserved_inferno$modifyDamage(AttributeModifier modifier) {
        modifier = new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 9.0, AttributeModifier.Operation.ADD_VALUE);
        return modifier;
    }

    @ModifyArg(
            method = "createAttributes",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/component/ItemAttributeModifiers$Builder;add(Lnet/minecraft/core/Holder;Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;Lnet/minecraft/world/entity/EquipmentSlotGroup;)Lnet/minecraft/world/item/component/ItemAttributeModifiers$Builder;",
                    ordinal = 1),
            index = 1
    )
    private static AttributeModifier preserved_inferno$modifyAttackSpeed(AttributeModifier modifier) {
        modifier = new AttributeModifier(BASE_ATTACK_SPEED_ID, -2.8F, AttributeModifier.Operation.ADD_VALUE);
        return modifier;
    }
}