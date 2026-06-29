package sircow.preservedinferno.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.components.ModComponents;
import sircow.preservedinferno.other.ModTags;

import java.util.Objects;
import java.util.Set;

@Mixin(SmithingMenu.class)
public abstract class SmithingMenuMixin extends ItemCombinerMenu {
    @Unique private ItemStack templateBeforeUse;

    public SmithingMenuMixin(@Nullable MenuType<?> menuType, int containerId, Inventory inventory, ContainerLevelAccess access, ItemCombinerMenuSlotDefinition slotDefinition) {
        super(menuType, containerId, inventory, access, slotDefinition);
    }

    @Inject(method = "onTake", at = @At("HEAD"))
    private void pinferno$captureTemplate(Player player, ItemStack stack, CallbackInfo ci) {
        ItemStack slot = this.inputSlots.getItem(SmithingMenu.TEMPLATE_SLOT);

        if (slot.is(ModTags.ARMOR_TRIM_TEMPLATES)) templateBeforeUse = slot.copy();
        else templateBeforeUse = null;
    }

    @Inject(method = "onTake", at = @At("TAIL"))
    private void pinferno$giveExhausted(Player player, ItemStack stack, CallbackInfo ci) {
        if (templateBeforeUse != null) {
            ItemStack exhausted = new ItemStack(templateBeforeUse.getItem());
            exhausted.set(ModComponents.EXHAUSTED_TEMPLATE, true);

            boolean added = player.getInventory().add(exhausted);
            if (!added) player.drop(exhausted, false);

            templateBeforeUse = null;
        }
    }


    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    private void pinferno$disallowExhaustedTemplate(CallbackInfo ci) {
        ItemStack template = this.inputSlots.getItem(SmithingMenu.TEMPLATE_SLOT);
        if (!template.isEmpty() && Boolean.TRUE.equals(template.get(ModComponents.EXHAUSTED_TEMPLATE))) {
            this.getSlot(SmithingMenu.RESULT_SLOT).set(ItemStack.EMPTY);
            ci.cancel();
        }
    }

    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    private void pinferno$preventResultForForbiddenHelmet(CallbackInfo ci) {
        ItemStack baseStack = this.inputSlots.getItem(SmithingMenu.BASE_SLOT);
        CustomData customData = baseStack.get(DataComponents.CUSTOM_DATA);

        if (customData != null) {
            if (Set.of("upgraded_nether_alloy", "upgraded_echoing_prism").contains(Objects.requireNonNull(baseStack.get(DataComponents.CUSTOM_DATA)).toString())) {
                this.resultSlots.setItem(0, ItemStack.EMPTY);
                this.resultSlots.setRecipeUsed(null);
                ci.cancel();
            }
        }
    }

    @Inject(method = "createResult", at = @At("TAIL"))
    private void pinferno$dynamicHelmetNameAtTail(CallbackInfo ci) {
        ItemStack resultStack = this.resultSlots.getItem(0);
        CustomData customData = resultStack.get(DataComponents.CUSTOM_DATA);
        int prefixColour = 0;
        boolean hasUpgrade = false;

        if (customData != null) {
            if (Objects.requireNonNull(resultStack.get(DataComponents.CUSTOM_DATA)).toString().contains("upgraded_nether_alloy")) {
                prefixColour = 0xF3B6B6;
                hasUpgrade = true;
            }
            if (Objects.requireNonNull(resultStack.get(DataComponents.CUSTOM_DATA)).toString().contains("upgraded_echoing_prism"))
            {
                prefixColour = 0x009295;
                hasUpgrade = true;
            }

            if (hasUpgrade) {
                ItemStack baseStack = this.inputSlots.getItem(SmithingMenu.BASE_SLOT);
                Component originalName = baseStack.get(DataComponents.CUSTOM_NAME);
                if (originalName == null) {
                    originalName = baseStack.getHoverName();
                }
                MutableComponent colouredPrefix;
                MutableComponent combinedText;
                MutableComponent finalName;
                if (!originalName.getSiblings().isEmpty() && !originalName.getSiblings().getFirst().getString().contains("♦")) {
                    colouredPrefix = Component.literal("♦ ").withColor(prefixColour);
                    combinedText = Component.empty().append(colouredPrefix).append(originalName);
                    finalName = combinedText.withStyle(combinedText.getStyle().withItalic(originalName.getStyle().isItalic()));
                    resultStack.set(DataComponents.CUSTOM_NAME, finalName);
                }
                else if (originalName.getSiblings().isEmpty() && !originalName.getString().contains("♦")) {
                    colouredPrefix = Component.literal("♦ ").withColor(prefixColour);
                    combinedText = Component.empty().append(colouredPrefix).append(originalName);
                    finalName = combinedText.withStyle(combinedText.getStyle().withItalic(originalName.getStyle().isItalic()));
                    resultStack.set(DataComponents.CUSTOM_NAME, finalName);
                }
            }
        }
    }
}
