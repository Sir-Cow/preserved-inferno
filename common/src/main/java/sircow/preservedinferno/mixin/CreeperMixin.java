package sircow.preservedinferno.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Creeper.class)
public class CreeperMixin {
    @Shadow private int explosionRadius;
    @Shadow private int maxSwell;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void pinferno$onInit(EntityType<? extends Creeper> entityType, Level level, CallbackInfo ci) {
        explosionRadius = 4;
        maxSwell = 24;
    }

    @Inject(method = "createAttributes", at = @At("RETURN"), cancellable = true)
    private static void pinferno$overwriteAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.setReturnValue(Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
        );
    }

    @ModifyConstant(method = "readAdditionalSaveData", constant = @Constant(intValue = 30))
    private int pinferno$modifySaveData(int original) {
        return 24;
    }
    @ModifyConstant(method = "readAdditionalSaveData", constant = @Constant(intValue = 3))
    private int pinferno$modifySaveData2(int original) {
        return 4;
    }
    @ModifyConstant(method = "causeFallDamage", constant = @Constant(doubleValue = 1.5))
    private double pinferno$modifyFallDamageMulti(double original) {
        return 0;
    }

    @WrapOperation(method = "explodeCreeper", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;explode(Lnet/minecraft/world/entity/Entity;DDDFLnet/minecraft/world/level/Level$ExplosionInteraction;)V"))
    private void pinferno$modifyExplosionMultiplier(ServerLevel level, Entity source, double x, double y, double z, float radius, Level.ExplosionInteraction interaction, Operation<Void> original) {
        Creeper creeper = (Creeper) (Object) this;
        float multiplier = creeper.isPowered() ? 1.5F : 0.75F;

        original.call(level, source, x, y, z, this.explosionRadius * multiplier, interaction);
    }
}
