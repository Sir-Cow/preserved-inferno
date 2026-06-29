package sircow.preservedinferno.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sircow.preservedinferno.effect.consume.IgniteConsumeEffect;
import sircow.preservedinferno.other.HeatAccessor;

public class ModConsumables {
    public static final Consumable LAVA_BOTTLE = defaultDrink()
            .onConsume(new IgniteConsumeEffect(15.0F))
            .onConsume(new ConsumeEffect() {
                @Override
                public @NotNull Type<? extends ConsumeEffect> getType() { return null; }

                @Override
                public boolean apply(@NotNull Level level, @NotNull ItemStack itemStack, @NotNull LivingEntity livingEntity) {
                    if (livingEntity instanceof Player player && !livingEntity.fireImmune()) {
                        if (!level.isClientSide()) {
                            HeatAccessor heatAccessor = (HeatAccessor) player;
                            heatAccessor.pinferno$increaseHeat(5);
                        }
                    }
                    return false;
                }
            })
            .build();
    public static final Consumable MILK_BOTTLE = defaultDrink()
            .onConsume(ClearAllStatusEffectsConsumeEffect.INSTANCE)
            .onConsume(new ConsumeEffect() {
                @Override
                public @NotNull Type<? extends ConsumeEffect> getType() { return null; }

                @Override
                public boolean apply(@NotNull Level level, @NotNull ItemStack itemStack, @NotNull LivingEntity livingEntity) {
                    if (livingEntity instanceof Player player) {
                        if (!level.isClientSide()) {
                            HeatAccessor heatAccessor = (HeatAccessor) player;
                            if (heatAccessor.pinferno$getHeat() > 0) heatAccessor.pinferno$decreaseHeat(5);
                        }
                    }
                    return false;
                }
            })
            .build();

    public static Consumable.Builder defaultDrink() {
        return Consumable.builder().consumeSeconds(1.6F).animation(ItemUseAnimation.DRINK).sound(SoundEvents.GENERIC_DRINK).hasConsumeParticles(false);
    }
}
