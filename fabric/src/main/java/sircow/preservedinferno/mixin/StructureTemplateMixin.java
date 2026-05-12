package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(StructureTemplate.class)
public class StructureTemplateMixin {
    @Inject(method = "processBlockInfos", at = @At("RETURN"), cancellable = true)
    private static void preserved_inferno$replaceCauldronsOnStructureGen(ServerLevelAccessor level, BlockPos position, BlockPos referencePos, StructurePlaceSettings settings, List<StructureTemplate.StructureBlockInfo> blockInfoList, CallbackInfoReturnable<List<StructureTemplate.StructureBlockInfo>> cir) {
        List<StructureTemplate.StructureBlockInfo> result = new ArrayList<>(cir.getReturnValue());

        for (int i = 0; i < result.size(); i++) {
            StructureTemplate.StructureBlockInfo info = result.get(i);

            if (info.state().getBlock() instanceof LayeredCauldronBlock) {
                int levelValue = info.state().getValue(LayeredCauldronBlock.LEVEL);
                int waterProgress = switch (levelValue) {
                    case 1 -> 21;
                    case 2 -> 43;
                    case 3 -> 64;
                    default -> 0;
                };

                CompoundTag nbt = info.nbt() != null ? info.nbt().copy() : new CompoundTag();
                nbt.putInt("CauldronWaterProgress", waterProgress);
                nbt.putInt("CauldronMaxWaterProgress", 64);
                result.set(i, new StructureTemplate.StructureBlockInfo(info.pos(), Blocks.CAULDRON.defaultBlockState(), nbt));
            }
        }
        cir.setReturnValue(result);
    }
}
