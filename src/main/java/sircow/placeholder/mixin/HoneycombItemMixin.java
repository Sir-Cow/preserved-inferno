package sircow.placeholder.mixin;

import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.HoneycombItem;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(HoneycombItem.class)
public abstract class HoneycombItemMixin {
    // make honeycomb edible
    @ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
    private static Item.Settings modifyItemSettings(Item.Settings value) {
        value.food(new FoodComponent.Builder().nutrition(6).saturationModifier(0.1F).build(), ConsumableComponent.builder().consumeSeconds(0.8F).build());
        return value;
    }
}
