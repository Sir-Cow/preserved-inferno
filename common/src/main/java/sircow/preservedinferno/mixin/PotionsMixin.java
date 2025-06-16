package sircow.preservedinferno.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(Potions.class)
public class PotionsMixin {
    @ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 900))
    private static int preserved_inferno$modifyIntValue5(int original) {
        return 600;
    }
    @ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 3600))
    private static int preserved_inferno$modifyIntValue(int original) {
        return 6000;
    }
    @ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 4800))
    private static int preserved_inferno$modifyIntValue4(int original) {
        return 12000;
    }
    @ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 9600))
    private static int preserved_inferno$modifyIntValue3(int original) {
        return 12000;
    }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=slowness")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/alchemy/Potions;register(Ljava/lang/String;Lnet/minecraft/world/item/alchemy/Potion;)Lnet/minecraft/core/Holder;", ordinal = 0), index = 1)
    private static Potion preserved_inferno$modifySlowness(Potion potion) {
        return new Potion("slowness", new MobEffectInstance(MobEffects.SLOWNESS, 6000));
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=weakness")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/alchemy/Potions;register(Ljava/lang/String;Lnet/minecraft/world/item/alchemy/Potion;)Lnet/minecraft/core/Holder;", ordinal = 0), index = 1)
    private static Potion preserved_inferno$modifyWeakness(Potion potion) {
        return new Potion("weakness", new MobEffectInstance(MobEffects.WEAKNESS, 6000));
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=slow_falling")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/alchemy/Potions;register(Ljava/lang/String;Lnet/minecraft/world/item/alchemy/Potion;)Lnet/minecraft/core/Holder;", ordinal = 0), index = 1)
    private static Potion preserved_inferno$modifySlowFalling(Potion potion) {
        return new Potion("slow_falling", new MobEffectInstance(MobEffects.SLOW_FALLING, 6000));
    }
    // strong
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=strong_swiftness")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/alchemy/Potions;register(Ljava/lang/String;Lnet/minecraft/world/item/alchemy/Potion;)Lnet/minecraft/core/Holder;", ordinal = 0), index = 1)
    private static Potion preserved_inferno$modifyStrongSwiftness(Potion potion) {
        return new Potion("swiftness", new MobEffectInstance(MobEffects.SPEED, 6000, 1));
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=strong_leaping")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/alchemy/Potions;register(Ljava/lang/String;Lnet/minecraft/world/item/alchemy/Potion;)Lnet/minecraft/core/Holder;", ordinal = 0), index = 1)
    private static Potion preserved_inferno$modifyStrongLeaping(Potion potion) {
        return new Potion("leaping", new MobEffectInstance(MobEffects.JUMP_BOOST, 6000, 1));
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=strong_poison")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/alchemy/Potions;register(Ljava/lang/String;Lnet/minecraft/world/item/alchemy/Potion;)Lnet/minecraft/core/Holder;", ordinal = 0), index = 1)
    private static Potion preserved_inferno$modifyStrongPoison(Potion potion) {
        return new Potion("poison", new MobEffectInstance(MobEffects.POISON, 600, 1));
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=strong_slowness")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/alchemy/Potions;register(Ljava/lang/String;Lnet/minecraft/world/item/alchemy/Potion;)Lnet/minecraft/core/Holder;", ordinal = 0), index = 1)
    private static Potion preserved_inferno$modifyStrongSlowness(Potion potion) {
        return new Potion("slowness", new MobEffectInstance(MobEffects.SLOWNESS, 6000, 3));
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=strong_regeneration")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/alchemy/Potions;register(Ljava/lang/String;Lnet/minecraft/world/item/alchemy/Potion;)Lnet/minecraft/core/Holder;", ordinal = 0), index = 1)
    private static Potion preserved_inferno$modifyStrongRegeneration(Potion potion) {
        return new Potion("regeneration", new MobEffectInstance(MobEffects.REGENERATION, 600, 1));
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=strong_strength")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/alchemy/Potions;register(Ljava/lang/String;Lnet/minecraft/world/item/alchemy/Potion;)Lnet/minecraft/core/Holder;", ordinal = 0), index = 1)
    private static Potion preserved_inferno$modifyStrongStrength(Potion potion) {
        return new Potion("strength", new MobEffectInstance(MobEffects.STRENGTH, 600, 1));
    }
    // long
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=long_regeneration")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/alchemy/Potions;register(Ljava/lang/String;Lnet/minecraft/world/item/alchemy/Potion;)Lnet/minecraft/core/Holder;", ordinal = 0), index = 1)
    private static Potion preserved_inferno$modifyLongRegeneration(Potion potion) {
        return new Potion("regeneration", new MobEffectInstance(MobEffects.REGENERATION, 1200));
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=long_poison")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/alchemy/Potions;register(Ljava/lang/String;Lnet/minecraft/world/item/alchemy/Potion;)Lnet/minecraft/core/Holder;", ordinal = 0), index = 1)
    private static Potion preserved_inferno$modifyLongPoison(Potion potion) {
        return new Potion("poison", new MobEffectInstance(MobEffects.POISON, 1200));
    }
}
