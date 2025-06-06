package sircow.preservedinferno.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(SmithingMenu.class)
public abstract class SmithingMenuMixin extends ItemCombinerMenu {
    public SmithingMenuMixin(@Nullable MenuType<?> menuType, int containerId, Inventory inventory, ContainerLevelAccess access, ItemCombinerMenuSlotDefinition slotDefinition) {
        super(menuType, containerId, inventory, access, slotDefinition);
    }

    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$preventResultForForbiddenHelmet(CallbackInfo ci) {
        ItemStack baseStack = this.inputSlots.getItem(SmithingMenu.BASE_SLOT);
        CustomData customData = baseStack.get(DataComponents.CUSTOM_DATA);

        if (customData != null) {
            if (Objects.requireNonNull(baseStack.get(DataComponents.CUSTOM_DATA)).toString().contains("upgraded_nether_alloy")) {
                this.resultSlots.setItem(0, ItemStack.EMPTY);
                this.resultSlots.setRecipeUsed(null);
                ci.cancel();
            }
        }
    }

    @Inject(method = "createResult", at = @At("TAIL"))
    private void preserved_inferno$dynamicHelmetNameAtTail(CallbackInfo ci) {
        ItemStack resultStack = this.resultSlots.getItem(0);
        CustomData customData = resultStack.get(DataComponents.CUSTOM_DATA);

        if (customData != null) {
            if (Objects.requireNonNull(resultStack.get(DataComponents.CUSTOM_DATA)).toString().contains("upgraded_nether_alloy")) {
                ItemStack baseStack = this.inputSlots.getItem(SmithingMenu.BASE_SLOT);
                Component originalName = baseStack.get(DataComponents.CUSTOM_NAME);
                if (originalName == null) {
                    originalName = baseStack.getHoverName();
                }

                MutableComponent coloredPrefix = Component.literal("♦ ").withColor(0xF3B6B6);
                MutableComponent combinedText = Component.empty().append(coloredPrefix).append(originalName);
                MutableComponent finalName = combinedText.withStyle(combinedText.getStyle().withItalic(originalName.getStyle().isItalic()));
                resultStack.set(DataComponents.CUSTOM_NAME, finalName);
            }
        }
    }
}
