package sircow.preservedinferno.mixin;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Shadow protected abstract boolean isEquipped(Item p_365145_);

    @Unique
    boolean isFromTurtleHelmet = false;

    // make water breathing duration from turtle helmet infinite, also check the water breathing is ONLY from the turtle helmet
    @Inject(method = "turtleHelmetTick", at = @At("HEAD"), cancellable = true)
    private void updateTurtleHelmet(CallbackInfo ci) {
        ItemStack itemStack = this.getItemBySlot(EquipmentSlot.HEAD);
        if (itemStack.is(Items.TURTLE_HELMET) && this.isEyeInFluid(FluidTags.WATER)) {
            this.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, -1, 0, false, false, true));
            isFromTurtleHelmet = true;
        }
        if ((!isEquipped(Items.TURTLE_HELMET) || !this.isEyeInFluid(FluidTags.WATER)) && isFromTurtleHelmet) {
            this.removeEffect(MobEffects.WATER_BREATHING);
            isFromTurtleHelmet = false;
        }
        ci.cancel();
    }
}
