package sircow.preservedinferno.other;

import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sircow.preservedinferno.effect.ModEffects;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

// used for handling inventory keeping when dying with well rested effect
public class TempInventoryStorage {
    private static final Map<UUID, ListTag> SAVED_INVENTORIES = new HashMap<>();
    private static final Map<UUID, Integer> SAVED_EXPERIENCE_LEVELS = new HashMap<>();
    private static final Map<UUID, Float> SAVED_EXPERIENCE_PROGRESS = new HashMap<>();
    private static final Map<UUID, Boolean> PLAYER_HAD_WELL_RESTED_ON_DEATH = new HashMap<>();

    public static void savePlayerInventory(Player player) {
        if (player.hasEffect(ModEffects.WELL_RESTED.holder)) {
            ListTag inventoryTag = new ListTag();
            Inventory inventory = player.getInventory();

            for (int i = 0; i < inventory.getContainerSize(); ++i) {
                ItemStack stack = inventory.getItem(i);
                if (!stack.isEmpty()) {
                    CompoundTag tag = new CompoundTag();
                    tag.putByte("Slot", (byte) i);
                    var ops = RegistryOps.create(NbtOps.INSTANCE, player.level().registryAccess());
                    ItemStack.CODEC.encodeStart(ops, stack).resultOrPartial(error -> {
                    }).ifPresent(encoded -> tag.put("Item", encoded));
                    inventoryTag.add(tag);
                }
            }

            for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
                ItemStack stack = player.getItemBySlot(slot);
                if (!stack.isEmpty()) {
                    CompoundTag tag = getCompoundTag(slot);
                    var ops = RegistryOps.create(NbtOps.INSTANCE, player.level().registryAccess());
                    ItemStack.CODEC.encodeStart(ops, stack).resultOrPartial(error -> {
                    }).ifPresent(encoded -> tag.put("Item", encoded));
                    inventoryTag.add(tag);
                }
            }

            ItemStack offhand = player.getItemBySlot(EquipmentSlot.OFFHAND);
            if (!offhand.isEmpty()) {
                CompoundTag tag = new CompoundTag();
                tag.putByte("Slot", (byte) 150);
                var ops = RegistryOps.create(NbtOps.INSTANCE, player.level().registryAccess());
                ItemStack.CODEC.encodeStart(ops, offhand).resultOrPartial(error -> {
                }).ifPresent(encoded -> tag.put("Item", encoded));
                inventoryTag.add(tag);
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
        CompoundTag tag = new CompoundTag();
        byte id = switch (slot) {
            case FEET -> 100;
            case LEGS -> 101;
            case CHEST -> 102;
            case HEAD -> 103;
            default -> throw new IllegalStateException("Unexpected EquipmentSlot: " + slot);
        };
        tag.putByte("Slot", id);
        return tag;
    }

    public static boolean restorePlayerInventory(ServerPlayer player) {
        UUID uuid = player.getUUID();

        ListTag tagList = SAVED_INVENTORIES.remove(uuid);
        if (tagList != null) {
            player.getInventory().clearContent();

            for (Tag value : tagList) {
                CompoundTag tag = (CompoundTag) value;
                int slotId = tag.getByteOr("Slot", (byte) 0) & 255;

                var ops = RegistryOps.create(NbtOps.INSTANCE, player.level().registryAccess());
                Optional<ItemStack> stackOpt = Optional.ofNullable(tag.get("Item"))
                        .flatMap(itemTag -> ItemStack.CODEC.decode(ops, itemTag).result())
                        .map(Pair::getFirst);
                ItemStack stack = stackOpt.orElse(ItemStack.EMPTY);

                if (!stack.isEmpty()) {
                    if (slotId < player.getInventory().getContainerSize()) {
                        player.getInventory().setItem(slotId, stack);
                    }
                    else if (slotId >= 100 && slotId < 104) {
                        EquipmentSlot slot = switch (slotId) {
                            case 100 -> EquipmentSlot.FEET;
                            case 101 -> EquipmentSlot.LEGS;
                            case 102 -> EquipmentSlot.CHEST;
                            case 103 -> EquipmentSlot.HEAD;
                            default -> throw new IllegalStateException("Invalid equipment slot ID: " + slotId);
                        };
                        player.setItemSlot(slot, stack);
                    }
                    else if (slotId == 150) {
                        player.setItemSlot(EquipmentSlot.OFFHAND, stack);
                    }
                }
            }
        }

        if (SAVED_EXPERIENCE_LEVELS.containsKey(uuid)) {
            player.experienceLevel = SAVED_EXPERIENCE_LEVELS.remove(uuid);
        }
        if (SAVED_EXPERIENCE_PROGRESS.containsKey(uuid)) {
            player.experienceProgress = SAVED_EXPERIENCE_PROGRESS.remove(uuid);
        }

        boolean hadWellRestedEffectOnDeath = PLAYER_HAD_WELL_RESTED_ON_DEATH.remove(uuid) != null;

        if (hadWellRestedEffectOnDeath) {
            player.removeEffect(ModEffects.WELL_RESTED.holder);
        }

        return hadWellRestedEffectOnDeath;
    }
}
