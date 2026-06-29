package sircow.preservedinferno.mixin;

import net.minecraft.world.damagesource.CombatRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(CombatRules.class)
public class CombatRulesMixin {
    @ModifyConstant(method = "getDamageAfterAbsorb", constant = @Constant(floatValue = 20.0F))
    private static float pinferno$modifyFloatVal1(float constant) {
        return 100.0F;
    }
    @ModifyConstant(method = "getDamageAfterAbsorb", constant = @Constant(floatValue = 4.0F))
    private static float pinferno$modifyFloatVal2(float constant) {
        return 20.0F;
    }
    @ModifyConstant(method = "getDamageAfterAbsorb", constant = @Constant(floatValue = 25.0F))
    private static float pinferno$modifyFloatVal3(float constant) {
        return 125.0F;
    }

    @ModifyConstant(method = "getDamageAfterMagicAbsorb", constant = @Constant(floatValue = 20.0F))
    private static float pinferno$modifyFloatVal4(float constant) {
        return 100.0F;
    }
    @ModifyConstant(method = "getDamageAfterMagicAbsorb", constant = @Constant(floatValue = 25.0F))
    private static float pinferno$modifyFloatVal5(float constant) {
        return 125.0F;
    }
}
