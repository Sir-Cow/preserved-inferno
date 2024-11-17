package sircow.placeholder;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sircow.placeholder.block.ModBlocks;
import sircow.placeholder.block.entity.ModBlockEntities;
import sircow.placeholder.item.ModItemGroups;
import sircow.placeholder.item.ModItems;
import sircow.placeholder.other.ModDamageTypes;
import sircow.placeholder.other.ModEvents;
import sircow.placeholder.screen.ModScreenHandlers;
import sircow.placeholder.sound.ModSounds;

public class Placeholder implements ModInitializer {
	public static final String MOD_ID = "placeholder";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final TagKey<Block> TAG_COPPER_RAILS = TagKey.of(
			RegistryKeys.BLOCK, Identifier.of("placeholder", "placeholder_rails")
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