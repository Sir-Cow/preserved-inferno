package sircow.preservedinferno.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.block.custom.BoomBoxBlock;
import sircow.preservedinferno.entity.custom.DynamiteEntity;
import sircow.preservedinferno.sound.ModSounds;

public class DynamiteItem extends Item implements ProjectileItem {
    public DynamiteItem(final Item.Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull InteractionResult use(final Level level, final Player player, final @NonNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.DYNAMITE_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

        if (level instanceof ServerLevel serverLevel) Projectile.spawnProjectileFromRotation(DynamiteEntity::new, serverLevel, itemStack, player, 0.0F, 1.5F, 1.0F);

        player.awardStat(Stats.ITEM_USED.get(this));
        itemStack.consume(1, player);
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NonNull InteractionResult useOn(final UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        if (state.getBlock() instanceof BoomBoxBlock) {
            int dynamite = state.getValue(BoomBoxBlock.DYNAMITE);

            if (dynamite < 4) {
                Player player = context.getPlayer();
                if (player != null) {
                    if (!level.isClientSide()) {
                        level.setBlock(pos, state.setValue(BoomBoxBlock.DYNAMITE, dynamite + 1), Block.UPDATE_ALL);
                        context.getItemInHand().consume(1, player);
                    }
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.BOOM_BOX_LOAD, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.FAIL;
        }
        return super.useOn(context);
    }

    @Override
    public @NonNull Projectile asProjectile(final @NonNull Level level, final Position position, final @NonNull ItemStack itemStack, final @NonNull Direction direction) {
        return new DynamiteEntity(level, position.x(), position.y(), position.z(), itemStack);
    }
}
