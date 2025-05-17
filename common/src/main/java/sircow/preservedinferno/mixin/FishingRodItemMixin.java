package sircow.preservedinferno.mixin;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.components.ModComponents;
import sircow.preservedinferno.item.ModItems;

import java.util.Objects;

@Mixin(FishingRodItem.class)
public class FishingRodItemMixin {
    @Unique
    private void updateComponentDurability(ItemStack stack, String componentType, String material, Item durabilityItem) {
        DataComponentType<String> componentKey;
        DataComponentType<Integer> durabilityKey;

        switch (componentType) {
            case "hook":
                componentKey = ModComponents.HOOK_COMPONENT;
                durabilityKey = ModComponents.HOOK_DURABILITY;
                break;
            case "line":
                componentKey = ModComponents.LINE_COMPONENT;
                durabilityKey = ModComponents.LINE_DURABILITY;
                break;
            case "sinker":
                componentKey = ModComponents.SINKER_COMPONENT;
                durabilityKey = ModComponents.SINKER_DURABILITY;
                break;
            default:
                return;
        }

        if (Objects.equals(stack.get(componentKey), material) && stack.get(durabilityKey) <= durabilityItem.getDefaultInstance().getMaxDamage()) {
            if (stack.get(durabilityKey) == durabilityItem.getDefaultInstance().getMaxDamage()) {
                stack.set(componentKey, "none");
                stack.set(durabilityKey, 0);
            }
            else {
                stack.set(durabilityKey, stack.get(durabilityKey) + 1);
            }
        }
    }

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/FishingHook;retrieve(Lnet/minecraft/world/item/ItemStack;)I"))
    private void preserved_inferno$onUseRetrieve(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (!level.isClientSide && player.fishing != null && player.gameMode() != GameType.CREATIVE) {
            ItemStack fishingRod = player.getItemInHand(hand);

            if (!Objects.equals(fishingRod.get(ModComponents.HOOK_COMPONENT), "none")) {
                if (Objects.equals(fishingRod.get(ModComponents.HOOK_COMPONENT), "iron")) {
                    updateComponentDurability(fishingRod, "hook", "iron", ModItems.IRON_FISHING_HOOK);
                }
                else if (Objects.equals(fishingRod.get(ModComponents.HOOK_COMPONENT), "diamond")) {
                    updateComponentDurability(fishingRod, "hook", "diamond", ModItems.DIAMOND_FISHING_HOOK);
                }
                else if (Objects.equals(fishingRod.get(ModComponents.HOOK_COMPONENT), "netherite")) {
                    updateComponentDurability(fishingRod, "hook", "netherite", ModItems.NETHERITE_FISHING_HOOK);
                }
            }

            if (!Objects.equals(fishingRod.get(ModComponents.LINE_COMPONENT), "none")) {
                if (Objects.equals(fishingRod.get(ModComponents.LINE_COMPONENT), "iron")) {
                    updateComponentDurability(fishingRod, "line", "iron", ModItems.IRON_LACED_FISHING_LINE);
                }
                else if (Objects.equals(fishingRod.get(ModComponents.LINE_COMPONENT), "diamond")) {
                    updateComponentDurability(fishingRod, "line", "diamond", ModItems.DIAMOND_LACED_FISHING_LINE);
                }
                else if (Objects.equals(fishingRod.get(ModComponents.LINE_COMPONENT), "netherite")) {
                    updateComponentDurability(fishingRod, "line", "netherite", ModItems.NETHERITE_LACED_FISHING_LINE);
                }
            }

            if (!Objects.equals(fishingRod.get(ModComponents.SINKER_COMPONENT), "none")) {
                if (Objects.equals(fishingRod.get(ModComponents.SINKER_COMPONENT), "iron")) {
                    updateComponentDurability(fishingRod, "sinker", "iron", ModItems.IRON_SINKER);
                }
                else if (Objects.equals(fishingRod.get(ModComponents.SINKER_COMPONENT), "diamond")) {
                    updateComponentDurability(fishingRod, "sinker", "diamond", ModItems.DIAMOND_SINKER);
                }
                else if (Objects.equals(fishingRod.get(ModComponents.SINKER_COMPONENT), "netherite")) {
                    updateComponentDurability(fishingRod, "sinker", "netherite", ModItems.NETHERITE_SINKER);
                }
            }
        }
    }
}
