package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.InsideBlockEffectType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Consumer;

@Mixin(PowderSnowBlock.class)
public abstract class PowderSnowBlockMixin extends Block {
    public PowderSnowBlockMixin(Properties properties) {
        super(properties);
    }

    @Redirect(method = "entityInside", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/InsideBlockEffectApplier;runBefore" + "(Lnet/minecraft/world/entity/InsideBlockEffectType;" + "Ljava/util/function/Consumer;)V"))
    private void pinferno$blockExtinguishBreak(InsideBlockEffectApplier applier, InsideBlockEffectType type, Consumer<Entity> callback) {
        if (type == InsideBlockEffectType.EXTINGUISH) {
            return;
        }
        applier.runBefore(type, callback);
    }
}
