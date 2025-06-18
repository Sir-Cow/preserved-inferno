package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ConduitBlockEntity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.other.ModDamageTypes;

import java.util.List;

@Mixin(ConduitBlockEntity.class)
public class ConduitBlockEntityMixin {
    // extend conduit radius
    @ModifyConstant(method = "updateDestroyTarget", constant = @Constant(doubleValue = 8.0F))
    private static double preserved_inferno$modifyDoubleValue(double original) {
        return 16.0F;
    }
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
    @Redirect(method = "updateAndAttackTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private static boolean preserved_inferno$customConduitDamage(LivingEntity target, ServerLevel level, DamageSource originalSource, float amount) {
        Player player = level.players().getFirst();
        DamageSource customSource = ModDamageTypes.of(level, ModDamageTypes.CONDUIT, player);
        target.hurtServer(level, customSource, amount);
        return false;
    }
}