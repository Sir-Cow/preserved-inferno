package sircow.preservedinferno.item.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sircow.preservedinferno.components.ModComponents;
import sircow.preservedinferno.entity.custom.FlareGunProjectileEntity;

public class PreservedFlareGunItem extends Item {
    public PreservedFlareGunItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (player.containerMenu != player.inventoryMenu) {
            return InteractionResult.PASS;
        }

        ItemStack itemStack = player.getItemInHand(hand);
        player.startUsingItem(hand);

        if (level instanceof ServerLevel serverLevel) {
            FlareGunProjectileEntity flareProjectile = new FlareGunProjectileEntity(level, player, new ItemStack(Items.FIREWORK_ROCKET));
            flareProjectile.setItem(new ItemStack(Items.FIREWORK_ROCKET));
            flareProjectile.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
            flareProjectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 0.0F);
            serverLevel.addFreshEntity(flareProjectile);

            serverLevel.playSound(
                    null,
                    player.blockPosition(),
                    SoundEvents.FIREWORK_ROCKET_LAUNCH,
                    SoundSource.BLOCKS,
                    0.5F,
                    2.6F + (serverLevel.random.nextFloat() - serverLevel.random.nextFloat()) * 0.8F
            );
        }

        if (player.gameMode() != GameType.CREATIVE) {
            if (hand == InteractionHand.MAIN_HAND) {
                itemStack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
            }
            if (hand == InteractionHand.OFF_HAND) {
                itemStack.hurtAndBreak(1, player, EquipmentSlot.OFFHAND);
            }
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        player.stopUsingItem();
        return InteractionResult.CONSUME;
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, ServerLevel level, @NotNull Entity entity, EquipmentSlot slot) {
        if (!level.isClientSide() && entity instanceof Player player) {
            if (player.getCooldowns().isOnCooldown(stack)) {
                stack.remove(ModComponents.ON_COOLDOWN);
            }
            else {
                stack.set(ModComponents.ON_COOLDOWN, true);
            }
        }
    }
}
