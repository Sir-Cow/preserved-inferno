package sircow.preservedinferno.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.monster.Shulker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Shulker.class)
public class ShulkerMixin {
    @Shadow
    private static final ResourceLocation COVERED_ARMOR_MODIFIER_ID = ResourceLocation.withDefaultNamespace("covered");
    @Unique
    private static final AttributeModifier NEW_COVERED_ARMOR_MODIFIER = new AttributeModifier(
            COVERED_ARMOR_MODIFIER_ID, 150.0, AttributeModifier.Operation.ADD_VALUE
    );

    @ModifyArg(method = "setRawPeekAmount", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;addPermanentModifier(Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;)V"))
    private AttributeModifier preserved_inferno$replaceCoveredModifier(AttributeModifier original) {
        return NEW_COVERED_ARMOR_MODIFIER;
    }
}
