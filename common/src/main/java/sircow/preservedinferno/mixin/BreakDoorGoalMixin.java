package sircow.preservedinferno.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.BreakDoorGoal;
import net.minecraft.world.entity.ai.goal.DoorInteractGoal;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.other.ModTags;

@Mixin(BreakDoorGoal.class)
public abstract class BreakDoorGoalMixin extends DoorInteractGoal {
    @Shadow protected int breakTime;
    @Shadow protected int lastBreakProgress;

    @Shadow protected abstract int getDoorBreakTime();
    @Shadow public abstract void stop();

    public BreakDoorGoalMixin(Mob mob) {
        super(mob);
    }

    @ModifyReturnValue(method = "getDoorBreakTime", at = @At("RETURN"))
    private int pinferno$doorBreakTime(int original) {
        BlockState state = this.mob.level().getBlockState(this.doorPos);
        if (state.is(ModTags.REINFORCED_DOORS_BLOCK)) return Integer.MAX_VALUE;
        if (state.is(ModTags.COPPER_DOORS)) return 15 * 20;
        if (state.is(BlockTags.WOODEN_DOORS)) return 6 * 20;
        return original;
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void pinferno$checkDoorTypeDist(CallbackInfo ci) {
        var level = this.mob.level();
        var pos = this.doorPos;
        var state = level.getBlockState(pos);
        var block = state.getBlock();

        if (block instanceof DoorBlock door) {
            if (state.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER) {
                var lower = doorPos.below();
                if (this.mob.level().getBlockState(lower).getBlock() == door) this.doorPos = lower;
            }
        }
        double distanceSq = this.mob.distanceToSqr(this.doorPos.getX() + 0.5D, this.doorPos.getY(), this.doorPos.getZ() + 0.5D);
        double rangeSq = 1.5D * 1.5D;

        if (distanceSq > rangeSq) {
            this.mob.getNavigation().moveTo(this.doorPos.getX() + 0.5D, this.doorPos.getY(), this.doorPos.getZ() + 0.5D, 1.0D);
        }

        if (state.is(ModTags.REINFORCED_DOORS_BLOCK)) {
            ci.cancel();
            return;
        }

        if (block instanceof FenceGateBlock) {
            this.breakTime++;

            if (this.mob.getRandom().nextInt(20) == 0) {
                this.mob.level().levelEvent(1019, this.doorPos, 0);
                if (!this.mob.swinging) this.mob.swing(this.mob.getUsedItemHand());
            }

            int required = this.getDoorBreakTime();
            int i = (int)((float)this.breakTime / required * 10.0F);
            if (i != this.lastBreakProgress) {
                this.mob.level().destroyBlockProgress(this.mob.getId(), this.doorPos, i);
                this.lastBreakProgress = i;
            }

            if (this.breakTime >= required) {
                level.levelEvent(1021, pos, 0);
                level.levelEvent(2001, pos, Block.getId(state));
                level.removeBlock(pos, false);

                this.hasDoor = false;
                this.doorPos = this.mob.blockPosition();
                this.breakTime = 0;
                this.stop();
            }
            ci.cancel();
        }
    }

    @Inject(method = "canUse", at = @At("RETURN"), cancellable = true)
    private void pinferno$allowFenceGatesAndDoors(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            for (BlockPos pos : BlockPos.betweenClosed(
                    this.mob.blockPosition().offset(-1, -1, -1),
                    this.mob.blockPosition().offset(1, 1, 1)))
            {
                if (pos.getY() < this.mob.blockPosition().getY() || pos.getY() > this.mob.blockPosition().getY() + 1) continue;

                BlockState state = this.mob.level().getBlockState(pos);
                boolean foundBreakableTarget = false;

                if (state.getBlock() instanceof FenceGateBlock && !state.getValue(FenceGateBlock.OPEN)) {
                    foundBreakableTarget = true;
                }
                else if (state.getBlock() instanceof DoorBlock) {
                    if (state.is(ModTags.BREAKABLE_DOORS)) foundBreakableTarget = true;
                }

                if (foundBreakableTarget) {
                    this.doorPos = pos.immutable();
                    this.hasDoor = true;
                    cir.setReturnValue(true);
                    return;
                }
            }
        }
    }

    @Inject(method = "canContinueToUse", at = @At("HEAD"), cancellable = true)
    private void pinferno$stopIfFenceGateGone(CallbackInfoReturnable<Boolean> cir) {
        if (!this.hasDoor) {
            cir.setReturnValue(false);
            return;
        }

        var state = this.mob.level().getBlockState(this.doorPos);

        if (state.getBlock() instanceof FenceGateBlock) {
            if (!(state.getBlock() instanceof FenceGateBlock) || this.breakTime > this.getDoorBreakTime()) {
                this.hasDoor = false;
                this.doorPos = this.mob.blockPosition();
                cir.setReturnValue(false);
            }
            else cir.setReturnValue(true);
        }
        else if (state.getBlock() instanceof DoorBlock) {
            if (this.breakTime > this.getDoorBreakTime() || !this.doorPos.closerToCenterThan(this.mob.position(), 2.0)) {
                this.hasDoor = false;
                this.doorPos = this.mob.blockPosition();
                cir.setReturnValue(false);
            }
            else cir.setReturnValue(true);
        }
    }
}
