package sircow.preservedinferno.compat.mixin;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.block.entity.PreservedCauldronBlockEntity;
import snownee.jade.api.Accessor;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.fluid.JadeFluidObject;
import snownee.jade.api.view.FluidView;
import snownee.jade.api.view.ViewGroup;
import snownee.jade.util.CommonProxy;

import java.util.List;

@Mixin(CommonProxy.class)
public class CommonProxyMixin {
    @Inject(method = "wrapFluidStorage", at = @At("HEAD"), cancellable = true)
    private static void preserved_inferno$replaceCauldronFluid(Accessor<?> accessor, CallbackInfoReturnable<List<ViewGroup<FluidView.Data>>> cir) {
        if (accessor instanceof BlockAccessor blockAccessor && blockAccessor.getBlockEntity() instanceof PreservedCauldronBlockEntity cauldron) {
            long amount = (FluidConstants.BUCKET / 8L) * cauldron.progressWater;
            long capacity = (FluidConstants.BUCKET / 8L) * cauldron.maxWaterProgress;

            FluidView.Data data = new FluidView.Data(JadeFluidObject.of(Fluids.WATER, amount), capacity);
            cir.setReturnValue(List.of(new ViewGroup<>(List.of(data))));
        }
    }
}
