package sircow.preservedinferno.mixin;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.syncher.SyncedDataHolder;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SynchedEntityData.Builder.class)
public class SynchedEntityDataMixin {
    @Mutable @Shadow @Final private SynchedEntityData.DataItem<?>[] itemsById;

    @Unique
    private boolean preserved_inferno$hasIncreasedCapacity = false;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void preserved_inferno$increaseBuilderCapacity(SyncedDataHolder entity, CallbackInfo ci) {
        if (((entity instanceof Player || entity instanceof ServerPlayer) && !(entity instanceof LocalPlayer)) && !preserved_inferno$hasIncreasedCapacity) {
            int originalLength = this.itemsById.length;
            if (originalLength != 24) {
                SynchedEntityData.DataItem<?>[] newItemsById = new SynchedEntityData.DataItem[originalLength + 3];
                System.arraycopy(this.itemsById, 0, newItemsById, 0, originalLength);
                this.itemsById = newItemsById;
                preserved_inferno$hasIncreasedCapacity = true;
            }
        }
    }
}
