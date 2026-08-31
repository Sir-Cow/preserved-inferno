package sircow.preservedinferno.component;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public record RodTooltipComponent(ItemStack rod, ItemStack hook, ItemStack line, ItemStack sinker) implements TooltipComponent {}
