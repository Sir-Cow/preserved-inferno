package sircow.preservedinferno.mixin;

import net.minecraft.world.item.TippedArrowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(TippedArrowItem.class)
public class TippedArrowItemMixin {
    @ModifyConstant(method = "appendHoverText", constant = @Constant(floatValue = 0.125F))
    private float modifyFloatValue(float original) { return 0.5F; }
}
