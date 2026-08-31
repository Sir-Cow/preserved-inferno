package sircow.preservedinferno.mixin;

import net.minecraft.world.inventory.CrafterMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.component.ModComponents;
import sircow.preservedinferno.tag.ModTags;

@Mixin(CrafterMenu.class)
public class CrafterMenuMixin {
    @Shadow @Final private ResultContainer resultContainer;

    @Inject(method = "refreshRecipeResult", at = @At("TAIL"))
    private void pinferno$invalidateExhaustedTrimRecipes(CallbackInfo ci) {
        ItemStack result = this.resultContainer.getItem(0);

        if (result.is(ModTags.ARMOR_TRIM_TEMPLATES) && hasExhaustedTemplateInRecipe()) this.resultContainer.setItem(0, ItemStack.EMPTY);
    }

    @Unique
    private boolean hasExhaustedTemplateInRecipe() {
        CrafterMenu self = (CrafterMenu)(Object)this;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = self.getContainer().getItem(i);

            if (!stack.isEmpty() && stack.is(ModTags.ARMOR_TRIM_TEMPLATES) && Boolean.TRUE.equals(stack.get(ModComponents.EXHAUSTED_TEMPLATE))) return false;
        }
        return true;
    }
}
