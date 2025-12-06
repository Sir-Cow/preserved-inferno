package sircow.preservedinferno.other;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.storage.DimensionDataStorage;
import sircow.preservedinferno.Constants;

import java.util.*;

public class ModWorldData extends SavedData {
    public final Map<UUID, Integer> playerPoints = new HashMap<>();
    public final Map<UUID, Set<ResourceLocation>> playerAwardedAdvancements = new HashMap<>();
    public final Map<UUID, String> PLAYER_RANKS = new HashMap<>();

    public ModWorldData() {
    }

    public static ModWorldData load(CompoundTag tag, HolderLookup.Provider provider) {
        ModWorldData data = new ModWorldData();

        CompoundTag playerPointsTag = tag.getCompound("playerPoints").get();
        for (String key : playerPointsTag.keySet()) {
            UUID uuid = UUID.fromString(key);
            data.playerPoints.put(uuid, playerPointsTag.getInt(key).get());
        }

        CompoundTag awardedAdvancementsTag = tag.getCompound("playerAwardedAdvancements").get();
        for (String uuidString : awardedAdvancementsTag.keySet()) {
            UUID uuid = UUID.fromString(uuidString);
            ListTag advListTag = awardedAdvancementsTag.getList(uuidString).get();
            Set<ResourceLocation> advancements = new HashSet<>();
            for (int i = 0; i < advListTag.size(); i++) {
                advancements.add(ResourceLocation.parse(advListTag.getString(i).get()));
            }
            data.playerAwardedAdvancements.put(uuid, advancements);
        }

        CompoundTag playerRanksTag = tag.getCompound("playerRanks").get();
        for (String uuidString : playerRanksTag.keySet()) {
            UUID uuid = UUID.fromString(uuidString);
            data.PLAYER_RANKS.put(uuid, playerRanksTag.getString(uuidString).get());
        }

        return data;
    }

    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {

        CompoundTag playerPointsTag = new CompoundTag();
        for (Map.Entry<UUID, Integer> entry : this.playerPoints.entrySet()) {
            playerPointsTag.putInt(entry.getKey().toString(), entry.getValue());
        }
        tag.put("playerPoints", playerPointsTag);

        CompoundTag awardedAdvancementsTag = new CompoundTag();
        for (Map.Entry<UUID, Set<ResourceLocation>> entry : this.playerAwardedAdvancements.entrySet()) {
            ListTag advListTag = new ListTag();
            for (ResourceLocation adv : entry.getValue()) {
                advListTag.add(StringTag.valueOf(adv.toString()));
            }
            awardedAdvancementsTag.put(entry.getKey().toString(), advListTag);
        }
        tag.put("playerAwardedAdvancements", awardedAdvancementsTag);

        CompoundTag playerRanksTag = new CompoundTag();
        for (Map.Entry<UUID, String> entry : this.PLAYER_RANKS.entrySet()) {
            playerRanksTag.putString(entry.getKey().toString(), entry.getValue());
        }
        tag.put("playerRanks", playerRanksTag);

        return tag;
    }

    public static ModWorldData get(MinecraftServer server) {
        DimensionDataStorage dataStorage = Objects.requireNonNull(server.getLevel(ServerLevel.OVERWORLD)).getDataStorage();
        return dataStorage.computeIfAbsent(TYPE);
    }

    private static Codec<ModWorldData> createWorldDataCodec(Context context) {
        return new Codec<>() {
            @Override
            public <U> DataResult<Pair<ModWorldData, U>> decode(DynamicOps<U> ops, U input) {
                return CompoundTag.CODEC.decode(ops, input)
                        .flatMap(pair -> {
                            CompoundTag tag = pair.getFirst();
                            ServerLevel level = context.level();
                            if (level == null) {
                                return DataResult.error(() -> "ServerLevel is null in SavedData.Context. Cannot get HolderLookup.Provider for decoding.");
                            }
                            HolderLookup.Provider provider = level.registryAccess();
                            ModWorldData loadedData = ModWorldData.load(tag, provider);
                            return DataResult.success(Pair.of(loadedData, pair.getSecond()));
                        });
            }

            @Override
            public <U> DataResult<U> encode(ModWorldData input, DynamicOps<U> ops, U prefix) {
                ServerLevel level = context.level();
                if (level == null) {
                    return DataResult.error(() -> "ServerLevel is null in SavedData.Context. Cannot get HolderLookup.Provider for encoding.");
                }
                HolderLookup.Provider provider = level.registryAccess();
                CompoundTag tagToSave = new CompoundTag();
                CompoundTag resultTag = input.save(tagToSave, provider);
                return CompoundTag.CODEC.encode(resultTag, ops, prefix);
            }
        };
    }

    public static final SavedDataType<ModWorldData> TYPE = new SavedDataType<>(
            Constants.MOD_ID + "_world_data",
            context -> new ModWorldData(),
            ModWorldData::createWorldDataCodec,
            DataFixTypes.SAVED_DATA_RANDOM_SEQUENCES
    );
}
