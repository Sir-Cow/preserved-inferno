package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.ConduitBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(ConduitBlockEntity.class)
public interface ConduitBlockEntityAccessor {
    @Accessor("effectBlocks")
    List<BlockPos> getEffectBlocks();
}
