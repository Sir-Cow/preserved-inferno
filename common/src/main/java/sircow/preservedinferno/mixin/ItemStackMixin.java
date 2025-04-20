package sircow.preservedinferno.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.other.HeatAccessor;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Unique private static final int HEAT_MODIFIER = 3;

    @Inject(method = "applyAfterUseComponentSideEffects", at = @At("HEAD"))
    private void preserved_inferno$drinkWaterReduceHeat(LivingEntity entity, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (!entity.level().isClientSide()) {
            PotionContents potioncontents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            if (entity instanceof Player && potioncontents.is(Potions.WATER)) {
                if (((HeatAccessor) entity).preserved_inferno$getHeat() >= HEAT_MODIFIER) {
                    ((HeatAccessor) entity).preserved_inferno$decreaseHeat(HEAT_MODIFIER);
                }
                else if (((HeatAccessor) entity).preserved_inferno$getHeat() < HEAT_MODIFIER && ((HeatAccessor) entity).preserved_inferno$getHeat() > 0) {
                    ((HeatAccessor) entity).preserved_inferno$setHeat(0);
                }
            }
        }
    }
}
