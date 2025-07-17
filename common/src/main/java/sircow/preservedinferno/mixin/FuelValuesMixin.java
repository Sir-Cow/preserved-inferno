package sircow.preservedinferno.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import it.unimi.dsi.fastutil.objects.Object2IntSortedMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.FuelValues;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FuelValues.class)
public abstract class FuelValuesMixin {
    @ModifyReturnValue(method = "vanillaBurnTimes(Lnet/minecraft/core/HolderLookup$Provider;Lnet/minecraft/world/flag/FeatureFlagSet;I)Lnet/minecraft/world/level/block/entity/FuelValues;", at = @At("RETURN"))
    private static FuelValues preserved_inferno$modifyFuelValues(FuelValues original, HolderLookup.Provider registries, FeatureFlagSet enabledFeatures, int smeltingTime) {
        FuelValuesAccessor accessor = (FuelValuesAccessor) original;
        Object2IntSortedMap<Item> values = accessor.getValues();

        values.put(Blocks.COAL_BLOCK.asItem(), smeltingTime * 8 * 5);
        values.put(Items.BLAZE_POWDER, smeltingTime * 8);
        values.put(Items.BLAZE_ROD, smeltingTime * 16);

        return original;
    }
}
