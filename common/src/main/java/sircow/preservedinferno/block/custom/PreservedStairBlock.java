package sircow.preservedinferno.block.custom;

import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;

public class PreservedStairBlock extends StairBlock {
    // made because StairBlock has protected access
    public PreservedStairBlock(BlockState baseState, Properties properties) {
        super(baseState, properties);
    }
}
