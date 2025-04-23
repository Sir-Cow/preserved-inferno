package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.boss.wither.WitherBoss;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(WitherBoss.class)
public class WitherBossMixin {
    // modify armour value
    @ModifyArg(
            method = "createAttributes",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;add(Lnet/minecraft/core/Holder;D)Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;",
                    ordinal = 4),
            index = 1
    )
    private static double preserved_inferno$modifyDamage(double baseValue) {
        baseValue = 10.0F;
        return baseValue;
    }
}
