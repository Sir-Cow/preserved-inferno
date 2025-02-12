package sircow.preservedinferno.mixin;

import net.minecraft.entity.player.HungerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(HungerManager.class)
public class HungerManagerMixin {
    @ModifyConstant(method = "update", constant = @Constant(intValue = 10))
    private int modifySaturationHealRateAgain(int constant) {
        return 30;
    }
}
