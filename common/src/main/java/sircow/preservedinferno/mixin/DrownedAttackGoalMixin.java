package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.zombie.Drowned;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import sircow.preservedinferno.item.ModItems;

@Mixin(targets = "net.minecraft.world.entity.monster.zombie.Drowned$DrownedTridentAttackGoal")
public class DrownedAttackGoalMixin extends RangedAttackGoal {
    @Shadow @Final private Drowned drowned;

    public DrownedAttackGoalMixin(RangedAttackMob rangedAttackMob, double speedModifier, int attackInterval, float attackRadius) {
        super(rangedAttackMob, speedModifier, attackInterval, attackRadius);
    }

    @Overwrite
    public boolean canUse() {
        return super.canUse() && this.drowned.getMainHandItem().is(ModItems.COPPER_TRIDENT.get());
    }
}
