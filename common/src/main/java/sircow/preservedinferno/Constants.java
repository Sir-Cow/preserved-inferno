package sircow.preservedinferno;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sircow.preservedinferno.screen.PreservedEnchantmentMenu;
import sircow.preservedinferno.screen.PreservedFletchingTableMenu;
import sircow.preservedinferno.screen.PreservedLoomMenu;

import java.util.function.Supplier;

public class Constants {
	public static final String MOD_ID = "pinferno";
	public static final String MOD_NAME = "Preserved: Inferno";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

	public static Supplier<MenuType<PreservedEnchantmentMenu>> PRESERVED_ENCHANT_MENU_TYPE;
	public static Supplier<MenuType<PreservedFletchingTableMenu>> PRESERVED_FLETCHING_TABLE_MENU_TYPE;
	public static Supplier<MenuType<PreservedLoomMenu>> PRESERVED_LOOM_MENU_TYPE;

	public static ResourceLocation id(String name) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
	}
}