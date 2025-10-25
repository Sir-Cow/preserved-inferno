package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AgeableMob.class)
public abstract class AgeableMobMixin extends PathfinderMob {
    @Shadow
    public abstract int getAge();

    @Unique
    private int prevAge = 0;

    protected AgeableMobMixin(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    @Inject(method = "setAge", at = @At("HEAD"))
    private void preserved_inferno$storePrevAge(int age, CallbackInfo ci) {
        if (!this.level().isClientSide()) {
            this.prevAge = this.getAge();
        }
    }

    @Inject(method = "setAge", at = @At("TAIL"))
    private void preserved_inferno$onAgeChanged(int age, CallbackInfo ci) {
        if (this.level().isClientSide()) return;

        int now = this.getAge();
        if ((this.prevAge < 0) == (now < 0)) return;

        AttributeInstance health = this.getAttribute(Attributes.MAX_HEALTH);
        if (health == null) return;

        @SuppressWarnings("unchecked")
        double defaultAdult = DefaultAttributes.getSupplier((EntityType<? extends LivingEntity>) this.getType()).getBaseValue(Attributes.MAX_HEALTH);

        if (now < 0) {
            health.setBaseValue(defaultAdult * 0.75);
        }
        else {
            health.setBaseValue(defaultAdult);
        }

        this.setHealth(this.getMaxHealth());
    }
}
