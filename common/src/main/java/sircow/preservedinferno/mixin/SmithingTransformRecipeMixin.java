package sircow.preservedinferno.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SmithingTransformRecipe.class)
public class SmithingTransformRecipeMixin {
    @Inject(method = "assemble(Lnet/minecraft/world/item/crafting/SmithingRecipeInput;)Lnet/minecraft/world/item/ItemStack;", at = @At("RETURN"), cancellable = true)
    private void pinferno$applyDiamondPrefix(SmithingRecipeInput input, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack result = cir.getReturnValue();
        CustomData customData = result.get(DataComponents.CUSTOM_DATA);
        if (customData == null) return;

        String dataStr = customData.toString();
        int prefixColour = 0;
        boolean hasUpgrade = false;

        if (dataStr.contains("upgraded_nether_alloy")) {
            prefixColour = 0xF3B6B6;
            hasUpgrade = true;
        }
        else if (dataStr.contains("upgraded_echoing_prism")) {
            prefixColour = 0x009295;
            hasUpgrade = true;
        }

        if (!hasUpgrade) return;

        Component originalName = result.has(DataComponents.CUSTOM_NAME) ? result.get(DataComponents.CUSTOM_NAME) : result.getHoverName();

        if (originalName != null) {
            if (originalName.getString().contains("♦")) return;

            MutableComponent colouredPrefix = Component.literal("♦ ").withColor(prefixColour);
            MutableComponent combinedText = Component.empty().append(colouredPrefix).append(originalName);
            MutableComponent finalName = combinedText.withStyle(combinedText.getStyle().withItalic(originalName.getStyle().isItalic()));
            result.set(DataComponents.CUSTOM_NAME, finalName);
        }

        cir.setReturnValue(result);
    }
}
