package sircow.preservedinferno.component;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import sircow.preservedinferno.item.ModItems;

public final class RodComponentResolver {

    private RodComponentResolver() {}

    public static ItemStack resolveHook(ItemStack rod) {
        String type = rod.get(ModComponents.HOOK_COMPONENT);
        if (type == null || type.equals("none")) return ItemStack.EMPTY;

        ItemStack result = switch (type) {
            case "copper" -> new ItemStack(ModItems.COPPER_FISHING_HOOK.get());
            case "iron" -> new ItemStack(ModItems.IRON_FISHING_HOOK.get());
            case "golden" -> new ItemStack(ModItems.GOLDEN_FISHING_HOOK.get());
            case "diamond" -> new ItemStack(ModItems.DIAMOND_FISHING_HOOK.get());
            case "netherite" -> new ItemStack(ModItems.NETHERITE_FISHING_HOOK.get());
            case "prismarine" -> new ItemStack(ModItems.PRISMARINE_FISHING_HOOK.get());
            default -> ItemStack.EMPTY;
        };

        int unbreaking = rod.getOrDefault(ModComponents.HOOK_UNBREAKING, 0);
        if (unbreaking > 0 && !result.isEmpty()) result.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
        return result;
    }

    public static ItemStack resolveLine(ItemStack rod) {
        String type = rod.get(ModComponents.LINE_COMPONENT);
        if (type == null || type.equals("none")) return ItemStack.EMPTY;

        ItemStack result = switch (type)  {
            case "copper" -> new ItemStack(ModItems.COPPER_LACED_FISHING_LINE.get());
            case "iron" -> new ItemStack(ModItems.IRON_LACED_FISHING_LINE.get());
            case "golden" -> new ItemStack(ModItems.GOLDEN_LACED_FISHING_LINE.get());
            case "diamond" -> new ItemStack(ModItems.DIAMOND_LACED_FISHING_LINE.get());
            case "netherite" -> new ItemStack(ModItems.NETHERITE_LACED_FISHING_LINE.get());
            case "prismarine" -> new ItemStack(ModItems.PRISMARINE_LACED_FISHING_LINE.get());
            default -> ItemStack.EMPTY;
        };

        int unbreaking = rod.getOrDefault(ModComponents.LINE_UNBREAKING, 0);
        if (unbreaking > 0 && !result.isEmpty()) result.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
        return result;
    }

    public static ItemStack resolveSinker(ItemStack rod) {
        String type = rod.get(ModComponents.SINKER_COMPONENT);
        if (type == null || type.equals("none")) return ItemStack.EMPTY;

        ItemStack result = switch (type)  {
            case "copper" -> new ItemStack(ModItems.COPPER_SINKER.get());
            case "iron" -> new ItemStack(ModItems.IRON_SINKER.get());
            case "golden" -> new ItemStack(ModItems.GOLDEN_SINKER.get());
            case "diamond" -> new ItemStack(ModItems.DIAMOND_SINKER.get());
            case "netherite" -> new ItemStack(ModItems.NETHERITE_SINKER.get());
            case "prismarine" -> new ItemStack(ModItems.PRISMARINE_SINKER.get());
            default -> ItemStack.EMPTY;
        };

        int unbreaking = rod.getOrDefault(ModComponents.SINKER_UNBREAKING, 0);
        if (unbreaking > 0 && !result.isEmpty()) result.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
        return result;
    }
}
