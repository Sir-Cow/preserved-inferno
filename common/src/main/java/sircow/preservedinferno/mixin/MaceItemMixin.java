package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.MaceItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static net.minecraft.world.item.Item.BASE_ATTACK_DAMAGE_ID;

@Mixin(MaceItem.class)
public class MaceItemMixin {
    // modify base damage
    @ModifyArg(
            method = "createAttributes",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/component/ItemAttributeModifiers$Builder;add(Lnet/minecraft/core/Holder;Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;Lnet/minecraft/world/entity/EquipmentSlotGroup;)Lnet/minecraft/world/item/component/ItemAttributeModifiers$Builder;",
                    ordinal = 0),
            index = 1
    )
    private static AttributeModifier preserved_inferno$modifyDamage(AttributeModifier modifier) {
        modifier = new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 9.0, AttributeModifier.Operation.ADD_VALUE);
        return modifier;
    }
}
