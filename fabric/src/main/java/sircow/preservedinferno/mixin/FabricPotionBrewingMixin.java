package sircow.preservedinferno.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.item.ModItems;
import sircow.preservedinferno.potion.ModPotions;

@Mixin(PotionBrewing.class)
public class FabricPotionBrewingMixin {
    @Inject(method = "addVanillaMixes", at = @At("HEAD"), cancellable = true)
    private static void pinferno$addVanillaMixes(PotionBrewing.Builder builder, CallbackInfo ci) {
        builder.addContainer(Items.POTION);
        builder.addContainer(Items.SPLASH_POTION);
        builder.addContainer(Items.LINGERING_POTION);
        builder.addContainer(Items.HONEY_BOTTLE);
        builder.addContainer(ModItems.SPLASH_HONEY_BOTTLE.get());
        builder.addContainer(ModItems.LINGERING_HONEY_BOTTLE.get());
        builder.addContainer(ModItems.LAVA_BOTTLE.get());
        builder.addContainer(ModItems.SPLASH_LAVA_BOTTLE.get());
        builder.addContainer(ModItems.LINGERING_LAVA_BOTTLE.get());
        builder.addContainer(ModItems.MILK_BOTTLE.get());
        builder.addContainer(ModItems.SPLASH_MILK_BOTTLE.get());
        builder.addContainer(ModItems.LINGERING_MILK_BOTTLE.get());
        builder.addContainerRecipe(Items.POTION, Items.GUNPOWDER, Items.SPLASH_POTION);
        builder.addContainerRecipe(Items.POTION, Items.FIRE_CHARGE, Items.LINGERING_POTION);
        builder.addContainerRecipe(Items.HONEY_BOTTLE, Items.GUNPOWDER, ModItems.SPLASH_HONEY_BOTTLE.get());
        builder.addContainerRecipe(ModItems.SPLASH_HONEY_BOTTLE.get(), Items.FIRE_CHARGE, ModItems.LINGERING_HONEY_BOTTLE.get());
        builder.addContainerRecipe(ModItems.LAVA_BOTTLE.get(), Items.GUNPOWDER, ModItems.SPLASH_LAVA_BOTTLE.get());
        builder.addContainerRecipe(ModItems.SPLASH_LAVA_BOTTLE.get(), Items.FIRE_CHARGE, ModItems.LINGERING_LAVA_BOTTLE.get());
        builder.addContainerRecipe(ModItems.MILK_BOTTLE.get(), Items.GUNPOWDER, ModItems.SPLASH_MILK_BOTTLE.get());
        builder.addContainerRecipe(ModItems.SPLASH_MILK_BOTTLE.get(), Items.FIRE_CHARGE, ModItems.LINGERING_MILK_BOTTLE.get());
        builder.addMix(Potions.WATER, Items.AMETHYST_SHARD, ModPotions.hasteHolder());
        builder.addMix(ModPotions.hasteHolder(), Items.REDSTONE, ModPotions.longHasteHolder());
        builder.addMix(ModPotions.hasteHolder(), Items.GLOWSTONE_DUST, ModPotions.strongHasteHolder());
        builder.addMix(ModPotions.hasteHolder(), Items.FERMENTED_SPIDER_EYE, ModPotions.miningFatigueHolder());
        builder.addMix(ModPotions.miningFatigueHolder(), Items.REDSTONE, ModPotions.longMiningFatigueHolder());
        builder.addMix(ModPotions.miningFatigueHolder(), Items.GLOWSTONE_DUST, ModPotions.strongMiningFatigueHolder());
        builder.addMix(Potions.WATER, Items.INK_SAC, ModPotions.blindnessHolder());
        builder.addMix(ModPotions.blindnessHolder(), Items.REDSTONE, ModPotions.longBlindnessHolder());
        builder.addMix(Potions.WATER, Items.RABBIT_FOOT, ModPotions.luckHolder());
        builder.addMix(ModPotions.luckHolder(), Items.REDSTONE, ModPotions.longLuckHolder());
        builder.addMix(ModPotions.luckHolder(), Items.GLOWSTONE_DUST, ModPotions.strongLuckHolder());
        builder.addMix(Potions.WATER, Items.BREEZE_ROD, Potions.WIND_CHARGED);
        builder.addMix(Potions.WATER, Items.SLIME_BLOCK, Potions.OOZING);
        builder.addMix(Potions.WATER, Items.STONE, Potions.INFESTED);
        builder.addMix(Potions.WATER, Items.COBWEB, Potions.WEAVING);
        builder.addMix(Potions.WATER, Items.GOLDEN_CARROT, Potions.NIGHT_VISION);
        builder.addMix(Potions.NIGHT_VISION, Items.REDSTONE, Potions.LONG_NIGHT_VISION);
        builder.addMix(Potions.NIGHT_VISION, Items.FERMENTED_SPIDER_EYE, Potions.INVISIBILITY);
        builder.addMix(Potions.LONG_NIGHT_VISION, Items.FERMENTED_SPIDER_EYE, Potions.LONG_INVISIBILITY);
        builder.addMix(Potions.INVISIBILITY, Items.REDSTONE, Potions.LONG_INVISIBILITY);
        builder.addMix(Potions.WATER, Items.MAGMA_CREAM, Potions.FIRE_RESISTANCE);
        builder.addMix(Potions.FIRE_RESISTANCE, Items.REDSTONE, Potions.LONG_FIRE_RESISTANCE);
        builder.addMix(Potions.WATER, Items.RABBIT_HIDE, Potions.LEAPING);
        builder.addMix(Potions.LEAPING, Items.REDSTONE, Potions.LONG_LEAPING);
        builder.addMix(Potions.LEAPING, Items.GLOWSTONE_DUST, Potions.STRONG_LEAPING);
        builder.addMix(Potions.LEAPING, Items.FERMENTED_SPIDER_EYE, Potions.SLOWNESS);
        builder.addMix(Potions.LONG_LEAPING, Items.FERMENTED_SPIDER_EYE, Potions.LONG_SLOWNESS);
        builder.addMix(Potions.SLOWNESS, Items.REDSTONE, Potions.LONG_SLOWNESS);
        builder.addMix(Potions.SLOWNESS, Items.GLOWSTONE_DUST, Potions.STRONG_SLOWNESS);
        builder.addMix(Potions.WATER, Items.TURTLE_SCUTE, Potions.TURTLE_MASTER);
        builder.addMix(Potions.TURTLE_MASTER, Items.REDSTONE, Potions.LONG_TURTLE_MASTER);
        builder.addMix(Potions.TURTLE_MASTER, Items.GLOWSTONE_DUST, Potions.STRONG_TURTLE_MASTER);
        builder.addMix(Potions.SWIFTNESS, Items.FERMENTED_SPIDER_EYE, Potions.SLOWNESS);
        builder.addMix(Potions.LONG_SWIFTNESS, Items.FERMENTED_SPIDER_EYE, Potions.LONG_SLOWNESS);
        builder.addMix(Potions.WATER, Items.SUGAR, Potions.SWIFTNESS);
        builder.addMix(Potions.SWIFTNESS, Items.REDSTONE, Potions.LONG_SWIFTNESS);
        builder.addMix(Potions.SWIFTNESS, Items.GLOWSTONE_DUST, Potions.STRONG_SWIFTNESS);
        builder.addMix(Potions.WATER, Items.PUFFERFISH, Potions.WATER_BREATHING);
        builder.addMix(Potions.WATER_BREATHING, Items.REDSTONE, Potions.LONG_WATER_BREATHING);
        builder.addMix(Potions.WATER, Items.GLISTERING_MELON_SLICE, Potions.HEALING);
        builder.addMix(Potions.HEALING, Items.GLOWSTONE_DUST, Potions.STRONG_HEALING);
        builder.addMix(Potions.HEALING, Items.FERMENTED_SPIDER_EYE, Potions.HARMING);
        builder.addMix(Potions.STRONG_HEALING, Items.FERMENTED_SPIDER_EYE, Potions.STRONG_HARMING);
        builder.addMix(Potions.HARMING, Items.GLOWSTONE_DUST, Potions.STRONG_HARMING);
        builder.addMix(Potions.POISON, Items.FERMENTED_SPIDER_EYE, Potions.HARMING);
        builder.addMix(Potions.LONG_POISON, Items.FERMENTED_SPIDER_EYE, Potions.HARMING);
        builder.addMix(Potions.STRONG_POISON, Items.FERMENTED_SPIDER_EYE, Potions.STRONG_HARMING);
        builder.addMix(Potions.WATER, Items.SPIDER_EYE, Potions.POISON);
        builder.addMix(Potions.POISON, Items.REDSTONE, Potions.LONG_POISON);
        builder.addMix(Potions.POISON, Items.GLOWSTONE_DUST, Potions.STRONG_POISON);
        builder.addMix(Potions.WATER, Items.GHAST_TEAR, Potions.REGENERATION);
        builder.addMix(Potions.REGENERATION, Items.REDSTONE, Potions.LONG_REGENERATION);
        builder.addMix(Potions.REGENERATION, Items.GLOWSTONE_DUST, Potions.STRONG_REGENERATION);
        builder.addMix(Potions.WATER, Items.BLAZE_POWDER, Potions.STRENGTH);
        builder.addMix(Potions.STRENGTH, Items.REDSTONE, Potions.LONG_STRENGTH);
        builder.addMix(Potions.STRENGTH, Items.GLOWSTONE_DUST, Potions.STRONG_STRENGTH);
        builder.addMix(Potions.STRENGTH, Items.FERMENTED_SPIDER_EYE, Potions.WEAKNESS);
        builder.addMix(Potions.WEAKNESS, Items.REDSTONE, Potions.LONG_WEAKNESS);
        builder.addMix(Potions.WATER, ModItems.PHANTOM_SINEW.get(), Potions.SLOW_FALLING);
        builder.addMix(Potions.SLOW_FALLING, Items.REDSTONE, Potions.LONG_SLOW_FALLING);
        builder.addMix(Potions.WATER, Items.NAUTILUS_SHELL, ModPotions.nautilusBlessingHolder());
        ci.cancel();
    }

