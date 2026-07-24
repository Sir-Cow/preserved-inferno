package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.golem.CopperGolem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CopperGolem.class)
public class CopperGolemMixin {
    @Inject(method = "createAttributes", at = @At("RETURN"), cancellable = true)
    private static void pinferno$overwriteAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.setReturnValue(Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.2F)
                .add(Attributes.STEP_HEIGHT, 1.0)
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.ARMOR, 10.0)
        );
    }
}
