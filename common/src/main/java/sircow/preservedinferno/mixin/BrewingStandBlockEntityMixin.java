package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrewingStandBlockEntity.class)
public abstract class BrewingStandBlockEntityMixin extends BaseContainerBlockEntity {
    @Shadow int fuel;

    protected BrewingStandBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @ModifyConstant(method = "serverTick", constant = @Constant(intValue = 400))
    private static int preserved_inferno$modifyBrewTime(int original) {
        return 160;
    }

    @Redirect(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/tags/TagKey;)Z", ordinal = 0))
    private static boolean preserved_inferno$disableVanillaFuelCheck(ItemStack instance, TagKey<Item> tag) {
        return false;
    }

    @Inject(method = "serverTick", at = @At(value = "HEAD"))
    private static void preserved_inferno$handleCustomFuel(Level level, BlockPos pos, BlockState state, BrewingStandBlockEntity blockEntity, CallbackInfo ci) {
        BrewingStandBlockEntityMixin self = (BrewingStandBlockEntityMixin) (Object) blockEntity;
        if (self != null) {
            ItemStack fuelStack = self.getItem(4);

            if (!fuelStack.isEmpty() && fuelStack.is(ItemTags.BREWING_FUEL)) {
                int maxFuel = 20;
                int fuelPerItem = 3;

                while (self.fuel <= maxFuel - fuelPerItem && !fuelStack.isEmpty()) {
                    self.fuel += fuelPerItem;
                    fuelStack.shrink(1);
                }

                BrewingStandBlockEntity.setChanged(level, pos, state);
            }
        }
    }
}
