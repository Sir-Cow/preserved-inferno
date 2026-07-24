package sircow.preservedinferno.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Unit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import sircow.preservedinferno.RegisterItemChecker;
import sircow.preservedinferno.components.ModComponents;
import sircow.preservedinferno.other.HeatAccessor;

import java.util.function.Function;

@Mixin(value = Items.class)
public class ItemsMixin {
    @Shadow private static Item registerItem(final ResourceKey<Item> id, final Item.Properties properties) {
        return registerItem(id, Item::new, properties);
    }

    @Shadow private static Item registerItem(final ResourceKey<Item> key, final Function<Item.Properties, Item> itemFactory, final Item.Properties properties) {
        Item item = itemFactory.apply(properties.setId(key));
        if (item instanceof BlockItem blockItem) blockItem.registerBlocks(Item.BY_BLOCK, item);
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    // modify stack sizes
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;POTION:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;stacksTo(I)Lnet/minecraft/world/item/Item$Properties;", ordinal = 0))
    private static int pinferno$modifyPotionStackSize(int old) { return 4; }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;SPLASH_POTION:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;stacksTo(I)Lnet/minecraft/world/item/Item$Properties;", ordinal = 0))
    private static int pinferno$modifySplashPotionStackSize(int old) { return 4; }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;LINGERING_POTION:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;stacksTo(I)Lnet/minecraft/world/item/Item$Properties;", ordinal = 0))
    private static int pinferno$modifyLingeringPotionStackSize(int old) { return 4; }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;BEETROOT_SOUP:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;stacksTo(I)Lnet/minecraft/world/item/Item$Properties;", ordinal = 0))
    private static int pinferno$modifyBeetrootSoupStackSize(int old) { return 16; }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;MUSHROOM_STEW:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;stacksTo(I)Lnet/minecraft/world/item/Item$Properties;", ordinal = 0))
    private static int pinferno$modifyMushroomStewStackSize(int old) { return 16; }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;RABBIT_STEW:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;stacksTo(I)Lnet/minecraft/world/item/Item$Properties;", ordinal = 0))
    private static int pinferno$modifyRabbitStewStackSize(int old) { return 16; }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;SUSPICIOUS_STEW:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;stacksTo(I)Lnet/minecraft/world/item/Item$Properties;", ordinal = 0))
    private static int pinferno$modifySuspiciousStewStackSize(int old) { return 16; }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;EGG:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;stacksTo(I)Lnet/minecraft/world/item/Item$Properties;", ordinal = 0))
    private static int pinferno$modifyEggStackSize(int old) { return 64; }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;BLUE_EGG:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;stacksTo(I)Lnet/minecraft/world/item/Item$Properties;", ordinal = 0))
    private static int pinferno$modifyBlueEggStackSize(int old) { return 64; }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;BROWN_EGG:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;stacksTo(I)Lnet/minecraft/world/item/Item$Properties;", ordinal = 0))
    private static int pinferno$modifyBrownEggStackSize(int old) { return 64; }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;SNOWBALL:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;stacksTo(I)Lnet/minecraft/world/item/Item$Properties;", ordinal = 0))
    private static int pinferno$modifySnowballStackSize(int old) { return 64; }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;ENDER_PEARL:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;stacksTo(I)Lnet/minecraft/world/item/Item$Properties;", ordinal = 0))
    private static int pinferno$modifyEnderPearlStackSize(int old) { return 64; }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;HONEY_BOTTLE:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties pinferno$modifyHoneyBottle(Item.Properties original) {
        return original
                .craftRemainder(Items.GLASS_BOTTLE)
                .food(Foods.HONEY_BOTTLE, Consumables.HONEY_BOTTLE)
                .usingConvertsTo(Items.GLASS_BOTTLE)
                .stacksTo(4)
                .component(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
    }

    // modify shears durability
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;SHEARS:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;durability(I)Lnet/minecraft/world/item/Item$Properties;", ordinal = 0))
    private static int pinferno$modifyShearsDurability(int old) { return 128; }

    // modify food (mainly to speed up eating time or modify status effects)
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;APPLE:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties pinferno$modifyApple(Item.Properties original) {
        return original.food(Foods.APPLE, Consumable.builder().consumeSeconds(0.8F).build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;BEETROOT:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties pinferno$modifyBeetroot(Item.Properties original) {
        return original.food(Foods.BEETROOT, Consumable.builder().consumeSeconds(0.8F).build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;CARROT_CROP:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties pinferno$modifyCarrot(Item.Properties original) {
        return original.food(Foods.CARROT, Consumable.builder().consumeSeconds(0.8F).build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;COOKIE:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties pinferno$modifyCookie(Item.Properties original) {
        return original.food(Foods.COOKIE, Consumable.builder().consumeSeconds(0.8F).build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;GLOW_BERRY_CROP:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties pinferno$modifyGlowBerries(Item.Properties original) {
        return original.food(Foods.GLOW_BERRIES, Consumable.builder().consumeSeconds(0.8F).build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;MELON_SLICE:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties pinferno$modifyMelonSlice(Item.Properties original) {
        return original.food(Foods.MELON_SLICE, Consumable.builder().consumeSeconds(0.8F)
                .onConsume(new ConsumeEffect() {
                    @Override
                    public @NotNull Type<? extends ConsumeEffect> getType() { return null; }

                    @Override
                    public boolean apply(@NotNull Level level, @NotNull ItemStack itemStack, @NotNull LivingEntity livingEntity) {
                        if (livingEntity instanceof Player player) {
                            if (!level.isClientSide()) {
                                int currentHeat = ((HeatAccessor) player).pinferno$getHeat();
                                if (currentHeat >= 1) ((HeatAccessor) player).pinferno$decreaseHeat(1);
                            }
                        }
                        return false;
                    }
                })
                .build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;POTATO_CROP:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties pinferno$modifyPotato(Item.Properties original) {
        return original.food(Foods.POTATO, Consumable.builder().consumeSeconds(0.8F).build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;BEEF:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties pinferno$modifyBeef(Item.Properties original) {
        return original.food(Foods.BEEF, Consumable.builder()
                .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HUNGER, 300, 0), 0.3F))
                .build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;COD:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties pinferno$modifyCod(Item.Properties original) {
        return original.food(Foods.COD, Consumable.builder()
                .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HUNGER, 300, 0), 0.3F))
                .build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;MUTTON:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties pinferno$modifyMutton(Item.Properties original) {
        return original.food(Foods.MUTTON, Consumable.builder()
                .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HUNGER, 300, 0), 0.3F))
                .build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;PORKCHOP:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties pinferno$modifyPorkchop(Item.Properties original) {
        return original.food(Foods.PORKCHOP, Consumable.builder()
                .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HUNGER, 300, 0), 0.3F))
                .build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;RABBIT:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties pinferno$modifyRabbit(Item.Properties original) {
        return original.food(Foods.RABBIT, Consumable.builder()
                .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HUNGER, 300, 0), 0.3F))
                .build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;SALMON:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties pinferno$modifySalmon(Item.Properties original) {
        return original.food(Foods.SALMON, Consumable.builder()
                .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HUNGER, 300, 0), 0.3F))
                .build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/BlockItemIds;SWEET_BERRY_CROP:Lnet/minecraft/references/BlockItemId;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/references/BlockItemId;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties pinferno$modifySweetBerries(Item.Properties original) {
        return original.food(Foods.SWEET_BERRIES, Consumable.builder().consumeSeconds(0.8F).build());
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;TROPICAL_FISH:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties pinferno$modifyTropicalFish(Item.Properties original) {
        return original.food(Foods.TROPICAL_FISH, Consumable.builder()
                .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 1.0F))
                .build());
    }

    // make items edible
    @WrapOperation(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;GLISTERING_MELON_SLICE:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item pinferno$modifyGlisteringMelonSlice(ResourceKey<Item> id, Operation<Item> original) {
        return registerItem(ItemIds.GLISTERING_MELON_SLICE,
                new Item.Properties()
                        .food(new FoodProperties.Builder().nutrition(6).saturationModifier(1.2F).alwaysEdible().build(),
                                Consumable.builder().consumeSeconds(0.8F)
                                        .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION, 160, 1), 1.0F))
                                        .onConsume(new ConsumeEffect() {
                                            @Override
                                            public @NotNull Type<? extends ConsumeEffect> getType() { return null; }

                                            @Override
                                            public boolean apply(@NotNull Level level, @NotNull ItemStack itemStack, @NotNull LivingEntity livingEntity) {
                                                if (livingEntity instanceof Player player) {
                                                    if (!level.isClientSide()) {
                                                        int currentHeat = ((HeatAccessor) player).pinferno$getHeat();
                                                        if (currentHeat >= 1) ((HeatAccessor) player).pinferno$decreaseHeat(1);
                                                    }
                                                }
                                                return false;
                                            }
                                        })
                                        .build()));
    }

    // remove elytra repair item
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;ELYTRA:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties pinferno$modifyElytra(Item.Properties original) {
        return original.durability(432)
                .rarity(Rarity.EPIC)
                .component(DataComponents.GLIDER, Unit.INSTANCE)
                .component(DataComponents.EQUIPPABLE,
                        Equippable.builder(EquipmentSlot.CHEST)
                                .setEquipSound(SoundEvents.ARMOR_EQUIP_ELYTRA)
                                .setAsset(EquipmentAssets.ELYTRA)
                                .setDamageOnHurt(false).build()
                );
    }

    // catch item names
    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Ljava/util/function/Function;)Lnet/minecraft/world/item/Item;"), index = 0)
    private static ResourceKey<Item> pinferno$catchItemName2Arg(ResourceKey<Item> id) {
        handleItemName(id);
        return id;
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;"), index = 0)
    private static ResourceKey<Item> pinferno$catchItemName3Arg(ResourceKey<Item> id) {
        handleItemName(id);
        return id;
    }

    @Unique
    private static void handleItemName(ResourceKey<Item> id) {
        String name = id.identifier().getPath();
        if (RegisterItemChecker.AXES.contains(name) || RegisterItemChecker.SHOVELS.contains(name)) {
            callFlip(name);
        }
    }

    @Unique
    private static void callFlip(String itemName) {
        RegisterItemChecker.flip = true;
        RegisterItemChecker.itemName = itemName;
    }

    // modify sword attack damage
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;WOODEN_SWORD:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;sword(Lnet/minecraft/world/item/ToolMaterial;FF)Lnet/minecraft/world/item/Item$Properties;", ordinal = 0), index = 1)
    private static float pinferno$modifyWoodenSword(float attackDamage) {
        return 1.0F;
    }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;GOLDEN_SWORD:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;sword(Lnet/minecraft/world/item/ToolMaterial;FF)Lnet/minecraft/world/item/Item$Properties;", ordinal = 0), index = 1)
    private static float pinferno$modifyGoldenSword(float attackDamage) {
        return 3.0F;
    }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;IRON_SWORD:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;sword(Lnet/minecraft/world/item/ToolMaterial;FF)Lnet/minecraft/world/item/Item$Properties;", ordinal = 0), index = 1)
    private static float pinferno$modifyIronSword(float attackDamage) {
        return 2.0F;
    }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;COPPER_SWORD:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;sword(Lnet/minecraft/world/item/ToolMaterial;FF)Lnet/minecraft/world/item/Item$Properties;", ordinal = 0), index = 1)
    private static float pinferno$modifyCopperSword(float attackDamage) {
        return 2.0F;
    }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;STONE_SWORD:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;sword(Lnet/minecraft/world/item/ToolMaterial;FF)Lnet/minecraft/world/item/Item$Properties;", ordinal = 0), index = 1)
    private static float pinferno$modifyStoneSword(float attackDamage) {
        return 1.0F;
    }

    // shovels
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;NETHERITE_SHOVEL:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0), index = 1)
    private static Function<Item.Properties, Item> pinferno$modifyNetheriteShovel(Function<Item.Properties, Item> itemFactory) {
        return properties -> new ShovelItem(ToolMaterial.NETHERITE, 3.0F, -2.6F, properties);
    }

    // other weapons
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;TRIDENT:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties pinferno$modifyTrident(Item.Properties properties) {
        return new Item.Properties()
                .rarity(Rarity.EPIC)
                .durability(2048)
                .attributes(TridentItem.createAttributes())
                .component(DataComponents.TOOL, TridentItem.createToolProperties())
                .enchantable(1)
                .component(DataComponents.WEAPON, new Weapon(1));
    }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;MACE:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties pinferno$modifyMace(Item.Properties properties) {
        return new Item.Properties()
                .rarity(Rarity.EPIC)
                .durability(2048)
                .component(DataComponents.TOOL, MaceItem.createToolProperties())
                .attributes(MaceItem.createAttributes())
                .enchantable(15)
                .component(DataComponents.WEAPON, new Weapon(1));
    }

    // modify durabilities
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;BOW:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties pinferno$modifyBow(Item.Properties properties) {
        return new Item.Properties().durability(465).enchantable(1);
    }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;FISHING_ROD:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties pinferno$modifyFishingRod(Item.Properties properties) {
        return new Item.Properties()
                .enchantable(1)
                .stacksTo(1)
                .component(ModComponents.HOOK_COMPONENT, "none")
                .component(ModComponents.HOOK_DURABILITY, 0)
                .component(ModComponents.LINE_COMPONENT, "none")
                .component(ModComponents.LINE_DURABILITY, 0)
                .component(ModComponents.SINKER_COMPONENT, "none")
                .component(ModComponents.SINKER_DURABILITY, 0)
                .component(ModComponents.HOOK_UNBREAKING, 0)
                .component(ModComponents.LINE_UNBREAKING, 0)
                .component(ModComponents.SINKER_UNBREAKING, 0);
    }

    // modify chainmail rarity
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;CHAINMAIL_HELMET:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties pinferno$modifyChainmail1(Item.Properties properties) {
        return new Item.Properties().humanoidArmor(ArmorMaterials.CHAINMAIL, ArmorType.HELMET).rarity(Rarity.COMMON);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;CHAINMAIL_CHESTPLATE:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties pinferno$modifyChainmail2(Item.Properties properties) {
        return new Item.Properties().humanoidArmor(ArmorMaterials.CHAINMAIL, ArmorType.CHESTPLATE).rarity(Rarity.COMMON);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;CHAINMAIL_LEGGINGS:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties pinferno$modifyChainmail3(Item.Properties properties) {
        return new Item.Properties().humanoidArmor(ArmorMaterials.CHAINMAIL, ArmorType.LEGGINGS).rarity(Rarity.COMMON);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;CHAINMAIL_BOOTS:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Item.Properties pinferno$modifyChainmail4(Item.Properties properties) {
        return new Item.Properties().humanoidArmor(ArmorMaterials.CHAINMAIL, ArmorType.BOOTS).rarity(Rarity.COMMON);
    }

    // modify hoe properties
    @ModifyArg(method = "<clinit>", slice = @Slice (from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;WOODEN_HOE:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Ljava/util/function/Function;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Function<Item.Properties, Item> pinferno$modifyWoodenHoe(Function<Item.Properties, Item> p) {
        return (properties) -> new HoeItem(ToolMaterial.WOOD, 0.0F, -2.2F, properties);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice (from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;GOLDEN_HOE:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Ljava/util/function/Function;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Function<Item.Properties, Item> pinferno$modifyGoldenHoe(Function<Item.Properties, Item> p) {
        return (properties) -> new HoeItem(ToolMaterial.GOLD, 2.0F, -2.2F, properties);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice (from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;STONE_HOE:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Ljava/util/function/Function;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Function<Item.Properties, Item> pinferno$modifyStoneHoe(Function<Item.Properties, Item> p) {
        return (properties) -> new HoeItem(ToolMaterial.STONE, 0.0F, -2.2F, properties);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice (from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;COPPER_HOE:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Ljava/util/function/Function;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Function<Item.Properties, Item> pinferno$modifyCopperHoe(Function<Item.Properties, Item> p) {
        return (properties) -> new HoeItem(ToolMaterial.COPPER, 1.0F, -2.2F, properties);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice (from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;IRON_HOE:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Ljava/util/function/Function;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Function<Item.Properties, Item> pinferno$modifyIronHoe(Function<Item.Properties, Item> p) {
        return (properties) -> new HoeItem(ToolMaterial.IRON, 1.0F, -2.2F, properties);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice (from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;DIAMOND_HOE:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Ljava/util/function/Function;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Function<Item.Properties, Item> pinferno$modifyDiamondHoe(Function<Item.Properties, Item> p) {
        return (properties) -> new HoeItem(ToolMaterial.DIAMOND, 2.0F, -2.2F, properties);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice (from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;NETHERITE_HOE:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
    private static Function<Item.Properties, Item> pinferno$modifyNetheriteHoe(Function<Item.Properties, Item> p) {
        return (properties) -> new HoeItem(ToolMaterial.NETHERITE, 2.0F, -2.2F, properties);
    }

    // modify pickaxe properties
    @ModifyArg(method = "<clinit>", slice = @Slice (from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;WOODEN_PICKAXE:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0), index = 1)
    private static Item.Properties pinferno$modifyWoodenPickaxe(Item.Properties properties) {
        return new Item.Properties().pickaxe(ToolMaterial.WOOD, 2.0F, -3.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice (from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;GOLDEN_PICKAXE:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0), index = 1)
    private static Item.Properties pinferno$modifyGoldenPickaxe(Item.Properties properties) {
        return new Item.Properties().pickaxe(ToolMaterial.GOLD, 4.0F, -3.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice (from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;STONE_PICKAXE:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0), index = 1)
    private static Item.Properties pinferno$modifyStonePickaxe(Item.Properties properties) {
        return new Item.Properties().pickaxe(ToolMaterial.STONE, 2.0F, -3.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice (from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;COPPER_PICKAXE:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0), index = 1)
    private static Item.Properties pinferno$modifyCopperPickaxe(Item.Properties properties) {
        return new Item.Properties().pickaxe(ToolMaterial.COPPER, 3.0F, -3.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice (from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;IRON_PICKAXE:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0), index = 1)
    private static Item.Properties pinferno$modifyIronPickaxe(Item.Properties properties) {
        return new Item.Properties().pickaxe(ToolMaterial.IRON, 3.0F, -3.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice (from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;DIAMOND_PICKAXE:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0), index = 1)
    private static Item.Properties pinferno$modifyDiamondPickaxe(Item.Properties properties) {
        return new Item.Properties().pickaxe(ToolMaterial.DIAMOND, 4.0F, -3.0F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice (from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;NETHERITE_PICKAXE:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0), index = 1)
    private static Item.Properties pinferno$modifyNetheritePickaxe(Item.Properties properties) {
        return new Item.Properties().pickaxe(ToolMaterial.NETHERITE, 4.0F, -3.0F).fireResistant();
    }

    // modify spear properties
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;WOODEN_SPEAR:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;spear(Lnet/minecraft/world/item/ToolMaterial;FFFFFFFFF)Lnet/minecraft/world/item/Item$Properties;"), index = 2)
    private static float pinferno$modifyWoodenSpear(float damageMultiplier) {
        return 0.66F;
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;STONE_SPEAR:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;spear(Lnet/minecraft/world/item/ToolMaterial;FFFFFFFFF)Lnet/minecraft/world/item/Item$Properties;"), index = 2)
    private static float pinferno$modifyStoneSpear(float damageMultiplier) {
        return 0.7F;
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;COPPER_SPEAR:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;"), index = 1)
    private static Item.Properties pinferno$modifyCopperSpear(Item.Properties properties) {
        return setAttributes(properties, ToolMaterial.COPPER.attackDamageBonus() + 1.0F, 1.18F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;IRON_SPEAR:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;"), index = 1)
    private static Item.Properties pinferno$modifyIronSpear(Item.Properties properties) {
        return setAttributes(properties, ToolMaterial.IRON.attackDamageBonus() + 1.0F, 1.05F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;GOLDEN_SPEAR:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;"), index = 1)
    private static Item.Properties pinferno$modifyGoldenSpear(Item.Properties properties) {
        Item.Properties newProps = new Item.Properties().spear(ToolMaterial.GOLD, 1.54F, 0.82F, 0.65F, 2.5F, 9.0F, 5.5F, 5.1F, 8.75F, 4.6F);
        return setAttributes(newProps, ToolMaterial.GOLD.attackDamageBonus() + 2.0F, 1.54F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;DIAMOND_SPEAR:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;"), index = 1)
    private static Item.Properties pinferno$modifyDiamondSpear(Item.Properties properties) {
        return setAttributes(properties, ToolMaterial.DIAMOND.attackDamageBonus() + 2.0F, 0.95F);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;NETHERITE_SPEAR:Lnet/minecraft/resources/ResourceKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;"), index = 1)
    private static Item.Properties pinferno$modifyNetheriteSpear(Item.Properties properties) {
        Item.Properties newProps = new Item.Properties().spear(ToolMaterial.NETHERITE, 1.15F, 1.2F, 0.4F, 2.5F, 9.0F, 5.5F, 5.1F, 8.75F, 4.6F).fireResistant();
        return setAttributes(newProps, ToolMaterial.NETHERITE.attackDamageBonus() + 2.0F, 0.87F);
    }

    @Unique
    private static Item.Properties setAttributes(Item.Properties properties, float totalAttackDamage, float speedModifier) {
        int durationTicks = (int) Math.round(20.0 / speedModifier);
        return properties
                .component(DataComponents.SWING_ANIMATION, new SwingAnimation(SwingAnimationType.STAB, durationTicks))
                .component(DataComponents.ATTRIBUTE_MODIFIERS,
                ItemAttributeModifiers.builder()
                        .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, totalAttackDamage, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                        .add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, speedModifier - 4.0F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                        .build()
        );
    }
}
