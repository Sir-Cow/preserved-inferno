package sircow.preservedinferno.mixin;

import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(VillagerTrade.class)
public interface VillagerTradeAccessor {
    @Accessor("maxUses")
    NumberProvider getMaxUses();
}
