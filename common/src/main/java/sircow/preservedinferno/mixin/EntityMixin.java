package sircow.preservedinferno.mixin;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.other.FreezeAccessor;
import sircow.preservedinferno.other.HeatAccessor;

@Mixin(Entity.class)
public class EntityMixin implements FreezeAccessor {
    @Unique private int pinferno$freezeDelay;
    @Unique private int pinferno$lastFrozenTicks;

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

    @Inject(method = "startRiding(Lnet/minecraft/world/entity/Entity;ZZ)Z", at = @At("HEAD"), cancellable = true)
    private void pinferno$preventEndermanBoatRiding(Entity entityToRide, boolean force, boolean sendEventAndTriggers, CallbackInfoReturnable<Boolean> cir) {
        Entity self = (Entity) (Object) this;

        if (self instanceof EnderMan enderman && entityToRide instanceof AbstractBoat) {
            if (!enderman.level().isClientSide()) ((EnderManAccessor) enderman).pinferno$callTeleport();
            cir.setReturnValue(false);
        }
    }

    @ModifyConstant(method = "getTicksRequiredToFreeze", constant = @Constant(intValue = 140))
    private static int pinferno$modifyIntValue(int constant) {
        return 60;
    }

    @Override
    public int pinferno$getFreezeDelay() {
        return this.pinferno$freezeDelay;
    }

    @Override
    public void pinferno$setFreezeDelay(int delay) {
        this.pinferno$freezeDelay = delay;
    }

    @Inject(method = "setTicksFrozen", at = @At("HEAD"))
    private void pinferno$onSetTicksFrozen(int ticks, CallbackInfo ci) {
        if (ticks >= this.pinferno$lastFrozenTicks && ticks > 0) this.pinferno$freezeDelay = 0;

        this.pinferno$lastFrozenTicks = ticks;
    }
}
