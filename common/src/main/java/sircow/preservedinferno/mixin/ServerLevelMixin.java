package sircow.preservedinferno.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    @Inject(method = "addEntity", at = @At("HEAD"))
    private void pinferno$cancelEndermanSpawningOverworld(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity.getType() == EntityTypes.ENDERMAN && entity.level().dimension() == Level.OVERWORLD) {
            entity.setRemoved(Entity.RemovalReason.DISCARDED);
        }
    }
}
