package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.level.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @Inject(method = "ignoreExplosion", at = @At("HEAD"), cancellable = true)
    private void pinferno$ignoreMobExplosions(Explosion explosion, CallbackInfoReturnable<Boolean> cir) {
        Entity direct = explosion.getDirectSourceEntity();

        if (direct instanceof Creeper) {
            cir.setReturnValue(true);
            return;
        }

        LivingEntity indirect = explosion.getIndirectSourceEntity();

        if (indirect instanceof Ghast || indirect instanceof WitherBoss) {
            cir.setReturnValue(true);
        }
    }
}
