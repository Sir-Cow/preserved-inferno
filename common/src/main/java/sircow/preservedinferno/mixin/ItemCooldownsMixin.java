package sircow.preservedinferno.mixin;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.enchantment.ModEnchantments;

@Mixin(ItemCooldowns.class)
public class ItemCooldownsMixin {
    @Inject(method = "getCooldownGroup", at = @At("HEAD"), cancellable = true)
    private void pinferno$blusteringCooldownGroup(ItemStack stack, CallbackInfoReturnable<Identifier> cir) {
        if (!(stack.getItem() instanceof MaceItem)) return;

        for (Object2IntMap.Entry<Holder<Enchantment>> entry : stack.getEnchantments().entrySet()) {
            if (entry.getKey().is(ModEnchantments.BLUSTERING)) {
                cir.setReturnValue(Constants.id("blustering_mace"));
                return;
            }
        }
    }
}
