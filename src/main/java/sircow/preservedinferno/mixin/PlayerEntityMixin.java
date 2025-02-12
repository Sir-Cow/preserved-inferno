package sircow.preservedinferno.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Unique
    boolean isFromTurtleHelmet = false;

    // make water breathing duration from turtle helmet infinite, also check the water breathing is ONLY from the turtle helmet
    @Inject(method = "updateTurtleHelmet", at = @At("HEAD"), cancellable = true)
    private void updateTurtleHelmet(CallbackInfo ci) {
        ItemStack itemStack = this.getEquippedStack(EquipmentSlot.HEAD);
        if (itemStack.isOf(Items.TURTLE_HELMET) && this.isSubmergedIn(FluidTags.WATER)) {
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, -1, 0, false, false, true));
            isFromTurtleHelmet = true;
        }
        if ((!itemStack.isOf(Items.TURTLE_HELMET) || !this.isSubmergedIn(FluidTags.WATER)) && isFromTurtleHelmet) {
            this.removeStatusEffect(StatusEffects.WATER_BREATHING);
            isFromTurtleHelmet = false;
        }
        ci.cancel();
    }
}
