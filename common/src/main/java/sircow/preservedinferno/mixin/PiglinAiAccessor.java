package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PiglinAi.class)
public interface PiglinAiAccessor {
    @Invoker("wantsToPickup")
    static boolean callWantsToPickup(Piglin piglin, ItemStack stack) {
        throw new AssertionError();
    }
}
