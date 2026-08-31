package sircow.preservedinferno.entity;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.entity.custom.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModEntities {
    private static final Map<Identifier, EntityType<?>> ENTITIES = new LinkedHashMap<>();

    public static final EntityType<DynamiteEntity> DYNAMITE_PROJECTILE = register("dynamite_projectile",
            EntityType.Builder.<DynamiteEntity>of(DynamiteEntity::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
    );
    public static final EntityType<FlareGunProjectileEntity> FLARE_GUN_PROJECTILE = register("flare_gun_projectile",
            EntityType.Builder.<FlareGunProjectileEntity>of(FlareGunProjectileEntity::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(128)
                    .updateInterval(10)
    );
    public static final EntityType<PrimedBoomBox> PRIMED_BOOM_BOX = register("primed_boom_box",
            EntityType.Builder.<PrimedBoomBox>of(PrimedBoomBox::new, MobCategory.MISC)
                    .noLootTable()
                    .fireImmune()
                    .sized(0.98F, 0.98F)
                    .eyeHeight(0.15F)
                    .clientTrackingRange(10)
                    .updateInterval(10)
    );
    public static final EntityType<ThrownCopperTrident> COPPER_TRIDENT = register("copper_trident",
            EntityType.Builder.<ThrownCopperTrident>of(ThrownCopperTrident::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(0.5F, 0.5F)
                    .eyeHeight(0.13F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
    );
    public static final EntityType<ThrownPreservedLingeringBottle> PRESERVED_LINGERING_BOTTLE = register("preserved_lingering_bottle",
            EntityType.Builder.<ThrownPreservedLingeringBottle>of(ThrownPreservedLingeringBottle::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
    );
    public static final EntityType<ThrownPreservedSplashBottle> PRESERVED_SPLASH_BOTTLE = register("preserved_splash_bottle",
            EntityType.Builder.<ThrownPreservedSplashBottle>of(ThrownPreservedSplashBottle::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
    );
    public static final EntityType<PreservedAreaEffectCloud> PRESERVED_AREA_EFFECT_CLOUD = register("preserved_area_effect_cloud",
            EntityType.Builder.<PreservedAreaEffectCloud>of(PreservedAreaEffectCloud::new, MobCategory.MISC)
                    .noLootTable()
                    .fireImmune()
                    .sized(6.0F, 0.5F)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
    );

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        Identifier id = Constants.id(name);
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, id);
        EntityType<T> type = builder.build(key);
        ENTITIES.put(id, type);
        return type;
    }

    public static void registerModEntities() {
        ENTITIES.forEach((id, entityType) ->
                Registry.register(BuiltInRegistries.ENTITY_TYPE, id, entityType)
        );
    }

    public static Map<Identifier, EntityType<?>> getEntities() {
        return ENTITIES;
    }
}
