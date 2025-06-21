package sircow.preservedinferno.mixin;

import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Set;

@Mixin(ServerAdvancementManager.class)
public class ServerAdvancementManagerMixin {
//    @Inject(method = "apply*", at = @At("HEAD"))
//    private void onApply(Map<ResourceLocation, Advancement> advancementsIn, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo ci) {
//        Set<ResourceLocation> blacklist = Set.of(ResourceLocation.withDefaultNamespace("story/root"));
//        advancementsIn.keySet().removeIf(blacklist::contains);
//    }
}
