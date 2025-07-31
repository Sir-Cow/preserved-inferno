package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.item.ModItems;
import sircow.preservedinferno.other.MobLineOfSight;
import sircow.preservedinferno.other.ModEntityData;

@Mixin(BedBlock.class)
public abstract class BedBlockMixin extends HorizontalDirectionalBlock {
    @Unique private static final long REGEN_COOLDOWN_MS = 10 * 60 * 1000;

    protected BedBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "useWithoutItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;startSleepInBed(Lnet/minecraft/core/BlockPos;)Lcom/mojang/datafixers/util/Either;", shift = At.Shift.BEFORE), cancellable = true)
    private void preserved_inferno$onUseWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack mainHandItem = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHandItem = player.getItemInHand(InteractionHand.OFF_HAND);
        boolean holdingDreamcatcher = mainHandItem.getItem() == ModItems.DREAMCATCHER || offHandItem.getItem() == ModItems.DREAMCATCHER;

        if (level.getLevelData().isHardcore() && !holdingDreamcatcher) {
            long now = System.currentTimeMillis();
            long lastUsed = player.getEntityData().get(ModEntityData.PLAYER_HARDCORE_REGEN_COOLDOWN);

            if (now - lastUsed >= REGEN_COOLDOWN_MS && !MobLineOfSight.hasMonsterLineOfSight(player.level(), pos)) {
                player.getEntityData().set(ModEntityData.PLAYER_HARDCORE_REGEN_COOLDOWN, now);
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 20, 0));
                player.displayClientMessage(Component.translatable("block.minecraft.bed.hardcore_regen"), true);
                cir.setReturnValue(InteractionResult.SUCCESS_SERVER);
            }
            else {
                if (level.isMoonVisible()) {
                    player.displayClientMessage(Component.translatable("block.minecraft.bed.no_dreamcatcher"), true);
                }
                else  {
                    if (MobLineOfSight.hasMonsterLineOfSight(player.level(), pos)) {
                        player.displayClientMessage(Component.translatable("block.minecraft.bed.not_safe_hardcore"), true);
                    }
                    else {
                        player.displayClientMessage(Component.translatable("block.minecraft.bed.hardcore_cooldown"), true);
                    }
                }
                cir.setReturnValue(InteractionResult.SUCCESS_SERVER);
            }
        }
    }
}
