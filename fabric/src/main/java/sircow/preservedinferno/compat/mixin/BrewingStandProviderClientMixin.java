package sircow.preservedinferno.compat.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import snownee.jade.addon.vanilla.BrewingStandProvider;

@Mixin(BrewingStandProvider.Client.class)
public class BrewingStandProviderClientMixin {
    @Redirect(method = "appendTooltip*", at = @At(value = "NEW", target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack preserved_inferno$replaceFuelItem(ItemLike item) {
        if (item == Items.BLAZE_POWDER) return new ItemStack(Items.NETHER_WART);
        return new ItemStack(item);
    }
}
