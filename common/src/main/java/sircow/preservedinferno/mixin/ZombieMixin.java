package sircow.preservedinferno.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Zombie.class)
public class ZombieMixin extends Monster {
    protected ZombieMixin(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    // modify armour value
    @ModifyArg(method = "createAttributes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;add(Lnet/minecraft/core/Holder;D)Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;", ordinal = 3), index = 1)
    private static double preserved_inferno$modifyArmour(double baseValue) {
        baseValue = 10.0F;
        return baseValue;
    }

    @Inject(method = "setBaby", at = @At("TAIL"))
    private void preserved_inferno$modifyBaby(boolean baby, CallbackInfo ci) {
        if (this.level().isClientSide()) return;

        AttributeInstance health = this.getAttribute(Attributes.MAX_HEALTH);
        if (health == null) return;

        if (baby) {
            health.setBaseValue(health.getBaseValue() * 0.75);
        }
        else {
            health.setBaseValue(health.getBaseValue());
        }
        this.setHealth(this.getMaxHealth());
    }

    @Inject(method = "killedEntity", at = @At("HEAD"), cancellable = true)
    private void alwaysConvertVillagers(ServerLevel level, LivingEntity entity, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        Zombie self = (Zombie) (Object) this;

        if (entity instanceof Villager villager) {
            boolean converted = self.convertVillagerToZombieVillager(level, villager);
            cir.setReturnValue(!converted);
        }
    }
}
