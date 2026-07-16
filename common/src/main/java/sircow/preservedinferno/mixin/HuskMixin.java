package sircow.preservedinferno.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.zombie.Husk;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Husk.class)
public class HuskMixin {
    @WrapOperation(method = "finalizeSpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ServerLevelAccessor;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
    private boolean pinferno$restrictCamelHuskSpawn(ServerLevelAccessor level, Entity entity, Operation<Boolean> original) {
        BlockState ground = level.getBlockState(entity.blockPosition().below());

        if (ground.is(BlockTags.SAND) || ground.is(BlockTags.TERRACOTTA)) return original.call(level, entity);
        else return false;
    }
}
