package sircow.preservedinferno.other;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class NoLootingPlayerWrapper extends Player {
    private final Player real;

    public NoLootingPlayerWrapper(Player real) {
        super(real.level(), real.getGameProfile());
        this.real = real;
        this.setPos(real.getX(), real.getY(), real.getZ());
    }

    @Override
    public @NotNull ItemStack getMainHandItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack getItemInHand(@NotNull InteractionHand hand) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull UUID getUUID() {
        return real.getUUID();
    }

    @Override
    public @NotNull Component getName() {
        return Component.literal(String.valueOf(this.real.getName()));
    }

    @Override
    public GameType gameMode() {
        return real.gameMode();
    }

    @Override
    public boolean isCreative() {
        return real.isCreative();
    }

    @Override
    public void tick() {
        // do nothing
    }

    @Override
    public boolean isSpectator() {
        return real.isSpectator();
    }
}
