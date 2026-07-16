package sircow.preservedinferno.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import sircow.preservedinferno.entity.custom.PrimedBoomBox;
import sircow.preservedinferno.item.ModItems;
import sircow.preservedinferno.sound.ModSounds;

import java.util.Map;
import java.util.function.BiConsumer;

public class BoomBoxBlock extends Block implements WorldlyContainerHolder {
    public static final IntegerProperty DYNAMITE = IntegerProperty.create("dynamite", 0, 4);

    public BoomBoxBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(DYNAMITE, 0));
    }

    @Override
    protected void onPlace(final BlockState state, final @NonNull Level level, final @NonNull BlockPos pos, final BlockState oldState, final boolean movedByPiston) {
        if (!oldState.is(state.getBlock())) {
            if (level.hasNeighborSignal(pos) && prime(level, pos, state, null)) level.removeBlock(pos, false);
        }
    }

    @Override
    protected void neighborChanged(final @NonNull BlockState state, final Level level, final @NonNull BlockPos pos, final @NonNull Block block, final @Nullable Orientation orientation, final boolean movedByPiston) {
        if (level.hasNeighborSignal(pos) && prime(level, pos, state, null)) level.removeBlock(pos, false);
    }

    @Override
    public void wasExploded(final ServerLevel level, final @NonNull BlockPos pos, final @NonNull Explosion explosion) {
        if (level.getGameRules().get(GameRules.TNT_EXPLODES)) {
            BlockState state = level.getBlockState(pos);
            if (!state.is(this)) state = this.defaultBlockState();
            if (state.getValue(DYNAMITE) > 0) {
                PrimedBoomBox primed = new PrimedBoomBox(level, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, explosion.getIndirectSourceEntity());
                primed.setBlockState(state);
                primed.setFuse(PrimedBoomBox.getRandomShortFuse(primed.getFuse(), level.getRandom()));
                level.addFreshEntity(primed);
            }
        }
    }

    @Override
    public void onExplosionHit(@NonNull BlockState state, ServerLevel level, @NonNull BlockPos pos, @NonNull Explosion explosion, @NonNull BiConsumer<ItemStack, BlockPos> onHit) {
        if (!level.isClientSide() && state.getValue(DYNAMITE) > 0) {
            PrimedBoomBox primedBoomBox = new PrimedBoomBox(level, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, explosion.getIndirectSourceEntity() instanceof LivingEntity living ? living : null);
            primedBoomBox.setBlockState(state);
            primedBoomBox.setFuse(PrimedBoomBox.getRandomShortFuse(80, level.getRandom()));
            level.addFreshEntity(primedBoomBox);
        }
    }

    public static boolean prime(final Level level, final BlockPos pos, final BlockState state, final @Nullable LivingEntity source) {
        if (!state.hasProperty(DYNAMITE) || state.getValue(DYNAMITE) <= 0) return false;

        if (level instanceof ServerLevel serverLevel && serverLevel.getGameRules().get(GameRules.TNT_EXPLODES)) {
            PrimedBoomBox boomBox = new PrimedBoomBox(level, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, source);
            boomBox.setBlockState(state);
            level.addFreshEntity(boomBox);
            level.playSound(null, boomBox.getX(), boomBox.getY(), boomBox.getZ(), ModSounds.BOOM_BOX_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(source, GameEvent.PRIME_FUSE, pos);
            return true;
        }
        else return false;
    }

    @Override
    protected @NonNull InteractionResult useItemOn(final ItemStack itemStack, @NonNull final BlockState state, @NonNull final Level level, @NonNull final BlockPos pos, @NonNull final Player player, final @NonNull InteractionHand hand, final @NonNull BlockHitResult hitResult) {
        if (!itemStack.is(Items.FLINT_AND_STEEL) && !itemStack.is(Items.FIRE_CHARGE)) return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);

        if (!state.hasProperty(DYNAMITE) || state.getValue(DYNAMITE) <= 0) return InteractionResult.FAIL;

        if (level instanceof ServerLevel serverLevel && !serverLevel.getGameRules().get(GameRules.TNT_EXPLODES)) {
            player.sendOverlayMessage(Component.translatable("block.minecraft.tnt.disabled"));
            return InteractionResult.PASS;
        }

        if (prime(level, pos, state, null)) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
            Item item = itemStack.getItem();
            if (itemStack.is(Items.FLINT_AND_STEEL)) itemStack.hurtAndBreak(1, player, hand.asEquipmentSlot());
            else itemStack.consume(1, player);

            player.awardStat(Stats.ITEM_USED.get(item));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void onProjectileHit(@NonNull final Level level, @NonNull final BlockState state, @NonNull final BlockHitResult blockHit, @NonNull final Projectile projectile) {
        if (level instanceof ServerLevel serverLevel) {
            BlockPos pos = blockHit.getBlockPos();
            Entity owner = projectile.getOwner();
            if (projectile.isOnFire() && projectile.mayInteract(serverLevel, pos) && prime(level, pos, state, owner instanceof LivingEntity livingEntity ? livingEntity : null)) {
                level.removeBlock(pos, false);
            }
        }
    }

    @Override
    public boolean dropFromExplosion(final @NonNull Explosion explosion) {
        return true;
    }

    @Override
    public @NonNull BlockState playerWillDestroy(Level level, @NonNull BlockPos pos, @NonNull BlockState state, @NonNull Player player) {
        if (!level.isClientSide() && player.isCreative()) {
            int dynamite = state.getValue(DYNAMITE);

            if (dynamite > 0) popResource(level, pos, new ItemStack(ModItems.DYNAMITE, dynamite));
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DYNAMITE);
    }

    @Override
    public @NonNull WorldlyContainer getContainer(@NonNull BlockState state, @NonNull LevelAccessor level, @NonNull BlockPos pos) {
        return new BoomBoxContainer(state, level, pos);
    }

    private static class BoomBoxContainer extends SimpleContainer implements WorldlyContainer {
        private final BlockState state;
        private final LevelAccessor level;
        private final BlockPos pos;

        public BoomBoxContainer(BlockState state, LevelAccessor level, BlockPos pos) {
            super(2);
            this.state = state;
            this.level = level;
            this.pos = pos;
        }

        @Override
        public int getMaxStackSize() {
            return 4;
        }

        @Override
        public @NonNull ItemStack getItem(int slot) {
            if (slot == 1) {
                int dynamite = this.state.getValue(DYNAMITE);
                return dynamite > 0 ? new ItemStack(ModItems.DYNAMITE, dynamite) : ItemStack.EMPTY;
            }
            return ItemStack.EMPTY;
        }

        @Override
        public int @NonNull [] getSlotsForFace(@NonNull Direction direction) {
            if (direction == Direction.UP && this.state.getValue(DYNAMITE) < 4) return new int[]{0};
            if (direction == Direction.DOWN && this.state.getValue(DYNAMITE) > 0) return new int[]{1};
            return new int[0];
        }

        @Override
        public boolean canPlaceItemThroughFace(int slot, @NonNull ItemStack stack, @Nullable Direction direction) {
            return direction == Direction.UP && slot == 0 && stack.is(ModItems.DYNAMITE) && this.state.getValue(DYNAMITE) < 4;
        }

        @Override
        public boolean canTakeItemThroughFace(int slot, @NonNull ItemStack stack, @NonNull Direction direction) {
            return direction == Direction.DOWN && slot == 1 && this.state.getValue(DYNAMITE) > 0;
        }

        @Override
        public @NonNull ItemStack removeItem(int slot, int amount) {
            if (slot == 1) {
                int dynamite = this.state.getValue(DYNAMITE);

                if (dynamite > 0) {
                    int removed = Math.min(amount, dynamite);

                    this.level.setBlock(this.pos, this.state.setValue(DYNAMITE, dynamite - removed), 3);
                    return new ItemStack(ModItems.DYNAMITE, removed);
                }
            }
            return ItemStack.EMPTY;
        }

        @Override
        public @NonNull ItemStack removeItemNoUpdate(int slot) {
            if (slot == 1) {
                int dynamite = this.state.getValue(DYNAMITE);

                if (dynamite > 0) {
                    this.level.setBlock(this.pos, this.state.setValue(DYNAMITE, 0), 3);
                    return new ItemStack(ModItems.DYNAMITE, dynamite);
                }
            }
            return ItemStack.EMPTY;
        }

        @Override
        public void setItem(int slot, @NonNull ItemStack stack) {
            if (slot == 0 && stack.is(ModItems.DYNAMITE)) {
                int current = this.state.getValue(DYNAMITE);
                int amount = Math.min(4, current + stack.getCount());

                this.level.setBlock(this.pos, this.state.setValue(DYNAMITE, amount), 3);
            }
        }

        @Override
        public boolean isEmpty() {
            return this.state.getValue(DYNAMITE) == 0;
        }

        @Override
        public void setChanged() {}
    }

    @Override
    protected @NonNull ItemStack getCloneItemStack(@NonNull LevelReader level, @NonNull BlockPos pos, BlockState state, boolean includeData)  {
        ItemStack stack = new ItemStack(this);

        if (state.getValue(DYNAMITE) > 0) {
            stack.set(DataComponents.BLOCK_STATE, new BlockItemStateProperties(Map.of(DYNAMITE.getName(), String.valueOf(state.getValue(DYNAMITE)))));
        }
        return stack;
    }
}
