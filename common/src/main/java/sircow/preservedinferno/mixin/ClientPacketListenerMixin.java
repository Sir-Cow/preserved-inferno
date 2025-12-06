package sircow.preservedinferno.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Redirect(method = "handleAnimate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleEngine;createTrackingEmitter(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/particles/ParticleOptions;)V"))
    private void preserved_inferno$blockCritParticlesIfNoWeapon(ParticleEngine engine, Entity target, ParticleOptions particle) {
        if (particle == ParticleTypes.CRIT && Minecraft.getInstance().player != null) {
            LocalPlayer player = Minecraft.getInstance().player;

            if (!(target instanceof Player)) {
                if (!player.getMainHandItem().is(ItemTags.SHARP_WEAPON_ENCHANTABLE)
                        && !player.getMainHandItem().is(ItemTags.MACE_ENCHANTABLE)
                        && !player.getMainHandItem().is(ItemTags.TRIDENT_ENCHANTABLE)) {
                    return;
                }
            }
        }

        engine.createTrackingEmitter(target, particle);
    }
}
