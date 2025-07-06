package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ConduitBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.other.ModDamageTypes;
import sircow.preservedinferno.trigger.ModTriggers;

import java.util.List;

@Mixin(ConduitBlockEntity.class)
public class ConduitBlockEntityMixin {
    @Unique private boolean wasActive = false;
    @Unique private boolean wasFullyPowered = false;

    // extend conduit radius
    @ModifyConstant(method = "getDestroyRangeAABB", constant = @Constant(doubleValue = 8.0F))
    private static double preserved_inferno$modifyDoubleValueAgain(double original) {
        return 16.0F;
    }
    // damage speed
    @ModifyConstant(method = "clientTick", constant = @Constant(longValue = 40L))
    private static long preserved_inferno$modifyLongValue(long original) {
        return 1L;
    }
    @ModifyConstant(method = "serverTick", constant = @Constant(longValue = 40L))
    private static long preserved_inferno$modifyLongValueAgain(long original) {
        return 1L;
    }

    // remove in rain or water to grant effect
    @Inject(method = "applyEffects", at = @At("HEAD"), cancellable = true)
    private static void preserved_inferno$givePlayersEffects(Level level, BlockPos pos, List<BlockPos> positions, CallbackInfo ci) {
        int i = positions.size();
        int j = i / 7 * 16;
        int k = pos.getX();
        int l = pos.getY();
        int m = pos.getZ();
        AABB box = new AABB(k, l, m, k + 1, l + 1, m + 1)
                .inflate(j)
                .expandTowards(0.0, level.getHeight(), 0.0);
        List<Player> list = level.getEntitiesOfClass(Player.class, box);
        if (!list.isEmpty()) {
            for (Player playerEntity : list) {
                if (pos.closerThan(playerEntity.blockPosition(), j)) {
                    playerEntity.addEffect(new MobEffectInstance(MobEffects.CONDUIT_POWER, 260, 0, true, true));
                }
            }
        }
        ci.cancel();
    }

    // change magic damage to custom damage type which makes player-killed loot drop
    @Inject(method = "updateAndAttackTarget", at = @At("HEAD"), cancellable = true)
    private static void preserved_inferno$attackMultipleTargets(ServerLevel level, BlockPos pos, BlockState state, ConduitBlockEntity conduit, boolean canDestroy, CallbackInfo ci) {
        if (!canDestroy) {
            return;
        }

        AABB range = new AABB(pos).inflate(16.0);
        List<LivingEntity> targets = level.getEntitiesOfClass(
                LivingEntity.class,
                range,
                entity -> entity instanceof Enemy && entity.isInWaterOrRain()
        );

        if (!targets.isEmpty()) {
            Player fakeSource = level.players().isEmpty() ? null : level.players().getFirst();
            DamageSource source = ModDamageTypes.of(level, ModDamageTypes.CONDUIT, fakeSource);

            for (LivingEntity target : targets) {
                if (!target.isAlive()) continue;
                target.hurtServer(level, source, 4.0F);

                if (level.getGameTime() % 20L == 0L) {
                    level.playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.CONDUIT_ATTACK_TARGET, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            }
        }
        ci.cancel();
    }

    // award conduit advancements
    @Inject(method = "serverTick", at = @At("TAIL"))
    private static void preserved_inferno$onActivation(Level level, BlockPos pos, BlockState state, ConduitBlockEntity conduit, CallbackInfo ci) {
        if (level.isClientSide) return;

        ConduitBlockEntityMixin self = (ConduitBlockEntityMixin)(Object)conduit;
        if (self != null) {
            boolean wasActive = self.wasActive;
            boolean isActive = conduit.isActive();
            int structureSize = ((ConduitBlockEntityAccessor) conduit).getEffectBlocks().size();
            boolean isFullyPowered = structureSize >= 42;
            boolean wasFullyPowered = self.wasFullyPowered;

            // activating conduit
            if (!wasActive && isActive) {
                List<Player> nearbyPlayers = level.getEntitiesOfClass(Player.class, new AABB(pos).inflate(16));
                for (Player player : nearbyPlayers) {
                    if (player instanceof ServerPlayer serverPlayer) {
                        ModTriggers.CONDUIT_POWER.trigger(serverPlayer);
                    }
                }
            }

            // fully activated conduit
            if (!wasFullyPowered && isFullyPowered) {
                List<Player> nearbyPlayers = level.getEntitiesOfClass(Player.class, new AABB(pos).inflate(16));
                for (Player player : nearbyPlayers) {
                    if (player instanceof ServerPlayer serverPlayer) {
                        ModTriggers.CONDUIT_POWER_FULL.trigger(serverPlayer);
                    }
                }
            }

            self.wasActive = isActive;
            self.wasFullyPowered = isFullyPowered;
        }
    }
}
