package sircow.preservedinferno.other;

import net.minecraft.client.Minecraft;

public interface IMinecraftMixin {
    void startWaitForAdvancement(Minecraft client, int attempts);
}
