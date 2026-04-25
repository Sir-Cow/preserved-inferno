package sircow.preservedinferno.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.item.ModItems;
import sircow.preservedinferno.trigger.ModTriggers;

@Mixin(FurnaceResultSlot.class)
public class FurnaceResultSlotMixin {
    @Shadow @Final private Player player;

    @Inject(method = "onTake", at = @At("HEAD"))
    private void preserved_inferno$onLeatherFabricTaken(Player player, ItemStack stack, CallbackInfo ci) {
        if (stack.is(ModItems.LEATHER_FABRIC)) {
            if (player instanceof ServerPlayer serverPlayer) ModTriggers.SMELT_LEATHER_FABRIC.get().trigger(serverPlayer);
        }
    }
    @Inject(method = "onQuickCraft", at = @At("HEAD"))
    private void preserved_inferno$onLeatherFabricTakenShifted(ItemStack stack, int count, CallbackInfo ci) {
        if (stack.is(ModItems.LEATHER_FABRIC)) {
            if (this.player instanceof ServerPlayer serverPlayer) ModTriggers.SMELT_LEATHER_FABRIC.get().trigger(serverPlayer);
        }
    }
}
