package sircow.placeholder.mixin;

import net.minecraft.component.type.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.HoneycombItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(Items.class)
public abstract class ItemsMixin {
    // modify stack size of potions
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=potion")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item$Settings;maxCount(I)Lnet/minecraft/item/Item$Settings;", ordinal = 0))
    private static int modifyPotionStackSize(int old) { return 16; }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=splash_potion")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item$Settings;maxCount(I)Lnet/minecraft/item/Item$Settings;", ordinal = 0))
    private static int modifySplashPotionStackSize(int old) { return 16; }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=lingering_potion")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item$Settings;maxCount(I)Lnet/minecraft/item/Item$Settings;", ordinal = 0))
    private static int modifyLingeringPotionStackSize(int old) { return 16; }

    // modify stew/soup stack sizes
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=beetroot_soup")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item$Settings;maxCount(I)Lnet/minecraft/item/Item$Settings;", ordinal = 0))
    private static int modifyBeetrootSoupStackSize(int old) { return 16; }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=mushroom_stew")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item$Settings;maxCount(I)Lnet/minecraft/item/Item$Settings;", ordinal = 0))
    private static int modifyMushroomStewStackSize(int old) { return 16; }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=rabbit_stew")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item$Settings;maxCount(I)Lnet/minecraft/item/Item$Settings;", ordinal = 0))
    private static int modifyRabbitStewStackSize(int old) { return 16; }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=suspicious_stew")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item$Settings;maxCount(I)Lnet/minecraft/item/Item$Settings;", ordinal = 0))
    private static int modifySuspiciousStewStackSize(int old) { return 16; }

    // modify food (mainly to speed up eating time or modify status effects)
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=apple")), at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 0))
    private static Item.Settings modifyApple(Item.Settings original) {
        return original.food(FoodComponents.APPLE, ConsumableComponent.builder().consumeSeconds(0.8F).build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=beetroot")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 0))
    private static Item.Settings modifyBeetroot(Item.Settings original) {
        return original.food(FoodComponents.BEETROOT, ConsumableComponent.builder().consumeSeconds(0.8F).build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=carrot")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 0))
    private static Item.Settings modifyCarrot(Item.Settings original) {
        return original.food(FoodComponents.CARROT, ConsumableComponent.builder().consumeSeconds(0.8F).build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=chorus_fruit")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 0))
    private static Item.Settings modifyChorusFruit(Item.Settings original) {
        return original.food(FoodComponents.CHORUS_FRUIT, ConsumableComponent.builder().consumeSeconds(0.8F).build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=cookie")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 0))
    private static Item.Settings modifyCookie(Item.Settings original) {
        return original.food(FoodComponents.COOKIE, ConsumableComponent.builder().consumeSeconds(0.8F).build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=glow_berries")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 0))
    private static Item.Settings modifyGlowBerries(Item.Settings original) {
        return original.food(FoodComponents.GLOW_BERRIES, ConsumableComponent.builder().consumeSeconds(0.8F).build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=melon_slice")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 0))
    private static Item.Settings modifyMelonSlice(Item.Settings original) {
        return original.food(FoodComponents.MELON_SLICE, ConsumableComponent.builder().consumeSeconds(0.8F).build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=poisonous_potato")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 0))
    private static Item.Settings modifyPoisonousPotato(Item.Settings original) {
        return original.food(FoodComponents.POISONOUS_POTATO, ConsumableComponent.builder().consumeSeconds(0.8F).build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=potato")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 0))
    private static Item.Settings modifyPotato(Item.Settings original) {
        return original.food(FoodComponents.POTATO, ConsumableComponent.builder().consumeSeconds(0.8F).build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=beef")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 0))
    private static Item.Settings modifyBeef(Item.Settings original) {
        return original.food(FoodComponents.BEEF, ConsumableComponent.builder()
                .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.HUNGER, 300, 0), 0.3F))
                .build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=chicken")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 0))
    private static Item.Settings modifyChicken(Item.Settings original) {
        return original.food(FoodComponents.CHICKEN, ConsumableComponent.builder()
                .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.HUNGER, 300, 0), 0.3F))
                .build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=cod")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 0))
    private static Item.Settings modifyCod(Item.Settings original) {
        return original.food(FoodComponents.COD, ConsumableComponent.builder()
                .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.HUNGER, 300, 0), 0.3F))
                .build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=mutton")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 0))
    private static Item.Settings modifyMutton(Item.Settings original) {
        return original.food(FoodComponents.MUTTON, ConsumableComponent.builder()
                .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.HUNGER, 300, 0), 0.3F))
                .build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=porkchop")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 0))
    private static Item.Settings modifyPorkchop(Item.Settings original) {
        return original.food(FoodComponents.PORKCHOP, ConsumableComponent.builder()
                .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.HUNGER, 300, 0), 0.3F))
                .build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=rabbit")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 0))
    private static Item.Settings modifyRabbit(Item.Settings original) {
        return original.food(FoodComponents.RABBIT, ConsumableComponent.builder()
                .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.HUNGER, 300, 0), 0.3F))
                .build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=salmon")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 0))
    private static Item.Settings modifySalmon(Item.Settings original) {
        return original.food(FoodComponents.SALMON, ConsumableComponent.builder()
                .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.HUNGER, 300, 0), 0.3F))
                .build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=rotten_flesh")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 0))
    private static Item.Settings modifyRottenFlesh(Item.Settings original) {
        return original.food(FoodComponents.ROTTEN_FLESH, ConsumableComponent.builder()
                .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.HUNGER, 600, 0), 1.0F))
                .build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=spider_eye")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 0))
    private static Item.Settings modifySpiderEye(Item.Settings original) {
        return original.food(FoodComponents.SPIDER_EYE, ConsumableComponent.builder().consumeSeconds(0.8F).build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=sweet_berries")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 0))
    private static Item.Settings modifySweetBerries(Item.Settings original) {
        return original.food(FoodComponents.SWEET_BERRIES, ConsumableComponent.builder().consumeSeconds(0.8F).build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=porkchop")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 0))
    private static Item.Settings modifyTropicalFish(Item.Settings original) {
        return original.food(FoodComponents.TROPICAL_FISH, ConsumableComponent.builder()
                .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.HUNGER, 600, 0), 1.0F))
                .build());
    }

    // make honeycomb edible
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=honeycomb")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 0))
    private static Item.Settings modifyHoneycomb (Item.Settings settings) {
        return new HoneycombItem.Settings().food(new FoodComponent.Builder().nutrition(6).saturationModifier(0.1F).build(), ConsumableComponent.builder().consumeSeconds(0.8F).build());
    }
}
