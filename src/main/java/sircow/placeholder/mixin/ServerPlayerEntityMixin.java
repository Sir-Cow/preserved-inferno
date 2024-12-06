package sircow.placeholder.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.placeholder.other.ModDamageTypes;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    // prevent advancements where player needs to kill a mob from granting when killed by conduit
    @Inject(method = "updateKilledAdvancementCriterion", at = @At("HEAD"), cancellable = true)
    private void preventAdvancementStatIncrease(Entity entityKilled, DamageSource damageSource, CallbackInfo ci) {
        if (damageSource.isOf(ModDamageTypes.TAG_CONDUIT)) {
            ci.cancel();
        }
    }
}
