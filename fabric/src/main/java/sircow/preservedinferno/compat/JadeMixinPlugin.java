package sircow.preservedinferno.compat;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import sircow.preservedinferno.platform.Services;

import java.util.List;
import java.util.Set;

public class JadeMixinPlugin implements IMixinConfigPlugin {
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.endsWith("BrewingStandProviderClientMixin")
                || mixinClassName.endsWith("CommonProxyMixin")
                || mixinClassName.endsWith("HorseStatsProviderMixin")
                || mixinClassName.endsWith("ItemStorageProviderMixin")
        ) {
            return Services.PLATFORM.isModLoaded("jade");
        }
        return true;
    }

    @Override public void onLoad(String mixinPackage) {}
    @Override public String getRefMapperConfig() { return null; }
    @Override public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}
    @Override public List<String> getMixins() { return null; }
    @Override public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
    @Override public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
