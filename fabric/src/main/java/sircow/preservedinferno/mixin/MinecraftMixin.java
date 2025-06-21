package sircow.preservedinferno.mixin;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.client.FabricPreservedInfernoClient;
import sircow.preservedinferno.other.IMinecraftMixin;
import sircow.preservedinferno.other.OpenAdvancementPayload;

import java.util.Map;

@Mixin(Minecraft.class)
public class MinecraftMixin implements IMinecraftMixin {
    @Override
    public void startWaitForAdvancement(Minecraft client, int attempts) {
        waitForAdvancementExists(client, attempts);
    }

    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$interceptAdvancementsScreen(Screen screen, CallbackInfo ci) {
        if (screen instanceof AdvancementsScreen) {
            if (FabricPreservedInfernoClient.suppressNextOpen) {
                FabricPreservedInfernoClient.suppressNextOpen = false;
                return;
            }

            if (!FabricPreservedInfernoClient.hasTriggeredOnce) {
                ci.cancel();
                FabricPreservedInfernoClient.hasTriggeredOnce = true;
                FabricPreservedInfernoClient.waitingForAdvancement = true;
                FabricPreservedInfernoClient.advancementDelayTicks = 20;

                ClientPlayNetworking.send(new OpenAdvancementPayload());
            }
            else if (FabricPreservedInfernoClient.waitingForAdvancement) {
                ci.cancel();
            }
            else {
                // Let it open normally
            }
        }
    }

    @Unique
    private void waitForAdvancementExists(Minecraft client, int attempts) {
        if (attempts > 20) {
            FabricPreservedInfernoClient.waitingForAdvancement = false;
            return;
        }

        if (client.player != null) {
            ClientAdvancements advancements = client.player.connection.getAdvancements();
            AdvancementHolder holder = advancements.get(ResourceLocation.withDefaultNamespace("story/root"));

            if (holder != null) {
                FabricPreservedInfernoClient.waitingForAdvancement = false;
                FabricPreservedInfernoClient.suppressNextOpen = true;
                client.setScreen(new AdvancementsScreen(advancements));

                client.execute(() -> waitForAdvancementGranted(client, 0));
            }
            else {
                client.execute(() -> waitForAdvancementExists(client, attempts + 1));
            }
        }
    }

    @Unique
    private void waitForAdvancementGranted(Minecraft client, int attempts) {
        if (attempts > 20) {
            return;
        }
        if (client.player != null) {
            Map<AdvancementHolder, AdvancementProgress> progressMap = ((ClientAdvancementsAccessor) client.player.connection.getAdvancements()).getProgress();
            boolean isGranted = progressMap.entrySet().stream().anyMatch(entry -> entry.getKey().id().toString().equals("minecraft:story/root") && entry.getValue().isDone());

            if (isGranted) {
                FabricPreservedInfernoClient.suppressNextOpen = true;
                FabricPreservedInfernoClient.advancementGranted = true;
                client.setScreen(new AdvancementsScreen(client.player.connection.getAdvancements()));
            }
            else {
                client.execute(() -> waitForAdvancementGranted(client, attempts + 1));
            }
        }
    }
}
