package sircow.preservedinferno.other;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.CreakingHeartState;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.PreservedInferno;
import sircow.preservedinferno.block.ModBlocks;
import sircow.preservedinferno.block.custom.SparklingBlackstoneBlock;
import sircow.preservedinferno.components.ModComponents;
import sircow.preservedinferno.effect.ModEffects;
import sircow.preservedinferno.enchantment.ModEnchantments;
import sircow.preservedinferno.item.ModItems;
import sircow.preservedinferno.network.BashfulPayload;
import sircow.preservedinferno.network.ModMessages;
import sircow.preservedinferno.screen.PreservedFletchingTableMenu;
import sircow.preservedinferno.trigger.ModTriggers;

import java.net.URI;
import java.util.*;

public class FabricModEvents {
    private static final long REGEN_COOLDOWN_MS = 10 * 60 * 1000;
    private static boolean updateCheckScheduled;
    private static MinecraftServer currentServer;

    public static void limitCropBreak() {
        LootTableEvents.MODIFY_DROPS.register((entry, context, drops) -> {
            if (context.hasParameter(LootContextParams.BLOCK_STATE) &&
                    context.hasParameter(LootContextParams.TOOL) &&
                    context.hasParameter(LootContextParams.THIS_ENTITY)) {
                BlockState brokenState = context.getParameter(LootContextParams.BLOCK_STATE);
                ItemInstance toolUsed = context.getParameter(LootContextParams.TOOL);
                Player playerEntity = null;

                if (context.getOptionalParameter(LootContextParams.THIS_ENTITY) instanceof Player) playerEntity = (Player) context.getOptionalParameter(LootContextParams.THIS_ENTITY);

                if (playerEntity != null) {
                    if (playerEntity.isCreative()) return;

                    if (brokenState.is(Blocks.WHEAT) ||
                            brokenState.is(Blocks.CARROTS) ||
                            brokenState.is(Blocks.POTATOES) ||
                            brokenState.is(Blocks.BEETROOTS) ||
                            brokenState.is(Blocks.PUMPKIN_STEM) ||
                            brokenState.is(Blocks.MELON_STEM) ||
                            brokenState.is(Blocks.NETHER_WART)) {

                        if (!toolUsed.is(ItemTags.HOES)) drops.clear();
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
            float moonAngle = player.level().environmentAttributes().getValue(EnvironmentAttributes.MOON_ANGLE, new Vec3(player.getX(), player.getY(), player.getZ()), null);
            boolean moonVisible = moonAngle > 0.0F;

            if (MobLineOfSight.hasMonsterLineOfSight(player.level(), pos)) {
                player.sendOverlayMessage(Component.translatable("block.minecraft.bed.not_safe"));
                return Player.BedSleepingProblem.OTHER_PROBLEM;
            }

            if (!holdingDreamcatcher) {
                player.sendOverlayMessage(Component.translatable("block.minecraft.bed.no_dreamcatcher"));
                return Player.BedSleepingProblem.OTHER_PROBLEM;
            }

            if (moonVisible) return null;
            return null;
        });

        EntitySleepEvents.START_SLEEPING.register((entity, sleepingPos) -> {
            if (entity instanceof Player player) {
                ItemStack main = player.getMainHandItem();
                ItemStack off = player.getOffhandItem();

                if (main.is(ModItems.DREAMCATCHER)) main.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                else if (off.is(ModItems.DREAMCATCHER)) off.hurtAndBreak(1, player, EquipmentSlot.OFFHAND);
            }
        });

        EntitySleepEvents.STOP_SLEEPING.register((entity, sleepingPos) -> {
            if (entity instanceof Player player) {
                if (player.getSleepTimer() > 20) {
                    MinecraftServer server = player.level().getServer();
                    if (server != null) {
                        for (ServerPlayer serverPlayer : server.getPlayerList().getPlayers()) {
                            serverPlayer.addEffect(new MobEffectInstance(ModEffects.WELL_RESTED.holder, 24000, 0, false, false, true));
                            if (player.getUUID() == serverPlayer.getUUID()) serverPlayer.sendOverlayMessage(Component.translatable("effect.pinferno.well_rested_awake"));
                            else serverPlayer.sendOverlayMessage(Component.translatable("effect.pinferno.well_rested_awake_not_sleeping", player.getName()));
                        }
                    }
                }
            }
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayer player = handler.player;
            if (player.getEntityData().get(ModEntityData.PLAYER_HARDCORE_REGEN_COOLDOWN) == 0) player.getEntityData().set(ModEntityData.PLAYER_HARDCORE_REGEN_COOLDOWN, System.currentTimeMillis() - REGEN_COOLDOWN_MS);
        });
    }

    public static void handleEntityDeath() {
        ServerLivingEntityEvents.AFTER_DEATH.register((livingEntity, damageSource) -> {
            // reset shield cooldown
            if (livingEntity instanceof Player player) ShieldStaminaHandler.playerShieldCooldownMap.remove(player.getUUID());
        });

        ServerLivingEntityEvents.ALLOW_DEATH.register((livingEntity, damageSource, damageAmount) -> {
            if (livingEntity instanceof Player player) TempInventoryStorage.savePlayerInventory(player);
            // hardcore
            if (livingEntity instanceof ServerPlayer player) {
                if (player.level().getLevelData().isHardcore() && player.hasEffect(ModEffects.WELL_RESTED.holder)) {
                    player.setHealth(1.0F);
                    player.removeEffect(ModEffects.WELL_RESTED.holder);
                    player.invulnerableTime = 60;
                    player.sendOverlayMessage(Component.translatable("effect.pinferno.well_rested_hardcore"));
                    return false;
                }
            }
            return true;
        });

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            boolean hadWellRestedEffectOnDeath = TempInventoryStorage.restorePlayerInventory(newPlayer);
            int oldHeat = oldPlayer.getEntityData().get(ModEntityData.PLAYER_HEAT);
            int resultHeat = oldHeat / 2;

            if (oldHeat >= 200) resultHeat = 99;
            if (newPlayer.level().dimension() == Level.NETHER) resultHeat = Math.max(resultHeat, 1);
            else resultHeat = Math.max(resultHeat, 0);

            // don't reset heat on death unless used respawn anchor
            if (oldPlayer.getEntityData().get(ModEntityData.PLAYER_HEAT) >= 100 && newPlayer.level().dimension() != Level.NETHER) newPlayer.getEntityData().set(ModEntityData.PLAYER_HEAT, resultHeat);
            else if (newPlayer.level().dimension() == Level.NETHER) newPlayer.getEntityData().set(ModEntityData.PLAYER_HEAT, resultHeat);
            else newPlayer.getEntityData().set(ModEntityData.PLAYER_HEAT, resultHeat);

            // display message if player had well rested effect
            if (hadWellRestedEffectOnDeath  && !oldPlayer.level().getLevelData().isHardcore() && !newPlayer.level().getLevelData().isHardcore()) {
                Objects.requireNonNull(newPlayer.level().getServer()).execute(() -> newPlayer.sendSystemMessage(Component.translatable("effect.pinferno.well_rested_consume"), true));
            }

            if (!alive) {
                int oldFood = oldPlayer.getFoodData().getFoodLevel();
                float saturation;

                ServerPlayer.RespawnConfig respawnConfig = newPlayer.getRespawnConfig();
                boolean isBedSpawn = false;

                if (respawnConfig != null) isBedSpawn = newPlayer.level().getBlockState(respawnConfig.respawnData().pos()).getBlock() instanceof BedBlock;

                if (isBedSpawn) {
                    newPlayer.setHealth(16.0F);

                    if (oldFood < 16) {
                        int newFood = Math.max(oldFood, 10);
                        newPlayer.getFoodData().setFoodLevel(newFood);

                        if (oldFood < 10) saturation = 1.0F;
                        else saturation = 6.0F;
                    }
                    else {
                        newPlayer.getFoodData().setFoodLevel(16);
                        saturation = 6.0F;
                    }
                }
                else {
                    newPlayer.setHealth(12.0F);

                    if (oldFood < 12) {
                        int newFood = Math.max(oldFood, 6);
                        newPlayer.getFoodData().setFoodLevel(newFood);
                        saturation = 1.0F;
                    }
                    else {
                        newPlayer.getFoodData().setFoodLevel(12);
                        saturation = 6.0F;
                    }
                }
                newPlayer.getFoodData().setSaturation(saturation);
            }
        });
    }

    public static void handleBlockPlace() {
        UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
            // check snow layer melting
            if (level.isClientSide()) return InteractionResult.PASS;

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

    public static void checkBlockBreak() {
        PlayerBlockBreakEvents.AFTER.register((level, player, pos, state, blockEntity) -> {
            if (level.isClientSide() || player.isCreative()) return;

            ItemStack mainHandItem = player.getMainHandItem();

            boolean isCrop = state.is(Blocks.WHEAT) ||
                    state.is(Blocks.CARROTS) ||
                    state.is(Blocks.POTATOES) ||
                    state.is(Blocks.BEETROOTS) ||
                    state.is(Blocks.NETHER_WART);

            if (isCrop) {
                boolean isFullyGrown = false;

                if (state.getBlock() instanceof CropBlock cropBlock) isFullyGrown = cropBlock.isMaxAge(state);
                else if (state.getBlock() instanceof NetherWartBlock) isFullyGrown = state.hasProperty(NetherWartBlock.AGE) && state.getValue(NetherWartBlock.AGE) == NetherWartBlock.MAX_AGE;
                if (isFullyGrown && mainHandItem.is(ItemTags.HOES)) ModTriggers.BREAK_GROWN_CROP.get().trigger((ServerPlayer) player);
            }

            if (mainHandItem.is(ItemTags.HOES) && (state.is(Blocks.SHORT_GRASS) || state.is(Blocks.TALL_GRASS))) mainHandItem.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
            if (state.is(Blocks.SCULK_SHRIEKER)) ModTriggers.BREAK_SCULK_SHRIEKER.get().trigger((ServerPlayer) player);
            if (state.is(Blocks.CREAKING_HEART) && state.getValue(BlockStateProperties.CREAKING_HEART_STATE) == CreakingHeartState.AWAKE) ModTriggers.BREAK_CREAKING_HEART.get().trigger((ServerPlayer) player);
            if (state.is(ModBlocks.SPARKLING_BLACKSTONE) && state.getValue(SparklingBlackstoneBlock.STAGE) == 4) ModTriggers.BREAK_SPARKLING_BLACKSTONE.get().trigger((ServerPlayer) player);

        });
    }

    private static void keyPressForFirstAdvancement() {
        ServerPlayNetworking.registerGlobalReceiver(OpenAdvancementPayload.ID, (payload, context) -> ModTriggers.OPENED_ADVANCEMENT_SCREEN.get().trigger(context.player())
        );
    }

    public static void hardcoreSetup() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayer player = handler.getPlayer();

            if (!server.isHardcore()) return;

            if (!player.getEntityData().get(ModEntityData.PLAYER_HUNGER_INITIALIZED)) {
                player.getFoodData().setFoodLevel(10);
                player.setHealth(10.0F);
                player.getEntityData().set(ModEntityData.PLAYER_HUNGER_INITIALIZED, true);
            }
            // patch max health for existing hardcore worlds
            if (player.getEntityData().get(ModEntityData.RESET_HARDCORE_HEALTH)) {
                if (Objects.requireNonNull(player.getAttribute(Attributes.MAX_HEALTH)).getBaseValue() != 20.0) Objects.requireNonNull(player.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(20.0);
                player.getEntityData().set(ModEntityData.RESET_HARDCORE_HEALTH, false);
            }
        });
    }

