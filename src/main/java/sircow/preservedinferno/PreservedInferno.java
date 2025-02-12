package sircow.preservedinferno;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sircow.preservedinferno.block.ModBlocks;
import sircow.preservedinferno.block.entity.ModBlockEntities;
import sircow.preservedinferno.item.ModItemGroups;
import sircow.preservedinferno.item.ModItems;
import sircow.preservedinferno.other.ModDamageTypes;
import sircow.preservedinferno.other.ModEvents;
import sircow.preservedinferno.screen.ModScreenHandlers;
import sircow.preservedinferno.sound.ModSounds;

public class PreservedInferno implements ModInitializer {
	public static final String MOD_ID = "preservedinferno";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final TagKey<Block> TAG_COPPER_RAILS = TagKey.of(
			RegistryKeys.BLOCK, Identifier.of("preservedinferno", "preservedinferno_rails")
	);

	@Override
	public void onInitialize() {
		// registering
		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModDamageTypes.registerModDamageTypes();
		ModBlockEntities.registerBlockEntities();
		ModScreenHandlers.registerScreenHandlers();
		ModEvents.registerModEvents();
		ModSounds.registerSounds();
		// other
		ModBlocks.initialize();
		ModEvents.modifySleeping();
	}
}