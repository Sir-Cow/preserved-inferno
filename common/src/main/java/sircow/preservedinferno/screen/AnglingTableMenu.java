package sircow.preservedinferno.screen;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.block.ModBlocks;

public class AnglingTableMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess access;

    public AnglingTableMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public AnglingTableMenu(int containerId, Inventory playerInventory, final ContainerLevelAccess access) {
        super(Constants.ANGLING_TABLE_MENU_TYPE.get(), containerId);
        this.access = access;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, ModBlocks.ANGLING_TABLE);
    }
}
