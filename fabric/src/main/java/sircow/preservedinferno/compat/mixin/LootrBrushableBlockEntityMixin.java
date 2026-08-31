package sircow.preservedinferno.compat.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import noobanidus.mods.lootr.common.block.entity.LootrBrushableBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.trigger.ModTriggers;

@Mixin(LootrBrushableBlockEntity.class)
public class LootrBrushableBlockEntityMixin {
    @Inject(method = "brushingCompleted", at = @At("TAIL"))
    private void pinferno$onBrushingCompleted(Player player, CallbackInfo ci) {
        if (player instanceof ServerPlayer serverPlayer) ModTriggers.BRUSH_BLOCK.trigger(serverPlayer);
    }
}
