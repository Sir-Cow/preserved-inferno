package sircow.preservedinferno.mixin;

import net.minecraft.world.item.ToolMaterial;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import sircow.preservedinferno.tag.ModTags;

@Mixin(ToolMaterial.class)
public class ToolMaterialMixin {
    @Shadow @Final @Mutable public static ToolMaterial WOOD;
    @Shadow @Final @Mutable public static ToolMaterial STONE;
    @Shadow @Final @Mutable public static ToolMaterial COPPER;
    @Shadow @Final @Mutable public static ToolMaterial IRON;
    @Shadow @Final @Mutable public static ToolMaterial GOLD;
    @Shadow @Final @Mutable public static ToolMaterial DIAMOND;
    @Shadow @Final @Mutable public static ToolMaterial NETHERITE;

    static {
        WOOD = new ToolMaterial(WOOD.incorrectBlocksForDrops(), 64, 1.5F, WOOD.attackDamageBonus(), WOOD.enchantmentValue(), WOOD.repairItems());
        STONE = new ToolMaterial(STONE.incorrectBlocksForDrops(), 128, 3.0F, STONE.attackDamageBonus(), STONE.enchantmentValue(), STONE.repairItems());
        COPPER = new ToolMaterial(COPPER.incorrectBlocksForDrops(), 256, 4.5F, COPPER.attackDamageBonus(), COPPER.enchantmentValue(), COPPER.repairItems());
        IRON = new ToolMaterial(IRON.incorrectBlocksForDrops(), 512, 6.0F, IRON.attackDamageBonus(), IRON.enchantmentValue(), IRON.repairItems());
        GOLD = new ToolMaterial(GOLD.incorrectBlocksForDrops(), 256, 12.0F, GOLD.attackDamageBonus(), GOLD.enchantmentValue(), GOLD.repairItems());
        DIAMOND = new ToolMaterial(DIAMOND.incorrectBlocksForDrops(), 2048, 9.0F, DIAMOND.attackDamageBonus(), DIAMOND.enchantmentValue(), DIAMOND.repairItems());
        NETHERITE = new ToolMaterial(NETHERITE.incorrectBlocksForDrops(), 4096, 16.0F, NETHERITE.attackDamageBonus(), NETHERITE.enchantmentValue(), ModTags.REPAIRS_NETHERITE_TOOL);
    }
}
