package sircow.preservedinferno.other;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import sircow.preservedinferno.Constants;

public class ModDamageTypes {
    public static final ResourceKey<DamageType> CONDUIT = ResourceKey.create(Registries.DAMAGE_TYPE, Constants.id("conduit"));
    public static final ResourceKey<DamageType> HEAT = ResourceKey.create(Registries.DAMAGE_TYPE, Constants.id("heat"));

    public static DamageSource of(Level world, ResourceKey<DamageType> key, @Nullable Entity attacker) {
        return new DamageSource(world.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(key), attacker);
    }

    public static void registerModDamageTypes() {
        Constants.LOG.info("Registering Mod Damage Types for " + Constants.MOD_ID);
    }
}
