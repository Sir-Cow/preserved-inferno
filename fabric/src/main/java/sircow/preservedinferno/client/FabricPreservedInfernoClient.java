package sircow.preservedinferno.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.MenuTypes;
import sircow.preservedinferno.block.ModBlocks;

public class FabricPreservedInfernoClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // menus
        MenuScreens.register(Constants.PRESERVED_LOOM_MENU_TYPE.get(), PreservedLoomScreen::new);
        MenuScreens.register(Constants.PRESERVED_ENCHANT_MENU_TYPE.get(), PreservedEnchantingTableScreen::new);
        MenuScreens.register(MenuTypes.PRESERVED_CAULDRON_MENU_TYPE.get(), PreservedCauldronScreen::new);
        MenuScreens.register(MenuTypes.PRESERVED_FLETCHING_TABLE_MENU_TYPE.get(), PreservedFletchingTableScreen::new);

        // enable rail textures to be transparent
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.INDUCTOR_RAIL, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.EXPOSED_INDUCTOR_RAIL, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WEATHERED_INDUCTOR_RAIL, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.OXIDIZED_INDUCTOR_RAIL, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WAXED_INDUCTOR_RAIL, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WAXED_EXPOSED_INDUCTOR_RAIL, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WAXED_WEATHERED_INDUCTOR_RAIL, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WAXED_OXIDIZED_INDUCTOR_RAIL, RenderType.cutout());
    }
}
