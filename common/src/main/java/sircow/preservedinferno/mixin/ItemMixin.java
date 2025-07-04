package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(method = "mineBlock", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$modifyToolDamage(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity, CallbackInfoReturnable<Boolean> cir) {
        if (level.isClientSide()) return;

        Tool tool = stack.get(DataComponents.TOOL);
        if (tool == null) return;

        if (stack.is(ItemTags.SWORDS)) return;

        float hardness = state.getDestroySpeed(level, pos);
        if (hardness == 0.0f) return;

        int damage;
        if (hardness <= 1.0f) {
            damage = (int) Math.ceil(hardness);
        }
        else {
            damage = (int) Math.ceil(hardness / 3.0f);
        }

        damage = Math.min(damage, 5);
        stack.hurtAndBreak(damage, miningEntity, EquipmentSlot.MAINHAND);
        cir.setReturnValue(true);
    }
}
