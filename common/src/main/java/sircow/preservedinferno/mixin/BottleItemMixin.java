package sircow.preservedinferno.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BottleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.item.ModItems;

@Mixin(BottleItem.class)
public class BottleItemMixin extends Item {
    public BottleItemMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getFluidState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/material/FluidState;", shift = At.Shift.AFTER), cancellable = true)
    private void pinferno$fillFromLava(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir, @Local(name = "hitResult") BlockHitResult hitResult, @Local(name = "itemStack") ItemStack itemStack) {
        BlockPos pos = hitResult.getBlockPos();

        if (level.getFluidState(pos).is(FluidTags.LAVA)) {
            level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
            level.gameEvent(player, GameEvent.FLUID_PICKUP, pos);
            cir.setReturnValue(InteractionResult.SUCCESS.heldItemTransformedTo(this.turnBottleIntoItem(itemStack, player, new ItemStack(ModItems.LAVA_BOTTLE.get()))));
        }
    }

    @Unique
    protected ItemStack turnBottleIntoItem(final ItemStack itemStack, final Player player, final ItemStack itemStackToTurnInto) {
        player.awardStat(Stats.ITEM_USED.get(this));
        return ItemUtils.createFilledResult(itemStack, player, itemStackToTurnInto);
    }
}
