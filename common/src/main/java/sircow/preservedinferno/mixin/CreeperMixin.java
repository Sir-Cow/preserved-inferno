package sircow.preservedinferno.mixin;

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
    private void preserved_inferno$onInit(EntityType<? extends Creeper> entityType, Level level, CallbackInfo ci) {
        explosionRadius = 4;
        maxSwell = 24;
    }

    @Inject(method = "createAttributes", at = @At("RETURN"), cancellable = true)
    private static void preserved_inferno$overwriteAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.setReturnValue(Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
        );
    }

    @ModifyConstant(method = "readAdditionalSaveData", constant = @Constant(intValue = 30))
    private int preserved_inferno$modifySaveData(int original) {
        return 24;
    }
    @ModifyConstant(method = "readAdditionalSaveData", constant = @Constant(intValue = 3))
    private int preserved_inferno$modifySaveData2(int original) {
        return 4;
    }
    @ModifyConstant(method = "causeFallDamage", constant = @Constant(doubleValue = 1.5))
    private double preserved_inferno$modifyFallDamageMulti(double original) {
        return 0;
    }
}
