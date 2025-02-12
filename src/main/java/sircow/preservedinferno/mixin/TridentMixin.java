package sircow.preservedinferno.mixin;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.TridentItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TridentItem.class)
public class TridentMixin {
    // allow riptide to be used outside of rain or touching water while having conduit power effect
    @Redirect(method = {"onStoppedUsing", "use"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isTouchingWaterOrRain()Z"))
    private boolean replaceWaterCheck(PlayerEntity playerEntity) {
        if (playerEntity.hasStatusEffect(StatusEffects.CONDUIT_POWER) || playerEntity.isTouchingWaterOrRain()) {
            return true;
        }
        else if (!playerEntity.hasStatusEffect(StatusEffects.CONDUIT_POWER) && !playerEntity.isTouchingWaterOrRain()) {
            return false;
        }
        return false;
    }
}