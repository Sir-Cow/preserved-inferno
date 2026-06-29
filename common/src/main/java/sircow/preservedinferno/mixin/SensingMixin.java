package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.sensing.Sensing;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Sensing.class)
public class SensingMixin {
    @Shadow @Final private Mob mob;

    @Inject(method = "hasLineOfSight", at = @At("HEAD"), cancellable = true)
    private void pinferno$zombieWallHack(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (this.mob instanceof Zombie) {
            if (entity instanceof Player) {
                if (this.mob.distanceToSqr(entity) <= 25.0 * 25.0) {
                    cir.setReturnValue(true);
                    cir.cancel();
                }
            }
        }
    }
}
