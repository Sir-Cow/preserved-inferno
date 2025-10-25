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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.other.ModTags;

@Mixin(BreakDoorGoal.class)
public abstract class BreakDoorGoalMixin extends DoorInteractGoal {
    public BreakDoorGoalMixin(Mob mob) {
        super(mob);
    }

    @ModifyReturnValue(method = "getDoorBreakTime", at = @At("RETURN"))
    private int preserved_inferno$doorBreakTime(int original) {
        BlockState state = this.mob.level().getBlockState(this.doorPos);
        if (state.is(ModTags.REINFORCED_DOORS_BLOCK)) return Integer.MAX_VALUE; // can't break
        if (state.is(ModTags.COPPER_DOORS)) return 15 * 20;
        if (state.is(BlockTags.WOODEN_DOORS)) return 6 * 20;
        return original;
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$checkDoorTypeDist(CallbackInfo ci) {
        BlockState state = this.mob.level().getBlockState(this.doorPos);
        if (state.is(ModTags.REINFORCED_DOORS_BLOCK)) ci.cancel();

        if (state.getBlock() instanceof DoorBlock door) {
            if (state.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER) {
                var lower = doorPos.below();
                if (this.mob.level().getBlockState(lower).getBlock() == door)
                    this.doorPos = lower;
            }
        }

        double distanceSq = this.mob.distanceToSqr(this.doorPos.getX() + 0.5D, this.doorPos.getY(), this.doorPos.getZ() + 0.5D);
        double rangeSq = 1.5D * 1.5D;

        if (distanceSq > rangeSq) {
            this.mob.getNavigation().moveTo(this.doorPos.getX() + 0.5D, this.doorPos.getY(), this.doorPos.getZ() + 0.5D, 1.0D);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void preserved_inferno$break(CallbackInfo ci) {
        BreakDoorGoal self = (BreakDoorGoal) (Object) this;
        var level = this.mob.level();
        var pos = this.doorPos;
        if (pos == null || level == null) return;

        var state = level.getBlockState(pos);
        var block = state.getBlock();
        if (!(block instanceof DoorBlock) && !(block instanceof FenceGateBlock)) return;

        try {
            var breakTimeField = BreakDoorGoal.class.getDeclaredField("breakTime");
            var doorBreakTimeField = BreakDoorGoal.class.getDeclaredField("doorBreakTime");
            breakTimeField.setAccessible(true);
            doorBreakTimeField.setAccessible(true);

            int breakTime = breakTimeField.getInt(self);
            int required = Math.max(5 * 20, (int) doorBreakTimeField.get(self));

            if (breakTime >= required) {
                boolean broken = false;

                if (block instanceof DoorBlock door) {
                    DoubleBlockHalf half = state.getValue(DoorBlock.HALF);
                    BlockPos lower = half == DoubleBlockHalf.UPPER ? pos.below() : pos;
                    BlockPos upper = half == DoubleBlockHalf.UPPER ? pos : pos.above();
                    BlockState lowerState = level.getBlockState(lower);
                    BlockState upperState = level.getBlockState(upper);

                    if (lowerState.getBlock() == door) {
                        level.levelEvent(1021, lower, 0);
                        level.levelEvent(2001, lower, Block.getId(lowerState));
                        level.removeBlock(lower, false);
                        broken = true;
                    }
                    if (upperState.getBlock() == door) {
                        level.levelEvent(2001, upper, Block.getId(upperState));
                        level.removeBlock(upper, false);
                        broken = true;
                    }
                } else if (block instanceof FenceGateBlock) {
                    level.levelEvent(1021, pos, 0);
                    level.levelEvent(2001, pos, Block.getId(state));
                    level.removeBlock(pos, false);
                    broken = true;
                }

                if (broken) {
                    this.hasDoor = false;
                    this.doorPos = this.mob.blockPosition();
                    try {
                        var breakingTimeField = BreakDoorGoal.class.getDeclaredField("breakTime");
                        breakingTimeField.setAccessible(true);
                        breakingTimeField.setInt(self, 0);
                    }
                    catch (ReflectiveOperationException ignored) {}

                    self.stop();
                }
            }
        }
        catch (ReflectiveOperationException ignored) {}
    }

    @Inject(method = "canUse", at = @At("RETURN"), cancellable = true)
    private void preserved_inferno$allowFenceGatesAndDoors(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            for (BlockPos pos : BlockPos.betweenClosed(
                    this.mob.blockPosition().offset(-1, -1, -1),
                    this.mob.blockPosition().offset(1, 1, 1)))
            {
                if (pos.getY() < this.mob.blockPosition().getY() || pos.getY() > this.mob.blockPosition().getY() + 1) {
                    continue;
                }

                BlockState state = this.mob.level().getBlockState(pos);
                boolean foundBreakableTarget = false;

                if (state.getBlock() instanceof FenceGateBlock && !state.getValue(FenceGateBlock.OPEN)) {
                    foundBreakableTarget = true;
                }
                else if (state.getBlock() instanceof DoorBlock) {
                    if (state.is(ModTags.BREAKABLE_DOORS)) {
                        if (!state.getValue(DoorBlock.OPEN)) {
                            foundBreakableTarget = true;
                        }
                    }
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
    private void preserved_inferno$stopIfDoorGone(CallbackInfoReturnable<Boolean> cir) {
        if (!this.hasDoor) {
            cir.setReturnValue(false);
            return;
        }

        var state = this.mob.level().getBlockState(this.doorPos);
        if (!(state.getBlock() instanceof DoorBlock || state.getBlock() instanceof FenceGateBlock)) {
            this.hasDoor = false;
            this.doorPos = this.mob.blockPosition();
            cir.setReturnValue(false);
        }
    }
}
