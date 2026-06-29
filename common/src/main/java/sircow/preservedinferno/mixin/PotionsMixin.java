package sircow.preservedinferno.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(Potions.class)
public class PotionsMixin {
    @ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 900))
    private static int pinferno$modifyIntValue5(int original) {
        return 600;
    }
    @ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 3600))
    private static int pinferno$modifyIntValue(int original) {
        return 6000;
    }
    @ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 4800))
    private static int pinferno$modifyIntValue4(int original) {
        return 12000;
    }
    @ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 9600))
    private static int pinferno$modifyIntValue3(int original) {
        return 12000;
    }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=slowness")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/alchemy/Potions;register(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/alchemy/Potion;)Lnet/minecraft/core/Holder;", ordinal = 0), index = 1)
    private static Potion pinferno$modifySlowness(Potion potion) {
        return new Potion("slowness", new MobEffectInstance(MobEffects.SLOWNESS, 6000));
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=weakness")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/alchemy/Potions;register(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/alchemy/Potion;)Lnet/minecraft/core/Holder;", ordinal = 0), index = 1)
    private static Potion pinferno$modifyWeakness(Potion potion) {
        return new Potion("weakness", new MobEffectInstance(MobEffects.WEAKNESS, 6000));
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=slow_falling")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/alchemy/Potions;register(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/alchemy/Potion;)Lnet/minecraft/core/Holder;", ordinal = 0), index = 1)
    private static Potion pinferno$modifySlowFalling(Potion potion) {
        return new Potion("slow_falling", new MobEffectInstance(MobEffects.SLOW_FALLING, 6000));
    }
    // strong
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/world/item/alchemy/PotionIds;STRONG_SWIFTNESS:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/alchemy/Potions;register(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/alchemy/Potion;)Lnet/minecraft/core/Holder;", ordinal = 0), index = 1)
    private static Potion pinferno$modifyStrongSwiftness(Potion potion) {
        return new Potion("swiftness", new MobEffectInstance(MobEffects.SPEED, 6000, 1));
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/world/item/alchemy/PotionIds;STRONG_LEAPING:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/alchemy/Potions;register(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/alchemy/Potion;)Lnet/minecraft/core/Holder;", ordinal = 0), index = 1)
    private static Potion pinferno$modifyStrongLeaping(Potion potion) {
        return new Potion("leaping", new MobEffectInstance(MobEffects.JUMP_BOOST, 6000, 1));
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/world/item/alchemy/PotionIds;STRONG_POISON:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/alchemy/Potions;register(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/alchemy/Potion;)Lnet/minecraft/core/Holder;", ordinal = 0), index = 1)
    private static Potion pinferno$modifyStrongPoison(Potion potion) {
        return new Potion("poison", new MobEffectInstance(MobEffects.POISON, 600, 1));
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/world/item/alchemy/PotionIds;STRONG_SLOWNESS:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/alchemy/Potions;register(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/alchemy/Potion;)Lnet/minecraft/core/Holder;", ordinal = 0), index = 1)
    private static Potion pinferno$modifyStrongSlowness(Potion potion) {
        return new Potion("slowness", new MobEffectInstance(MobEffects.SLOWNESS, 6000, 1));
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/world/item/alchemy/PotionIds;STRONG_REGENERATION:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/alchemy/Potions;register(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/alchemy/Potion;)Lnet/minecraft/core/Holder;", ordinal = 0), index = 1)
    private static Potion pinferno$modifyStrongRegeneration(Potion potion) {
        return new Potion("regeneration", new MobEffectInstance(MobEffects.REGENERATION, 600, 1));
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/world/item/alchemy/PotionIds;STRONG_STRENGTH:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/alchemy/Potions;register(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/alchemy/Potion;)Lnet/minecraft/core/Holder;", ordinal = 0), index = 1)
    private static Potion pinferno$modifyStrongStrength(Potion potion) {
        return new Potion("strength", new MobEffectInstance(MobEffects.STRENGTH, 6000, 1));
    }
    // long
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/world/item/alchemy/PotionIds;LONG_REGENERATION:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/alchemy/Potions;register(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/alchemy/Potion;)Lnet/minecraft/core/Holder;", ordinal = 0), index = 1)
    private static Potion pinferno$modifyLongRegeneration(Potion potion) {
        return new Potion("regeneration", new MobEffectInstance(MobEffects.REGENERATION, 1200));
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/world/item/alchemy/PotionIds;LONG_POISON:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/alchemy/Potions;register(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/alchemy/Potion;)Lnet/minecraft/core/Holder;", ordinal = 0), index = 1)
    private static Potion pinferno$modifyLongPoison(Potion potion) {
        return new Potion("poison", new MobEffectInstance(MobEffects.POISON, 1200));
    }
}
