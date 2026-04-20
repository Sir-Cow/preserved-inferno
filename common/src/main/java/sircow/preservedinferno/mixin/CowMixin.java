package sircow.preservedinferno.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.cow.AbstractCow;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.animal.cow.CowVariant;
import net.minecraft.world.entity.animal.cow.CowVariants;
import net.minecraft.world.entity.variant.PriorityProvider;
import net.minecraft.world.entity.variant.SpawnContext;
import net.minecraft.world.entity.variant.VariantUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Cow.class)
public abstract class CowMixin extends AbstractCow {
    public CowMixin(EntityType<? extends AbstractCow> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    public abstract void setVariant(Holder<CowVariant> variant);

//               :3
//         \|/         (__)
//             `\------(oo)
//    \|/        ||    (__) - moo!
//               ||w--||     \|/
//           \|/

    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    private void preserved_inferno$onFinalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData spawnGroupData, CallbackInfoReturnable<SpawnGroupData> cir) {
        var context = SpawnContext.create(level, this.blockPosition());
        var registry = level.registryAccess().lookupOrThrow(Registries.COW_VARIANT);

        List<Holder<CowVariant>> validVariants = registry.listElements()
                .map(holder -> (Holder<CowVariant>) holder)
                .filter(holder -> holder.value().selectors().stream().anyMatch(selector ->
                        selector.condition().map(cond -> cond.test(context)).orElse(false)
                ))
                .toList();

        if (validVariants.isEmpty()) return;

        int totalPriority = validVariants.stream()
                .mapToInt(holder -> holder.value().selectors().stream()
                        .mapToInt(PriorityProvider.Selector::priority)
                        .max().orElse(0))
                .sum();

        if (totalPriority <= 0) {
            this.setVariant(validVariants.getFirst());
            return;
        }

        int roll = this.random.nextInt(totalPriority);
        for (Holder<CowVariant> holder : validVariants) {
            int priority = holder.value().selectors().stream()
                    .mapToInt(PriorityProvider.Selector::priority)
                    .max().orElse(0);

            if (roll < priority) {
                this.setVariant(holder);
                return;
            }
            roll -= priority;
        }
        this.setVariant(validVariants.getFirst());
    }
}
