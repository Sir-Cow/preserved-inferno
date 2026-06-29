package sircow.preservedinferno.compat.mixin;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.block.entity.PreservedCauldronBlockEntity;
import sircow.preservedinferno.fluid.CauldronFluid;
import sircow.preservedinferno.fluid.ModFluids;
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
    private static void pinferno$replaceCauldronFluid(Accessor<?> accessor, CallbackInfoReturnable<List<ViewGroup<FluidView.Data>>> cir) {
        if (!(accessor instanceof BlockAccessor blockAccessor)) return;
        if (!(blockAccessor.getBlockEntity() instanceof PreservedCauldronBlockEntity cauldron)) return;

        long capacity = FluidConstants.BUCKET * 8L;
        long amount = (cauldron.fluid == CauldronFluid.EMPTY) ? 0L : (capacity / cauldron.maxFluidAmount) * cauldron.fluidAmount;

        JadeFluidObject fluidObj = switch (cauldron.fluid) {
            case HONEY -> JadeFluidObject.of(ModFluids.HONEY, amount);
            case LAVA -> JadeFluidObject.of(Fluids.LAVA, amount);
            case MILK -> JadeFluidObject.of(ModFluids.MILK, amount);
            case SNOW -> JadeFluidObject.of(ModFluids.SNOW, amount);
            case WATER -> JadeFluidObject.of(Fluids.WATER, amount);
            default -> JadeFluidObject.empty();
        };

        FluidView.Data data = new FluidView.Data(List.of(fluidObj), capacity);
        cir.setReturnValue(List.of(new ViewGroup<>(List.of(data))));
    }
}
