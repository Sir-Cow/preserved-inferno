package sircow.preservedinferno.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import sircow.preservedinferno.components.ModComponents;
import sircow.preservedinferno.other.ModTags;
import sircow.preservedinferno.trigger.ModTriggers;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@SuppressWarnings("FieldCanBeLocal")
@Mixin(FishingHook.class)
public abstract class FishingHookMixin {
    @Unique private final int HOOK_SPEED_COPPER = 50;
    @Unique private final int HOOK_SPEED_PRISMARINE = 150;
    @Unique private final int HOOK_SPEED_IRON = 100;
    @Unique private final int HOOK_SPEED_GOLDEN = 300;
    @Unique private final int HOOK_SPEED_DIAMOND = 200;
    @Unique private final int HOOK_SPEED_NETHERITE = 300;
    @Unique private final double LINE_FORTUNE_COPPER = 0.5;
    @Unique private final double LINE_FORTUNE_PRISMARINE = 1.5;
    @Unique private final double LINE_FORTUNE_IRON = 1.0;
    @Unique private final double LINE_FORTUNE_GOLDEN = 3.0;
    @Unique private final double LINE_FORTUNE_DIAMOND = 2.0;
    @Unique private final double LINE_FORTUNE_NETHERITE = 3.0;
    @Unique private final float SINKER_LUCK_COPPER = 0.5F;
    @Unique private final float SINKER_LUCK_PRISMARINE = 1.5F;
    @Unique private final float SINKER_LUCK_IRON = 1.0F;
    @Unique private final float SINKER_LUCK_GOLDEN = 3.0F;
    @Unique private final float SINKER_LUCK_DIAMOND = 2.0F;
    @Unique private final float SINKER_LUCK_NETHERITE = 3.0F;

    @Shadow @Mutable @Final private int lureSpeed;
    @Unique private boolean lureSpeedModified = false;
    @Unique private ItemStack fishingWithStack = null;
    @Shadow public abstract @Nullable Player getPlayerOwner();

