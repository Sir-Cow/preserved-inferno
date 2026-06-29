package sircow.preservedinferno.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Inject(method = "loadLevel", at = @At("TAIL"))
    private void pinferno$setNaturalRegenFalseIfHardcore(CallbackInfo ci) {
        MinecraftServer server = (MinecraftServer) (Object) this;
        ServerLevel overworld = server.overworld();

        if (overworld != null && server.getWorldData().isHardcore()) {
            overworld.getGameRules().set(GameRules.NATURAL_HEALTH_REGENERATION,false, server);
        }
    }
}
