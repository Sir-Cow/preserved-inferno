package sircow.preservedinferno.mixin;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.other.HeatAccessor;

@Mixin(Entity.class)
public class EntityMixin {
    @ModifyConstant(method = "getTicksRequiredToFreeze", constant = @Constant(intValue = 140))
    private static int pinferno$modifyIntValue(int constant) {
        return 60;
    }

    @Inject(method = "displayFireAnimation", at = @At("RETURN"), cancellable = true)
    private void pinferno$customFireCheck(CallbackInfoReturnable<Boolean> cir) {
        Entity self = (Entity) (Object) this;

        if (!(self instanceof AbstractClientPlayer player)) return;
        if (!(player instanceof HeatAccessor accessor)) return;

        if (accessor.pinferno$getHeat() < 100) return;
        if (player.isSpectator()) return;
        if (player.isCreative()) return;

        cir.setReturnValue(true);
    }
}
