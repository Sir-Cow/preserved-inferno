package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.monster.zombie.Drowned;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net.minecraft.world.entity.monster.zombie.Drowned$DrownedTridentAttackGoal")
public interface DrownedTridentAttackGoalAccessor {
    @Accessor("drowned")
    Drowned preserved_inferno$getDrowned();
}
