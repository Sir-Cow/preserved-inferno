package sircow.preservedinferno.other;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import sircow.preservedinferno.Constants;

import java.util.*;

public class ModWorldData extends SavedData {
    private static final Codec<UUID> UUID_CODEC = Codec.STRING.xmap(UUID::fromString, UUID::toString);
    private static final Codec<Set<Identifier>> IDENTIFIER_SET_CODEC = Codec.list(Identifier.CODEC).xmap(HashSet::new, List::copyOf);
    public static final Codec<ModWorldData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.unboundedMap(UUID_CODEC, Codec.INT).fieldOf("playerPoints").forGetter(d -> d.playerPoints),
                    Codec.unboundedMap(UUID_CODEC, IDENTIFIER_SET_CODEC).fieldOf("playerAwardedAdvancements").forGetter(d -> d.playerAwardedAdvancements),
                    Codec.unboundedMap(UUID_CODEC, Codec.STRING).fieldOf("playerRanks").forGetter(d -> d.playerRanks)
            ).apply(instance, ModWorldData::new));
    public final Map<UUID, Integer> playerPoints;
    public final Map<UUID, Set<Identifier>> playerAwardedAdvancements;
    public final Map<UUID, String> playerRanks;

    public ModWorldData() {
        this(new HashMap<>(), new HashMap<>(), new HashMap<>());
    }

    private ModWorldData(Map<UUID, Integer> playerPoints, Map<UUID, Set<Identifier>> playerAwardedAdvancements, Map<UUID, String> playerRanks) {
        this.playerPoints = new HashMap<>(playerPoints);
        this.playerAwardedAdvancements = new HashMap<>();
        playerAwardedAdvancements.forEach((uuid, set) -> this.playerAwardedAdvancements.put(uuid, new HashSet<>(set)));
        this.playerRanks = new HashMap<>(playerRanks);
    }

    public static ModWorldData get(MinecraftServer server) {
        return server.getDataStorage().computeIfAbsent(TYPE);
    }

    public static final SavedDataType<ModWorldData> TYPE = new SavedDataType<>(Constants.id( "world_data"), ModWorldData::new, CODEC, null);
}
