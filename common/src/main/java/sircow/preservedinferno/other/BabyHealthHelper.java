package sircow.preservedinferno.other;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import sircow.preservedinferno.Constants;

public final class BabyHealthHelper {
    private static final Identifier BABY_HEALTH_ID = Constants.id("baby_health");

    private BabyHealthHelper() {}

    public static void updateBabyHealth(LivingEntity entity, boolean baby) {
        if (entity.level().isClientSide()) return;

        AttributeInstance health = entity.getAttribute(Attributes.MAX_HEALTH);
        if (health == null) return;

        if (baby) health.addOrReplacePermanentModifier(new AttributeModifier(BABY_HEALTH_ID, -0.25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        else health.removeModifier(BABY_HEALTH_ID);

        entity.setHealth(entity.getMaxHealth());
    }
}
