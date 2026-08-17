package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.ai.goal.RunAroundLikeCrazyGoal;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RunAroundLikeCrazyGoal.class)
public interface RunAroundLikeCrazyGoalAccessor {
    @Accessor("horse")
    AbstractHorse pinferno$getHorse();
}
