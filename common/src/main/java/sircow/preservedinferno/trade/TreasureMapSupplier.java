package sircow.preservedinferno.trade;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import java.util.Optional;
import java.util.function.Function;

public class TreasureMapSupplier implements Function<Villager, MerchantOffer> {
    private final int emeraldCost;
    private final TagKey<Structure> destination;
    private final String displayNameKey;
    private final Holder<MapDecorationType> decoration;
    private final int maxUses;
    private final int villagerXp;

    public TreasureMapSupplier(
            int emeraldCost,
            TagKey<Structure> destination,
            String displayNameKey,
            Holder<MapDecorationType> decoration,
            int maxUses,
            int villagerXp
    ) {
        this.emeraldCost = emeraldCost;
        this.destination = destination;
        this.displayNameKey = displayNameKey;
        this.decoration = decoration;
        this.maxUses = maxUses;
        this.villagerXp = villagerXp;
    }

    @Override
    public MerchantOffer apply(Villager villager) {
        if (!(villager.level() instanceof ServerLevel serverLevel)) return null;

        BlockPos pos = serverLevel.findNearestMapStructure(destination, villager.blockPosition(), 100, true);
        if (pos == null) return null;

        ItemStack map = MapItem.create(serverLevel, pos.getX(), pos.getZ(), (byte) 2, true, true);
        MapItem.renderBiomePreviewMap(serverLevel, map);
        MapItemSavedData.addTargetDecoration(map, pos, "+", decoration);
        map.set(DataComponents.ITEM_NAME, Component.translatable(displayNameKey));

        return new MerchantOffer(
                new ItemCost(Items.EMERALD, emeraldCost),
                Optional.of(new ItemCost(Items.COMPASS)),
                map,
                maxUses,
                villagerXp,
                0.2F
        );
    }
}