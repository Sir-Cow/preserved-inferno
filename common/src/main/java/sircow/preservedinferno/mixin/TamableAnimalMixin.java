package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.Team;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TamableAnimal.class)
public abstract class TamableAnimalMixin extends AgeableMob {
    protected TamableAnimalMixin(EntityType<? extends AgeableMob> type, Level level) {
        super(type, level);
    }

    @Inject(method = "applyTamingSideEffects", at = @At("TAIL"))
    private void pinferno$applyBabyHealthModifier(CallbackInfo ci) {
        if (this.level().isClientSide() || !this.isBaby()) return;

        AttributeInstance health = this.getAttribute(Attributes.MAX_HEALTH);
        if (health != null) {
            double currentBase = health.getBaseValue();
            health.setBaseValue(currentBase * 0.75);
            this.setHealth(this.getMaxHealth());
        }
    }

    @Inject(method = "getTeam", at = @At("RETURN"), cancellable = true)
    private void pinferno$preventRankPrefix(CallbackInfoReturnable<Team> cir) {
        TamableAnimal entity = (TamableAnimal) (Object) this;
        Team ownerTeam = cir.getReturnValue();
        if (ownerTeam == null || !entity.isTame()) return;

        if (entity.hasCustomName()) {
            cir.setReturnValue(null);
        }
    }
}
