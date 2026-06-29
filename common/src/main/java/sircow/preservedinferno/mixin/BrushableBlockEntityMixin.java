package sircow.preservedinferno.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.trigger.ModTriggers;

@Mixin(BrushableBlockEntity.class)
public class BrushableBlockEntityMixin {
    @Inject(method = "brushingCompleted", at = @At("TAIL"))
    private void pinferno$onBrushingCompleted(ServerLevel level, LivingEntity user, ItemStack brush, CallbackInfo ci) {
        if (user instanceof Player player) {
            if (player instanceof ServerPlayer serverPlayer) ModTriggers.BRUSH_BLOCK.get().trigger(serverPlayer);
        }
    }
}
