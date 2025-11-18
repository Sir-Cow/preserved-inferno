package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Entity.class)
public class EntityMixin {
    @ModifyConstant(method = "getTicksRequiredToFreeze", constant = @Constant(intValue = 140))
    private static int preserved_inferno$modifyIntValue(int constant) {
        return 60;
    }
}
