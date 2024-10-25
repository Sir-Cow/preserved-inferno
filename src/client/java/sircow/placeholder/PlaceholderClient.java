package sircow.placeholder;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import sircow.placeholder.block.ModBlocks;
import sircow.placeholder.screen.ModScreenHandlers;
import sircow.placeholder.screen.NewCauldronBlockScreen;
import sircow.placeholder.screen.NewLoomBlockScreen;
import sircow.placeholder.screen.NewFletchingTableBlockScreen;
import sircow.placeholder.screen.NewEnchantingTableBlockScreen;

public class PlaceholderClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// gui screens
		HandledScreens.register(ModScreenHandlers.NEW_CAULDRON_BLOCK_SCREEN_HANDLER, NewCauldronBlockScreen::new);
		HandledScreens.register(ModScreenHandlers.NEW_LOOM_BLOCK_SCREEN_HANDLER, NewLoomBlockScreen::new);
		HandledScreens.register(ModScreenHandlers.NEW_FLETCHING_TABLE_BLOCK_SCREEN_HANDLER, NewFletchingTableBlockScreen::new);
		HandledScreens.register(ModScreenHandlers.NEW_ENCHANTING_TABLE_BLOCK_SCREEN_HANDLER, NewEnchantingTableBlockScreen::new);
		// enable rail textures to be transparent
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.INDUCTOR_RAIL, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.EXPOSED_INDUCTOR_RAIL, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WEATHERED_INDUCTOR_RAIL, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.OXIDIZED_INDUCTOR_RAIL, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WAXED_INDUCTOR_RAIL, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WAXED_EXPOSED_INDUCTOR_RAIL, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WAXED_WEATHERED_INDUCTOR_RAIL, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WAXED_OXIDIZED_INDUCTOR_RAIL, RenderLayer.getCutout());
	}
}