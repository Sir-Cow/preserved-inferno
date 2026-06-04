package sircow.preservedinferno.other;

import com.google.gson.*;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.platform.Services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public final class UpdateChecker {
    private static final String PROJECT_ID = "preserved-inferno";
    private static final String LOADER = Services.PLATFORM.getPlatformName().toLowerCase();
    private static volatile String latestVersion;
    private static volatile boolean checked;

    public static void checkAsync(Runnable callback) {
        if (checked) return;
        checked = true;

        CompletableFuture.runAsync(() -> {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://api.modrinth.com/v2/project/" + PROJECT_ID + "/version"))
                        .header("User-Agent", "pinferno-update-checker")
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JsonArray versions = JsonParser.parseString(response.body()).getAsJsonArray();
                String best = null;

                for (JsonElement element : versions) {
                    JsonObject obj = element.getAsJsonObject();

                    JsonArray loaders = obj.getAsJsonArray("loaders");
                    if (!loaders.contains(new JsonPrimitive(LOADER))) continue;

                    String ver = obj.get("version_number").getAsString();
                    if (best == null) best = ver;
                }

                latestVersion = best;
            }
            catch (Exception ignored) {}

            if (callback != null) callback.run();
        });
    }

    public static boolean hasUpdate() {
        if (latestVersion == null) return false;

        SemVer current = SemVer.parse(Constants.INSTANCE.getVersion());
        SemVer latest = SemVer.parse(latestVersion);

        if (current == null || latest == null) return false;
        return latest.compareTo(current) > 0;
    }

    public static String getLatest() {
        return latestVersion;
    }
}
