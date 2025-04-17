package sircow.preservedinferno.mixin;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Foods.class)
public class FoodsMixin {
    @Mutable @Final @Shadow public static FoodProperties APPLE;
    @Mutable @Final @Shadow public static FoodProperties BAKED_POTATO;
    @Mutable @Final @Shadow public static FoodProperties BEETROOT;
    @Mutable @Final @Shadow public static FoodProperties BEETROOT_SOUP;
    @Mutable @Final @Shadow public static FoodProperties BREAD;
    @Mutable @Final @Shadow public static FoodProperties CARROT;
    @Mutable @Final @Shadow public static FoodProperties CHORUS_FRUIT;
    @Mutable @Final @Shadow public static FoodProperties COOKED_CHICKEN;
    @Mutable @Final @Shadow public static FoodProperties COOKED_COD;
    @Mutable @Final @Shadow public static FoodProperties COOKED_MUTTON;
    @Mutable @Final @Shadow public static FoodProperties COOKED_PORKCHOP;
    @Mutable @Final @Shadow public static FoodProperties COOKED_RABBIT;
    @Mutable @Final @Shadow public static FoodProperties COOKED_SALMON;
    @Mutable @Final @Shadow public static FoodProperties COOKIE;
    @Mutable @Final @Shadow public static FoodProperties DRIED_KELP;
    @Mutable @Final @Shadow public static FoodProperties ENCHANTED_GOLDEN_APPLE;
    @Mutable @Final @Shadow public static FoodProperties GOLDEN_APPLE;
    @Mutable @Final @Shadow public static FoodProperties GLOW_BERRIES;
    @Mutable @Final @Shadow public static FoodProperties GOLDEN_CARROT;
    @Mutable @Final @Shadow public static FoodProperties HONEY_BOTTLE;
    @Mutable @Final @Shadow public static FoodProperties MELON_SLICE;
    @Mutable @Final @Shadow public static FoodProperties MUSHROOM_STEW;
    @Mutable @Final @Shadow public static FoodProperties POISONOUS_POTATO;
    @Mutable @Final @Shadow public static FoodProperties POTATO;
    @Mutable @Final @Shadow public static FoodProperties PUFFERFISH;
    @Mutable @Final @Shadow public static FoodProperties PUMPKIN_PIE;
    @Mutable @Final @Shadow public static FoodProperties RABBIT_STEW;
    @Mutable @Final @Shadow public static FoodProperties BEEF;
    @Mutable @Final @Shadow public static FoodProperties CHICKEN;
    @Mutable @Final @Shadow public static FoodProperties COD;
    @Mutable @Final @Shadow public static FoodProperties MUTTON;
    @Mutable @Final @Shadow public static FoodProperties PORKCHOP;
    @Mutable @Final @Shadow public static FoodProperties RABBIT;
    @Mutable @Final @Shadow public static FoodProperties SALMON;
    @Mutable @Final @Shadow public static FoodProperties ROTTEN_FLESH;
    @Mutable @Final @Shadow public static FoodProperties SPIDER_EYE;
    @Mutable @Final @Shadow public static FoodProperties COOKED_BEEF;
    @Mutable @Final @Shadow public static FoodProperties SUSPICIOUS_STEW;
    @Mutable @Final @Shadow public static FoodProperties SWEET_BERRIES;
    @Mutable @Final @Shadow public static FoodProperties TROPICAL_FISH;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void preserved_inferno$modifyFood(CallbackInfo ci) {
        APPLE = new FoodProperties.Builder().nutrition(4).saturationModifier(1.0F).build();
        BAKED_POTATO = new FoodProperties.Builder().nutrition(5).saturationModifier(1.2F).build();
        BEETROOT = new FoodProperties.Builder().nutrition(2).saturationModifier(1.0F).build();
        BEETROOT_SOUP = new FoodProperties.Builder().nutrition(8).saturationModifier(1.5F).build();
        BREAD = new FoodProperties.Builder().nutrition(4).saturationModifier(0.8F).build();
        CARROT = new FoodProperties.Builder().nutrition(3).saturationModifier(1.0F).build();
        CHORUS_FRUIT = new FoodProperties.Builder().nutrition(2).saturationModifier(1.0F).alwaysEdible().build();
        COOKED_CHICKEN = new FoodProperties.Builder().nutrition(6).saturationModifier(1.2F).build();
        COOKED_COD = new FoodProperties.Builder().nutrition(5).saturationModifier(1.2F).build();
        COOKED_MUTTON = new FoodProperties.Builder().nutrition(6).saturationModifier(1.2F).build();
        COOKED_PORKCHOP = new FoodProperties.Builder().nutrition(6).saturationModifier(1.4F).build();
        COOKED_RABBIT = new FoodProperties.Builder().nutrition(5).saturationModifier(1.2F).build();
        COOKED_SALMON = new FoodProperties.Builder().nutrition(6).saturationModifier(1.2F).build();
        COOKIE = new FoodProperties.Builder().nutrition(2).saturationModifier(0.2F).build();
        DRIED_KELP = new FoodProperties.Builder().nutrition(1).saturationModifier(1.2F).build();
        ENCHANTED_GOLDEN_APPLE = new FoodProperties.Builder().nutrition(8).saturationModifier(1.2F).alwaysEdible().build();
        GOLDEN_APPLE = new FoodProperties.Builder().nutrition(4).saturationModifier(1.2F).alwaysEdible().build();
        GLOW_BERRIES = new FoodProperties.Builder().nutrition(2).saturationModifier(1.2F).build();
        GOLDEN_CARROT = new FoodProperties.Builder().nutrition(6).saturationModifier(2.4F).build();
        HONEY_BOTTLE = new FoodProperties.Builder().nutrition(6).saturationModifier(0.2F).build();
        MELON_SLICE = new FoodProperties.Builder().nutrition(2).saturationModifier(1.0F).build();
        MUSHROOM_STEW = new FoodProperties.Builder().nutrition(8).saturationModifier(1.5F).build();
        POISONOUS_POTATO = new FoodProperties.Builder().nutrition(2).saturationModifier(0.0F).build();
        POTATO = new FoodProperties.Builder().nutrition(2).saturationModifier(1.0F).build();
        PUFFERFISH = new FoodProperties.Builder().nutrition(1).saturationModifier(0.2F).build();
        PUMPKIN_PIE = new FoodProperties.Builder().nutrition(8).saturationModifier(1.2F).build();
        RABBIT_STEW = new FoodProperties.Builder().nutrition(10).saturationModifier(1.2F).build();
        BEEF = new FoodProperties.Builder().nutrition(2).saturationModifier(0.6F).build();
        CHICKEN = new FoodProperties.Builder().nutrition(2).saturationModifier(0.6F).build();
        COD = new FoodProperties.Builder().nutrition(2).saturationModifier(0.6F).build();
        MUTTON = new FoodProperties.Builder().nutrition(2).saturationModifier(0.6F).build();
        PORKCHOP = new FoodProperties.Builder().nutrition(2).saturationModifier(0.6F).build();
        RABBIT = new FoodProperties.Builder().nutrition(2).saturationModifier(0.6F).build();
        SALMON = new FoodProperties.Builder().nutrition(2).saturationModifier(0.6F).build();
        ROTTEN_FLESH = new FoodProperties.Builder().nutrition(4).saturationModifier(0.0F).build();
        SPIDER_EYE = new FoodProperties.Builder().nutrition(2).saturationModifier(0.0F).build();
        COOKED_BEEF = new FoodProperties.Builder().nutrition(6).saturationModifier(1.2F).build();
        SUSPICIOUS_STEW = new FoodProperties.Builder().nutrition(8).saturationModifier(1.6F).alwaysEdible().build();
        SWEET_BERRIES = new FoodProperties.Builder().nutrition(2).saturationModifier(1.2F).build();
        TROPICAL_FISH = new FoodProperties.Builder().nutrition(2).saturationModifier(0.6F).build();
    }
}