    @Inject(method = "mix", at = @At("HEAD"), cancellable = true)
    private void pinferno$mixCustomContainers(ItemStack ingredient, ItemStack source, CallbackInfoReturnable<ItemStack> cir) {
        if (source.isEmpty()) return;

        ItemStack result = null;

        if (ingredient.is(Items.GUNPOWDER)) {
            if (source.is(Items.HONEY_BOTTLE)) result = new ItemStack(ModItems.SPLASH_HONEY_BOTTLE.get());
            else if (source.is(ModItems.LAVA_BOTTLE.get())) result = new ItemStack(ModItems.SPLASH_LAVA_BOTTLE.get());
            else if (source.is(ModItems.MILK_BOTTLE.get())) result = new ItemStack(ModItems.SPLASH_MILK_BOTTLE.get());
        }

        else if (ingredient.is(Items.FIRE_CHARGE)) {
            if (source.is(ModItems.SPLASH_HONEY_BOTTLE.get())) result = new ItemStack(ModItems.LINGERING_HONEY_BOTTLE.get());
            else if (source.is(ModItems.SPLASH_LAVA_BOTTLE.get())) result = new ItemStack(ModItems.LINGERING_LAVA_BOTTLE.get());
            else if (source.is(ModItems.SPLASH_MILK_BOTTLE.get())) result = new ItemStack(ModItems.LINGERING_MILK_BOTTLE.get());
        }

        if (result != null) {
            result.applyComponents(source.getComponentsPatch());
            cir.setReturnValue(result);
        }
    }
}
