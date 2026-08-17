package sircow.preservedinferno.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.level.ServerExplosion;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.effect.ModEffects;

@Mixin(ServerExplosion.class)
public abstract class ServerExplosionMixin {
    @Shadow @Final private Entity source;
    @Shadow @Final private float radius;
    @Shadow @Final private Vec3 center;

    @Inject(method = "hurtEntities", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/entity/Entity;hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private void pinferno$applyFumigated(CallbackInfo ci, @Local(name = "entity") Entity entity, @Local(name = "shouldDamageEntity") boolean shouldDamageEntity) {
        if (!shouldDamageEntity) return;
        if (!(this.source instanceof Creeper creeper)) return;
        if (!(entity instanceof LivingEntity living)) return;

        double distance = Math.sqrt(entity.distanceToSqr(this.center));
        double falloffStart = 0.5D;
        double maxDistance = this.radius * 2.0D;
        double normalized = distance <= falloffStart ? 0.0D : (distance - falloffStart) / (maxDistance - falloffStart);
        normalized = Math.clamp(normalized, 0.0D, 1.0D);
        int duration = Math.max(60, (int)(600 * (1.0D - normalized)));

        living.addEffect(new MobEffectInstance(ModEffects.FUMIGATED.holder, duration, 0), creeper);
    }

    @WrapOperation(method = "explode", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ServerExplosion;interactsWithBlocks()Z"))
    private boolean pinferno$preventBlockDamage(ServerExplosion instance, Operation<Boolean> original) {
        if (this.source instanceof Creeper || this.source instanceof LargeFireball) {
            return false;
        }
        return original.call(instance);
    }
}
