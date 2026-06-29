package sircow.preservedinferno.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.throwableitemprojectile.AbstractThrownPotion;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.other.HeatAccessor;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

@Mixin(AbstractThrownPotion.class)
public abstract class AbstractThrownPotionMixin extends ThrowableItemProjectile  {
    @Unique private static final int HEAT_MODIFIER = 3;
    @Unique private static final Predicate<LivingEntity> PLAYER = livingEntity -> livingEntity instanceof Player;
    @Unique private final Set<Player> hitPlayers = new HashSet<>();

    public AbstractThrownPotionMixin(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "onHitAsWater", at = @At("HEAD"))
    public void pinferno$waterThrow(ServerLevel level, CallbackInfo ci) {
        AbstractThrownPotion potion = (AbstractThrownPotion) (Object) this;
        AABB box = potion.getBoundingBox().inflate(4.0, 2.0, 4.0);

        hitPlayers.clear();

        for (Player player : level.getEntitiesOfClass(Player.class, box, PLAYER)) {
            if (!hitPlayers.add(player)) continue;

            HeatAccessor heat = (HeatAccessor) player;
            int currentHeat = heat.pinferno$getHeat();

            if (currentHeat >= HEAT_MODIFIER) {
                heat.pinferno$decreaseHeat(HEAT_MODIFIER);
                continue;
            }

            if (currentHeat > 0) heat.pinferno$setHeat(level.dimension() == Level.NETHER ? 1 : 0);
        }
    }

    @Inject(method = "onHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/throwableitemprojectile/AbstractThrownPotion;onHitAsPotion(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/phys/HitResult;)V", shift = At.Shift.BEFORE))
    public void pinferno$beforePotionHit(HitResult hitResult, CallbackInfo ci) {
        AbstractThrownPotion potion = (AbstractThrownPotion) (Object) this;
        AABB box = potion.getBoundingBox().inflate(4.0, 2.0, 4.0);

        hitPlayers.clear();

        for (Player player : level().getEntitiesOfClass(Player.class, box, PLAYER)) {
            if (!hitPlayers.add(player)) continue;

            HeatAccessor heat = (HeatAccessor) player;
            int currentHeat = heat.pinferno$getHeat();

            if (currentHeat >= HEAT_MODIFIER) {
                heat.pinferno$decreaseHeat(HEAT_MODIFIER);
                continue;
            }

            if (currentHeat > 0) heat.pinferno$setHeat(level().dimension() == Level.NETHER ? 1 : 0);
        }
    }
}
