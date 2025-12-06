package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.scores.Team;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.other.WorldDataManager;

@Mixin(TamableAnimal.class)
public abstract class TamableAnimalMixin {
    @Inject(method = "getTeam", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$preventRankPrefix(CallbackInfoReturnable<Team> cir) {
        TamableAnimal entity = (TamableAnimal) (Object) this;

        if (!entity.isTame() || !entity.hasCustomName()) {
            return;
        }

        Team returnedTeam = cir.getReturnValue();

        if (returnedTeam != null) {
            String teamName = returnedTeam.getName();

            if (WorldDataManager.RANK_PREFIXES.containsKey(teamName)) {
                cir.setReturnValue(null);
            }
        }

        if (entity.isTame() && entity.hasCustomName()) {
            cir.setReturnValue(null);
            cir.cancel();
        }
    }
}
