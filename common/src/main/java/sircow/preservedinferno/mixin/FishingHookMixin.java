package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.components.ModComponents;
import sircow.preservedinferno.other.ModTags;

import java.util.Objects;
import java.util.Random;

@Mixin(FishingHook.class)
public abstract class FishingHookMixin {
    @Shadow @Mutable @Final private int luck;
    @Shadow @Mutable @Final private int lureSpeed;
    @Unique private boolean lureSpeedModified = false;
    @Shadow public abstract @Nullable Player getPlayerOwner();

    // hook effect
    @Inject(method = "catchingFish", at = @At("HEAD"))
    private void addLureSpeed(BlockPos pos, CallbackInfo ci) {
        if (!lureSpeedModified) {
            Player owner = this.getPlayerOwner();
            if (owner != null) {
                if (Objects.equals(owner.getMainHandItem().get(ModComponents.HOOK_COMPONENT), "iron")) {
                    this.lureSpeed += 100;
                    lureSpeedModified = true;
                }
                if (Objects.equals(owner.getMainHandItem().get(ModComponents.HOOK_COMPONENT), "diamond")) {
                    this.lureSpeed += 200;
                    lureSpeedModified = true;
                }
                if (Objects.equals(owner.getMainHandItem().get(ModComponents.HOOK_COMPONENT), "netherite")) {
                    this.lureSpeed += 300;
                    lureSpeedModified = true;
                }
            }
        }
    }
    // line effect
    @ModifyArg(method = "retrieve(Lnet/minecraft/world/item/ItemStack;)I", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/item/ItemEntity;<init>(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)V",
            ordinal = 0), index = 4)
    private ItemStack addFortune(ItemStack originalStack) {
        boolean doFortune = false;
        double randomNum = new Random().nextDouble();
        Player owner = this.getPlayerOwner();
        if (owner != null && (originalStack.is(ModTags.FISHING_LOOT_FISH) || originalStack.is(ModTags.FISHING_LOOT_VARIETY) || originalStack.is(ModTags.FISHING_LOOT_JUNK))) {
            if (Objects.equals(owner.getMainHandItem().get(ModComponents.LINE_COMPONENT), "iron")) {
                double chance = 1.0 - (2.0 / (1.0 + 2.0));
                if (randomNum < chance) {
                    doFortune = true;
                }
            }
            if (Objects.equals(owner.getMainHandItem().get(ModComponents.LINE_COMPONENT), "diamond")) {
                double chance = 1.0 - (2.0 / (2.0 + 2.0));
                if (randomNum < chance) {
                    doFortune = true;
                }
            }
            if (Objects.equals(owner.getMainHandItem().get(ModComponents.LINE_COMPONENT), "netherite")) {
                double chance = 1.0 - (2.0 / (3.0 + 2.0));
                if (randomNum < chance) {
                    doFortune = true;
                }
            }
        }

        if (originalStack != null && !originalStack.isEmpty() && doFortune) {
            ItemStack fortuneStack = originalStack.copy();
            fortuneStack.grow(originalStack.getCount());
            return fortuneStack;
        }
        return originalStack;
    }
    // sinker effect
    @Inject(method = "retrieve", at = @At("HEAD"))
    public void addLuck(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        Player owner = this.getPlayerOwner();
        if (owner != null) {
            if (Objects.equals(owner.getMainHandItem().get(ModComponents.SINKER_COMPONENT), "iron")) {
                this.luck += 1;
            }
            if (Objects.equals(owner.getMainHandItem().get(ModComponents.SINKER_COMPONENT), "diamond")) {
                this.luck += 2;
            }
            if (Objects.equals(owner.getMainHandItem().get(ModComponents.SINKER_COMPONENT), "netherite")) {
                this.luck += 3;
            }
        }
    }
}
