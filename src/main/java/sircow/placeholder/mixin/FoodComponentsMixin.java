package sircow.placeholder.mixin;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.FoodComponents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodComponents.class)
public class FoodComponentsMixin {
    @Mutable @Final @Shadow public static FoodComponent APPLE;
    @Mutable @Final @Shadow public static FoodComponent BAKED_POTATO;
    @Mutable @Final @Shadow public static FoodComponent BEETROOT;
    @Mutable @Final @Shadow public static FoodComponent BEETROOT_SOUP;
    @Mutable @Final @Shadow public static FoodComponent BREAD;
    @Mutable @Final @Shadow public static FoodComponent CARROT;
    @Mutable @Final @Shadow public static FoodComponent CHORUS_FRUIT;
    @Mutable @Final @Shadow public static FoodComponent COOKED_CHICKEN;
    @Mutable @Final @Shadow public static FoodComponent COOKED_COD;
    @Mutable @Final @Shadow public static FoodComponent COOKED_MUTTON;
    @Mutable @Final @Shadow public static FoodComponent COOKED_PORKCHOP;
    @Mutable @Final @Shadow public static FoodComponent COOKED_RABBIT;
    @Mutable @Final @Shadow public static FoodComponent COOKED_SALMON;
    @Mutable @Final @Shadow public static FoodComponent COOKIE;
    @Mutable @Final @Shadow public static FoodComponent DRIED_KELP;
    @Mutable @Final @Shadow public static FoodComponent ENCHANTED_GOLDEN_APPLE;
    @Mutable @Final @Shadow public static FoodComponent GOLDEN_APPLE;
    @Mutable @Final @Shadow public static FoodComponent GLOW_BERRIES;
    @Mutable @Final @Shadow public static FoodComponent GOLDEN_CARROT;
    @Mutable @Final @Shadow public static FoodComponent HONEY_BOTTLE;
    @Mutable @Final @Shadow public static FoodComponent MELON_SLICE;
    @Mutable @Final @Shadow public static FoodComponent MUSHROOM_STEW;
    @Mutable @Final @Shadow public static FoodComponent POISONOUS_POTATO;
    @Mutable @Final @Shadow public static FoodComponent POTATO;
    @Mutable @Final @Shadow public static FoodComponent PUFFERFISH;
    @Mutable @Final @Shadow public static FoodComponent PUMPKIN_PIE;
    @Mutable @Final @Shadow public static FoodComponent RABBIT_STEW;
    @Mutable @Final @Shadow public static FoodComponent BEEF;
    @Mutable @Final @Shadow public static FoodComponent CHICKEN;
    @Mutable @Final @Shadow public static FoodComponent COD;
    @Mutable @Final @Shadow public static FoodComponent MUTTON;
    @Mutable @Final @Shadow public static FoodComponent PORKCHOP;
    @Mutable @Final @Shadow public static FoodComponent RABBIT;
    @Mutable @Final @Shadow public static FoodComponent SALMON;
    @Mutable @Final @Shadow public static FoodComponent ROTTEN_FLESH;
    @Mutable @Final @Shadow public static FoodComponent SPIDER_EYE;
    @Mutable @Final @Shadow public static FoodComponent COOKED_BEEF;
    @Mutable @Final @Shadow public static FoodComponent SUSPICIOUS_STEW;
    @Mutable @Final @Shadow public static FoodComponent SWEET_BERRIES;
    @Mutable @Final @Shadow public static FoodComponent TROPICAL_FISH;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void modifyFood(CallbackInfo ci) {
        APPLE = new FoodComponent.Builder().nutrition(4).saturationModifier(1.0F).build();
        BAKED_POTATO = new FoodComponent.Builder().nutrition(5).saturationModifier(1.2F).build();
        BEETROOT = new FoodComponent.Builder().nutrition(2).saturationModifier(1.0F).build();
        BEETROOT_SOUP = new FoodComponent.Builder().nutrition(8).saturationModifier(1.5F).build();
        BREAD = new FoodComponent.Builder().nutrition(4).saturationModifier(0.8F).build();
        CARROT = new FoodComponent.Builder().nutrition(3).saturationModifier(1.0F).build();
        CHORUS_FRUIT = new FoodComponent.Builder().nutrition(2).saturationModifier(1.0F).alwaysEdible().build();
        COOKED_CHICKEN = new FoodComponent.Builder().nutrition(6).saturationModifier(1.2F).build();
        COOKED_COD = new FoodComponent.Builder().nutrition(5).saturationModifier(1.2F).build();
        COOKED_MUTTON = new FoodComponent.Builder().nutrition(6).saturationModifier(1.2F).build();
        COOKED_PORKCHOP = new FoodComponent.Builder().nutrition(6).saturationModifier(1.4F).build();
        COOKED_RABBIT = new FoodComponent.Builder().nutrition(5).saturationModifier(1.2F).build();
        COOKED_SALMON = new FoodComponent.Builder().nutrition(6).saturationModifier(1.2F).build();
        COOKIE = new FoodComponent.Builder().nutrition(2).saturationModifier(0.2F).build();
        DRIED_KELP = new FoodComponent.Builder().nutrition(1).saturationModifier(1.2F).build();
        ENCHANTED_GOLDEN_APPLE = new FoodComponent.Builder().nutrition(8).saturationModifier(1.2F).alwaysEdible().build();
        GOLDEN_APPLE = new FoodComponent.Builder().nutrition(4).saturationModifier(1.2F).alwaysEdible().build();
        GLOW_BERRIES = new FoodComponent.Builder().nutrition(2).saturationModifier(1.2F).build();
        GOLDEN_CARROT = new FoodComponent.Builder().nutrition(6).saturationModifier(2.4F).build();
        HONEY_BOTTLE = new FoodComponent.Builder().nutrition(6).saturationModifier(0.2F).build();
        MELON_SLICE = new FoodComponent.Builder().nutrition(2).saturationModifier(1.0F).build();
        MUSHROOM_STEW = new FoodComponent.Builder().nutrition(8).saturationModifier(1.5F).build();
        POISONOUS_POTATO = new FoodComponent.Builder().nutrition(2).saturationModifier(0.0F).build();
        POTATO = new FoodComponent.Builder().nutrition(2).saturationModifier(1.0F).build();
        PUFFERFISH = new FoodComponent.Builder().nutrition(1).saturationModifier(0.2F).build();
        PUMPKIN_PIE = new FoodComponent.Builder().nutrition(8).saturationModifier(1.2F).build();
        RABBIT_STEW = new FoodComponent.Builder().nutrition(10).saturationModifier(1.2F).build();
        BEEF = new FoodComponent.Builder().nutrition(2).saturationModifier(0.6F).build();
        CHICKEN = new FoodComponent.Builder().nutrition(2).saturationModifier(0.6F).build();
        COD = new FoodComponent.Builder().nutrition(2).saturationModifier(0.6F).build();
        MUTTON = new FoodComponent.Builder().nutrition(2).saturationModifier(0.6F).build();
        PORKCHOP = new FoodComponent.Builder().nutrition(2).saturationModifier(0.6F).build();
        RABBIT = new FoodComponent.Builder().nutrition(2).saturationModifier(0.6F).build();
        SALMON = new FoodComponent.Builder().nutrition(2).saturationModifier(0.6F).build();
        ROTTEN_FLESH = new FoodComponent.Builder().nutrition(4).saturationModifier(0.0F).build();
        SPIDER_EYE = new FoodComponent.Builder().nutrition(2).saturationModifier(0.0F).build();
        COOKED_BEEF = new FoodComponent.Builder().nutrition(6).saturationModifier(1.2F).build();
        SUSPICIOUS_STEW = new FoodComponent.Builder().nutrition(8).saturationModifier(1.6F).alwaysEdible().build();
        SWEET_BERRIES = new FoodComponent.Builder().nutrition(2).saturationModifier(1.2F).build();
        TROPICAL_FISH = new FoodComponent.Builder().nutrition(2).saturationModifier(0.6F).build();
    }
}
