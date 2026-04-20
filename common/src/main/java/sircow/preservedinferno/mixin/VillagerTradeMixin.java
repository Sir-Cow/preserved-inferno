package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(VillagerTrade.class)
public class VillagerTradeMixin {
    @Redirect(method = "getOffer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/loot/providers/number/NumberProvider;getInt(Lnet/minecraft/world/level/storage/loot/LootContext;)I"))
    private int preserved_inferno$modifyTradeXp(NumberProvider provider, LootContext context) {
        int originalXp = provider.getInt(context);

        Object entity = context.getParameter(LootContextParams.THIS_ENTITY);
        if (!(entity instanceof Villager villager)) {
            return originalXp;
        }

        return switch (villager.getVillagerData().level()) {
            case 1 -> 2;
            case 2 -> 5;
            case 3 -> 10;
            case 4 -> 20;
            case 5 -> 50;
            default -> originalXp;
        };
    }
}
