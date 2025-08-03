package sircow.preservedinferno.other;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class ExperimentsUtil {
    private static List<String> globalFeatures = ImmutableList.of("vanilla");

    public static void addGlobalFeature(String id) {
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        for (String s : globalFeatures) builder.add(s);
        builder.add(id);
        globalFeatures = builder.build();
    }

    public static List<String> getGlobalFeatures() {
        return globalFeatures;
    }
}
