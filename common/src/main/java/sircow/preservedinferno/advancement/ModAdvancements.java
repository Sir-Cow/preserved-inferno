package sircow.preservedinferno.advancement;

import net.minecraft.resources.Identifier;
import sircow.preservedinferno.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ModAdvancements {
    private static final Map<UUID, Integer> clientPlayerPoints = new HashMap<>();
    public static final List<Identifier> EXCLUDED_ADVANCEMENTS = List.of(
            Constants.id("mastery/root"),
            Constants.id("mastery/beginner"),
            Constants.id("mastery/centurion"),
            Constants.id("mastery/champion"),
            Constants.id("mastery/disciple"),
            Constants.id("mastery/infernal"),
            Constants.id("mastery/knight"),
            Constants.id("mastery/master"),
            Constants.id("mastery/novice"),
            Constants.id("mastery/squire"),
            Constants.id("mastery/starter"),
            Constants.id("adventure/root"),
            Constants.id("agriculture/root"),
            Constants.id("exploration/root"),
            Constants.id("fishing/root"),
            Constants.id("nether/root"),
            Identifier.withDefaultNamespace("end/root"),
            Identifier.withDefaultNamespace("story/root")
    );

    public static void setPlayerPoints(UUID playerUUID, int points) {
        clientPlayerPoints.put(playerUUID, points);
    }

    public static int getPlayerPoints(UUID playerUUID) {
        return clientPlayerPoints.getOrDefault(playerUUID, 0);
    }

    private ModAdvancements() {}
}
