package sircow.preservedinferno.block.entity;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.BlockPos;
import sircow.preservedinferno.fluid.CauldronFluid;

public class PreservedCauldronRenderState extends BlockEntityRenderState {
    public float fillRatio;
    public int packedLight;
    public BlockPos pos;
    public CauldronFluid fluid = CauldronFluid.EMPTY;
}
