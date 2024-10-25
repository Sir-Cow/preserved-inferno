package sircow.placeholder.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.block.entity.SmokerBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private static void onTick(ServerWorld world, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci) {
        AbstractFurnaceBlockEntityAccessor accessor = (AbstractFurnaceBlockEntityAccessor) blockEntity;

        if (blockEntity instanceof FurnaceBlockEntity
                || blockEntity instanceof SmokerBlockEntity
                || blockEntity instanceof BlastFurnaceBlockEntity) {
            if (accessor.invokeIsBurning() && accessor.getCookTime() < (accessor.getCookTimeTotal() - 1) && accessor.getBurnTime() > 2) {
                accessor.setCookTime(accessor.getCookTime() + 1); // faster cooking/smelting
                accessor.setBurnTime(accessor.getBurnTime() - 1); // making the cooking/smelting faster means burn time of fuel needs to decrease quicker
            }
        }
    }
}
