package sircow.preservedinferno.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import sircow.preservedinferno.effect.ModEffects;

import java.util.Objects;

@Mixin(MobEffectUtil.class)
public class MobEffectUtilMixin {
    @Overwrite
    public static boolean hasDigSpeed(LivingEntity mob) {
        return mob.hasEffect(MobEffects.HASTE) || mob.hasEffect(ModEffects.PINFERNO_CONDUIT_POWER.holder);
    }

    @Overwrite
    public static int getDigSpeedAmplification(LivingEntity mob) {
        int a = 0;
        int b = 0;

        if (mob.hasEffect(MobEffects.HASTE)) a = Objects.requireNonNull(mob.getEffect(MobEffects.HASTE)).getAmplifier();

        if (mob.hasEffect(ModEffects.PINFERNO_CONDUIT_POWER.holder)) {
            MobEffectInstance effect = mob.getEffect(ModEffects.PINFERNO_CONDUIT_POWER.holder);
            if (effect != null) b = effect.getAmplifier();
        }
        return Math.max(a, b);
    }

    @Overwrite
    public static boolean hasWaterBreathing(LivingEntity mob) {
        return mob.hasEffect(MobEffects.WATER_BREATHING) || mob.hasEffect(ModEffects.PINFERNO_CONDUIT_POWER.holder) || mob.hasEffect(MobEffects.BREATH_OF_THE_NAUTILUS);
    }

    @Overwrite
    public static boolean shouldEffectsRefillAirsupply(LivingEntity mob) {
        return !mob.hasEffect(MobEffects.BREATH_OF_THE_NAUTILUS) || mob.hasEffect(MobEffects.WATER_BREATHING) || mob.hasEffect(ModEffects.PINFERNO_CONDUIT_POWER.holder);
    }
}
