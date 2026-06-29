package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.npc.villager.VillagerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(VillagerData.class)
public class VillagerDataMixin {
    @ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 70))
    private static int pinferno$replaceJourneymanXp(int original) {
        return 50;
    }

    @ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 150))
    private static int pinferno$replaceExpertXp(int original) {
        return 120;
    }
}
