package sircow.preservedinferno.mixin;

import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.SlotSelectTime;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.components.ModComponents;
import sircow.preservedinferno.other.ModTags;

import java.util.List;

@Mixin(GhostSlots.class)
public class GhostSlotsMixin {
    @Shadow @Final private Reference2ObjectMap<Slot, ?> ingredients;
    @Shadow @Final private SlotSelectTime slotSelectTime;

    @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$changeGhostSlotTexture(GuiGraphicsExtractor guiGraphics, Minecraft minecraft, boolean isBiggerResultSlot, CallbackInfo ci) {
        for (var entry : this.ingredients.entrySet()) {
            Slot slot = entry.getKey();
            Object ghostSlotObj = entry.getValue();
            GhostSlotAccessor accessor = (GhostSlotAccessor) ghostSlotObj;
            List<ItemStack> items = accessor.getItems();
            boolean isResult = accessor.isResultSlot();
            int x = slot.x;
            int y = slot.y;
            ItemStack stack = items.isEmpty() ? ItemStack.EMPTY : items.get(slotSelectTime.currentIndex() % items.size());

            if (slot.index == 2 && stack.is(ModTags.ARMOR_TRIM_TEMPLATES)) {
                stack.set(ModComponents.EXHAUSTED_TEMPLATE, true);

                MutableComponent prefix = Component.translatable("item.pinferno.exhausted_template");
                MutableComponent base = Component.translatable(stack.getItem().getDescriptionId());
                MutableComponent name = prefix.append(Component.literal(" ")).append(base);

                stack.set(DataComponents.ITEM_NAME, name);
            }

            if (isResult && isBiggerResultSlot) {
                guiGraphics.fill(x - 4, y - 4, x + 20, y + 20, 822018048);
            }
            else {
                guiGraphics.fill(x, y, x + 16, y + 16, 822018048);
            }

            guiGraphics.fakeItem(stack, x, y);
            guiGraphics.fill(x, y, x + 16, y + 16, 822083583);

            if (isResult) {
                guiGraphics.itemDecorations(minecraft.font, stack, x, y);
            }
        }
        ci.cancel();
    }
}
