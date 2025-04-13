package sircow.preservedinferno.client;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.screen.PreservedEnchantmentMenu;
import sircow.preservedinferno.sound.ModSounds;

import java.util.*;

public class PreservedEnchantingTableScreen extends AbstractContainerScreen<PreservedEnchantmentMenu> {
    private static final ResourceLocation[] LEVEL_TEXTURES = new ResourceLocation[]{
            Constants.id("container/enchanting_table/10_levels_enabled"),
            Constants.id("container/enchanting_table/20_levels_enabled"),
            Constants.id("container/enchanting_table/30_levels_enabled")
    };
    private static final ResourceLocation[] LEVEL_DISABLED_TEXTURES = new ResourceLocation[]{
            Constants.id("container/enchanting_table/10_levels_disabled"),
            Constants.id("container/enchanting_table/20_levels_disabled"),
            Constants.id("container/enchanting_table/30_levels_disabled")
    };
    private static final ResourceLocation[] ENCHANTMENT_ICON_TEXTURES = new ResourceLocation[]{
            Constants.id("container/enchanting_table/enchant_overlay/aqua_affinity"),
            Constants.id("container/enchanting_table/enchant_overlay/bane_of_arthropods"),
            Constants.id("container/enchanting_table/enchant_overlay/blast_protection"),
            Constants.id("container/enchanting_table/enchant_overlay/breach"),
            Constants.id("container/enchanting_table/enchant_overlay/channeling"),
            Constants.id("container/enchanting_table/enchant_overlay/density"),
            Constants.id("container/enchanting_table/enchant_overlay/depth_strider"),
            Constants.id("container/enchanting_table/enchant_overlay/efficiency"),
            Constants.id("container/enchanting_table/enchant_overlay/feather_falling"),
            Constants.id("container/enchanting_table/enchant_overlay/fire_aspect"),
            Constants.id("container/enchanting_table/enchant_overlay/fire_protection"),
            Constants.id("container/enchanting_table/enchant_overlay/flame"),
            Constants.id("container/enchanting_table/enchant_overlay/fortune"),
            Constants.id("container/enchanting_table/enchant_overlay/impaling"),
            Constants.id("container/enchanting_table/enchant_overlay/infinity"),
            Constants.id("container/enchanting_table/enchant_overlay/knockback"),
            Constants.id("container/enchanting_table/enchant_overlay/looting"),
            Constants.id("container/enchanting_table/enchant_overlay/loyalty"),
            Constants.id("container/enchanting_table/enchant_overlay/multishot"),
            Constants.id("container/enchanting_table/enchant_overlay/piercing"),
            Constants.id("container/enchanting_table/enchant_overlay/power"),
            Constants.id("container/enchanting_table/enchant_overlay/projectile_protection"),
            Constants.id("container/enchanting_table/enchant_overlay/protection"),
            Constants.id("container/enchanting_table/enchant_overlay/punch"),
            Constants.id("container/enchanting_table/enchant_overlay/quick_charge"),
            Constants.id("container/enchanting_table/enchant_overlay/respiration"),
            Constants.id("container/enchanting_table/enchant_overlay/riptide"),
            Constants.id("container/enchanting_table/enchant_overlay/sharpness"),
            Constants.id("container/enchanting_table/enchant_overlay/silk_touch"),
            Constants.id("container/enchanting_table/enchant_overlay/smite"),
            Constants.id("container/enchanting_table/enchant_overlay/sweeping_edge"),
            Constants.id("container/enchanting_table/enchant_overlay/thorns"),
            Constants.id("container/enchanting_table/enchant_overlay/unbreaking"),
    };

