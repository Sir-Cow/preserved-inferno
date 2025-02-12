package sircow.preservedinferno;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import sircow.preservedinferno.block.ModBlocks;
import sircow.preservedinferno.block.entity.ModBlockEntities;
import sircow.preservedinferno.entity.NewEnchantingTableBlockEntityRenderer;
import sircow.preservedinferno.screen.ModScreenHandlers;
import sircow.preservedinferno.screen.NewCauldronBlockScreen;
import sircow.preservedinferno.screen.NewLoomBlockScreen;
import sircow.preservedinferno.screen.NewFletchingTableBlockScreen;
import sircow.preservedinferno.screen.NewEnchantingTableBlockScreen;

public class PreservedInfernoClient implements ClientModInitializer {
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
		// block entity renderers
		BlockEntityRendererFactories.register(ModBlockEntities.NEW_ENCHANTING_TABLE_BLOCK_ENTITY, NewEnchantingTableBlockEntityRenderer::new);
	}
}