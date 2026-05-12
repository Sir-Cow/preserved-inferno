package sircow.preservedinferno.client;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.book.BookModel;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.enchantment.ModEnchantments;
import sircow.preservedinferno.other.ModTags;
import sircow.preservedinferno.screen.PreservedEnchantmentMenu;
import sircow.preservedinferno.sound.ModSounds;

import java.util.*;

public class PreservedEnchantingTableScreen extends AbstractContainerScreen<PreservedEnchantmentMenu> {
    private static final Identifier[] LEVEL_TEXTURES = new Identifier[]{
            Constants.id("container/enchanting_table/10_levels_enabled"),
            Constants.id("container/enchanting_table/20_levels_enabled"),
            Constants.id("container/enchanting_table/30_levels_enabled")
    };
    private static final Identifier[] LEVEL_DISABLED_TEXTURES = new Identifier[]{
            Constants.id("container/enchanting_table/10_levels_disabled"),
            Constants.id("container/enchanting_table/20_levels_disabled"),
            Constants.id("container/enchanting_table/30_levels_disabled")
    };
    private static final Identifier[] ENCHANTMENT_ICON_TEXTURES = new Identifier[]{
            Constants.id("container/enchanting_table/enchant_overlay/aqua_affinity"), // 0
            Constants.id("container/enchanting_table/enchant_overlay/bane_of_arthropods"), // 1
            Constants.id("container/enchanting_table/enchant_overlay/blast_protection"), // 2
            Constants.id("container/enchanting_table/enchant_overlay/breach"), // 3
            Constants.id("container/enchanting_table/enchant_overlay/buckler"), // 4
            Constants.id("container/enchanting_table/enchant_overlay/channeling"), // 5
            Constants.id("container/enchanting_table/enchant_overlay/density"), // 6
            Constants.id("container/enchanting_table/enchant_overlay/depth_strider"), // 7
            Constants.id("container/enchanting_table/enchant_overlay/efficiency"), // 8
            Constants.id("container/enchanting_table/enchant_overlay/endurance"), // 9
            Constants.id("container/enchanting_table/enchant_overlay/feather_falling"), // 10
            Constants.id("container/enchanting_table/enchant_overlay/fire_aspect"), // 11
            Constants.id("container/enchanting_table/enchant_overlay/fire_protection"), // 12
            Constants.id("container/enchanting_table/enchant_overlay/flame"), // 13
            Constants.id("container/enchanting_table/enchant_overlay/fortune"), // 14
            Constants.id("container/enchanting_table/enchant_overlay/impaling"), // 15
            Constants.id("container/enchanting_table/enchant_overlay/infinity"), // 16
            Constants.id("container/enchanting_table/enchant_overlay/knockback"), // 17
            Constants.id("container/enchanting_table/enchant_overlay/looting"), // 18
            Constants.id("container/enchanting_table/enchant_overlay/loyalty"), // 19
            Constants.id("container/enchanting_table/enchant_overlay/lunge"), // 20
            Constants.id("container/enchanting_table/enchant_overlay/multishot"), // 21
            Constants.id("container/enchanting_table/enchant_overlay/piercing"), // 22
            Constants.id("container/enchanting_table/enchant_overlay/power"), // 23
            Constants.id("container/enchanting_table/enchant_overlay/projectile_protection"), // 24
            Constants.id("container/enchanting_table/enchant_overlay/protection"), // 25
            Constants.id("container/enchanting_table/enchant_overlay/punch"), // 26
            Constants.id("container/enchanting_table/enchant_overlay/quick_charge"), // 27
            Constants.id("container/enchanting_table/enchant_overlay/respiration"), // 28
            Constants.id("container/enchanting_table/enchant_overlay/respite"), // 29
            Constants.id("container/enchanting_table/enchant_overlay/riptide"), // 30
            Constants.id("container/enchanting_table/enchant_overlay/sharpness"), // 31
            Constants.id("container/enchanting_table/enchant_overlay/silk_touch"), // 32
            Constants.id("container/enchanting_table/enchant_overlay/smite"), // 33
            Constants.id("container/enchanting_table/enchant_overlay/splattering"), // 34
            Constants.id("container/enchanting_table/enchant_overlay/sweeping_edge"), // 35
            Constants.id("container/enchanting_table/enchant_overlay/thorns"), // 36
            Constants.id("container/enchanting_table/enchant_overlay/unbreaking"), // 37
            Constants.id("container/enchanting_table/enchant_overlay/vigor") // 38
    };

    private static final Identifier ENCHANTMENT_SLOT_DISABLED_TEXTURE = Constants.id("container/enchanting_table/enchantment_slot_disabled");
    private static final Identifier ENCHANTMENT_SLOT_HIGHLIGHTED_TEXTURE = Constants.id("container/enchanting_table/enchantment_slot_highlighted");
    private static final Identifier ENCHANTMENT_SLOT_TEXTURE = Constants.id("container/enchanting_table/enchantment_slot");
    private static final Identifier TEXTURE = Constants.id("textures/gui/container/preserved_enchanting_table_gui.png");
    private static final Identifier BOOK_TEXTURE = Identifier.withDefaultNamespace("textures/entity/enchantment/enchanting_table_book.png");
    private static final Identifier SCROLLER_TEXTURE = Constants.id("container/enchanting_table/scroller");
    private static final Identifier SCROLLER_DISABLED_TEXTURE = Constants.id("container/enchanting_table/scroller_disabled");
    private final RandomSource random = RandomSource.create();
    private ItemStack last = ItemStack.EMPTY;
    private String itemCategory;
    public Level world;
    private BookModel bookModel;
    private boolean itemInEnchantSlot, scrolling, tenTextureActive, twentyTextureActive, thirtyTextureActive;
    public float flip, oFlip, flipT, flipA, open, oOpen, scrollAmount;
    private int scrollOffset, enchPower;

