package sircow.preservedinferno.trade;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

public class ModEmeraldsForItems implements VillagerTrades.ItemListing {
    private final ItemCost itemStack;
    private final int maxUses;
    private final int villagerXp;
    private final int emeraldAmount;
    private final float priceMultiplier;

    public ModEmeraldsForItems(ItemLike item, int cost, int maxUses, int villagerXp) {
        this(item, cost, maxUses, villagerXp, 1);
    }

    public ModEmeraldsForItems(ItemLike item, int cost, int maxUses, int villagerXp, int emeraldAmount) {
        this(new ItemCost(item.asItem(), cost), maxUses, villagerXp, emeraldAmount);
    }

    public ModEmeraldsForItems(ItemCost itemStack, int maxUses, int villagerXp, int emeraldAmount) {
        this.itemStack = itemStack;
        this.maxUses = maxUses;
        this.villagerXp = villagerXp;
        this.emeraldAmount = emeraldAmount;
        this.priceMultiplier = 0.05F;
    }

    @Override
    public MerchantOffer getOffer(@NotNull Entity trader, @NotNull RandomSource random) {
        return new MerchantOffer(this.itemStack, new ItemStack(Items.EMERALD, this.emeraldAmount), this.maxUses, this.villagerXp, this.priceMultiplier);
    }
}
