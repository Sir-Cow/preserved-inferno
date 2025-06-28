package sircow.preservedinferno.mixin;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.trigger.ModTriggers;

@Mixin(ThrownTrident.class)
public class ThrownTridentMixin {
    // modify thrown trident damage
    @ModifyVariable(method = "onHitEntity", at = @At("STORE"), ordinal = 0)
    private float preserved_inferno$modifyDamage(float originalValue) {
        return 10.0F;
    }
    // trigger channeling advancement
    @Inject(method = "onHitEntity", at = @At("TAIL"))
    private void onTridentHit(EntityHitResult result, CallbackInfo ci) {
        ThrownTrident self = (ThrownTrident) (Object) this;
        Level level = self.level();
        if (level.isClientSide || !(level instanceof ServerLevel serverLevel)) return;

        Entity owner = self.getOwner();
        if (!(owner instanceof ServerPlayer player)) return;

        ItemStack weapon = self.getPickupItemStackOrigin();
        Holder<Enchantment> channelling = serverLevel.registryAccess().lookupOrThrow(Enchantments.CHANNELING.registryKey()).getOrThrow(Enchantments.CHANNELING);

        if (EnchantmentHelper.getItemEnchantmentLevel(channelling, weapon) <= 0) return;

        Entity target = result.getEntity();
        if (!(target instanceof LivingEntity living)) return;
        if (!serverLevel.isThundering()) return;
        if (!serverLevel.canSeeSky(living.blockPosition())) return;

        ModTriggers.CHANNELING.trigger(player);
    }
}
