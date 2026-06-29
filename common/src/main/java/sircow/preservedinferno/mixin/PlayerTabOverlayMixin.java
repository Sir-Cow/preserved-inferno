package sircow.preservedinferno.mixin;

import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.advancement.ModAdvancements;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin {
    @Inject(method = "getPlayerInfos", at = @At("RETURN"), cancellable = true)
    private void pinferno$modifyPlayerTabListSort(CallbackInfoReturnable<List<PlayerInfo>> cir) {
        List<PlayerInfo> sorted = new ArrayList<>(cir.getReturnValue());

        sorted.sort((a, b) -> {
            UUID uuidA = a.getProfile().id();
            UUID uuidB = b.getProfile().id();
            int pointsA = ModAdvancements.getPlayerPoints(uuidA);
            int pointsB = ModAdvancements.getPlayerPoints(uuidB);
            int compare = Integer.compare(pointsB, pointsA);

            if (compare != 0) return compare;

            return a.getProfile().name().compareToIgnoreCase(b.getProfile().name());
        });

        cir.setReturnValue(sorted);
    }
}
