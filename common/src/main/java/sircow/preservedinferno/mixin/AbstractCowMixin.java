package sircow.preservedinferno.mixin;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.cow.AbstractCow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.item.ModItems;

@Mixin(AbstractCow.class)
public class AbstractCowMixin {
    //               :3
//         \|/         (__)
//             `\------(oo)
//    \|/        ||    (__) - moo!
//               ||w--||     \|/
//           \|/
    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void pinferno$glassBottleMilk(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        AbstractCow cow = (AbstractCow) (Object) this;
        ItemStack stack = player.getItemInHand(hand);

        if (stack.is(Items.GLASS_BOTTLE) && !cow.isBaby()) {
            player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);

            ItemStack result = ItemUtils.createFilledResult(stack, player, new ItemStack(ModItems.MILK_BOTTLE));

            player.setItemInHand(hand, result);
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}
