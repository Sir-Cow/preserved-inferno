package sircow.preservedinferno.other;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.player.Player;

public abstract class ModEntityData {
    public static final EntityDataAccessor<Float> PLAYER_SHIELD_STAMINA = SynchedEntityData.defineId(Player.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Integer> PLAYER_HEAT = SynchedEntityData.defineId(Player.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> PLAYER_CAN_DO_HEAT_CHANGE = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Long> PLAYER_HARDCORE_REGEN_COOLDOWN = SynchedEntityData.defineId(Player.class, EntityDataSerializers.LONG);
    public static final EntityDataAccessor<Boolean> PLAYER_HUNGER_INITIALIZED = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> RESET_HARDCORE_HEALTH = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Integer> PLAYER_TRADED_PROFESSIONS = SynchedEntityData.defineId(Player.class, EntityDataSerializers.INT);

    public static void registerModEntityData() {
        // Constants.LOG.info("Registering Mod Entity Data for " + Constants.MOD_ID);
    }
}
