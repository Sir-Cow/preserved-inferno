package sircow.preservedinferno.mixin;

import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Spider.class)
public abstract class SpiderMixin {
    @Inject(method = "finalizeSpawn", at = @At("RETURN"))
    private void preserved_inferno$modifySpiderSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason reason, SpawnGroupData data, CallbackInfoReturnable<SpawnGroupData> cir) {
        Spider self = (Spider) (Object) this;
        RandomSource random = level.getRandom();
        switch (reason) {
            case NATURAL, CHUNK_GENERATION -> {}
            default -> { return; }
        }

        Holder<MobEffect> effect = chooseEffect(random);
        int amplifier = 0;

        if (random.nextFloat() < 0.10F * difficulty.getSpecialMultiplier()) {
            amplifier = 1;
        }

        // convert to cave spider
        if (!(self instanceof CaveSpider) && random.nextFloat() < -(self.getY() * 0.01F)) {
            CaveSpider caveSpider = new CaveSpider(EntityType.CAVE_SPIDER, self.level());
            caveSpider.setPos(self.getX(), self.getY(), self.getZ());
            caveSpider.setYRot(self.getYRot());
            caveSpider.setXRot(self.getXRot());
            caveSpider.finalizeSpawn(level, difficulty, reason, null);
            caveSpider.addEffect(new MobEffectInstance(effect, -1, amplifier));
            self.discard();
            level.addFreshEntity(caveSpider);
        }

        self.addEffect(new MobEffectInstance(effect, -1, amplifier));
    }

    @Unique
    private Holder<MobEffect> chooseEffect(RandomSource random) {
        int i = random.nextInt(4);
        return switch (i) {
            case 0 -> MobEffects.SPEED;
            case 1 -> MobEffects.STRENGTH;
            case 2 -> MobEffects.RESISTANCE;
            default -> MobEffects.INVISIBILITY;
        };
    }
}
