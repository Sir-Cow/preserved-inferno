package sircow.preservedinferno.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Piglin.class)
public class PiglinMixin extends Monster {
    protected PiglinMixin(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    // modify health value
    @ModifyArg(method = "createAttributes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;add(Lnet/minecraft/core/Holder;D)Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;", ordinal = 0), index = 1)
    private static double pinferno$modifyHealth(double baseValue) {
        baseValue = 24.0F;
        return baseValue;
    }

    @Inject(method = "setBaby", at = @At("TAIL"))
    private void pinferno$modifyBaby(boolean baby, CallbackInfo ci) {
        if (this.level().isClientSide()) return;

        AttributeInstance health = this.getAttribute(Attributes.MAX_HEALTH);
        if (health == null) return;

        if (baby) health.setBaseValue(health.getBaseValue() * 0.75);
        else health.setBaseValue(health.getBaseValue());

        this.setHealth(this.getMaxHealth());
    }

    // force picking up when mobGriefing false
    @Overwrite
    public boolean wantsToPickUp(@NonNull ServerLevel level, @NonNull ItemStack stack) {
        return this.canPickUpLoot() && PiglinAiAccessor.callWantsToPickup((Piglin) (Object) this, stack);
    }
}