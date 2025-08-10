package sircow.preservedinferno.entity;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.entity.custom.FlareGunProjectileEntity;
import sircow.preservedinferno.entity.custom.ThrownCopperTrident;

public class ModEntities {
    public static final ResourceKey<EntityType<?>> FLARE_GUN_PROJECTILE_KEY = ResourceKey.create(Registries.ENTITY_TYPE, Constants.id("flare_gun_projectile"));
    public static final ResourceKey<EntityType<?>> COPPER_TRIDENT_KEY = ResourceKey.create(Registries.ENTITY_TYPE, Constants.id("copper_trident"));

    public static final EntityType<FlareGunProjectileEntity> FLARE_GUN_PROJECTILE =
            EntityType.Builder.<FlareGunProjectileEntity>of(FlareGunProjectileEntity::new, MobCategory.MISC)
                .noLootTable()
                .sized(0.25F, 0.25F)
                .clientTrackingRange(128)
                .updateInterval(10).build(FLARE_GUN_PROJECTILE_KEY);

    public static final EntityType<ThrownCopperTrident> COPPER_TRIDENT =
            EntityType.Builder.<ThrownCopperTrident>of(ThrownCopperTrident::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(0.5F, 0.5F)
                    .eyeHeight(0.13F)
                    .clientTrackingRange(4)
                    .updateInterval(20).build(COPPER_TRIDENT_KEY);

    public static void registerModEntities() {
        Registry.register(BuiltInRegistries.ENTITY_TYPE, Constants.id("flare_gun_projectile"), FLARE_GUN_PROJECTILE);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, Constants.id("copper_trident"), COPPER_TRIDENT);
        // Constants.LOG.info("Registering Mod Entities for " + Constants.MOD_ID);
    }
}
