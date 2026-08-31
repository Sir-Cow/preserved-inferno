package sircow.preservedinferno.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedinferno.component.RodComponentResolver;
import sircow.preservedinferno.component.RodTooltipComponent;
import sircow.preservedinferno.enchantment.ModEnchantments;

import java.util.Optional;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "getTooltipImage", at = @At("HEAD"), cancellable = true)
    private void pinferno$fishingRodTooltip(ItemStack itemStack, CallbackInfoReturnable<Optional<TooltipComponent>> cir) {
        if (!(itemStack.getItem() instanceof FishingRodItem)) return;

        ItemStack hook = RodComponentResolver.resolveHook(itemStack);
        ItemStack line = RodComponentResolver.resolveLine(itemStack);
        ItemStack sinker = RodComponentResolver.resolveSinker(itemStack);

        cir.setReturnValue(Optional.of(new RodTooltipComponent(itemStack, hook, line, sinker)));
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void pInferno$blusteringMaceEnchant(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack stack = player.getItemInHand(hand);

        if (!(stack.getItem() instanceof MaceItem)) return;

        Holder<Enchantment> blustering = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ModEnchantments.BLUSTERING);

        if (EnchantmentHelper.getItemEnchantmentLevel(blustering, stack) <= 0) return;

        if (!level.isClientSide()) {
            Vec3 look = player.getLookAngle();
            WindCharge windCharge = new WindCharge(player, level, player.getX(), player.getEyeY() - 0.15, player.getZ());

            windCharge.shoot(look.x(), look.y(), look.z(), 1.5F, 1.0F);
            level.addFreshEntity(windCharge);
            stack.hurtAndBreak(1, player, hand);
        }

        player.getCooldowns().addCooldown(stack, 60);
        player.swing(hand);
        cir.setReturnValue(InteractionResult.SUCCESS);
    }
}
