package sircow.preservedinferno.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.animal.nautilus.Nautilus;
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
    @Unique private final int HOOK_SPEED_IRON = 100;
    @Unique private final int HOOK_SPEED_PRISMARINE = 150;
    @Unique private final int HOOK_SPEED_GOLDEN = 150;
    @Unique private final int HOOK_SPEED_DIAMOND = 200;
    @Unique private final int HOOK_SPEED_NETHERITE = 300;
    @Unique private final double LINE_FORTUNE_COPPER = 0.5;
    @Unique private final double LINE_FORTUNE_IRON = 1.0;
    @Unique private final double LINE_FORTUNE_PRISMARINE = 1.5;
    @Unique private final double LINE_FORTUNE_GOLDEN = 1.5;
    @Unique private final double LINE_FORTUNE_DIAMOND = 2.0;
    @Unique private final double LINE_FORTUNE_NETHERITE = 3.0;
    @Unique private final float SINKER_LUCK_COPPER = 0.5F;
    @Unique private final float SINKER_LUCK_IRON = 1.0F;
    @Unique private final float SINKER_LUCK_PRISMARINE = 1.5F;
    @Unique private final float SINKER_LUCK_GOLDEN = 1.5F;
    @Unique private final float SINKER_LUCK_DIAMOND = 2.0F;
    @Unique private final float SINKER_LUCK_NETHERITE = 3.0F;

    @Shadow @Mutable @Final private int lureSpeed;
    @Unique private boolean lureSpeedModified;
    @Shadow public abstract @Nullable Player getPlayerOwner();

    @Unique
    private ItemStack getRod(Player player) {
        ItemStack main = player.getMainHandItem();
        if (main.has(ModComponents.HOOK_COMPONENT)) return main;

        ItemStack off = player.getOffhandItem();
        if (off.has(ModComponents.HOOK_COMPONENT)) return off;

        return ItemStack.EMPTY;
    }

    // hook effect
    @Inject(method = "catchingFish", at = @At("HEAD"))
    private void pinferno$addLureSpeed(BlockPos pos, CallbackInfo ci) {
        if (lureSpeedModified) return;

        Player owner = this.getPlayerOwner();
        if (owner == null) return;

        ItemStack rod = getRod(owner);
        if (rod.isEmpty()) return;

        MobEffectInstance conduit = owner.getEffect(MobEffects.CONDUIT_POWER);
        if (conduit != null) this.lureSpeed += (int)((conduit.getAmplifier() + 1) * 50.0);
        MobEffectInstance blessing = owner.getEffect(MobEffects.BREATH_OF_THE_NAUTILUS);
        if (blessing != null) this.lureSpeed += 50;

        String hook = rod.get(ModComponents.HOOK_COMPONENT);

        if (Objects.equals(hook, "copper")) this.lureSpeed += HOOK_SPEED_COPPER;
        else if (Objects.equals(hook, "iron")) this.lureSpeed += HOOK_SPEED_IRON;
        else if (Objects.equals(hook, "prismarine")) this.lureSpeed += HOOK_SPEED_PRISMARINE;
        else if (Objects.equals(hook, "golden")) this.lureSpeed += HOOK_SPEED_GOLDEN;
        else if (Objects.equals(hook, "diamond")) this.lureSpeed += HOOK_SPEED_DIAMOND;
        else if (Objects.equals(hook, "netherite")) this.lureSpeed += HOOK_SPEED_NETHERITE;

        lureSpeedModified = true;
    }

    // line effect
    @ModifyArg(method = "retrieve(Lnet/minecraft/world/item/ItemStack;)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;<init>(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)V", ordinal = 0), index = 4)
    private ItemStack pinferno$addFortune(ItemStack originalStack) {
        Player owner = this.getPlayerOwner();
        if (owner == null || originalStack.isEmpty()) return originalStack;

        ItemStack rod = getRod(owner);
        if (rod.isEmpty()) return originalStack;

        if (!(originalStack.is(ModTags.FISHING_LOOT_FISH) || originalStack.is(ModTags.FISHING_LOOT_VARIETY) || originalStack.is(ModTags.FISHING_LOOT_JUNK))) return originalStack;

        Random random = new Random();

        int bonus = 0;
        String line = rod.get(ModComponents.LINE_COMPONENT);
        double chance;

        if (Objects.equals(line, "copper")) {
            chance = 1.0 - (2.0 / (LINE_FORTUNE_COPPER + 2.0));
            if (random.nextDouble() < chance) bonus++;
        }
        else if (Objects.equals(line, "iron")) {
            chance = 1.0 - (2.0 / (LINE_FORTUNE_IRON + 2.0));
            if (random.nextDouble() < chance) bonus++;
        }
        else if (Objects.equals(line, "prismarine")) {
            chance = 1.0 - (2.0 / (LINE_FORTUNE_PRISMARINE + 2.0));
            if (random.nextDouble() < chance) bonus++;
            if (random.nextDouble() < chance) bonus++;
        }
        else if (Objects.equals(line, "golden")) {
            chance = 1.0 - (2.0 / (LINE_FORTUNE_GOLDEN + 2.0));
            if (random.nextDouble() < chance) bonus++;
            if (random.nextDouble() < chance) bonus++;
        }
        else if (Objects.equals(line, "diamond")) {
            chance = 1.0 - (2.0 / (LINE_FORTUNE_DIAMOND + 2.0));
            if (random.nextDouble() < chance) bonus++;
            if (random.nextDouble() < chance) bonus++;
        }
        else if (Objects.equals(line, "netherite")) {
            chance = 1.0 - (2.0 / (LINE_FORTUNE_NETHERITE + 2.0));
            if (random.nextDouble() < chance) bonus++;
            if (random.nextDouble() < chance) bonus++;
            if (random.nextDouble() < chance) bonus++;
        }

        if (bonus <= 0) return originalStack;

        ItemStack copy = originalStack.copy();
        copy.grow(bonus);
        return copy;
    }

    // sinker effect
    @ModifyArgs(method = "retrieve(Lnet/minecraft/world/item/ItemStack;)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/loot/LootParams$Builder;withLuck(F)Lnet/minecraft/world/level/storage/loot/LootParams$Builder;"))
    private void pinferno$addLuck(Args args) {
        float base = args.get(0);
        Player owner = this.getPlayerOwner();
        if (owner == null) return;

        ItemStack rod = getRod(owner);
        if (rod.isEmpty()) return;

        float result = base;

        MobEffectInstance conduit = owner.getEffect(MobEffects.CONDUIT_POWER);
        if (conduit != null) result += (conduit.getAmplifier() + 1) * 0.5F;
        MobEffectInstance blessing = owner.getEffect(MobEffects.BREATH_OF_THE_NAUTILUS);
        if (blessing != null) result += 0.5F;

        String sinker = rod.get(ModComponents.SINKER_COMPONENT);

        if (Objects.equals(sinker, "copper")) result += SINKER_LUCK_COPPER;
        else if (Objects.equals(sinker, "iron")) result += SINKER_LUCK_IRON;
        else if (Objects.equals(sinker, "prismarine")) result += SINKER_LUCK_PRISMARINE;
        else if (Objects.equals(sinker, "golden")) result += SINKER_LUCK_GOLDEN;
        else if (Objects.equals(sinker, "diamond")) result += SINKER_LUCK_DIAMOND;
        else if (Objects.equals(sinker, "netherite")) result += SINKER_LUCK_NETHERITE;

        args.set(0, result);
    }

    // trigger advancement
    @Inject(method = "retrieve", at = @At(value = "INVOKE", target = "Ljava/util/List;iterator()Ljava/util/Iterator;"))
    private void pinferno$onEachFishedItem(ItemStack rod, CallbackInfoReturnable<Integer> cir, @Local(name = "items") List<ItemStack> items) {
        Player owner = this.getPlayerOwner();
        if (!(owner instanceof ServerPlayer serverPlayer)) return;

        for (ItemStack itemStack : items) {
            if (itemStack.is(ModTags.FISHING_LOOT_FISH) && serverPlayer.getVehicle() instanceof Nautilus) ModTriggers.FISH_ON_NAUTILUS.get().trigger(serverPlayer);
        }
    }

    @Inject(method = "retrieve", at = @At(value = "TAIL"))
    private void pinferno$causeExhaustion(ItemStack rod, CallbackInfoReturnable<Integer> cir) {
        Player owner = this.getPlayerOwner();
        if (owner != null) owner.causeFoodExhaustion(0.2F);
    }
}
