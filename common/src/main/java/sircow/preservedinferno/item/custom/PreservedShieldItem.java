package sircow.preservedinferno.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sircow.preservedinferno.components.ModComponents;
import sircow.preservedinferno.other.ShieldStaminaHandler;

public class PreservedShieldItem extends ShieldItem {
    public PreservedShieldItem(Properties properties) {
        super(properties);
    }

    public int getMaxStamina(ItemStack stack) {
        return stack.get(ModComponents.SHIELD_MAX_STAMINA_COMPONENT);
    }

    public float getRegenerationRate(ItemStack stack) {
        return stack.get(ModComponents.SHIELD_REGEN_RATE_COMPONENT);
    }

    @Override
    public @NotNull ItemUseAnimation getUseAnimation(@NotNull ItemStack stack) {
        return ItemUseAnimation.BLOCK;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return 72000;
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (hand == InteractionHand.OFF_HAND && player.getOffhandItem().is(this)) {
            if (!ShieldStaminaHandler.isOnCooldown(player)) {
                player.startUsingItem(hand);
                return InteractionResult.CONSUME;
            }
            else {
                return InteractionResult.FAIL;
            }
        }
        return InteractionResult.PASS;
    }
}
