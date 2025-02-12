package sircow.preservedinferno.mixin;

import net.minecraft.world.spawner.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {
    // modify phantom spawn time
    @ModifyConstant(method = "spawn", constant = @Constant(intValue = 72000))
    private int modifyIntValue(int original) { return 168000; }
}
