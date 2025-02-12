package sircow.preservedinferno.mixin;

import net.minecraft.item.TippedArrowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(TippedArrowItem.class)
public class TippedArrowItemMixin {
    @ModifyConstant(method = "appendTooltip", constant = @Constant(floatValue = 0.125F))
    private float modifyFloatValue(float original) { return 0.5F; }
}
