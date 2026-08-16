package sircow.preservedinferno.mixin;

import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractHorse.class)
public interface AbstractHorseAccessor {
    @Invoker("doPlayerRide")
    void pinferno$callDoPlayerRide(Player player);
}
