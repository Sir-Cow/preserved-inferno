package sircow.preservedinferno.mixin;

import net.minecraft.world.item.ToolMaterial;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import sircow.preservedinferno.other.ModTags;

@Mixin(ToolMaterial.class)
public class ToolMaterialMixin {
    @Shadow @Final @Mutable public static ToolMaterial WOOD;
    @Shadow @Final @Mutable public static ToolMaterial STONE;
    @Shadow @Final @Mutable public static ToolMaterial IRON;
    @Shadow @Final @Mutable public static ToolMaterial GOLD;
    @Shadow @Final @Mutable public static ToolMaterial DIAMOND;
    @Shadow @Final @Mutable public static ToolMaterial NETHERITE;

    static {
        WOOD = new ToolMaterial(WOOD.incorrectBlocksForDrops(), 64, 1.0F, WOOD.attackDamageBonus(), WOOD.enchantmentValue(), WOOD.repairItems());
        STONE = new ToolMaterial(STONE.incorrectBlocksForDrops(), 128, 2.0F, STONE.attackDamageBonus(), STONE.enchantmentValue(), STONE.repairItems());
        IRON = new ToolMaterial(IRON.incorrectBlocksForDrops(), 512, 6.0F, IRON.attackDamageBonus(), IRON.enchantmentValue(), IRON.repairItems());
        GOLD = new ToolMaterial(GOLD.incorrectBlocksForDrops(), 256, 12.0F, GOLD.attackDamageBonus(), GOLD.enchantmentValue(), GOLD.repairItems());
        DIAMOND = new ToolMaterial(DIAMOND.incorrectBlocksForDrops(), 1024, 10.0F, DIAMOND.attackDamageBonus(), DIAMOND.enchantmentValue(), DIAMOND.repairItems());
        NETHERITE = new ToolMaterial(NETHERITE.incorrectBlocksForDrops(), 2048, 15.0F, NETHERITE.attackDamageBonus(), NETHERITE.enchantmentValue(), ModTags.REPAIRS_NETHERITE_TOOL);
    }
}
