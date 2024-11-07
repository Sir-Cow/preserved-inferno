package sircow.placeholder.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import sircow.placeholder.Placeholder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    private ItemStack stack;
    private boolean itemInEnchantSlot;
    private float scrollAmount;
    private boolean mouseClicked;
    private int scrollOffset;
    private String itemCategory;
    private boolean tenTextureActive;
    private boolean twentyTextureActive;
    private boolean thirtyTextureActive;

    private static final Map<String, Set<Integer>> itemCategorySlots = new HashMap<>();
    static {
        itemCategorySlots.put("sword", Set.of(1, 9, 15, 16, 29, 31, 32, 34));
        itemCategorySlots.put("axe", Set.of(7, 12, 30, 34));
        itemCategorySlots.put("tool", Set.of(7, 12, 30, 34));
        itemCategorySlots.put("bow", Set.of(11, 14, 22, 25, 34));
        itemCategorySlots.put("fishing_rod", Set.of(18, 19, 34));
        itemCategorySlots.put("trident", Set.of(4, 13, 17, 28, 34));
        itemCategorySlots.put("mace", Set.of(3, 5, 9));
        itemCategorySlots.put("crossbow", Set.of(20, 21, 26, 34));
    }

    public String[] enchantmentNames = {
            "Aqua Affinity",
            "Bane Of Arthropods",
            "Blast Protection",
            "Breach",
            "Channeling",
            "Density",
            "Depth Strider",
            "Efficiency",
            "Feather Falling",
            "Fire Aspect",
            "Fire Protection",
            "Flame",
            "Fortune",
            "Impaling",
            "Infinity",
            "Knockback",
            "Looting",
            "Loyalty",
            "Luck Of The Sea",
            "Lure",
            "Multishot",
            "Piercing",
            "Power",
            "Projectile Protection",
            "Protection",
            "Punch",
            "Quick Charge",
            "Respiration",
            "Riptide",
            "Sharpness",
            "Silk Touch",
            "Smite",
            "Sweeping Edge",
            "Thorns",
            "Unbreaking"
    };

    private final Slot itemSlot = this.handler.getInputSlot();

    public NewEnchantingTableBlockScreen(NewEnchantingTableBlockScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.stack = ItemStack.EMPTY;
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
        int m = this.y + 12;
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
                context.drawGuiTexture(RenderLayer::getGuiTextured, ENCHANTMENT_SLOT_DISABLED_TEXTURE, k, m, 14, 14);
            }
            Set<Integer> slots = itemCategorySlots.get(this.itemCategory);
            context.drawGuiTexture(RenderLayer::getGuiTextured, slots != null && slots.contains(i) ? ENCHANTMENT_SLOT_TEXTURE : ENCHANTMENT_SLOT_DISABLED_TEXTURE, k, m, 14, 14);
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
        if (this.itemInEnchantSlot) {
            for (int i = this.scrollOffset; i < scrollOffset && i < ENCHANTMENT_ICON_TEXTURES.length; i++) {
                int j = i - this.scrollOffset;
                int k = 97 + j % 4 * 14;
                int l = j / 4;
                int m = 12 + l * 14 + 2;
                if (this.isPointWithinBounds(k, m, 14, 14, x, y) && slots != null && slots.contains(i)) {
                    context.drawTooltip(this.textRenderer, Text.literal(this.enchantmentNames[i]), x, y);
                    break;
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.mouseClicked = false;
        if (this.itemInEnchantSlot) {
            int i = this.x + 52;
            int j = this.y + 14;
            int k = this.scrollOffset + 12;

            for (int l = this.scrollOffset; l < k; l++) {
                int m = l - this.scrollOffset;
                double d = mouseX - (double)(i + m % 4 * 16);
                double e = mouseY - (double)(j + m / 4 * 18);
                if (d >= 0.0 && e >= 0.0 && d < 16.0 && e < 18.0) {
                    if (this.client != null) {
                        if (this.handler.onButtonClick(this.client.player, l)) {
                            if (this.client.interactionManager != null) {
                                this.client.interactionManager.clickButton(this.handler.syncId, l);
                                return true;
                            }
                        }
                    }
                }
            }

            i = this.x + 156;
            j = this.y + 9;
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
        if (super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)) {
            return true;
        } else {
            if (this.shouldScroll()) {
                int i = this.getMaxScroll();
                float f = (float)verticalAmount / (float)i;
                this.scrollAmount = MathHelper.clamp(this.scrollAmount - f, 0.0F, 1.0F);
                this.scrollOffset = (int)((double)(this.scrollAmount * (float)i) + 0.5) * 4;
            }

            return true;
        }
    }

    public void doTick() {
        ItemStack itemStack = this.handler.getSlot(0).getStack();

        if (!this.handler.inputSlot.getStack().isEmpty()) {
            this.itemInEnchantSlot = true;

            RegistryEntry<Item> entry = Registries.ITEM.getEntry(itemStack.getItem());

            if (entry.isIn(ItemTags.SWORDS)) {
                this.itemCategory = "sword";
            }
            else if (entry.isIn(ItemTags.AXES)) {
                this.itemCategory = "axe";
            }
            else if (entry.isIn(ItemTags.PICKAXES) || entry.isIn(ItemTags.SHOVELS) || entry.isIn(ItemTags.HOES)) {
                this.itemCategory = "tool";
            }
            else if (entry.isIn(ItemTags.BOW_ENCHANTABLE)) {
                this.itemCategory = "bow";
            }
            else if (entry.isIn(ItemTags.FISHING_ENCHANTABLE)) {
                this.itemCategory = "fishing_rod";
            }
            else if (entry.isIn(ItemTags.TRIDENT_ENCHANTABLE)) {
                this.itemCategory = "trident";
            }
            else if (entry.isIn(ItemTags.MACE_ENCHANTABLE)) {
                this.itemCategory = "mace";
            }
            else if (entry.isIn(ItemTags.CROSSBOW_ENCHANTABLE)) {
                this.itemCategory = "crossbow";
            }
        }
        else {
            this.itemInEnchantSlot = false;
            this.itemCategory = "";
        }


        if (!ItemStack.areEqual(itemStack, this.stack)) {
            this.stack = itemStack;

            do {
                this.approximatePageAngle += (float)(this.random.nextInt(4) - this.random.nextInt(4));
            } while(this.nextPageAngle <= this.approximatePageAngle + 1.0F && this.nextPageAngle >= this.approximatePageAngle - 1.0F);
        }

        this.pageAngle = this.nextPageAngle;
        this.pageTurningSpeed = this.nextPageTurningSpeed;
        boolean bl = false;

        for(int i = 0; i < 3; ++i) {
            if (this.handler.enchantmentPower[i] != 0) {
                bl = true;
                break;
            }
        }

        if (bl) {
            this.nextPageTurningSpeed += 0.2F;
        } else {
            this.nextPageTurningSpeed -= 0.2F;
        }

        this.nextPageTurningSpeed = MathHelper.clamp(this.nextPageTurningSpeed, 0.0F, 1.0F);
        float f = (this.approximatePageAngle - this.nextPageAngle) * 0.4F;
        float g = 0.2F;
        f = MathHelper.clamp(f, -0.2F, 0.2F);
        this.pageRotationSpeed += (f - this.pageRotationSpeed) * 0.9F;
        this.nextPageAngle += this.pageRotationSpeed;
    }

    private boolean shouldScroll() {
        return this.itemInEnchantSlot && ENCHANTMENT_ICON_TEXTURES.length > 16;
    }

    protected int getMaxScroll() {
        return (ENCHANTMENT_ICON_TEXTURES.length + 4 - 1) / 4 - 3;
    }
}
