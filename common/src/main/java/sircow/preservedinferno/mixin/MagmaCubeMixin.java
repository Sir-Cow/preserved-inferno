package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.monster.MagmaCube;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MagmaCube.class)
public class MagmaCubeMixin {
    @Unique
    public int sizeTemp;

    @Inject(method = "setSize", at = @At("HEAD"))
    public void preserved_inferno$setSizeTemp(int size, boolean resetHealth, CallbackInfo ci) {
        this.sizeTemp = size;
    }

    @ModifyArg(
            method = "setSize(IZ)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;setBaseValue(D)V"),
            index = 0
    )
    private double preserved_inferno$modifyArmorValue(double baseValue) {
        return (double)(this.sizeTemp * 15);
    }
}
