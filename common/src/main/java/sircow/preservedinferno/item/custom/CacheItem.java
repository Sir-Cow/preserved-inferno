package sircow.preservedinferno.item.custom;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public class CacheItem extends Item {
    private final int size;

    public CacheItem(Properties properties, int size) {
        super(properties);
        this.size = size;
    }

    public int getSize() {
        return this.size;
    }

    @Override
    public @NonNull InteractionResult use(@NonNull Level level, @NonNull Player player, @NonNull InteractionHand usedHand) {
        return super.use(level, player, usedHand);
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return false;
    }

    @Override
    public void onDestroyed(final ItemEntity entity) {
        ItemContainerContents contents = entity.getItem().get(DataComponents.CONTAINER);
        if (contents != null) {
            entity.getItem().set(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
            ItemUtils.onContainerDestroyed(entity, contents.allItemsCopyStream());
        }
    }
}
