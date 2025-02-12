package sircow.preservedinferno.mixin;

import net.minecraft.block.entity.ConduitBlockEntity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ConduitBlockEntity.class)

public interface ConduitBlockEntityAccessor {
    @Accessor("targetEntity")
    LivingEntity getTargetEntity();
}
