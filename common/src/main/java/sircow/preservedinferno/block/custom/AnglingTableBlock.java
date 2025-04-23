package sircow.preservedinferno.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import sircow.preservedinferno.screen.AnglingTableMenu;

public class AnglingTableBlock extends Block {
    public static final MapCodec<AnglingTableBlock> CODEC = simpleCodec(AnglingTableBlock::new);
    private static final Component CONTAINER_TITLE = Component.translatable("container.pinferno.angling_table");

    public MapCodec<? extends AnglingTableBlock> codec() {
        return CODEC;
    }

    public AnglingTableBlock(Properties properties) {
        super(properties);
    }

    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if (!level.isClientSide) {
            player.openMenu(blockState.getMenuProvider(level, blockPos));
            player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
        }

        return InteractionResult.SUCCESS;
    }

    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider((windowID, inventory, player) -> new AnglingTableMenu(windowID, inventory, ContainerLevelAccess.create(level, pos)), CONTAINER_TITLE);
    }
}
