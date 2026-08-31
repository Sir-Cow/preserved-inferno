package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.CrafterBlock;
import net.minecraft.world.level.block.entity.CrafterBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.component.ModComponents;
import sircow.preservedinferno.tag.ModTags;

import java.util.Optional;

@Mixin(CrafterBlock.class)
public class CrafterBlockMixin {
    @Inject(method = "dispenseFrom", at = @At("HEAD"), cancellable = true)
    private void pinferno$blockExhaustedTrimRecipes(BlockState state, ServerLevel level, BlockPos pos, CallbackInfo ci) {
        if (!(level.getBlockEntity(pos) instanceof CrafterBlockEntity crafter)) return;

        CraftingInput input = crafter.asCraftInput();
        Optional<RecipeHolder<CraftingRecipe>> recipe = CrafterBlock.getPotentialResults(level, input);
        if (recipe.isEmpty()) return;

        ItemStack result = recipe.get().value().assemble(input);

        if (!result.isEmpty() && result.is(ModTags.ARMOR_TRIM_TEMPLATES) && containsExhaustedTemplate(crafter)) {
            ci.cancel();
        }
    }

    @Unique
    private boolean containsExhaustedTemplate(CrafterBlockEntity crafter) {
        for (ItemStack stack : crafter.getItems()) {
            if (!stack.isEmpty() && stack.is(ModTags.ARMOR_TRIM_TEMPLATES) && Boolean.TRUE.equals(stack.get(ModComponents.EXHAUSTED_TEMPLATE))) return false;
        }
        return true;
    }
}
