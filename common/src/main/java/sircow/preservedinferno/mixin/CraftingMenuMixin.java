package sircow.preservedinferno.mixin;

import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.components.ModComponents;
import sircow.preservedinferno.other.ModTags;

@Mixin(CraftingMenu.class)
public abstract class CraftingMenuMixin {
    @Inject(method = "slotChangedCraftingGrid", at = @At("HEAD"), cancellable = true)
    private static void preserved_inferno$blockIfNotExhaustedTemplate(AbstractContainerMenu menu, ServerLevel level, Player player, CraftingContainer craftSlots, ResultContainer resultSlots, RecipeHolder<?> recipe, CallbackInfo ci) {
        CraftingInput input = craftSlots.asCraftInput();
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);

            if (stack.is(ModTags.ARMOR_TRIM_TEMPLATES)) {
                boolean exhausted = stack.getOrDefault(ModComponents.EXHAUSTED_TEMPLATE, false);

                if (!exhausted) {
                    resultSlots.setItem(0, ItemStack.EMPTY);
                    menu.setRemoteSlot(0, ItemStack.EMPTY);

                    if (player instanceof ServerPlayer serverPlayer) {
                        serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), 0, ItemStack.EMPTY));
                    }
                    ci.cancel();
                    return;
                }
            }
        }
    }
}
