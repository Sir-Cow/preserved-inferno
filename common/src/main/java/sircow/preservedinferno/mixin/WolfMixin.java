package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.wolf.Wolf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Wolf.class)
public class WolfMixin {
    // modify health value
    @Inject(method = "createAttributes", at = @At("RETURN"), cancellable = true)
    private static void pinferno$overwriteAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.setReturnValue(Animal.createAnimalAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.ATTACK_DAMAGE, 4.0)
        );
    }

    @Inject(method = "applyTamingSideEffects", at = @At("TAIL"))
    private void pinferno$fixTamingHealth(CallbackInfo ci) {
        Wolf wolf = (Wolf) (Object) this;
        AttributeInstance healthAttr = wolf.getAttribute(Attributes.MAX_HEALTH);

        if (healthAttr == null) return;

        double adultBase = wolf.isTame() ? 40.0 : 20.0;

        if (wolf.isBaby()) healthAttr.setBaseValue(adultBase * 0.75);
        else healthAttr.setBaseValue(adultBase);

        wolf.setHealth(wolf.getMaxHealth());
    }
}
