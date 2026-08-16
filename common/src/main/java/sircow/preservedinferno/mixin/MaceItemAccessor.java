package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.MaceItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Predicate;

@Mixin(MaceItem.class)
public interface MaceItemAccessor {
    @Invoker("knockbackPredicate")
    static Predicate<LivingEntity> invokeKnockbackPredicate(Entity attacker, Entity entity) {
        throw new AssertionError();
    }
}
