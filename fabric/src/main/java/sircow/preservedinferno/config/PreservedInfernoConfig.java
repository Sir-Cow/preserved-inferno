package sircow.preservedinferno.config;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.Config;
import io.github.notenoughupdates.moulconfig.Social;
import io.github.notenoughupdates.moulconfig.annotations.Category;
import io.github.notenoughupdates.moulconfig.common.MyResourceLocation;
import io.github.notenoughupdates.moulconfig.common.text.StructuredText;
import sircow.preservedinferno.Constants;

import java.util.List;

public class PreservedInfernoConfig extends Config {
    private final MyResourceLocation github = new MyResourceLocation(Constants.MOD_ID, "social/github.png");
    private final MyResourceLocation twitter = new MyResourceLocation(Constants.MOD_ID, "social/twitter.png");

    @Override
    public StructuredText getTitle() {
        return StructuredText.of("Preserved: Inferno");
    }

    @Override
    public List<Social> getSocials() {
        return List.of(
                Social.forLink(StructuredText.of("GitHub"), github, "https://github.com/Sir-Cow/preserved-inferno"),
                Social.forLink(StructuredText.of("Twitter"), twitter, "https://twitter.com/PreservedMC")
        );
    }

    @Expose
    @Category(name = "Misc", desc = "")
    public MiscCategory miscCategory = new MiscCategory();
}
