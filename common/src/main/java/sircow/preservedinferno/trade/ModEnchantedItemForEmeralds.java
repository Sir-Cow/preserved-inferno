package sircow.preservedinferno.trade;

import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.villager.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public class ModEnchantedItemForEmeralds implements VillagerTrades.ItemListing {
    private final ItemStack itemStack;
    private final int baseEmeraldCost, maxUses, villagerXp;
    private final float priceMultiplier;

    public ModEnchantedItemForEmeralds(Item item, int baseEmeraldCost, int maxUses, int villagerXp) {
        this(item, baseEmeraldCost, maxUses, villagerXp, 0.05F);
    }

    public ModEnchantedItemForEmeralds(Item item, int baseEmeraldCost, int maxUses, int villagerXp, float priceMultiplier) {
        this.itemStack = new ItemStack(item);
        this.baseEmeraldCost = baseEmeraldCost;
        this.maxUses = maxUses;
        this.villagerXp = villagerXp;
        this.priceMultiplier = priceMultiplier;
    }

    @Override
    public MerchantOffer getOffer(@NonNull ServerLevel serverLevel, Entity trader, RandomSource random) {
        int i = 5 + random.nextInt(15);
        RegistryAccess registryAccess = trader.level().registryAccess();
        Optional<HolderSet.Named<Enchantment>> optional = registryAccess.lookupOrThrow(Registries.ENCHANTMENT).get(EnchantmentTags.ON_TRADED_EQUIPMENT);
        ItemStack itemStack = EnchantmentHelper.enchantItem(random, new ItemStack(this.itemStack.getItem()), i, registryAccess, optional);
        int j = Math.min(this.baseEmeraldCost, 64);
        ItemCost itemCost = new ItemCost(Items.EMERALD, j);
        return new MerchantOffer(itemCost, itemStack, this.maxUses, this.villagerXp, this.priceMultiplier);
    }
}