    private static final ResourceLocation ENCHANTMENT_SLOT_DISABLED_TEXTURE = Constants.id("container/enchanting_table/enchantment_slot_disabled");
    private static final ResourceLocation ENCHANTMENT_SLOT_HIGHLIGHTED_TEXTURE = Constants.id("container/enchanting_table/enchantment_slot_highlighted");
    private static final ResourceLocation ENCHANTMENT_SLOT_TEXTURE = Constants.id("container/enchanting_table/enchantment_slot");
    private static final ResourceLocation TEXTURE = Constants.id("textures/gui/container/preserved_enchanting_table_gui.png");
    private static final ResourceLocation BOOK_TEXTURE = ResourceLocation.withDefaultNamespace("textures/entity/enchanting_table_book.png");
    private static final ResourceLocation SCROLLER_TEXTURE = Constants.id("container/enchanting_table/scroller");
    private static final ResourceLocation SCROLLER_DISABLED_TEXTURE = Constants.id("container/enchanting_table/scroller_disabled");
    private final RandomSource random = RandomSource.create();
    private BookModel BOOK_MODEL;
    public float nextPageAngle;
    public float pageAngle;
    public float approximatePageAngle;
    public float pageRotationSpeed;
    public float nextPageTurningSpeed;
    public float pageTurningSpeed;
    private ItemStack stack = ItemStack.EMPTY;
    private boolean itemInEnchantSlot;
    private float scrollAmount;
    private boolean mouseClicked;
    private int scrollOffset;
    private String itemCategory;
    private boolean tenTextureActive;
    private boolean twentyTextureActive;
    private boolean thirtyTextureActive;
    public Level world;
    public int enchPower;

    private static final Map<String, Set<Integer>> itemCategorySlots = new HashMap<>();
    static {
        itemCategorySlots.put("sword", Set.of(1, 9, 15, 16, 27, 29, 30, 32));
        itemCategorySlots.put("swordBane", Set.of(1, 9, 15, 16, 30, 32));
        itemCategorySlots.put("swordSharp", Set.of(9, 15, 16, 27, 30, 32));
        itemCategorySlots.put("swordSmite", Set.of(9, 15, 16, 29, 30, 32));
        itemCategorySlots.put("tool", Set.of(7, 12, 28, 32));
        itemCategorySlots.put("toolFort", Set.of(7, 12, 32));
        itemCategorySlots.put("toolSilk", Set.of(7, 28, 32));
        itemCategorySlots.put("bow", Set.of(11, 14, 20, 23, 32));
        itemCategorySlots.put("bowMending", Set.of(11, 20, 23, 32));
        itemCategorySlots.put("fishingRod", Set.of(32));
        itemCategorySlots.put("trident", Set.of(4, 13, 17, 26, 32));
        itemCategorySlots.put("tridentRip", Set.of(13, 26, 32));
        itemCategorySlots.put("tridentWithoutRip", Set.of(4, 13, 17, 32));
        itemCategorySlots.put("crossbow", Set.of(18, 19, 24, 32));
        itemCategorySlots.put("crossbowPierce", Set.of(19, 24, 32));
        itemCategorySlots.put("crossbowMulti", Set.of(18, 24, 32));
        itemCategorySlots.put("mace", Set.of(3, 5, 9));
        itemCategorySlots.put("maceBreach", Set.of(3, 9));
        itemCategorySlots.put("maceDensity", Set.of(5, 9));
        itemCategorySlots.put("helmet", Set.of(0, 2, 10, 21, 22, 25, 31, 32));
        itemCategorySlots.put("chestplate", Set.of(2, 10, 21, 22, 31, 32));
        itemCategorySlots.put("leggings", Set.of(2, 10, 21, 22, 31, 32));
        itemCategorySlots.put("boots", Set.of(2, 6, 8, 10, 21, 22, 31, 32));
        itemCategorySlots.put("helmetProt", Set.of(0, 22, 25, 31, 32));
        itemCategorySlots.put("helmetProj", Set.of(0, 21, 25, 31, 32));
        itemCategorySlots.put("helmetFire", Set.of(0, 10, 25, 31, 32));
        itemCategorySlots.put("helmetBlast", Set.of(0, 2, 25, 31, 32));
        itemCategorySlots.put("depthNoProt", Set.of(2, 6, 8, 10, 21, 22, 31, 32));
        itemCategorySlots.put("depthProt", Set.of(6, 8, 22, 31, 32));
        itemCategorySlots.put("depthProj", Set.of(6, 8, 21, 31, 32));
        itemCategorySlots.put("depthFire", Set.of(6, 8, 10, 31, 32));
        itemCategorySlots.put("depthBlast", Set.of(2, 6, 8, 31, 32));
        itemCategorySlots.put("frostWalkNoProt", Set.of(2, 8, 10, 21, 22, 31, 32));
        itemCategorySlots.put("frostWalkProt", Set.of(8, 22, 31, 32));
        itemCategorySlots.put("frostWalkProj", Set.of(8, 21, 31, 32));
        itemCategorySlots.put("frostWalkFire", Set.of(8, 10, 31, 32));
        itemCategorySlots.put("frostWalkBlast", Set.of(2, 8, 31, 32));
        itemCategorySlots.put("armourProt", Set.of(22, 31, 32));
        itemCategorySlots.put("armourProj", Set.of(21, 31, 32));
        itemCategorySlots.put("armourFire", Set.of(10, 31, 32));
        itemCategorySlots.put("armourBlast", Set.of(2, 31, 32));
        itemCategorySlots.put("shears", Set.of(7, 32));
        itemCategorySlots.put("book", Set.of(
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
                11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
                21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
                31, 32
        ));
    }

