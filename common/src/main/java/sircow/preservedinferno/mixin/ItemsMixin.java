package sircow.preservedinferno.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Unit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.item.consume_effects.TeleportRandomlyConsumeEffect;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import sircow.preservedinferno.RegisterItemChecker;
import sircow.preservedinferno.item.ModItems;
import sircow.preservedinferno.other.HeatAccessor;

@Mixin(value = Items.class, priority = 1100)
public abstract class ItemsMixin {
    // modify stack size of potions
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=potion")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;stacksTo(I)Lnet/minecraft/world/item/Item$Properties;", ordinal = 0))
    private static int preserved_inferno$modifyPotionStackSize(int old) { return 4; }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=splash_potion")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;stacksTo(I)Lnet/minecraft/world/item/Item$Properties;", ordinal = 0))
    private static int preserved_inferno$modifySplashPotionStackSize(int old) { return 4; }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=lingering_potion")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;stacksTo(I)Lnet/minecraft/world/item/Item$Properties;", ordinal = 0))
    private static int preserved_inferno$modifyLingeringPotionStackSize(int old) { return 4; }

    // modify stew/soup stack sizes
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=beetroot_soup")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;stacksTo(I)Lnet/minecraft/world/item/Item$Properties;", ordinal = 0))
    private static int preserved_inferno$modifyBeetrootSoupStackSize(int old) { return 16; }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=mushroom_stew")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;stacksTo(I)Lnet/minecraft/world/item/Item$Properties;", ordinal = 0))
    private static int preserved_inferno$modifyMushroomStewStackSize(int old) { return 16; }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=rabbit_stew")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;stacksTo(I)Lnet/minecraft/world/item/Item$Properties;", ordinal = 0))
    private static int preserved_inferno$modifyRabbitStewStackSize(int old) { return 16; }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=suspicious_stew")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;stacksTo(I)Lnet/minecraft/world/item/Item$Properties;", ordinal = 0))
    private static int preserved_inferno$modifySuspiciousStewStackSize(int old) { return 16; }

    // modify projectile stack sizes
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=egg")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;stacksTo(I)Lnet/minecraft/world/item/Item$Properties;", ordinal = 0))
    private static int preserved_inferno$modifyEggStackSize(int old) { return 64; }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=snowball")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;stacksTo(I)Lnet/minecraft/world/item/Item$Properties;", ordinal = 0))
    private static int preserved_inferno$modifySnowballStackSize(int old) { return 64; }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=ender_pearl")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;stacksTo(I)Lnet/minecraft/world/item/Item$Properties;", ordinal = 0))
    private static int preserved_inferno$modifyEnderPearlStackSize(int old) { return 64; }

    // modify food (mainly to speed up eating time or modify status effects)
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=apple")), at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties preserved_inferno$modifyApple(Item.Properties original) {
        return original.food(Foods.APPLE, Consumable.builder().consumeSeconds(0.8F).build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=beetroot")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties preserved_inferno$modifyBeetroot(Item.Properties original) {
        return original.food(Foods.BEETROOT, Consumable.builder().consumeSeconds(0.8F).build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=carrot")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties preserved_inferno$modifyCarrot(Item.Properties original) {
        return original.food(Foods.CARROT, Consumable.builder().consumeSeconds(0.8F).build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=chorus_fruit")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties preserved_inferno$modifyChorusFruit(Item.Properties original) {
        return original.food(Foods.CHORUS_FRUIT, Consumable.builder()
                .consumeSeconds(0.8F)
                .onConsume(new TeleportRandomlyConsumeEffect())
                .build())
                .useCooldown(1.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=cookie")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties preserved_inferno$modifyCookie(Item.Properties original) {
        return original.food(Foods.COOKIE, Consumable.builder().consumeSeconds(0.8F).build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=glow_berries")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties preserved_inferno$modifyGlowBerries(Item.Properties original) {
        return original.food(Foods.GLOW_BERRIES, Consumable.builder().consumeSeconds(0.8F).build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=melon_slice")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties preserved_inferno$modifyMelonSlice(Item.Properties original) {
        return original.food(Foods.MELON_SLICE, Consumable.builder().consumeSeconds(0.8F)
                .onConsume(new ConsumeEffect() {
                    @Override
                    public @NotNull Type<? extends ConsumeEffect> getType() { return null; }

                    @Override
                    public boolean apply(@NotNull Level level, @NotNull ItemStack itemStack, @NotNull LivingEntity livingEntity) {
                        if (livingEntity instanceof Player player) {
                            if (!level.isClientSide()) {
                                int currentHeat = ((HeatAccessor) player).preserved_inferno$getHeat();
                                if (currentHeat >= 1) {
                                    ((HeatAccessor) player).preserved_inferno$decreaseHeat(1);
                                }
                            }
                        }
                        return false;
                    }
                })
                .build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=poisonous_potato")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties preserved_inferno$modifyPoisonousPotato(Item.Properties original) {
        return original.food(Foods.POISONOUS_POTATO, Consumable.builder()
        .consumeSeconds(0.8F)
        .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.POISON, 100, 0), 0.6F))
        .build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=potato")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties preserved_inferno$modifyPotato(Item.Properties original) {
        return original.food(Foods.POTATO, Consumable.builder().consumeSeconds(0.8F).build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=beef")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties preserved_inferno$modifyBeef(Item.Properties original) {
        return original.food(Foods.BEEF, Consumable.builder()
                .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HUNGER, 300, 0), 0.3F))
                .build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=chicken")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties preserved_inferno$modifyChicken(Item.Properties original) {
        return original.food(Foods.CHICKEN, Consumable.builder()
                .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HUNGER, 300, 0), 0.3F))
                .build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=cod")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties preserved_inferno$modifyCod(Item.Properties original) {
        return original.food(Foods.COD, Consumable.builder()
                .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HUNGER, 300, 0), 0.3F))
                .build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=mutton")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties preserved_inferno$modifyMutton(Item.Properties original) {
        return original.food(Foods.MUTTON, Consumable.builder()
                .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HUNGER, 300, 0), 0.3F))
                .build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=porkchop")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties preserved_inferno$modifyPorkchop(Item.Properties original) {
        return original.food(Foods.PORKCHOP, Consumable.builder()
                .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HUNGER, 300, 0), 0.3F))
                .build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=rabbit")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties preserved_inferno$modifyRabbit(Item.Properties original) {
        return original.food(Foods.RABBIT, Consumable.builder()
                .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HUNGER, 300, 0), 0.3F))
                .build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=salmon")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties preserved_inferno$modifySalmon(Item.Properties original) {
        return original.food(Foods.SALMON, Consumable.builder()
                .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HUNGER, 300, 0), 0.3F))
                .build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=rotten_flesh")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties preserved_inferno$modifyRottenFlesh(Item.Properties original) {
        return original.food(Foods.ROTTEN_FLESH, Consumable.builder()
                .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 1.0F))
                .build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=spider_eye")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties preserved_inferno$modifySpiderEye(Item.Properties original) {
        return original.food(Foods.SPIDER_EYE, Consumable.builder().consumeSeconds(0.8F).build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=sweet_berries")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties preserved_inferno$modifySweetBerries(Item.Properties original) {
        return original.food(Foods.SWEET_BERRIES, Consumable.builder().consumeSeconds(0.8F).build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=porkchop")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties preserved_inferno$modifyTropicalFish(Item.Properties original) {
        return original.food(Foods.TROPICAL_FISH, Consumable.builder()
                .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 1.0F))
                .build());
    }

    // make items edible
    @WrapOperation(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=glistering_melon_slice")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item preserved_inferno$modifyGlisteringMelonSlice(String id, Operation<Item> original) {
        return Items.registerItem("glistering_melon_slice",
                new Item.Properties()
                        .food(new FoodProperties.Builder().nutrition(6).saturationModifier(1.2F).alwaysEdible().build(),
                                Consumable.builder().consumeSeconds(0.8F)
                                        .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION, 80, 1), 1.0F))
                                        .onConsume(new ConsumeEffect() {
                                            @Override
                                            public @NotNull Type<? extends ConsumeEffect> getType() { return null; }

                                            @Override
                                            public boolean apply(@NotNull Level level, @NotNull ItemStack itemStack, @NotNull LivingEntity livingEntity) {
                                                if (livingEntity instanceof Player player) {
                                                    if (!level.isClientSide()) {
                                                        int currentHeat = ((HeatAccessor) player).preserved_inferno$getHeat();
                                                        if (currentHeat >= 1) {
                                                            ((HeatAccessor) player).preserved_inferno$decreaseHeat(1);
                                                        }
                                                    }
                                                }
                                                return false;
                                            }
                                        })
                                        .build()));
    }

    // modify elytra repair item
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=elytra")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties preserved_inferno$modifyElytra(Item.Properties original) {
        return original.durability(432)
                .rarity(Rarity.EPIC)
                .component(DataComponents.GLIDER, Unit.INSTANCE)
                .component(
                        DataComponents.EQUIPPABLE,
                        Equippable.builder(EquipmentSlot.CHEST)
                                .setEquipSound(SoundEvents.ARMOR_EQUIP_ELYTRA)
                                .setAsset(EquipmentAssets.ELYTRA)
                                .setDamageOnHurt(false).build()
                ).repairable(ModItems.HOLLOW_TWINE);
    }

    // catch item names
    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Ljava/util/function/Function;)Lnet/minecraft/world/item/Item;"
            ),
            index = 0
    )
    private static String preserved_inferno$catchItemName(String name) {
        if (RegisterItemChecker.AXES.contains(name)) {
            preserved_inferno$callFlip(name);
        }
        return name;
    }

    @Unique
    private static void preserved_inferno$callFlip(String itemName) {
        RegisterItemChecker.flip = true;
        RegisterItemChecker.itemName = itemName;
    }

    // modify sword attack damage
    @ModifyArg(method = "<clinit>", slice = @Slice(
            from = @At(value = "CONSTANT", args = "stringValue=wooden_sword"),
            to = @At(value = "CONSTANT", args = "stringValue=stone_sword")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item$Properties;sword(Lnet/minecraft/world/item/ToolMaterial;FF)Lnet/minecraft/world/item/Item$Properties;"),
            index = 1
    )
    private static float preserved_inferno$modifyWoodenSword(float attackDamage) {
        return 2.0F;
    }

    @ModifyArg(method = "<clinit>", slice = @Slice(
            from = @At(value = "CONSTANT", args = "stringValue=golden_sword"),
            to = @At(value = "CONSTANT", args = "stringValue=iron_sword")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item$Properties;sword(Lnet/minecraft/world/item/ToolMaterial;FF)Lnet/minecraft/world/item/Item$Properties;"),
            index = 1
    )
    private static float preserved_inferno$modifyGoldenSword(float attackDamage) {
        return 2.0F;
    }

    @ModifyArg(method = "<clinit>", slice = @Slice(
            from = @At(value = "CONSTANT", args = "stringValue=iron_sword"),
            to = @At(value = "CONSTANT", args = "stringValue=diamond_sword")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item$Properties;sword(Lnet/minecraft/world/item/ToolMaterial;FF)Lnet/minecraft/world/item/Item$Properties;"),
            index = 1
    )
    private static float preserved_inferno$modifyIronSword(float attackDamage) {
        return 2.0F;
    }

    @ModifyArg(method = "<clinit>", slice = @Slice(
            from = @At(value = "CONSTANT", args = "stringValue=stone_sword"),
            to = @At(value = "CONSTANT", args = "stringValue=golden_sword")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item$Properties;sword(Lnet/minecraft/world/item/ToolMaterial;FF)Lnet/minecraft/world/item/Item$Properties;"),
            index = 1
    )
    private static float preserved_inferno$modifyStoneSword(float attackDamage) {
        return 2.0F;
    }

    @ModifyArg(method = "<clinit>", slice = @Slice(
            from = @At(value = "CONSTANT", args = "stringValue=trident")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;",
            ordinal = 0
    ))
    private static Item.Properties preserved_inferno$modifyTrident(Item.Properties properties) {
        return new Item.Properties()
                .rarity(Rarity.EPIC)
                .durability(2031)
                .attributes(TridentItem.createAttributes())
                .component(DataComponents.TOOL, TridentItem.createToolProperties())
                .enchantable(1)
                .repairable(Items.PRISMARINE_CRYSTALS);
    }

    @ModifyArg(method = "<clinit>", slice = @Slice(
            from = @At(value = "CONSTANT", args = "stringValue=mace")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;",
            ordinal = 0
    ))
    private static Item.Properties preserved_inferno$modifyMace(Item.Properties properties) {
        return new Item.Properties()
                .rarity(Rarity.EPIC)
                .durability(2031)
                .component(DataComponents.TOOL, MaceItem.createToolProperties())
                .repairable(Items.BREEZE_ROD)
                .attributes(MaceItem.createAttributes())
                .enchantable(15)
                .component(DataComponents.WEAPON, new Weapon(1));
    }

    // modify durabilities
    @ModifyArg(method = "<clinit>", slice = @Slice(
            from = @At(value = "CONSTANT", args = "stringValue=bow")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;",
            ordinal = 0
    ))
    private static Item.Properties preserved_blizzard$modifyBow(Item.Properties properties) {
        return new Item.Properties().durability(465).enchantable(1);
    }

    @ModifyArg(method = "<clinit>", slice = @Slice(
            from = @At(value = "CONSTANT", args = "stringValue=fishing_rod")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;",
            ordinal = 0
    ))
    private static Item.Properties preserved_blizzard$modifyFishingRod(Item.Properties properties) {
        return new Item.Properties().enchantable(1).stacksTo(1);
    }
}
