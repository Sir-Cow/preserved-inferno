package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.Objects;

@Mixin(Attributes.class)
public class AttributesMixin {
    @Unique private static boolean change = false;

    // change max value of armour points
    @ModifyArg(method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=armor")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/ai/attributes/RangedAttribute;<init>(Ljava/lang/String;DDD)V"))
    private static String preserved_inferno$changeArmourMaxx(String descriptionId) {
        if (Objects.equals(descriptionId, "attribute.name.armor")) {
            change = true;
        }
        return descriptionId;
    }

    @ModifyArg(method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=armor")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/ai/attributes/RangedAttribute;<init>(Ljava/lang/String;DDD)V"), index = 3)
    private static double preserved_inferno$changeArmourMax(double maxValue) {
        if (change) {
            maxValue = 150.0F;
            change = false;
        }
        return maxValue;
    }
}
