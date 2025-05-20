package sircow.preservedinferno.mixin;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.other.HeatAccessor;

import java.util.List;

import static net.minecraft.world.item.component.Consumables.defaultDrink;
import static net.minecraft.world.item.component.Consumables.defaultFood;

@Mixin(Consumables.class)
public class ConsumablesMixin {
    @Mutable @Final @Shadow public static Consumable CHICKEN;
    @Mutable @Final @Shadow public static Consumable CHORUS_FRUIT;
    @Mutable @Final @Shadow public static Consumable ENCHANTED_GOLDEN_APPLE;
    @Mutable @Final @Shadow public static Consumable GOLDEN_APPLE;
    @Mutable @Final @Shadow public static Consumable HONEY_BOTTLE;
    @Mutable @Final @Shadow public static Consumable MILK_BUCKET;
    @Mutable @Final @Shadow public static Consumable POISONOUS_POTATO;
    @Mutable @Final @Shadow public static Consumable ROTTEN_FLESH;
    @Mutable @Final @Shadow public static Consumable SPIDER_EYE;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void preserved_inferno$modifyConsumableComponents(CallbackInfo ci) {
        CHICKEN = defaultFood()
                .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HUNGER, 300, 0), 0.3F))
                .build();
        CHORUS_FRUIT = defaultFood()
                .consumeSeconds(0.8F)
                .onConsume(new TeleportRandomlyConsumeEffect())
                .build();
        ENCHANTED_GOLDEN_APPLE = defaultFood()
                .onConsume(
                        new ApplyStatusEffectsConsumeEffect(
                                List.of(
                                        new MobEffectInstance(MobEffects.REGENERATION, 400, 1),
                                        new MobEffectInstance(MobEffects.RESISTANCE, 6000, 0),
                                        new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 6000, 0),
                                        new MobEffectInstance(MobEffects.ABSORPTION, 12000, 3)
                                )
                        )
                )
                .build();
        GOLDEN_APPLE = defaultFood()
                .onConsume(
                        new ApplyStatusEffectsConsumeEffect(List.of(
                                new MobEffectInstance(MobEffects.REGENERATION, 240, 1),
                                new MobEffectInstance(MobEffects.ABSORPTION, 12000, 0)
                        ))
                )
                .build();
        HONEY_BOTTLE = defaultDrink()
                .consumeSeconds(0.8F)
                .sound(SoundEvents.HONEY_DRINK)
                .onConsume(new RemoveStatusEffectsConsumeEffect(MobEffects.POISON))
                .build();
        MILK_BUCKET = defaultDrink()
                .onConsume(ClearAllStatusEffectsConsumeEffect.INSTANCE)
                .onConsume(new ConsumeEffect() {
                    @Override
                    public @NotNull Type<? extends ConsumeEffect> getType() { return null; }

                    @Override
                    public boolean apply(@NotNull Level level, @NotNull ItemStack itemStack, @NotNull LivingEntity livingEntity) {
                        if (livingEntity instanceof Player player) {
                            if (!level.isClientSide()) {
                                int currentHeat = ((HeatAccessor) player).preserved_inferno$getHeat();
                                if (currentHeat >= 15) {
                                    ((HeatAccessor) player).preserved_inferno$decreaseHeat(15);
                                }
                                else if (currentHeat > 0) {
                                    ((HeatAccessor) player).preserved_inferno$setHeat(0);
                                }
                            }
                        }
                        return false;
                    }
                })
                .build();
        POISONOUS_POTATO = defaultFood()
                .consumeSeconds(0.8F)
                .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.POISON, 100, 0), 0.6F))
                .build();
        ROTTEN_FLESH = defaultFood()
                .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 1.0F))
                .build();
        SPIDER_EYE = defaultFood()
                .consumeSeconds(0.8F)
                .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.POISON, 100, 0)))
                .build();
    }

    @ModifyConstant(method = "defaultDrink", constant = @Constant(floatValue = 1.6F))
    private static float preserved_inferno$modifyFloatValue(float original) { return 0.8F; }
}
