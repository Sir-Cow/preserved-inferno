package sircow.preservedinferno;

import net.minecraft.world.inventory.MenuType;
import sircow.preservedinferno.screen.PreservedCauldronMenu;
import sircow.preservedinferno.screen.PreservedFletchingTableMenu;

import java.util.function.Supplier;

public class MenuTypes {
    public static Supplier<MenuType<PreservedCauldronMenu>> PRESERVED_CAULDRON_MENU_TYPE;
    public static Supplier<MenuType<PreservedFletchingTableMenu>> PRESERVED_FLETCHING_TABLE_MENU_TYPE;
}
