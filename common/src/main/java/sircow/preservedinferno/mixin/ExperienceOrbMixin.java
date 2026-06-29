package sircow.preservedinferno.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.item.custom.SculkInfusionItem;

@Mixin(ExperienceOrb.class)
public abstract class ExperienceOrbMixin extends Entity {
    @Shadow private int count;
    @Shadow public abstract int getValue();

    public ExperienceOrbMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "playerTouch", at = @At("HEAD"), cancellable = true)
    private void pinferno$onPlayerTouch(Player player, CallbackInfo ci) {
        if (!(player instanceof ServerPlayer)) return;
        if (player.takeXpDelay != 0) return;

        ItemStack sculkInfusion = findSculkInfusion(player);
        if (sculkInfusion == null) return;

        player.takeXpDelay = 2;
        player.take(this, 1);

        int orbValue = getValue();
        if (orbValue <= 0) return;

        int damage = sculkInfusion.getDamageValue();
        if (damage > 0) {
            int maxRepairable = orbValue * 2;
            int repair = Math.min(maxRepairable, damage);
            sculkInfusion.setDamageValue(damage - repair);
            orbValue -= repair / 2;
        }

        if (orbValue > 0) {
            player.giveExperiencePoints(orbValue);
        }

        this.count = 0;
        this.discard();

        ci.cancel();
    }

    @Unique
    private ItemStack findSculkInfusion(Player player) {
        ItemStack main = player.getMainHandItem();
        ItemStack off = player.getOffhandItem();
        if (main.getItem() instanceof SculkInfusionItem) {
            return main;
        }
        if (off.getItem() instanceof SculkInfusionItem) {
            return off;
        }
        return null;
    }
}