    // hook effect
    @Inject(method = "catchingFish", at = @At("HEAD"))
    private void preserved_inferno$addLureSpeed(BlockPos pos, CallbackInfo ci) {
        if (!lureSpeedModified) {
            Player owner = this.getPlayerOwner();

            if (owner != null) {
                ItemStack mainStack = owner.getMainHandItem();
                ItemStack offStack = owner.getOffhandItem();

                if (Boolean.TRUE.equals(mainStack.get(ModComponents.IS_FISHING))) {
                    fishingWithStack = mainStack;
                }
                if (Boolean.TRUE.equals(offStack.get(ModComponents.IS_FISHING))) {
                    fishingWithStack = offStack;
                }

                if (fishingWithStack != null) {
                    if (Objects.equals(fishingWithStack.get(ModComponents.HOOK_COMPONENT), "copper")) {
                        this.lureSpeed += HOOK_SPEED_COPPER;
                        lureSpeedModified = true;
                    }
                    if (Objects.equals(fishingWithStack.get(ModComponents.HOOK_COMPONENT), "prismarine")) {
                        this.lureSpeed += HOOK_SPEED_PRISMARINE;
                        lureSpeedModified = true;
                    }
                    if (Objects.equals(fishingWithStack.get(ModComponents.HOOK_COMPONENT), "iron")) {
                        this.lureSpeed += HOOK_SPEED_IRON;
                        lureSpeedModified = true;
                    }
                    if (Objects.equals(fishingWithStack.get(ModComponents.HOOK_COMPONENT), "golden")) {
                        this.lureSpeed += HOOK_SPEED_GOLDEN;
                        lureSpeedModified = true;
                    }
                    if (Objects.equals(fishingWithStack.get(ModComponents.HOOK_COMPONENT), "diamond")) {
                        this.lureSpeed += HOOK_SPEED_DIAMOND;
                        lureSpeedModified = true;
                    }
                    if (Objects.equals(fishingWithStack.get(ModComponents.HOOK_COMPONENT), "netherite")) {
                        this.lureSpeed += HOOK_SPEED_NETHERITE;
                        lureSpeedModified = true;
                    }
                    if (Objects.equals(fishingWithStack.get(ModComponents.HOOK_COMPONENT), "none")) {
                        lureSpeedModified = true;
                    }
                }
            }
        }
    }
    // line effect
    @ModifyArg(method = "retrieve(Lnet/minecraft/world/item/ItemStack;)I", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/item/ItemEntity;<init>(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)V",
            ordinal = 0), index = 4)
    private ItemStack preserved_inferno$addFortune(ItemStack originalStack) {
        boolean doFortune = false;
        double randomNum = new Random().nextDouble();
        double randomNum2 = new Random().nextDouble();
        double randomNum3 = new Random().nextDouble();
        int fortuneCounter = 0;
        Player owner = this.getPlayerOwner();

        if (owner != null) {
            if (fishingWithStack != null) {
                if (originalStack.is(ModTags.FISHING_LOOT_FISH) || originalStack.is(ModTags.FISHING_LOOT_VARIETY) || originalStack.is(ModTags.FISHING_LOOT_JUNK)) {
                    if (Objects.equals(fishingWithStack.get(ModComponents.LINE_COMPONENT), "copper")) {
                        double chance = 1.0 - (2.0 / (LINE_FORTUNE_COPPER + 2.0));
                        if (randomNum < chance) {
                            fortuneCounter += 1;
                            doFortune = true;
                        }
                    }
                    if (Objects.equals(fishingWithStack.get(ModComponents.LINE_COMPONENT), "prismarine")) {
                        double chance = 1.0 - (2.0 / (LINE_FORTUNE_PRISMARINE + 2.0));
                        if (randomNum < chance) {
                            fortuneCounter += 1;
                            doFortune = true;
                        }
                        if (randomNum2 < chance) {
                            fortuneCounter += 1;
                            doFortune = true;
                        }
                    }
                    if (Objects.equals(fishingWithStack.get(ModComponents.LINE_COMPONENT), "iron")) {
                        double chance = 1.0 - (2.0 / (LINE_FORTUNE_IRON + 2.0));
                        if (randomNum < chance) {
                            fortuneCounter += 1;
                            doFortune = true;
                        }
                    }
                    if (Objects.equals(fishingWithStack.get(ModComponents.LINE_COMPONENT), "golden")) {
                        double chance = 1.0 - (2.0 / (LINE_FORTUNE_GOLDEN + 2.0));
                        if (randomNum < chance) {
                            fortuneCounter += 1;
                            doFortune = true;
                        }
                        if (randomNum2 < chance) {
                            fortuneCounter += 1;
                            doFortune = true;
                        }
                        if (randomNum3 < chance) {
                            fortuneCounter += 1;
                            doFortune = true;
                        }
                    }
                    if (Objects.equals(fishingWithStack.get(ModComponents.LINE_COMPONENT), "diamond")) {
                        double chance = 1.0 - (2.0 / (LINE_FORTUNE_DIAMOND + 2.0));
                        if (randomNum < chance) {
                            fortuneCounter += 1;
                            doFortune = true;
                        }
                        if (randomNum2 < chance) {
                            fortuneCounter += 1;
                            doFortune = true;
                        }
                    }
                    if (Objects.equals(fishingWithStack.get(ModComponents.LINE_COMPONENT), "netherite")) {
                        double chance = 1.0 - (2.0 / (LINE_FORTUNE_NETHERITE + 2.0));
                        if (randomNum < chance) {
                            fortuneCounter += 1;
                            doFortune = true;
                        }
                        if (randomNum2 < chance) {
                            fortuneCounter += 1;
                            doFortune = true;
                        }
                        if (randomNum3 < chance) {
                            fortuneCounter += 1;
                            doFortune = true;
                        }
                    }
                }
                if (!originalStack.isEmpty() && doFortune) {
                    ItemStack fortuneStack = originalStack.copy();
                    fortuneStack.grow(fortuneCounter);
                    return fortuneStack;
                }
            }
        }
        return originalStack;
    }
    // sinker effect
    @ModifyArgs(method = "retrieve(Lnet/minecraft/world/item/ItemStack;)I", at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/storage/loot/LootParams$Builder;withLuck(F)Lnet/minecraft/world/level/storage/loot/LootParams$Builder;"))
    private void addCustomLuckToFishing(Args args) {
        float originalLuck = args.get(0);
        float newLuck = originalLuck;
        Player owner = this.getPlayerOwner();
        if (owner != null) {
            if (fishingWithStack != null) {
                if (Objects.equals(fishingWithStack.get(ModComponents.SINKER_COMPONENT), "copper")) {
                    newLuck = originalLuck + SINKER_LUCK_COPPER;
                }
                if (Objects.equals(fishingWithStack.get(ModComponents.SINKER_COMPONENT), "prismarine")) {
                    newLuck = originalLuck + SINKER_LUCK_PRISMARINE;
                }
                if (Objects.equals(fishingWithStack.get(ModComponents.SINKER_COMPONENT), "iron")) {
                    newLuck = originalLuck + SINKER_LUCK_IRON;
                }
                if (Objects.equals(fishingWithStack.get(ModComponents.SINKER_COMPONENT), "golden")) {
                    newLuck = originalLuck + SINKER_LUCK_GOLDEN;
                }
                if (Objects.equals(fishingWithStack.get(ModComponents.SINKER_COMPONENT), "diamond")) {
                    newLuck = originalLuck + SINKER_LUCK_DIAMOND;
                }
                if (Objects.equals(fishingWithStack.get(ModComponents.SINKER_COMPONENT), "netherite")) {
                    newLuck = originalLuck + SINKER_LUCK_NETHERITE;
                }
            }
        }
        args.set(0, newLuck);
    }

    // trigger fish treasure advancement
    @Inject(method = "retrieve", at = @At(value = "INVOKE", target = "Ljava/util/List;iterator()Ljava/util/Iterator;"))
    private void onEachFishedItem(ItemStack stack, CallbackInfoReturnable<Integer> cir, @Local(ordinal = 0) List<ItemStack> list) {
        Player owner = this.getPlayerOwner();
        if (owner != null) {
            for (ItemStack itemStack : list) {
                if (itemStack.is(ModTags.FISHING_LOOT_TREASURE) && owner instanceof ServerPlayer serverPlayer) {
                    ModTriggers.FISH_TREASURE.trigger(serverPlayer);
                }
            }
        }
    }
}
