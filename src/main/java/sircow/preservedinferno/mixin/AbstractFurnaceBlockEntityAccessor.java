package sircow.preservedinferno.mixin;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractFurnaceBlockEntity.class)
public interface AbstractFurnaceBlockEntityAccessor {
    @Accessor("cookingTotalTime")
    int getCookTimeTotal();;

    @Accessor("cookingTimeSpent")
    int getCookTime();

    @Accessor("cookingTimeSpent")
    void setCookTime(int cookTime);

    @Accessor("litTimeRemaining")
    int getBurnTime();

    @Accessor("litTimeRemaining")
    void setBurnTime(int burnTime);

    @Invoker("isBurning")
    boolean invokeIsBurning();
}