package sircow.preservedinferno.other;

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

        int i = 0;
        for (ItemStack stack : sourceContainer.nonEmptyItems()) {
            if (i < this.getContainerSize()) {
                this.setItem(i, stack.copy());
                i++;
            }
            else {
                break;
            }
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();

        if (itemStack != null) {
            List<ItemStack> contents = new ArrayList<>();
            for (int i = 0; i < this.getContainerSize(); i++) {
                contents.add(this.getItem(i).copy());
            }
            sourceContainer = ItemContainerContents.fromItems(contents);
            itemStack.set(DataComponents.CONTAINER, sourceContainer);
        }
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return itemStack != null && player.getInventory().contains(itemStack);
    }
}
