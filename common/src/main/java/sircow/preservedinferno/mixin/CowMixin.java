package sircow.preservedinferno.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.AbstractCow;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.CowVariant;
import net.minecraft.world.entity.animal.CowVariants;
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

    @Inject(method = "finalizeSpawn", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$onFinalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData spawnGroupData, CallbackInfoReturnable<SpawnGroupData> cir) {
//               :3
//         \|/         (__)
//             `\------(oo)
//    \|/        ||    (__) - moo!
//               ||w--||     \|/
//           \|/
        var context = SpawnContext.create(level, this.blockPosition());
        var registry = level.registryAccess().lookupOrThrow(Registries.COW_VARIANT);

        List<Holder<CowVariant>> allVariantHolders = registry.listElements()
                .map(holderReference -> (Holder<CowVariant>) holderReference)
                .toList();

        List<Holder<CowVariant>> validVariants = allVariantHolders.stream()
                .filter(holder -> {
                    CowVariant variant = holder.value();
                    return variant.selectors().stream().anyMatch(selector -> {
                        try {
                            return selector.condition()
                                    .map(cond -> cond.test(context))
                                    .orElse(false);
                        } catch (Exception e) {
                            return false;
                        }
                    });
                })
                .toList();

        if (validVariants.isEmpty()) {
            this.setVariant(VariantUtils.getDefaultOrAny(level.registryAccess(), CowVariants.TEMPERATE));
            cir.setReturnValue(super.finalizeSpawn(level, difficulty, spawnReason, spawnGroupData));
            return;
        }

        int totalPriority = validVariants.stream()
                .mapToInt(holder -> holder.value().selectors().stream()
                        .mapToInt(PriorityProvider.Selector::priority)
                        .max().orElse(0))
                .sum();

        if (totalPriority == 0) {
            this.setVariant(validVariants.getFirst());
            cir.setReturnValue(super.finalizeSpawn(level, difficulty, spawnReason, spawnGroupData));
            return;
        }

        int roll = this.random.nextInt(totalPriority);
        for (Holder<CowVariant> holder : validVariants) {
            int priority = holder.value().selectors().stream()
                    .mapToInt(PriorityProvider.Selector::priority)
                    .max().orElse(0);

            if (roll < priority) {
                this.setVariant(holder);
                cir.setReturnValue(super.finalizeSpawn(level, difficulty, spawnReason, spawnGroupData));
                return;
            }
            roll -= priority;
        }

        this.setVariant(validVariants.getFirst());
        cir.setReturnValue(super.finalizeSpawn(level, difficulty, spawnReason, spawnGroupData));
    }
}
