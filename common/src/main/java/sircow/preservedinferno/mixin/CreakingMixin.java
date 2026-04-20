package sircow.preservedinferno.mixin;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.creaking.Creaking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.effect.ModEffects;
import sircow.preservedinferno.other.ModDamageTypes;

@Mixin(Creaking.class)
public class CreakingMixin {
    @Inject(method = "doHurtTarget", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$trueDamage(ServerLevel level, Entity target, CallbackInfoReturnable<Boolean> cir) {
        if (!(target instanceof LivingEntity living)) {
            cir.setReturnValue(false);
            return;
        }

        Creaking self = (Creaking)(Object)this;

        float damage;
        Difficulty difficulty = level.getDifficulty();

        switch (difficulty) {
            case EASY -> damage = 1.5F;
            case HARD -> damage = 6.0F;
            default -> damage = 3.0F;
        }

        DamageSource source = new DamageSource(
                level.registryAccess()
                        .lookupOrThrow(Registries.DAMAGE_TYPE)
                        .getOrThrow(ModDamageTypes.TRUE_DAMAGE),
                self
        );

        living.hurt(source, damage);
        living.addEffect(new MobEffectInstance(ModEffects.HINDERED.holder, 60, 0, false, true, true));
        self.swing(self.getUsedItemHand());
        self.level().broadcastEntityEvent(self, (byte)4);
        cir.setReturnValue(true);
    }
}
