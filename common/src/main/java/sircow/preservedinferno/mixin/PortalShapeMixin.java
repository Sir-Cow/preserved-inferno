package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CryingObsidianBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PortalShape.class)
public class PortalShapeMixin {
    @Shadow @Final @Mutable private static BlockBehaviour.StatePredicate FRAME;

    @Inject(method = "<clinit>", at = @At(value = "TAIL"))
    private static void preserved_inferno$changeCheck(CallbackInfo ci) {
        FRAME = (BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) ->
                blockState.is(Blocks.OBSIDIAN) || blockState.getBlock() instanceof CryingObsidianBlock;
    }
}
