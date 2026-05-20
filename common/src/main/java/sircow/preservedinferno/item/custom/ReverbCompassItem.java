package sircow.preservedinferno.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import sircow.preservedinferno.item.ModItems;
import sircow.preservedinferno.sound.ModSounds;
import sircow.preservedinferno.trigger.ModTriggers;

import java.util.Optional;

public class ReverbCompassItem extends Item {
    public ReverbCompassItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(final ItemStack itemStack) {
        return itemStack.has(DataComponents.LODESTONE_TRACKER) || super.isFoil(itemStack);
    }

    @Override
    public void inventoryTick(final ItemStack itemStack, final @NonNull ServerLevel level, final @NonNull Entity owner, @Nullable final EquipmentSlot slot) {
        LodestoneTracker tracker = itemStack.get(DataComponents.LODESTONE_TRACKER);
        if (tracker != null) {
            LodestoneTracker newTracker = tracker.tick(level);
            if (newTracker != tracker) itemStack.set(DataComponents.LODESTONE_TRACKER, newTracker);
        }

        if (!(owner instanceof Player player)) return;
        if (level.isClientSide()) return;

        if (slot != EquipmentSlot.MAINHAND && slot != EquipmentSlot.OFFHAND) return;

        if (slot == EquipmentSlot.OFFHAND) {
            ItemStack mainHand = player.getMainHandItem();
            if (mainHand.getItem() instanceof CompassItem || mainHand.getItem() == ModItems.REVERB_COMPASS) return;
        }
    }

    @Override
    public @NonNull InteractionResult useOn(final UseOnContext context) {
        BlockPos blockPos = context.getClickedPos();
        Level level = context.getLevel();

        if (!level.getBlockState(blockPos).is(Blocks.LODESTONE)) return super.useOn(context);

        Player player = context.getPlayer();
        ItemStack itemStack = context.getItemInHand();

        level.playSound(null, blockPos, SoundEvents.LODESTONE_COMPASS_LOCK, SoundSource.PLAYERS, 1.0F, 1.0F);

        LodestoneTracker target = new LodestoneTracker(Optional.of(GlobalPos.of(level.dimension(), blockPos)), true);

        if (player != null && player.isShiftKeyDown()) {
            itemStack.set(DataComponents.LODESTONE_TRACKER, target);
            return InteractionResult.SUCCESS;
        }

        boolean replaceExistingStack = !player.hasInfiniteMaterials() && itemStack.getCount() == 1;

        if (replaceExistingStack) itemStack.set(DataComponents.LODESTONE_TRACKER, target);
        else {
            ItemStack lodestoneCompass = itemStack.transmuteCopy(ModItems.REVERB_COMPASS, 1);
            itemStack.consume(1, player);
            lodestoneCompass.set(DataComponents.LODESTONE_TRACKER, target);

            if (!player.getInventory().add(lodestoneCompass)) player.drop(lodestoneCompass, false);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        LodestoneTracker tracker = stack.get(DataComponents.LODESTONE_TRACKER);

        if (tracker == null || tracker.target().isEmpty()) return InteractionResult.PASS;

        GlobalPos globalPos = tracker.target().get();
        if (level.dimension() != globalPos.dimension()) return InteractionResult.FAIL;

        if (!level.isClientSide()) {
            BlockPos pos = globalPos.pos().above();

            player.teleportTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.REVERB_COMPASS_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.REVERB_COMPASS_USE1, SoundSource.PLAYERS, 1.0F, 1.0F);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.REVERB_COMPASS_USE2, SoundSource.PLAYERS, 1.0F, 1.0F);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.REVERB_COMPASS_USE3, SoundSource.PLAYERS, 1.0F, 1.0F);

            if (player instanceof ServerPlayer serverPlayer) ModTriggers.USE_REVERB_COMPASS.get().trigger(serverPlayer);

            if (!player.hasInfiniteMaterials()) {
                ItemStack newCompass = new ItemStack(Items.COMPASS);

                if (stack.getCount() == 1) player.setItemInHand(hand, newCompass);
                else {
                    stack.consume(1, player);
                    if (!player.getInventory().add(newCompass)) player.drop(newCompass, false);
                }
            }
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResult.CONSUME;
    }
}
