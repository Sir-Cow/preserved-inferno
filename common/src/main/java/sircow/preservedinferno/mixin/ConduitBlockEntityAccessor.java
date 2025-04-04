package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.ConduitBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ConduitBlockEntity.class)

public interface ConduitBlockEntityAccessor {
    @Accessor("destroyTarget")
    LivingEntity getTargetEntity();
}
