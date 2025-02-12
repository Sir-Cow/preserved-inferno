package sircow.preservedinferno.mixin;

/*
@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin extends Entity {

    public AbstractMinecartEntityMixin(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "getLaunchDirection", at = @At(value = "HEAD"), cancellable = true)
    public void acceleration(BlockPos railPos, CallbackInfoReturnable<Vec3d> cir) {
        BlockState blockState = this.getWorld().getBlockState(railPos);
        // powered rail
        if (blockState.isOf(Blocks.POWERED_RAIL) && blockState.get(PoweredRailBlock.POWERED)) {
            RailShape railShape = blockState.get(((AbstractRailBlock) blockState.getBlock()).getShapeProperty());
            if (railShape == RailShape.EAST_WEST) {
                if (this.getWorld().getBlockState(railPos.west()).isSolidBlock(this.getWorld(), railPos.west())) {
                    cir.setReturnValue(new Vec3d(1.0, 0.0, 0.0));
                } else if (this.getWorld().getBlockState(railPos.east()).isSolidBlock(this.getWorld(), railPos.east())) {
                    cir.setReturnValue(new Vec3d(-1.0, 0.0, 0.0));
                }
            } else if (railShape == RailShape.NORTH_SOUTH) {
                if (this.getWorld().getBlockState(railPos.north()).isSolidBlock(this.getWorld(), railPos.north())) {
                    cir.setReturnValue(new Vec3d(0.0, 0.0, 1.0));
                } else if (this.getWorld().getBlockState(railPos.south()).isSolidBlock(this.getWorld(), railPos.south())) {
                    cir.setReturnValue(new Vec3d(0.0, 0.0, -1.0));
                }
            }
            else {
                cir.setReturnValue(Vec3d.ZERO);
            }
        }
        // inductor rail
        else if (blockState.isOf(ModBlocks.INDUCTOR_RAIL)) {
            RailShape railShape = blockState.get(((AbstractRailBlock) blockState.getBlock()).getShapeProperty());
            if (railShape == RailShape.EAST_WEST) {
                if (this.getWorld().getBlockState(railPos.west()).isSolidBlock(this.getWorld(), railPos.west())) {
                    cir.setReturnValue(new Vec3d(16.0, 0.0, 0.0));
                } else if (this.getWorld().getBlockState(railPos.east()).isSolidBlock(this.getWorld(), railPos.east())) {
                    cir.setReturnValue(new Vec3d(-16.0, 0.0, 0.0));
                }
            } else if (railShape == RailShape.NORTH_SOUTH) {
                if (this.getWorld().getBlockState(railPos.north()).isSolidBlock(this.getWorld(), railPos.north())) {
                    cir.setReturnValue(new Vec3d(0.0, 0.0, 16.0));
                } else if (this.getWorld().getBlockState(railPos.south()).isSolidBlock(this.getWorld(), railPos.south())) {
                    cir.setReturnValue(new Vec3d(0.0, 0.0, -16.0));
                }
            }
            else {
                cir.setReturnValue(Vec3d.ZERO);
            }
        }

        else {
            cir.setReturnValue(Vec3d.ZERO);
        }
        cir.cancel();
    }
}
*/
