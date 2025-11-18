package sircow.preservedinferno.mixin;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(targets = "net.minecraft.client.gui.screens.recipebook.GhostSlots$GhostSlot")
public interface GhostSlotAccessor {
    @Accessor("items")
    List<ItemStack> getItems();

    @Accessor("isResultSlot")
    boolean isResultSlot();
}