    private static final Map<String, Set<Integer>> itemCategorySlots = new HashMap<>();
    static {
        // sword
        itemCategorySlots.put("sword", Set.of(idx("Bane Of Arthropods"), idx("Fire Aspect"), idx("Knockback"), idx("Looting"), idx("Sharpness"), idx("Smite"), idx("Sweeping Edge"), idx("Unbreaking")));
        itemCategorySlots.put("swordBane", Set.of(idx("Bane Of Arthropods"), idx("Fire Aspect"), idx("Knockback"), idx("Looting"), idx("Sweeping Edge"), idx("Unbreaking")));
        itemCategorySlots.put("swordSharp", Set.of(idx("Fire Aspect"), idx("Knockback"), idx("Looting"), idx("Sharpness"), idx("Sweeping Edge"), idx("Unbreaking")));
        itemCategorySlots.put("swordSmite", Set.of(idx("Fire Aspect"), idx("Knockback"), idx("Looting"), idx("Smite"), idx("Sweeping Edge"), idx("Unbreaking")));
        // spear
        itemCategorySlots.put("spear", Set.of(idx("Bane Of Arthropods"), idx("Fire Aspect"), idx("Looting"), idx("Lunge"), idx("Sharpness"), idx("Smite"), idx("Unbreaking")));
        itemCategorySlots.put("spearBane", Set.of(idx("Bane Of Arthropods"), idx("Fire Aspect"), idx("Looting"), idx("Lunge"), idx("Unbreaking")));
        itemCategorySlots.put("spearSharp", Set.of(idx("Fire Aspect"), idx("Looting"), idx("Lunge"), idx("Sharpness"), idx("Unbreaking")));
        itemCategorySlots.put("spearSmite", Set.of(idx("Fire Aspect"), idx("Looting"), idx("Lunge"), idx("Smite"), idx("Unbreaking")));
        // pickaxe
        itemCategorySlots.put("pickaxe", Set.of(idx("Bane Of Arthropods"), idx("Breach"), idx("Efficiency"), idx("Fortune"), idx("Looting"), idx("Sharpness"), idx("Silk Touch"), idx("Smite"), idx("Unbreaking")));
        itemCategorySlots.put("pickaxeFort", Set.of(idx("Bane Of Arthropods"), idx("Breach"), idx("Efficiency"), idx("Fortune"), idx("Looting"), idx("Sharpness"), idx("Smite"), idx("Unbreaking")));
        itemCategorySlots.put("pickaxeSilk", Set.of(idx("Bane Of Arthropods"), idx("Breach"), idx("Efficiency"), idx("Looting"), idx("Sharpness"), idx("Silk Touch"), idx("Smite"), idx("Unbreaking")));
        itemCategorySlots.put("pickaxeFortBane", Set.of(idx("Bane Of Arthropods"), idx("Breach"), idx("Efficiency"), idx("Fortune"), idx("Looting"), idx("Unbreaking")));
        itemCategorySlots.put("pickaxeFortSharp", Set.of(idx("Breach"), idx("Efficiency"), idx("Fortune"), idx("Looting"), idx("Sharpness"), idx("Unbreaking")));
        itemCategorySlots.put("pickaxeFortSmite", Set.of(idx("Breach"), idx("Efficiency"), idx("Fortune"), idx("Looting"), idx("Smite"), idx("Unbreaking")));
        itemCategorySlots.put("pickaxeSilkBane", Set.of(idx("Bane Of Arthropods"), idx("Breach"), idx("Efficiency"), idx("Looting"), idx("Silk Touch"), idx("Unbreaking")));
        itemCategorySlots.put("pickaxeSilkSharp", Set.of(idx("Breach"), idx("Efficiency"), idx("Looting"), idx("Sharpness"), idx("Silk Touch"), idx("Unbreaking")));
        itemCategorySlots.put("pickaxeSilkSmite", Set.of(idx("Breach"), idx("Efficiency"), idx("Looting"), idx("Silk Touch"), idx("Smite"), idx("Unbreaking")));
        itemCategorySlots.put("pickaxeBane", Set.of(idx("Bane Of Arthropods"), idx("Breach"), idx("Efficiency"), idx("Fortune"), idx("Looting"), idx("Silk Touch"), idx("Unbreaking")));
        itemCategorySlots.put("pickaxeSharp", Set.of(idx("Breach"), idx("Efficiency"), idx("Fortune"), idx("Looting"), idx("Sharpness"), idx("Silk Touch"), idx("Unbreaking")));
        itemCategorySlots.put("pickaxeSmite", Set.of(idx("Breach"), idx("Efficiency"), idx("Fortune"), idx("Looting"), idx("Silk Touch"), idx("Smite"), idx("Unbreaking")));
        // shovel
        itemCategorySlots.put("shovel", Set.of(idx("Bane Of Arthropods"), idx("Breach"), idx("Efficiency"), idx("Fortune"), idx("Looting"), idx("Sharpness"), idx("Silk Touch"), idx("Smite"), idx("Splattering"), idx("Unbreaking")));
        itemCategorySlots.put("shovelFort", Set.of(idx("Bane Of Arthropods"), idx("Breach"), idx("Efficiency"), idx("Fortune"), idx("Looting"), idx("Sharpness"), idx("Smite"), idx("Splattering"), idx("Unbreaking")));
        itemCategorySlots.put("shovelSilk", Set.of(idx("Bane Of Arthropods"), idx("Breach"), idx("Efficiency"), idx("Looting"), idx("Sharpness"), idx("Silk Touch"), idx("Smite"), idx("Splattering"), idx("Unbreaking")));
        itemCategorySlots.put("shovelFortBane", Set.of(idx("Bane Of Arthropods"), idx("Breach"), idx("Efficiency"), idx("Fortune"), idx("Looting"), idx("Splattering"), idx("Unbreaking")));
        itemCategorySlots.put("shovelFortSharp", Set.of(idx("Breach"), idx("Efficiency"), idx("Fortune"), idx("Looting"), idx("Sharpness"), idx("Splattering"), idx("Unbreaking")));
        itemCategorySlots.put("shovelFortSmite", Set.of(idx("Breach"), idx("Efficiency"), idx("Fortune"), idx("Looting"), idx("Smite"), idx("Splattering"), idx("Unbreaking")));
        itemCategorySlots.put("shovelSilkBane", Set.of(idx("Bane Of Arthropods"), idx("Breach"), idx("Efficiency"), idx("Looting"), idx("Silk Touch"), idx("Splattering"), idx("Unbreaking")));
        itemCategorySlots.put("shovelSilkSharp", Set.of(idx("Breach"), idx("Efficiency"), idx("Looting"), idx("Sharpness"), idx("Silk Touch"), idx("Splattering"), idx("Unbreaking")));
        itemCategorySlots.put("shovelSilkSmite", Set.of(idx("Breach"), idx("Efficiency"), idx("Looting"), idx("Silk Touch"), idx("Smite"), idx("Splattering"), idx("Unbreaking")));
        itemCategorySlots.put("shovelBane", Set.of(idx("Bane Of Arthropods"), idx("Breach"), idx("Efficiency"), idx("Fortune"), idx("Looting"), idx("Silk Touch"), idx("Splattering"), idx("Unbreaking")));
        itemCategorySlots.put("shovelSharp", Set.of(idx("Breach"), idx("Efficiency"), idx("Fortune"), idx("Looting"), idx("Sharpness"), idx("Silk Touch"), idx("Splattering"), idx("Unbreaking")));
        itemCategorySlots.put("shovelSmite", Set.of(idx("Breach"), idx("Efficiency"), idx("Fortune"), idx("Looting"), idx("Silk Touch"), idx("Smite"), idx("Splattering"), idx("Unbreaking")));
        // tool weapon
        itemCategorySlots.put("toolWeapon", Set.of(idx("Bane Of Arthropods"), idx("Efficiency"), idx("Fortune"), idx("Looting"), idx("Sharpness"), idx("Silk Touch"), idx("Smite"), idx("Unbreaking")));
        itemCategorySlots.put("toolWeaponBane", Set.of(idx("Bane Of Arthropods"), idx("Efficiency"), idx("Fortune"), idx("Looting"), idx("Silk Touch"), idx("Unbreaking")));
        itemCategorySlots.put("toolWeaponSharp", Set.of(idx("Efficiency"), idx("Fortune"), idx("Looting"), idx("Sharpness"), idx("Silk Touch"), idx("Unbreaking")));
        itemCategorySlots.put("toolWeaponSmite", Set.of(idx("Efficiency"), idx("Fortune"), idx("Looting"), idx("Silk Touch"), idx("Smite"), idx("Unbreaking")));
        itemCategorySlots.put("toolFortWeapon", Set.of(idx("Bane Of Arthropods"), idx("Efficiency"), idx("Fortune"), idx("Looting"), idx("Sharpness"), idx("Smite"), idx("Unbreaking")));
        itemCategorySlots.put("toolFortWeaponBane", Set.of(idx("Bane Of Arthropods"), idx("Efficiency"), idx("Fortune"), idx("Looting"), idx("Unbreaking")));
        itemCategorySlots.put("toolFortWeaponSharp", Set.of(idx("Efficiency"), idx("Fortune"), idx("Looting"), idx("Sharpness"), idx("Unbreaking")));
        itemCategorySlots.put("toolFortWeaponSmite", Set.of(idx("Efficiency"), idx("Fortune"), idx("Looting"), idx("Smite"), idx("Unbreaking")));
        itemCategorySlots.put("toolSilkWeapon", Set.of(idx("Bane Of Arthropods"), idx("Efficiency"), idx("Looting"), idx("Sharpness"), idx("Silk Touch"), idx("Smite"), idx("Unbreaking")));
        itemCategorySlots.put("toolSilkWeaponBane", Set.of(idx("Bane Of Arthropods"), idx("Efficiency"), idx("Looting"), idx("Silk Touch"), idx("Unbreaking")));
        itemCategorySlots.put("toolSilkWeaponSharp", Set.of(idx("Efficiency"), idx("Looting"), idx("Sharpness"), idx("Silk Touch"), idx("Smite"), idx("Unbreaking")));
        itemCategorySlots.put("toolSilkWeaponSmite", Set.of(idx("Efficiency"), idx("Looting"), idx("Silk Touch"), idx("Smite"), idx("Unbreaking")));
        // multitool
        itemCategorySlots.put("multitool", Set.of(idx("Efficiency"), idx("Fortune"), idx("Silk Touch"), idx("Unbreaking")));
        itemCategorySlots.put("multitoolFort", Set.of(idx("Efficiency"), idx("Fortune"), idx("Unbreaking")));
        itemCategorySlots.put("multitoolSilk", Set.of(idx("Efficiency"), idx("Silk Touch"), idx("Unbreaking")));
        // bow
        itemCategorySlots.put("bow", Set.of(idx("Flame"), idx("Infinity"), idx("Power"), idx("Punch"), idx("Unbreaking")));
        itemCategorySlots.put("bowMending", Set.of(idx("Flame"), idx("Power"), idx("Punch"), idx("Unbreaking")));
        // trident
        itemCategorySlots.put("trident", Set.of(idx("Channeling"), idx("Impaling"), idx("Looting"), idx("Loyalty"), idx("Riptide"), idx("Unbreaking")));
        itemCategorySlots.put("tridentRip", Set.of(idx("Impaling"), idx("Looting"), idx("Loyalty"), idx("Riptide"), idx("Unbreaking")));
        itemCategorySlots.put("tridentWithoutRip", Set.of(idx("Channeling"), idx("Impaling"), idx("Looting"), idx("Loyalty"), idx("Unbreaking")));
        // crossbow
        itemCategorySlots.put("crossbow", Set.of(idx("Multishot"), idx("Piercing"), idx("Quick Charge"), idx("Unbreaking")));
        itemCategorySlots.put("crossbowMulti", Set.of(idx("Multishot"), idx("Quick Charge"), idx("Unbreaking")));
        itemCategorySlots.put("crossbowPierce", Set.of(idx("Piercing"), idx("Quick Charge"), idx("Unbreaking")));
        // mace
        itemCategorySlots.put("mace", Set.of(idx("Bane Of Arthropods"), idx("Breach"), idx("Density"), idx("Fire Aspect"), idx("Looting"), idx("Smite"), idx("Unbreaking")));
        itemCategorySlots.put("maceBane", Set.of(idx("Bane Of Arthropods"), idx("Breach"), idx("Density"), idx("Fire Aspect"), idx("Looting"), idx("Unbreaking")));
        itemCategorySlots.put("maceSmite", Set.of(idx("Breach"), idx("Density"), idx("Fire Aspect"), idx("Looting"), idx("Smite"), idx("Unbreaking")));
        itemCategorySlots.put("maceBreach", Set.of(idx("Bane Of Arthropods"), idx("Breach"), idx("Fire Aspect"), idx("Looting"), idx("Smite"), idx("Unbreaking")));
        itemCategorySlots.put("maceBreachBane", Set.of(idx("Bane Of Arthropods"), idx("Breach"), idx("Fire Aspect"), idx("Looting"), idx("Unbreaking")));
        itemCategorySlots.put("maceBreachSmite", Set.of(idx("Breach"), idx("Fire Aspect"), idx("Looting"), idx("Smite"), idx("Unbreaking")));
        itemCategorySlots.put("maceDensity", Set.of(idx("Bane Of Arthropods"), idx("Density"), idx("Fire Aspect"), idx("Looting"), idx("Smite"), idx("Unbreaking")));
        itemCategorySlots.put("maceDensityBane", Set.of(idx("Bane Of Arthropods"), idx("Density"), idx("Fire Aspect"), idx("Looting"), idx("Unbreaking")));
        itemCategorySlots.put("maceDensitySmite", Set.of(idx("Density"), idx("Fire Aspect"), idx("Looting"), idx("Smite"), idx("Unbreaking")));
        // shield
        itemCategorySlots.put("shieldEndurance", Set.of(idx("Buckler"), idx("Endurance"), idx("Respite"), idx("Unbreaking")));
        itemCategorySlots.put("shieldVigor", Set.of(idx("Buckler"), idx("Respite"), idx("Unbreaking"), idx("Vigor")));
        itemCategorySlots.put("shield", Set.of(idx("Buckler"), idx("Endurance"), idx("Respite"), idx("Unbreaking"), idx("Vigor")));
        // armour
        itemCategorySlots.put("helmet", Set.of(idx("Aqua Affinity"), idx("Blast Protection"), idx("Fire Protection"), idx("Projectile Protection"), idx("Protection"), idx("Respiration"), idx("Thorns"), idx("Unbreaking")));
        itemCategorySlots.put("chestplate", Set.of(idx("Blast Protection"), idx("Fire Protection"), idx("Projectile Protection"), idx("Protection"), idx("Thorns"), idx("Unbreaking")));
        itemCategorySlots.put("leggings", Set.of(idx("Blast Protection"), idx("Fire Protection"), idx("Projectile Protection"), idx("Protection"), idx("Thorns"), idx("Unbreaking")));
        itemCategorySlots.put("boots", Set.of(idx("Blast Protection"), idx("Depth Strider"), idx("Feather Falling"), idx("Fire Protection"), idx("Projectile Protection"), idx("Protection"), idx("Thorns"), idx("Unbreaking")));
        itemCategorySlots.put("helmetProt", Set.of(idx("Aqua Affinity"), idx("Protection"), idx("Respiration"), idx("Thorns"), idx("Unbreaking")));
        itemCategorySlots.put("helmetProj", Set.of(idx("Aqua Affinity"), idx("Projectile Protection"), idx("Respiration"), idx("Thorns"), idx("Unbreaking")));
        itemCategorySlots.put("helmetFire", Set.of(idx("Aqua Affinity"), idx("Fire Protection"), idx("Respiration"), idx("Thorns"), idx("Unbreaking")));
        itemCategorySlots.put("helmetBlast", Set.of(idx("Aqua Affinity"), idx("Blast Protection"), idx("Respiration"), idx("Thorns"), idx("Unbreaking")));
        itemCategorySlots.put("depthNoProt", Set.of(idx("Blast Protection"), idx("Feather Falling"), idx("Fire Protection"), idx("Projectile Protection"), idx("Protection"), idx("Thorns"), idx("Unbreaking")));
        itemCategorySlots.put("depthProt", Set.of(idx("Depth Strider"), idx("Feather Falling"), idx("Protection"), idx("Thorns"), idx("Unbreaking")));
        itemCategorySlots.put("depthProj", Set.of(idx("Depth Strider"), idx("Feather Falling"), idx("Projectile Protection"), idx("Thorns"), idx("Unbreaking")));
        itemCategorySlots.put("depthFire", Set.of(idx("Depth Strider"), idx("Feather Falling"), idx("Fire Protection"), idx("Thorns"), idx("Unbreaking")));
        itemCategorySlots.put("depthBlast", Set.of(idx("Blast Protection"), idx("Depth Strider"), idx("Feather Falling"), idx("Thorns"), idx("Unbreaking")));
        itemCategorySlots.put("frostWalkNoProt", Set.of(idx("Blast Protection"), idx("Feather Falling"), idx("Fire Protection"), idx("Projectile Protection"), idx("Protection"), idx("Thorns"), idx("Unbreaking")));
        itemCategorySlots.put("frostWalkProt", Set.of(idx("Feather Falling"), idx("Protection"), idx("Thorns"), idx("Unbreaking")));
        itemCategorySlots.put("frostWalkProj", Set.of(idx("Feather Falling"), idx("Projectile Protection"), idx("Thorns"), idx("Unbreaking")));
        itemCategorySlots.put("frostWalkFire", Set.of(idx("Feather Falling"), idx("Fire Protection"), idx("Thorns"), idx("Unbreaking")));
        itemCategorySlots.put("frostWalkBlast", Set.of(idx("Blast Protection"), idx("Feather Falling"), idx("Thorns"), idx("Unbreaking")));
        itemCategorySlots.put("armourProt", Set.of(idx("Protection"), idx("Thorns"), idx("Unbreaking")));
        itemCategorySlots.put("armourProj", Set.of(idx("Projectile Protection"), idx("Thorns"), idx("Unbreaking")));
        itemCategorySlots.put("armourFire", Set.of(idx("Fire Protection"), idx("Thorns"), idx("Unbreaking")));
        itemCategorySlots.put("armourBlast", Set.of(idx("Blast Protection"), idx("Thorns"), idx("Unbreaking")));
        // other
        itemCategorySlots.put("shears", Set.of(idx("Efficiency"), idx("Unbreaking")));
        itemCategorySlots.put("misc", Set.of(idx("Unbreaking")));
        itemCategorySlots.put("book", Set.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38));
    }

    public PreservedEnchantingTableScreen(PreservedEnchantmentMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        world = inventory.player.level();
    }

    @Override
    protected void init() {
        super.init();
        this.bookModel = new BookModel(this.minecraft.getEntityModels().bakeLayer(ModelLayers.BOOK));
        this.tenTextureActive = false;
        this.twentyTextureActive = false;
        this.thirtyTextureActive = false;
        this.menu.enchantSelected = false;
    }

    public void containerTick() {
        super.containerTick();
        this.tickBook();
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, i, j, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
        this.extractBook(graphics, i, j);

        int k = (int)(41.0F * this.scrollAmount);
        Identifier identifier = this.shouldScroll() ? SCROLLER_TEXTURE : SCROLLER_DISABLED_TEXTURE;
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, identifier, i + 156, j + 13 + k, 12, 15);
        int l = this.leftPos + 97;
        int m = this.topPos + 11;
        int n = this.scrollOffset + 16;
        this.renderIcons(graphics, l, m, n);
        this.renderEXPIcons(graphics, this.leftPos + 71, this.topPos + 13);
    }

    private void extractBook(final GuiGraphicsExtractor graphics, final int left, final int top) {
        float a = this.minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false);
        float open = Mth.lerp(a, this.oOpen, this.open);
        float flip = Mth.lerp(a, this.oFlip, this.flip);
        int x0 = left + 14;
        int y0 = top + 14;
        int x1 = x0 + 38;
        int y1 = y0 + 31;
        graphics.book(this.bookModel, BOOK_TEXTURE, 40.0F, open, flip, x0, y0, x1, y1);
    }

    private void renderIcons(GuiGraphicsExtractor graphics, int x, int y, int scrollOffset) {
        for (int i = this.scrollOffset; i < scrollOffset && i < ENCHANTMENT_ICON_TEXTURES.length; i++) {
            int j = i - this.scrollOffset;
            int k = x + j % 4 * 14;
            int l = j / 4;
            int m = y + l * 14 + 2;

            if (!this.itemInEnchantSlot) {
                this.tenTextureActive = false;
                this.twentyTextureActive = false;
                this.thirtyTextureActive = false;
                this.menu.enchantSelected = false;
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ENCHANTMENT_SLOT_DISABLED_TEXTURE, k, m, 14, 14);
            }
            Set<Integer> slots = itemCategorySlots.get(this.itemCategory);
            if (!this.menu.enchantSelected) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, slots != null && slots.contains(i) ? ENCHANTMENT_SLOT_TEXTURE : ENCHANTMENT_SLOT_DISABLED_TEXTURE, k, m, 14, 14);
            }
            else {
                if (i != this.menu.getSelectedEnchantID()) {
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, slots != null && slots.contains(i) ? ENCHANTMENT_SLOT_TEXTURE : ENCHANTMENT_SLOT_DISABLED_TEXTURE, k, m, 14, 14);
                }
                else {
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ENCHANTMENT_SLOT_HIGHLIGHTED_TEXTURE, k, m, 14, 14);
                    if (Objects.equals(PreservedEnchantmentMenu.ENCHANTMENT_DATA.get(i).levelCost(), "10")) {
                        this.tenTextureActive = true;
                        this.twentyTextureActive = false;
                        this.thirtyTextureActive = false;
                    }
                    else if (Objects.equals(PreservedEnchantmentMenu.ENCHANTMENT_DATA.get(i).levelCost(), "20")) {
                        this.tenTextureActive = false;
                        this.twentyTextureActive = true;
                        this.thirtyTextureActive = false;
                    }
                    else if (Objects.equals(PreservedEnchantmentMenu.ENCHANTMENT_DATA.get(i).levelCost(), "30")) {
                        this.tenTextureActive = false;
                        this.twentyTextureActive = false;
                        this.thirtyTextureActive = true;
                    }
                }
            }
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ENCHANTMENT_ICON_TEXTURES[i], k, m, 14, 14);
        }
    }

    private void renderEXPIcons(GuiGraphicsExtractor graphics, int x, int y) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, !tenTextureActive ? LEVEL_DISABLED_TEXTURES[0] : LEVEL_TEXTURES[0], x, y, 16, 16);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, !twentyTextureActive ? LEVEL_DISABLED_TEXTURES[1] : LEVEL_TEXTURES[1], x, y + 20, 16, 16);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, !thirtyTextureActive ? LEVEL_DISABLED_TEXTURES[2] : LEVEL_TEXTURES[2], x, y + 40, 16, 16);
    }

    @Override
    protected void extractTooltip(@NotNull GuiGraphicsExtractor graphics, int x, int y) {
        super.extractTooltip(graphics, x, y);
        int scrollOffset = this.scrollOffset + 16;
        Set<Integer> slots = itemCategorySlots.get(this.itemCategory);

        if (this.itemInEnchantSlot) {
            for (int i = this.scrollOffset; i < scrollOffset && i < ENCHANTMENT_ICON_TEXTURES.length; i++) {
                int j = i - this.scrollOffset;
                int k = 97 + j % 4 * 14;
                int l = j / 4;
                int m = 11 + l * 14 + 2;
                if (this.isHovering(k, m, 14, 14, x, y) && slots != null && slots.contains(i)) {
                    List<Component> list = Lists.newArrayList();
                    list.add(Component.literal(PreservedEnchantmentMenu.ENCHANTMENT_DATA.get(i).name() + " I"));
                    graphics.setComponentTooltipForNextFrame(this.font, list, x, y);
                    break;
                }
            }

            // hover over animated book
            if (this.isHovering(24, 18, 40, 24, x, y)) {
                List<Component> list = Lists.newArrayList();
                list.add(Component.translatable("enchanting_table.pinferno.bookshelf_power", this.enchPower));
                graphics.setComponentTooltipForNextFrame(this.font, list, x, y);
            }

            // hover over 10 exp
            if (this.isHovering(71, 13, 16, 16, x, y)) {
                List<Component> list = Lists.newArrayList();
                if (this.menu.getSlot(1).getItem().isEmpty() && !this.twentyTextureActive && !this.thirtyTextureActive) list.add(Component.translatable("enchanting_table.pinferno.requires_lapis_lazuli", 1));
                if (!this.tenTextureActive && !this.twentyTextureActive && !this.thirtyTextureActive && this.menu.enchantSelected) list.add(Component.translatable("enchanting_table.pinferno.levels_needed", 10));
                else if (this.itemInEnchantSlot && !this.menu.enchantSelected) list.add(Component.translatable("enchanting_table.pinferno.not_selected"));

                if (this.enchPower < 1 && !this.twentyTextureActive && !this.thirtyTextureActive) list.add(Component.translatable("enchanting_table.pinferno.no_bookshelf_power"));
                else if (this.tenTextureActive && this.menu.enchantSelected && !this.twentyTextureActive && !this.thirtyTextureActive) list.add(Component.translatable("enchanting_table.pinferno.enchant_for_levels", 10));

                graphics.setComponentTooltipForNextFrame(this.font, list, x, y);
            }

            // hover over 20 exp
            if (this.isHovering(71, 33, 16, 16, x, y)) {
                List<Component> list = Lists.newArrayList();
                if (this.menu.getSlot(1).getItem().isEmpty() && !this.tenTextureActive && !this.thirtyTextureActive) list.add(Component.translatable("enchanting_table.pinferno.requires_lapis_lazuli", 2));
                if (!this.tenTextureActive && !this.twentyTextureActive && !this.thirtyTextureActive && this.menu.enchantSelected) list.add(Component.translatable("enchanting_table.pinferno.levels_needed", 20));
                else if (this.itemInEnchantSlot && !this.menu.enchantSelected) list.add(Component.translatable("enchanting_table.pinferno.not_selected"));

                if (this.enchPower < 2 && !this.tenTextureActive && !this.thirtyTextureActive) list.add(Component.translatable("enchanting_table.pinferno.no_bookshelf_power"));
                else if (this.twentyTextureActive && this.menu.enchantSelected && !this.tenTextureActive && !this.thirtyTextureActive) list.add(Component.translatable("enchanting_table.pinferno.enchant_for_levels", 20));

                graphics.setComponentTooltipForNextFrame(this.font, list, x, y);
            }

            // hover over 30 exp
            if (this.isHovering(71, 53, 16, 16, x, y)) {
                List<Component> list = Lists.newArrayList();
                if (this.menu.getSlot(1).getItem().isEmpty() && !this.tenTextureActive && !this.twentyTextureActive) list.add(Component.translatable("enchanting_table.pinferno.requires_lapis_lazuli", 3));
                if (!this.tenTextureActive && !this.twentyTextureActive && !this.thirtyTextureActive && this.menu.enchantSelected) list.add(Component.translatable("enchanting_table.pinferno.levels_needed", 30));
                else if (this.itemInEnchantSlot && !this.menu.enchantSelected) list.add(Component.translatable("enchanting_table.pinferno.not_selected"));

                if (this.enchPower < 3 && !this.tenTextureActive && !this.twentyTextureActive) list.add(Component.translatable("enchanting_table.pinferno.no_bookshelf_power"));
                else if (this.thirtyTextureActive && this.menu.enchantSelected && !this.tenTextureActive && !this.twentyTextureActive) list.add(Component.translatable("enchanting_table.pinferno.enchant_for_levels", 30));

                graphics.setComponentTooltipForNextFrame(this.font, list, x, y);
            }
        }
    }

    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean isDoubleClick) {
        this.scrolling = false;
        if (this.itemInEnchantSlot) {
            int scrollLeft = this.leftPos + 156;
            int scrollTop = this.topPos + 9;
            int scrollRight = scrollLeft + 12;
            int scrollBottom = scrollTop + 54;

            // click in scroll bar
            if (event.x() >= scrollLeft && event.x() < scrollRight && event.y() >= scrollTop && event.y() < scrollBottom) {
                this.scrolling = true;
                return true;
            }
            // enchant slots
            int scrollOffset = this.scrollOffset + 16;
            Set<Integer> slots = itemCategorySlots.get(this.itemCategory);

            for (int i2 = this.scrollOffset; i2 < scrollOffset && i2 < ENCHANTMENT_ICON_TEXTURES.length; i2++) {
                int j2 = i2 - this.scrollOffset;
                int k2 = 97 + j2 % 4 * 14;
                int l2 = j2 / 4;
                int m2 = 11 + l2 * 14 + 2;
                // slot click
                if (this.isHovering(k2, m2, 14, 14, event.x(), event.y()) && slots != null && slots.contains(i2)) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(ModSounds.ENCHANT_CLICK, 1.0F));
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
                    this.menu.enchantSelected = true;
                    this.menu.selectedEnchantID = i2;

                    if (this.minecraft.gameMode != null) {
                        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, i2);
                        return true;
                    }
                }
            }
            // level click
            if (this.menu.getSlot(1).getItem().getItem() == Items.LAPIS_LAZULI) {
                // 10 level
                if (this.isHovering(71, 13, 16, 16, event.x(), event.y()) && this.tenTextureActive) {
                    if (this.minecraft.gameMode != null) {
                        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 100 + 1);
                        return true;
                    }
                }
                // 20 level
                else if (this.isHovering(71, 13 + 20, 16, 16, event.x(), event.y()) && this.twentyTextureActive) {
                    if (this.minecraft.gameMode != null) {
                        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 100 + 2);
                        return true;
                    }
                }
                // 30 level
                else if (this.isHovering(71, 13 + 40, 16, 16, event.x(), event.y()) && this.thirtyTextureActive) {
                    if (this.minecraft.gameMode != null) {
                        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 100 + 3);
                        return true;
                    }
                }
            }
            // scroll
            int i = this.leftPos + 156;
            int j = this.topPos + 9;
            if (event.x() >= (double)i && event.x() < (double)(i + 12) && event.y() >= (double)j && event.y() < (double)(j + 54)) {
                this.scrolling = true;
            }
        }
        return super.mouseClicked(event, isDoubleClick);
    }

    @Override
    public boolean mouseDragged(@NotNull MouseButtonEvent event, double mouseX, double mouseY) {
        if (this.scrolling && this.shouldScroll()) {
            int scrollTop = this.topPos + 9;
            int scrollBottom = scrollTop + 54;

            this.scrollAmount = Mth.clamp(
                    ((float) event.y() - (float) scrollTop - 7.5F) / ((float) (scrollBottom - scrollTop) - 15.0F),
                    0.0F,
                    1.0F
            );

            int maxScroll = this.getMaxScroll();
            this.scrollOffset = Mth.clamp((int) (this.scrollAmount * (float) maxScroll + 0.5F), 0, maxScroll) * 4;
            return true;
        }
        return super.mouseDragged(event, mouseX, mouseY);
    }

    @Override
    public boolean mouseReleased(@NotNull MouseButtonEvent event) {
        this.scrolling = false;
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (this.shouldScroll()) {
            int max = this.getMaxScroll();
            this.scrollAmount = Mth.clamp(this.scrollAmount - (float) (scrollY / (double) max), 0.0F, 1.0F);
            this.scrollOffset = (int) ((this.scrollAmount * (float) max) + 0.5F) * 4;
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private boolean hasEnchantment(ItemStack itemStack, ResourceKey enchantment) {
        return itemStack.getEnchantments().keySet().contains(this.world.registryAccess().lookupOrThrow(enchantment.registryKey()).getOrThrow(enchantment));
    }

    private void determineItemCategory(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            this.itemInEnchantSlot = false;
            this.itemCategory = "";
            return;
        }
        this.itemInEnchantSlot = true;
        this.itemCategory = null;

        if (itemStack.is(ItemTags.SWORDS)) {
            if (hasEnchantment(itemStack, Enchantments.BANE_OF_ARTHROPODS)) this.itemCategory = "swordBane";
            else if (hasEnchantment(itemStack, Enchantments.SHARPNESS)) this.itemCategory = "swordSharp";
            else if (hasEnchantment(itemStack, Enchantments.SMITE)) this.itemCategory = "swordSmite";
            else this.itemCategory = "sword";
        }
        else if (itemStack.is(ItemTags.SPEARS)) {
            if (hasEnchantment(itemStack, Enchantments.BANE_OF_ARTHROPODS)) this.itemCategory = "spearBane";
            else if (hasEnchantment(itemStack, Enchantments.SHARPNESS)) this.itemCategory = "spearSharp";
            else if (hasEnchantment(itemStack, Enchantments.SMITE)) this.itemCategory = "spearSmite";
            else this.itemCategory = "spear";
        }
        else if (itemStack.is(ItemTags.PICKAXES) && !itemStack.is(ModTags.MULTITOOLS)) {
            if (hasEnchantment(itemStack, Enchantments.FORTUNE)) {
                if (hasEnchantment(itemStack, Enchantments.BANE_OF_ARTHROPODS)) this.itemCategory = "pickaxeFortBane";
                else if (hasEnchantment(itemStack, Enchantments.SHARPNESS)) this.itemCategory = "pickaxeFortSharp";
                else if (hasEnchantment(itemStack, Enchantments.SMITE)) this.itemCategory = "pickaxeFortSmite";
                else this.itemCategory = "pickaxeFort";
            }
            else if (hasEnchantment(itemStack, Enchantments.SILK_TOUCH)) {
                if (hasEnchantment(itemStack, Enchantments.BANE_OF_ARTHROPODS)) this.itemCategory = "pickaxeSilkBane";
                else if (hasEnchantment(itemStack, Enchantments.SHARPNESS)) this.itemCategory = "pickaxeSilkSharp";
                else if (hasEnchantment(itemStack, Enchantments.SMITE)) this.itemCategory = "pickaxeSilkSmite";
                else this.itemCategory = "pickaxeSilk";
            }
            else {
                if (hasEnchantment(itemStack, Enchantments.BANE_OF_ARTHROPODS)) this.itemCategory = "pickaxeBane";
                else if (hasEnchantment(itemStack, Enchantments.SHARPNESS)) this.itemCategory = "pickaxeSharp";
                else if (hasEnchantment(itemStack, Enchantments.SMITE)) this.itemCategory = "pickaxeSmite";
                else this.itemCategory = "pickaxe";
            }
        }
        else if (itemStack.is(ItemTags.SHOVELS) && !itemStack.is(ModTags.MULTITOOLS)) {
            if (hasEnchantment(itemStack, Enchantments.FORTUNE)) {
                if (hasEnchantment(itemStack, Enchantments.BANE_OF_ARTHROPODS)) this.itemCategory = "shovelFortBane";
                else if (hasEnchantment(itemStack, Enchantments.SHARPNESS)) this.itemCategory = "shovelFortSharp";
                else if (hasEnchantment(itemStack, Enchantments.SMITE)) this.itemCategory = "shovelFortSmite";
                else this.itemCategory = "shovelFort";
            }
            else if (hasEnchantment(itemStack, Enchantments.SILK_TOUCH)) {
                if (hasEnchantment(itemStack, Enchantments.BANE_OF_ARTHROPODS)) this.itemCategory = "shovelSilkBane";
                else if (hasEnchantment(itemStack, Enchantments.SHARPNESS)) this.itemCategory = "shovelSilkSharp";
                else if (hasEnchantment(itemStack, Enchantments.SMITE)) this.itemCategory = "shovelSilkSmite";
                else this.itemCategory = "shovelSilk";
            }
            else {
                if (hasEnchantment(itemStack, Enchantments.BANE_OF_ARTHROPODS)) this.itemCategory = "shovelBane";
                else if (hasEnchantment(itemStack, Enchantments.SHARPNESS)) this.itemCategory = "shovelSharp";
                else if (hasEnchantment(itemStack, Enchantments.SMITE)) this.itemCategory = "shovelSmite";
                else this.itemCategory = "shovel";
            }
        }
        else if ((itemStack.is(ItemTags.AXES) || itemStack.is(ItemTags.HOES) || itemStack.is(ItemTags.SHOVELS)) && !itemStack.is(ModTags.MULTITOOLS)) {
            if (hasEnchantment(itemStack, Enchantments.FORTUNE)) {
                if (hasEnchantment(itemStack, Enchantments.BANE_OF_ARTHROPODS)) this.itemCategory = "toolFortWeaponBane";
                else if (hasEnchantment(itemStack, Enchantments.SHARPNESS)) this.itemCategory = "toolFortWeaponSharp";
                else if (hasEnchantment(itemStack, Enchantments.SMITE)) this.itemCategory = "toolFortWeaponSmite";
                else this.itemCategory = "toolFortWeapon";
            }
            else if (hasEnchantment(itemStack, Enchantments.SILK_TOUCH)) {
                if (hasEnchantment(itemStack, Enchantments.BANE_OF_ARTHROPODS)) this.itemCategory = "toolSilkWeaponBane";
                else if (hasEnchantment(itemStack, Enchantments.SHARPNESS)) this.itemCategory = "toolSilkWeaponSharp";
                else if (hasEnchantment(itemStack, Enchantments.SMITE)) this.itemCategory = "toolSilkWeaponSmite";
                else this.itemCategory = "toolSilkWeapon";
            }
            else {
                if (hasEnchantment(itemStack, Enchantments.BANE_OF_ARTHROPODS)) this.itemCategory = "toolWeaponBane";
                else if (hasEnchantment(itemStack, Enchantments.SHARPNESS)) this.itemCategory = "toolWeaponSharp";
                else if (hasEnchantment(itemStack, Enchantments.SMITE)) this.itemCategory = "toolWeaponSmite";
                else this.itemCategory = "toolWeapon";
            }
        }
        else if (itemStack.is(ModTags.MULTITOOLS)) {
            if (hasEnchantment(itemStack, Enchantments.FORTUNE)) this.itemCategory = "multitoolFort";
            else if (hasEnchantment(itemStack, Enchantments.SILK_TOUCH)) this.itemCategory = "multitoolSilk";
            else this.itemCategory = "multitool";
        }
        else if (itemStack.is(ItemTags.BOW_ENCHANTABLE)) {
            this.itemCategory = hasEnchantment(itemStack, Enchantments.MENDING) ? "bowMending" : "bow";
        }
        else if (itemStack.is(ItemTags.TRIDENT_ENCHANTABLE)) {
            if (hasEnchantment(itemStack, Enchantments.RIPTIDE)) this.itemCategory = "tridentRip";
            else if (hasEnchantment(itemStack, Enchantments.CHANNELING) || hasEnchantment(itemStack, Enchantments.LOYALTY)) this.itemCategory = "tridentWithoutRip";
            else this.itemCategory = "trident";
        }
        else if (itemStack.is(ItemTags.CROSSBOW_ENCHANTABLE)) {
            if (hasEnchantment(itemStack, Enchantments.PIERCING)) this.itemCategory = "crossbowPierce";
            else if (hasEnchantment(itemStack, Enchantments.MULTISHOT)) this.itemCategory = "crossbowMulti";
            else this.itemCategory = "crossbow";
        }
        else if (itemStack.is(ItemTags.MACE_ENCHANTABLE)) {
            if (hasEnchantment(itemStack, Enchantments.BREACH)) {
                if (hasEnchantment(itemStack, Enchantments.BANE_OF_ARTHROPODS)) this.itemCategory = "maceBreachBane";
                else if (hasEnchantment(itemStack, Enchantments.SMITE)) this.itemCategory = "maceBreachSmite";
                else this.itemCategory = "maceBreach";
            }
            else if (hasEnchantment(itemStack, Enchantments.DENSITY)) {
                if (hasEnchantment(itemStack, Enchantments.BANE_OF_ARTHROPODS)) this.itemCategory = "maceDensityBane";
                else if (hasEnchantment(itemStack, Enchantments.SMITE)) this.itemCategory = "maceDensitySmite";
                else this.itemCategory = "maceDensity";
            }
            else {
                if (hasEnchantment(itemStack, Enchantments.BANE_OF_ARTHROPODS)) this.itemCategory = "maceBane";
                else if (hasEnchantment(itemStack, Enchantments.SMITE)) this.itemCategory = "maceSmite";
                else this.itemCategory = "mace";
            }
        }
        else if (itemStack.is(ModTags.SHIELDS)) {
            if (hasEnchantment(itemStack, ModEnchantments.ENDURANCE)) this.itemCategory = "shieldEndurance";
            else if (hasEnchantment(itemStack, ModEnchantments.VIGOR)) this.itemCategory = "shieldVigor";
            else this.itemCategory = "shield";
        }
        else if (itemStack.is(ItemTags.HEAD_ARMOR_ENCHANTABLE)) {
            if (itemStack.getEnchantments().keySet().contains(this.world.registryAccess()
                    .lookupOrThrow(Enchantments.PROTECTION.registryKey())
                    .getOrThrow(Enchantments.PROTECTION))) this.itemCategory = "helmetProt";
            else if (itemStack.getEnchantments().keySet().contains(this.world.registryAccess()
                    .lookupOrThrow(Enchantments.PROJECTILE_PROTECTION.registryKey())
                    .getOrThrow(Enchantments.PROJECTILE_PROTECTION))) this.itemCategory = "helmetProj";
            else if (itemStack.getEnchantments().keySet().contains(this.world.registryAccess()
                    .lookupOrThrow(Enchantments.FIRE_PROTECTION.registryKey())
                    .getOrThrow(Enchantments.FIRE_PROTECTION))) this.itemCategory = "helmetFire";
            else if (itemStack.getEnchantments().keySet().contains(this.world.registryAccess()
                    .lookupOrThrow(Enchantments.BLAST_PROTECTION.registryKey())
                    .getOrThrow(Enchantments.BLAST_PROTECTION))) this.itemCategory = "helmetBlast";
            else this.itemCategory = "helmet";
        }
        else if (itemStack.is(ItemTags.CHEST_ARMOR_ENCHANTABLE)) {
            if (itemStack.getEnchantments().keySet().contains(this.world.registryAccess()
                    .lookupOrThrow(Enchantments.PROTECTION.registryKey())
                    .getOrThrow(Enchantments.PROTECTION))) this.itemCategory = "armourProt";
            else if (itemStack.getEnchantments().keySet().contains(this.world.registryAccess()
                    .lookupOrThrow(Enchantments.PROJECTILE_PROTECTION.registryKey())
                    .getOrThrow(Enchantments.PROJECTILE_PROTECTION))) this.itemCategory = "armourProj";
            else if (itemStack.getEnchantments().keySet().contains(this.world.registryAccess()
                    .lookupOrThrow(Enchantments.FIRE_PROTECTION.registryKey())
                    .getOrThrow(Enchantments.FIRE_PROTECTION))) this.itemCategory = "armourFire";
            else if (itemStack.getEnchantments().keySet().contains(this.world.registryAccess()
                    .lookupOrThrow(Enchantments.BLAST_PROTECTION.registryKey())
                    .getOrThrow(Enchantments.BLAST_PROTECTION))) this.itemCategory = "armourBlast";
            else this.itemCategory = "chestplate";
        }
        else if (itemStack.is(ItemTags.LEG_ARMOR_ENCHANTABLE)) {
            if (itemStack.getEnchantments().keySet().contains(this.world.registryAccess()
                    .lookupOrThrow(Enchantments.PROTECTION.registryKey())
                    .getOrThrow(Enchantments.PROTECTION))) this.itemCategory = "armourProt";
            else if (itemStack.getEnchantments().keySet().contains(this.world.registryAccess()
                    .lookupOrThrow(Enchantments.PROJECTILE_PROTECTION.registryKey())
                    .getOrThrow(Enchantments.PROJECTILE_PROTECTION))) this.itemCategory = "armourProj";
            else if (itemStack.getEnchantments().keySet().contains(this.world.registryAccess()
                    .lookupOrThrow(Enchantments.FIRE_PROTECTION.registryKey())
                    .getOrThrow(Enchantments.FIRE_PROTECTION))) this.itemCategory = "armourFire";
            else if (itemStack.getEnchantments().keySet().contains(this.world.registryAccess()
                    .lookupOrThrow(Enchantments.BLAST_PROTECTION.registryKey())
                    .getOrThrow(Enchantments.BLAST_PROTECTION))) this.itemCategory = "armourBlast";
            else this.itemCategory = "leggings";
        }
        else if (itemStack.is(ItemTags.FOOT_ARMOR_ENCHANTABLE)) {
            if (itemStack.getEnchantments().keySet().contains(this.world.registryAccess()
                    .lookupOrThrow(Enchantments.FROST_WALKER.registryKey())
                    .getOrThrow(Enchantments.FROST_WALKER))) {
                if (itemStack.getEnchantments().keySet().contains(this.world.registryAccess()
                        .lookupOrThrow(Enchantments.PROTECTION.registryKey())
                        .getOrThrow(Enchantments.PROTECTION))) this.itemCategory = "frostWalkProt";
                else if (itemStack.getEnchantments().keySet().contains(this.world.registryAccess()
                        .lookupOrThrow(Enchantments.PROJECTILE_PROTECTION.registryKey())
                        .getOrThrow(Enchantments.PROJECTILE_PROTECTION))) this.itemCategory = "frostWalkProj";
                else if (itemStack.getEnchantments().keySet().contains(this.world.registryAccess()
                        .lookupOrThrow(Enchantments.FIRE_PROTECTION.registryKey())
                        .getOrThrow(Enchantments.FIRE_PROTECTION))) this.itemCategory = "frostWalkFire";
                else if (itemStack.getEnchantments().keySet().contains(this.world.registryAccess()
                        .lookupOrThrow(Enchantments.BLAST_PROTECTION.registryKey())
                        .getOrThrow(Enchantments.BLAST_PROTECTION))) this.itemCategory = "frostWalkBlast";
                else this.itemCategory = "frostWalkNoProt";
            }
            else if (itemStack.getEnchantments().keySet().contains(this.world.registryAccess()
                    .lookupOrThrow(Enchantments.DEPTH_STRIDER.registryKey())
                    .getOrThrow(Enchantments.DEPTH_STRIDER))) {
                if (itemStack.getEnchantments().keySet().contains(this.world.registryAccess()
                        .lookupOrThrow(Enchantments.PROTECTION.registryKey())
                        .getOrThrow(Enchantments.PROTECTION))) this.itemCategory = "depthProt";
                else if (itemStack.getEnchantments().keySet().contains(this.world.registryAccess()
                        .lookupOrThrow(Enchantments.PROJECTILE_PROTECTION.registryKey())
                        .getOrThrow(Enchantments.PROJECTILE_PROTECTION))) this.itemCategory = "depthProj";
                else if (itemStack.getEnchantments().keySet().contains(this.world.registryAccess()
                        .lookupOrThrow(Enchantments.FIRE_PROTECTION.registryKey())
                        .getOrThrow(Enchantments.FIRE_PROTECTION))) this.itemCategory = "depthFire";
                else if (itemStack.getEnchantments().keySet().contains(this.world.registryAccess()
                        .lookupOrThrow(Enchantments.BLAST_PROTECTION.registryKey())
                        .getOrThrow(Enchantments.BLAST_PROTECTION))) this.itemCategory = "depthBlast";
                else this.itemCategory = "depthNoProt";
            }
            else this.itemCategory = "boots";
        }
        else if (itemStack.getItem() == Items.SHEARS) this.itemCategory = "shears";
        else if (itemStack.getItem() == Items.FLINT_AND_STEEL
                || itemStack.getItem() == Items.BRUSH
                || itemStack.getItem() == Items.CARROT_ON_A_STICK
                || itemStack.getItem() == Items.WARPED_FUNGUS_ON_A_STICK
                || itemStack.getItem() == Items.ELYTRA
                || itemStack.is(ModTags.ROD_UPGRADES)
        ) this.itemCategory = "misc";
        else if (itemStack.getItem() == Items.BOOK) this.itemCategory = "book";
    }

    public void tickBook() {
        this.enchPower = this.menu.enchantmentPower.get();
        ItemStack itemStack = this.menu.getSlot(0).getItem();

        if (!ItemStack.matches(itemStack, this.last)) {
            this.last = itemStack;

            do {
                this.flipT = this.flipT + (this.random.nextInt(4) - this.random.nextInt(4));
            } while (this.flip <= this.flipT + 1.0F && this.flip >= this.flipT - 1.0F);
        }

        this.oFlip = this.flip;
        this.oOpen = this.open;
        boolean shouldBeOpen = this.menu.enchantmentPower.get() != 0;

        if (shouldBeOpen) this.open += 0.2F;
        else this.open -= 0.2F;

        this.open = Mth.clamp(this.open, 0.0F, 1.0F);
        float diff = (this.flipT - this.flip) * 0.4F;
        diff = Mth.clamp(diff, -0.2F, 0.2F);
        this.flipA = this.flipA + (diff - this.flipA) * 0.9F;
        this.flip = this.flip + this.flipA;

        if (!this.menu.getSlot(0).getItem().isEmpty()) {
            this.itemInEnchantSlot = true;
            determineItemCategory(this.menu.getSlot(0).getItem());
        }
        else {
            this.itemInEnchantSlot = false;
            this.itemCategory = "";
        }
    }

    private boolean shouldScroll() {
        return this.itemInEnchantSlot;
    }

    private int getMaxScroll() {
        return (ENCHANTMENT_ICON_TEXTURES.length + 4 - 1) / 4 - 4;
    }

    private static int idx(String name) {
        for (int i = 0; i < PreservedEnchantmentMenu.ENCHANTMENT_DATA.size(); i++) {
            if (PreservedEnchantmentMenu.ENCHANTMENT_DATA.get(i).name().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }
}
