package sircow.placeholder.screen;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import sircow.placeholder.Placeholder;
import sircow.placeholder.block.custom.NewEnchantingTableBlock;
import sircow.placeholder.sound.ModSounds;

import java.util.*;

@Environment(EnvType.CLIENT)
public class NewEnchantingTableBlockScreen extends HandledScreen<NewEnchantingTableBlockScreenHandler> {
    private static final Identifier[] LEVEL_TEXTURES = new Identifier[]{
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/10_levels_enabled"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/20_levels_enabled"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/30_levels_enabled")
    };
    private static final Identifier[] LEVEL_DISABLED_TEXTURES = new Identifier[]{
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/10_levels_disabled"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/20_levels_disabled"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/30_levels_disabled")
    };
    private static final Identifier[] ENCHANTMENT_ICON_TEXTURES = new Identifier[]{
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/aqua_affinity"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/bane_of_arthropods"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/blast_protection"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/breach"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/channeling"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/density"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/depth_strider"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/efficiency"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/feather_falling"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/fire_aspect"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/fire_protection"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/flame"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/fortune"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/impaling"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/infinity"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/knockback"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/looting"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/loyalty"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/luck_of_the_sea"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/lure"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/multishot"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/piercing"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/power"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/projectile_protection"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/protection"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/punch"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/quick_charge"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/respiration"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/riptide"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/sharpness"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/silk_touch"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/smite"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/sweeping_edge"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/thorns"),
            Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchant_overlay/unbreaking"),
    };

    private static final Identifier ENCHANTMENT_SLOT_DISABLED_TEXTURE = Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchantment_slot_disabled");
    private static final Identifier ENCHANTMENT_SLOT_HIGHLIGHTED_TEXTURE = Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchantment_slot_highlighted");
    private static final Identifier ENCHANTMENT_SLOT_TEXTURE = Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/enchantment_slot");
    private static final Identifier TEXTURE = Identifier.of(Placeholder.MOD_ID,"textures/gui/container/new_enchanting_table_gui.png");
    private static final Identifier BOOK_TEXTURE = Identifier.ofVanilla("textures/entity/enchanting_table_book.png");
    private static final Identifier SCROLLER_TEXTURE = Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/scroller");
    private static final Identifier SCROLLER_DISABLED_TEXTURE = Identifier.of(Placeholder.MOD_ID,"container/enchanting_table/scroller_disabled");
    private final Random random = Random.create();
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
    public World world;
    public int enchPower;

    private static final Map<String, Set<Integer>> itemCategorySlots = new HashMap<>();
    static {
        itemCategorySlots.put("sword", Set.of(1, 9, 15, 16, 29, 31, 32, 34));
        itemCategorySlots.put("swordBane", Set.of(1, 9, 15, 16, 32, 34));
        itemCategorySlots.put("swordSharp", Set.of(9, 15, 16, 29, 32, 34));
        itemCategorySlots.put("swordSmite", Set.of(9, 15, 16, 31, 32, 34));
        itemCategorySlots.put("tool", Set.of(7, 12, 30, 34));
        itemCategorySlots.put("toolFort", Set.of(7, 12, 34));
        itemCategorySlots.put("toolSilk", Set.of(7, 30, 34));
        itemCategorySlots.put("bow", Set.of(11, 14, 22, 25, 34));
        itemCategorySlots.put("bowMending", Set.of(11, 22, 25, 34));
        itemCategorySlots.put("fishingRod", Set.of(18, 19, 34));
        itemCategorySlots.put("trident", Set.of(4, 13, 17, 28, 34));
        itemCategorySlots.put("tridentRip", Set.of(13, 28, 34));
        itemCategorySlots.put("tridentWithoutRip", Set.of(4, 13, 17, 34));
        itemCategorySlots.put("crossbow", Set.of(20, 21, 26, 34));
        itemCategorySlots.put("crossbowPierce", Set.of(21, 26, 34));
        itemCategorySlots.put("crossbowMulti", Set.of(20, 26, 34));
        itemCategorySlots.put("mace", Set.of(3, 5, 9));
        itemCategorySlots.put("maceBreach", Set.of(3, 9));
        itemCategorySlots.put("maceDensity", Set.of(5, 9));
        itemCategorySlots.put("helmet", Set.of(0, 2, 10, 23, 24, 27, 33, 34));
        itemCategorySlots.put("chestplate", Set.of(2, 10, 23, 24, 33, 34));
        itemCategorySlots.put("leggings", Set.of(2, 10, 23, 24, 33, 34));
        itemCategorySlots.put("boots", Set.of(2, 6, 8, 10, 23, 24, 33, 34));
        itemCategorySlots.put("helmetProt", Set.of(0, 24, 27, 33, 34));
        itemCategorySlots.put("helmetProj", Set.of(0, 23, 27, 33, 34));
        itemCategorySlots.put("helmetFire", Set.of(0, 10, 27, 33, 34));
        itemCategorySlots.put("helmetBlast", Set.of(0, 2, 27, 33, 34));
        itemCategorySlots.put("depthNoProt", Set.of(2, 6, 8, 10, 23, 24, 33, 34));
        itemCategorySlots.put("depthProt", Set.of(6, 8, 24, 33, 34));
        itemCategorySlots.put("depthProj", Set.of(6, 8, 23, 33, 34));
        itemCategorySlots.put("depthFire", Set.of(6, 8, 10, 33, 34));
        itemCategorySlots.put("depthBlast", Set.of(2, 6, 8, 33, 34));
        itemCategorySlots.put("frostWalkNoProt", Set.of(2, 8, 10, 23, 24, 33, 34));
        itemCategorySlots.put("frostWalkProt", Set.of(8, 24, 33, 34));
        itemCategorySlots.put("frostWalkProj", Set.of(8, 23, 33, 34));
        itemCategorySlots.put("frostWalkFire", Set.of(8, 10, 33, 34));
        itemCategorySlots.put("frostWalkBlast", Set.of(2, 8, 33, 34));
        itemCategorySlots.put("armourProt", Set.of(24, 33, 34));
        itemCategorySlots.put("armourProj", Set.of(23, 33, 34));
        itemCategorySlots.put("armourFire", Set.of(10, 33, 34));
        itemCategorySlots.put("armourBlast", Set.of(2, 33, 34));
        itemCategorySlots.put("book", Set.of(
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
                11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
                21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
                31, 32, 33, 34
        ));
    }

    public String[] enchantmentNames = {
            "Aqua Affinity", "Bane Of Arthropods", "Blast Protection", "Breach", "Channeling",
            "Density", "Depth Strider", "Efficiency", "Feather Falling", "Fire Aspect",
            "Fire Protection", "Flame", "Fortune", "Impaling", "Infinity",
            "Knockback", "Looting", "Loyalty", "Luck Of The Sea", "Lure",
            "Multishot", "Piercing", "Power", "Projectile Protection", "Protection",
            "Punch", "Quick Charge", "Respiration", "Riptide", "Sharpness",
            "Silk Touch", "Smite", "Sweeping Edge", "Thorns", "Unbreaking"
    };

    public String[] enchantmentLevelCosts = {
            "30", "10", "10", "10", "30", "10", "10", "10", "10", "10",
            "10", "20", "20", "10", "30", "20", "20", "10", "10", "10",
            "30", "10", "10", "10", "10", "20", "10", "10", "10", "10",
            "30", "10", "10", "10", "10",
    };

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

    public NewEnchantingTableBlockScreen(NewEnchantingTableBlockScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.stack = ItemStack.EMPTY;
        world = inventory.player.getWorld();
    }

    @Override
    protected void init() {
        super.init();
        if (this.client != null) {
            this.BOOK_MODEL = new BookModel(this.client.getEntityModelLoader().getModelPart(EntityModelLayers.BOOK));
        }
        this.tenTextureActive = false;
        this.twentyTextureActive = false;
        this.thirtyTextureActive = false;
        this.handler.enchantSelected = false;
    }

    public void handledScreenTick() {
        super.handledScreenTick();
        this.doTick();
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(RenderLayer::getGuiTextured, TEXTURE, i, j, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);
        this.drawBook(context, i, j, delta);

        int k = (int)(41.0F * this.scrollAmount);
        Identifier identifier = this.shouldScroll() ? SCROLLER_TEXTURE : SCROLLER_DISABLED_TEXTURE;
        context.drawGuiTexture(RenderLayer::getGuiTextured, identifier, i + 156, j + 13 + k, 12, 15);
        int l = this.x + 97;
        int m = this.y + 11;
        int n = this.scrollOffset + 16;
        this.renderIcons(context, l, m, n);
        this.renderEXPIcons(context, this.x + 71, this.y + 13);
    }

    private void drawBook(DrawContext context, int x, int y, float delta) {
        float f = MathHelper.lerp(delta, this.pageTurningSpeed, this.nextPageTurningSpeed);
        float g = MathHelper.lerp(delta, this.pageAngle, this.nextPageAngle);
        context.draw();
        DiffuseLighting.method_34742();
        context.getMatrices().push();
        context.getMatrices().translate((float)x + 43.0F, (float)y + 31.0F, 100.0F);
        float h = 40.0F;
        context.getMatrices().scale(-40.0F, 40.0F, 40.0F);
        context.getMatrices().multiply(RotationAxis.POSITIVE_X.rotationDegrees(25.0F));
        context.getMatrices().translate((1.0F - f) * 0.2F, (1.0F - f) * 0.1F, (1.0F - f) * 0.25F);
        float i = -(1.0F - f) * 90.0F - 90.0F;
        context.getMatrices().multiply(RotationAxis.POSITIVE_Y.rotationDegrees(i));
        context.getMatrices().multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));
        float j = MathHelper.clamp(MathHelper.fractionalPart(g + 0.25F) * 1.6F - 0.3F, 0.0F, 1.0F);
        float k = MathHelper.clamp(MathHelper.fractionalPart(g + 0.75F) * 1.6F - 0.3F, 0.0F, 1.0F);
        this.BOOK_MODEL.setPageAngles(0.0F, j, k, f);
        context.draw((vertexConsumers) -> {
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.BOOK_MODEL.getLayer(BOOK_TEXTURE));
            this.BOOK_MODEL.render(context.getMatrices(), vertexConsumer, 15728880, OverlayTexture.DEFAULT_UV);
        });
        context.draw();
        context.getMatrices().pop();
        DiffuseLighting.enableGuiDepthLighting();
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (this.client != null){
            float f = this.client.getRenderTickCounter().getTickDelta(false);
            super.render(context, mouseX, mouseY, f);
            this.drawMouseoverTooltip(context, mouseX, mouseY);
        }
    }

    private void renderIcons(DrawContext context, int x, int y, int scrollOffset) {
        for (int i = this.scrollOffset; i < scrollOffset && i < ENCHANTMENT_ICON_TEXTURES.length; i++) {
            int j = i - this.scrollOffset;
            int k = x + j % 4 * 14;
            int l = j / 4;
            int m = y + l * 14 + 2;

            if (!this.itemInEnchantSlot) {
                this.tenTextureActive = false;
                this.twentyTextureActive = false;
                this.thirtyTextureActive = false;
                this.handler.enchantSelected = false;
                context.drawGuiTexture(RenderLayer::getGuiTextured, ENCHANTMENT_SLOT_DISABLED_TEXTURE, k, m, 14, 14);
            }
            Set<Integer> slots = itemCategorySlots.get(this.itemCategory);
            if (!this.handler.enchantSelected) {
                context.drawGuiTexture(RenderLayer::getGuiTextured, slots != null && slots.contains(i) ? ENCHANTMENT_SLOT_TEXTURE : ENCHANTMENT_SLOT_DISABLED_TEXTURE, k, m, 14, 14);
            }
            else {
                if (i != this.handler.getSelectedEnchantID()) {
                    context.drawGuiTexture(RenderLayer::getGuiTextured, slots != null && slots.contains(i) ? ENCHANTMENT_SLOT_TEXTURE : ENCHANTMENT_SLOT_DISABLED_TEXTURE, k, m, 14, 14);
                }
                else {
                    context.drawGuiTexture(RenderLayer::getGuiTextured, ENCHANTMENT_SLOT_HIGHLIGHTED_TEXTURE, k, m, 14, 14);
                    if (Objects.equals(enchantmentLevelCosts[i], "10")) {
                        this.tenTextureActive = true;
                        this.twentyTextureActive = false;
                        this.thirtyTextureActive = false;
                    }
                    else if (Objects.equals(enchantmentLevelCosts[i], "20")) {
                        this.tenTextureActive = false;
                        this.twentyTextureActive = true;
                        this.thirtyTextureActive = false;
                    }
                    else if (Objects.equals(enchantmentLevelCosts[i], "30")) {
                        this.tenTextureActive = false;
                        this.twentyTextureActive = false;
                        this.thirtyTextureActive = true;
                    }
                }
            }
            context.drawGuiTexture(RenderLayer::getGuiTextured, ENCHANTMENT_ICON_TEXTURES[i], k, m, 14, 14);
        }
    }

    private void renderEXPIcons(DrawContext context, int x, int y) {
        context.drawGuiTexture(RenderLayer::getGuiTextured, !tenTextureActive ? LEVEL_DISABLED_TEXTURES[0] : LEVEL_TEXTURES[0], x, y, 16, 16);
        context.drawGuiTexture(RenderLayer::getGuiTextured, !twentyTextureActive ? LEVEL_DISABLED_TEXTURES[1] : LEVEL_TEXTURES[1], x, y + 20, 16, 16);
        context.drawGuiTexture(RenderLayer::getGuiTextured, !thirtyTextureActive ? LEVEL_DISABLED_TEXTURES[2] : LEVEL_TEXTURES[2], x, y + 40, 16, 16);
    }

    @Override
    protected void drawMouseoverTooltip(DrawContext context, int x, int y) {
        super.drawMouseoverTooltip(context, x, y);
        int scrollOffset = this.scrollOffset + 16;
        Set<Integer> slots = itemCategorySlots.get(this.itemCategory);

        if (!this.itemInEnchantSlot) {
            if (this.isPointWithinBounds(71, 13, 16, 16, x, y)) {
                context.drawTooltip(this.textRenderer, Text.literal("No item present") , x, y);
            }
        }

        if (this.itemInEnchantSlot) {
            for (int i = this.scrollOffset; i < scrollOffset && i < ENCHANTMENT_ICON_TEXTURES.length; i++) {
                int j = i - this.scrollOffset;
                int k = 97 + j % 4 * 14;
                int l = j / 4;
                int m = 11 + l * 14 + 2;
                if (this.isPointWithinBounds(k, m, 14, 14, x, y) && slots != null && slots.contains(i)) {
                    List<Text> list = Lists.<Text>newArrayList();
                    list.add(Text.literal(this.enchantmentNames[i] + " I"));
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
                    context.drawTooltip(this.textRenderer, list, x, y);
                    break;
                }
            }

            // hover over animated book
            if (this.isPointWithinBounds(24, 18, 40, 24, x, y)) {
                context.drawTooltip(this.textRenderer, Text.literal("Bookshelf Power: " + this.enchPower) , x, y);
            }
            // hover over 10 exp
            if (this.isPointWithinBounds(71, 13, 16, 16, x, y)) {
                List<Text> list = Lists.<Text>newArrayList();
                if (this.handler.getSlot(1).getStack().isEmpty()) {
                    list.add(Text.literal("Lapis Lazuli missing"));
                }
                if (!this.tenTextureActive && this.handler.enchantSelected) {
                    list.add(Text.literal("10 Levels needed"));
                }
                else if (this.itemInEnchantSlot && !this.handler.enchantSelected) {
                    list.add(Text.literal("Enchant not selected"));
                }

                if (this.enchPower < 1) {
                    list.add(Text.literal("Not enough bookshelf power"));
                }
                else if (this.tenTextureActive && this.handler.enchantSelected) {
                    list.add(Text.literal("Enchant for 10 Levels"));
                }

                context.drawTooltip(this.textRenderer, list, x, y);
            }

            // hover over 20 exp
            if (this.isPointWithinBounds(71, 33, 16, 16, x, y)) {
                List<Text> list = Lists.<Text>newArrayList();
                if (this.handler.getSlot(1).getStack().isEmpty()) {
                    list.add(Text.literal("Lapis Lazuli missing"));
                }
                if (!this.twentyTextureActive && this.handler.enchantSelected) {
                    list.add(Text.literal("20 Levels needed"));
                }
                else if (this.itemInEnchantSlot && !this.handler.enchantSelected) {
                    list.add(Text.literal("Enchant not selected"));
                }

                if (this.enchPower < 1) {
                    list.add(Text.literal("Not enough bookshelf power"));
                }
                else if (this.twentyTextureActive && this.handler.enchantSelected) {
                    list.add(Text.literal("Enchant for 20 Levels"));
                }

                context.drawTooltip(this.textRenderer, list, x, y);
            }

            // hover over 30 exp
            if (this.isPointWithinBounds(71, 53, 16, 16, x, y)) {
                List<Text> list = Lists.<Text>newArrayList();
                if (this.handler.getSlot(1).getStack().isEmpty()) {
                    list.add(Text.literal("Lapis Lazuli missing"));
                }
                if (!this.twentyTextureActive && this.handler.enchantSelected) {
                    list.add(Text.literal("30 Levels needed"));
                }
                else if (this.itemInEnchantSlot && !this.handler.enchantSelected) {
                    list.add(Text.literal("Enchant not selected"));
                }

                if (this.enchPower < 1) {
                    list.add(Text.literal("Not enough bookshelf power"));
                }
                else if (this.twentyTextureActive && this.handler.enchantSelected) {
                    list.add(Text.literal("Enchant for 30 Levels"));
                }

                context.drawTooltip(this.textRenderer, list, x, y);
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
                if (this.client != null) {
                    // slot click
                    if (this.isPointWithinBounds(k2, m2, 14, 14, mouseX, mouseY)
                            && slots != null && slots.contains(i2)) {
                        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(ModSounds.ENCHANT_CLICK, 1.0F));
                        int randomNum = (int)(Math.random() * 3);
                        switch (randomNum) {
                            case 0 ->
                                    MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(ModSounds.ENCHANT_OPEN_FLIP_ONE, 1.0F));
                            case 1 ->
                                    MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(ModSounds.ENCHANT_OPEN_FLIP_TWO, 1.0F));
                            case 2 ->
                                    MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(ModSounds.ENCHANT_OPEN_FLIP_THREE, 1.0F));
                        }
                        this.handler.enchantSelected = true;
                        this.handler.selectedEnchantID = i2;
                        if (this.client.interactionManager != null) {
                            this.client.interactionManager.clickButton(this.handler.syncId, i2);
                            return true;
                        }
                    }
                }
            }
            // level click
            if (this.client != null) {
                if (this.handler.getSlot(1).getStack().getItem() == Items.LAPIS_LAZULI) {
                    // 10 level
                    if (this.isPointWithinBounds(71, 13, 16, 16, mouseX, mouseY) && this.tenTextureActive) {
                        if (this.client.interactionManager != null) {
                            this.client.interactionManager.clickButton(this.handler.syncId, 100 + 1);
                            return true;
                        }
                    }
                    // 20 level
                    else if (this.isPointWithinBounds(71, 13 + 20, 16, 16, mouseX, mouseY) && this.twentyTextureActive) {
                        if (this.client.interactionManager != null) {
                            this.client.interactionManager.clickButton(this.handler.syncId, 100 + 2);
                            return true;
                        }
                    }
                    // 30 level
                    else if (this.isPointWithinBounds(71, 13 + 40, 16, 16, mouseX, mouseY) && this.thirtyTextureActive) {
                        if (this.client.interactionManager != null) {
                            this.client.interactionManager.clickButton(this.handler.syncId, 100 + 3);
                            return true;
                        }
                    }
                }
            }
            // scroll
            int i = this.x + 156;
            int j = this.y + 9;
            if (mouseX >= (double)i && mouseX < (double)(i + 12) && mouseY >= (double)j && mouseY < (double)(j + 54)) {
                this.mouseClicked = true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.mouseClicked && this.shouldScroll()) {
            int i = this.y + 14;
            int j = i + 54;
            this.scrollAmount = ((float)mouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
            this.scrollAmount = MathHelper.clamp(this.scrollAmount, 0.0F, 1.0F);
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
                this.scrollAmount = MathHelper.clamp(this.scrollAmount - f, 0.0F, 1.0F);
                this.scrollOffset = (int) ((double) (this.scrollAmount * (float) i) + 0.5) * 4;
            }
        }
        return true;
    }

    public void doTick() {
        this.enchPower = this.handler.enchantmentPower.get();

        ItemStack itemStack = this.handler.getSlot(0).getStack();

        if (!ItemStack.areEqual(itemStack, this.stack)) {
            this.stack = itemStack;
            do {
                this.approximatePageAngle = this.approximatePageAngle + (float)(this.random.nextInt(4) - this.random.nextInt(4));
            } while (this.nextPageAngle <= this.approximatePageAngle + 1.0F && this.nextPageAngle >= this.approximatePageAngle - 1.0F);
        }

        this.pageAngle = this.nextPageAngle;
        this.pageTurningSpeed = this.nextPageTurningSpeed;
        boolean bl = this.handler.enchantmentPower.get() != 0;

        if (bl) {
            this.nextPageTurningSpeed += 0.2F;
        } else {
            this.nextPageTurningSpeed -= 0.2F;
        }

        this.nextPageTurningSpeed = MathHelper.clamp(this.nextPageTurningSpeed, 0.0F, 1.0F);
        float f = (this.approximatePageAngle - this.nextPageAngle) * 0.4F;
        float g = 0.2F;
        f = MathHelper.clamp(f, -0.2F, 0.2F);
        this.pageRotationSpeed = this.pageRotationSpeed + (f - this.pageRotationSpeed) * 0.9F;
        this.nextPageAngle = this.nextPageAngle + this.pageRotationSpeed;

        if (!this.handler.getSlot(0).getStack().isEmpty()) {
            this.itemInEnchantSlot = true;

            RegistryEntry<Item> entry = Registries.ITEM.getEntry(itemStack.getItem());

            if (entry.isIn(ItemTags.SWORDS)) {
                if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                        .getOrThrow(Enchantments.BANE_OF_ARTHROPODS.getRegistryRef())
                        .getOrThrow(Enchantments.BANE_OF_ARTHROPODS))) {
                    this.itemCategory = "swordBane";
                }
                else if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                        .getOrThrow(Enchantments.SHARPNESS.getRegistryRef())
                        .getOrThrow(Enchantments.SHARPNESS))) {
                    this.itemCategory = "swordSharp";
                }
                else if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                        .getOrThrow(Enchantments.SMITE.getRegistryRef())
                        .getOrThrow(Enchantments.SMITE))) {
                    this.itemCategory = "swordSmite";
                }
                else {
                    this.itemCategory = "sword";
                }
            }
            else if (entry.isIn(ItemTags.AXES) || entry.isIn(ItemTags.PICKAXES) || entry.isIn(ItemTags.SHOVELS) || entry.isIn(ItemTags.HOES)) {
                if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                        .getOrThrow(Enchantments.FORTUNE.getRegistryRef())
                        .getOrThrow(Enchantments.FORTUNE))) {
                    this.itemCategory = "toolFort";
                }
                else if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                        .getOrThrow(Enchantments.SILK_TOUCH.getRegistryRef())
                        .getOrThrow(Enchantments.SILK_TOUCH))) {
                    this.itemCategory = "toolSilk";
                }
                else {
                    this.itemCategory = "tool";
                }
            }
            else if (entry.isIn(ItemTags.BOW_ENCHANTABLE)) {
                if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                        .getOrThrow(Enchantments.MENDING.getRegistryRef())
                        .getOrThrow(Enchantments.MENDING))) {
                    this.itemCategory = "bowMending";
                }
                else {
                    this.itemCategory = "bow";
                }
            }
            else if (entry.isIn(ItemTags.FISHING_ENCHANTABLE)) {
                this.itemCategory = "fishingRod";
            }
            else if (entry.isIn(ItemTags.TRIDENT_ENCHANTABLE)) {
                if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                        .getOrThrow(Enchantments.RIPTIDE.getRegistryRef())
                        .getOrThrow(Enchantments.RIPTIDE))) {
                    this.itemCategory = "tridentRip";
                }
                else if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                        .getOrThrow(Enchantments.CHANNELING.getRegistryRef())
                        .getOrThrow(Enchantments.CHANNELING)) ||
                        itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                                .getOrThrow(Enchantments.LOYALTY.getRegistryRef())
                                .getOrThrow(Enchantments.LOYALTY))) {
                    this.itemCategory = "tridentWithoutRip";
                }
                else {
                    this.itemCategory = "trident";
                }
            }
            else if (entry.isIn(ItemTags.CROSSBOW_ENCHANTABLE)) {
                if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                        .getOrThrow(Enchantments.PIERCING.getRegistryRef())
                        .getOrThrow(Enchantments.PIERCING))) {
                    this.itemCategory = "crossbowPierce";
                }
                else if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                        .getOrThrow(Enchantments.MULTISHOT.getRegistryRef())
                        .getOrThrow(Enchantments.MULTISHOT))) {
                    this.itemCategory = "crossbowMulti";
                }
                else {
                    this.itemCategory = "crossbow";
                }
            }
            else if (entry.isIn(ItemTags.MACE_ENCHANTABLE)) {
                if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                        .getOrThrow(Enchantments.BREACH.getRegistryRef())
                        .getOrThrow(Enchantments.BREACH))) {
                    this.itemCategory = "maceBreach";
                }
                else if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                        .getOrThrow(Enchantments.DENSITY.getRegistryRef())
                        .getOrThrow(Enchantments.DENSITY))) {
                    this.itemCategory = "maceDensity";
                }
                else {
                    this.itemCategory = "mace";
                }
            }
            else if (entry.isIn(ItemTags.HEAD_ARMOR_ENCHANTABLE)) {
                if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                        .getOrThrow(Enchantments.PROTECTION.getRegistryRef())
                        .getOrThrow(Enchantments.PROTECTION))) {
                    this.itemCategory = "helmetProt";
                }
                else if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                        .getOrThrow(Enchantments.PROJECTILE_PROTECTION.getRegistryRef())
                        .getOrThrow(Enchantments.PROJECTILE_PROTECTION))) {
                    this.itemCategory = "helmetProj";
                }
                else if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                        .getOrThrow(Enchantments.FIRE_PROTECTION.getRegistryRef())
                        .getOrThrow(Enchantments.FIRE_PROTECTION))) {
                    this.itemCategory = "helmetFire";
                }
                else if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                        .getOrThrow(Enchantments.BLAST_PROTECTION.getRegistryRef())
                        .getOrThrow(Enchantments.BLAST_PROTECTION))) {
                    this.itemCategory = "helmetBlast";
                }
                else {
                    this.itemCategory = "helmet";
                }
            }
            else if (entry.isIn(ItemTags.CHEST_ARMOR_ENCHANTABLE)) {
                if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                        .getOrThrow(Enchantments.PROTECTION.getRegistryRef())
                        .getOrThrow(Enchantments.PROTECTION))) {
                    this.itemCategory = "armourProt";
                }
                else if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                        .getOrThrow(Enchantments.PROJECTILE_PROTECTION.getRegistryRef())
                        .getOrThrow(Enchantments.PROJECTILE_PROTECTION))) {
                    this.itemCategory = "armourProj";
                }
                else if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                        .getOrThrow(Enchantments.FIRE_PROTECTION.getRegistryRef())
                        .getOrThrow(Enchantments.FIRE_PROTECTION))) {
                    this.itemCategory = "armourFire";
                }
                else if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                        .getOrThrow(Enchantments.BLAST_PROTECTION.getRegistryRef())
                        .getOrThrow(Enchantments.BLAST_PROTECTION))) {
                    this.itemCategory = "armourBlast";
                }
                else {
                    this.itemCategory = "chestplate";
                }
            }
            else if (entry.isIn(ItemTags.LEG_ARMOR_ENCHANTABLE)) {
                if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                        .getOrThrow(Enchantments.PROTECTION.getRegistryRef())
                        .getOrThrow(Enchantments.PROTECTION))) {
                    this.itemCategory = "armourProt";
                }
                else if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                        .getOrThrow(Enchantments.PROJECTILE_PROTECTION.getRegistryRef())
                        .getOrThrow(Enchantments.PROJECTILE_PROTECTION))) {
                    this.itemCategory = "armourProj";
                }
                else if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                        .getOrThrow(Enchantments.FIRE_PROTECTION.getRegistryRef())
                        .getOrThrow(Enchantments.FIRE_PROTECTION))) {
                    this.itemCategory = "armourFire";
                }
                else if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                        .getOrThrow(Enchantments.BLAST_PROTECTION.getRegistryRef())
                        .getOrThrow(Enchantments.BLAST_PROTECTION))) {
                    this.itemCategory = "armourBlast";
                }
                else {
                    this.itemCategory = "leggings";
                }
            }
            else if (entry.isIn(ItemTags.FOOT_ARMOR_ENCHANTABLE)) {
                if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                        .getOrThrow(Enchantments.FROST_WALKER.getRegistryRef())
                        .getOrThrow(Enchantments.FROST_WALKER))) {
                    if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                            .getOrThrow(Enchantments.PROTECTION.getRegistryRef())
                            .getOrThrow(Enchantments.PROTECTION))) {
                        this.itemCategory = "frostWalkProt";
                    }
                    else if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                            .getOrThrow(Enchantments.PROJECTILE_PROTECTION.getRegistryRef())
                            .getOrThrow(Enchantments.PROJECTILE_PROTECTION))) {
                        this.itemCategory = "frostWalkProj";
                    }
                    else if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                            .getOrThrow(Enchantments.FIRE_PROTECTION.getRegistryRef())
                            .getOrThrow(Enchantments.FIRE_PROTECTION))) {
                        this.itemCategory = "frostWalkFire";
                    }
                    else if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                            .getOrThrow(Enchantments.BLAST_PROTECTION.getRegistryRef())
                            .getOrThrow(Enchantments.BLAST_PROTECTION))) {
                        this.itemCategory = "frostWalkBlast";
                    }
                    else {
                        this.itemCategory = "frostWalkNoProt";
                    }
                }
                else if (!itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                        .getOrThrow(Enchantments.FROST_WALKER.getRegistryRef())
                        .getOrThrow(Enchantments.FROST_WALKER))) {
                    if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                            .getOrThrow(Enchantments.PROTECTION.getRegistryRef())
                            .getOrThrow(Enchantments.PROTECTION))) {
                        this.itemCategory = "depthProt";
                    }
                    else if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                            .getOrThrow(Enchantments.PROJECTILE_PROTECTION.getRegistryRef())
                            .getOrThrow(Enchantments.PROJECTILE_PROTECTION))) {
                        this.itemCategory = "depthProj";
                    }
                    else if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                            .getOrThrow(Enchantments.FIRE_PROTECTION.getRegistryRef())
                            .getOrThrow(Enchantments.FIRE_PROTECTION))) {
                        this.itemCategory = "depthFire";
                    }
                    else if (itemStack.getEnchantments().getEnchantments().contains(this.world.getRegistryManager()
                            .getOrThrow(Enchantments.BLAST_PROTECTION.getRegistryRef())
                            .getOrThrow(Enchantments.BLAST_PROTECTION))) {
                        this.itemCategory = "depthBlast";
                    }
                    else {
                        this.itemCategory = "depthNoProt";
                    }
                }
            }
            else if (this.handler.getSlot(0).getStack().getItem() == Items.BOOK) {
                this.itemCategory = "book";
            }
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