    /*
    public String[] enchantmentDescriptions = {
            "Mining speed is no longer affected while submerged.",
            "Deal 3.5 more damage to arthropods.",
            "Decrease damage taken from explosions by 4%.",
            "Reduces the effectiveness of armor by 20%.",
            "Summon a lightning bolt upon hitting a target.",
            "Increase the damage dealt per block fallen by 0.75.",
            "Decrease the slowing effect of water by 33%.",
            "Gain +4 Mining Speed.",
            "Decrease damage taken from falling by 10%.",
            "Ignite targets for 3 seconds.",
            "Decrease damage taken from fire by 4%.",
            "Ignite targets for 3 seconds.",
            "Increase the chance for double drops by +50%.",
            "Deal 3.5 more damage to soaked enemies.",
            "The bow no longer consumes arrows.",
            "Increase knockback dealt by 2.5 blocks.",
            "Increase the chance for a common drop by 1.",
            "Returns the trident to the user",
            "Increases the weight of obtaining treasure by 1.",
            "Decreases the wait time of a catch by 5 seconds.",
            "Shoot 2 extra arrows but with 75%",
            "Arrows can pierce 2 enemies and reduces",
            "Deal 0.5 more damage with arrows.",
            "Decrease damage taken from projectiles by 4%.",
            "Decrease damage taken by 1%.",
            "Increase the knockback dealt with arrows.",
            "Decrease the loading time by 0.25 seconds.",
            "Increases your underwater breathing",
            "Propels you 6 blocks ahead while soaked.",
            "Deal 1 more damage.",
            "Mined blocks drop themselves.",
            "Deal 3.5 more damage to undead mobs.",
            "Increases the damage of your sweeping attack",
            "Gain a 15% chance to deal damage",
            "Decrease the chance for your tool"
    };
    */

    public PreservedEnchantingTableScreen(PreservedEnchantmentMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.stack = ItemStack.EMPTY;
        world = inventory.player.level();
    }

    @Override
    protected void init() {
        super.init();
        if (this.minecraft != null) {
            this.BOOK_MODEL = new BookModel(this.minecraft.getEntityModels().bakeLayer(ModelLayers.BOOK));
        }
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
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        context.blit(RenderType::guiTextured, TEXTURE, i, j, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
        this.drawBook(context, i, j, delta);

        int k = (int)(41.0F * this.scrollAmount);
        ResourceLocation identifier = this.shouldScroll() ? SCROLLER_TEXTURE : SCROLLER_DISABLED_TEXTURE;
        context.blitSprite(RenderType::guiTextured, identifier, i + 156, j + 13 + k, 12, 15);
        int l = this.leftPos + 97;
        int m = this.topPos + 11;
        int n = this.scrollOffset + 16;
        this.renderIcons(context, l, m, n);
        this.renderEXPIcons(context, this.leftPos + 71, this.topPos + 13);
    }

    private void drawBook(GuiGraphics context, int x, int y, float delta) {
        float f = Mth.lerp(delta, this.pageTurningSpeed, this.nextPageTurningSpeed);
        float g = Mth.lerp(delta, this.pageAngle, this.nextPageAngle);
        context.flush();
        Lighting.setupForEntityInInventory();
        context.pose().pushPose();
        context.pose().translate((float)x + 43.0F, (float)y + 31.0F, 100.0F);
        float h = 40.0F;
        context.pose().scale(-40.0F, 40.0F, 40.0F);
        context.pose().mulPose(Axis.XP.rotationDegrees(25.0F));
        context.pose().translate((1.0F - f) * 0.2F, (1.0F - f) * 0.1F, (1.0F - f) * 0.25F);
        float i = -(1.0F - f) * 90.0F - 90.0F;
        context.pose().mulPose(Axis.YP.rotationDegrees(i));
        context.pose().mulPose(Axis.XP.rotationDegrees(180.0F));
        float j = Mth.clamp(Mth.frac(g + 0.25F) * 1.6F - 0.3F, 0.0F, 1.0F);
        float k = Mth.clamp(Mth.frac(g + 0.75F) * 1.6F - 0.3F, 0.0F, 1.0F);
        this.BOOK_MODEL.setupAnim(0.0F, j, k, f);
        context.drawSpecial((vertexConsumers) -> {
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.BOOK_MODEL.renderType(BOOK_TEXTURE));
            this.BOOK_MODEL.renderToBuffer(context.pose(), vertexConsumer, 15728880, OverlayTexture.NO_OVERLAY);
        });
        context.flush();
        context.pose().popPose();
        Lighting.setupFor3DItems();
    }

