package sircow.preservedinferno.effect.custom;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.effect.PreservedEffect;
import sircow.preservedinferno.other.ModDamageTypes;

public class FumigatedEffect extends PreservedEffect {
    public FumigatedEffect() {
        super(MobEffectCategory.HARMFUL, 0x1E4E18);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 10 == 0;
    }

    @Override
    public boolean applyEffectTick(final @NonNull ServerLevel serverLevel, final @NonNull LivingEntity mob, final int amplification) {
        DamageSource source = new DamageSource(mob.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(ModDamageTypes.FUMIGATED));

        if (mob.getHealth() > 1.0F) mob.hurtServer(serverLevel, source, (amplification + 1));

        if (mob instanceof Player player) player.causeFoodExhaustion(0.005F * (amplification + 1));

        return true;
    }
}
