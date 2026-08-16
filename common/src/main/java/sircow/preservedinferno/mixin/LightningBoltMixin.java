package sircow.preservedinferno.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningBolt.class)
public class LightningBoltMixin {
    @Inject(method = "spawnFire", at = @At("HEAD"), cancellable = true)
    public void pinferno$cancelFireSpawning(CallbackInfo ci) {
        ci.cancel();
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;thunderHit(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LightningBolt;)V"))
    private void pinferno$skipItemThunderHit(Entity entity, ServerLevel level, LightningBolt bolt) {
        if (!(entity instanceof ItemEntity)) {
            entity.thunderHit(level, bolt);
        }
    }
}
