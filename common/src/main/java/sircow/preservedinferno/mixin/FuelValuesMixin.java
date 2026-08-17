package sircow.preservedinferno.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import it.unimi.dsi.fastutil.objects.Object2IntSortedMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.FuelValues;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import sircow.preservedinferno.block.ModBlocks;
import sircow.preservedinferno.other.ModTags;

@Mixin(FuelValues.class)
public class FuelValuesMixin {
    @ModifyReturnValue(method = "vanillaBurnTimes(Lnet/minecraft/core/HolderLookup$Provider;Lnet/minecraft/world/flag/FeatureFlagSet;I)Lnet/minecraft/world/level/block/entity/FuelValues;", at = @At("RETURN"))
    private static FuelValues pinferno$modifyFuelValues(FuelValues original, HolderLookup.Provider registries, FeatureFlagSet enabledFeatures, int baseUnit) {
        FuelValuesAccessor accessor = (FuelValuesAccessor) original;
        Object2IntSortedMap<Item> values = accessor.getValues();

        record TagModifier(TagKey<Item> tag, float multiplier) {}
        TagModifier[] modifiers = new TagModifier[] {
                new TagModifier(ModTags.CLOTH, 1.0F),
                new TagModifier(ItemTags.LOGS_THAT_BURN, 2.0F),
                new TagModifier(ModTags.OVERWORLD_PLANKS, 0.5F),
                new TagModifier(ModTags.OVERWORLD_WOODEN_PRESSURE_PLATES, 1.0F),
                new TagModifier(ModTags.OVERWORLD_WOODEN_SLABS, 0.25F)
        };
        for (TagModifier modifier : modifiers) {
            registries.lookupOrThrow(Registries.ITEM).get(modifier.tag()).ifPresent(named -> {
                for (var holder : named) {
                    values.put(holder.value(), (int) (baseUnit * modifier.multiplier()));
                }
            });
        }

        values.put(Items.BLAZE_POWDER, baseUnit * 8);
        values.put(Items.BLAZE_ROD, baseUnit * 16);
        values.put(Items.CHARCOAL, baseUnit * 4);
        values.put(Items.LAVA_BUCKET, baseUnit * 32);
        values.put(ModBlocks.ANGLING_TABLE.asItem(), (int) (baseUnit * 1.5F));
        values.put(Blocks.COAL_BLOCK.asItem(), baseUnit * 8 * 5);
        values.put(Blocks.DRIED_KELP_BLOCK.asItem(), baseUnit * 8);
        values.put(Blocks.LEAF_LITTER.asItem(), (int) (baseUnit * 0.25F));
        values.put(Blocks.MANGROVE_ROOTS.asItem(), baseUnit * 2);
        values.put(ModBlocks.REINFORCED_ACACIA_DOOR.asItem(), baseUnit);
        values.put(ModBlocks.REINFORCED_BAMBOO_DOOR.asItem(), baseUnit);
        values.put(ModBlocks.REINFORCED_BIRCH_DOOR.asItem(), baseUnit);
        values.put(ModBlocks.REINFORCED_CHERRY_DOOR.asItem(), baseUnit);
        values.put(ModBlocks.REINFORCED_DARK_OAK_DOOR.asItem(), baseUnit);
        values.put(ModBlocks.REINFORCED_JUNGLE_DOOR.asItem(), baseUnit);
        values.put(ModBlocks.REINFORCED_MANGROVE_DOOR.asItem(), baseUnit);
        values.put(ModBlocks.REINFORCED_OAK_DOOR.asItem(), baseUnit);
        values.put(ModBlocks.REINFORCED_PALE_OAK_DOOR.asItem(), baseUnit);
        values.put(ModBlocks.REINFORCED_SPRUCE_DOOR.asItem(), baseUnit);

        return original;
    }
}
