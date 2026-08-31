package sircow.preservedinferno.item.custom;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sircow.preservedinferno.sound.ModSounds;
import sircow.preservedinferno.trigger.ModTriggers;

public class SculkInfusionItem extends Item {
    public static final int MAX_DURABILITY = 1396;

    public SculkInfusionItem(Properties props) {
        super(props);
    }

    @Override
    public void onCraftedBy(ItemStack stack, @NotNull Player player) {
        stack.setDamageValue(stack.getMaxDamage() - 1);
        super.onCraftedBy(stack, player);
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return super.isFoil(stack) || stack.getDamageValue() == 0;
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() != this) return InteractionResult.PASS;

        if (stack.getDamageValue() != 0) {
            return InteractionResult.FAIL;
        }

        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity livingEntity) {
        if (livingEntity instanceof Player player && !level.isClientSide()) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.SCULK_INFUSION, SoundSource.PLAYERS, 1.0F, 1.0F);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.SCULK_INFUSION1, SoundSource.PLAYERS, 0.1F, 1.0F);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.SCULK_INFUSION2, SoundSource.PLAYERS, 1.0F, 1.0F);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.SCULK_INFUSION3, SoundSource.PLAYERS, 0.1F, 1.0F);

            if (!player.isCreative() && !player.isSpectator()) {
                stack.shrink(1);
            }
            player.giveExperiencePoints(MAX_DURABILITY);

        }
        if (livingEntity instanceof ServerPlayer serverPlayer) {
            ModTriggers.CONSUME_SCULK_INFUSION.trigger(serverPlayer);
        }
        return stack;
    }
}
