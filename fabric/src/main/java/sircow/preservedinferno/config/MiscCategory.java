package sircow.preservedinferno.config;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorDropdown;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;

public class MiscCategory {
    public enum TimeFormat {
        TWENTY_FOUR_HOUR("International"),
        TWELVE_HOUR("Traditional (EU)"),
        TWELVE_HOUR_ALT("Traditional (US)");

        private final String label;

        TimeFormat(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    public enum UnitSystem {
        METRIC("Metric"),
        IMPERIAL("Imperial");

        private final String label;

        UnitSystem(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    @Expose
    @ConfigOption(name = "Clock Time Format", desc = "Time format when holding a clock\nInternational: 23:59\nTraditional (US): 11:59 p.m.\nTraditional (EU): 11:59 PM")
    @ConfigEditorDropdown
    public TimeFormat timeFormat = TimeFormat.TWENTY_FOUR_HOUR;

    @Expose
    @ConfigOption(name = "Distance Units", desc = "Distance format when holding a compass\nMetric: m/km\nImperial: ft/miles")
    @ConfigEditorDropdown
    public UnitSystem unitSystem = UnitSystem.METRIC;
}
