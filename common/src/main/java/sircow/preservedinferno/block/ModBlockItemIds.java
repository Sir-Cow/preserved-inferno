package sircow.preservedinferno.block;

import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;
import sircow.preservedinferno.Constants;

public class ModBlockItemIds {
    public static final BlockItemId RHYOLITE = create("rhyolite");
    public static final BlockItemId POLISHED_RHYOLITE = create("polished_rhyolite");
    public static final BlockItemId RHYOLITE_BRICKS = create("rhyolite_bricks");
    public static final BlockItemId CRACKED_RHYOLITE_BRICKS = create("cracked_rhyolite_bricks");
    public static final BlockItemId RHYOLITE_STAIRS = create("rhyolite_stairs");
    public static final BlockItemId POLISHED_RHYOLITE_STAIRS = create("polished_rhyolite_stairs");
    public static final BlockItemId RHYOLITE_BRICK_STAIRS = create("rhyolite_brick_stairs");
    public static final BlockItemId RHYOLITE_SLAB = create("rhyolite_slab");
    public static final BlockItemId POLISHED_RHYOLITE_SLAB = create("polished_rhyolite_slab");
    public static final BlockItemId RHYOLITE_BRICK_SLAB = create("rhyolite_brick_slab");
    public static final BlockItemId RHYOLITE_WALL = create("rhyolite_wall");
    public static final BlockItemId RHYOLITE_BRICK_WALL = create("rhyolite_brick_wall");

    public static final BlockItemId SPARKLING_BLACKSTONE = create("sparkling_blackstone");

    public static final BlockItemId ANGLING_TABLE = create("angling_table");
    public static final BlockItemId BOOM_BOX = create("boom_box");

    public static final BlockItemId INDUCTOR_RAIL = create("inductor_rail");
    public static final BlockItemId EXPOSED_INDUCTOR_RAIL = create("exposed_inductor_rail");
    public static final BlockItemId WEATHERED_INDUCTOR_RAIL = create("weathered_inductor_rail");
    public static final BlockItemId OXIDIZED_INDUCTOR_RAIL = create("oxidized_inductor_rail");
    public static final BlockItemId WAXED_INDUCTOR_RAIL = create("waxed_inductor_rail");
    public static final BlockItemId WAXED_EXPOSED_INDUCTOR_RAIL = create("waxed_exposed_inductor_rail");
    public static final BlockItemId WAXED_WEATHERED_INDUCTOR_RAIL = create("waxed_weathered_inductor_rail");
    public static final BlockItemId WAXED_OXIDIZED_INDUCTOR_RAIL = create("waxed_oxidized_inductor_rail");

    public static final BlockItemId REINFORCED_OAK_DOOR = create("reinforced_oak_door");
    public static final BlockItemId REINFORCED_SPRUCE_DOOR = create("reinforced_spruce_door");
    public static final BlockItemId REINFORCED_BIRCH_DOOR = create("reinforced_birch_door");
    public static final BlockItemId REINFORCED_JUNGLE_DOOR = create("reinforced_jungle_door");
    public static final BlockItemId REINFORCED_ACACIA_DOOR = create("reinforced_acacia_door");
    public static final BlockItemId REINFORCED_CHERRY_DOOR = create("reinforced_cherry_door");
    public static final BlockItemId REINFORCED_DARK_OAK_DOOR = create("reinforced_dark_oak_door");
    public static final BlockItemId REINFORCED_PALE_OAK_DOOR = create("reinforced_pale_oak_door");
    public static final BlockItemId REINFORCED_MANGROVE_DOOR = create("reinforced_mangrove_door");
    public static final BlockItemId REINFORCED_BAMBOO_DOOR = create("reinforced_bamboo_door");
    public static final BlockItemId REINFORCED_CRIMSON_DOOR = create("reinforced_crimson_door");
    public static final BlockItemId REINFORCED_WARPED_DOOR = create("reinforced_warped_door");
    public static final BlockItemId REINFORCED_COPPER_DOOR = create("reinforced_copper_door");
    public static final BlockItemId REINFORCED_EXPOSED_COPPER_DOOR = create("reinforced_exposed_copper_door");
    public static final BlockItemId REINFORCED_OXIDIZED_COPPER_DOOR = create("reinforced_oxidized_copper_door");
    public static final BlockItemId REINFORCED_WEATHERED_COPPER_DOOR = create("reinforced_weathered_copper_door");
    public static final BlockItemId REINFORCED_WAXED_COPPER_DOOR = create("reinforced_waxed_copper_door");
    public static final BlockItemId REINFORCED_WAXED_EXPOSED_COPPER_DOOR = create("reinforced_waxed_exposed_copper_door");
    public static final BlockItemId REINFORCED_WAXED_OXIDIZED_COPPER_DOOR = create("reinforced_waxed_oxidized_copper_door");
    public static final BlockItemId REINFORCED_WAXED_WEATHERED_COPPER_DOOR = create("reinforced_waxed_weathered_copper_door");

    private static BlockItemId create(String name) {
        Identifier id = Constants.id(name);
        return BlockItemId.create(id, id);
    }
}
