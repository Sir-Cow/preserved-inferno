package sircow.preservedinferno.other;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sircow.preservedinferno.effect.ModEffects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// used for handling inventory keeping when dying with well rested effect
public class TempInventoryStorage {
    private static final Map<UUID, ListTag> SAVED_INVENTORIES = new HashMap<>();
    private static final Map<UUID, Integer> SAVED_EXPERIENCE_LEVELS = new HashMap<>();
    private static final Map<UUID, Float> SAVED_EXPERIENCE_PROGRESS = new HashMap<>();
    private static final Map<UUID, Boolean> PLAYER_HAD_WELL_RESTED_ON_DEATH = new HashMap<>();

    public static void savePlayerInventory(Player player) {
        if (player.hasEffect(ModEffects.WELL_RESTED)) {
            ListTag inventoryTag = new ListTag();
            Inventory playerInventory = player.getInventory();
            RegistryAccess registryAccess = player.level().registryAccess();

            for (int i = 0; i < playerInventory.getContainerSize(); ++i) {
                ItemStack itemStack = playerInventory.getItem(i);
                if (!itemStack.isEmpty()) {
                    CompoundTag itemNbt = new CompoundTag();
                    itemNbt.putByte("Slot", (byte) i);
                    CompoundTag itemDataTag = (CompoundTag) itemStack.save(registryAccess);
                    itemNbt.merge(itemDataTag);
                    inventoryTag.add(itemNbt);
                }
            }

            for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
                ItemStack itemStack = player.getItemBySlot(slot);
                if (!itemStack.isEmpty()) {
                    CompoundTag itemNbt = getCompoundTag(slot);
                    CompoundTag itemDataTag = (CompoundTag) itemStack.save(registryAccess);
                    itemNbt.merge(itemDataTag);
                    inventoryTag.add(itemNbt);
                }
            }

            ItemStack offhandStack = player.getItemBySlot(EquipmentSlot.OFFHAND);
            if (!offhandStack.isEmpty()) {
                CompoundTag itemNbt = new CompoundTag();
                itemNbt.putByte("Slot", (byte) Inventory.SLOT_OFFHAND);
                CompoundTag itemDataTag = (CompoundTag) offhandStack.save(registryAccess);
                itemNbt.merge(itemDataTag);
                inventoryTag.add(itemNbt);
            }
            SAVED_INVENTORIES.put(player.getUUID(), inventoryTag);

            if (player instanceof ServerPlayer serverPlayer) {
                SAVED_EXPERIENCE_LEVELS.put(serverPlayer.getUUID(), serverPlayer.experienceLevel);
                SAVED_EXPERIENCE_PROGRESS.put(serverPlayer.getUUID(), serverPlayer.experienceProgress);
            }
            PLAYER_HAD_WELL_RESTED_ON_DEATH.put(player.getUUID(), true);
        }
    }

    private static @NotNull CompoundTag getCompoundTag(EquipmentSlot slot) {
        CompoundTag itemNbt = new CompoundTag();
        byte nbtSlotId;
        switch (slot) {
            case FEET -> nbtSlotId = 100;
            case LEGS -> nbtSlotId = 101;
            case CHEST -> nbtSlotId = 102;
            case HEAD -> nbtSlotId = 103;
            default -> throw new IllegalStateException("Unexpected EquipmentSlot: " + slot);
        }
        itemNbt.putByte("Slot", nbtSlotId);
        return itemNbt;
    }

    public static boolean restorePlayerInventory(ServerPlayer newPlayer) {
        UUID playerUUID = newPlayer.getUUID();
        if (SAVED_INVENTORIES.containsKey(playerUUID)) {
            ListTag inventoryTag = SAVED_INVENTORIES.remove(playerUUID);
            newPlayer.getInventory().clearContent();
            for (int i = 0; i < inventoryTag.size(); i++) {
                CompoundTag itemNbt = inventoryTag.getCompound(i).orElse(new CompoundTag());
                int slotId = itemNbt.getByte("Slot").orElse((byte)0) & 255;
                ItemStack itemStack = ItemStack.parse(newPlayer.registryAccess(), itemNbt).orElse(ItemStack.EMPTY);

                if (!itemStack.isEmpty()) {
                    if (slotId < newPlayer.getInventory().getContainerSize()) {
                        newPlayer.getInventory().setItem(slotId, itemStack);
                    }
                    else if (slotId >= 100 && slotId < 104) {
                        EquipmentSlot equipmentSlot = switch (slotId) {
                            case 100 -> EquipmentSlot.FEET;
                            case 101 -> EquipmentSlot.LEGS;
                            case 102 -> EquipmentSlot.CHEST;
                            case 103 -> EquipmentSlot.HEAD;
                            default -> null;
                        };
                        newPlayer.setItemSlot(equipmentSlot, itemStack);
                    }
                    else if (slotId == 150) {
                        newPlayer.setItemSlot(EquipmentSlot.OFFHAND, itemStack);
                    }
                }
            }
        }

        if (SAVED_EXPERIENCE_LEVELS.containsKey(playerUUID)) {
            newPlayer.experienceLevel = SAVED_EXPERIENCE_LEVELS.remove(playerUUID);
        }
        if (SAVED_EXPERIENCE_PROGRESS.containsKey(playerUUID)) {
            newPlayer.experienceProgress = SAVED_EXPERIENCE_PROGRESS.remove(playerUUID);
        }

        boolean hadWellRestedEffectOnDeath = PLAYER_HAD_WELL_RESTED_ON_DEATH.remove(playerUUID) != null;

        if (hadWellRestedEffectOnDeath) {
            newPlayer.removeEffect(ModEffects.WELL_RESTED);
        }

        return hadWellRestedEffectOnDeath;
    }
}
