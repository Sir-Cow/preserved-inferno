package sircow.preservedinferno.mixin;

import net.minecraft.client.gui.screens.inventory.BrewingStandScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BrewingStandScreen.class)
public class BrewingStandScreenMixin {
    @ModifyConstant(method = "renderBg", constant = @Constant(floatValue = 400.0F))
    private float modifyBrewTime(float original) {
        return 160;
    }
}
