package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.entity.SmokerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin {
    @Inject(method = "serverTick", at = @At("HEAD"))
    private static void pinferno$onTick(ServerLevel level, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity furnace, CallbackInfo ci) {
        AbstractFurnaceBlockEntityAccessor accessor = (AbstractFurnaceBlockEntityAccessor) furnace;
        boolean isBurning = accessor.getBurnTime() > 0;

        if (furnace instanceof FurnaceBlockEntity || furnace instanceof SmokerBlockEntity || furnace instanceof BlastFurnaceBlockEntity) {
            if (isBurning && accessor.getCookTime() < (accessor.getCookTimeTotal() - 1) && accessor.getBurnTime() > 2) {
                accessor.setCookTime(accessor.getCookTime() + 1); // faster cooking/smelting
                accessor.setBurnTime(accessor.getBurnTime() - 1); // making the cooking/smelting faster means burn time of fuel needs to decrease quicker
            }
        }
    }
}
