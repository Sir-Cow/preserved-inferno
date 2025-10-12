package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AgeableMob.class)
public class AgeableMobMixin extends PathfinderMob {
    protected AgeableMobMixin(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    @Inject(method = "setBaby", at = @At("TAIL"))
    private void preserved_inferno$modifyBaby(boolean baby, CallbackInfo ci) {
        if (this.level().isClientSide()) return;

        AttributeInstance health = this.getAttribute(Attributes.MAX_HEALTH);
        if (health == null) return;

        if (baby) {
            health.setBaseValue(health.getBaseValue() * 0.75);
        }
        else {
            health.setBaseValue(health.getBaseValue());
        }
        this.setHealth(this.getMaxHealth());
    }
}
