package sircow.preservedinferno.mixin;

import net.minecraft.world.level.levelgen.structure.structures.IglooPieces;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(IglooPieces.class)
public class IglooPiecesMixin {
    @ModifyConstant(method = "addPieces", constant = @Constant(doubleValue = 0.5))
    private static double pinferno$modifyIglooBasementSpawnChance(double original) {
        return 1.0;
    }
}