    public void render(@NotNull GuiGraphics context, int mouseX, int mouseY, float delta) {
        if (this.minecraft != null){
            float f = this.minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false);
            super.render(context, mouseX, mouseY, f);
            this.renderTooltip(context, mouseX, mouseY);
        }
    }

    private void renderIcons(GuiGraphics context, int x, int y, int scrollOffset) {
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
                context.blitSprite(RenderType::guiTextured, ENCHANTMENT_SLOT_DISABLED_TEXTURE, k, m, 14, 14);
            }
            Set<Integer> slots = itemCategorySlots.get(this.itemCategory);
            if (!this.menu.enchantSelected) {
                context.blitSprite(RenderType::guiTextured, slots != null && slots.contains(i) ? ENCHANTMENT_SLOT_TEXTURE : ENCHANTMENT_SLOT_DISABLED_TEXTURE, k, m, 14, 14);
            }
            else {
                if (i != this.menu.getSelectedEnchantID()) {
                    context.blitSprite(RenderType::guiTextured, slots != null && slots.contains(i) ? ENCHANTMENT_SLOT_TEXTURE : ENCHANTMENT_SLOT_DISABLED_TEXTURE, k, m, 14, 14);
                }
                else {
                    context.blitSprite(RenderType::guiTextured, ENCHANTMENT_SLOT_HIGHLIGHTED_TEXTURE, k, m, 14, 14);
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
            context.blitSprite(RenderType::guiTextured, ENCHANTMENT_ICON_TEXTURES[i], k, m, 14, 14);
        }
    }

    private void renderEXPIcons(GuiGraphics context, int x, int y) {
        context.blitSprite(RenderType::guiTextured, !tenTextureActive ? LEVEL_DISABLED_TEXTURES[0] : LEVEL_TEXTURES[0], x, y, 16, 16);
        context.blitSprite(RenderType::guiTextured, !twentyTextureActive ? LEVEL_DISABLED_TEXTURES[1] : LEVEL_TEXTURES[1], x, y + 20, 16, 16);
        context.blitSprite(RenderType::guiTextured, !thirtyTextureActive ? LEVEL_DISABLED_TEXTURES[2] : LEVEL_TEXTURES[2], x, y + 40, 16, 16);
    }

    @Override
    protected void renderTooltip(@NotNull GuiGraphics context, int x, int y) {
        super.renderTooltip(context, x, y);
        int scrollOffset = this.scrollOffset + 16;
        Set<Integer> slots = itemCategorySlots.get(this.itemCategory);

        if (!this.itemInEnchantSlot) {
            if (this.isHovering(71, 13, 16, 16, x, y)) {
                context.renderTooltip(this.font, Component.literal("No item present") , x, y);
            }
        }

        if (this.itemInEnchantSlot) {
            for (int i = this.scrollOffset; i < scrollOffset && i < ENCHANTMENT_ICON_TEXTURES.length; i++) {
                int j = i - this.scrollOffset;
                int k = 97 + j % 4 * 14;
                int l = j / 4;
                int m = 11 + l * 14 + 2;
                if (this.isHovering(k, m, 14, 14, x, y) && slots != null && slots.contains(i)) {
                    List<Component> list = Lists.<Component>newArrayList();
                    list.add(Component.literal(PreservedEnchantmentMenu.ENCHANTMENT_DATA.get(i).name() + " I"));
                    /*
                    list.add(Text.literal(this.enchantmentDescriptions[i]).formatted(Formatting.WHITE));
                    // these are line breaks
                    switch (i) {
                        case 10 -> list.add(Text.literal("Also reduces burning time by 10%."));
                        case 16 -> list.add(Text.literal("Also increases the chance of rare drops by 0.5%."));
                        case 17 -> list.add(Text.literal("at 20 blocks per second."));
                        case 20 -> list.add(Text.literal("of the original damage."));
                        case 21 -> list.add(Text.literal("the effectiveness of armor by 20%."));
                        case 27 -> list.add(Text.literal("time by 10 seconds."));
                        case 32 -> list.add(Text.literal("by 50% of your weapon's attack damage."));
                        case 33 -> list.add(Text.literal("when attacked by an enemy."));
                        case 34 -> list.add(Text.literal("to consume durability by 20%."));
                    }
                    */
                    context.renderTooltip(this.font, list, Optional.empty(), x, y);
                    break;
                }
            }

            // hover over animated book
            if (this.isHovering(24, 18, 40, 24, x, y)) {
                context.renderTooltip(this.font, Component.literal("Bookshelf Power: " + this.enchPower) , x, y);
            }
            // hover over 10 exp
            if (this.isHovering(71, 13, 16, 16, x, y)) {
                List<Component> list = Lists.newArrayList();
                if (this.menu.getSlot(1).getItem().isEmpty() && !this.twentyTextureActive && !this.thirtyTextureActive) {
                    list.add(Component.literal("Lapis Lazuli missing"));
                }
                if (!this.tenTextureActive && !this.twentyTextureActive && !this.thirtyTextureActive && this.menu.enchantSelected) {
                    list.add(Component.literal("10 Levels needed"));
                }
                else if (this.itemInEnchantSlot && !this.menu.enchantSelected) {
                    list.add(Component.literal("Enchant not selected"));
                }

                if (this.enchPower < 1 && !this.twentyTextureActive && !this.thirtyTextureActive) {
                    list.add(Component.literal("Not enough bookshelf power"));
                }
                else if (this.tenTextureActive && this.menu.enchantSelected && !this.twentyTextureActive && !this.thirtyTextureActive) {
                    list.add(Component.literal("Enchant for 10 Levels"));
                }

                context.renderTooltip(this.font, list, Optional.empty(), x, y);
            }

            // hover over 20 exp
            if (this.isHovering(71, 33, 16, 16, x, y)) {
                List<Component> list = Lists.newArrayList();
                if (this.menu.getSlot(1).getItem().isEmpty() && !this.tenTextureActive && !this.thirtyTextureActive) {
                    list.add(Component.literal("Lapis Lazuli missing"));
                }
                if (!this.tenTextureActive && !this.twentyTextureActive && !this.thirtyTextureActive && this.menu.enchantSelected) {
                    list.add(Component.literal("20 Levels needed"));
                }
                else if (this.itemInEnchantSlot && !this.menu.enchantSelected) {
                    list.add(Component.literal("Enchant not selected"));
                }

                if (this.enchPower < 2 && !this.tenTextureActive && !this.thirtyTextureActive) {
                    list.add(Component.literal("Not enough bookshelf power"));
                }
                else if (this.twentyTextureActive && this.menu.enchantSelected && !this.tenTextureActive && !this.thirtyTextureActive) {
                    list.add(Component.literal("Enchant for 20 Levels"));
                }

                context.renderTooltip(this.font, list, Optional.empty(), x, y);
            }

            // hover over 30 exp
            if (this.isHovering(71, 53, 16, 16, x, y)) {
                List<Component> list = Lists.newArrayList();
                if (this.menu.getSlot(1).getItem().isEmpty() && !this.tenTextureActive && !this.twentyTextureActive) {
                    list.add(Component.literal("Lapis Lazuli missing"));
                }
                if (!this.tenTextureActive && !this.twentyTextureActive && !this.thirtyTextureActive && this.menu.enchantSelected) {
                    list.add(Component.literal("30 Levels needed"));
                }
                else if (this.itemInEnchantSlot && !this.menu.enchantSelected) {
                    list.add(Component.literal("Enchant not selected"));
                }

                if (this.enchPower < 3 && !this.tenTextureActive && !this.twentyTextureActive) {
                    list.add(Component.literal("Not enough bookshelf power"));
                }
                else if (this.thirtyTextureActive && this.menu.enchantSelected && !this.tenTextureActive && !this.twentyTextureActive) {
                    list.add(Component.literal("Enchant for 30 Levels"));
                }

                context.renderTooltip(this.font, list, Optional.empty(), x, y);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.mouseClicked = false;
        if (this.itemInEnchantSlot) {
            // enchant slots
            int scrollOffset = this.scrollOffset + 16;
            Set<Integer> slots = itemCategorySlots.get(this.itemCategory);

            for (int i2 = this.scrollOffset; i2 < scrollOffset && i2 < ENCHANTMENT_ICON_TEXTURES.length; i2++) {
                int j2 = i2 - this.scrollOffset;
                int k2 = 97 + j2 % 4 * 14;
                int l2 = j2 / 4;
                int m2 = 11 + l2 * 14 + 2;
                if (this.minecraft != null) {
                    // slot click
                    if (this.isHovering(k2, m2, 14, 14, mouseX, mouseY)
                            && slots != null && slots.contains(i2)) {
                        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(ModSounds.ENCHANT_CLICK, 1.0F));
                        int randomNum = (int)(Math.random() * 3);
                        switch (randomNum) {
                            case 0 ->
                                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(ModSounds.ENCHANT_OPEN_FLIP_ONE, 1.0F));
                            case 1 ->
                                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(ModSounds.ENCHANT_OPEN_FLIP_TWO, 1.0F));
                            case 2 ->
                                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(ModSounds.ENCHANT_OPEN_FLIP_THREE, 1.0F));
                        }
                        this.menu.enchantSelected = true;
                        this.menu.selectedEnchantID = i2;

                        if (this.minecraft.gameMode != null) {
                            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, i2);
                            return true;
                        }
                    }
                }
            }
            // level click
            if (this.minecraft != null) {
                if (this.menu.getSlot(1).getItem().getItem() == Items.LAPIS_LAZULI) {
                    // 10 level
                    if (this.isHovering(71, 13, 16, 16, mouseX, mouseY) && this.tenTextureActive) {
                        if (this.minecraft.gameMode != null) {
                            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 100 + 1);
                            return true;
                        }
                    }
                    // 20 level
                    else if (this.isHovering(71, 13 + 20, 16, 16, mouseX, mouseY) && this.twentyTextureActive) {
                        if (this.minecraft.gameMode != null) {
                            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 100 + 2);
                            return true;
                        }
                    }
                    // 30 level
                    else if (this.isHovering(71, 13 + 40, 16, 16, mouseX, mouseY) && this.thirtyTextureActive) {
                        if (this.minecraft.gameMode != null) {
                            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 100 + 3);
                            return true;
                        }
                    }
                }
            }
            // scroll
            int i = this.leftPos + 156;
            int j = this.topPos + 9;
            if (mouseX >= (double)i && mouseX < (double)(i + 12) && mouseY >= (double)j && mouseY < (double)(j + 54)) {
                this.mouseClicked = true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.mouseClicked && this.shouldScroll()) {
            int i = this.topPos + 14;
            int j = i + 54;
            this.scrollAmount = ((float)mouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
            this.scrollAmount = Mth.clamp(this.scrollAmount, 0.0F, 1.0F);
            this.scrollOffset = (int)((double)(this.scrollAmount * (float)this.getMaxScroll()) + 0.5) * 4;
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (!super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)) {
            if (this.shouldScroll()) {
                int i = this.getMaxScroll();
                float f = (float) verticalAmount / (float) i;
                this.scrollAmount = Mth.clamp(this.scrollAmount - f, 0.0F, 1.0F);
                this.scrollOffset = (int) ((double) (this.scrollAmount * (float) i) + 0.5) * 4;
            }
        }
        return true;
    }

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
        else if (itemStack.is(ItemTags.AXES) || itemStack.is(ItemTags.PICKAXES) || itemStack.is(ItemTags.SHOVELS) || itemStack.is(ItemTags.HOES)) {
            if (hasEnchantment(itemStack, Enchantments.FORTUNE)) this.itemCategory = "toolFort";
            else if (hasEnchantment(itemStack, Enchantments.SILK_TOUCH)) this.itemCategory = "toolSilk";
            else this.itemCategory = "tool";
        }
        else if (itemStack.is(ItemTags.BOW_ENCHANTABLE)) {
            this.itemCategory = hasEnchantment(itemStack, Enchantments.MENDING) ? "bowMending" : "bow";
        }
        else if (itemStack.is(ItemTags.FISHING_ENCHANTABLE)) {
            this.itemCategory = "fishingRod";
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
            if (hasEnchantment(itemStack, Enchantments.BREACH)) this.itemCategory = "maceBreach";
            else if (hasEnchantment(itemStack, Enchantments.DENSITY)) this.itemCategory = "maceDensity";
            else this.itemCategory = "mace";
        }
        else if (itemStack.is(ItemTags.HEAD_ARMOR_ENCHANTABLE)) {
            this.itemCategory = getArmorCategory(itemStack, "helmet");
        }
        else if (itemStack.is(ItemTags.CHEST_ARMOR_ENCHANTABLE)) {
            this.itemCategory = getArmorCategory(itemStack, "chestplate", "armour");
        }
        else if (itemStack.is(ItemTags.LEG_ARMOR_ENCHANTABLE)) {
            this.itemCategory = getArmorCategory(itemStack, "leggings", "armour");
        }
        else if (itemStack.is(ItemTags.FOOT_ARMOR_ENCHANTABLE)) {
            if (hasEnchantment(itemStack, Enchantments.FROST_WALKER)) {
                this.itemCategory = getBootCategory(itemStack, "frostWalkNoProt", "frostWalk");
            }
            else {
                this.itemCategory = getBootCategory(itemStack, "depthNoProt", "depth");
            }
        }
        else if (itemStack.getItem() == Items.SHEARS) {
            this.itemCategory = "shears";
        }
        else if (itemStack.getItem() == Items.BOOK) {
            this.itemCategory = "book";
        }
    }

    private String getArmorCategory(ItemStack itemStack, String baseCategory, String protectionPrefix) {
        if (hasEnchantment(itemStack, Enchantments.PROTECTION)) return protectionPrefix + "Prot";
        if (hasEnchantment(itemStack, Enchantments.PROJECTILE_PROTECTION)) return protectionPrefix + "Proj";
        if (hasEnchantment(itemStack, Enchantments.FIRE_PROTECTION)) return protectionPrefix + "Fire";
        if (hasEnchantment(itemStack, Enchantments.BLAST_PROTECTION)) return protectionPrefix + "Blast";
        return baseCategory;
    }

    private String getArmorCategory(ItemStack itemStack, String baseCategory) {
        return getArmorCategory(itemStack, baseCategory, baseCategory.substring(0, baseCategory.length() - 4)); // Remove "Prot" etc.
    }

    private String getBootCategory(ItemStack itemStack, String noProtectionCategory, String protectionPrefix) {
        if (hasEnchantment(itemStack, Enchantments.PROTECTION)) return protectionPrefix + "Prot";
        if (hasEnchantment(itemStack, Enchantments.PROJECTILE_PROTECTION)) return protectionPrefix + "Proj";
        if (hasEnchantment(itemStack, Enchantments.FIRE_PROTECTION)) return protectionPrefix + "Fire";
        if (hasEnchantment(itemStack, Enchantments.BLAST_PROTECTION)) return protectionPrefix + "Blast";
        return noProtectionCategory;
    }

    public void tickBook() {
        this.enchPower = this.menu.enchantmentPower.get();

        ItemStack itemStack = this.menu.getSlot(0).getItem();

        if (!ItemStack.matches(itemStack, this.stack)) {
            this.stack = itemStack;
            do {
                this.approximatePageAngle = this.approximatePageAngle + (float)(this.random.nextInt(4) - this.random.nextInt(4));
            } while (this.nextPageAngle <= this.approximatePageAngle + 1.0F && this.nextPageAngle >= this.approximatePageAngle - 1.0F);
        }

        this.pageAngle = this.nextPageAngle;
        this.pageTurningSpeed = this.nextPageTurningSpeed;
        boolean bl = this.menu.enchantmentPower.get() != 0;

        if (bl) {
            this.nextPageTurningSpeed += 0.2F;
        } else {
            this.nextPageTurningSpeed -= 0.2F;
        }

        this.nextPageTurningSpeed = Mth.clamp(this.nextPageTurningSpeed, 0.0F, 1.0F);
        float f = (this.approximatePageAngle - this.nextPageAngle) * 0.4F;
        f = Mth.clamp(f, -0.2F, 0.2F);
        this.pageRotationSpeed = this.pageRotationSpeed + (f - this.pageRotationSpeed) * 0.9F;
        this.nextPageAngle = this.nextPageAngle + this.pageRotationSpeed;

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
        return this.itemInEnchantSlot && ENCHANTMENT_ICON_TEXTURES.length > 16;
    }

    protected int getMaxScroll() {
        return (ENCHANTMENT_ICON_TEXTURES.length) / 4 - 3;
    }
}
