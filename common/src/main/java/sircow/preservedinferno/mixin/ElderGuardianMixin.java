package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.monster.ElderGuardian;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ElderGuardian.class)
public class ElderGuardianMixin {
    @ModifyConstant(method = "createAttributes", constant = @Constant(doubleValue = 80.0F))
    private static double modifyDoubleValue(double original) {
        return 300.0F;
    }
}
