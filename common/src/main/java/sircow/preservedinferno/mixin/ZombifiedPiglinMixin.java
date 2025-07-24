package sircow.preservedinferno.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.other.HeatAccessor;
import sircow.preservedinferno.other.HeatAggroGoal;

@Mixin(ZombifiedPiglin.class)
public class ZombifiedPiglinMixin {
    @Inject(method = "createAttributes", at = @At("RETURN"), cancellable = true)
    private static void preserved_inferno$overwriteAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.setReturnValue(Zombie.createAttributes()
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.0)
                .add(Attributes.MOVEMENT_SPEED, 0.23F)
                .add(Attributes.ATTACK_DAMAGE, 5.0)
                .add(Attributes.ARMOR, 10.0));
    }

    @Inject(method = "alertOthers", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$conditionallyCancelAlertOthers(CallbackInfo ci) {
        ZombifiedPiglin self = (ZombifiedPiglin)(Object)this;

        if (!(self.getTarget() instanceof ServerPlayer player)) return;

        int heat = ((HeatAccessor) player).preserved_inferno$getHeat();
        double heatRadius = 0.4 * heat;
        double distSqr = self.distanceToSqr(player);

        if (heat > 0 && distSqr <= heatRadius * heatRadius) {
            ci.cancel();
        }
    }

    @Inject(method = "maybeAlertOthers", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$disableMaybeGroupAlert(CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "addBehaviourGoals", at = @At("TAIL"))
    private void preserved_inferno$replaceHurtByGoal(CallbackInfo ci) {
        ZombifiedPiglin self = (ZombifiedPiglin)(Object)this;
        ((MobAccessor) self).preserved_inferno$getTargetSelector().removeAllGoals(goal -> goal instanceof HurtByTargetGoal);
        ((MobAccessor) self).preserved_inferno$getTargetSelector().addGoal(1, new HeatAggroGoal(self));
    }
}
