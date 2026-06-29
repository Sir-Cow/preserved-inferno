package sircow.preservedinferno.mixin;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Consumable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(HoneycombItem.class)
public abstract class HoneycombItemMixin {
    // make honeycomb edible
    @ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true, name = "properties")
    private static Item.Properties pinferno$modifyItemSettings(Item.Properties properties) {
        properties.food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.6F).build(), Consumable.builder().consumeSeconds(0.8F).build());
        return properties;
    }
}
