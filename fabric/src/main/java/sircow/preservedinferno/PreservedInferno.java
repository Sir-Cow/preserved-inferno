package sircow.preservedinferno;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import sircow.preservedinferno.block.FabricModBlocks;
import sircow.preservedinferno.block.entity.PreservedCauldronBlockData;
import sircow.preservedinferno.block.entity.PreservedCauldronBlockEntity;
import sircow.preservedinferno.block.entity.PreservedFletchingTableBlockData;
import sircow.preservedinferno.block.entity.PreservedFletchingTableBlockEntity;
import sircow.preservedinferno.other.FabricModEvents;
import sircow.preservedinferno.screen.PreservedCauldronMenu;
import sircow.preservedinferno.screen.PreservedEnchantmentMenu;
import sircow.preservedinferno.screen.PreservedFletchingTableMenu;
import sircow.preservedinferno.screen.PreservedLoomMenu;

public class PreservedInferno implements ModInitializer {
    // menus
    public static final MenuType<PreservedCauldronMenu> PRESERVED_CAULDRON_MENU_TYPE =
            Registry.register(BuiltInRegistries.MENU, Constants.id("preserved_cauldron"),
                    new ExtendedScreenHandlerType<>(PreservedCauldronMenu::new, PreservedCauldronBlockData.STREAM_CODEC));
    public static final MenuType<PreservedFletchingTableMenu> PRESERVED_FLETCHING_TABLE_MENU_TYPE =
            Registry.register(BuiltInRegistries.MENU, Constants.id("preserved_fletching_table"),
                    new ExtendedScreenHandlerType<>(PreservedFletchingTableMenu::new, PreservedFletchingTableBlockData.STREAM_CODEC));
    private static final MenuType<PreservedLoomMenu> PRESERVED_LOOM_MENU_TYPE =
            Registry.register(BuiltInRegistries.MENU, Constants.id("preserved_loom"),
                    new ExtendedScreenHandlerType<>((pWindowID, pInventory, pData) -> new PreservedLoomMenu(pWindowID, pInventory), BlockData.CODEC));
    private static final MenuType<PreservedEnchantmentMenu> PRESERVED_ENCHANT_MENU_TYPE =
            Registry.register(BuiltInRegistries.MENU, Constants.id("preserved_enchant"),
                    new ExtendedScreenHandlerType<>((pWindowID, pInventory, pData) -> new PreservedEnchantmentMenu(pWindowID, pInventory), BlockData.CODEC));

    public record BlockData(boolean empty) {
        public static final StreamCodec<RegistryFriendlyByteBuf, BlockData> CODEC = StreamCodec.composite(
                ByteBufCodecs.BOOL,
                BlockData::empty,
                BlockData::new
        );
    }

    static {
        Constants.PRESERVED_LOOM_MENU_TYPE = () -> PRESERVED_LOOM_MENU_TYPE;
        Constants.PRESERVED_ENCHANT_MENU_TYPE = () -> PRESERVED_ENCHANT_MENU_TYPE;
        MenuTypes.PRESERVED_CAULDRON_MENU_TYPE = () -> PRESERVED_CAULDRON_MENU_TYPE;
        MenuTypes.PRESERVED_FLETCHING_TABLE_MENU_TYPE = () -> PRESERVED_FLETCHING_TABLE_MENU_TYPE;
    }

    // block entities
    public static final BlockEntityType<PreservedCauldronBlockEntity> PRESERVED_CAULDRON_BLOCK_ENTITY = register("preserved_cauldron_entity",
            FabricBlockEntityTypeBuilder.create(PreservedCauldronBlockEntity::new, Blocks.CAULDRON).build());
    public static final BlockEntityType<PreservedFletchingTableBlockEntity> PRESERVED_FLETCHING_TABLE_BLOCK_ENTITY = register("preserved_fletching_table_entity",
            FabricBlockEntityTypeBuilder.create(PreservedFletchingTableBlockEntity::new, Blocks.FLETCHING_TABLE).build());

    public static <T extends BlockEntityType<?>> T register(String path, T blockEntityType) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Constants.id(path), blockEntityType);
    }

    @Override
    public void onInitialize() {
        CommonClass.init();
        FabricModEvents.registerModEvents();
        FabricModBlocks.registerBlocks();
    }
}
