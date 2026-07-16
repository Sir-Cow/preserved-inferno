package sircow.preservedinferno.entity;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.entity.custom.*;

public class ModEntities {
    public static final ResourceKey<EntityType<?>> DYNAMITE_PROJECTILE_KEY = ResourceKey.create(Registries.ENTITY_TYPE, Constants.id("dynamite_projectile"));
    public static final ResourceKey<EntityType<?>> FLARE_GUN_PROJECTILE_KEY = ResourceKey.create(Registries.ENTITY_TYPE, Constants.id("flare_gun_projectile"));
    public static final ResourceKey<EntityType<?>> PRIMED_BOOM_BOX_KEY = ResourceKey.create(Registries.ENTITY_TYPE, Constants.id("primed_boom_box"));
    public static final ResourceKey<EntityType<?>> COPPER_TRIDENT_KEY = ResourceKey.create(Registries.ENTITY_TYPE, Constants.id("copper_trident"));
    public static final ResourceKey<EntityType<?>> PRESERVED_LINGERING_BOTTLE_KEY = ResourceKey.create(Registries.ENTITY_TYPE, Constants.id("preserved_lingering_bottle"));
    public static final ResourceKey<EntityType<?>> PRESERVED_SPLASH_BOTTLE_KEY = ResourceKey.create(Registries.ENTITY_TYPE, Constants.id("preserved_splash_bottle"));
    public static final ResourceKey<EntityType<?>> PRESERVED_AREA_EFFECT_CLOUD_KEY = ResourceKey.create(Registries.ENTITY_TYPE, Constants.id("preserved_area_effect_cloud"));

    public static final EntityType<DynamiteEntity> DYNAMITE_PROJECTILE =
            EntityType.Builder.<DynamiteEntity>of(DynamiteEntity::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build(DYNAMITE_PROJECTILE_KEY);
    public static final EntityType<FlareGunProjectileEntity> FLARE_GUN_PROJECTILE =
            EntityType.Builder.<FlareGunProjectileEntity>of(FlareGunProjectileEntity::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(128)
                    .updateInterval(10)
                    .build(FLARE_GUN_PROJECTILE_KEY);
    public static final EntityType<PrimedBoomBox> PRIMED_BOOM_BOX =
            EntityType.Builder.<PrimedBoomBox>of(PrimedBoomBox::new, MobCategory.MISC)
                    .noLootTable()
                    .fireImmune()
                    .sized(0.98F, 0.98F)
                    .eyeHeight(0.15F)
                    .clientTrackingRange(10)
                    .updateInterval(10)
                    .build(PRIMED_BOOM_BOX_KEY);
    public static final EntityType<ThrownCopperTrident> COPPER_TRIDENT =
            EntityType.Builder.<ThrownCopperTrident>of(ThrownCopperTrident::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(0.5F, 0.5F)
                    .eyeHeight(0.13F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build(COPPER_TRIDENT_KEY);
    public static final EntityType<ThrownPreservedLingeringBottle> PRESERVED_LINGERING_BOTTLE =
            EntityType.Builder.<ThrownPreservedLingeringBottle>of(ThrownPreservedLingeringBottle::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build(PRESERVED_LINGERING_BOTTLE_KEY);
    public static final EntityType<ThrownPreservedSplashBottle> PRESERVED_SPLASH_BOTTLE =
            EntityType.Builder.<ThrownPreservedSplashBottle>of(ThrownPreservedSplashBottle::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build(PRESERVED_SPLASH_BOTTLE_KEY);
    public static final EntityType<PreservedAreaEffectCloud> PRESERVED_AREA_EFFECT_CLOUD =
            EntityType.Builder.<PreservedAreaEffectCloud>of(PreservedAreaEffectCloud::new, MobCategory.MISC)
                    .noLootTable()
                    .fireImmune()
                    .sized(6.0F, 0.5F)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build(PRESERVED_AREA_EFFECT_CLOUD_KEY);

    public static void registerModEntities() {
        Registry.register(BuiltInRegistries.ENTITY_TYPE, Constants.id("dynamite_projectile"), DYNAMITE_PROJECTILE);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, Constants.id("flare_gun_projectile"), FLARE_GUN_PROJECTILE);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, Constants.id("primed_boom_box"), PRIMED_BOOM_BOX);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, Constants.id("copper_trident"), COPPER_TRIDENT);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, Constants.id("preserved_lingering_bottle"), PRESERVED_LINGERING_BOTTLE);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, Constants.id("preserved_splash_bottle"), PRESERVED_SPLASH_BOTTLE);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, Constants.id("preserved_area_effect_cloud"), PRESERVED_AREA_EFFECT_CLOUD);
    }
}
