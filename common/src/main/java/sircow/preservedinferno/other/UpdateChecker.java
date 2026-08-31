package sircow.preservedinferno.other;

import com.google.gson.*;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.platform.Services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
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
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://api.modrinth.com/v2/project/" + PROJECT_ID + "/version"))
                        .header("User-Agent", "pinferno-update-checker")
                        .GET()
                        .build();

                HttpResponse<String> response;

                try (HttpClient client = HttpClient.newHttpClient()) {
                    response = client.send(request, HttpResponse.BodyHandlers.ofString());
                }

                JsonArray versions = JsonParser.parseString(response.body()).getAsJsonArray();
                String bestVer = null;
                int[] bestMc = null;

                for (JsonElement element : versions) {
                    JsonObject obj = element.getAsJsonObject();
                    JsonArray loaders = obj.getAsJsonArray("loaders");

                    if (!loaders.contains(new JsonPrimitive(LOADER))) continue;

                    String ver = obj.get("version_number").getAsString();
                    SemVer parsed = SemVer.parse(ver);

                    if (parsed == null) continue;

                    JsonArray gameVersions = obj.getAsJsonArray("game_versions");
                    int[] mc = parseMcVersion(gameVersions);

                    if (mc == null) continue;

                    if (bestVer == null || compareIntArrays(mc, bestMc) > 0 || (compareIntArrays(mc, bestMc) == 0 && parsed.compareTo(Objects.requireNonNull(SemVer.parse(bestVer))) > 0)) {
                        bestVer = ver;
                        bestMc = mc;
                    }
                }
                latestVersion = bestVer;
            }
            catch (Exception ignored) {}

            if (callback != null) callback.run();
        });
    }

    private static int[] parseMcVersion(JsonArray gameVersions) {
        int[] best = null;

        for (JsonElement gv : gameVersions) {
            String v = gv.getAsString();
            int[] parsed = parseVersionString(v);
            if (parsed != null && (best == null || compareIntArrays(parsed, best) > 0)) {
                best = parsed;
            }
        }
        return best;
    }

    private static int[] parseVersionString(String v) {
        String[] parts = v.split("\\.");
        int[] result = new int[parts.length];

        try {
            for (int i = 0; i < parts.length; i++) {
                result[i] = Integer.parseInt(parts[i]);
            }
        }
        catch (NumberFormatException e) {
            return null;
        }
        return result;
    }

    private static int compareIntArrays(int[] a, int[] b) {
        int len = Math.min(a.length, b.length);

        for (int i = 0; i < len; i++) {
            if (a[i] != b[i]) return Integer.compare(a[i], b[i]);
        }
        return Integer.compare(a.length, b.length);
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
