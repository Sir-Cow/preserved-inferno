package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
}
