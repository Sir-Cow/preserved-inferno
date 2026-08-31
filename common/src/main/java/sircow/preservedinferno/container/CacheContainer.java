package sircow.preservedinferno.container;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CacheContainer extends SimpleContainer {
    private final ItemStack itemStack;
    private ItemContainerContents sourceContainer;

    public CacheContainer(int size, ItemStack itemStack, ItemContainerContents sourceContainer) {
        super(size);
        this.itemStack = itemStack;
        this.sourceContainer = sourceContainer;

        NonNullList<ItemStack> stacks = NonNullList.withSize(size, ItemStack.EMPTY);
        sourceContainer.copyInto(stacks);

        for (int i = 0; i < size; i++) {
            this.setItem(i, stacks.get(i).copy());
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();

        if (itemStack == null) return;

        List<ItemStack> contents = new ArrayList<>(this.getContainerSize());
        for (int i = 0; i < this.getContainerSize(); i++) {
            contents.add(this.getItem(i).copy());
        }

        this.sourceContainer = ItemContainerContents.fromItems(contents);
        this.itemStack.set(DataComponents.CONTAINER, this.sourceContainer);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return itemStack != null && player.getInventory().contains(itemStack);
    }
}
