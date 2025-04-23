package sircow.preservedinferno.mixin;

import net.minecraft.world.item.ToolMaterial;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ToolMaterial.class)
public class ToolMaterialMixin {
    @Shadow @Final @Mutable public static ToolMaterial WOOD;
    @Shadow @Final @Mutable public static ToolMaterial STONE;
    @Shadow @Final @Mutable public static ToolMaterial GOLD;
    @Shadow @Final @Mutable public static ToolMaterial DIAMOND;
    @Shadow @Final @Mutable public static ToolMaterial NETHERITE;

    static {
        WOOD = new ToolMaterial(WOOD.incorrectBlocksForDrops(), WOOD.durability(), 1.0F, WOOD.attackDamageBonus(), WOOD.enchantmentValue(), WOOD.repairItems());
        STONE = new ToolMaterial(STONE.incorrectBlocksForDrops(), STONE.durability(), 2.0F, STONE.attackDamageBonus(), STONE.enchantmentValue(), STONE.repairItems());
        GOLD = new ToolMaterial(GOLD.incorrectBlocksForDrops(), GOLD.durability(), 21.0F, GOLD.attackDamageBonus(), GOLD.enchantmentValue(), GOLD.repairItems());
        DIAMOND = new ToolMaterial(DIAMOND.incorrectBlocksForDrops(), DIAMOND.durability(), 10.0F, DIAMOND.attackDamageBonus(), DIAMOND.enchantmentValue(), DIAMOND.repairItems());
        NETHERITE = new ToolMaterial(NETHERITE.incorrectBlocksForDrops(), NETHERITE.durability(), 15.0F, NETHERITE.attackDamageBonus(), NETHERITE.enchantmentValue(), NETHERITE.repairItems());
    }
}
