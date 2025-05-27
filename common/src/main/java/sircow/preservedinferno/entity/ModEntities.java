package sircow.preservedinferno.entity;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.entity.custom.FlareGunProjectileEntity;

public class ModEntities {
    public static final ResourceKey<EntityType<?>> FLARE_GUN_PROJECTILE_KEY =
            ResourceKey.create(Registries.ENTITY_TYPE, Constants.id("flare_gun_projectile"));

    public static final EntityType<FlareGunProjectileEntity> FLARE_GUN_PROJECTILE =
            EntityType.Builder.<FlareGunProjectileEntity>of(
                            FlareGunProjectileEntity::new,
                            MobCategory.MISC
                    )
                .noLootTable()
                .sized(0.25F, 0.25F)
                .clientTrackingRange(128)
                .updateInterval(10).build(FLARE_GUN_PROJECTILE_KEY);

    public static void registerModEntities() {
        Registry.register(BuiltInRegistries.ENTITY_TYPE, Constants.id("flare_gun_projectile"), FLARE_GUN_PROJECTILE);
        Constants.LOG.info("Registering Mod Entities for " + Constants.MOD_ID);
    }
}
