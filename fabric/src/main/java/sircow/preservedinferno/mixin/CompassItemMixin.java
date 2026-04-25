package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.PreservedInferno;
import sircow.preservedinferno.config.MiscCategory;
import sircow.preservedinferno.item.FabricModItems;

import java.util.Optional;

@Mixin(CompassItem.class)
public class CompassItemMixin {
    @Inject(method = "inventoryTick", at = @At("TAIL"))
    private void preserved_inferno$showDistance(ItemStack stack, ServerLevel level, Entity owner, EquipmentSlot slot, CallbackInfo ci) {
        if (!(owner instanceof Player player)) return;
        if (level.isClientSide()) return;

        if (slot != EquipmentSlot.MAINHAND && slot != EquipmentSlot.OFFHAND) return;

        if (slot == EquipmentSlot.OFFHAND) {
            ItemStack mainHand = player.getMainHandItem();
            if (mainHand.getItem() instanceof CompassItem || mainHand.getItem() == FabricModItems.REVERB_COMPASS) return;
        }
        BlockPos targetPos = null;

        LodestoneTracker tracker = stack.get(DataComponents.LODESTONE_TRACKER);
        if (tracker != null) {
            Optional<GlobalPos> optional = tracker.target();
            if (optional.isPresent() && optional.get().dimension() == level.dimension()) targetPos = optional.get().pos();
        }
        else {
            GlobalPos respawn = level.getRespawnData().globalPos();
            if (level.getRespawnData().globalPos().dimension() == level.dimension()) targetPos = respawn.pos();
        }

        if (targetPos == null) return;

        Vec3 playerPos = player.position();
        double dx = playerPos.x - targetPos.getX();
        double dy = playerPos.y - targetPos.getY();
        double dz = playerPos.z - targetPos.getZ();
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        String formatted = "";

        MiscCategory.UnitSystem unit = PreservedInferno.config.miscCategory.unitSystem;

        if (unit == MiscCategory.UnitSystem.IMPERIAL) {
            double feet = distance / 0.3048;

            if (feet >= 5280.0) {
                double miles = feet / 5280.0;
                formatted = String.format("%.2f", miles) + "mi";
            }
            else formatted = String.format("%.2f", feet) + "ft";
        }
        else if (unit == MiscCategory.UnitSystem.METRIC) {
            if (distance >= 1000.0) {
                double km = distance / 1000.0;
                formatted = String.format("%.2f", km) + "km";
            }
            else formatted = String.format("%.2f", distance) + "m";
        }

        Component message = Component.translatable("item.pinferno.compass_tooltip").append(formatted);
        player.sendOverlayMessage(message);
    }
}

