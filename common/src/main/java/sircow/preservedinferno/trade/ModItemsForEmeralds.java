package sircow.preservedinferno.trade;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.villager.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public class ModItemsForEmeralds implements VillagerTrades.ItemListing {
    private final ItemStack itemStack;
    private final int emeraldCost, maxUses, villagerXp;
    private final float priceMultiplier;
    private final Optional<ResourceKey<EnchantmentProvider>> enchantmentProvider;

    public ModItemsForEmeralds(Block block, int emeraldCost, int numberOfItems, int maxUses, int villagerXp) {
        this(new ItemStack(block), emeraldCost, numberOfItems, maxUses, villagerXp);
    }

    public ModItemsForEmeralds(Item item, int emeraldCost, int numberOfItems, int villagerXp) {
        this(new ItemStack(item), emeraldCost, numberOfItems, 12, villagerXp);
    }

    public ModItemsForEmeralds(Item item, int emeraldCost, int numberOfItems, int maxUses, int villagerXp) {
        this(new ItemStack(item), emeraldCost, numberOfItems, maxUses, villagerXp);
    }

    public ModItemsForEmeralds(ItemStack itemStack, int emeraldCost, int numberOfItems, int maxUses, int villagerXp) {
        this(itemStack, emeraldCost, numberOfItems, maxUses, villagerXp, 0.05F);
    }

    public ModItemsForEmeralds(Item item, int emeraldCost, int numberOfItems, int maxUses, int villagerXp, float priceMultiplier) {
        this(new ItemStack(item), emeraldCost, numberOfItems, maxUses, villagerXp, priceMultiplier);
    }

    public ModItemsForEmeralds(
            Item item, int emeraldCost, int numberOfItems, int maxUses, int villagerXp, float priceMultiplier, ResourceKey<EnchantmentProvider> enchantmentProvider
    ) {
        this(new ItemStack(item), emeraldCost, numberOfItems, maxUses, villagerXp, priceMultiplier, Optional.of(enchantmentProvider));
    }

    public ModItemsForEmeralds(ItemStack itemStack, int emeraldCost, int numberOfItems, int maxUses, int villagerXp, float priceMultiplier) {
        this(itemStack, emeraldCost, numberOfItems, maxUses, villagerXp, priceMultiplier, Optional.empty());
    }

    public ModItemsForEmeralds(
            ItemStack itemStack,
            int emeraldCost,
            int numberOfItems,
            int maxUses,
            int villagerXp,
            float priceMultiplier,
            Optional<ResourceKey<EnchantmentProvider>> enchantmentProvider
    ) {
        this.itemStack = itemStack;
        this.emeraldCost = emeraldCost;
        this.itemStack.setCount(numberOfItems);
        this.maxUses = maxUses;
        this.villagerXp = villagerXp;
        this.priceMultiplier = priceMultiplier;
        this.enchantmentProvider = enchantmentProvider;
    }

    @Override
    public MerchantOffer getOffer(@NonNull ServerLevel serverLevel, Entity trader, @NotNull RandomSource random) {
        ItemStack itemStack = this.itemStack.copy();
        ServerLevel srv = (ServerLevel) trader.level();

        this.enchantmentProvider.ifPresent(resourceKey ->
                EnchantmentHelper.enchantItemFromProvider(
                        itemStack,
                        srv.registryAccess(),
                        resourceKey,
                        srv.getCurrentDifficultyAt(trader.blockPosition()),
                        random
                )
        );

        return new MerchantOffer(
                new ItemCost(Items.EMERALD, this.emeraldCost),
                itemStack,
                this.maxUses,
                this.villagerXp,
                this.priceMultiplier
        );
    }
}
