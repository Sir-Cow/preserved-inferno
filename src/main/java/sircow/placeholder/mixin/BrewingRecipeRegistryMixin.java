package sircow.placeholder.mixin;

import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.placeholder.item.ModItems;

@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipeRegistryMixin {
    @Inject(method = "registerDefaults", at = @At("HEAD"), cancellable = true)
    private static void registerDefaults(BrewingRecipeRegistry.Builder builder, CallbackInfo ci) {
        builder.registerPotionType(Items.POTION);
        builder.registerPotionType(Items.SPLASH_POTION);
        builder.registerPotionType(Items.LINGERING_POTION);
        builder.registerItemRecipe(Items.POTION, Items.GUNPOWDER, Items.SPLASH_POTION);
        builder.registerItemRecipe(Items.SPLASH_POTION, Items.DRAGON_BREATH, Items.LINGERING_POTION);
        builder.registerPotionRecipe(Potions.WATER, Items.BREEZE_ROD, Potions.WIND_CHARGED);
        builder.registerPotionRecipe(Potions.WATER, Items.SLIME_BLOCK, Potions.OOZING);
        builder.registerPotionRecipe(Potions.WATER, Items.STONE, Potions.INFESTED);
        builder.registerPotionRecipe(Potions.WATER, Items.COBWEB, Potions.WEAVING);
        builder.registerPotionRecipe(Potions.WATER, Items.GOLDEN_CARROT, Potions.NIGHT_VISION);
        builder.registerPotionRecipe(Potions.NIGHT_VISION, Items.REDSTONE, Potions.LONG_NIGHT_VISION);
        builder.registerPotionRecipe(Potions.NIGHT_VISION, Items.FERMENTED_SPIDER_EYE, Potions.INVISIBILITY);
        builder.registerPotionRecipe(Potions.LONG_NIGHT_VISION, Items.FERMENTED_SPIDER_EYE, Potions.LONG_INVISIBILITY);
        builder.registerPotionRecipe(Potions.INVISIBILITY, Items.REDSTONE, Potions.LONG_INVISIBILITY);
        builder.registerPotionRecipe(Potions.WATER, Items.MAGMA_CREAM, Potions.FIRE_RESISTANCE);
        builder.registerPotionRecipe(Potions.FIRE_RESISTANCE, Items.REDSTONE, Potions.LONG_FIRE_RESISTANCE);
        builder.registerPotionRecipe(Potions.WATER, Items.RABBIT_FOOT, Potions.LEAPING);
        builder.registerPotionRecipe(Potions.LEAPING, Items.REDSTONE, Potions.LONG_LEAPING);
        builder.registerPotionRecipe(Potions.LEAPING, Items.GLOWSTONE_DUST, Potions.STRONG_LEAPING);
        builder.registerPotionRecipe(Potions.LEAPING, Items.FERMENTED_SPIDER_EYE, Potions.SLOWNESS);
        builder.registerPotionRecipe(Potions.LONG_LEAPING, Items.FERMENTED_SPIDER_EYE, Potions.LONG_SLOWNESS);
        builder.registerPotionRecipe(Potions.SLOWNESS, Items.REDSTONE, Potions.LONG_SLOWNESS);
        builder.registerPotionRecipe(Potions.SLOWNESS, Items.GLOWSTONE_DUST, Potions.STRONG_SLOWNESS);
        builder.registerPotionRecipe(Potions.WATER, Items.TURTLE_HELMET, Potions.TURTLE_MASTER);
        builder.registerPotionRecipe(Potions.TURTLE_MASTER, Items.REDSTONE, Potions.LONG_TURTLE_MASTER);
        builder.registerPotionRecipe(Potions.TURTLE_MASTER, Items.GLOWSTONE_DUST, Potions.STRONG_TURTLE_MASTER);
        builder.registerPotionRecipe(Potions.SWIFTNESS, Items.FERMENTED_SPIDER_EYE, Potions.SLOWNESS);
        builder.registerPotionRecipe(Potions.LONG_SWIFTNESS, Items.FERMENTED_SPIDER_EYE, Potions.LONG_SLOWNESS);
        builder.registerPotionRecipe(Potions.WATER, Items.SUGAR, Potions.SWIFTNESS);
        builder.registerPotionRecipe(Potions.SWIFTNESS, Items.REDSTONE, Potions.LONG_SWIFTNESS);
        builder.registerPotionRecipe(Potions.SWIFTNESS, Items.GLOWSTONE_DUST, Potions.STRONG_SWIFTNESS);
        builder.registerPotionRecipe(Potions.WATER, Items.PUFFERFISH, Potions.WATER_BREATHING);
        builder.registerPotionRecipe(Potions.WATER_BREATHING, Items.REDSTONE, Potions.LONG_WATER_BREATHING);
        builder.registerPotionRecipe(Potions.WATER, Items.GLISTERING_MELON_SLICE, Potions.HEALING);
        builder.registerPotionRecipe(Potions.HEALING, Items.GLOWSTONE_DUST, Potions.STRONG_HEALING);
        builder.registerPotionRecipe(Potions.HEALING, Items.FERMENTED_SPIDER_EYE, Potions.HARMING);
        builder.registerPotionRecipe(Potions.STRONG_HEALING, Items.FERMENTED_SPIDER_EYE, Potions.STRONG_HARMING);
        builder.registerPotionRecipe(Potions.HARMING, Items.GLOWSTONE_DUST, Potions.STRONG_HARMING);
        builder.registerPotionRecipe(Potions.POISON, Items.FERMENTED_SPIDER_EYE, Potions.HARMING);
        builder.registerPotionRecipe(Potions.LONG_POISON, Items.FERMENTED_SPIDER_EYE, Potions.HARMING);
        builder.registerPotionRecipe(Potions.STRONG_POISON, Items.FERMENTED_SPIDER_EYE, Potions.STRONG_HARMING);
        builder.registerPotionRecipe(Potions.WATER, Items.SPIDER_EYE, Potions.POISON);
        builder.registerPotionRecipe(Potions.POISON, Items.REDSTONE, Potions.LONG_POISON);
        builder.registerPotionRecipe(Potions.POISON, Items.GLOWSTONE_DUST, Potions.STRONG_POISON);
        builder.registerPotionRecipe(Potions.WATER, Items.GHAST_TEAR, Potions.REGENERATION);
        builder.registerPotionRecipe(Potions.REGENERATION, Items.REDSTONE, Potions.LONG_REGENERATION);
        builder.registerPotionRecipe(Potions.REGENERATION, Items.GLOWSTONE_DUST, Potions.STRONG_REGENERATION);
        builder.registerPotionRecipe(Potions.WATER, Items.BLAZE_POWDER, Potions.STRENGTH);
        builder.registerPotionRecipe(Potions.STRENGTH, Items.REDSTONE, Potions.LONG_STRENGTH);
        builder.registerPotionRecipe(Potions.STRENGTH, Items.GLOWSTONE_DUST, Potions.STRONG_STRENGTH);
        builder.registerPotionRecipe(Potions.WATER, Items.FERMENTED_SPIDER_EYE, Potions.WEAKNESS);
        builder.registerPotionRecipe(Potions.WEAKNESS, Items.REDSTONE, Potions.LONG_WEAKNESS);
        builder.registerPotionRecipe(Potions.WATER, ModItems.PHANTOM_SINEW, Potions.SLOW_FALLING);
        builder.registerPotionRecipe(Potions.SLOW_FALLING, Items.REDSTONE, Potions.LONG_SLOW_FALLING);
        ci.cancel();
    }
}
