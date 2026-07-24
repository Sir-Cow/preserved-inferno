package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.item.ModItems;

@Mixin(BrewingStandBlockEntity.class)
public abstract class BrewingStandBlockEntityMixin extends BaseContainerBlockEntity {
    @Shadow private int fuel;

    protected BrewingStandBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @ModifyConstant(method = "serverTick", constant = @Constant(intValue = 400))
    private static int pinferno$modifyBrewTime(int original) {
        return 160;
    }

    @Redirect(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/tags/TagKey;)Z", ordinal = 0))
    private static boolean pinferno$disableVanillaFuelCheck(ItemStack instance, TagKey<Item> tag) {
        return false;
    }

    @Inject(method = "serverTick", at = @At(value = "HEAD"))
    private static void pinferno$handleCustomFuel(Level level, BlockPos pos, BlockState state, BrewingStandBlockEntity blockEntity, CallbackInfo ci) {
        BrewingStandBlockEntityMixin self = (BrewingStandBlockEntityMixin) (Object) blockEntity;
        if (self != null) {
            ItemStack fuelStack = self.getItem(4);

            if (!fuelStack.isEmpty() && fuelStack.is(ItemTags.BREWING_FUEL)) {
                int maxFuel = 21;
                int fuelPerItem = 3;

                while (self.fuel <= maxFuel - fuelPerItem && !fuelStack.isEmpty()) {
                    self.fuel += fuelPerItem;
                    fuelStack.shrink(1);
                }

                BrewingStandBlockEntity.setChanged(level, pos, state);
            }
        }
    }

    @Inject(method = "canPlaceItem", at = @At("HEAD"), cancellable = true)
    private void pinferno$allowCustomBottlesInBrewingStand(int slot, ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        if (slot != 3 && slot != 4) {
            BrewingStandBlockEntity stand = (BrewingStandBlockEntity) (Object) this;
            if (stand.getItem(slot).isEmpty()) {
                if (itemStack.is(Items.HONEY_BOTTLE)
                        || itemStack.is(ModItems.SPLASH_HONEY_BOTTLE)
                        || itemStack.is(ModItems.LINGERING_HONEY_BOTTLE)
                        || itemStack.is(ModItems.LAVA_BOTTLE)
                        || itemStack.is(ModItems.SPLASH_LAVA_BOTTLE)
                        || itemStack.is(ModItems.LINGERING_LAVA_BOTTLE)
                        || itemStack.is(ModItems.MILK_BOTTLE)
                        || itemStack.is(ModItems.SPLASH_MILK_BOTTLE)
                        || itemStack.is(ModItems.LINGERING_MILK_BOTTLE)) {
                    cir.setReturnValue(true);
                }
            }
        }
    }
}
