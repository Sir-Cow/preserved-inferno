package sircow.preservedinferno.other;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.PreservedInferno;
import sircow.preservedinferno.effect.ModEffects;
import sircow.preservedinferno.item.ModItems;

import java.util.List;

public class FabricModEvents {
    private static final double MONSTER_DETECTION_RADIUS = 32.0;

    public static void modifySleeping() {
        // only allow sleeping if holding a dreamcatcher
        EntitySleepEvents.ALLOW_SLEEPING.register((Player player, BlockPos pos) -> {
            ItemStack mainHandItem = player.getItemInHand(InteractionHand.MAIN_HAND);
            ItemStack offHandItem = player.getItemInHand(InteractionHand.OFF_HAND);
            boolean holdingDreamcatcherInMainHand = mainHandItem.getItem() == ModItems.DREAMCATCHER;
            boolean holdingDreamcatcherInOffHand = offHandItem.getItem() == ModItems.DREAMCATCHER;

            if (holdingDreamcatcherInMainHand || holdingDreamcatcherInOffHand) {
                if (holdingDreamcatcherInMainHand) {
                    mainHandItem.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                }
                else {
                    offHandItem.hurtAndBreak(1, player, EquipmentSlot.OFFHAND);
                }

                if (hasMonsterLineOfSight(player.level(), pos)) {
                    player.displayClientMessage(Component.translatable("block.minecraft.bed.not_safe"), true);
                    return Player.BedSleepingProblem.OTHER_PROBLEM;
                }

                return null;
            }
            else {
                if (player.level().isMoonVisible()) {
                    player.displayClientMessage(Component.translatable("block.minecraft.bed.no_dreamcatcher"), true);
                }
                return Player.BedSleepingProblem.OTHER_PROBLEM;
            }
        });

        EntitySleepEvents.STOP_SLEEPING.register((entity, sleepingPos) -> {
            if (entity instanceof Player player) {
                if (player.getSleepTimer() > 20 && !player.level().isMoonVisible()) {
                    player.addEffect(new MobEffectInstance(ModEffects.WELL_RESTED, 12000, 0, false, false, true));
                    player.displayClientMessage(Component.translatable("effect.pinferno.well_rested_awake"), true);
                }
            }
        });
    }

    public static void handleEntityDeath() {
        ServerLivingEntityEvents.AFTER_DEATH.register((livingEntity, damageSource) -> {
            // prevent drowned dropping trident
            if (livingEntity instanceof Drowned drowned) {
                ItemStack heldItem = drowned.getItemInHand(InteractionHand.MAIN_HAND);
                if (heldItem.getItem() instanceof TridentItem) {
                    drowned.setDropChance(EquipmentSlot.MAINHAND, 0);
                }
            }
            // reset shield cooldown
            if (livingEntity instanceof Player player) {
                ShieldStaminaHandler.playerShieldCooldownMap.remove(player.getUUID());
            }
        });

        ServerPlayerEvents.ALLOW_DEATH.register((player, damageSource, damageAmount) -> {
            TempInventoryStorage.savePlayerInventory(player);
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
            if (hadWellRestedEffectOnDeath) {
                newPlayer.server.execute(() -> {
                    newPlayer.sendSystemMessage(Component.translatable("effect.pinferno.well_rested_consume"), true);
                });
            }

            newPlayer.setHealth(10.0F);
            newPlayer.getFoodData().setFoodLevel(10);
            newPlayer.getFoodData().setSaturation(10.0F);
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

    private static boolean hasMonsterLineOfSight(Level level, BlockPos bedPos) {
        AABB searchBox = new AABB(
                bedPos.getX() - MONSTER_DETECTION_RADIUS, bedPos.getY() - MONSTER_DETECTION_RADIUS, bedPos.getZ() - MONSTER_DETECTION_RADIUS,
                bedPos.getX() + MONSTER_DETECTION_RADIUS, bedPos.getY() + MONSTER_DETECTION_RADIUS, bedPos.getZ() + MONSTER_DETECTION_RADIUS
        );
        List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, searchBox, (entity) -> entity instanceof Monster);
        Vec3 bedVec = Vec3.atCenterOf(bedPos).add(0, 0.5, 0);

        for (LivingEntity entity : nearbyEntities) {
            if (entity instanceof Monster) {
                Vec3 monsterEyePos = entity.getEyePosition();
                HitResult hitResult = level.clip(
                        new ClipContext(
                                bedVec,
                                monsterEyePos,
                                ClipContext.Block.VISUAL,
                                ClipContext.Fluid.NONE,
                                entity
                        )
                );

                if (hitResult.getType() == HitResult.Type.MISS) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void registerModEvents() {
        Constants.LOG.info("Registering Fabric Mod Events for " + Constants.MOD_ID);
        modifySleeping();
        handleEntityDeath();
        handleBlockPlace();
    }
}
