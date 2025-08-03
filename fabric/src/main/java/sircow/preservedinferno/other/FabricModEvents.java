package sircow.preservedinferno.other;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import sircow.preservedinferno.PreservedInferno;
import sircow.preservedinferno.effect.ModEffects;
import sircow.preservedinferno.item.ModItems;
import sircow.preservedinferno.platform.Services;
import sircow.preservedinferno.trigger.ModTriggers;

import java.util.*;

public class FabricModEvents {
    private static final long REGEN_COOLDOWN_MS = 10 * 60 * 1000;

    public static void checkInitialAdvancement() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                ServerLevel level = player.level();
                UUID uuid = player.getUUID();

                if (AdvancementDelayCache.hasCompleted(level, uuid)) continue;

                var advancement = server.getAdvancements().get(ResourceLocation.withDefaultNamespace("story/root"));
                if (advancement == null) continue;

                var progress = player.getAdvancements().getOrStartProgress(advancement);
                if (progress.isDone()) {
                    AdvancementDelayCache.markCompleted(level, uuid);
                }
            }
        });
    }

    public static void limitCropBreak() {
        LootTableEvents.MODIFY_DROPS.register((entry, context, drops) -> {
            if (context.hasParameter(LootContextParams.BLOCK_STATE) &&
                    context.hasParameter(LootContextParams.TOOL) &&
                    context.hasParameter(LootContextParams.THIS_ENTITY)) {

                BlockState brokenState = context.getParameter(LootContextParams.BLOCK_STATE);
                ItemStack toolUsed = context.getParameter(LootContextParams.TOOL);
                Player playerEntity = null;

                if (context.getOptionalParameter(LootContextParams.THIS_ENTITY) instanceof Player) {
                    playerEntity = (Player) context.getOptionalParameter(LootContextParams.THIS_ENTITY);
                }

                if (playerEntity != null) {
                    if (brokenState.is(Blocks.WHEAT) ||
                            brokenState.is(Blocks.CARROTS) ||
                            brokenState.is(Blocks.POTATOES) ||
                            brokenState.is(Blocks.BEETROOTS) ||
                            brokenState.is(Blocks.PUMPKIN_STEM) ||
                            brokenState.is(Blocks.MELON_STEM) ||
                            brokenState.is(Blocks.NETHER_WART)) {

                        if (!toolUsed.is(ItemTags.HOES)) {
                            drops.clear();
                        }
                    }
                }
            }
        });
    }

    public static void modifySleeping() {
        // only allow sleeping if holding a dreamcatcher & no mobs in line of sight of bed
        EntitySleepEvents.ALLOW_SLEEPING.register((Player player, BlockPos pos) -> {
            ItemStack mainHandItem = player.getItemInHand(InteractionHand.MAIN_HAND);
            ItemStack offHandItem = player.getItemInHand(InteractionHand.OFF_HAND);
            boolean holdingDreamcatcher = mainHandItem.getItem() == ModItems.DREAMCATCHER || offHandItem.getItem() == ModItems.DREAMCATCHER;

            if (MobLineOfSight.hasMonsterLineOfSight(player.level(), pos)) {
                player.displayClientMessage(Component.translatable("block.minecraft.bed.not_safe"), true);
                return Player.BedSleepingProblem.OTHER_PROBLEM;
            }
            if (player.level().isMoonVisible() && !holdingDreamcatcher) {
                player.displayClientMessage(Component.translatable("block.minecraft.bed.no_dreamcatcher"), true);
                return Player.BedSleepingProblem.OTHER_PROBLEM;
            }
            if (player.level().isMoonVisible() && holdingDreamcatcher) {
                return null;
            }
            else {
                return Player.BedSleepingProblem.OTHER_PROBLEM;
            }
        });

        EntitySleepEvents.START_SLEEPING.register((entity, sleepingPos) -> {
            if (entity instanceof Player player) {
                ItemStack main = player.getMainHandItem();
                ItemStack off = player.getOffhandItem();

                if (main.is(ModItems.DREAMCATCHER)) {
                    main.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                }
                else if (off.is(ModItems.DREAMCATCHER)) {
                    off.hurtAndBreak(1, player, EquipmentSlot.OFFHAND);
                }
            }
        });

        EntitySleepEvents.STOP_SLEEPING.register((entity, sleepingPos) -> {
            if (!Services.PLATFORM.isModLoaded("pblizzard")) {
                if (entity instanceof Player player) {
                    if (player.getSleepTimer() > 20 && !player.level().isMoonVisible()) {
                        player.addEffect(new MobEffectInstance(ModEffects.WELL_RESTED, 24000, 0, false, false, true));
                        player.displayClientMessage(Component.translatable("effect.pinferno.well_rested_awake"), true);
                    }
                }
            }
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayer player = handler.player;
            if (player.getEntityData().get(ModEntityData.PLAYER_HARDCORE_REGEN_COOLDOWN) == 0) {
                player.getEntityData().set(ModEntityData.PLAYER_HARDCORE_REGEN_COOLDOWN, System.currentTimeMillis() - REGEN_COOLDOWN_MS);
            }
        });
    }

    public static void handleEntityDeath() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            // prevent drowned dropping trident
            if (entity instanceof Drowned drowned) {
                if (drowned.getMainHandItem().is(Items.TRIDENT)) {
                    drowned.setDropChance(EquipmentSlot.MAINHAND, 0.0f);
                }
            }
        });

        ServerLivingEntityEvents.AFTER_DEATH.register((livingEntity, damageSource) -> {
            // reset shield cooldown
            if (livingEntity instanceof Player player) {
                ShieldStaminaHandler.playerShieldCooldownMap.remove(player.getUUID());
            }
        });

        ServerLivingEntityEvents.ALLOW_DEATH.register((livingEntity, damageSource, damageAmount) -> {
            if (livingEntity instanceof Player player) {
                TempInventoryStorage.savePlayerInventory(player);
            }
            // hardcore
            if (livingEntity instanceof ServerPlayer player) {
                if (player.level().getLevelData().isHardcore() && player.hasEffect(ModEffects.WELL_RESTED)) {
                    player.setHealth(1.0F);
                    player.removeEffect(ModEffects.WELL_RESTED);
                    player.invulnerableTime = 60;
                    player.displayClientMessage(Component.translatable("effect.pinferno.well_rested_hardcore"), true);
                    return false;
                }
            }
            return true;
        });

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            boolean hadWellRestedEffectOnDeath = TempInventoryStorage.restorePlayerInventory(newPlayer);

            // don't reset heat on death unless used respawn anchor
            if (oldPlayer.getEntityData().get(ModEntityData.PLAYER_HEAT) >= 100 && newPlayer.level().dimension() != Level.NETHER) {
                newPlayer.getEntityData().set(ModEntityData.PLAYER_HEAT, 99);
            }
            else if (newPlayer.level().dimension() == Level.NETHER) {
                newPlayer.getEntityData().set(ModEntityData.PLAYER_HEAT, 0);
            }
            else {
                newPlayer.getEntityData().set(ModEntityData.PLAYER_HEAT, oldPlayer.getEntityData().get(ModEntityData.PLAYER_HEAT));
            }

            // display message if player had well rested effect
            if (hadWellRestedEffectOnDeath && !Services.PLATFORM.isModLoaded("pblizzard") && !oldPlayer.level().getLevelData().isHardcore() && !newPlayer.level().getLevelData().isHardcore()) {
                Objects.requireNonNull(newPlayer.getServer()).execute(() -> newPlayer.sendSystemMessage(Component.translatable("effect.pinferno.well_rested_consume"), true));
            }

            if (!alive) {
                newPlayer.setHealth(10.0F);
                newPlayer.getFoodData().setFoodLevel(10);
                newPlayer.getFoodData().setSaturation(5.0F);
            }
        });
    }

    public static void handleBlockPlace() {
        UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
            // check snow layer melting
            if (level.isClientSide()) {
                return InteractionResult.PASS;
            }

            ItemStack itemInHand = player.getItemInHand(hand);
            BlockPos targetPos = hitResult.getBlockPos();
            BlockState stateAtTargetPos = level.getBlockState(targetPos);

            if (itemInHand.is(Blocks.SNOW.asItem()) && stateAtTargetPos.is(Blocks.SNOW) && level.dimension() == Level.NETHER) {
                int currentLayers = stateAtTargetPos.getValue(SnowLayerBlock.LAYERS);
                int maxLayers = 8;

                if (currentLayers < maxLayers) {
                    PreservedInferno.cancelTaskAt(targetPos);
                    PreservedInferno.scheduleDelayedTask(
                            new SimpleBlockTransformationTask(
                                    PreservedInferno.INSTANCE,
                                    (ServerLevel) level,
                                    targetPos,
                                    Blocks.AIR.defaultBlockState(),
                                    Blocks.SNOW,
                                    200
                            )
                    );
                }
                return InteractionResult.PASS;
            }
            return InteractionResult.PASS;
        });
    }

    public static void checkBreakFullyGrownCrop() {
        PlayerBlockBreakEvents.AFTER.register((level, player, pos, state, blockEntity) -> {
            if (level.isClientSide()) {
                return;
            }

            boolean isCrop = state.is(Blocks.WHEAT) ||
                    state.is(Blocks.CARROTS) ||
                    state.is(Blocks.POTATOES) ||
                    state.is(Blocks.BEETROOTS) ||
                    state.is(Blocks.NETHER_WART);

            if (isCrop) {
                boolean isFullyGrown = false;
                ItemStack mainHandItem = player.getMainHandItem();

                if (state.getBlock() instanceof CropBlock cropBlock) {
                    isFullyGrown = cropBlock.isMaxAge(state);
                }
                else if (state.getBlock() instanceof NetherWartBlock) {
                    isFullyGrown = state.hasProperty(NetherWartBlock.AGE) && state.getValue(NetherWartBlock.AGE) == NetherWartBlock.MAX_AGE;
                }

                if (isFullyGrown && mainHandItem.is(ItemTags.HOES)) {
                    ModTriggers.BREAK_GROWN_CROP.trigger((ServerPlayer) player);
                }
            }
        });
    }

    private static void keyPressForFirstAdvancement() {
        ServerPlayNetworking.registerGlobalReceiver(OpenAdvancementPayload.ID, (payload, context) -> ModTriggers.OPENED_ADVANCEMENT_SCREEN.trigger(context.player())
        );
    }

    public static void hardcoreSetup() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof ServerPlayer player) {
                if (world.getLevelData().isHardcore()) {
                    AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
                    if (maxHealth != null && maxHealth.getBaseValue() != 10.0) {
                        maxHealth.setBaseValue(10.0);
                    }
                }
            }
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayer player = handler.getPlayer();

            if (!server.isHardcore()) return;

            if (!player.getEntityData().get(ModEntityData.PLAYER_HUNGER_INITIALIZED)) {
                player.getFoodData().setFoodLevel(10);
                player.getEntityData().set(ModEntityData.PLAYER_HUNGER_INITIALIZED, true);
            }
        });
    }

    public static void enableMinecartExperiment() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            ServerLevel overworld = server.overworld();
            PackRepository registries = server.getPackRepository();

            if (overworld.getGameRules().getRule(GameRules.RULE_MINECART_MAX_SPEED).get() != 64) {
                overworld.getGameRules().getRule(GameRules.RULE_MINECART_MAX_SPEED).set(64, server);
            }

            boolean isEnabled = registries.getSelectedPacks().stream().anyMatch(pack -> pack.getId().equals("minecart_improvements"));
            if (!isEnabled) {
                registries.addPack("minecart_improvements");
            }
        });
    }

    public static void registerModEvents() {
        // Constants.LOG.info("Registering Fabric Mod Events for " + Constants.MOD_ID);
        checkInitialAdvancement();
        limitCropBreak();
        modifySleeping();
        handleEntityDeath();
        handleBlockPlace();
        checkBreakFullyGrownCrop();
        keyPressForFirstAdvancement();
        hardcoreSetup();
        enableMinecartExperiment();
    }
}
