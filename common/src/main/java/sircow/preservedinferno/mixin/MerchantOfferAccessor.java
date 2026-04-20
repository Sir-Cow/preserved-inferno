package sircow.preservedinferno.mixin;

import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MerchantOffer.class)
public interface MerchantOfferAccessor {
    @Accessor("maxUses")
    @Mutable
    void setMaxUses(int maxUses);

    @Accessor("demand")
    void setDemand(int demand);
}
