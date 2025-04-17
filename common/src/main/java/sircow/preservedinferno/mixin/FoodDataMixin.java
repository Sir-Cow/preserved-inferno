package sircow.preservedinferno.mixin;

import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(FoodData.class)
public class FoodDataMixin {
    @ModifyConstant(method = "tick", constant = @Constant(intValue = 10))
    private int preserved_inferno$modifySaturationHealRateAgain(int constant) {
        return 30;
    }
}
