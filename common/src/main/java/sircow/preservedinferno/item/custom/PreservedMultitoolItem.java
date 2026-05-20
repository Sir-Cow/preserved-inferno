package sircow.preservedinferno.item.custom;

import com.google.common.collect.BiMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import sircow.preservedinferno.mixin.AxeItemAccessor;
import sircow.preservedinferno.mixin.HoeItemAccessor;
import sircow.preservedinferno.mixin.ShovelItemAccessor;
import sircow.preservedinferno.trigger.ModTriggers;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PreservedMultitoolItem extends Item {
    public PreservedMultitoolItem(final ToolMaterial material, final float attackDamageBaseline, final float attackSpeedBaseline, final Item.Properties properties) {
        super(buildProperties(material, attackDamageBaseline, attackSpeedBaseline, properties));
    }

    private static Item.Properties buildProperties(ToolMaterial material, float attackDamage, float attackSpeed, Item.Properties properties) {
        HolderGetter<Block> lookup = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK);

        return properties
                .tool(material, BlockTags.MINEABLE_WITH_PICKAXE, attackDamage, attackSpeed, 0.0F)
                .component(DataComponents.TOOL, new Tool(
                        List.of(
                                Tool.Rule.deniesDrops(lookup.getOrThrow(material.incorrectBlocksForDrops())),
                                Tool.Rule.minesAndDrops(lookup.getOrThrow(BlockTags.MINEABLE_WITH_PICKAXE), material.speed()),
                                Tool.Rule.minesAndDrops(lookup.getOrThrow(BlockTags.MINEABLE_WITH_AXE), material.speed()),
                                Tool.Rule.minesAndDrops(lookup.getOrThrow(BlockTags.MINEABLE_WITH_SHOVEL), material.speed()),
                                Tool.Rule.minesAndDrops(lookup.getOrThrow(BlockTags.MINEABLE_WITH_HOE), material.speed())
                        ),
                        1.0F,
                        1,
                        true
                ));
    }

    @Override
    public @NonNull InteractionResult useOn(final UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        BlockState blockState = level.getBlockState(pos);

        if (playerHasBlockingItemUseIntent(context)) return InteractionResult.PASS;

        ItemStack stack = context.getItemInHand();

        Optional<BlockState> modified = evaluateNewBlockState(level, pos, player, blockState);
        if (modified.isPresent()) {
            BlockState before = level.getBlockState(pos);
            BlockState after = modified.get();

            if (before.getBlock() instanceof WeatheringCopper) {
                Optional<BlockState> prev = WeatheringCopper.getPrevious(before);

                if (prev.isPresent() && prev.get().getBlock() == after.getBlock()) {
                    if (player instanceof ServerPlayer serverPlayer) ModTriggers.SCRAPE_COPPER.get().trigger(serverPlayer);
                }
            }

            if (player instanceof ServerPlayer serverPlayer) CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);

            level.setBlock(pos, after, 11);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, modified.get()));

            if (player != null) stack.hurtAndBreak(1, player, context.getHand().asEquipmentSlot());
            return InteractionResult.SUCCESS;
        }

        boolean crouching = player != null && player.isShiftKeyDown();

        if (crouching) {
            // shovel
            if (context.getClickedFace() != Direction.DOWN) {
                BlockState flatten = ShovelItemAccessor.getFlattenables().get(blockState.getBlock());
                BlockState resultState = null;

                if (flatten != null && level.getBlockState(pos.above()).isAir()) {
                    level.playSound(player, pos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
                    resultState = flatten;
                }
                else if (blockState.getBlock() instanceof CampfireBlock && blockState.getValue(CampfireBlock.LIT)) {
                    if (!level.isClientSide()) level.levelEvent(null, 1009, pos, 0);

                    CampfireBlock.dowse(player, level, pos, blockState);
                    resultState = blockState.setValue(CampfireBlock.LIT, false);
                }

                if (resultState != null) {
                    if (!level.isClientSide()) {
                        level.setBlock(pos, resultState, 11);
                        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, resultState));
                        stack.hurtAndBreak(1, player, context.getHand().asEquipmentSlot());
                    }
                    return InteractionResult.SUCCESS;
                }
            }

            Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> hoeLogic = HoeItemAccessor.getTillables().get(context.getLevel().getBlockState(context.getClickedPos()).getBlock());
            if (hoeLogic != null && hoeLogic.getFirst().test(context)) {
                level.playSound(player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);

                if (!level.isClientSide()) {
                    hoeLogic.getSecond().accept(context);
                    stack.hurtAndBreak(1, player, context.getHand().asEquipmentSlot());
                }
                return InteractionResult.SUCCESS;
            }
        }
        else {
            // hoe
            Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> hoeLogic = HoeItemAccessor.getTillables().get(context.getLevel().getBlockState(context.getClickedPos()).getBlock());
            if (hoeLogic != null && hoeLogic.getFirst().test(context)) {
                level.playSound(player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);

                if (!level.isClientSide()) {
                    hoeLogic.getSecond().accept(context);

                    if (player != null) stack.hurtAndBreak(1, player, context.getHand().asEquipmentSlot());
                }
                return InteractionResult.SUCCESS;
            }

            if (context.getClickedFace() != Direction.DOWN) {
                BlockState flatten = ShovelItemAccessor.getFlattenables().get(blockState.getBlock());
                BlockState resultState = null;

                if (flatten != null && level.getBlockState(pos.above()).isAir()) {
                    level.playSound(player, pos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
                    resultState = flatten;
                }
                else if (blockState.getBlock() instanceof CampfireBlock && blockState.getValue(CampfireBlock.LIT)) {
                    if (!level.isClientSide()) level.levelEvent(null, 1009, pos, 0);

                    CampfireBlock.dowse(player, level, pos, blockState);
                    resultState = blockState.setValue(CampfireBlock.LIT, false);
                }

                if (resultState != null) {
                    if (!level.isClientSide()) {
                        level.setBlock(pos, resultState, 11);
                        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, resultState));

                        if (player != null) stack.hurtAndBreak(1, player, context.getHand().asEquipmentSlot());
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    private static boolean playerHasBlockingItemUseIntent(final UseOnContext context) {
        Player player = context.getPlayer();
        return context.getHand().equals(InteractionHand.MAIN_HAND) && player.getOffhandItem().has(DataComponents.BLOCKS_ATTACKS) && !player.isSecondaryUseActive();
    }

    private Optional<BlockState> evaluateNewBlockState(final Level level, final BlockPos pos, @Nullable final Player player, final BlockState oldState) {
        Optional<BlockState> strippedBlock = this.getStripped(oldState);
        if (strippedBlock.isPresent()) {
            level.playSound(player, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            return strippedBlock;
        }
        else {
            Optional<BlockState> scrapedBlock = WeatheringCopper.getPrevious(oldState);
            if (scrapedBlock.isPresent()) {
                spawnSoundAndParticle(level, pos, player, oldState, SoundEvents.AXE_SCRAPE, 3005);
                return scrapedBlock;
            }
            else {
                Optional<BlockState> waxoffBlock = Optional.ofNullable((Block)((BiMap) HoneycombItem.WAX_OFF_BY_BLOCK.get()).get(oldState.getBlock()))
                        .map(b -> b.withPropertiesOf(oldState));
                if (waxoffBlock.isPresent()) {
                    spawnSoundAndParticle(level, pos, player, oldState, SoundEvents.AXE_WAX_OFF, 3004);
                    return waxoffBlock;
                }
                else return Optional.empty();
            }
        }
    }

    private static void spawnSoundAndParticle(final Level level, final BlockPos pos, @Nullable final Player player, final BlockState oldState, final SoundEvent soundEvent, final int particle) {
        level.playSound(player, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.levelEvent(player, particle, pos, 0);
        if (oldState.getBlock() instanceof ChestBlock && oldState.getValue(ChestBlock.TYPE) != ChestType.SINGLE) {
            BlockPos neighborPos = ChestBlock.getConnectedBlockPos(pos, oldState);
            level.gameEvent(GameEvent.BLOCK_CHANGE, neighborPos, GameEvent.Context.of(player, level.getBlockState(neighborPos)));
            level.levelEvent(player, particle, neighborPos, 0);
        }
    }

    private Optional<BlockState> getStripped(BlockState state) {
        Block block = AxeItemAccessor.getStrippables().get(state.getBlock());
        if (block == null) return Optional.empty();

        return Optional.of(block.defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS)));
    }

    public static Consumer<UseOnContext> changeIntoState(final BlockState state) {
        return context -> {
            context.getLevel().setBlock(context.getClickedPos(), state, 11);
            context.getLevel().gameEvent(GameEvent.BLOCK_CHANGE, context.getClickedPos(), GameEvent.Context.of(context.getPlayer(), state));
        };
    }

    public static Consumer<UseOnContext> changeIntoStateAndDropItem(final BlockState state, final ItemLike item) {
        return context -> {
            context.getLevel().setBlock(context.getClickedPos(), state, 11);
            context.getLevel().gameEvent(GameEvent.BLOCK_CHANGE, context.getClickedPos(), GameEvent.Context.of(context.getPlayer(), state));
            Block.popResourceFromFace(context.getLevel(), context.getClickedPos(), context.getClickedFace(), new ItemStack(item));
        };
    }

    public static boolean onlyIfAirAbove(final UseOnContext context) {
        return context.getClickedFace() != Direction.DOWN && context.getLevel().getBlockState(context.getClickedPos().above()).isAir();
    }
}