    public static void enableMinecartExperiment() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            ServerLevel overworld = server.overworld();
            PackRepository registries = server.getPackRepository();

            if (overworld.getGameRules().get(GameRules.MAX_MINECART_SPEED) != 32) overworld.getGameRules().set(GameRules.MAX_MINECART_SPEED, 32, server);

            boolean isEnabled = registries.getSelectedPacks().stream().anyMatch(pack -> pack.getId().equals("minecart_improvements"));
            if (!isEnabled) registries.addPack("minecart_improvements");
        });
    }

    @SuppressWarnings("rawtypes")
    public static void openFletchingTable() {
        UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
            BlockPos pos = hitResult.getBlockPos();

            if (hand == InteractionHand.MAIN_HAND && level.getBlockState(hitResult.getBlockPos()).is(Blocks.FLETCHING_TABLE)) {
                if (player.isCrouching()) return InteractionResult.PASS;

                if (!level.isClientSide()) {
                    player.openMenu(new ExtendedMenuProvider() {
                        @Override
                        public @NotNull AbstractContainerMenu createMenu(int syncId, @NonNull Inventory playerInventory, @NonNull Player player) {
                            return new PreservedFletchingTableMenu(syncId, playerInventory, ContainerLevelAccess.create(level, pos));
                        }

                        @Override
                        public @NotNull Component getDisplayName() {
                            return Component.translatable("block.minecraft.fletching_table");
                        }

                        @Override
                        public Object getScreenOpeningData(@NonNull ServerPlayer serverPlayer) {
                            boolean isEmpty = level.getBlockEntity(pos) == null;
                            return new PreservedInferno.BlockData(isEmpty);
                        }
                    });

                    player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
                    return InteractionResult.CONSUME;
                }
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        });
    }

    public static void afterDeath() {
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
            if (source.getEntity() instanceof ServerPlayer serverPlayer) {
                KillTracker.onEntityKilled(serverPlayer, entity, source);
                if (serverPlayer.getOffhandItem().is(ModTags.SHIELDS) && serverPlayer.getOffhandItem().getEnchantments().getLevel(serverPlayer.level().registryAccess().lookupOrThrow(ModEnchantments.RESPITE.registryKey()).getOrThrow(ModEnchantments.RESPITE)) > 0) {
                    int currentDuration = serverPlayer.getEntityData().get(ModEntityData.PLAYER_SHIELD_REGEN_DURATION);
                    serverPlayer.getEntityData().set(ModEntityData.PLAYER_SHIELD_REGEN_DURATION, currentDuration + 20);
                }
            }
        });
    }

    public static void staminaRegenTick() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                int remainingTicks = player.getEntityData().get(ModEntityData.PLAYER_SHIELD_REGEN_DURATION);

                if (remainingTicks > 0) {
                    ItemStack shield = player.getOffhandItem();
                    if (shield.is(ModTags.SHIELDS)) {
                        float currentStamina = player.getEntityData().get(ModEntityData.PLAYER_SHIELD_STAMINA);
                        float maxStamina = getEffectiveMaxStamina(shield, player.level().registryAccess());
                        float baseRegen = getEffectiveRegenRate(shield, player.level().registryAccess());
                        float totalRegen = baseRegen + 0.25F;

                        if (currentStamina < maxStamina) {
                            player.getEntityData().set(ModEntityData.PLAYER_SHIELD_STAMINA, Math.min(maxStamina, currentStamina + totalRegen));
                        }
                    }
                    player.getEntityData().set(ModEntityData.PLAYER_SHIELD_REGEN_DURATION, remainingTicks - 1);
                }
            }
        });
    }

    public static float getEffectiveMaxStamina(ItemStack stack, RegistryAccess registryAccess) {
        return stack.getOrDefault(ModComponents.SHIELD_MAX_STAMINA_COMPONENT, 100).floatValue() + (stack.getEnchantments().getLevel(registryAccess.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ModEnchantments.ENDURANCE)) * 4.0F);
    }

    public static float getEffectiveRegenRate(ItemStack stack, RegistryAccess registryAccess) {
        float baseRate = stack.getOrDefault(ModComponents.SHIELD_REGEN_RATE_COMPONENT, 0.1f);
        int vigorLevel = stack.getEnchantments().getLevel(registryAccess.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ModEnchantments.VIGOR));

        return baseRate * (1.0f + (vigorLevel * 0.5f));
    }

    private static void bashfulEnchant() {
        ServerPlayNetworking.registerGlobalReceiver(BashfulPayload.TYPE, (payload, context) -> context.server().execute(() -> {
                    ServerPlayer player = context.player();
                    ItemStack shield = player.getUseItem();

                    if (!player.isUsingItem()) return;
                    if (!shield.is(ModTags.SHIELDS)) return;

                    int level = EnchantmentHelper.getItemEnchantmentLevel(player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ModEnchantments.BASHFUL), shield);

                    if (level <= 0) return;
                    if (player.isPassenger() || player.isFallFlying() || player.isInWater()) return;

                    float currentStamina = player.getEntityData().get(ModEntityData.PLAYER_SHIELD_STAMINA);
                    float cost = 4.0F;

                    if (currentStamina < cost) return;

                    if (!player.isCreative() && !player.isSpectator()) {
                        float newStamina = Math.max(0.0F, currentStamina - cost);
                        player.getEntityData().set(ModEntityData.PLAYER_SHIELD_STAMINA, newStamina);

                        if (newStamina <= 0.0F && currentStamina > 0.0F) ShieldStaminaHandler.triggerCooldown(player, shield);
                        ShieldStaminaHandler.triggerRegenBlock(player, 40);
                        player.causeFoodExhaustion(4.0F * level);
                        shield.hurtAndBreak(1, player, EquipmentSlot.OFFHAND);
                    }

                    player.getCooldowns().addCooldown(shield, 20);
                    player.stopUsingItem();
                    player.hurtMarked = true;

                    float magnitude = 1.374F + (0.458F * (level - 1));
                    Vec3 look = player.getLookAngle();
                    player.setDeltaMovement(new Vec3(look.x * magnitude, 0.0, look.z * magnitude));

                    player.level().playSound(
                            null,
                            player.getX(), player.getY(), player.getZ(),
                            SoundEvents.PLAYER_ATTACK_NODAMAGE,
                            SoundSource.PLAYERS,
                            1.0F,
                            1.0F
                    );
                })
        );
    }

    public static void updateCheck() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
                if (!updateCheckScheduled) {
                    updateCheckScheduled = true;

                    UpdateChecker.checkAsync(() -> {
                        client.execute(() -> {
                            if (client.player == null) return;

                            String current = Constants.INSTANCE.getVersion();
                            String latest = UpdateChecker.getLatest();

                            if (latest == null) return;

                            if (UpdateChecker.hasUpdate()) {
                                ClickEvent click = new ClickEvent.OpenUrl(URI.create("https://modrinth.com/mod/preserved-inferno/version/" + latest));
                                HoverEvent hover = new HoverEvent.ShowText(Component.literal("Open version page"));
                                Style updateLink = Style.EMPTY
                                        .withClickEvent(click)
                                        .withHoverEvent(hover)
                                        .withUnderlined(true)
                                        .withColor(ChatFormatting.BLUE);

                                Component message = Component.literal("[Preserved: Inferno]").withStyle(ChatFormatting.RED)
                                        .append(Component.literal(" Update available: ").withStyle(ChatFormatting.WHITE))
                                        .append(Component.literal(latest).setStyle(updateLink))
                                        .append(Component.literal(" (current: " + current + ")").withStyle(ChatFormatting.WHITE));

                                client.player.sendSystemMessage(message);
                            }
                        });
                    });
                }
            });
        }
    }

    // masteries
    private static String getPlayerRankId(UUID playerUuid) {
        if (currentServer == null) {
            Constants.LOG.warn("currentServer is null when trying to get player rank for {}", playerUuid);
            return "";
        }
        return WorldDataManager.getPlayerRank(currentServer, playerUuid);
    }

    private static Component getPrefixForRank(String rankId) {
        return WorldDataManager.RANK_PREFIXES.getOrDefault(rankId, Component.empty());
    }

    private static Component getSuffixForRank(String rankId) {
        return WorldDataManager.RANK_SUFFIXES.getOrDefault(rankId, Component.empty());
    }

    private static void createOrUpdateAllRankTeams() {
        if (currentServer == null) return;

        Scoreboard scoreboard = currentServer.getScoreboard();

        String[] rankIds = {"starter", "beginner", "novice", "disciple", "adequate", "advanced", "master", "champion", "infernal", "placeholder"};
        for (String rankId : rankIds) {
            PlayerTeam playerTeam = scoreboard.getPlayerTeam(rankId);

            if (playerTeam == null) {
                playerTeam = scoreboard.addPlayerTeam(rankId);
            }

            playerTeam.setPlayerPrefix(getPrefixForRank(rankId));
            playerTeam.setPlayerSuffix(getSuffixForRank(rankId));
        }
    }

    public static void assignPlayerToRankTeam(ServerPlayer player) {
        if (currentServer == null) return;

        Scoreboard scoreboard = currentServer.getScoreboard();
        String playerRankId = getPlayerRankId(player.getUUID());
        String scoreboardEntry = player.getScoreboardName();
        PlayerTeam currentTeam = scoreboard.getPlayersTeam(scoreboardEntry);
        if (currentTeam != null && !currentTeam.getName().equals(playerRankId)) {
            scoreboard.removePlayerFromTeam(scoreboardEntry);
        }

        PlayerTeam targetTeam = scoreboard.getPlayerTeam(playerRankId);
        if (targetTeam == null) {
            targetTeam = scoreboard.addPlayerTeam(playerRankId);
            targetTeam.setPlayerPrefix(getPrefixForRank(playerRankId));
            targetTeam.setPlayerSuffix(getSuffixForRank(playerRankId));
        }

        if (!targetTeam.getPlayers().contains(scoreboardEntry)) {
            scoreboard.addPlayerToTeam(scoreboardEntry, targetTeam);
        }
    }


    public static void initialiseMasteries() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            currentServer = server;
            createOrUpdateAllRankTeams();
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                FabricWorldDataManager.syncPlayerPointsWithAdvancements(server, player);
            }
        });

        ServerLifecycleEvents.SERVER_STOPPED.register(server -> currentServer = null);

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayer player = handler.player;
            ModTriggers.WORLD_JOIN.get().trigger(player);
            assignPlayerToRankTeam(player);
            FabricWorldDataManager.syncPlayerPointsWithAdvancements(server, player);

            int currentPoints = WorldDataManager.getPlayerPoints(server, player.getUUID());
            ServerPlayNetworking.send(player, new ModMessages.PlayerPointsPayload(player.getUUID(), currentPoints));
        });
    }

    public static void checkInitialAdvancement() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                var advancement = server.getAdvancements().get(Identifier.withDefaultNamespace("story/root"));
                if (advancement == null) continue;
                if (player.getAdvancements().getOrStartProgress(advancement).isDone()) {
                    ModTriggers.WORLD_JOIN.get().trigger(player);
                }
            }
        });
    }

    public static void registerModEvents() {
        // Constants.LOG.info("Registering Fabric Mod Events for " + Constants.MOD_ID);
        initialiseMasteries();
        checkInitialAdvancement();
        limitCropBreak();
        modifySleeping();
        handleEntityDeath();
        handleBlockPlace();
        checkBlockBreak();
        keyPressForFirstAdvancement();
        hardcoreSetup();
        enableMinecartExperiment();
        openFletchingTable();
        afterDeath();
        staminaRegenTick();
        bashfulEnchant();
        updateCheck();
    }
}
