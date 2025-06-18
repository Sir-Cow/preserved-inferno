package sircow.preservedinferno.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractThrownPotion;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
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
    public void preserved_inferno$waterThrow(ServerLevel level, CallbackInfo ci) {
        AbstractThrownPotion thisAsPotion = (AbstractThrownPotion) (Object) this;
        AABB aABB = thisAsPotion.getBoundingBox().inflate(4.0, 2.0, 4.0);
        hitPlayers.clear();

        for (LivingEntity livingEntity : level.getEntitiesOfClass(LivingEntity.class, aABB, PLAYER)) {
            if (livingEntity instanceof Player player) {
                int currentHeat = ((HeatAccessor) player).preserved_inferno$getHeat();
                if (!hitPlayers.contains(player)) {
                    if (currentHeat >= HEAT_MODIFIER) {
                        ((HeatAccessor) player).preserved_inferno$decreaseHeat(HEAT_MODIFIER);
                    }
                    else if (currentHeat < HEAT_MODIFIER && currentHeat > 0) {
                        ((HeatAccessor) player).preserved_inferno$setHeat(0);
                    }
                    hitPlayers.add(player);
                }
            }
        }
    }

    @Inject(
            method = "onHit",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/projectile/AbstractThrownPotion;onHitAsPotion(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/phys/HitResult;)V",
                    shift = At.Shift.BEFORE
            )
    )
    public void preserved_inferno$beforePotionHit(HitResult result, CallbackInfo ci) {
        AbstractThrownPotion thisAsPotion = (AbstractThrownPotion) (Object) this;
        AABB aABB = thisAsPotion.getBoundingBox().inflate(4.0, 2.0, 4.0);
        hitPlayers.clear();

        for (LivingEntity livingEntity : this.level().getEntitiesOfClass(LivingEntity.class, aABB, PLAYER)) {
            if (livingEntity instanceof Player player) {
                int currentHeat = ((HeatAccessor) player).preserved_inferno$getHeat();
                if (!hitPlayers.contains(player)) {
                    if (currentHeat >= HEAT_MODIFIER) {
                        ((HeatAccessor) player).preserved_inferno$decreaseHeat(HEAT_MODIFIER);
                    }
                    else if (currentHeat < HEAT_MODIFIER && currentHeat > 0) {
                        ((HeatAccessor) player).preserved_inferno$setHeat(0);
                    }
                    hitPlayers.add(player);
                }
            }
        }
    }
}
