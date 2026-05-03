package sircow.preservedinferno.mixin;

import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sircow.preservedinferno.item.ModItems;

@Mixin(AnvilScreen.class)
public class AnvilScreenMixin {
    @ModifyVariable(method = "extractLabels", at = @At(value = "STORE"), ordinal = 0)
    private Component preserved_inferno$replaceRepairText(Component line) {
        if (line == null) return null;

        if (line.getContents() instanceof TranslatableContents contents) {
            if (contents.getKey().equals("container.repair.cost")) {
                AnvilScreen screen = (AnvilScreen)(Object)this;
                AnvilMenu menu = screen.getMenu();
                ItemStack second = menu.getSlot(1).getItem();

                if (second.is(ModItems.REPAIR_KIT) || second.is(ModItems.FORGE_DUST)) {
                    int cost = menu.getCost();
                    return Component.translatable("container.pinferno.repair.cost", cost);
                }
                if (second.isEmpty()) {
                    int cost = menu.getCost();
                    return Component.translatable("container.pinferno.rename.cost", cost);
                }
            }
        }
        return line;
    }
}
