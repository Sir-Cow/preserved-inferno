package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.other.BabyHealthHelper;

@Mixin(AgeableMob.class)
public abstract class AgeableMobMixin extends PathfinderMob {
    @Shadow public abstract int getAge();

    @Unique private int prevAge;

    protected AgeableMobMixin(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    @Inject(method = "setAge", at = @At("HEAD"))
    private void pinferno$storePrevAge(int newAge, CallbackInfo ci) {
        if (!this.level().isClientSide()) this.prevAge = this.getAge();
    }

    @Inject(method = "setAge", at = @At("TAIL"))
    private void pinferno$onAgeChanged(int newAge, CallbackInfo ci) {
        if ((this.prevAge < 0) == (this.getAge() < 0)) return;

        BabyHealthHelper.updateBabyHealth(this, this.getAge() < 0);
    }
}
