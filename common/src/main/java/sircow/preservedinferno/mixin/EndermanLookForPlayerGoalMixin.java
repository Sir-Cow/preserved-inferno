package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.world.entity.monster.EnderMan$EndermanLookForPlayerGoal")
public class EndermanLookForPlayerGoalMixin {
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/EnderMan;isPassenger()Z"))
    private boolean pinferno$allowTeleportFromBoats(EnderMan enderman) {
        return enderman.isPassenger() && !(enderman.getVehicle() instanceof AbstractBoat);
    }
}
