package sircow.preservedinferno.mixin;

import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BrewingStandBlockEntity.class)
public class BrewingStandBlockEntityMixin {
    @ModifyConstant(method = "serverTick", constant = @Constant(intValue = 400))
    private static int preserved_inferno$modifyBrewTime(int original) {
        return 160;
    }
}
