package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.other.HeatAggroGoal;

@Mixin(ZombifiedPiglin.class)
public class ZombifiedPiglinMixin {
    @Inject(method = "createAttributes", at = @At("RETURN"), cancellable = true)
    private static void pinferno$overwriteAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.setReturnValue(Zombie.createAttributes()
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(Attributes.ATTACK_DAMAGE, 5.0)
                .add(Attributes.MAX_HEALTH, 14.0)
                .add(Attributes.ARMOR, 10.0));
    }

    @Inject(method = "alertOthers", at = @At("HEAD"), cancellable = true)
    private void pinferno$disableVanillaAlert(CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "maybeAlertOthers", at = @At("HEAD"), cancellable = true)
    private void pinferno$disableMaybeGroupAlert(CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "addBehaviourGoals", at = @At("TAIL"))
    private void pinferno$replaceHurtByGoal(CallbackInfo ci) {
        ZombifiedPiglin self = (ZombifiedPiglin)(Object)this;
        ((MobAccessor) self).pinferno$getTargetSelector().removeAllGoals(goal -> goal instanceof HurtByTargetGoal);
        ((MobAccessor) self).pinferno$getTargetSelector().addGoal(1, new HeatAggroGoal(self));
    }
}
