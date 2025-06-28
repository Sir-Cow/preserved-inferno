package sircow.preservedinferno;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import sircow.preservedinferno.block.FabricModBlocks;
import sircow.preservedinferno.block.entity.PreservedCauldronBlockData;
import sircow.preservedinferno.block.entity.PreservedCauldronBlockEntity;
import sircow.preservedinferno.block.entity.PreservedCauldronBlockEntityRenderer;
import sircow.preservedinferno.item.FabricModItemGroups;
import sircow.preservedinferno.item.FabricModItems;
import sircow.preservedinferno.other.*;
import sircow.preservedinferno.screen.*;

import java.util.ArrayList;
import java.util.List;

public class PreservedInferno implements ModInitializer {
    private static final List<DelayedBlockTransformationTask> scheduledTasks = new ArrayList<>();
    public static PreservedInferno INSTANCE;

    // menus
    private static final MenuType<AnglingTableMenu> ANGLING_TABLE_MENU_TYPE =
            Registry.register(BuiltInRegistries.MENU, Constants.id("angling_table"),
                    new ExtendedScreenHandlerType<>((pWindowID, pInventory, pData) -> new AnglingTableMenu(pWindowID, pInventory), BlockData.CODEC));
    public static final MenuType<PreservedCauldronMenu> PRESERVED_CAULDRON_MENU_TYPE =
            Registry.register(BuiltInRegistries.MENU, Constants.id("preserved_cauldron"),
                    new ExtendedScreenHandlerType<>(PreservedCauldronMenu::new, PreservedCauldronBlockData.STREAM_CODEC));
    public static final MenuType<PreservedFletchingTableMenu> PRESERVED_FLETCHING_TABLE_MENU_TYPE =
            Registry.register(BuiltInRegistries.MENU, Constants.id("preserved_fletching_table"),
                    new ExtendedScreenHandlerType<>((pWindowID, pInventory, pData) -> new PreservedFletchingTableMenu(pWindowID, pInventory), BlockData.CODEC));
    private static final MenuType<PreservedLoomMenu> PRESERVED_LOOM_MENU_TYPE =
            Registry.register(BuiltInRegistries.MENU, Constants.id("preserved_loom"),
                    new ExtendedScreenHandlerType<>((pWindowID, pInventory, pData) -> new PreservedLoomMenu(pWindowID, pInventory), BlockData.CODEC));
    private static final MenuType<PreservedEnchantmentMenu> PRESERVED_ENCHANT_MENU_TYPE =
            Registry.register(BuiltInRegistries.MENU, Constants.id("preserved_enchant"),
                    new ExtendedScreenHandlerType<>((pWindowID, pInventory, pData) -> new PreservedEnchantmentMenu(pWindowID, pInventory), BlockData.CODEC));
    private static final MenuType<CacheMenu> CACHE_MENU_TYPE =
            Registry.register(BuiltInRegistries.MENU, Constants.id("cache"),
                    new ExtendedScreenHandlerType<>(CacheMenu::new, PreservedInferno.ItemData.CODEC));

    static {
        Constants.ANGLING_TABLE_MENU_TYPE = () -> ANGLING_TABLE_MENU_TYPE;
        MenuTypes.CACHE_MENU_TYPE = () -> CACHE_MENU_TYPE;
        Constants.PRESERVED_ENCHANT_MENU_TYPE = () -> PRESERVED_ENCHANT_MENU_TYPE;
        Constants.PRESERVED_FLETCHING_TABLE_MENU_TYPE = () -> PRESERVED_FLETCHING_TABLE_MENU_TYPE;
        Constants.PRESERVED_LOOM_MENU_TYPE = () -> PRESERVED_LOOM_MENU_TYPE;
        MenuTypes.PRESERVED_CAULDRON_MENU_TYPE = () -> PRESERVED_CAULDRON_MENU_TYPE;
    }

    // codecs
    public record BlockData(boolean empty) {
        public static final StreamCodec<RegistryFriendlyByteBuf, BlockData> CODEC = StreamCodec.composite(
                ByteBufCodecs.BOOL,
                BlockData::empty,
                BlockData::new
        );
    }

    public record ItemData(int containerSize) {
        public static final StreamCodec<RegistryFriendlyByteBuf, ItemData> CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT,
                ItemData::containerSize,
                ItemData::new
        );

        public void write(RegistryFriendlyByteBuf buf) {
            buf.writeVarInt(containerSize);
        }

        public static ItemData read(RegistryFriendlyByteBuf buf) {
            return new ItemData(buf.readVarInt());
        }
    }

    // block entities
    public static final BlockEntityType<PreservedCauldronBlockEntity> PRESERVED_CAULDRON_BLOCK_ENTITY = register("preserved_cauldron_entity",
            FabricBlockEntityTypeBuilder.create(PreservedCauldronBlockEntity::new, Blocks.CAULDRON).build());

    public static <T extends BlockEntityType<?>> T register(String path, T blockEntityType) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Constants.id(path), blockEntityType);
    }

    public static void scheduleDelayedTask(DelayedBlockTransformationTask task) {
        scheduledTasks.add(task);
    }

    // cancel snow layer melt
    public static void cancelTaskAt(BlockPos pos) {
        scheduledTasks.removeIf(task -> task.getPos().equals(pos));
    }

    // server tick
    private void onServerTick(MinecraftServer server) {
        // handling melting nether blocks
        List<DelayedBlockTransformationTask> tasksToRemove = new ArrayList<>();
        List<DelayedBlockTransformationTask> tasksToSchedule = new ArrayList<>();
        for (DelayedBlockTransformationTask task : scheduledTasks) {
            task.tick();
            if (task.isFinished()) {
                if (task.getServerLevel().getBlockState(task.getPos()).is(task.expectedInitialBlock)) {
                    DelayedBlockTransformationTask nextTask = task.transformBlock();
                    if (nextTask != null) {
                        tasksToSchedule.add(nextTask);
                    }
                }
                else {
                    task.removeBreakingAnimation();
                }
                tasksToRemove.add(task);
            }
            else if (!task.getServerLevel().getBlockState(task.getPos()).is(task.expectedInitialBlock)) {
                tasksToRemove.add(task);
                task.removeBreakingAnimation();
            }
        }
        scheduledTasks.removeAll(tasksToRemove);
        scheduledTasks.addAll(tasksToSchedule);

        // handle custom shields
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            ShieldStaminaHandler.onServerTick(player);
        }
    }

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playC2S().register(OpenAdvancementPayload.ID, OpenAdvancementPayload.CODEC);
        INSTANCE = this;
        CommonClass.init();
        FabricModEvents.registerModEvents();
        FabricModItems.registerModItems();
        FabricModBlocks.registerBlocks();
        FabricModItemGroups.registerItemGroups();
        ServerTickEvents.END_SERVER_TICK.register(this::onServerTick);
    }
}
