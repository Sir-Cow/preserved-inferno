package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
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
    private void pinferno$onUseWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack mainHandItem = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHandItem = player.getItemInHand(InteractionHand.OFF_HAND);
        boolean holdingDreamcatcher = mainHandItem.getItem() == ModItems.DREAMCATCHER.get() || offHandItem.getItem() == ModItems.DREAMCATCHER.get();

        if (level.getLevelData().isHardcore() && !holdingDreamcatcher) {
            long now = System.currentTimeMillis();
            long lastUsed = player.getEntityData().get(ModEntityData.PLAYER_HARDCORE_REGEN_COOLDOWN);

            if (now - lastUsed >= REGEN_COOLDOWN_MS && !MobLineOfSight.hasMonsterLineOfSight(player.level(), pos)) {
                player.getEntityData().set(ModEntityData.PLAYER_HARDCORE_REGEN_COOLDOWN, now);
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 20, 0));
                player.sendOverlayMessage(Component.translatable("block.minecraft.bed.hardcore_regen"));
                cir.setReturnValue(InteractionResult.SUCCESS_SERVER);
            }
            else {
                boolean moonVisible = player.level().environmentAttributes().getValue(EnvironmentAttributes.MOON_ANGLE, new Vec3(player.getX(), player.getY(), player.getZ()), null) > 0.0F;
                if (moonVisible) {
                    player.sendOverlayMessage(Component.translatable("block.minecraft.bed.no_dreamcatcher"));
                }
                else  {
                    if (MobLineOfSight.hasMonsterLineOfSight(player.level(), pos)) {
                        player.sendOverlayMessage(Component.translatable("block.minecraft.bed.not_safe_hardcore"));
                    }
                    else {
                        player.sendOverlayMessage(Component.translatable("block.minecraft.bed.hardcore_cooldown"));
                    }
                }
                cir.setReturnValue(InteractionResult.SUCCESS_SERVER);
            }
        }
    }
}
