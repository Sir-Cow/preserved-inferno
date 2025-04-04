package sircow.preservedinferno.mixin;

import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractFurnaceBlockEntity.class)
public interface AbstractFurnaceBlockEntityAccessor {
    @Accessor("cookingTotalTime")
    int getCookTimeTotal();;

    @Accessor("cookingTimer")
    int getCookTime();

    @Accessor("cookingTimer")
    void setCookTime(int cookTime);

    @Accessor("litTimeRemaining")
    int getBurnTime();

    @Accessor("litTimeRemaining")
    void setBurnTime(int burnTime);

    @Invoker("isLit")
    boolean invokeIsBurning();
}