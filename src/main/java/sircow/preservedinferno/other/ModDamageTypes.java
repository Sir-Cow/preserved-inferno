package sircow.preservedinferno.other;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import sircow.preservedinferno.PreservedInferno;

public class ModDamageTypes {
    public static final RegistryKey<DamageType> TAG_CONDUIT = RegistryKey.of(
            RegistryKeys.DAMAGE_TYPE, Identifier.of("preservedinferno", "conduit")
    );

    public static DamageSource of(World world, RegistryKey<DamageType> key, @Nullable Entity attacker) {
        return new DamageSource(world.getRegistryManager().getOrThrow(RegistryKeys.DAMAGE_TYPE).getOrThrow(key), attacker);
    }

    public static void registerModDamageTypes() {
        PreservedInferno.LOGGER.info("Registering Mod Damage Types for " + PreservedInferno.MOD_ID);
    }
}
