package sircow.preservedinferno.other;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.player.Player;
import sircow.preservedinferno.Constants;

public abstract class ModEntityData {
    public static final EntityDataAccessor<Float> PLAYER_SHIELD_STAMINA =
            SynchedEntityData.defineId(Player.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Integer> PLAYER_HEAT =
            SynchedEntityData.defineId(Player.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> PLAYER_CAN_DO_HEAT_CHANGE =
            SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);

    public static void registerModEntityData() {
        Constants.LOG.info("Registering Mod Entity Data for " + Constants.MOD_ID);
    }
}