package sircow.preservedinferno.mixin;

import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.monster.spider.CaveSpider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CaveSpider.class)
public class CaveSpiderMixin {
    @ModifyVariable(method = "doHurtTarget", at = @At(value = "STORE"), name = "poisonTime")
    private int pinferno$easyPoisonDuration(int poisonTime) {
        if (((CaveSpider) (Object) this).level().getDifficulty() == Difficulty.EASY) {
            poisonTime = 4;
        }
        return poisonTime;
    }
}
